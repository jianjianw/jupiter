package com.qiein.jupiter.web.repository;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.qiein.jupiter.constant.ClientStatusConst;
import com.qiein.jupiter.constant.CommonConstant;
import com.qiein.jupiter.constant.ReportsConfigConst;
import com.qiein.jupiter.util.DBSplitUtil;
import com.qiein.jupiter.util.NumUtil;
import com.qiein.jupiter.util.StringUtil;
import com.qiein.jupiter.web.entity.vo.AnalyzeVO;
import com.qiein.jupiter.web.entity.vo.ZjsNumVO;
import com.qiein.jupiter.web.entity.vo.ZjsSourceVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class ZjsEntryStaffReportDao {


    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    public HashMap<String, Object> getZjsEntryStaff(AnalyzeVO vo)  {


        HashMap<String, Object> rst = new HashMap<String, Object>();

        // 获取转介绍来源集合,总客资数量
        List<ZjsSourceVO> srcList = getAllStaffList(vo);

        // 获取报表配置
        JSONObject config = getRepertsConfig(vo.getCompanyId());

        //获取提报量
        getNumAllList(vo, srcList);
        // 获取客资量
        getKzNumList(vo, srcList);
        // 获取无效客资量
        getYxNumList(vo, srcList, config);
        // 获取入店客资量
        getRdNumList(vo, srcList);
        // 获取成交客资量,成交营业额和成交均价
        getCjNumList(vo, srcList);
        getRdCjNumList(vo, srcList);
        getzxCjNumList(vo, srcList);
        // 实际收费
        getRealCash(vo, srcList);
        //补款
        getBkCash(vo,srcList);
        // 计算渠道合计
        getSrcTotal(vo, srcList);
        // 计算率和成本
        getRateAndCost(srcList);

        rst.put("analysis", srcList);
        return rst;
    }



    /**
     * 获取所有员工集合
     *
     * @param vo
     * @return
     * @throws
     */
    public List<ZjsSourceVO> getAllStaffList(AnalyzeVO vo){

        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT info.COLLECTORID , det.COLLECTORNAME FROM ");
        sb.append(DBSplitUtil.getInfoTabName(vo.getCompanyId()));
        sb.append(" info LEFT JOIN ");
        sb.append(DBSplitUtil.getDetailTabName(vo.getCompanyId()));
        sb.append(" det ON info.KZID = det.KZID AND info.COMPANYID = det.COMPANYID ");
        sb.append(" WHERE info.ISDEL = 0  AND info.SRCTYPE IN ( 3, 4, 5 ) ");
        sb.append(" AND (( info.CREATETIME >= ? AND info.CREATETIME <= ? ) OR ( info.COMESHOPTIME >= ? AND info.COMESHOPTIME <= ? ) OR ( info.SUCCESSTIME >= ? AND info.SUCCESSTIME <= ? ) )AND info.COMPANYID = ? ");
        LinkedList<Object> fieldList = new LinkedList<>();
        fieldList.add(vo.getStart());
        fieldList.add(vo.getEnd());
        fieldList.add(vo.getStart());
        fieldList.add(vo.getEnd());
        fieldList.add(vo.getStart());
        fieldList.add(vo.getEnd());
        fieldList.add(vo.getCompanyId());
        if (StringUtils.isNotEmpty(vo.getStaffId())) {
            sb.append(" AND INSTR( ? ,CONCAT(',',info.COLLECTORID+'',',')) ");
            fieldList.add(CommonConstant.STR_SEPARATOR + vo.getStaffId() + CommonConstant.STR_SEPARATOR);
        }
        sb.append(" GROUP BY info.COLLECTORID ");
        Object[] objects = fieldList.toArray();
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sb.toString(),objects);
        List<ZjsSourceVO> srcList = new LinkedList<>();
        ZjsSourceVO zjsSourceVO = null;
        for(Map<String, Object> map : list){
            zjsSourceVO = new ZjsSourceVO();
            zjsSourceVO.setStaffId(((Long)map.get("COLLECTORID")).intValue());
            zjsSourceVO.setNickName((String)map.get("COLLECTORNAME"));
            zjsSourceVO.setCompanyId(vo.getCompanyId());
            zjsSourceVO.setZjsNumVO(new ZjsNumVO());
            srcList.add(zjsSourceVO);
        }
        return srcList;
    }


    /**
     * 获取无效指标定义规则
     *
     * @param companyId
     * @return
     */
    private JSONObject getRepertsConfig(int companyId) {
        if (NumUtil.isInValid(companyId)) {
            return null;
        }
        String config = "";
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT rpset.DEFINESET FROM hm_crm_reports_set rpset WHERE rpset.COMPANYID =? ");
        config = jdbcTemplate.queryForObject(sb.toString(), new Object[]{companyId}, String.class);
        if(StringUtils.isEmpty(config)){
            return JSONObject.parseObject(config);
        }
        return JSONObject.parseObject(config);
    }

    /**
     * 实际收费
     * @param vo
     * @param srcList
     * @
     */
    public void getRealCash(AnalyzeVO vo, List<ZjsSourceVO> srcList) {
        StringBuilder sb=new StringBuilder();
        sb.append(" SELECT client.COLLECTORID COLLECTORID,");
        sb.append(" sum(cash.AMOUNT) NUMALL");
        sb.append(" FROM hm_crm_cash_log cash");
        sb.append(" LEFT JOIN hm_crm_client_info client ON client.KZID = cash.KZID");
        sb.append(" AND client.COMPANYID = cash.COMPANYID");
        sb.append(" WHERE cash.PAYMENTTIME BETWEEN ? AND ?");
        sb.append(" AND cash.COMPANYID = ?");
        sb.append(" AND client.SRCTYPE IN (3, 4, 5)");
        sb.append(" GROUP BY client.COLLECTORID");
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sb.toString(), vo.getStart(), vo.getEnd(), vo.getCompanyId());
        List<ZjsNumVO> rstList = new LinkedList<>();
        ZjsNumVO zjsNumVO = null;
        for(Map<String, Object> map : list ){
            zjsNumVO = new ZjsNumVO();
            zjsNumVO.setStaffId((int)map.get("COLLECTORID"));
            zjsNumVO.setRealAmount((Double)map.get("NUMALL"));
            rstList.add(zjsNumVO);
        }

        // 毛客资归类到渠道
        for (ZjsSourceVO sourceVO : srcList) {
            for (ZjsNumVO numAll : rstList) {
                if (sourceVO.getStaffId() == numAll.getStaffId()) {
                    sourceVO.getZjsNumVO().setRealAmount(numAll.getRealAmount());
                    break;
                }
            }
        }
    }


    /**
     * 实际收费
     * @param vo
     * @param srcList
     * @
     */
    public void getBkCash(AnalyzeVO vo, List<ZjsSourceVO> srcList) {
        StringBuilder sb=new StringBuilder();
        sb.append(" SELECT client.COLLECTORID COLLECTORID,");
        sb.append(" sum(cash.AMOUNT) NUMALL");
        sb.append(" FROM hm_crm_cash_log cash");
        sb.append(" LEFT JOIN hm_crm_client_info client ON client.KZID = cash.KZID");
        sb.append(" AND client.COMPANYID = cash.COMPANYID");
        sb.append(" WHERE cash.PAYMENTTIME BETWEEN ? AND ?");
        sb.append(" AND cash.COMPANYID = ?");
        sb.append(" AND client.SRCTYPE IN (3, 4, 5)");
        sb.append(" and cash.typeid=2");
        sb.append(" GROUP BY client.COLLECTORID");

        List<Map<String, Object>> list = jdbcTemplate.queryForList(sb.toString(), vo.getStart(), vo.getEnd(), vo.getCompanyId());
        List<ZjsNumVO> rstList = new LinkedList<>();
        ZjsNumVO zjsNumVO = null;
        for(Map<String, Object> map : list ){
            zjsNumVO = new ZjsNumVO();
            zjsNumVO.setStaffId((int)map.get("COLLECTORID"));
            zjsNumVO.setBkAmount((Double)map.get("NUMALL"));
            rstList.add(zjsNumVO);
        }


        // 毛客资归类到渠道
        for (ZjsSourceVO sourceVO : srcList) {
            for (ZjsNumVO numAll : rstList) {
                if (sourceVO.getStaffId() == numAll.getStaffId()) {
                    sourceVO.getZjsNumVO().setBkAmount(numAll.getBkAmount());
                    break;
                }
            }
        }
    }
    /**
     * 统计提报量
     *
     * @param vo
     * @param srcList
     * @return
     * @
     */
    public void getNumAllList(AnalyzeVO vo, List<ZjsSourceVO> srcList)  {

        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT info.COLLECTORID , COUNT( 1 ) NUMALL FROM ");
        sb.append(DBSplitUtil.getInfoTabName(vo.getCompanyId()));
        sb.append(" info WHERE info.ISDEL = 0  AND info.SRCTYPE IN ( 3, 4, 5 ) ");
        sb.append(" AND info.CREATETIME >= ? AND info.CREATETIME <= ?  AND info.COMPANYID = ? ");
        LinkedList<Object> fieldList = new LinkedList<>();
        fieldList.add(vo.getStart());
        fieldList.add(vo.getEnd());
        fieldList.add(vo.getCompanyId());

        if (StringUtils.isNotEmpty(vo.getStaffId())) {
            sb.append(" AND INSTR( ? ,CONCAT(',',info.COLLECTORID+'',',')) ");
            fieldList.add(CommonConstant.STR_SEPARATOR + vo.getStaffId() + CommonConstant.STR_SEPARATOR);
        }
        sb.append(" GROUP BY info.COLLECTORID ");
        Object[] objects = fieldList.toArray();
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sb.toString(), objects);

        List<ZjsNumVO> rstList = new LinkedList<>();
        ZjsNumVO zjsNumVO = null;
        for(Map<String,Object> map : list){
            zjsNumVO = new ZjsNumVO();
            zjsNumVO.setStaffId((int)map.get("COLLECTORID"));
            zjsNumVO.setNumAll((int)map.get("NUMALL"));
            rstList.add(zjsNumVO);
        }

        // 毛客资归类到渠道
        for (ZjsSourceVO sourceVO : srcList) {
            for (ZjsNumVO numAll : rstList) {
                if (sourceVO.getStaffId() == numAll.getStaffId()) {
                    sourceVO.getZjsNumVO().setNumAll(numAll.getNumAll());
                    break;
                }
            }
        }
    }


    /**
     * 统计毛客资数量
     *
     * @param vo
     * @param srcList
     * @return
     * @
     */
    public void getKzNumList(AnalyzeVO vo, List<ZjsSourceVO> srcList)  {

        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT info.COLLECTORID , COUNT( 1 ) KZNUM FROM ");
        sb.append(DBSplitUtil.getInfoTabName(vo.getCompanyId()));
        sb.append(" info WHERE info.ISDEL = 0  AND info.SRCTYPE IN ( 3, 4, 5 ) ");
        sb.append(" AND info.CREATETIME >= ? AND info.CREATETIME <= ?  AND info.COMPANYID = ? ");
        sb.append(" AND info.STATUSID != ?  AND info.STATUSID != ?  AND info.STATUSID != ? ");

        LinkedList<Object> fieldList = new LinkedList<>();
        fieldList.add(vo.getStart());
        fieldList.add(vo.getEnd());
        fieldList.add(vo.getCompanyId());
        fieldList.add(ClientStatusConst.BE_FILTER_INVALID);
        fieldList.add(ClientStatusConst.BE_WAIT_FILTER);
        fieldList.add(ClientStatusConst.BE_WAIT_WAITING);
        if (StringUtils.isNotEmpty(vo.getStaffId())) {
            sb.append(" AND INSTR( ? ,CONCAT(',',info.COLLECTORID+'',',')) ");
            fieldList.add(CommonConstant.STR_SEPARATOR + vo.getStaffId() + CommonConstant.STR_SEPARATOR);
        }
        sb.append(" GROUP BY info.COLLECTORID ");
        Object[] objects = fieldList.toArray();
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sb.toString(), objects);
        List<ZjsNumVO> rstList = new LinkedList<>();
        ZjsNumVO zjsNumVO = null;
        for(Map<String,Object> map : list){
            zjsNumVO = new ZjsNumVO();
            zjsNumVO.setStaffId((int) map.get("COLLECTORID"));
            zjsNumVO.setKzNum((int) map.get("KZNUM"));
            rstList.add(zjsNumVO);
        }
        // 毛客资归类到渠道
        for (ZjsSourceVO sourceVO : srcList) {
            for (ZjsNumVO kzNum : rstList) {
                if (sourceVO.getStaffId() == kzNum.getStaffId()) {
                    sourceVO.getZjsNumVO().setKzNum(kzNum.getKzNum());
                    break;
                }
            }
        }
    }

    /**
     * 统计转介绍有效客资
     *
     * @param vo
     * @param srcList
     * @return
     * @
     */
    public void getYxNumList(AnalyzeVO vo, List<ZjsSourceVO> srcList, JSONObject config)
             {
        JSONArray xkzArr = config.getJSONObject(ReportsConfigConst.ZJS_VALID_SET).getJSONArray(ReportsConfigConst.XKZ_CLASS);
        JSONArray yyyArr = config.getJSONObject(ReportsConfigConst.ZJS_VALID_SET).getJSONArray(ReportsConfigConst.YYY_CLASS);
        JSONArray dzzArr = config.getJSONObject(ReportsConfigConst.ZJS_VALID_SET).getJSONArray(ReportsConfigConst.DZZ_CLASS);
        JSONArray yjdArr = config.getJSONObject(ReportsConfigConst.ZJS_VALID_SET).getJSONArray(ReportsConfigConst.YJD_CLASS);
        JSONArray yddArr = config.getJSONObject(ReportsConfigConst.ZJS_VALID_SET).getJSONArray(ReportsConfigConst.YDD_CLASS);
        JSONArray wxArr = config.getJSONObject(ReportsConfigConst.ZJS_VALID_SET).getJSONArray(ReportsConfigConst.WX_CLASS);
        JSONArray param = new JSONArray();
        param.addAll(xkzArr);
        param.addAll(yyyArr);
        param.addAll(dzzArr);
        param.addAll(yjdArr);
        param.addAll(yddArr);
        param.addAll(wxArr);
        if (param.isEmpty()) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT info.COLLECTORID , COUNT( 1 ) YXNUM FROM ");
        sb.append(DBSplitUtil.getInfoTabName(vo.getCompanyId()));
        sb.append(" info WHERE info.ISDEL = 0  AND info.SRCTYPE IN ( 3, 4, 5 ) ");
        sb.append(" AND info.CREATETIME >= ? AND info.CREATETIME <= ?  AND info.COMPANYID = ? ");
        LinkedList<Object> fieldList = new LinkedList<Object>();
        fieldList.add(vo.getStart());
        fieldList.add(vo.getEnd());
        fieldList.add(vo.getCompanyId());
        getWhereCondition(sb, "info.STATUSID", param);
        if (StringUtils.isNotEmpty(vo.getStaffId())) {
            sb.append(" AND INSTR( ? ,CONCAT(',',info.COLLECTORID+'',',')) ");
            fieldList.add(CommonConstant.STR_SEPARATOR + vo.getStaffId() + CommonConstant.STR_SEPARATOR);
        }
        sb.append(" GROUP BY info.COLLECTORID ");
        Object[] objects = fieldList.toArray();
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sb.toString(), objects);
        List<ZjsNumVO> rstList = new LinkedList<>();
        ZjsNumVO zjsNumVO = null;
        for (Map<String, Object> map : list) {
            zjsNumVO = new ZjsNumVO();
            zjsNumVO.setStaffId((int)map.get("COLLECTORID"));
            zjsNumVO.setYxNum((int)map.get("YXNUM"));
            rstList.add(zjsNumVO);
        }

        // 毛客资归类到渠道
        for (ZjsSourceVO sourceVO : srcList) {
            for (ZjsNumVO yxNum : rstList) {
                if (sourceVO.getStaffId() == yxNum.getStaffId()) {
                    sourceVO.getZjsNumVO().setYxNum(yxNum.getYxNum());
                    break;
                }
            }
        }
    }


    /**
     * 统计电商入店客资数量并归类到渠道
     *
     * @param vo
     * @param srcList
     * @return
     * @
     */
    public void getRdNumList(AnalyzeVO vo, List<ZjsSourceVO> srcList)  {

        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT info.COLLECTORID , COUNT( 1 ) RDNUM FROM ");
        sb.append(DBSplitUtil.getInfoTabName(vo.getCompanyId()));
        sb.append(" info WHERE info.ISDEL = 0  AND info.SRCTYPE IN ( 3, 4, 5 ) ");
        sb.append(" AND info.COMESHOPTIME >= ? AND info.COMESHOPTIME <= ?  AND info.COMPANYID = ? ");
//        sb.append(" AND INSTR( ?, CONCAT(',',info.STATUSID + '',',')) != 0");
        LinkedList<Object> fieldList = new LinkedList<Object>();
        fieldList.add(vo.getStart());
        fieldList.add(vo.getEnd());
        fieldList.add(vo.getCompanyId());
//        fieldList.add(ClientStatusConst.IS_COME_SHOP);

        if (StringUtils.isNotEmpty(vo.getStaffId())) {
            sb.append(" AND INSTR( ? ,CONCAT(',',info.COLLECTORID+'',',')) ");
            fieldList.add(CommonConstant.STR_SEPARATOR + vo.getStaffId() + CommonConstant.STR_SEPARATOR);
        }
        sb.append(" GROUP BY info.COLLECTORID ");
        Object[] objects = fieldList.toArray();
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sb.toString(), objects);
        List<ZjsNumVO> rstList = new LinkedList<>();
        ZjsNumVO zjsNumVO = null;
        for(Map<String,Object> map : list ){
            zjsNumVO = new ZjsNumVO();
            zjsNumVO.setStaffId((int)map.get("COLLECTORID"));
            zjsNumVO.setRdNum((int)map.get("RDNUM"));
            rstList.add(zjsNumVO);
        }
        // 毛客资归类到渠道
        for (ZjsSourceVO sourceVO : srcList) {
            for (ZjsNumVO rdNum : rstList) {
                if (sourceVO.getStaffId() == rdNum.getStaffId()) {
                    sourceVO.getZjsNumVO().setRdNum(rdNum.getRdNum());
                    break;
                }
            }
        }

    }

    /**
     * 统计电商成交客资数量并归类到渠道
     *
     * @param vo
     * @param srcList
     * @return
     * @
     */
    public void getCjNumList(AnalyzeVO vo, List<ZjsSourceVO> srcList)  {

        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT info.COLLECTORID , COUNT( 1 ) CJNUM, SUM(det.AMOUNT) YY, SUM(det.STAYAMOUNT) YS, AVG(det.AMOUNT) JJ FROM ");
        sb.append(DBSplitUtil.getInfoTabName(vo.getCompanyId()));
        sb.append(" info INNER JOIN ");
        sb.append(DBSplitUtil.getDetailTabName(vo.getCompanyId()));
        sb.append(" det ON det.KZID = info.KZID AND det.COMPANYID = info.COMPANYID ");
        sb.append(" WHERE info.ISDEL = 0  AND info.SRCTYPE IN ( 3, 4, 5 ) ");
        sb.append(" AND info.SUCCESSTIME >= ? AND info.SUCCESSTIME <= ?  AND info.COMPANYID = ? ");
        sb.append(" AND INSTR( ?, CONCAT(',',info.STATUSID + '',',')) != 0");
        LinkedList<Object> fieldList = new LinkedList<>();
        fieldList.clear();
        fieldList.add(vo.getStart());
        fieldList.add(vo.getEnd());
        fieldList.add(vo.getCompanyId());
        fieldList.add(ClientStatusConst.IS_SUCCESS);
        if (StringUtils.isNotEmpty(vo.getStaffId())) {
            sb.append(" AND INSTR( ? ,CONCAT(',',info.COLLECTORID+'',',')) ");
            fieldList.add(CommonConstant.STR_SEPARATOR + vo.getStaffId() + CommonConstant.STR_SEPARATOR);
        }
        sb.append(" GROUP BY info.COLLECTORID ");
        Object[] objects = fieldList.toArray();
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sb.toString(), objects);
        List<ZjsNumVO> rstList = new LinkedList<>();
        for(Map<String,Object> map : list){
            ZjsNumVO zjsNumVO = null;
            zjsNumVO = new ZjsNumVO();
            zjsNumVO.setStaffId((int)map.get("COLLECTORID"));
            zjsNumVO.setCjNum((int)map.get("CJNUM"));
            zjsNumVO.setYyAmount((Double)map.get("YY"));
            zjsNumVO.setJjAmount((Double)map.get("JJ"));
            rstList.add(zjsNumVO);
        }

        // 毛客资归类到渠道
        for (ZjsSourceVO sourceVO : srcList) {
            for (ZjsNumVO cjNum : rstList) {
                if (sourceVO.getStaffId() == cjNum.getStaffId()) {
                    sourceVO.getZjsNumVO().setCjNum(cjNum.getCjNum());
                    sourceVO.getZjsNumVO().setYyAmount(cjNum.getYyAmount());
                    sourceVO.getZjsNumVO().setJjAmount(cjNum.getJjAmount());
                    break;
                }
            }
        }
    }
    public void getRdCjNumList(AnalyzeVO vo, List<ZjsSourceVO> srcList)  {

        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT info.COLLECTORID , COUNT( 1 ) CJNUM, SUM(det.AMOUNT) YY, SUM(det.STAYAMOUNT) YS, AVG(det.AMOUNT) JJ FROM ");
        sb.append(DBSplitUtil.getInfoTabName(vo.getCompanyId()));
        sb.append(" info INNER JOIN ");
        sb.append(DBSplitUtil.getDetailTabName(vo.getCompanyId()));
        sb.append(" det ON det.KZID = info.KZID AND det.COMPANYID = info.COMPANYID ");
        sb.append(" WHERE info.ISDEL = 0  AND info.SRCTYPE IN ( 3, 4, 5 ) ");
        sb.append(" AND info.SUCCESSTIME >= ? AND info.SUCCESSTIME <= ?  AND info.COMPANYID = ? ");
        sb.append(" AND INSTR( ?, CONCAT(',',info.STATUSID + '',',')) != 0");
        sb.append(" and info.statusid in (9,30)");
        LinkedList<Object> fieldList = new LinkedList<>();
        fieldList.clear();
        fieldList.add(vo.getStart());
        fieldList.add(vo.getEnd());
        fieldList.add(vo.getCompanyId());
        fieldList.add(ClientStatusConst.IS_SUCCESS);
        if (StringUtils.isNotEmpty(vo.getStaffId())) {
            sb.append(" AND INSTR( ? ,CONCAT(',',info.COLLECTORID+'',',')) ");
            fieldList.add(CommonConstant.STR_SEPARATOR + vo.getStaffId() + CommonConstant.STR_SEPARATOR);
        }
        sb.append(" GROUP BY info.COLLECTORID ");
        Object[] objects = fieldList.toArray();
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sb.toString(), objects);
        List<ZjsNumVO> rstList = new LinkedList<>();
        ZjsNumVO zjsNumVO = null;
        for(Map<String,Object> map : list ){
            zjsNumVO = new ZjsNumVO();
            zjsNumVO.setStaffId((int) map.get("COLLECTORID"));
            zjsNumVO.setRdCjNum((int) map.get("CJNUM"));
            rstList.add(zjsNumVO);
        }
        // 毛客资归类到渠道
        for (ZjsSourceVO sourceVO : srcList) {
            for (ZjsNumVO cjNum : rstList) {
                if (sourceVO.getStaffId() == cjNum.getStaffId()) {
                    sourceVO.getZjsNumVO().setRdCjNum(cjNum.getRdCjNum());
                    break;
                }
            }
        }
    }
    public void getzxCjNumList(AnalyzeVO vo, List<ZjsSourceVO> srcList)  {

        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT info.COLLECTORID , COUNT( 1 ) CJNUM, SUM(det.AMOUNT) YY, SUM(det.STAYAMOUNT) YS, AVG(det.AMOUNT) JJ FROM ");
        sb.append(DBSplitUtil.getInfoTabName(vo.getCompanyId()));
        sb.append(" info INNER JOIN ");
        sb.append(DBSplitUtil.getDetailTabName(vo.getCompanyId()));
        sb.append(" det ON det.KZID = info.KZID AND det.COMPANYID = info.COMPANYID ");
        sb.append(" WHERE info.ISDEL = 0  AND info.SRCTYPE IN ( 3, 4, 5 ) ");
        sb.append(" AND info.SUCCESSTIME >= ? AND info.SUCCESSTIME <= ?  AND info.COMPANYID = ? ");
        sb.append(" AND INSTR( ?, CONCAT(',',info.STATUSID + '',',')) != 0");
        sb.append(" and info.statusid in (40)");
        LinkedList<Object> fieldList = new LinkedList<Object>();
        fieldList.clear();
        fieldList.add(vo.getStart());
        fieldList.add(vo.getEnd());
        fieldList.add(vo.getCompanyId());
        fieldList.add(ClientStatusConst.IS_SUCCESS);
        if (StringUtils.isNotEmpty(vo.getStaffId())) {
            sb.append(" AND INSTR( ? ,CONCAT(',',info.COLLECTORID+'',',')) ");
            fieldList.add(CommonConstant.STR_SEPARATOR + vo.getStaffId() + CommonConstant.STR_SEPARATOR);
        }
        sb.append(" GROUP BY info.COLLECTORID ");
        Object[] objects = fieldList.toArray();
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sb.toString(), objects);
        List<ZjsNumVO> rstList = new LinkedList<>();
        ZjsNumVO zjsNumVO = null;
        for(Map<String,Object> map : list){
            zjsNumVO = new ZjsNumVO();
            zjsNumVO.setStaffId((int)map.get("COLLECTORID"));
            zjsNumVO.setZxCjNum((int)map.get("CJNUM"));
            rstList.add(zjsNumVO);

        }
        // 毛客资归类到渠道
        for (ZjsSourceVO sourceVO : srcList) {
            for (ZjsNumVO cjNum : rstList) {
                if (sourceVO.getStaffId() == cjNum.getStaffId()) {
                    sourceVO.getZjsNumVO().setZxCjNum(cjNum.getZxCjNum());
                    break;
                }
            }
        }
    }
    /**
     * 计算渠道合计
     *
     * @param vo
     * @param srcList
     * @return
     */
    private List<ZjsSourceVO> getSrcTotal(AnalyzeVO vo, List<ZjsSourceVO> srcList) {
        ZjsSourceVO channelTotal = new ZjsSourceVO("total", vo.getCompanyId());
        ZjsNumVO numTotal = new ZjsNumVO("total");
        int numAll = 0;
        int kzNum = 0;
        int yxNum = 0;
        int rdNum = 0;
        int cjNum = 0;
        int rdCjNum=0;
        int zxCjNum=0;
        double yyAmount = 0.0;
        double realAmount=0.0;
        double bkAmount=0.0;
        for (ZjsSourceVO sourceVO : srcList) {
            numAll += sourceVO.getZjsNumVO().getNumAll();
            kzNum += sourceVO.getZjsNumVO().getKzNum();
            yxNum += sourceVO.getZjsNumVO().getYxNum();
            rdNum += sourceVO.getZjsNumVO().getRdNum();
            cjNum += sourceVO.getZjsNumVO().getCjNum();
            rdCjNum+=sourceVO.getZjsNumVO().getRdCjNum();
            zxCjNum+=sourceVO.getZjsNumVO().getZxCjNum();
            yyAmount += Double.valueOf(sourceVO.getZjsNumVO().getYyAmount());
            realAmount+=Double.valueOf(sourceVO.getZjsNumVO().getRealAmount());
            bkAmount+=Double.valueOf(sourceVO.getZjsNumVO().getBkAmount());
        }
        numTotal.setNumAll(numAll);
        numTotal.setKzNum(kzNum);
        numTotal.setYxNum(yxNum);
        numTotal.setRdNum(rdNum);
        numTotal.setCjNum(cjNum);
        numTotal.setZxCjNum(zxCjNum);
        numTotal.setRdCjNum(rdCjNum);
        numTotal.setYyAmount(yyAmount);
        numTotal.setRealAmount(realAmount);
        numTotal.setBkAmount(bkAmount);
        channelTotal.setZjsNumVO(numTotal);
        srcList.add(0, channelTotal);
        return srcList;
    }

    // 计算率和成本
    private void getRateAndCost(List<ZjsSourceVO> srcList) {

        for (ZjsSourceVO sourceVO : srcList) {
            // 有效率
            if (sourceVO.getZjsNumVO().getKzNum() != 0) {
                sourceVO.getZjsNumVO().setYxRate(StringUtil.decimalFormat(sourceVO.getZjsNumVO().getYxNum() / Double.valueOf(sourceVO.getZjsNumVO().getKzNum()) * 100));
            }
            // 毛客资咨询入店率
            if (sourceVO.getZjsNumVO().getKzNum() != 0) {
                sourceVO.getZjsNumVO()
                        .setGrossRdRate(StringUtil.decimalFormat(sourceVO.getZjsNumVO().getRdNum() / Double.valueOf(sourceVO.getZjsNumVO().getKzNum()) * 100));
            }
            // 入店成交率
            if (sourceVO.getZjsNumVO().getRdNum() != 0) {
                sourceVO.getZjsNumVO()
                        .setCjRate(StringUtil.decimalFormat(sourceVO.getZjsNumVO().getCjNum() / Double.valueOf(sourceVO.getZjsNumVO().getRdNum()) * 100));
            }
            // 毛客资成交率
            if (sourceVO.getZjsNumVO().getKzNum() != 0) {
                sourceVO.getZjsNumVO()
                        .setGrossCjRate(StringUtil.decimalFormat(sourceVO.getZjsNumVO().getCjNum() / Double.valueOf(sourceVO.getZjsNumVO().getKzNum()) * 100));
            }
            // 有效客资成交率
            if (sourceVO.getZjsNumVO().getYxNum() != 0) {
                sourceVO.getZjsNumVO()
                        .setYxCjRate(StringUtil.decimalFormat(sourceVO.getZjsNumVO().getCjNum() / Double.valueOf(sourceVO.getZjsNumVO().getYxNum()) * 100));
            }
            // 均价
            if (sourceVO.getZjsNumVO().getCjNum() != 0) {
                sourceVO.getZjsNumVO().setJjAmount(StringUtil.decimalFormatDouble(sourceVO.getZjsNumVO().getYyAmount() / sourceVO.getZjsNumVO().getCjNum()));
            }
        }
    }

    /**
     * 数组转换成where条件
     *
     * @param sb
     */
    public void getWhereCondition(StringBuilder sb, String colum, JSONArray paramArr) {
        sb.append(" AND ( ");
        for (int i = 0; i < paramArr.size(); i++) {
            if (StringUtils.isEmpty(paramArr.getString(i))) {
                continue;
            }
            sb.append(" " + colum + " = '");
            sb.append(paramArr.getString(i));
            if (i != paramArr.size() - 1) {
                sb.append("' OR ");
            } else {
                sb.append("'");
            }
        }
        sb.append(" ) ");
    }



}
