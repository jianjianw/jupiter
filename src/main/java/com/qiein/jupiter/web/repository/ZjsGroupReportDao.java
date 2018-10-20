package com.qiein.jupiter.web.repository;



import com.qiein.jupiter.constant.DictionaryConstant;
import com.qiein.jupiter.util.DBSplitUtil;
import com.qiein.jupiter.util.StringUtil;
import com.qiein.jupiter.web.entity.vo.DsInvalidVO;
import com.qiein.jupiter.web.entity.vo.ReportsParamVO;
import com.qiein.jupiter.web.entity.vo.ZjsClientDetailReportVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.*;

@Repository
public class ZjsGroupReportDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private CommonReportsDao commonReportsDao;

    /**
     * 转介绍报表详情，按客服组汇总
     *
     */
    public List<ZjsClientDetailReportVO> getZjsGroupReport(ReportsParamVO reportsParamVO){
        List<ZjsClientDetailReportVO> reportVOS = new ArrayList<ZjsClientDetailReportVO>();



        //查询表头（意向登记）
        Map<String, String> tableHead = getTableHead(reportsParamVO);
        //可以根据查出来的意向等级动态的显示
       /* Set<String> sets = tableHead.keySet();
        for (int i = 0; i < tableHead.size(); i++) {
            for (String string: sets) {
                String s = tableHead.get(string);

            }
        }*/
        /*Set<Map.Entry<String, String>> entries = tableHead.entrySet();
        Iterator<Map.Entry<String, String>> iterator = entries.iterator();
        while (iterator.hasNext()){
            Map.Entry<String, String> next = iterator.next();
            String key = next.getKey();
            String value = next.getValue();
        }*/

        //获取毛客资
        getTotalClientCount(reportsParamVO,reportVOS);

        //获取A类客资数
        getClientSourceLevelACount(reportsParamVO,reportVOS,DictionaryConstant.YX_LEVEL_A);
        //获取A类客资进店数
        getClientSourceLevelAInShopCount(reportsParamVO,reportVOS,DictionaryConstant.YX_LEVEL_A);
        //获取A类客资转化率

        //获取B类客资数
        getClientSourceLevelACount(reportsParamVO,reportVOS,DictionaryConstant.YX_LEVEL_B);
        //获取B类客资进店数
        getClientSourceLevelAInShopCount(reportsParamVO,reportVOS,DictionaryConstant.YX_LEVEL_B);
        //计算B类客资转化率

        //获取C类客资数
        getClientSourceLevelACount(reportsParamVO,reportVOS,DictionaryConstant.YX_LEVEL_C);
        //获取C类客资进店数
        getClientSourceLevelAInShopCount(reportsParamVO,reportVOS,DictionaryConstant.YX_LEVEL_C);
        //获取C类客资转化率

        //获取D类客资数
        getClientSourceLevelACount(reportsParamVO,reportVOS,DictionaryConstant.YX_LEVEL_D);

        //获取有效客资数（等于A类客资+B类客资）
        getValidClientCount(reportVOS);

        //无效数  查询有效客资数的其余客资
        getInvalidClientSourceCount(reportsParamVO,reportVOS);

        //获取总进店数
        getTotalInShopCount(reportsParamVO,reportVOS);
        //总成交数
        getTotalSuccessCount(reportsParamVO,reportVOS);
        //总成交率
        getTotalSuccessRate(reportVOS);

        //毛客资进店率（总进店/毛客资数）
        getClientInShopRate(reportVOS);
        //有效客资进店率（总进店/ 有效客资）
        getValidClientInShopRate(reportVOS);

        //周末进店数
        getWeekendInShopCount(reportsParamVO,reportVOS);
        //非周末进店数 and 非周末进店占比
        getUnWeekendInShopCount(reportsParamVO,reportVOS);

        //周末成交数
        getWeekendSuccessCount(reportsParamVO,reportVOS);
        //非周末成交数  and  周末成交率  and  非周末成交率
        unWeekendSuccessCount(reportsParamVO,reportVOS);

        //总金额 and 均价
        getAmount(reportsParamVO,reportVOS);
        //获取客服组名称
        getGroupNane(reportsParamVO,reportVOS);


        return reportVOS;

    }

    private Map<String,String> getTableHead(ReportsParamVO reportsParamVO) {

        StringBuilder sb = new StringBuilder();
        sb.append("select dic.DICCODE code ,dic.DICNAME name from (hm_crm_client_info info inner join hm_crm_client_detail detail ");
        sb.append("on info.KZID = detail.KZID) ");
        sb.append("inner join hm_crm_dictionary dic on dic.diccode = detail.yxlevel ");
        sb.append("where dic.dictype = 'yx_level' and dic.companyID = ? ");

        List<Map<String, Object>> list = jdbcTemplate.queryForList(sb.toString(), reportsParamVO.getCompanyId());//得到公司的的所有意向等级

        Map<String,String> tableHeads = new HashMap<>();
        for (Map<String, Object> map : list) {
            String code = (String) map.get("code");
            String name = (String) map.get("name");
            tableHeads.put(code,name);


        }
        return tableHeads;
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
        sb.append("where SRCTYPE in(3, 4, 5) and COMPANYID = ? ");
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
        sb.append("from (").append(infoTabName).append("info inner join ").append(detailTabName).append("detail on info.KZID = detail.KZID ) ");
        sb.append("inner join hm_crm_dictionary dic on detail.YXLEVEL = dic.DICCODE ");
        sb.append("where info.SRCTYPE in(3, 4, 5) and dic.COMPANYID = ? and dic.DICTYPE = 'yx_level' and dic.DICNAME = '"+dicName+"' ");
        sb.append("and info.CREATETIME BETWEEN ? AND ? ");
        sb.append("and info.ISDEL = 0 and info.GROUPID is not null ");
        sb.append("group by info.GROUPID ");

        List<Map<String, Object>> list = jdbcTemplate.queryForList(sb.toString(), reportsParamVO.getCompanyId(),
                reportsParamVO.getStart(), reportsParamVO.getEnd());

        for (Map<String, Object> map:list) {
            String groupId = (String)map.get("groupId");
            for (ZjsClientDetailReportVO reportVo :reportVOS){
                String id = reportVo.getId();
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
        sb.append("from (").append(infoTabName).append("info inner join ").append(detailTabName).append("detail on info.KZID = detail.KZID ) ");
        sb.append("inner join hm_crm_dictionary dic on detail.YXLEVEL = dic.DICCODE ");
        sb.append("where info.SRCTYPE in(3, 4, 5) and dic.COMPANYID = ? and dic.DICTYPE = 'yx_level' and dic.DICNAME = '"+dicName+"' ");
        sb.append("and info.COMESHOPTIME BETWEEN ? AND ? ");
        sb.append("and info.ISDEL = 0 and info.GROUPID is not null ");
        sb.append("group by info.GROUPID ");

        List<Map<String, Object>> list = jdbcTemplate.queryForList(sb.toString(), reportsParamVO.getCompanyId(),
                reportsParamVO.getStart(), reportsParamVO.getEnd());


        for (Map<String, Object> map:list) {
            String groupId = (String)map.get("groupId");

            for (ZjsClientDetailReportVO reportVo :reportVOS){
                String id = reportVo.getId();
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
                        reportVO.setClientSourceLevelARate(inShopCount/count*100);
                    }
                    break;
                case DictionaryConstant.YX_LEVEL_B:
                    inShopCount = reportVO.getClientSourceLevelBInShopCount();
                    count = reportVO.getClientSourceLevelBCount();
                    if(count == 0){
                        reportVO.setClientSourceLevelBRate(0);
                    }else {
                        reportVO.setClientSourceLevelBRate(inShopCount/count*100);
                    }
                    break;
                case DictionaryConstant.YX_LEVEL_C:
                    inShopCount = reportVO.getClientSourceLevelAInShopCount();
                    count = reportVO.getClientSourceLevelACount();
                    if(count == 0){
                        reportVO.setClientSourceLevelCRate(0);
                    }else {
                        reportVO.setClientSourceLevelCRate(inShopCount/count*100);
                    }
                    break;
                default:
                    break;
            }

        }
    }

    //计算有效客资数（A类客资+B 类客资）
    private void getValidClientCount(List<ZjsClientDetailReportVO> reportVOS) {
        for (ZjsClientDetailReportVO reportVO:reportVOS) {
            int levelACount = reportVO.getClientSourceLevelACount();
            int levelBCount = reportVO.getClientSourceLevelBCount();
            reportVO.setValidClientSourceCount(levelACount+levelBCount);
        }
    }

    //获取总进店数
    private void getTotalInShopCount(ReportsParamVO reportsParamVO, List<ZjsClientDetailReportVO> reportVOS) {

        String infoTabName = DBSplitUtil.getInfoTabName(reportsParamVO.getCompanyId());
        StringBuilder sb = new StringBuilder();
        sb.append("select GROUPID groupId,count(KZID) totalCount ");
        sb.append("from ").append(infoTabName);
        sb.append("where SRCTYPE in(3, 4, 5) and companyId = ? ");
        sb.append("and COMESHOPTIME BETWEEN ? AND ? ");
        sb.append("and ISDEL = 0 and GROUPID is not null ");
        sb.append("group by GROUPID ");
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sb.toString(), reportsParamVO.getCompanyId(),
                reportsParamVO.getStart(), reportsParamVO.getEnd());

        for (Map<String, Object> map : list ) {
            String groupId = (String)map.get("groupId");

            for (ZjsClientDetailReportVO reportVO : reportVOS) {
                String id = reportVO.getId();
                if(StringUtils.equals(id,groupId)){
                    Long totalCount = (Long)map.get("totalCount");
                    reportVO.setTotalInShopCount(totalCount.intValue());//总进店数
                }
            }
        }
    }

    //总成交数
    private void getTotalSuccessCount(ReportsParamVO reportsParamVO, List<ZjsClientDetailReportVO> reportVOS) {
        String infoTabName = DBSplitUtil.getInfoTabName(reportsParamVO.getCompanyId());
        StringBuilder sb = new StringBuilder();
        sb.append("select GROUPID groupId,count(KZID) totalCount ");
        sb.append("from ").append(infoTabName);
        sb.append("where SRCTYPE in(3, 4, 5) and companyId = ? ");
        sb.append("and SUCCESSTIME BETWEEN ? AND ? ");//总成交数
        sb.append("and ISDEL = 0 and GROUPID is not null ");
        sb.append("group by GROUPID ");
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sb.toString(), reportsParamVO.getCompanyId(),
                reportsParamVO.getStart(), reportsParamVO.getEnd());

        for (Map<String, Object> map : list ) {
            String groupId = (String)map.get("groupId");

            for (ZjsClientDetailReportVO reportVO : reportVOS) {
                String id = reportVO.getId();
                if(StringUtils.equals(id,groupId)){
                    Long totalCount = (Long)map.get("totalCount");
                    reportVO.setTotalSuccessCount(totalCount.intValue());//总成交数
                }
            }
        }

    }

    //总成交率（总成交/总进店）
    private void getTotalSuccessRate(List<ZjsClientDetailReportVO> reportVOS) {

        for (ZjsClientDetailReportVO reportVO : reportVOS) {
            int totalSuccessCount = reportVO.getTotalSuccessCount();//总成交
            int totalInShopCount = reportVO.getTotalInShopCount();//总进店
            if(totalInShopCount == 0){
                reportVO.setTotalSuccessRate(0);
            }else{
                reportVO.setTotalSuccessRate(totalSuccessCount/totalInShopCount*100);//总成交率
            }
        }
    }
    //毛客资进店率（总进店/毛客资数）
    private void getClientInShopRate(List<ZjsClientDetailReportVO> reportVOS) {

        for (ZjsClientDetailReportVO reportVO:reportVOS) {
            int totalInShopCount = reportVO.getTotalInShopCount();//总进店
            int clientSourceCount = reportVO.getClientSourceCount();//毛客资数
            if(clientSourceCount == 0){
                reportVO.setClientInShopRate(0);
            }else{
                reportVO.setClientInShopRate(totalInShopCount/clientSourceCount*100);
            }
        }

    }
    
    
    //有效客资进店率（总进店/ 有效客资）
    private void getValidClientInShopRate(List<ZjsClientDetailReportVO> reportVOS) {
        for (ZjsClientDetailReportVO reportVO : reportVOS ) {
            int totalInShopCount = reportVO.getTotalInShopCount();
            int validClientSourceCount = reportVO.getValidClientSourceCount();
            if(validClientSourceCount == 0){
                reportVO.setValidClientInShopRate(0);
            }else{
                reportVO.setValidClientInShopRate(totalInShopCount/validClientSourceCount*100);
            }
        }
    }

    //总金额 ，均价
    private void getAmount(ReportsParamVO reportsParamVO, List<ZjsClientDetailReportVO> reportVOS) {
        String infoTabName = DBSplitUtil.getInfoTabName(reportsParamVO.getCompanyId());
        String detailTabName = DBSplitUtil.getDetailTabName(reportsParamVO.getCompanyId());
        StringBuilder sb = new StringBuilder();
        sb.append("select info.GROUPID groupId,sum(detail.amount) totalAmount,avg(detail.amount) avgAmount ");
        sb.append("from ").append(infoTabName).append("info inner join ").append(detailTabName).append("detail ");
        sb.append("on info.KZID = detail.KZID ");
        sb.append("where info.SRCTYPE in(3, 4, 5) and info.companyId = ? ");
        sb.append("and info.SUCCESSTIME BETWEEN ? AND ? ");
        sb.append("and info.ISDEL = 0 and info.GROUPID is not null ");
        sb.append("group by info.GROUPID ");
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sb.toString(), reportsParamVO.getCompanyId(),
                reportsParamVO.getStart(), reportsParamVO.getEnd());

        for (Map<String, Object> map : list ) {
            String groupId = (String)map.get("groupId");

            for (ZjsClientDetailReportVO reportVO : reportVOS) {
                String id = reportVO.getId();
                if(StringUtils.equals(id,groupId)){
                    BigDecimal totalAmount = (BigDecimal) map.get("totalAmount");
                    BigDecimal avgAmount = (BigDecimal) map.get("avgAmount");
                    reportVO.setAmount(totalAmount.intValue());//总金额
                    reportVO.setAvgAmount(avgAmount.intValue());//均价
                }
            }
        }
    }

    //周末进店数
    private void getWeekendInShopCount(ReportsParamVO reportsParamVO, List<ZjsClientDetailReportVO> reportVOS) {

        String infoTabName = DBSplitUtil.getInfoTabName(reportsParamVO.getCompanyId());
        StringBuilder sb = new StringBuilder();

        sb.append("select info.GROUPID groupId ,count(info.KZID) weekendCount ");
        sb.append("from ").append(infoTabName).append("info ");
        sb.append("where info.SRCTYPE in(3, 4, 5) and info.companyId = ? ");
        sb.append("and info.COMESHOPTIME BETWEEN ? AND ? ");
        sb.append("and DAYOFWEEK(from_unixtime(info.COMESHOPTIME)) IN (1,7) ");
        sb.append("and info.ISDEL = 0 and info.GROUPID is not null ");
        sb.append("group by info.GROUPID ");

        List<Map<String, Object>> list = jdbcTemplate.queryForList(sb.toString(), reportsParamVO.getCompanyId(),
                reportsParamVO.getStart(), reportsParamVO.getEnd());
        for (Map<String, Object> map:list){
            String groupId = (String)map.get("groupId");
            for(ZjsClientDetailReportVO reportVO :reportVOS ){
                String id = reportVO.getId();
                if(StringUtils.equals(id,groupId)){
                    Long weekendCount = (Long)map.get("weekendCount");
                    reportVO.setWeekendInShopCount(weekendCount.intValue());//周末进店数
                }
            }
        }
    }

    //非周末进店数  +  非周末进店占比
    private void getUnWeekendInShopCount(ReportsParamVO reportsParamVO, List<ZjsClientDetailReportVO> reportVOS) {
        for(ZjsClientDetailReportVO reportVO : reportVOS){
            int totalInShopCount = reportVO.getTotalInShopCount();
            int weekendInShopCount = reportVO.getWeekendInShopCount();
            reportVO.setUnWeekendInShopCount(totalInShopCount-weekendInShopCount);

            //非周末进店占比
            if(totalInShopCount == 0){
                reportVO.setUnWeekendInShopRate(0);
            }else{
                reportVO.setUnWeekendInShopRate(reportVO.getUnWeekendInShopCount()/totalInShopCount*100);
            }


        }
    }

    //周末成交数
    private void getWeekendSuccessCount(ReportsParamVO reportsParamVO, List<ZjsClientDetailReportVO> reportVOS) {

        String infoTabName = DBSplitUtil.getInfoTabName(reportsParamVO.getCompanyId());
        StringBuilder sb = new StringBuilder();

        sb.append("select info.GROUPID groupId ,count(info.KZID) weekendCount ");
        sb.append("from ").append(infoTabName).append("info ");
        sb.append("where info.SRCTYPE in(3, 4, 5) and info.companyId = ? ");
        sb.append("and info.SUCCESSTIME BETWEEN ? AND ? ");
        sb.append("and DAYOFWEEK(from_unixtime(info.SUCCESSTIME)) IN (1,7) ");
        sb.append("and info.ISDEL = 0 and info.GROUPID is not null ");
        sb.append("group by info.GROUPID ");

        List<Map<String, Object>> list = jdbcTemplate.queryForList(sb.toString(), reportsParamVO.getCompanyId(),
                reportsParamVO.getStart(), reportsParamVO.getEnd());
        for (Map<String, Object> map:list){
            String groupId = (String)map.get("groupId");
            for(ZjsClientDetailReportVO reportVO :reportVOS ){
                String id = reportVO.getId();
                if(StringUtils.equals(id,groupId)){
                    Long weekendCount = (Long)map.get("weekendCount");
                    reportVO.setWeekendSuccessCount(weekendCount.intValue());
                }
            }
        }
    }

    //非周末成交数
    private void unWeekendSuccessCount(ReportsParamVO reportsParamVO, List<ZjsClientDetailReportVO> reportVOS) {

        for(ZjsClientDetailReportVO reportVO : reportVOS){
            int totalSuccessCount = reportVO.getTotalSuccessCount();
            int weekendSuccessCount = reportVO.getWeekendSuccessCount();
            //非周末成交数
            reportVO.setUnWeekendSuccessCount(totalSuccessCount - weekendSuccessCount);

          if(totalSuccessCount == 0){
              reportVO.setWeekendSuccessRate(0);
              reportVO.setUnWeekendInShopRate(0);
          } else{
              //周末成交率
              reportVO.setWeekendSuccessRate(weekendSuccessCount/totalSuccessCount*100);
              //非周末成交率
              reportVO.setUnWeekendInShopRate(reportVO.getUnWeekendSuccessCount()/totalSuccessCount*100);
          }
        }

    }


    private void getInvalidClientSourceCount(ReportsParamVO reportsParamVO, List<ZjsClientDetailReportVO> reportVOS) {


        String infoTabName = DBSplitUtil.getInfoTabName(reportsParamVO.getCompanyId());

        DsInvalidVO invalidConfig = commonReportsDao.getInvalidConfig(reportsParamVO.getCompanyId());
        StringBuilder sb = new StringBuilder();
        sb.append("select info.GROUPID groupId, count(info.KZID) invalidCount  ");
        sb.append("from ").append(infoTabName).append("info ");
        sb.append("where info.SRCTYPE in (3, 4, 5) ");
        sb.append("and info.companyId = ? ");
        sb.append("and info.CREATETIME BETWEEN ? AND ? ");
        sb.append("and info.ISDEL = 0 ");
        sb.append("and info.GROUPID is not null ");
        if (StringUtil.isNotEmpty(invalidConfig.getZjsValidStatus())) {
            sb.append(" AND INSTR('" + invalidConfig.getZjsValidStatus() + "',CONCAT( '\"',info.STATUSID,'\"'))=0 ");//找不到返回0
        }
        sb.append("group by info.GROUPID ");

        List<Map<String, Object>> list = jdbcTemplate.queryForList(sb.toString(), reportsParamVO.getCompanyId(),
                reportsParamVO.getStart(), reportsParamVO.getEnd());

        for (Map<String, Object> map : list) {
            String groupId = (String) map.get("groupId");
            for(ZjsClientDetailReportVO reportVO : reportVOS ){
                String id = reportVO.getId();
                if(StringUtils.equals(id,groupId)){
                    Long invalidCount = (Long) map.get("invalidCount");
                    reportVO.setInvalidClientSourceCount(invalidCount.intValue());//无效量
                }
            }

        }

    }


    //获取客服组名称
    private void getGroupNane(ReportsParamVO reportsParamVO, List<ZjsClientDetailReportVO> reportVOS) {

        StringBuilder sb  = new StringBuilder();
        sb.append("select GROUPID groupId,GROUPNAME name from hm_pub_group where ");
        sb.append("COMPANYID = ? ");
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sb.toString(), reportsParamVO.getCompanyId());
        for(Map<String, Object> map : list){
            String groupId = (String)map.get("groupId");
            for(ZjsClientDetailReportVO reportVO : reportVOS){
                if(StringUtils.equals(reportVO.getId(),groupId)){
                    reportVO.setName((String) map.get("name"));
                }
            }
        }

    }

    private void getReportConfig(ReportsParamVO reportsParamVO) {

        /*//读取报表配置
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT comp.REPORTSCONFIG reportConfig FROM hm_pub_company comp WHERE comp.ID = ? AND comp.ISDEL = 0 ");
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sb.toString(), reportsParamVO.getCompanyId());

        if(list != null){
            Map<String, Object> map = list.get(0);
            String reportConfig = (String)map.get("reportConfig");
            JSONObject configObj = JSONObject.parseObject(reportConfig);
            JSONArray jsonArray = configObj.getJSONObject(ReportsConfigConst.ZJS_VALID_SET).getJSONArray(ReportsConfigConst.WX_CLASS);
            //无效状态
            String zjsValidStatus = null;
            if(!jsonArray.isEmpty()){
                zjsValidStatus = jsonArray.toString().substring(1, jsonArray.toString().length() - 1);
            }

        }*/
        //这种方式yekeyi
        //String name = (String) jdbcTemplate.queryForObject(sb.toString(),new Object[]{reportsParamVO.getCompanyId()},String.class);

    }













}
