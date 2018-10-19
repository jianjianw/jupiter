package com.qiein.jupiter.web.repository;

import com.graphbuilder.math.func.LnFunction;
import com.qiein.jupiter.constant.DictionaryConstant;
import com.qiein.jupiter.util.DBSplitUtil;
import com.qiein.jupiter.web.entity.dto.ReportParamDTO;
import com.qiein.jupiter.web.entity.vo.ReportsParamVO;
import com.qiein.jupiter.web.entity.vo.ZjsClientDetailReportVO;
import com.qiein.jupiter.web.entity.vo.ZjskzOfMonthReportsVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ZjsDetailReportDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 转介绍报表详情，按客服组汇总
     *
     */
    public Map<String,Object> getZjsDetailReportByGroup(ReportsParamVO reportsParamVO){
        List<ZjsClientDetailReportVO> reportVOS = new ArrayList<ZjsClientDetailReportVO>();



        //封装毛客资
        getTotalClientCount(reportsParamVO,reportVOS);

        //封装A类客资数
        getClientSourceLevelACount(reportsParamVO,reportVOS,DictionaryConstant.YX_LEVEL_A);
        //封装A类客资进店数
        getClientSourceLevelAInShopCount(reportsParamVO,reportVOS,DictionaryConstant.YX_LEVEL_A);
        //计算A类客资转化率

        //封装B类客资数
        getClientSourceLevelACount(reportsParamVO,reportVOS,DictionaryConstant.YX_LEVEL_B);
        //封装B类客资进店数
        getClientSourceLevelAInShopCount(reportsParamVO,reportVOS,DictionaryConstant.YX_LEVEL_B);
        //计算B类客资转化率

        //封装C类客资数
        getClientSourceLevelACount(reportsParamVO,reportVOS,DictionaryConstant.YX_LEVEL_C);
        //封装C类客资进店数
        getClientSourceLevelAInShopCount(reportsParamVO,reportVOS,DictionaryConstant.YX_LEVEL_C);
        //计算C类客资转化率

        //封装D类客资数
        getClientSourceLevelACount(reportsParamVO,reportVOS,DictionaryConstant.YX_LEVEL_D);

        //计算有效客资数（等于A类客资+B类客资）
        getValidClientCount(reportVOS);
















        //获取筛选待定98
       // getFilterWaitClientCount();
        //获取筛选中 0
        //获取筛选无效 99
        //jdbcTemplate.queryForList(sql);
        System.out.println();
        return null;

    }

    //计算有效客资数（A类客资+B 类客资）
    private void getValidClientCount(List<ZjsClientDetailReportVO> reportVOS) {
        for (ZjsClientDetailReportVO reportVO:reportVOS) {
            int levelACount = reportVO.getClientSourceLevelACount();
            int levelBCount = reportVO.getClientSourceLevelBCount();
            reportVO.setValidClientSourceCount(levelACount+levelBCount);
        }
    }

    // 获取毛客资
    private void getTotalClientCount(ReportsParamVO reportsParamVO,List<ZjsClientDetailReportVO> reportVOS ){

        String infoTabName = DBSplitUtil.getInfoTabName(reportsParamVO.getCompanyId());
        StringBuilder sb = new StringBuilder();
        sb.append("select GROUPID groupId,count(KZID) totalCount, ");
        sb.append("count(case when STATUSID = 98 then KZID else NULL end) filterWaitCount, ");
        sb.append("count(case when STATUSID = 0 then KZID else NULL end) filterInCount, ");
        sb.append("count(case when STATUSID = 99 then KZID else NULL end) filterInvalidCount ");
        sb.append("from ").append(infoTabName);
        sb.append("where companyId = ? ");
        sb.append("and CREATETIME BETWEEN ? AND ? ");
        sb.append("and ISDEL = 0 and GROUPID is not null ");
        sb.append("group by GROUPID ");
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sb.toString(), reportsParamVO.getCompanyId(),
                reportsParamVO.getStart(), reportsParamVO.getEnd());
        ZjsClientDetailReportVO reportVO;
        for (Map<String, Object> map:list) {
            reportVO = new ZjsClientDetailReportVO();
            Long totalCount = (Long) map.get("totalCount");
            reportVO.setId((String)map.get("groupId"));
            Long filterWaitCount = (Long) map.get("filterWaitCount");
            Long filterInCount = (Long) map.get("filterInCount");
            Long filterInvalidCount = (Long) map.get("filterInvalidCount");
            Long kzCount = totalCount - filterWaitCount - filterInCount - filterInvalidCount;//毛客资
            reportVO.setClientSourceCount(kzCount.intValue());
            reportVOS.add(reportVO);
        }
        System.out.println();
    }

    //获取等级客资数(A,B,C,D)
    private void getClientSourceLevelACount(ReportsParamVO reportsParamVO,List<ZjsClientDetailReportVO> reportVOS,String dicName ){

        String infoTabName = DBSplitUtil.getInfoTabName(reportsParamVO.getCompanyId());
        String detailTabName = DBSplitUtil.getDetailTabName(reportsParamVO.getCompanyId());
        StringBuilder sb = new StringBuilder();

        sb.append("select info.GROUPID groupId, count(info.KZID)  totalCount, ");
        sb.append("count(case when info.STATUSID = 98 then info.KZID else NULL end) filterWaitCount, ");
        sb.append("count(case when info.STATUSID = 0 then info.KZID else NULL end) filterInCount, ");
        sb.append("count(case when info.STATUSID = 99 then info.KZID else NULL end) filterInvalidCount ");
        sb.append("from (").append(infoTabName).append("info inner join ").append(detailTabName).append("detail on info.ID = detail.ID ) ");
        sb.append("inner join hm_crm_dictionary dic on detail.YXLEVEL = dic.DICCODE ");
        sb.append("where dic.COMPANYID = ? and dic.DICTYPE = 'yx_level' and dic.DICNAME = '"+dicName+"' ");
        sb.append("and info.CREATETIME BETWEEN ? AND ? ");
        sb.append("and info.ISDEL = 0 and info.GROUPID is not null ");
        sb.append("group by info.GROUPID ");

        List<Map<String, Object>> list = jdbcTemplate.queryForList(sb.toString(), reportsParamVO.getCompanyId(),
                reportsParamVO.getStart(), reportsParamVO.getEnd());

        for (Map<String, Object> map:list) {
            for (ZjsClientDetailReportVO reportVo :reportVOS){
                String id = reportVo.getId();
                String groupId = (String)map.get("groupId");
                if(StringUtils.equals(id,groupId)){
                    Long totalCount = (Long)map.get("totalCount");
                    Long filterWaitCount = (Long) map.get("filterWaitCount");
                    Long filterInCount = (Long) map.get("filterInCount");
                    Long filterInvalidCount = (Long) map.get("filterInvalidCount");
                    Long levelCount = totalCount - filterWaitCount - filterInCount - filterInvalidCount;//等级客资数
                    switch (dicName) {
                        case DictionaryConstant.YX_LEVEL_A:
                            reportVo.setClientSourceLevelACount(levelCount.intValue());
                            break;
                        case DictionaryConstant.YX_LEVEL_B:
                            reportVo.setClientSourceLevelBCount(levelCount.intValue());
                            break;
                        case DictionaryConstant.YX_LEVEL_C:
                            reportVo.setClientSourceLevelCCount(levelCount.intValue());
                            break;
                        case DictionaryConstant.YX_LEVEL_D:
                            reportVo.setClientSourceLevelDCount(levelCount.intValue());
                            break;
                        default:
                            break;
                    }
                    reportVOS.add(reportVo);
                }
            }
        }
        System.out.println();

    }
    //封装客资进店数 和 客资转化率(A,B,C)
    private void getClientSourceLevelAInShopCount(ReportsParamVO reportsParamVO,List<ZjsClientDetailReportVO> reportVOS, String dicName){
        String infoTabName = DBSplitUtil.getInfoTabName(reportsParamVO.getCompanyId());
        String detailTabName = DBSplitUtil.getDetailTabName(reportsParamVO.getCompanyId());
        StringBuilder sb = new StringBuilder();

        sb.append("select info.GROUPID groupId, count(info.KZID)  totalCount, ");
        sb.append("count(case when info.STATUSID = 98 then info.KZID else NULL end) filterWaitCount, ");
        sb.append("count(case when info.STATUSID = 0 then info.KZID else NULL end) filterInCount, ");
        sb.append("count(case when info.STATUSID = 99 then info.KZID else NULL end) filterInvalidCount ");
        sb.append("from (").append(infoTabName).append("info inner join ").append(detailTabName).append("detail on info.ID = detail.ID ) ");
        sb.append("inner join hm_crm_dictionary dic on detail.YXLEVEL = dic.DICCODE ");
        sb.append("where dic.COMPANYID = ? and dic.DICTYPE = 'yx_level' and dic.DICNAME = '"+dicName+"' ");
        sb.append("and info.COMESHOPTIME BETWEEN ? AND ? ");
        sb.append("and info.ISDEL = 0 and info.GROUPID is not null ");
        sb.append("group by info.GROUPID ");

        List<Map<String, Object>> list = jdbcTemplate.queryForList(sb.toString(), reportsParamVO.getCompanyId(),
                reportsParamVO.getStart(), reportsParamVO.getEnd());


        for (Map<String, Object> map:list) {
            for (ZjsClientDetailReportVO reportVo :reportVOS){
                String id = reportVo.getId();
                String groupId = (String)map.get("groupId");
                if(StringUtils.equals(id,groupId)){
                    Long totalCount = (Long)map.get("totalCount");
                    Long filterWaitCount = (Long) map.get("filterWaitCount");
                    Long filterInCount = (Long) map.get("filterInCount");
                    Long filterInvalidCount = (Long) map.get("filterInvalidCount");
                    Long levelCount = totalCount - filterWaitCount - filterInCount - filterInvalidCount;//客资进店数
                    switch (dicName) {
                        case DictionaryConstant.YX_LEVEL_A:
                            reportVo.setClientSourceLevelAInShopCount(levelCount.intValue());
                            break;
                        case DictionaryConstant.YX_LEVEL_B:
                            reportVo.setClientSourceLevelBInShopCount(levelCount.intValue());
                            break;
                        case DictionaryConstant.YX_LEVEL_C:
                            reportVo.setClientSourceLevelCInShopCount(levelCount.intValue());
                            break;
                        default:
                            break;
                    }
                    reportVo.setClientSourceLevelAInShopCount(levelCount.intValue());
                    reportVOS.add(reportVo);
                }
            }
        }
        //计算客资转化率
        convertRate(reportVOS,dicName);
    }


    //计算客资转化率（进店/客资）
    private void convertRate(List<ZjsClientDetailReportVO> reportVOS,String dicName){
        int inShopCount;
        int count;
        for (ZjsClientDetailReportVO reportVO: reportVOS) {

            switch (dicName) {
                case DictionaryConstant.YX_LEVEL_A:
                    inShopCount = reportVO.getClientSourceLevelAInShopCount();
                    count = reportVO.getClientSourceLevelACount();
                    if(count == 0){
                        reportVO.setClientSourceLevelARate(0);
                    }else {
                        reportVO.setClientSourceLevelARate(inShopCount/count);
                    }
                    break;
                case DictionaryConstant.YX_LEVEL_B:
                    inShopCount = reportVO.getClientSourceLevelBInShopCount();
                    count = reportVO.getClientSourceLevelBCount();
                    if(count == 0){
                        reportVO.setClientSourceLevelBRate(0);
                    }else {
                        reportVO.setClientSourceLevelBRate(inShopCount/count);
                    }
                    break;
                case DictionaryConstant.YX_LEVEL_C:
                    inShopCount = reportVO.getClientSourceLevelAInShopCount();
                    count = reportVO.getClientSourceLevelACount();
                    if(count == 0){
                        reportVO.setClientSourceLevelCRate(0);
                    }else {
                        reportVO.setClientSourceLevelCRate(inShopCount/count);
                    }
                    break;
                default:
                    break;
            }

        }
    }











}
