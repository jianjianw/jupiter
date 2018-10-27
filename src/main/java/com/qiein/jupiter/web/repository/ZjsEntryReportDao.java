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

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 转介绍提报统计
 */
@Repository
public class ZjsEntryReportDao {


    @Autowired
    private JdbcTemplate jdbcTemplate;


    public HashMap<String, Object> getZjsEntry(AnalyzeVO vo){
        
        HashMap<String, Object> rst = new HashMap<String, Object>();

        // 获取报表配置
        JSONObject config = getRepertsConfig(vo.getCompanyId());
        // 获取转介绍来源集合,总客资数量
        List<ZjsSourceVO> srcList = getZjsSourceList(vo, config);
        //获取总客资量
        getNumAll(vo, srcList);
        // 获取渠道客资量并归类到渠道
        getKzNumList(vo, srcList);
        // 获取渠道无效客资量并归类到渠道
        getYxNumList(vo, srcList, config);
        // 待定量
        getDdNumList(vo, srcList, config);
        // 获取渠道入店客资量并归类到渠道
        getRdNumList(vo, srcList);
        // 获取渠道成交客资量并归类到渠道,成交营业额和成交均价
        getCjNumList(vo, srcList);
        getRdCjNumList(vo, srcList);
        getzxCjNumList(vo, srcList);
        // 已加微信
        getAddWechatNum(vo, srcList);
        // 计算渠道合计
        getSrcTotal(vo, srcList);
        // 计算率和成本
        getRateAndCost(srcList);

        rst.put("analysis", srcList);
        return rst;
    }

    /**
     * 获取转介绍来源集合
     *
     */
    public List<ZjsSourceVO> getZjsSourceList(AnalyzeVO vo, JSONObject config)  {
        //获取报表配置
        boolean soureShow = config.getJSONObject(ReportsConfigConst.SHOW_SET).getBoolean(ReportsConfigConst.SOURCE_SHOW);
        StringBuilder sb = null;
        LinkedList<Object> fieldList = new LinkedList<>();
        if (soureShow) {
            //显示全部渠道
            sb = new StringBuilder();
            sb.append(" SELECT src.ID, src.SRCNAME, src.SRCIMG, src.COMPANYID  ");
            sb.append(" FROM hm_crm_source src LEFT JOIN hm_crm_channel c ON c.COMPANYID = src.COMPANYID AND src.CHANNELID = c.ID  ");
            sb.append(" WHERE src.TYPEID IN ( 3, 4, 5 ) AND src.COMPANYID = ?  ");
            fieldList.add(vo.getCompanyId());

            if (StringUtils.isNotEmpty(vo.getSourceIds())) {
                sb.append(" AND INSTR( ? ,CONCAT(',',src.ID+'',',')) ");
                fieldList.add(CommonConstant.STR_SEPARATOR + vo.getSourceIds() + CommonConstant.STR_SEPARATOR);
            }
            sb.append(" ORDER BY c.TYPEID ASC, c.ISSHOW DESC, c.PRIORITY ASC, src.ISSHOW DESC, src.PRIORITY ASC ");

        } else {

            sb = new StringBuilder();
            sb.append(" SELECT src.ID, src.SRCNAME, src.SRCIMG, info.COMPANYID FROM ");
            sb.append(DBSplitUtil.getInfoTabName(vo.getCompanyId()));
            sb.append(" info LEFT JOIN hm_crm_source src ON info.COMPANYID = src.COMPANYID AND info.SOURCEID = src.ID ");
            sb.append(" WHERE src.TYPEID IN ( 3, 4, 5 ) AND info.COMPANYID = ? ");
            sb.append(" AND (( info.CREATETIME >= ? AND info.CREATETIME <= ? ) OR  ( info.COMESHOPTIME >= ? AND info.COMESHOPTIME <= ? ) OR ( info.SUCCESSTIME >= ? AND info.SUCCESSTIME <= ? ) ) ");

            fieldList.add(vo.getCompanyId());
            fieldList.add(vo.getStart());
            fieldList.add(vo.getEnd());
            fieldList.add(vo.getStart());
            fieldList.add(vo.getEnd());
            fieldList.add(vo.getStart());
            fieldList.add(vo.getEnd());

            if (StringUtils.isNotEmpty(vo.getSourceIds())) {
                sb.append(" AND INSTR( ? ,CONCAT(',',src.ID+'',',')) ");
                fieldList.add(CommonConstant.STR_SEPARATOR + vo.getSourceIds() + CommonConstant.STR_SEPARATOR);
            }
            sb.append(" GROUP BY info.SOURCEID ");
            sb.append(" ORDER BY src.PRIORITY, src.ID ");
        }
        Object[] objects = fieldList.toArray();
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sb.toString(), objects);

        List<ZjsSourceVO> srcList = new LinkedList<>();
        ZjsSourceVO zjsSourceVO = null;
        for(Map<String,Object> map : list){
            zjsSourceVO = new ZjsSourceVO();
            zjsSourceVO.setSourceId(((Long)map.get("ID")).intValue());
            zjsSourceVO.setSourceName((String) map.get("SRCNAME"));
            zjsSourceVO.setCompanyId(vo.getCompanyId());
            zjsSourceVO.setSourceImg((String) map.get("SRCIMG"));
            zjsSourceVO.setZjsNumVO(new ZjsNumVO());
            srcList.add(zjsSourceVO);
        }
        return srcList;
    }

    /**
     * 获取总客资量
     *
     */
    public void getNumAll(AnalyzeVO vo, List<ZjsSourceVO> srcList)  {

        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT info.SOURCEID, COUNT( 1 ) NUMALL FROM ");
        sb.append(DBSplitUtil.getInfoTabName(vo.getCompanyId()));
        sb.append(" info WHERE info.ISDEL = 0  AND info.SRCTYPE IN ( 3, 4, 5 ) ");
        sb.append(" AND info.CREATETIME >= ? AND info.CREATETIME <= ?  AND info.COMPANYID = ? ");

        LinkedList<Object> fieldList = new LinkedList<>();
        fieldList.add(vo.getStart());
        fieldList.add(vo.getEnd());
        fieldList.add(vo.getCompanyId());

        if (StringUtils.isNotEmpty(vo.getSourceIds())) {
            sb.append(" AND INSTR( ? ,CONCAT(',',info.SOURCEID+'',',')) ");
            fieldList.add(CommonConstant.STR_SEPARATOR + vo.getSourceIds() + CommonConstant.STR_SEPARATOR);
        }
        sb.append(" GROUP BY info.SOURCEID ");

        Object[] objects = fieldList.toArray();
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sb.toString(), objects);

        List<ZjsNumVO> rstList = new LinkedList<>();
        ZjsNumVO zjsNumVO = null;
        for(Map<String,Object> map : list){
            zjsNumVO = new ZjsNumVO();
            zjsNumVO.setSourceId(((Long)map.get("SOURCEID")).intValue());
            zjsNumVO.setNumAll(((Long)map.get("NUMALL")).intValue());
            rstList.add(zjsNumVO);
        }
        // 毛客资归类到渠道
        for (ZjsSourceVO sourceVO : srcList) {
            for (ZjsNumVO kzNum : rstList) {
                if (sourceVO.getSourceId() == kzNum.getSourceId()) {
                    sourceVO.getZjsNumVO().setNumAll(kzNum.getNumAll());
                    break;
                }
            }
        }
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
        StringBuilder sb = new StringBuilder();
        sb.append("  SELECT rpset.DEFINESET FROM hm_crm_reports_set rpset WHERE rpset.COMPANYID =? ");

        String config = jdbcTemplate.queryForObject(sb.toString(), new Object[]{companyId}, String.class);

        if(StringUtils.isEmpty(config)){
            return JSONObject.parseObject("");
        }else{
            return JSONObject.parseObject(config);
        }
    }


    /**
     * 统计电商毛客资数量并归类到渠道
     *
     * @param vo
     * @param srcList
     * @return
     */
    public void getKzNumList(AnalyzeVO vo, List<ZjsSourceVO> srcList)  {

        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT info.SOURCEID , COUNT( 1 ) KZNUM FROM ");
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
        if (StringUtils.isNotEmpty(vo.getSourceIds())) {
            sb.append(" AND INSTR( ? ,CONCAT(',',info.SOURCEID+'',',')) ");
            fieldList.add(CommonConstant.STR_SEPARATOR + vo.getSourceIds() + CommonConstant.STR_SEPARATOR);
        }
        sb.append(" GROUP BY info.SOURCEID ");

        Object[] objects = fieldList.toArray();
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sb.toString(), objects);


        List<ZjsNumVO> rstList = new LinkedList<>();
        ZjsNumVO zjsNumVO = null;
        for(Map<String,Object> map : list){
            zjsNumVO = new ZjsNumVO();
            zjsNumVO.setSourceId(((Long) map.get("SOURCEID")).intValue());
            zjsNumVO.setKzNum(((Long) map.get("KZNUM")).intValue());
            rstList.add(zjsNumVO);
        }
        // 毛客资归类到渠道
        for (ZjsSourceVO sourceVO : srcList) {
            for (ZjsNumVO kzNum : rstList) {
                if (sourceVO.getSourceId() == kzNum.getSourceId()) {
                    sourceVO.getZjsNumVO().setKzNum(kzNum.getKzNum());
                    sourceVO.getZjsNumVO().setWxNum(kzNum.getKzNum());
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
        sb.append(" SELECT info.SOURCEID , COUNT( 1 ) YXNUM FROM ");
        sb.append(DBSplitUtil.getInfoTabName(vo.getCompanyId()));
        sb.append(" info WHERE info.ISDEL = 0  AND info.SRCTYPE IN ( 3, 4, 5 ) ");
        sb.append(" AND info.CREATETIME >= ? AND info.CREATETIME <= ?  AND info.COMPANYID = ? ");
        LinkedList<Object> fieldList = new LinkedList<>();

        fieldList.add(vo.getStart());
        fieldList.add(vo.getEnd());
        fieldList.add(vo.getCompanyId());
        getWhereCondition(sb, "info.STATUSID", param);
        if (StringUtils.isNotEmpty(vo.getSourceIds())) {
            sb.append(" AND INSTR( ? ,CONCAT(',',info.SOURCEID+'',',')) ");
            fieldList.add(CommonConstant.STR_SEPARATOR + vo.getSourceIds() + CommonConstant.STR_SEPARATOR);
        }
        sb.append(" GROUP BY info.SOURCEID ");

        Object[] objects = fieldList.toArray();
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sb.toString(), objects);
        List<ZjsNumVO> rstList = new LinkedList<>();
        ZjsNumVO zjsNumVO = null;
        for(Map<String,Object> map : list){
            zjsNumVO = new ZjsNumVO();
            zjsNumVO.setSourceId(((Long)map.get("SOURCEID")).intValue());
            zjsNumVO.setYxNum(((Long)map.get("YXNUM")).intValue());
            rstList.add(zjsNumVO);

        }
        // 毛客资归类到渠道
        for (ZjsSourceVO sourceVO : srcList) {
            for (ZjsNumVO yxNum : rstList) {
                if (sourceVO.getSourceId() == yxNum.getSourceId()) {
                    sourceVO.getZjsNumVO().setYxNum(yxNum.getYxNum());
                    sourceVO.getZjsNumVO().setWxNum(sourceVO.getZjsNumVO().getWxNum() - yxNum.getYxNum());
                    break;
                }
            }
        }
    }


    /**
     * 获取待定量，有效 = 客资量- 无效量 - 待定量
     *
     * @param vo
     * @param srcList
     * @return
     */
    public void getDdNumList(AnalyzeVO vo, List<ZjsSourceVO> srcList, JSONObject config)  {
        JSONArray xkzArr = config.getJSONObject(ReportsConfigConst.ZJS_DD_SET).getJSONArray(ReportsConfigConst.XKZ_CLASS);
        JSONArray yyyArr = config.getJSONObject(ReportsConfigConst.ZJS_DD_SET).getJSONArray(ReportsConfigConst.YYY_CLASS);
        JSONArray dzzArr = config.getJSONObject(ReportsConfigConst.ZJS_DD_SET).getJSONArray(ReportsConfigConst.DZZ_CLASS);
        JSONArray yjdArr = config.getJSONObject(ReportsConfigConst.ZJS_DD_SET).getJSONArray(ReportsConfigConst.YJD_CLASS);
        JSONArray yddArr = config.getJSONObject(ReportsConfigConst.ZJS_DD_SET).getJSONArray(ReportsConfigConst.YDD_CLASS);
        JSONArray wxArr = config.getJSONObject(ReportsConfigConst.ZJS_DD_SET).getJSONArray(ReportsConfigConst.WX_CLASS);
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
        sb.append(" SELECT info.SOURCEID , COUNT( 1 ) DDNUM FROM ");
        sb.append(DBSplitUtil.getInfoTabName(vo.getCompanyId()));
        sb.append(" info WHERE info.ISDEL = 0  AND info.SRCTYPE IN ( 3, 4, 5 )  ");
        sb.append(" AND info.CREATETIME >= ? AND info.CREATETIME <= ?  AND info.COMPANYID = ? ");
        LinkedList<Object> fieldList = new LinkedList<>();
        fieldList.add(vo.getStart());
        fieldList.add(vo.getEnd());
        fieldList.add(vo.getCompanyId());
        getWhereCondition(sb, "info.STATUSID", param);
        if (StringUtils.isNotEmpty(vo.getSourceIds())) {
            sb.append(" AND INSTR( ? ,CONCAT(',',info.SOURCEID+'',',')) ");
            fieldList.add(CommonConstant.STR_SEPARATOR + vo.getSourceIds() + CommonConstant.STR_SEPARATOR);
        }
        sb.append(" GROUP BY info.SOURCEID ");

        Object[] objects = fieldList.toArray();
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sb.toString(), objects);
        List<ZjsNumVO> rstList = new LinkedList<>();
        ZjsNumVO zjsNumVO = null;
        for(Map<String,Object> map : list){
            zjsNumVO = new ZjsNumVO();
            zjsNumVO.setSourceId(((Long)map.get("SOURCEID")).intValue());
            zjsNumVO.setDdNum(((Long)map.get("DDNUM")).intValue());
            rstList.add(zjsNumVO);
        }
        // 毛客资归类到渠道
        for (ZjsSourceVO sourceVO : srcList) {
            for (ZjsNumVO ddNum : rstList) {
                if (sourceVO.getSourceId() == ddNum.getSourceId()) {
                    sourceVO.getZjsNumVO().setDdNum(ddNum.getDdNum());
                    sourceVO.getZjsNumVO().setWxNum(sourceVO.getZjsNumVO().getWxNum() - ddNum.getDdNum());
                    if (config.getJSONObject(ReportsConfigConst.YX_SET).getBoolean(ReportsConfigConst.DD_IS_YX)) {
                        sourceVO.getZjsNumVO().setYxNum(sourceVO.getZjsNumVO().getYxNum() + ddNum.getDdNum());
                    }
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
     */
    public void getRdNumList(AnalyzeVO vo, List<ZjsSourceVO> srcList)  {

        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT info.SOURCEID , COUNT( 1 ) RDNUM FROM ");
        sb.append(DBSplitUtil.getInfoTabName(vo.getCompanyId()));
        sb.append(" info WHERE info.ISDEL = 0  AND info.SRCTYPE IN ( 3, 4, 5 ) ");
        sb.append(" AND info.COMESHOPTIME >= ? AND info.COMESHOPTIME <= ?  AND info.COMPANYID = ? ");
//        sb.append(" AND INSTR( ?, CONCAT(',',info.STATUSID + '',',')) != 0");
        LinkedList<Object> fieldList  = new LinkedList<>();
        fieldList.add(vo.getStart());
        fieldList.add(vo.getEnd());
        fieldList.add(vo.getCompanyId());
//        fieldList.add(ClientStatusConst.IS_COME_SHOP);

        if (StringUtils.isNotEmpty(vo.getSourceIds())) {
            sb.append(" AND INSTR( ? ,CONCAT(',',info.SOURCEID+'',',')) ");
            fieldList.add(CommonConstant.STR_SEPARATOR + vo.getSourceIds() + CommonConstant.STR_SEPARATOR);
        }
        sb.append(" GROUP BY info.SOURCEID ");
        Object[] objects = fieldList.toArray();
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sb.toString(), objects);
        List<ZjsNumVO> rstList = new LinkedList<>();
        ZjsNumVO zjsNumVO = null;
        for (Map<String,Object> map : list ){
            zjsNumVO = new ZjsNumVO();
            zjsNumVO.setSourceId(((Long)map.get("SOURCEID")).intValue());
            zjsNumVO.setRdNum(((Long)map.get("RDNUM")).intValue());
            rstList.add(zjsNumVO);

        }
        // 毛客资归类到渠道
        for (ZjsSourceVO sourceVO : srcList) {
            for (ZjsNumVO rdNum : rstList) {
                if (sourceVO.getSourceId() == rdNum.getSourceId()) {
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
        sb.append(" SELECT info.SOURCEID , COUNT( 1 ) CJNUM, SUM(det.AMOUNT) YY, SUM(det.STAYAMOUNT) YS, AVG(det.AMOUNT) JJ FROM ");
        sb.append(DBSplitUtil.getInfoTabName(vo.getCompanyId()));
        sb.append(" info INNER JOIN ");
        sb.append(DBSplitUtil.getDetailTabName(vo.getCompanyId()));
        sb.append(" det ON det.KZID = info.KZID AND det.COMPANYID = info.COMPANYID ");
        sb.append(" WHERE info.ISDEL = 0  AND info.SRCTYPE IN ( 3, 4, 5 ) ");
        sb.append(" AND info.SUCCESSTIME >= ? AND info.SUCCESSTIME <= ?  AND info.COMPANYID = ? ");
        sb.append(" AND INSTR( ?, CONCAT(',',info.STATUSID + '',',')) != 0");
        LinkedList<Object> fieldList = new LinkedList<>();
        fieldList.add(vo.getStart());
        fieldList.add(vo.getEnd());
        fieldList.add(vo.getCompanyId());
        fieldList.add(ClientStatusConst.IS_SUCCESS);
        if (StringUtils.isNotEmpty(vo.getSourceIds())) {
            sb.append(" AND INSTR( ? ,CONCAT(',',info.SOURCEID+'',',')) ");
            fieldList.add(CommonConstant.STR_SEPARATOR + vo.getSourceIds() + CommonConstant.STR_SEPARATOR);
        }
        sb.append(" GROUP BY info.SOURCEID ");
        Object[] objects = fieldList.toArray();
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sb.toString(), objects);
        List<ZjsNumVO> rstList = new LinkedList<>();
        ZjsNumVO zjsNumVO = null;
        for(Map<String,Object> map : list ){
            zjsNumVO = new ZjsNumVO();
            zjsNumVO.setSourceId(((Long)map.get("SOURCEID")).intValue());
            zjsNumVO.setCjNum(((Long)map.get("CJNUM")).intValue());
            zjsNumVO.setYyAmount(((BigDecimal)map.get("YY")).doubleValue());
            zjsNumVO.setJjAmount(((BigDecimal)map.get("JJ")).doubleValue());
            rstList.add(zjsNumVO);
        }
        // 毛客资归类到渠道
        for (ZjsSourceVO sourceVO : srcList) {
            for (ZjsNumVO cjNum : rstList) {
                if (sourceVO.getSourceId() == cjNum.getSourceId()) {
                    sourceVO.getZjsNumVO().setCjNum(cjNum.getCjNum());
                    sourceVO.getZjsNumVO().setYyAmount(cjNum.getYyAmount());
                    sourceVO.getZjsNumVO().setJjAmount(cjNum.getJjAmount());
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
    public void getRdCjNumList(AnalyzeVO vo, List<ZjsSourceVO> srcList)  {

        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT info.SOURCEID , COUNT( 1 ) CJNUM, SUM(det.AMOUNT) YY, SUM(det.STAYAMOUNT) YS, AVG(det.AMOUNT) JJ FROM ");
        sb.append(DBSplitUtil.getInfoTabName(vo.getCompanyId()));
        sb.append(" info INNER JOIN ");
        sb.append(DBSplitUtil.getDetailTabName(vo.getCompanyId()));
        sb.append(" det ON det.KZID = info.KZID AND det.COMPANYID = info.COMPANYID ");
        sb.append(" WHERE info.ISDEL = 0  AND info.SRCTYPE IN ( 3, 4, 5 ) ");
        sb.append(" AND info.SUCCESSTIME >= ? AND info.SUCCESSTIME <= ?  AND info.COMPANYID = ? ");
        sb.append(" AND INSTR( ?, CONCAT(',',info.STATUSID + '',',')) != 0");
        sb.append(" and info.statusid in (9,30)");
        LinkedList fieldList = new LinkedList();
        fieldList.add(vo.getStart());
        fieldList.add(vo.getEnd());
        fieldList.add(vo.getCompanyId());
        fieldList.add(ClientStatusConst.IS_SUCCESS);
        if (StringUtils.isNotEmpty(vo.getSourceIds())) {
            sb.append(" AND INSTR( ? ,CONCAT(',',info.SOURCEID+'',',')) ");
            fieldList.add(CommonConstant.STR_SEPARATOR + vo.getSourceIds() + CommonConstant.STR_SEPARATOR);
        }
        sb.append(" GROUP BY info.SOURCEID ");
        Object[] objects = fieldList.toArray();
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sb.toString(),objects);
        List<ZjsNumVO> rstList = new LinkedList<>();
        ZjsNumVO zjsNumVO = null;
        for(Map<String,Object> map : list){
            zjsNumVO = new ZjsNumVO();
            zjsNumVO.setSourceId(((Long)map.get("SOURCEID")).intValue());
            zjsNumVO.setRdCjNum(((Long)map.get("CJNUM")).intValue());
            rstList.add(zjsNumVO);
        }
        // 毛客资归类到渠道
        for (ZjsSourceVO sourceVO : srcList) {
            for (ZjsNumVO cjNum : rstList) {
                if (sourceVO.getSourceId() == cjNum.getSourceId()) {
                    sourceVO.getZjsNumVO().setRdCjNum(cjNum.getRdCjNum());
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
    public void getzxCjNumList(AnalyzeVO vo, List<ZjsSourceVO> srcList)  {

        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT info.SOURCEID , COUNT( 1 ) CJNUM, SUM(det.AMOUNT) YY, SUM(det.STAYAMOUNT) YS, AVG(det.AMOUNT) JJ FROM ");
        sb.append(DBSplitUtil.getInfoTabName(vo.getCompanyId()));
        sb.append(" info INNER JOIN ");
        sb.append(DBSplitUtil.getDetailTabName(vo.getCompanyId()));
        sb.append(" det ON det.KZID = info.KZID AND det.COMPANYID = info.COMPANYID ");
        sb.append(" WHERE info.ISDEL = 0  AND info.SRCTYPE IN ( 3, 4, 5 ) ");
        sb.append(" AND info.SUCCESSTIME >= ? AND info.SUCCESSTIME <= ?  AND info.COMPANYID = ? ");
        sb.append(" AND INSTR( ?, CONCAT(',',info.STATUSID + '',',')) != 0");
        sb.append(" and info.statusid in (40)");
        LinkedList<Object> fieldList = new LinkedList<>();
        fieldList.add(vo.getStart());
        fieldList.add(vo.getEnd());
        fieldList.add(vo.getCompanyId());
        fieldList.add(ClientStatusConst.IS_SUCCESS);
        if (StringUtils.isNotEmpty(vo.getSourceIds())) {
            sb.append(" AND INSTR( ? ,CONCAT(',',info.SOURCEID+'',',')) ");
            fieldList.add(CommonConstant.STR_SEPARATOR + vo.getSourceIds() + CommonConstant.STR_SEPARATOR);
        }
        sb.append(" GROUP BY info.SOURCEID ");
        Object[] objects = fieldList.toArray();
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sb.toString(), objects);
        List<ZjsNumVO> rstList = new LinkedList<>();
        ZjsNumVO zjsNumVO = null;
        for(Map<String,Object> map : list ){
            zjsNumVO = new ZjsNumVO();
            zjsNumVO.setSourceId(((Long)map.get("SOURCEID")).intValue());
            zjsNumVO.setZxCjNum(((Long)map.get("CJNUM")).intValue());
            rstList.add(zjsNumVO);
        }
        // 毛客资归类到渠道
        for (ZjsSourceVO sourceVO : srcList) {
            for (ZjsNumVO cjNum : rstList) {
                if (sourceVO.getSourceId() == cjNum.getSourceId()) {
                    sourceVO.getZjsNumVO().setZxCjNum(cjNum.getZxCjNum());
                    break;
                }
            }
        }
    }
    /**
     * 统计已加微信
     *
     */
    public void getAddWechatNum(AnalyzeVO vo, List<ZjsSourceVO> srcList)  {

        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT info.SOURCEID , COUNT( 1 ) NUM FROM ");
        sb.append(DBSplitUtil.getInfoTabName(vo.getCompanyId()));
        sb.append(" info WHERE info.ISDEL = 0  AND info.SRCTYPE IN ( 3, 4, 5 ) ");
        sb.append(" AND info.CREATETIME >= ? AND info.CREATETIME <= ?  AND info.COMPANYID = ? ");
        sb.append(" AND WEFLAG = 1 ");
        LinkedList<Object> fieldList = new LinkedList<>();
        fieldList.add(vo.getStart());
        fieldList.add(vo.getEnd());
        fieldList.add(vo.getCompanyId());
        if (StringUtils.isNotEmpty(vo.getSourceIds())) {
            sb.append(" AND INSTR( ? ,CONCAT(',',info.SOURCEID+'',',')) ");
            fieldList.add(CommonConstant.STR_SEPARATOR + vo.getSourceIds() + CommonConstant.STR_SEPARATOR);
        }
        sb.append(" GROUP BY info.SOURCEID ");

        Object[] objects = fieldList.toArray();
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sb.toString(), objects);
        List<ZjsNumVO> rstList = new LinkedList<>();
        ZjsNumVO zjsNumVO = null;
        for(Map<String,Object> map : list){
            zjsNumVO = new ZjsNumVO();
            zjsNumVO.setSourceId(((Long)map.get("SOURCEID")).intValue());
            zjsNumVO.setAddWechatNum(((Long)map.get("NUM")).intValue());
            rstList.add(zjsNumVO);
        }
        // 毛客资归类到渠道
        for (ZjsSourceVO sourceVO : srcList) {
            for (ZjsNumVO num : rstList) {
                if (sourceVO.getSourceId() == num.getSourceId()) {
                    sourceVO.getZjsNumVO().setAddWechatNum(num.getAddWechatNum());
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
     */
    private List<ZjsSourceVO> getSrcTotal(AnalyzeVO vo, List<ZjsSourceVO> srcList) {
        ZjsSourceVO channelTotal = new ZjsSourceVO("total", vo.getCompanyId());
        ZjsNumVO numTotal = new ZjsNumVO("total");
        int numAll = 0;
        int kzNum = 0;
        int yxNum = 0;
        int rdNum = 0;
        int cjNum = 0;
        double yyAmount = 0.0;
        int wxNum = 0;
        int ddNum = 0;
        int rdCjNum=0;
        int zxCjNum=0;
        int addWechatNum = 0;
        for (ZjsSourceVO sourceVO : srcList) {
            numAll += sourceVO.getZjsNumVO().getNumAll();
            kzNum += sourceVO.getZjsNumVO().getKzNum();
            yxNum += sourceVO.getZjsNumVO().getYxNum();
            rdNum += sourceVO.getZjsNumVO().getRdNum();
            cjNum += sourceVO.getZjsNumVO().getCjNum();
            wxNum += sourceVO.getZjsNumVO().getWxNum();
            ddNum += sourceVO.getZjsNumVO().getDdNum();
            rdCjNum+=sourceVO.getZjsNumVO().getRdCjNum();
            zxCjNum+=sourceVO.getZjsNumVO().getZxCjNum();
            addWechatNum += sourceVO.getZjsNumVO().getAddWechatNum();
            yyAmount += Double.valueOf(sourceVO.getZjsNumVO().getYyAmount());
        }
        numTotal.setNumAll(numAll);
        numTotal.setKzNum(kzNum);
        numTotal.setYxNum(yxNum);
        numTotal.setRdNum(rdNum);
        numTotal.setCjNum(cjNum);
        numTotal.setYyAmount(yyAmount);
        numTotal.setWxNum(wxNum);
        numTotal.setDdNum(ddNum);
        numTotal.setAddWechatNum(addWechatNum);
        numTotal.setZxCjNum(zxCjNum);
        numTotal.setRdCjNum(rdCjNum);
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
            //有效客资入店率
            if (sourceVO.getZjsNumVO().getYxNum() != 0) {
                sourceVO.getZjsNumVO()
                        .setRdRate(StringUtil.decimalFormat(sourceVO.getZjsNumVO().getRdNum() / Double.valueOf(sourceVO.getZjsNumVO().getYxNum()) * 100));
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
            //待定率
            if(sourceVO.getZjsNumVO().getKzNum()!=0){
                sourceVO.getZjsNumVO()
                        .setDdRate(StringUtil.decimalFormat(sourceVO.getZjsNumVO().getDdNum()/Double.valueOf(sourceVO.getZjsNumVO().getKzNum())*100));
            }
            //无效率
            if(sourceVO.getZjsNumVO().getKzNum()!=0){
                sourceVO.getZjsNumVO()
                        .setWxRate(StringUtil.decimalFormat(sourceVO.getZjsNumVO().getWxNum()/Double.valueOf(sourceVO.getZjsNumVO().getKzNum())*100));
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
