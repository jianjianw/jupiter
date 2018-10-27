package com.qiein.jupiter.web.repository;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.qiein.jupiter.constant.ClientStatusConst;
import com.qiein.jupiter.constant.CommonConstant;
import com.qiein.jupiter.constant.ReportsConfigConst;
import com.qiein.jupiter.util.CollectionUtils;
import com.qiein.jupiter.util.DBSplitUtil;
import com.qiein.jupiter.util.NumUtil;
import com.qiein.jupiter.util.StringUtil;
import com.qiein.jupiter.web.entity.vo.AnalyzeVO;
import com.qiein.jupiter.web.entity.vo.ChannelVO;
import com.qiein.jupiter.web.entity.vo.NumVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 电商推广渠道统计
 */
@Repository
public class DstgChannelReportDao {


    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<ChannelVO> getDstgChannelReport(AnalyzeVO vo)  {

        HashMap<String, Object> rst = new HashMap<String, Object>();

        //判断是否筛选纯电商推广
        getCollectorIdLimit(vo);

        // 获取报表配置
        JSONObject config = getRepertsConfig(vo.getCompanyId());

        // 获取电商渠道集合,总客资数量
        List<ChannelVO> channelList = getChannelList(vo, config);


        //获取总客资量
        getNumAll(vo, channelList);
        // 获取渠道毛客资量并归类到渠道
        getKzNumList(vo, channelList);
        // 获取渠道无效客资量并归类到渠道
        getWxNumList(vo, channelList, config);
        // 获取渠道待定客资量并归类到渠道
        getDdNumList(vo, channelList, config);
        // 获取渠道入店客资量并归类到渠道
        getRdNumList(vo, channelList);
        // 获取渠道成交客资量并归类到渠道,成交营业额和成交均价
        getCjNumList(vo, channelList);
        // 获取渠道成交客资量并归类到渠道,成交营业额和成交均价
        getzxCjNumList(vo, channelList);
        // 获取渠道成交客资量并归类到渠道,成交营业额和成交均价
        getRdCjNumList(vo, channelList);
        //获取本月录入，本月成交的客资量
        getMonthSuccess(vo, channelList);
        // 获取渠道花费
        getSrcCost(vo, channelList);
        // 计算渠道合计
        getSrcTotal(vo, channelList);
        // 计算率和成本
        getRateAndCost(channelList);
        return channelList;
    }

    /**
     * 推广筛选
     *
     * @param vo
     * @
     */
    public void getCollectorIdLimit(AnalyzeVO vo)  {
        if (vo.getTypeLimt() != 1 && vo.getTypeLimt() != 2) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT rela.STAFFID FROM hm_pub_group_staff rela ");
        sb.append(" INNER JOIN hm_pub_group grp ON grp.COMPANYID = rela.COMPANYID AND grp.GROUPID = rela.GROUPID  ");
        sb.append(" WHERE rela.COMPANYID = ?  AND grp.GROUPTYPE = 'dscj'");

        List<Map<String, Object>> list = jdbcTemplate.queryForList(sb.toString(), vo.getCompanyId());
        List<Integer> staffList = new LinkedList<>();
        for(Map<String,Object> map : list){
            staffList.add((Integer) map.get("STAFFID"));
        }
        vo.setStaffList(staffList);
    }


    public static void getCollectorLimitSql(StringBuilder sb, AnalyzeVO vo) {
        if (CollectionUtils.isEmpty(vo.getStaffList())) {
            return;
        }
        if (vo.getTypeLimt() == 1) {
            sb.append(" AND info.COLLECTORID IN ( ");
        } else if (vo.getTypeLimt() == 2) {
            sb.append(" AND info.COLLECTORID NOT IN ( ");
        }
        for (int i = 0; i < vo.getStaffList().size(); i++) {
            sb.append(vo.getStaffList().get(i));
            if (i != vo.getStaffList().size() - 1) {
                sb.append(" , ");
            } else {
                sb.append(" ");
            }
        }
        sb.append(" ) ");
    }


    /**
     * 获取电商渠道集合
     *
     * @param vo
     * @return
     * @
     */
    public List<ChannelVO> getChannelList(AnalyzeVO vo, JSONObject config)  {

        //获取报表配置
        boolean soureShow = config.getJSONObject(ReportsConfigConst.SHOW_SET).getBoolean(ReportsConfigConst.SOURCE_SHOW);
        StringBuilder sb = null;
        LinkedList<Object> fieldList = new LinkedList<>();
        if (soureShow) {
            //显示全部渠道
            sb = new StringBuilder();
            sb.append(" SELECT ID, CHANNELNAME, CHANNELIMG, COMPANYID FROM ");
            sb.append(" hm_crm_channel WHERE TYPEID IN ( 1, 2 ) AND COMPANYID = ? ");
            

            fieldList.add(vo.getCompanyId());

            if (StringUtils.isNotEmpty(vo.getChannelIds())) {
                sb.append(" AND INSTR( ? ,CONCAT(',',ID+'',',')) ");
                fieldList.add(CommonConstant.STR_SEPARATOR + vo.getChannelIds() + CommonConstant.STR_SEPARATOR);
            }
            sb.append(" ORDER BY ISSHOW DESC, PRIORITY ASC ");

        } else {
            //只显示有数据的渠道
            sb = new StringBuilder();
            sb.append(" SELECT channel.ID, channel.CHANNELNAME, channel.CHANNELIMG, channel.COMPANYID FROM ");
            sb.append(DBSplitUtil.getInfoTabName(vo.getCompanyId()));
            sb.append(" info LEFT JOIN hm_crm_channel channel ON info.COMPANYID = channel.COMPANYID AND info.CHANNELID = channel.ID ");
            sb.append(" WHERE channel.TYPEID IN ( 1, 2 ) AND info.COMPANYID = ? ");
            sb.append(" AND (( info.CREATETIME >= ? AND info.CREATETIME <= ? ) OR  ( info.COMESHOPTIME >= ? AND info.COMESHOPTIME <= ? ) OR ( info.SUCCESSTIME >= ? AND info.SUCCESSTIME <= ? ) ) ");
            fieldList.clear();
            fieldList.add(vo.getCompanyId());
            fieldList.add(vo.getStart());
            fieldList.add(vo.getEnd());
            fieldList.add(vo.getStart());
            fieldList.add(vo.getEnd());
            fieldList.add(vo.getStart());
            fieldList.add(vo.getEnd());

            if (StringUtils.isNotEmpty(vo.getChannelIds())) {
                sb.append(" AND INSTR( ? ,CONCAT(',',info.CHANNELID+'',',')) ");
                fieldList.add(CommonConstant.STR_SEPARATOR + vo.getChannelIds() + CommonConstant.STR_SEPARATOR);
            }
            sb.append(" GROUP BY channel.ID ");
            sb.append(" ORDER BY channel.PRIORITY, channel.TYPEID, channel.ID ");
        }


        Object[] objects = fieldList.toArray();
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sb.toString(), objects);
        List<ChannelVO> channelList = new LinkedList<ChannelVO>();
        ChannelVO channel = null;
        for(Map<String,Object> map : list){
            channel = new ChannelVO();
            channel.setChannelId(((Long)map.get("ID")).intValue());
            channel.setChannelName((String) map.get("CHANNELNAME"));
            channel.setCompanyId(vo.getCompanyId());
            channel.setChannelImg((String)map.get("CHANNELIMG"));
            channel.setNumVO(new NumVO());
            channelList.add(channel);
        }
        return channelList;
    }

    /**
     * 总客资数量
     *
     * @param vo
     * @return
     * @
     */
    public void getNumAll(AnalyzeVO vo, List<ChannelVO> channelList)  {

        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT info.CHANNELID, COUNT( 1 ) NUMALL FROM ");
        sb.append(DBSplitUtil.getInfoTabName(vo.getCompanyId()));
        sb.append(" info WHERE info.ISDEL = 0  AND info.SRCTYPE IN ( 1, 2 ) ");
        sb.append(" AND info.CREATETIME >= ? AND info.CREATETIME <= ?  AND info.COMPANYID = ? ");

        LinkedList<Object> fieldList = new LinkedList<>();
        fieldList.add(vo.getStart());
        fieldList.add(vo.getEnd());
        fieldList.add(vo.getCompanyId());
        if (StringUtils.isNotEmpty(vo.getChannelIds())) {
            sb.append(" AND INSTR( ? ,CONCAT(',',info.CHANNELID+'',',')) ");
            fieldList.add(CommonConstant.STR_SEPARATOR + vo.getChannelIds() + CommonConstant.STR_SEPARATOR);
        }
        getCollectorLimitSql(sb, vo);
        sb.append(" GROUP BY info.CHANNELID ");

        Object[] objects = fieldList.toArray();
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sb.toString(), objects);
        List<NumVO> rstList = new LinkedList<NumVO>();
        NumVO numVO = null;
        for(Map<String,Object> map : list){
            numVO = new NumVO();
            numVO.setChannelId(((Long)map.get("CHANNELID")).intValue());
            numVO.setNumAll(((Long)map.get("NUMALL")).intValue());
            rstList.add(numVO);
        }
        // 毛客资归类到渠道
        for (ChannelVO channelVO : channelList) {
            for (NumVO kzNum : rstList) {
                if (channelVO.getChannelId() == kzNum.getChannelId()) {
                    channelVO.getNumVO().setNumAll(kzNum.getNumAll());
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
     * @return
     * @
     */
    public void getKzNumList(AnalyzeVO vo, List<ChannelVO> channelList)  {

        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT info.CHANNELID , COUNT( 1 ) KZNUM FROM ");
        sb.append(DBSplitUtil.getInfoTabName(vo.getCompanyId()));
        sb.append(" info WHERE info.ISDEL = 0  AND info.SRCTYPE IN ( 1, 2 ) ");
        sb.append(" AND info.CREATETIME >= ? AND info.CREATETIME <= ?  AND info.COMPANYID = ? ");
        sb.append(" AND info.STATUSID != ?  AND info.STATUSID != ?  AND info.STATUSID != ? ");
        LinkedList<Object> fieldList = new LinkedList<>();
        fieldList.add(vo.getStart());
        fieldList.add(vo.getEnd());
        fieldList.add(vo.getCompanyId());
        fieldList.add(ClientStatusConst.BE_FILTER_INVALID);
        fieldList.add(ClientStatusConst.BE_WAIT_FILTER);
        fieldList.add(ClientStatusConst.BE_WAIT_WAITING);
        if (StringUtils.isNotEmpty(vo.getChannelIds())) {
            sb.append(" AND INSTR( ? ,CONCAT(',',info.CHANNELID+'',',')) ");
            fieldList.add(CommonConstant.STR_SEPARATOR + vo.getChannelIds() + CommonConstant.STR_SEPARATOR);
        }
        getCollectorLimitSql(sb, vo);
        sb.append(" GROUP BY info.CHANNELID ");
        Object[] objects = fieldList.toArray();
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sb.toString(), objects);
        List<NumVO> rstList = new LinkedList<NumVO>();
        NumVO numVO = null;
        for(Map<String,Object> map : list ){
            numVO = new NumVO();
            numVO.setChannelId(((Long)map.get("CHANNELID")).intValue());
            numVO.setKzNum(((Long)map.get("KZNUM")).intValue());
            rstList.add(numVO);
        }

        // 毛客资归类到渠道
        for (ChannelVO channelVO : channelList) {
            for (NumVO kzNum : rstList) {
                if (channelVO.getChannelId() == kzNum.getChannelId()) {
                    channelVO.getNumVO().setKzNum(kzNum.getKzNum());
                    channelVO.getNumVO().setYxNum(kzNum.getKzNum());
                    break;
                }
            }
        }
    }

    /**
     * 统计电商无效客资，有效 = 客资量-无效
     *
     * @param vo
     * @return
     * @
     */
    public void getWxNumList(AnalyzeVO vo, List<ChannelVO> channelList, JSONObject config)
             {
        if (config == null) {
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT info.CHANNELID , COUNT( 1 ) WXNUM FROM ");
        sb.append(DBSplitUtil.getInfoTabName(vo.getCompanyId()));
        sb.append(" info INNER JOIN ");
        sb.append(DBSplitUtil.getDetailTabName(vo.getCompanyId()));
        sb.append(" det ON det.KZID = info.KZID AND det.COMPANYID = info.COMPANYID ");
        sb.append(" WHERE info.ISDEL = 0  AND info.SRCTYPE IN ( 1, 2 ) ");
        sb.append(" AND info.CREATETIME >= ? AND info.CREATETIME <= ?  AND info.COMPANYID = ? ");
        LinkedList<Object> fieldList = new LinkedList<>();
        fieldList.add(vo.getStart());
        fieldList.add(vo.getEnd());
        fieldList.add(vo.getCompanyId());
        if (StringUtils.isNotEmpty(vo.getChannelIds())) {
            sb.append(" AND INSTR( ? ,CONCAT(',',info.CHANNELID+'',',')) ");
            fieldList.add(CommonConstant.STR_SEPARATOR + vo.getChannelIds() + CommonConstant.STR_SEPARATOR);
        }
        getCollectorLimitSql(sb, vo);
        sb.append(" AND ( 1 != 1 ");
        //筛选无效的意向等级
        JSONArray yxLevelArr = config.getJSONObject(ReportsConfigConst.WX_SET).getJSONArray(ReportsConfigConst.DZZ_YXDJ);
        if (!yxLevelArr.isEmpty()) {
            sb.append(" OR ( info.CLASSID = ? ");
            getWhereCondition(sb, "det.YXLEVEL", yxLevelArr);
            sb.append("  ) ");
            fieldList.add(ClientStatusConst.KZ_CLASS_TRACK);
        }
        //筛选无效状态
        JSONArray statusArr = config.getJSONObject(ReportsConfigConst.WX_SET).getJSONArray(ReportsConfigConst.WX_STATUS);
        if (!statusArr.isEmpty()) {
            for (int i = 0; i < statusArr.size(); i++) {
                if (StringUtils.isEmpty(statusArr.getString(i))) {
                    continue;
                }
                sb.append(" OR info.STATUSID = ");
                sb.append(statusArr.getIntValue(i));
            }
        }
        sb.append(" ) ");
        sb.append(" GROUP BY info.CHANNELID ");
        Object[] objects = fieldList.toArray();
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sb.toString(), objects);
        List<NumVO> rstList = new LinkedList<NumVO>();
        NumVO numVO = null;
        for(Map<String,Object> map : list){
            numVO = new NumVO();
            numVO.setChannelId(((Long)map.get("CHANNELID")).intValue());
            numVO.setWxNum(((Long)map.get("WXNUM")).intValue());
            rstList.add(numVO);
        }

        // 毛客资归类到渠道
        for (ChannelVO channelVO : channelList) {
            for (NumVO wxNum : rstList) {
                if (channelVO.getChannelId() == wxNum.getChannelId()) {
                    channelVO.getNumVO().setWxNum(wxNum.getWxNum());
                    channelVO.getNumVO().setYxNum(channelVO.getNumVO().getKzNum() - wxNum.getWxNum());
                    break;
                }
            }
        }
    }


    /**
     * 获取待定量，有效 = 客资量- 无效量 - 待定量
     *
     * @param vo
     * @return
     * @
     */
    public void getDdNumList(AnalyzeVO vo, List<ChannelVO> channelList, JSONObject config)  {
        JSONArray xkzArr = config.getJSONObject(ReportsConfigConst.DS_DD_SET).getJSONArray(ReportsConfigConst.XKZ_CLASS);
        JSONArray yyyArr = config.getJSONObject(ReportsConfigConst.DS_DD_SET).getJSONArray(ReportsConfigConst.YYY_CLASS);
        JSONArray dzzArr = config.getJSONObject(ReportsConfigConst.DS_DD_SET).getJSONArray(ReportsConfigConst.DZZ_CLASS);
        JSONArray yjdArr = config.getJSONObject(ReportsConfigConst.DS_DD_SET).getJSONArray(ReportsConfigConst.YJD_CLASS);
        JSONArray yddArr = config.getJSONObject(ReportsConfigConst.DS_DD_SET).getJSONArray(ReportsConfigConst.YDD_CLASS);
        JSONArray wxArr = config.getJSONObject(ReportsConfigConst.DS_DD_SET).getJSONArray(ReportsConfigConst.WX_CLASS);
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
        sb.append(" SELECT info.CHANNELID , COUNT( 1 ) DDNUM FROM ");
        sb.append(DBSplitUtil.getInfoTabName(vo.getCompanyId()));
        sb.append(" info  WHERE info.ISDEL = 0  AND info.SRCTYPE IN ( 1, 2 ) ");
        sb.append(" AND info.CREATETIME >= ? AND info.CREATETIME <= ?  AND info.COMPANYID = ? ");
        LinkedList<Object> fieldList = new LinkedList<>();
        fieldList.add(vo.getStart());
        fieldList.add(vo.getEnd());
        fieldList.add(vo.getCompanyId());
        if (StringUtils.isNotEmpty(vo.getChannelIds())) {
            sb.append(" AND INSTR( ? ,CONCAT(',',info.CHANNELID+'',',')) ");
            fieldList.add(CommonConstant.STR_SEPARATOR + vo.getChannelIds() + CommonConstant.STR_SEPARATOR);
        }
        getWhereCondition(sb, "info.STATUSID", param);
        getCollectorLimitSql(sb, vo);
        sb.append(" GROUP BY info.CHANNELID ");
        Object[] objects = fieldList.toArray();
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sb.toString(), objects);
        List<NumVO> rstList = new LinkedList<NumVO>();
        NumVO numVO = null;
        for(Map<String,Object> map : list){
            numVO = new NumVO();
            numVO.setChannelId(((Long)map.get("CHANNELID")).intValue());
            numVO.setDdNum(((Long)map.get("DDNUM")).intValue());
            rstList.add(numVO);
        }

        // 毛客资归类到渠道
        for (ChannelVO channelVO : channelList) {
            for (NumVO ddNum : rstList) {
                if (channelVO.getChannelId() == ddNum.getChannelId()) {
                    channelVO.getNumVO().setDdNum(ddNum.getDdNum());
                    if (!config.getJSONObject(ReportsConfigConst.YX_SET).getBoolean(ReportsConfigConst.DD_IS_YX)) {
                        channelVO.getNumVO().setYxNum(channelVO.getNumVO().getYxNum() - ddNum.getDdNum());
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
     * @return
     * @
     */
    public void getRdNumList(AnalyzeVO vo, List<ChannelVO> channelList)  {

        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT info.CHANNELID , COUNT( 1 ) RDNUM FROM ");
        sb.append(DBSplitUtil.getInfoTabName(vo.getCompanyId()));
        sb.append(" info  WHERE info.ISDEL = 0  AND info.SRCTYPE IN ( 1, 2 ) ");
        sb.append(" AND info.COMESHOPTIME >= ? AND info.COMESHOPTIME <= ?  AND info.COMPANYID = ? ");
        LinkedList<Object> fieldList = new LinkedList<>();
        fieldList.add(vo.getStart());
        fieldList.add(vo.getEnd());
        fieldList.add(vo.getCompanyId());
        if (StringUtils.isNotEmpty(vo.getChannelIds())) {
            sb.append(" AND INSTR( ? ,CONCAT(',',info.CHANNELID+'',',')) ");
            fieldList.add(CommonConstant.STR_SEPARATOR + vo.getChannelIds() + CommonConstant.STR_SEPARATOR);
        }
        getCollectorLimitSql(sb, vo);
//        sb.append(" AND INSTR( ?, CONCAT(',',info.STATUSID + '',',')) != 0");
        sb.append(" GROUP BY info.CHANNELID ");
//        fieldList.add(ClientStatusConst.IS_COME_SHOP);
        Object[] objects = fieldList.toArray();

        List<Map<String, Object>> list = jdbcTemplate.queryForList(sb.toString(), objects);
        List<NumVO> rstList = new LinkedList<NumVO>();
        NumVO numVO = null;
        for(Map<String,Object> map : list){
            numVO = new NumVO();
            numVO.setChannelId(((Long)map.get("CHANNELID")).intValue());
            numVO.setRdNum(((Long)map.get("RDNUM")).intValue());
            rstList.add(numVO);
        }
        // 毛客资归类到渠道
        for (ChannelVO channelVO : channelList) {
            for (NumVO rdNum : rstList) {
                if (channelVO.getChannelId() == rdNum.getChannelId()) {
                    channelVO.getNumVO().setRdNum(rdNum.getRdNum());
                    break;
                }
            }
        }

    }

    /**
     * 统计电商成交客资数量并归类到渠道
     *
     * @param vo
     * @return
     * @
     */
    public void getCjNumList(AnalyzeVO vo, List<ChannelVO> channelList)  {

        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT info.CHANNELID , COUNT( 1 ) CJNUM, SUM(det.AMOUNT) YY, SUM(det.STAYAMOUNT) YS, AVG(det.AMOUNT) JJ FROM ");
        sb.append(DBSplitUtil.getInfoTabName(vo.getCompanyId()));
        sb.append(" info INNER JOIN ");
        sb.append(DBSplitUtil.getDetailTabName(vo.getCompanyId()));
        sb.append(" det ON det.KZID = info.KZID AND det.COMPANYID = info.COMPANYID ");
        sb.append(" WHERE info.ISDEL = 0  AND info.SRCTYPE IN ( 1, 2 ) ");
        sb.append(" AND info.SUCCESSTIME >= ? AND info.SUCCESSTIME <= ?  AND info.COMPANYID = ? ");
        sb.append(" AND INSTR( ?, CONCAT(',',info.STATUSID + '',',')) != 0");
        LinkedList<Object> fieldList = new LinkedList<>();
        fieldList.add(vo.getStart());
        fieldList.add(vo.getEnd());
        fieldList.add(vo.getCompanyId());
        fieldList.add(ClientStatusConst.IS_SUCCESS);
        if (StringUtils.isNotEmpty(vo.getChannelIds())) {
            sb.append(" AND INSTR( ? ,CONCAT(',',info.CHANNELID+'',',')) ");
            fieldList.add(CommonConstant.STR_SEPARATOR + vo.getChannelIds() + CommonConstant.STR_SEPARATOR);
        }
        getCollectorLimitSql(sb, vo);
        sb.append(" GROUP BY info.CHANNELID ");
        Object[] objects = fieldList.toArray();
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sb.toString(), objects);
        List<NumVO> rstList = new LinkedList<NumVO>();
        NumVO numVO = null;
        for(Map<String,Object> map : list){
            numVO = new NumVO();
            numVO.setChannelId(((Long)map.get("CHANNELID")).intValue());
            numVO.setCjNum(((Long)map.get("CJNUM")).intValue());
            numVO.setYyAmount(((BigDecimal)map.get("YY")).doubleValue());
            numVO.setHaveAmount(((BigDecimal)map.get("YS")).doubleValue());
            numVO.setJjAmount(((BigDecimal)map.get("JJ")).doubleValue());
            rstList.add(numVO);
        }
        // 毛客资归类到渠道
        for (ChannelVO channelVO : channelList) {
            for (NumVO cjNum : rstList) {
                if (channelVO.getChannelId() == cjNum.getChannelId()) {
                    channelVO.getNumVO().setCjNum(cjNum.getCjNum());
                    channelVO.getNumVO().setYyAmount(cjNum.getYyAmount());
                    channelVO.getNumVO().setHaveAmount(cjNum.getHaveAmount());
                    channelVO.getNumVO().setJjAmount(cjNum.getJjAmount());
                    break;
                }
            }
        }
    }
    //到店成家量
    public void getRdCjNumList(AnalyzeVO vo, List<ChannelVO> channelList)  {

        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT info.CHANNELID , COUNT( 1 ) CJNUM  FROM ");
        sb.append(DBSplitUtil.getInfoTabName(vo.getCompanyId()));
        sb.append(" info INNER JOIN ");
        sb.append(DBSplitUtil.getDetailTabName(vo.getCompanyId()));
        sb.append(" det ON det.KZID = info.KZID AND det.COMPANYID = info.COMPANYID ");
        sb.append(" WHERE info.ISDEL = 0  AND info.SRCTYPE IN ( 1, 2 ) ");
        sb.append(" AND info.SUCCESSTIME >= ? AND info.SUCCESSTIME <= ?  AND info.COMPANYID = ? and info.statusid in (9,30) ");
        sb.append(" AND INSTR( ?, CONCAT(',',info.STATUSID + '',',')) != 0");
        LinkedList<Object> fieldList = new LinkedList<>();
        fieldList.add(vo.getStart());
        fieldList.add(vo.getEnd());
        fieldList.add(vo.getCompanyId());
        fieldList.add(ClientStatusConst.IS_SUCCESS);
        if (StringUtils.isNotEmpty(vo.getChannelIds())) {
            sb.append(" AND INSTR( ? ,CONCAT(',',info.CHANNELID+'',',')) ");
            fieldList.add(CommonConstant.STR_SEPARATOR + vo.getChannelIds() + CommonConstant.STR_SEPARATOR);
        }
        getCollectorLimitSql(sb, vo);
        sb.append(" GROUP BY info.CHANNELID ");
        Object[] objects = fieldList.toArray();
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sb.toString(), objects);
        List<NumVO> rstList = new LinkedList<NumVO>();
        NumVO numVO = null;
        for(Map<String,Object> map : list){
            numVO = new NumVO();
            numVO.setChannelId(((Long)map.get("CHANNELID")).intValue());
            numVO.setRdCjNum(((Long)map.get("CJNUM")).intValue());
            rstList.add(numVO);
        }
        // 毛客资归类到渠道
        for (ChannelVO channelVO : channelList) {
            for (NumVO cjNum : rstList) {
                if (channelVO.getChannelId() == cjNum.getChannelId()) {
                    channelVO.getNumVO().setRdCjNum(cjNum.getRdCjNum());
                    break;
                }
            }
        }
    }
    //到店成家量
    public void getzxCjNumList(AnalyzeVO vo, List<ChannelVO> channelList)  {

        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT info.CHANNELID , COUNT( 1 ) CJNUM  FROM ");
        sb.append(DBSplitUtil.getInfoTabName(vo.getCompanyId()));
        sb.append(" info INNER JOIN ");
        sb.append(DBSplitUtil.getDetailTabName(vo.getCompanyId()));
        sb.append(" det ON det.KZID = info.KZID AND det.COMPANYID = info.COMPANYID ");
        sb.append(" WHERE info.ISDEL = 0  AND info.SRCTYPE IN ( 1, 2 ) ");
        sb.append(" AND info.SUCCESSTIME >= ? AND info.SUCCESSTIME <= ?  AND info.COMPANYID = ? and info.statusid=40");
        sb.append(" AND INSTR( ?, CONCAT(',',info.STATUSID + '',',')) != 0");
        LinkedList<Object>  fieldList = new LinkedList<>();
        fieldList.add(vo.getStart());
        fieldList.add(vo.getEnd());
        fieldList.add(vo.getCompanyId());
        fieldList.add(ClientStatusConst.IS_SUCCESS);
        if (StringUtils.isNotEmpty(vo.getChannelIds())) {
            sb.append(" AND INSTR( ? ,CONCAT(',',info.CHANNELID+'',',')) ");
            fieldList.add(CommonConstant.STR_SEPARATOR + vo.getChannelIds() + CommonConstant.STR_SEPARATOR);
        }
        getCollectorLimitSql(sb, vo);
        sb.append(" GROUP BY info.CHANNELID ");
        Object[] objects = fieldList.toArray();
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sb.toString(), objects);
        List<NumVO> rstList = new LinkedList<NumVO>();
        NumVO numVO = null;
        for(Map<String,Object> map : list){
            numVO = new NumVO();
            numVO.setChannelId(((Long) map.get("CHANNELID")).intValue());
            numVO.setZxCjNum(((Long) map.get("CJNUM")).intValue());
            rstList.add(numVO);
        }
        // 毛客资归类到渠道
        for (ChannelVO channelVO : channelList) {
            for (NumVO cjNum : rstList) {
                if (channelVO.getChannelId() == cjNum.getChannelId()) {
                    channelVO.getNumVO().setZxCjNum(cjNum.getZxCjNum());
                    break;
                }
            }
        }
    }
    /**
     * 统计本月录入，本月成交的客资量
     *
     * @param vo
     * @return
     * @
     */
    public void getMonthSuccess(AnalyzeVO vo, List<ChannelVO> channelList)  {

        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT info.CHANNELID , COUNT( 1 ) MSNUM  FROM ");
        sb.append(DBSplitUtil.getInfoTabName(vo.getCompanyId()));
        sb.append(" info WHERE info.ISDEL = 0  AND info.SRCTYPE IN ( 1, 2 ) ");
        sb.append(" AND info.SUCCESSTIME >= ? AND info.SUCCESSTIME <= ?  AND info.COMPANYID = ? ");
        sb.append(" AND info.CREATETIME >= ? AND info.CREATETIME <= ?  ");
        sb.append(" AND INSTR( ?, CONCAT(',',info.STATUSID + '',',')) != 0 ");
        LinkedList<Object> fieldList = new LinkedList<>();
        fieldList.add(vo.getStart());
        fieldList.add(vo.getEnd());
        fieldList.add(vo.getCompanyId());
        fieldList.add(vo.getStart());
        fieldList.add(vo.getEnd());
        fieldList.add(ClientStatusConst.IS_SUCCESS);
        if (StringUtils.isNotEmpty(vo.getChannelIds())) {
            sb.append(" AND INSTR( ? ,CONCAT(',',info.CHANNELID+'',',')) ");
            fieldList.add(CommonConstant.STR_SEPARATOR + vo.getChannelIds() + CommonConstant.STR_SEPARATOR);
        }
        getCollectorLimitSql(sb, vo);
        sb.append(" GROUP BY info.CHANNELID ");

        Object[] objects = fieldList.toArray();
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sb.toString(), objects);
        List<NumVO> rstList = new LinkedList<NumVO>();
        NumVO numVO = null;
        for(Map<String,Object> map : list){
            numVO = new NumVO();
            numVO.setChannelId(((Long)map.get("CHANNELID")).intValue());
            numVO.setMsNum(((Long)map.get("MSNUM")).intValue());
            rstList.add(numVO);
        }
        // 毛客资归类到渠道
        for (ChannelVO channelVO : channelList) {
            for (NumVO msNum : rstList) {
                if (channelVO.getChannelId() == msNum.getChannelId()) {
                    channelVO.getNumVO().setMsNum(msNum.getMsNum());
                    break;
                }
            }
        }
    }

    // 获取渠道花费
    private void getSrcCost(AnalyzeVO vo, List<ChannelVO> channelList)  {

        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT channel.ID, SUM( cost.COST ) COST  FROM hm_crm_cost cost ");
        sb.append(" LEFT JOIN hm_crm_source src ON cost.SRCID = src.ID AND cost.COMPANYID = src.COMPANYID ");
        sb.append(" LEFT JOIN hm_crm_channel channel ON channel.ID = src.CHANNELID AND src.COMPANYID = channel.COMPANYID ");
        sb.append(" WHERE cost.COSTTIME >= ?  AND cost.COSTTIME <= ?  AND cost.COMPANYID = ? AND channel.TYPEID IN ( 1, 2 ) ");
        LinkedList<Object> fieldList = new LinkedList<>();
        fieldList.add(vo.getStart());
        fieldList.add(vo.getEnd());
        fieldList.add(vo.getCompanyId());

        if (StringUtils.isNotEmpty(vo.getChannelIds())) {
            sb.append(" AND INSTR( ? ,CONCAT(',',channel.ID+'',',')) ");
            fieldList.add(CommonConstant.STR_SEPARATOR + vo.getChannelIds() + CommonConstant.STR_SEPARATOR);
        }
        sb.append(" GROUP BY channel.ID ");

        Object[] objects = fieldList.toArray();
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sb.toString(), objects);
        List<NumVO> rstList = new LinkedList<NumVO>();
        NumVO numVO = null;
        for(Map<String,Object> map : list){
            numVO = new NumVO();
            numVO.setChannelId(((Long)map.get("ID")).intValue());
            numVO.setAmount(((BigDecimal)map.get("COST")).doubleValue());
            rstList.add(numVO);
        }
        // 毛客资归类到渠道
        for (ChannelVO channelVO : channelList) {
            for (NumVO amount : rstList) {
                if (channelVO.getChannelId() == amount.getChannelId()) {
                    channelVO.getNumVO().setAmount(amount.getAmount());
                    break;
                }
            }
        }
    }


    /**
     * 计算渠道合计
     *
     * @param vo
     * @return
     */
    private List<ChannelVO> getSrcTotal(AnalyzeVO vo, List<ChannelVO> channelList) {
        ChannelVO channelTotal = new ChannelVO("total", vo.getCompanyId());
        NumVO numTotal = new NumVO("total");
        int numAll = 0;
        int kzNum = 0;
        int wxNum = 0;
        int yxNum = 0;
        int ddNum = 0;
        int rdNum = 0;
        int cjNum = 0;
        int msNum = 0;
        int rdCjNum= 0;
        int zxCjNum=0;
        double amount = 0.0;
        double yyAmount = 0.0;
        double haveAmount = 0.0;
        for (ChannelVO channelVO : channelList) {
            numAll += channelVO.getNumVO().getNumAll();
            kzNum += channelVO.getNumVO().getKzNum();
            yxNum += channelVO.getNumVO().getYxNum();
            wxNum += channelVO.getNumVO().getWxNum();
            ddNum += channelVO.getNumVO().getDdNum();
            rdNum += channelVO.getNumVO().getRdNum();
            cjNum += channelVO.getNumVO().getCjNum();
            msNum += channelVO.getNumVO().getMsNum();
            rdCjNum+=channelVO.getNumVO().getRdCjNum();
            zxCjNum+=channelVO.getNumVO().getZxCjNum();
            amount += Double.valueOf(channelVO.getNumVO().getAmount());
            yyAmount += Double.valueOf(channelVO.getNumVO().getYyAmount());
            haveAmount += Double.valueOf(channelVO.getNumVO().getHaveAmount());
        }
        numTotal.setNumAll(numAll);
        numTotal.setKzNum(kzNum);
        numTotal.setYxNum(yxNum);
        numTotal.setWxNum(wxNum);
        numTotal.setDdNum(ddNum);
        numTotal.setRdNum(rdNum);
        numTotal.setCjNum(cjNum);
        numTotal.setZxCjNum(zxCjNum);
        numTotal.setRdCjNum(rdCjNum);
        numTotal.setAmount(StringUtil.decimalFormatDouble(amount));
        numTotal.setMsNum(msNum);
        numTotal.setYyAmount(yyAmount);
        numTotal.setHaveAmount(haveAmount);
        channelTotal.setNumVO(numTotal);
        channelList.add(0, channelTotal);
        return channelList;
    }

    // 计算率和成本
    private void getRateAndCost(List<ChannelVO> channelList) {

        for (ChannelVO channelVO : channelList) {
            // 有效率
            if (channelVO.getNumVO().getKzNum() != 0) {
                channelVO.getNumVO().setYxRate(StringUtil.decimalFormat(channelVO.getNumVO().getYxNum() / Double.valueOf(channelVO.getNumVO().getKzNum()) * 100));
            }
            // 无效率
            if (channelVO.getNumVO().getKzNum() != 0) {
                channelVO.getNumVO()
                        .setWxRate(StringUtil.decimalFormat(channelVO.getNumVO().getWxNum() / Double.valueOf(channelVO.getNumVO().getKzNum()) * 100));
            }
            // 待定率
            if (channelVO.getNumVO().getKzNum() != 0) {
                channelVO.getNumVO()
                        .setDdRate(StringUtil.decimalFormat(channelVO.getNumVO().getDdNum() / Double.valueOf(channelVO.getNumVO().getKzNum()) * 100));
            }
            // 毛客资咨询入店率
            if (channelVO.getNumVO().getKzNum() != 0) {
                channelVO.getNumVO()
                        .setGrossRdRate(StringUtil.decimalFormat(channelVO.getNumVO().getRdNum() / Double.valueOf(channelVO.getNumVO().getKzNum()) * 100));
            }
            // 有效客资咨询入店率
            if (channelVO.getNumVO().getYxNum() != 0) {
                channelVO.getNumVO()
                        .setRdRate(StringUtil.decimalFormat(channelVO.getNumVO().getRdNum() / Double.valueOf(channelVO.getNumVO().getYxNum()) * 100));
            }
            // 入店成交率
            if (channelVO.getNumVO().getRdNum() != 0) {
                channelVO.getNumVO()
                        .setCjRate(StringUtil.decimalFormat(channelVO.getNumVO().getCjNum() / Double.valueOf(channelVO.getNumVO().getRdNum()) * 100));
            }
            // 毛客资成交率
            if (channelVO.getNumVO().getKzNum() != 0) {
                channelVO.getNumVO()
                        .setGrossCjRate(StringUtil.decimalFormat(channelVO.getNumVO().getCjNum() / Double.valueOf(channelVO.getNumVO().getKzNum()) * 100));
            }
            // 有效客资成交率
            if (channelVO.getNumVO().getYxNum() != 0) {
                channelVO.getNumVO()
                        .setYxCjRate(StringUtil.decimalFormat(channelVO.getNumVO().getCjNum() / Double.valueOf(channelVO.getNumVO().getYxNum()) * 100));
            }
            // 毛客资成本
            if (channelVO.getNumVO().getKzNum() != 0) {
                channelVO.getNumVO().setGrossCost(StringUtil.decimalFormatDouble(channelVO.getNumVO().getAmount() / channelVO.getNumVO().getKzNum()));
            }
            // 有效客资成本
            if (channelVO.getNumVO().getYxNum() != 0) {
                channelVO.getNumVO().setYxCost(StringUtil.decimalFormatDouble(channelVO.getNumVO().getAmount() / channelVO.getNumVO().getYxNum()));
            }
            // 均价
            if (channelVO.getNumVO().getCjNum() != 0) {
                channelVO.getNumVO().setJjAmount(StringUtil.decimalFormatDouble(channelVO.getNumVO().getYyAmount() / channelVO.getNumVO().getCjNum()));
            }
            // 入店成本
            if (channelVO.getNumVO().getRdNum() != 0) {
                channelVO.getNumVO().setRdCost(StringUtil.decimalFormatDouble(channelVO.getNumVO().getAmount() / channelVO.getNumVO().getRdNum()));
            }
            // 成交成本
            if (channelVO.getNumVO().getCjNum() != 0) {
                channelVO.getNumVO().setCjCost(StringUtil.decimalFormatDouble(channelVO.getNumVO().getAmount() / channelVO.getNumVO().getCjNum()));
            }
            // 投入产出
            if (channelVO.getNumVO().getYyAmount() != null && channelVO.getNumVO().getYyAmount() != 0.00
                    && channelVO.getNumVO().getAmount() != null && channelVO.getNumVO().getAmount() != 0.00) {
                channelVO.getNumVO().setRoi(StringUtil.decimalFormat(
                        Double.valueOf(channelVO.getNumVO().getYyAmount()) / Double.valueOf(channelVO.getNumVO().getAmount())));
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
