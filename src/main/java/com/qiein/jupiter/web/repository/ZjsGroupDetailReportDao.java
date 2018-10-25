package com.qiein.jupiter.web.repository;

import com.qiein.jupiter.util.DBSplitUtil;
import com.qiein.jupiter.util.DynamicBeanUtil;
import com.qiein.jupiter.util.StringUtil;
import com.qiein.jupiter.web.entity.vo.DsInvalidVO;
import com.qiein.jupiter.web.entity.vo.ReportsParamVO;
import com.qiein.jupiter.web.entity.vo.ZjsClientDetailReportVO;
import com.qiein.jupiter.web.entity.vo.ZjsClientDynamicReportVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.*;

@Repository
public class ZjsGroupDetailReportDao {

    private static Logger logger = LoggerFactory.getLogger(ZjsGroupReportDao.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private CommonReportsDao commonReportsDao;

    public ZjsClientDynamicReportVO getZjsGroupDetailReport(ReportsParamVO reportsParamVO){

        ZjsClientDynamicReportVO zjsClientDynamicReportVO = new ZjsClientDynamicReportVO();

        List<ZjsClientDetailReportVO> reportVOS = new ArrayList<ZjsClientDetailReportVO>();
        //获取毛客资
        getTotalClientCount(reportsParamVO,reportVOS);

        if(reportVOS.size() == 0){
            return zjsClientDynamicReportVO;
        }

        //计算有效客资
        getValidClientCount(reportsParamVO,reportVOS);
        //无效数  查询有效客资数的其余客资
        getInvalidClientSourceCount(reportsParamVO,reportVOS);
        //获取总进店数
        getTotalInShopCount(reportsParamVO,reportVOS);
        //总成交数
        getTotalSuccessCount(reportsParamVO,reportVOS);
        //周末进店数
        getWeekendInShopCount(reportsParamVO,reportVOS);
        //周末成交数
        getWeekendSuccessCount(reportsParamVO,reportVOS);
        //总金额 and 均价
        getAmount(reportsParamVO,reportVOS);
        //客服组内员工的名称
        getGroupAppointorName(reportsParamVO,reportVOS);
        //计算转换率
        computerRate(reportVOS);
        //总合计  (固定列的总合计)
        computerTotal(reportVOS);



        //查询表头（意向登记）
        Map<String, String> tableHead = getTableHead(reportsParamVO);
        //创建动态对象
        List<Object> dynamicBeans = getDynamicBeans(reportVOS, tableHead);

        //获取客户意向等级客资数  封装书籍到dynamicBeans
        Set<Map.Entry<String, String>> entries = tableHead.entrySet();
        for(Map.Entry<String, String> set : entries ){
            String code = set.getKey();
            String name = set.getValue();
            //意向等级
            getClientSourceLevelCount(reportsParamVO,dynamicBeans,code);
            getClientSourceLevelInShopCount(reportsParamVO,dynamicBeans,code);
        }

        //封装表头返回
        Map<String,String> dynamicTableHead =  getDynamicTableHead(tableHead);
        zjsClientDynamicReportVO.setDynamicTableHead(dynamicTableHead);
        zjsClientDynamicReportVO.setDynamicData(dynamicBeans);


        return zjsClientDynamicReportVO;
    }


    //设置动态类的总合计（毛客资和进店数）type: Count or InShopCount
    private void computerDynamicTotal(List<Object> dynamicBeans, String dicCode,String type) {

        StringBuilder sb = new StringBuilder();
        String suffix = sb.append("Level").append(dicCode).append(type).toString();
        Object totalObject = null;//总计对象
        Integer totalClient = 0;
        try {
            for(Object obj : dynamicBeans){
                Class<?> clazz = obj.getClass();
                Method getName = clazz.getDeclaredMethod("getName");
                String name = (String) getName.invoke(obj);
                if(StringUtils.equals("总合计",name)){
                    totalObject = obj;
                    continue;
                }
                Method declaredMethod = clazz.getDeclaredMethod("get"+suffix);
                Integer invoke = (Integer)declaredMethod.invoke(obj);
                totalClient += invoke;
            }
            //总计
            Class<?> clazz = totalObject.getClass();
            Method setMethod = clazz.getDeclaredMethod("set" + suffix,Integer.class);
            //设置毛客资总合计 or  进店数的总合计
            setMethod.invoke(totalObject,totalClient);


            //计算转换率的总合计
            if(StringUtils.equals(type,"InShopCount")){//毛客资 和 进店数都封装完毕
                StringBuilder stringBuilder = new StringBuilder();
                StringBuilder level = stringBuilder.append("Level").append(dicCode);
                //毛客资
                Method clientMethod = clazz.getDeclaredMethod("get"+level+"Count");
                Integer client = (Integer)clientMethod.invoke(totalObject);

                //进店数
                Method inShopMethod = clazz.getDeclaredMethod("get"+level+"InShopCount");
                Integer inShopCount = (Integer)inShopMethod.invoke(totalObject);

                //  进店/客资  总转化率
                double rate = inShopCount / (double) client;
                rate = parseDouble(((Double.isNaN(rate) || Double.isInfinite(rate)) ? 0.0 : rate) * 100);

                Method declaredMethod = clazz.getDeclaredMethod("set" + level + "Rate",Double.class);
                declaredMethod.invoke(totalObject,rate);//封装总转化率

            }
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
        }
    }

    //获取客户意向等级
    private Map<String,String> getTableHead(ReportsParamVO reportsParamVO) {

        StringBuilder sb = new StringBuilder();
        sb.append("select dic.DICCODE code ,dic.DICNAME name from (hm_crm_client_info info inner join hm_crm_client_detail detail ");
        sb.append("on info.KZID = detail.KZID) ");
        sb.append("inner join hm_crm_dictionary dic on dic.diccode = detail.yxlevel ");
        sb.append("where dic.dictype = 'yx_level' and dic.companyID = ? ");
        sb.append("and dic.DICCODE != 0 order by dic.DICCODE asc");

        List<Map<String, Object>> list = jdbcTemplate.queryForList(sb.toString(), reportsParamVO.getCompanyId());//得到公司的的所有意向等级

        Map<String,String> tableHeads = new HashMap<>();
        for (Map<String, Object> map : list) {
            String code = String.valueOf((Integer) map.get("code"));
            String name = (String) map.get("name");
            tableHeads.put(code,name);
        }
        return tableHeads;
    }

    //创建动态bean
    private List<Object> getDynamicBeans(List<ZjsClientDetailReportVO> reportVOS,Map<String, String> tableHead){

        Map<String,Object> propertyMap = new HashMap<>();

        Set<Map.Entry<String, String>> entries = tableHead.entrySet();
        for (Map.Entry<String, String> set : entries) {
            StringBuilder sb = new StringBuilder();
            String code = set.getKey();//code
            //String name = set.getValue();//name   可能会出现汉字
            String prefix = sb.append("level").append(code).toString();
            propertyMap.put(prefix+"Count",0);//客资数
            propertyMap.put(prefix+"InShopCount",0);//进店数
            propertyMap.put(prefix+"Rate",0D);//转换率
        }

        List<Object> dynamicBeans = new ArrayList<Object>();
        //创建动态bean
        Object dynamicBean;
        for (ZjsClientDetailReportVO reportVO : reportVOS) {
            dynamicBean = DynamicBeanUtil.getDynamicBean(reportVO, propertyMap);
            dynamicBeans.add(dynamicBean);
        }
        return dynamicBeans;
    }

    //获取等级客资数(A,B,C,D)
    private void getClientSourceLevelCount(ReportsParamVO reportsParamVO,List<Object> dynamicBeans,String dicCode){

        String infoTabName = DBSplitUtil.getInfoTabName(reportsParamVO.getCompanyId());
        String detailTabName = DBSplitUtil.getDetailTabName(reportsParamVO.getCompanyId());
        StringBuilder sb = new StringBuilder();

        sb.append("select info.APPOINTORID kfId, count(info.KZID)  totalCount, ");
        sb.append("count(case when info.STATUSID = 98 then info.KZID else NULL end) filterWaitCount, ");
        sb.append("count(case when info.STATUSID = 0 then info.KZID else NULL end) filterInCount, ");
        sb.append("count(case when info.STATUSID = 99 then info.KZID else NULL end) filterInvalidCount ");
        sb.append("from (").append(infoTabName).append("info inner join ").append(detailTabName).append("detail on info.KZID = detail.KZID ) ");
        sb.append("inner join hm_crm_dictionary dic on detail.YXLEVEL = dic.DICCODE ");
        sb.append("where dic.COMPANYID = ? and dic.DICTYPE = 'yx_level' and dic.DICCODE = '"+dicCode+"' ");
        sb.append("and info.CREATETIME BETWEEN ? AND ? ");
        sb.append("and info.ISDEL = 0 ");
        sb.append("and info.GROUPID = ? ");
        sb.append("and info.SRCTYPE in(3, 4, 5) ");
        sb.append("group by info.APPOINTORID ");

        List<Map<String, Object>> list = jdbcTemplate.queryForList(sb.toString(), reportsParamVO.getCompanyId(),
                reportsParamVO.getStart(), reportsParamVO.getEnd(),reportsParamVO.getGroupId());

        //封装参数到dynamicBeans
        getDynamicBeanMethod(list,dynamicBeans,dicCode,"Count");

        //计算客资数的总合计
        computerDynamicTotal(dynamicBeans,dicCode,"Count");


    }

    //封装客资意向等级数据
    public void getDynamicBeanMethod(List<Map<String, Object>> list,List<Object> dynamicBeans,String dicCode,String type){

        StringBuilder sb = new StringBuilder();
        sb.append("set").append("Level").append(dicCode).append(type);//count or inshopCount
        try {
            for(Object obj : dynamicBeans){
                Class<?> clazz = obj.getClass();
                Method method = clazz.getDeclaredMethod("getId");
                String id = (String)method.invoke(obj);

                for (Map<String, Object> map: list) {
                    String kfId = String.valueOf((Long)map.get("kfId"));
                    if(StringUtils.equals(id,kfId)){
                        Long totalCount = (Long)map.get("totalCount");
                        Long filterWaitCount = (Long) map.get("filterWaitCount");
                        Long filterInCount = (Long) map.get("filterInCount");
                        Long filterInvalidCount = (Long) map.get("filterInvalidCount");
                        Long levelCount = totalCount - filterWaitCount - filterInCount - filterInvalidCount;//等级客资数
                        Method declaredMethod = clazz.getDeclaredMethod(sb.toString(), Integer.class);
                        declaredMethod.invoke(obj,levelCount.intValue());
                        break;
                    };
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
        }

    };
    //封装客资进店数 和 客资转化率(A,B,C)
    private void getClientSourceLevelInShopCount(ReportsParamVO reportsParamVO,List<Object> dynamicBeans, String dicCode){
        String infoTabName = DBSplitUtil.getInfoTabName(reportsParamVO.getCompanyId());
        String detailTabName = DBSplitUtil.getDetailTabName(reportsParamVO.getCompanyId());
        StringBuilder sb = new StringBuilder();

        sb.append("select info.APPOINTORID kfId, count(info.KZID)  totalCount, ");
        sb.append("count(case when info.STATUSID = 98 then info.KZID else NULL end) filterWaitCount, ");
        sb.append("count(case when info.STATUSID = 0 then info.KZID else NULL end) filterInCount, ");
        sb.append("count(case when info.STATUSID = 99 then info.KZID else NULL end) filterInvalidCount ");
        sb.append("from (").append(infoTabName).append("info inner join ").append(detailTabName).append("detail on info.KZID = detail.KZID ) ");
        sb.append("inner join hm_crm_dictionary dic on detail.YXLEVEL = dic.DICCODE ");
        sb.append("where dic.COMPANYID = ? and dic.DICTYPE = 'yx_level' and dic.DICCODE = '"+dicCode+"' ");
        sb.append("and info.COMESHOPTIME BETWEEN ? AND ? ");
        sb.append("and info.ISDEL = 0 ");
        sb.append("and info.GROUPID  = ? ");
        sb.append("and info.SRCTYPE in(3, 4, 5) ");
        sb.append("group by info.APPOINTORID ");

        List<Map<String, Object>> list = jdbcTemplate.queryForList(sb.toString(), reportsParamVO.getCompanyId(),
                reportsParamVO.getStart(), reportsParamVO.getEnd(),reportsParamVO.getGroupId());

        //封装参数到dynamicBeans
        getDynamicBeanMethod(list,dynamicBeans,dicCode,"InShopCount");
        //计算客资转化率
        convertClientRate(dynamicBeans,dicCode);

        //计算进店数的总合计 和  转化率的总合计
        computerDynamicTotal(dynamicBeans,dicCode,"InShopCount");
    }


    //计算客资转化率（进店/客资）
    private void convertClientRate(List<Object> dynamicBeans,String dicCode){
        StringBuilder sb = new StringBuilder();
        String prefix = sb.append("get").append("Level").append(dicCode).toString();
        try {
            for(Object obj : dynamicBeans){

                Class<?> clazz = obj.getClass();
                Method methodCount = clazz.getDeclaredMethod(prefix+"Count");
                Integer count = (Integer)methodCount.invoke(obj);

                Method methodInShopCount = clazz.getDeclaredMethod(prefix+"InShopCount");
                Integer inShopCount = (Integer)methodInShopCount.invoke(obj);

                double rate = parseDouble(count / (double)inShopCount * 100);
                sb.setLength(0);
                sb.append("set").append("Level").append(dicCode).append("Rate");
                Method methodRate = clazz.getDeclaredMethod(sb.toString(), Double.class);
                if(count == 0){
                    methodRate.invoke(obj, 0D);
                }else{
                    methodRate.invoke(obj, rate);//进店转化率
                }

            }
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
        }

    }

    // 获取毛客资
    private void getTotalClientCount(ReportsParamVO reportsParamVO,List<ZjsClientDetailReportVO> reportVOS ){

        String infoTabName = DBSplitUtil.getInfoTabName(reportsParamVO.getCompanyId());
        StringBuilder sb = new StringBuilder();
        sb.append("select APPOINTORID kfId,count(KZID) totalCount, ");
        sb.append("count(case when STATUSID = 98 then KZID else NULL end) filterWaitCount, ");
        sb.append("count(case when STATUSID = 0 then KZID else NULL end) filterInCount, ");
        sb.append("count(case when STATUSID = 99 then KZID else NULL end) filterInvalidCount ");
        sb.append("from ").append(infoTabName);
        sb.append("where COMPANYID = ? ");
        sb.append("and CREATETIME BETWEEN ? AND ? ");
        sb.append("and ISDEL = 0 ");
        sb.append("and GROUPID = ? ");
        sb.append("and SRCTYPE in(3, 4, 5) ");
        sb.append("group by APPOINTORID ");
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sb.toString(), reportsParamVO.getCompanyId(),
                reportsParamVO.getStart(), reportsParamVO.getEnd(),reportsParamVO.getGroupId());
        ZjsClientDetailReportVO reportVO;
        for (Map<String, Object> map:list) {
            reportVO = new ZjsClientDetailReportVO();
            Long totalCount = (Long) map.get("totalCount");
            reportVO.setId(String.valueOf((Long) map.get("kfId")));
            Long filterWaitCount = (Long) map.get("filterWaitCount");
            Long filterInCount = (Long) map.get("filterInCount");
            Long filterInvalidCount = (Long) map.get("filterInvalidCount");
            Long kzCount = totalCount - filterWaitCount - filterInCount - filterInvalidCount;//毛客资
            reportVO.setClientSourceCount(kzCount.intValue());
            reportVOS.add(reportVO);
        }
        System.out.println();
    }

    //计算有效客资数
    private void getValidClientCount(ReportsParamVO reportsParamVO,List<ZjsClientDetailReportVO> reportVOS) {

        String infoTabName = DBSplitUtil.getInfoTabName(reportsParamVO.getCompanyId());

        DsInvalidVO invalidConfig = commonReportsDao.getInvalidConfig(reportsParamVO.getCompanyId());
        StringBuilder sb = new StringBuilder();
        sb.append("select info.APPOINTORID kfId, count(info.KZID) validCount  ");
        sb.append("from ").append(infoTabName).append("info ");
        sb.append("where info.companyId = ? ");
        sb.append("and info.CREATETIME BETWEEN ? AND ? ");
        sb.append("and info.ISDEL = 0 ");
        sb.append("and info.GROUPID = ? ");
        if (StringUtil.isNotEmpty(invalidConfig.getZjsValidStatus())) {
            sb.append(" AND INSTR('" + invalidConfig.getZjsValidStatus() + "',CONCAT( '\"',info.STATUSID,'\"'))>0 ");//找到返回索引> 0 因为从一开始
        }
        sb.append("and info.SRCTYPE in (3, 4, 5) ");
        sb.append("group by info.APPOINTORID ");

        List<Map<String, Object>> list = jdbcTemplate.queryForList(sb.toString(), reportsParamVO.getCompanyId(),
                reportsParamVO.getStart(), reportsParamVO.getEnd(),reportsParamVO.getGroupId());

        for (Map<String, Object> map : list) {
            String groupId = (String) map.get("groupId");
            for(ZjsClientDetailReportVO reportVO : reportVOS ){
                String id = reportVO.getId();
                if(StringUtils.equals(id,groupId)){
                    Long validCount = (Long) map.get("validCount");
                    reportVO.setValidClientSourceCount(validCount.intValue());//有效量
                }
            }

        }
    }

    //获取总进店数
    private void getTotalInShopCount(ReportsParamVO reportsParamVO, List<ZjsClientDetailReportVO> reportVOS) {

        String infoTabName = DBSplitUtil.getInfoTabName(reportsParamVO.getCompanyId());
        StringBuilder sb = new StringBuilder();
        sb.append("select APPOINTORID kfId,count(KZID) totalCount ");
        sb.append("from ").append(infoTabName);
        sb.append("where companyId = ? ");
        sb.append("and COMESHOPTIME BETWEEN ? AND ? ");
        sb.append("and ISDEL = 0 ");
        sb.append("and GROUPID = ? ");
        sb.append("and SRCTYPE in(3, 4, 5) ");
        sb.append("group by APPOINTORID ");
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sb.toString(), reportsParamVO.getCompanyId(),
                reportsParamVO.getStart(), reportsParamVO.getEnd(),reportsParamVO.getGroupId());

        for (Map<String, Object> map : list ) {
            String kfId = String.valueOf((Long)map.get("kfId"));

            for (ZjsClientDetailReportVO reportVO : reportVOS) {
                String id = reportVO.getId();
                if(StringUtils.equals(id,kfId)){
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
        sb.append("select APPOINTORID kfId,count(KZID) totalCount ");
        sb.append("from ").append(infoTabName);
        sb.append("where companyId = ? ");
        sb.append("and SUCCESSTIME BETWEEN ? AND ? ");//总成交数
        sb.append("and ISDEL = 0 ");
        sb.append("and GROUPID = ? ");
        sb.append("and SRCTYPE in(3, 4, 5) ");
        sb.append("group by APPOINTORID ");
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sb.toString(), reportsParamVO.getCompanyId(),
                reportsParamVO.getStart(), reportsParamVO.getEnd(),reportsParamVO.getGroupId());

        for (Map<String, Object> map : list ) {
            String kfId = String.valueOf((Long)map.get("kfId"));

            for (ZjsClientDetailReportVO reportVO : reportVOS) {
                String id = reportVO.getId();
                if(StringUtils.equals(id,kfId)){
                    Long totalCount = (Long)map.get("totalCount");
                    reportVO.setTotalSuccessCount(totalCount.intValue());//总成交数
                }
            }
        }

    }

    //总金额 ，均价
    private void getAmount(ReportsParamVO reportsParamVO, List<ZjsClientDetailReportVO> reportVOS) {
        String infoTabName = DBSplitUtil.getInfoTabName(reportsParamVO.getCompanyId());
        String detailTabName = DBSplitUtil.getDetailTabName(reportsParamVO.getCompanyId());
        StringBuilder sb = new StringBuilder();
        sb.append("select info.APPOINTORID kfId,sum(detail.amount) totalAmount,avg(detail.amount) avgAmount ");
        sb.append("from ").append(infoTabName).append("info inner join ").append(detailTabName).append("detail ");
        sb.append("on info.KZID = detail.KZID ");
        sb.append("where info.companyId = ? ");
        sb.append("and info.SUCCESSTIME BETWEEN ? AND ? ");
        sb.append("and info.ISDEL = 0 ");
        sb.append("and info.GROUPID = ? ");
        sb.append("and info.SRCTYPE in(3, 4, 5) ");
        sb.append("group by info.APPOINTORID ");
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sb.toString(), reportsParamVO.getCompanyId(),
                reportsParamVO.getStart(), reportsParamVO.getEnd(),reportsParamVO.getGroupId());

        for (Map<String, Object> map : list ) {
            String kfId = String.valueOf((Long)map.get("kfId"));

            for (ZjsClientDetailReportVO reportVO : reportVOS) {
                String id = reportVO.getId();
                if(StringUtils.equals(id,kfId)){
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

        sb.append("select info.APPOINTORID kfId ,count(info.KZID) weekendCount ");
        sb.append("from ").append(infoTabName).append("info ");
        sb.append("where info.companyId = ? ");
        sb.append("and info.COMESHOPTIME BETWEEN ? AND ? ");
        sb.append("and DAYOFWEEK(from_unixtime(info.COMESHOPTIME)) IN (1,7) ");
        sb.append("and info.ISDEL = 0 ");
        sb.append("and info.GROUPID = ? ");
        sb.append("and info.SRCTYPE in(3, 4, 5) ");
        sb.append("group by info.APPOINTORID ");

        List<Map<String, Object>> list = jdbcTemplate.queryForList(sb.toString(), reportsParamVO.getCompanyId(),
                reportsParamVO.getStart(), reportsParamVO.getEnd(),reportsParamVO.getGroupId());
        for (Map<String, Object> map:list){
            String kfId = String.valueOf((Long)map.get("kfId"));
            for(ZjsClientDetailReportVO reportVO :reportVOS ){
                String id = reportVO.getId();
                if(StringUtils.equals(id,kfId)){
                    Long weekendCount = (Long)map.get("weekendCount");
                    reportVO.setWeekendInShopCount(weekendCount.intValue());//周末进店数
                }
            }
        }
    }

    //周末成交数
    private void getWeekendSuccessCount(ReportsParamVO reportsParamVO, List<ZjsClientDetailReportVO> reportVOS) {

        String infoTabName = DBSplitUtil.getInfoTabName(reportsParamVO.getCompanyId());
        StringBuilder sb = new StringBuilder();

        sb.append("select info.APPOINTORID kfId ,count(info.KZID) weekendCount ");
        sb.append("from ").append(infoTabName).append("info ");
        sb.append("where info.companyId = ? ");
        sb.append("and info.SUCCESSTIME BETWEEN ? AND ? ");
        sb.append("and DAYOFWEEK(from_unixtime(info.SUCCESSTIME)) IN (1,7) ");
        sb.append("and info.ISDEL = 0 ");
        sb.append("and info.GROUPID = ? ");
        sb.append("and info.SRCTYPE in(3, 4, 5) ");
        sb.append("group by info.APPOINTORID ");

        List<Map<String, Object>> list = jdbcTemplate.queryForList(sb.toString(), reportsParamVO.getCompanyId(),
                reportsParamVO.getStart(), reportsParamVO.getEnd(),reportsParamVO.getGroupId());
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

    private void getInvalidClientSourceCount(ReportsParamVO reportsParamVO, List<ZjsClientDetailReportVO> reportVOS) {


        String infoTabName = DBSplitUtil.getInfoTabName(reportsParamVO.getCompanyId());

        DsInvalidVO invalidConfig = commonReportsDao.getInvalidConfig(reportsParamVO.getCompanyId());
        StringBuilder sb = new StringBuilder();
        sb.append("select info.APPOINTORID kfId, count(info.KZID) invalidCount  ");
        sb.append("from ").append(infoTabName).append("info ");
        sb.append("where info.companyId = ? ");
        sb.append("and info.CREATETIME BETWEEN ? AND ? ");
        sb.append("and info.ISDEL = 0 ");
        sb.append("and info.GROUPID = ? ");
        if (StringUtil.isNotEmpty(invalidConfig.getZjsValidStatus())) {
            sb.append(" AND INSTR('" + invalidConfig.getZjsValidStatus() + "',CONCAT( '\"',info.STATUSID,'\"'))=0 ");//找不到返回0
        }
        sb.append("and info.SRCTYPE in (3, 4, 5) ");
        sb.append("group by APPOINTORID ");
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sb.toString(), reportsParamVO.getCompanyId(),
                reportsParamVO.getStart(), reportsParamVO.getEnd(),reportsParamVO.getGroupId());

        for (Map<String, Object> map : list) {
            String kfId = String.valueOf((Long)map.get("kfId"));
            for(ZjsClientDetailReportVO reportVO : reportVOS ){
                String id = reportVO.getId();
                if(StringUtils.equals(id,kfId)){
                    Long invalidCount = (Long) map.get("invalidCount");
                    reportVO.setInvalidClientSourceCount(invalidCount.intValue());//无效量
                }
            }

        }
    }


    //获取客服组内客服的名称
    private void getGroupAppointorName(ReportsParamVO reportsParamVO, List<ZjsClientDetailReportVO> reportVOS) {


        StringBuilder sb = new StringBuilder();
        sb.append("select staff.ID kfId,staff.NICKNAME name from hm_pub_group_staff groupStaff ");
        sb.append("inner join hm_pub_staff staff on groupStaff.STAFFID = staff.ID ");
        sb.append("where groupStaff.COMPANYID = ? ");
        sb.append("and groupStaff.GROUPID = ? ");

        List<Map<String, Object>> list = jdbcTemplate.queryForList(sb.toString(), reportsParamVO.getCompanyId(), reportsParamVO.getGroupId());
        for (Map<String, Object> map : list) {
            String kfId = String.valueOf((Long)map.get("kfId"));
            for(ZjsClientDetailReportVO reportVO : reportVOS ){
                String id = reportVO.getId();
                if(StringUtils.equals(id,kfId)){
                    reportVO.setName((String) map.get("name"));
                }
            }

        }

    }

    private void computerRate(List<ZjsClientDetailReportVO> reportVOS) {

        for (ZjsClientDetailReportVO reportVO : reportVOS){

            //总成交率（总成交/总进店）
            int totalSuccessCount = reportVO.getTotalSuccessCount();//总成交
            int totalInShopCount = reportVO.getTotalInShopCount();//总进店
            if(totalInShopCount == 0){
                reportVO.setTotalSuccessRate(0);
            }else{
                reportVO.setTotalSuccessRate(parseDouble((totalSuccessCount / (double)totalInShopCount) * 100));//总成交率
            }

            //毛客资进店率（总进店/毛客资数）
            int clientSourceCount = reportVO.getClientSourceCount();//毛客资数
            if(clientSourceCount == 0){
                reportVO.setClientInShopRate(0);
            }else{
                reportVO.setClientInShopRate(parseDouble((totalInShopCount / (double)clientSourceCount) * 100));
            }

            //有效客资进店率（总进店/ 有效客资）
            int validClientSourceCount = reportVO.getValidClientSourceCount();//有效客资
            if(validClientSourceCount == 0){
                reportVO.setValidClientInShopRate(0);
            }else{
                reportVO.setValidClientInShopRate(parseDouble((totalInShopCount / (double)validClientSourceCount) * 100));
            }


            //非周末进店数
            int weekendInShopCount = reportVO.getWeekendInShopCount();
            reportVO.setUnWeekendInShopCount(totalInShopCount-weekendInShopCount);//总进店-非周末进店数

            //非周末进店占比
            if(totalInShopCount == 0){
                reportVO.setUnWeekendInShopRate(0);
            }else{
                reportVO.setUnWeekendInShopRate(parseDouble((reportVO.getUnWeekendInShopCount()/(double)totalInShopCount) * 100));
            }



            int weekendSuccessCount = reportVO.getWeekendSuccessCount();
            //非周末成交数
            reportVO.setUnWeekendSuccessCount(totalSuccessCount - weekendSuccessCount);//总成交 - 周末成交



            if(totalSuccessCount == 0){
                reportVO.setWeekendSuccessRate(0);
                reportVO.setUnWeekendInShopRate(0);
            } else{
                //周末成交率
                reportVO.setWeekendSuccessRate(parseDouble((weekendSuccessCount/(double)totalSuccessCount) * 100));
                //非周末成交率
                reportVO.setUnWeekendInShopRate(parseDouble((reportVO.getUnWeekendSuccessCount()/(double)totalSuccessCount)* 100));
            }

        }
    }

    //计算总合计
    private void computerTotal(List<ZjsClientDetailReportVO> reportVOS) {
        //毛客资数
        int clientSourceCount = 0;
        //有效客资数
        int validClientSourceCount = 0;
        //无效数
        int invalidClientSourceCount = 0;
        //总进店数
        int totalInShopCount = 0;
        //总成交数
        int totalSuccessCount = 0;
        //总成交率
        double totalSuccessRate = 0d;
        //毛客资进店率
        double clientInShopRate = 0d;
        //有效客资进店率
        double validClientInShopRate = 0d;
        //周末进店数
        int weekendInShopCount = 0;
        //非周末进店数
        int unWeekendInShopCount = 0;
        //周末成交数
        int weekendSuccessCount = 0;
        //非周末成交数
        int unWeekendSuccessCount = 0;
        //非周末进店占比
        double unWeekendInShopRate = 0d;
        //周末成交率
        double weekendSuccessRate = 0d;
        //非周末成交率
        double unWeekendSuccessRate = 0d;
        //营业额
        double amount = 0;
        //均价
        double avgAmount = 0;
        ZjsClientDetailReportVO total = new ZjsClientDetailReportVO();
        total.setName("总合计");
        for (ZjsClientDetailReportVO reportVO : reportVOS) {
            clientSourceCount += reportVO.getClientSourceCount();
            validClientSourceCount += reportVO.getValidClientSourceCount();
            invalidClientSourceCount += reportVO.getInvalidClientSourceCount();
            totalInShopCount += reportVO.getTotalInShopCount();
            totalSuccessCount += reportVO.getTotalSuccessCount();
            weekendInShopCount += reportVO.getWeekendInShopCount();
            unWeekendInShopCount += reportVO.getUnWeekendInShopCount();
            weekendSuccessCount += reportVO.getWeekendSuccessCount();
            unWeekendSuccessCount += reportVO.getUnWeekendSuccessCount();
            amount += reportVO.getAmount();
            avgAmount += reportVO.getAvgAmount();
        }

        double rate = totalSuccessCount / (double) totalInShopCount;
        totalSuccessRate = parseDouble(parseDouble(((Double.isNaN(rate) || Double.isInfinite(rate)) ? 0.0 : rate) * 100));

        rate = totalInShopCount / (double) clientSourceCount;
        clientInShopRate = parseDouble(parseDouble(((Double.isNaN(rate) || Double.isInfinite(rate)) ? 0.0 : rate) * 100));

        rate = totalInShopCount / (double) validClientSourceCount;
        validClientInShopRate = parseDouble(parseDouble(((Double.isNaN(rate) || Double.isInfinite(rate)) ? 0.0 : rate) * 100));

        rate = unWeekendInShopCount / (double) totalInShopCount;
        unWeekendInShopRate = parseDouble(parseDouble(((Double.isNaN(rate) || Double.isInfinite(rate)) ? 0.0 : rate) * 100));

        rate = weekendSuccessCount / (double) totalSuccessCount;
        weekendSuccessRate = parseDouble(parseDouble(((Double.isNaN(rate) || Double.isInfinite(rate)) ? 0.0 : rate) * 100));
        rate = unWeekendSuccessCount / (double) totalSuccessCount;
        unWeekendSuccessRate = parseDouble(parseDouble(((Double.isNaN(rate) || Double.isInfinite(rate)) ? 0.0 : rate) * 100));

        total.setClientSourceCount(clientSourceCount);
        total.setValidClientSourceCount(validClientSourceCount);
        total.setInvalidClientSourceCount(invalidClientSourceCount);
        total.setTotalInShopCount(totalInShopCount);
        total.setTotalSuccessCount(totalSuccessCount);
        total.setTotalSuccessRate(totalSuccessRate);
        total.setClientInShopRate(clientInShopRate);
        total.setValidClientInShopRate(validClientInShopRate);
        total.setWeekendInShopCount(weekendInShopCount);
        total.setUnWeekendInShopCount(unWeekendInShopCount);
        total.setWeekendSuccessCount(weekendSuccessCount);
        total.setUnWeekendSuccessCount(unWeekendSuccessCount);
        total.setUnWeekendInShopRate(unWeekendInShopRate);
        total.setWeekendSuccessRate(weekendSuccessRate);
        total.setUnWeekendSuccessRate(unWeekendSuccessRate);
        total.setAmount(amount);
        total.setAvgAmount(avgAmount);
        reportVOS.add(0,total);
    }


    private Map<String,String> getDynamicTableHead(Map<String, String> tableHead) {

        StringBuilder sb = null;
        Map<String,String> dynamicTableHead = new LinkedHashMap<>();
        Set<Map.Entry<String, String>> entries = tableHead.entrySet();
        for(Map.Entry<String, String> set : entries ){
            String code = set.getKey();
            String name = set.getValue();
            sb = new StringBuilder();
            String tableKey = sb.append("level").append(code).toString();
            dynamicTableHead.put(tableKey+"Count",name+"类客资");//客资数
            dynamicTableHead.put(tableKey+"InShopCount",name+"类进店数");//进店数
            dynamicTableHead.put(tableKey+"Rate",name +"类客资转化率");//转换率
        }
        return dynamicTableHead;

    }

    /**
     * 只保留2位小数
     */
    public double parseDouble(double result) {
        return Double.parseDouble(String.format("%.2f", result));
    }





}
