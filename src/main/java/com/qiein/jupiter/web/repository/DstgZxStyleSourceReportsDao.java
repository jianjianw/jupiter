package com.qiein.jupiter.web.repository;

import com.qiein.jupiter.util.DBSplitUtil;
import com.qiein.jupiter.util.NumUtil;
import com.qiein.jupiter.util.ObjectUtil;
import com.qiein.jupiter.util.StringUtil;
import com.qiein.jupiter.web.entity.vo.DsInvalidVO;
import com.qiein.jupiter.web.entity.vo.DstgZxStyleReportsVO;
import com.qiein.jupiter.web.entity.vo.ReportsParamVO;
import com.qiein.jupiter.web.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author: yyx
 * @Date: 2018-8-15
 */
@Repository
public class DstgZxStyleSourceReportsDao {
    @Autowired
    JdbcTemplate jdbcTemplate;

    /**
     * 获取电商推广咨询方式汇总报表
     */
    public List<DstgZxStyleReportsVO> getDstgGoldDataReprots(ReportsParamVO reportsParamVO, DsInvalidVO invalidConfig) {
        //TODO 缺少添加条件
        List<DstgZxStyleReportsVO> DstgZxStyleReportsVOS = new ArrayList<>();
        getZxStyleList(reportsParamVO,DstgZxStyleReportsVOS);
        // 获取总客资
        getAllClientCount(reportsParamVO, DstgZxStyleReportsVOS);
        //获取待定量
        getPendingClientCount(reportsParamVO,DstgZxStyleReportsVOS,invalidConfig);
        //获取筛选待定
        getFilterWaitClientCount(reportsParamVO, DstgZxStyleReportsVOS);
        //获取筛选中
        getFilterInClientCount(reportsParamVO, DstgZxStyleReportsVOS);
        //获取筛选无效
        getFilterInValidClientCount(reportsParamVO, DstgZxStyleReportsVOS);
        //获取无效量
        getInValidClientCount(reportsParamVO,DstgZxStyleReportsVOS,invalidConfig);
        //入店量
        getComeShopClientCount(reportsParamVO, DstgZxStyleReportsVOS);
        //成交量
        getSuccessClientCount(reportsParamVO, DstgZxStyleReportsVOS);
        //成交均价
        getAvgAmount(reportsParamVO, DstgZxStyleReportsVOS);
        //成交总价
        getAmount(reportsParamVO, DstgZxStyleReportsVOS);
        //计算
        computerRate(reportsParamVO, DstgZxStyleReportsVOS,invalidConfig);
        //计算合计
        computerTotal(reportsParamVO, DstgZxStyleReportsVOS);
        return DstgZxStyleReportsVOS;
    }



    /**
     * 获取所有广告集合
     * */
    private void getZxStyleList(ReportsParamVO reportsParamVO, List<DstgZxStyleReportsVO> DstgZxStyleReportsVOS){
        StringBuilder sb = new StringBuilder();
        String infoTabName = DBSplitUtil.getInfoTabName(reportsParamVO.getCompanyId());
        String detailTabName = DBSplitUtil.getDetailTabName(reportsParamVO.getCompanyId());
        sb.append(" select ifnull(detail.zxstyle,0) zxstyle,info.sourceid,source.srcname ");
        sb.append(" from ");
        sb.append(infoTabName + " info ");
        sb.append(" left join "+detailTabName+" detail on info.kzid = detail.kzid");
        sb.append(" left join hm_crm_source source on source.id = info.sourceid");
        sb.append(" where");
        sb.append("  info.isdel = 0");
        sb.append(" and info.companyid = ?");
        addConditionByTypeAndZxCodeStyle(reportsParamVO,sb);
        sb.append(" and (info.srctype = 1 or info.srctype = 2)");
        sb.append(" group by info.sourceid ");
//        addCondition(reportsParamVO,sb);

        List<Map<String, Object>> dstgGoldDataReports = jdbcTemplate.queryForList(sb.toString(),
                new Object[]{reportsParamVO.getCompanyId()});

        for (Map<String, Object> dstgGoldDataReport : dstgGoldDataReports) {
            DstgZxStyleReportsVO DstgZxStyleReportsVO = new DstgZxStyleReportsVO();
            DstgZxStyleReportsVO.setZxStyleCode(String.valueOf(dstgGoldDataReport.get("sourceid").toString()));

            DstgZxStyleReportsVO.setZxStyle((String) dstgGoldDataReport.get("srcname"));
            DstgZxStyleReportsVOS.add(DstgZxStyleReportsVO);
        }

    }


    private void addConditionByTypeAndZxCodeStyle(ReportsParamVO reportsParamVO,StringBuilder sb){
        if(StringUtil.isNotEmpty(reportsParamVO.getZxStyleCode())){
            sb.append(" and detail.ZXSTYLE in ( "+reportsParamVO.getZxStyleCode()+") ");
        }
        if(StringUtil.isNotEmpty(reportsParamVO.getType())){
            sb.append(" and info.typeid in( "+reportsParamVO.getType()+") ");
        }
        System.out.println(reportsParamVO.getCollectorId());
        if(StringUtil.isNotEmpty(reportsParamVO.getCollectorId())){
            sb.append(" and info.COLLECTORID in ("+reportsParamVO.getCollectorId()+")");
        }
    }

    private StringBuilder getCommonsql(StringBuilder sb,String infoTabName,String detailTabName) {
        sb.append(" select   ifnull(detail.zxstyle,0) zxstyle,info.sourceid,source.srcname,count(info.id) as client_count ");
        sb.append(" from");
        sb.append(infoTabName + " info ");
        sb.append(" left join "+detailTabName+" detail on info.kzid = detail.kzid");
        sb.append(" left join hm_crm_source source on source.id = info.sourceid");
        sb.append(" where");
        sb.append("  info.isdel = 0");
        sb.append(" and (info.srctype = 1 or info.srctype = 2)");
        sb.append(" and info.companyid = ?");
        return sb;
    }

//    /**
//     * 添加条件
//     * */
//    public void addCondition(ReportsParamVO reportsParamVO,StringBuilder sb){
//        if(StringUtil.isNotEmpty(reportsParamVO.getZxStyleCode())){
//            sb.append(" having zxstyle = "+reportsParamVO.getZxStyleCode());
//        }
//    }

    /**
     * 获取广告总量
     */
    private void getAllClientCount(ReportsParamVO reportsParamVO, List<DstgZxStyleReportsVO> DstgZxStyleReportsVOS) {
        StringBuilder sb = new StringBuilder();
        String infoTabName = DBSplitUtil.getInfoTabName(reportsParamVO.getCompanyId());
        String detailTabName = DBSplitUtil.getDetailTabName(reportsParamVO.getCompanyId());
        sb =  getCommonsql(sb,infoTabName,detailTabName);
        addConditionByTypeAndZxCodeStyle(reportsParamVO,sb);
        sb.append(" and info.CREATETIME BETWEEN ? AND ? ");
        sb.append(" group by info.sourceid");
//        addCondition(reportsParamVO,sb);
        List<Map<String, Object>> dstgGoldDataReports = jdbcTemplate.queryForList(sb.toString(),
                new Object[]{reportsParamVO.getCompanyId(),
                        reportsParamVO.getStart(),
                        reportsParamVO.getEnd()});

        List<DstgZxStyleReportsVO> dstgGoldDataReportsBak = new LinkedList<>();
        // 处理数据
        for (Map<String, Object> dstgGoldDataReport : dstgGoldDataReports) {
            DstgZxStyleReportsVO DstgZxStyleReportsVO = new DstgZxStyleReportsVO();
            DstgZxStyleReportsVO.setZxStyle((String) dstgGoldDataReport.get("srcname"));
            DstgZxStyleReportsVO.setZxStyleCode(String.valueOf(dstgGoldDataReport.get("sourceid").toString()));
            DstgZxStyleReportsVO.setAllClientCount(Integer.parseInt(Long.toString((Long) dstgGoldDataReport.get("client_count"))));
            dstgGoldDataReportsBak.add(DstgZxStyleReportsVO);
        }

        for (DstgZxStyleReportsVO DstgZxStyleReportsVO : DstgZxStyleReportsVOS) {
            for (DstgZxStyleReportsVO dstgGoldDataReport : dstgGoldDataReportsBak) {
                if (DstgZxStyleReportsVO.getZxStyleCode().equalsIgnoreCase(dstgGoldDataReport.getZxStyleCode())) {
                    DstgZxStyleReportsVO.setAllClientCount(dstgGoldDataReport.getAllClientCount());
                    break;
                }
            }
        }

    }

    /**
     * 获取待定量
     */
    private void getPendingClientCount(ReportsParamVO reportsParamVO, List<DstgZxStyleReportsVO> DstgZxStyleReportsVOS,DsInvalidVO dsInvalidVO) {
        if(StringUtil.isEmpty(dsInvalidVO.getDsDdStatus())){
            return;
        }
        StringBuilder sb = new StringBuilder();
        String infoTabName = DBSplitUtil.getInfoTabName(reportsParamVO.getCompanyId());
        String detailTabName = DBSplitUtil.getDetailTabName(reportsParamVO.getCompanyId());
        sb = getCommonsql(sb,infoTabName,detailTabName);
        addConditionByTypeAndZxCodeStyle(reportsParamVO,sb);
        sb.append(" and info.CREATETIME BETWEEN ? AND ?");
//        sb.append(" AND INSTR( ?, CONCAT(',',info.STATUSID + '',',')) != 0");
        sb.append(" and info.STATUSID in ("+dsInvalidVO.getDsDdStatus()+")");
        sb.append(" group by info.sourceid");
//        addCondition(reportsParamVO,sb);
        List<Map<String, Object>> dstgGoldDataReports = jdbcTemplate.queryForList(sb.toString(),
                new Object[]{reportsParamVO.getCompanyId(),
                        reportsParamVO.getStart(),
                        reportsParamVO.getEnd() });

        // 处理数据
        List<DstgZxStyleReportsVO> dstgGoldDataReportsBak = new LinkedList<>();
        for (Map<String, Object> dstgGoldDataReport : dstgGoldDataReports) {
            DstgZxStyleReportsVO DstgZxStyleReportsVO = new DstgZxStyleReportsVO();
            DstgZxStyleReportsVO.setZxStyle((String) dstgGoldDataReport.get("srcname"));
            DstgZxStyleReportsVO.setZxStyleCode(String.valueOf(dstgGoldDataReport.get("sourceid").toString()));
            DstgZxStyleReportsVO.setPendingClientCount(Integer.parseInt(Long.toString((Long) dstgGoldDataReport.get("client_count"))));
            dstgGoldDataReportsBak.add(DstgZxStyleReportsVO);
        }

        for (DstgZxStyleReportsVO DstgZxStyleReportsVO : DstgZxStyleReportsVOS) {
            for (DstgZxStyleReportsVO dstgGoldDataReport : dstgGoldDataReportsBak) {
                if (DstgZxStyleReportsVO.getZxStyleCode().equalsIgnoreCase(dstgGoldDataReport.getZxStyleCode())) {
                    DstgZxStyleReportsVO.setPendingClientCount(dstgGoldDataReport.getPendingClientCount());
                    break;
                }
            }
        }

    }

    /**
     * 获取筛选待定
     * */
    private void getFilterWaitClientCount(ReportsParamVO reportsParamVO, List<DstgZxStyleReportsVO> DstgZxStyleReportsVOS){
        StringBuilder sb = new StringBuilder();
        String infoTabName = DBSplitUtil.getInfoTabName(reportsParamVO.getCompanyId());
        String detailTabName = DBSplitUtil.getDetailTabName(reportsParamVO.getCompanyId());
        sb = getCommonsql(sb, infoTabName, detailTabName);
        addConditionByTypeAndZxCodeStyle(reportsParamVO,sb);
        sb.append(" and info.CREATETIME BETWEEN ? AND ?");
        sb.append(" and info.CLASSID = 1 and info.STATUSID = 98 ");
        sb.append(" group by info.sourceid");
//        addCondition(reportsParamVO,sb);
        List<Map<String, Object>> dstgGoldDataReports = jdbcTemplate.queryForList(sb.toString(),
                new Object[]{reportsParamVO.getCompanyId(),
                        reportsParamVO.getStart(),
                        reportsParamVO.getEnd()
                });


        // 处理数据
        List<DstgZxStyleReportsVO> dstgGoldDataReportsBak = new LinkedList<>();
        for (Map<String, Object> dstgGoldDataReport : dstgGoldDataReports) {
            DstgZxStyleReportsVO DstgZxStyleReportsVO = new DstgZxStyleReportsVO();
            DstgZxStyleReportsVO.setZxStyleCode(String.valueOf(dstgGoldDataReport.get("sourceid").toString()));
            DstgZxStyleReportsVO.setZxStyle((String) dstgGoldDataReport.get("srcname"));
            DstgZxStyleReportsVO.setFilterPendingClientCount(Integer.parseInt(Long.toString((Long) dstgGoldDataReport.get("client_count"))));
            dstgGoldDataReportsBak.add(DstgZxStyleReportsVO);
        }

        for (DstgZxStyleReportsVO DstgZxStyleReportsVO : DstgZxStyleReportsVOS) {
            for (DstgZxStyleReportsVO dstgGoldDataReport : dstgGoldDataReportsBak) {
                if (DstgZxStyleReportsVO.getZxStyleCode().equalsIgnoreCase(dstgGoldDataReport.getZxStyleCode())) {
                    DstgZxStyleReportsVO.setFilterPendingClientCount(dstgGoldDataReport.getFilterPendingClientCount());
                    break;
                }
            }
        }
    }

    /**
     * 获取筛选无效量
     * */
    private void getFilterInValidClientCount(ReportsParamVO reportsParamVO, List<DstgZxStyleReportsVO> DstgZxStyleReportsVOS){
        StringBuilder sb = new StringBuilder();
        String infoTabName = DBSplitUtil.getInfoTabName(reportsParamVO.getCompanyId());
        String detailTabName = DBSplitUtil.getDetailTabName(reportsParamVO.getCompanyId());
        sb = getCommonsql(sb, infoTabName, detailTabName);
        addConditionByTypeAndZxCodeStyle(reportsParamVO,sb);
        sb.append(" and info.CREATETIME BETWEEN ? AND ?");
        sb.append(" and info.CLASSID = 6 and info.STATUSID = 99");
        sb.append(" group by info.sourceid");
//        addCondition(reportsParamVO,sb);
        List<Map<String, Object>> dstgGoldDataReports = jdbcTemplate.queryForList(sb.toString(),
                new Object[]{reportsParamVO.getCompanyId(),
                        reportsParamVO.getStart(),
                        reportsParamVO.getEnd()});


        // 处理数据
        List<DstgZxStyleReportsVO> dstgGoldDataReportsBak = new LinkedList<>();
        for (Map<String, Object> dstgGoldDataReport : dstgGoldDataReports) {
            DstgZxStyleReportsVO DstgZxStyleReportsVO = new DstgZxStyleReportsVO();
            DstgZxStyleReportsVO.setZxStyle((String) dstgGoldDataReport.get("srcname"));
            DstgZxStyleReportsVO.setZxStyleCode(String.valueOf(dstgGoldDataReport.get("sourceid").toString()));
            DstgZxStyleReportsVO.setFilterInValidClientCount(Integer.parseInt(Long.toString((Long) dstgGoldDataReport.get("client_count"))));
            dstgGoldDataReportsBak.add(DstgZxStyleReportsVO);
        }

        for (DstgZxStyleReportsVO DstgZxStyleReportsVO : DstgZxStyleReportsVOS) {
            for (DstgZxStyleReportsVO dstgGoldDataReport : dstgGoldDataReportsBak) {
                if (DstgZxStyleReportsVO.getZxStyleCode().equalsIgnoreCase(dstgGoldDataReport.getZxStyleCode())) {
                    DstgZxStyleReportsVO.setFilterInValidClientCount(dstgGoldDataReport.getFilterInValidClientCount());
                    break;
                }
            }
        }

    }

    /**
     * 获取筛选中
     * */
    private void getFilterInClientCount(ReportsParamVO reportsParamVO, List<DstgZxStyleReportsVO> DstgZxStyleReportsVOS){
        StringBuilder sb = new StringBuilder();
        String infoTabName = DBSplitUtil.getInfoTabName(reportsParamVO.getCompanyId());
        String detailTabName = DBSplitUtil.getDetailTabName(reportsParamVO.getCompanyId());
        sb = getCommonsql(sb, infoTabName, detailTabName);
        addConditionByTypeAndZxCodeStyle(reportsParamVO,sb);
        sb.append(" and info.CREATETIME BETWEEN ? AND ?");
        sb.append(" and info.CLASSID = 1 and info.STATUSID = 0");
        sb.append(" group by info.sourceid");
//        addCondition(reportsParamVO,sb);
        List<Map<String, Object>> dstgGoldDataReports = jdbcTemplate.queryForList(sb.toString(),
                new Object[]{reportsParamVO.getCompanyId(),
                        reportsParamVO.getStart(),
                        reportsParamVO.getEnd()});


        // 处理数据
        List<DstgZxStyleReportsVO> dstgGoldDataReportsBak = new LinkedList<>();
        for (Map<String, Object> dstgGoldDataReport : dstgGoldDataReports) {
            DstgZxStyleReportsVO DstgZxStyleReportsVO = new DstgZxStyleReportsVO();
            DstgZxStyleReportsVO.setZxStyle((String) dstgGoldDataReport.get("srcname"));
            DstgZxStyleReportsVO.setZxStyleCode(String.valueOf(dstgGoldDataReport.get("sourceid").toString()));
            DstgZxStyleReportsVO.setFilterInClientCount(Integer.parseInt(Long.toString((Long) dstgGoldDataReport.get("client_count"))));
            dstgGoldDataReportsBak.add(DstgZxStyleReportsVO);
        }

        for (DstgZxStyleReportsVO DstgZxStyleReportsVO : DstgZxStyleReportsVOS) {
            for (DstgZxStyleReportsVO dstgGoldDataReport : dstgGoldDataReportsBak) {
                if (DstgZxStyleReportsVO.getZxStyleCode().equalsIgnoreCase(dstgGoldDataReport.getZxStyleCode())) {
                    DstgZxStyleReportsVO.setFilterInClientCount(dstgGoldDataReport.getFilterInClientCount());
                    break;
                }
            }
        }

    }

    /**
     * 无效量
     * */
    public void getInValidClientCount(ReportsParamVO reportsParamVO, List<DstgZxStyleReportsVO> DstgZxStyleReportsVOS,DsInvalidVO dsInvalidVO){
        if(StringUtil.isEmpty(dsInvalidVO.getDsInvalidStatus()) && StringUtil.isEmpty(dsInvalidVO.getDsInvalidLevel())){
            return ;
        }
        StringBuilder sb = new StringBuilder();
        String infoTabName = DBSplitUtil.getInfoTabName(reportsParamVO.getCompanyId());
        String detailTabName = DBSplitUtil.getDetailTabName(reportsParamVO.getCompanyId());
        sb = getCommonsql(sb, infoTabName, detailTabName);
        addConditionByTypeAndZxCodeStyle(reportsParamVO,sb);
        sb.append(" and info.CREATETIME BETWEEN ? AND ?");
        if(StringUtil.isNotEmpty(dsInvalidVO.getDsInvalidStatus()) && StringUtil.isNotEmpty(dsInvalidVO.getDsInvalidLevel())){
            sb.append(" and (info.STATUSID in("+ dsInvalidVO.getDsInvalidStatus()+") or");
            sb.append("   detail.YXLEVEL IN("+ dsInvalidVO.getDsInvalidLevel()  +") )");
        }
        if(StringUtil.isNotEmpty(dsInvalidVO.getDsInvalidStatus()) && StringUtil.isEmpty(dsInvalidVO.getDsInvalidLevel())){
            sb.append(" and info.STATUSID in ("+ dsInvalidVO.getDsInvalidStatus()+")");
        }
        if(StringUtil.isNotEmpty(dsInvalidVO.getDsInvalidLevel()) && StringUtil.isEmpty(dsInvalidVO.getDsInvalidStatus())){
            sb.append(" and detail.YXLEVEL IN("+ dsInvalidVO.getDsInvalidLevel()  +") ");
        }
        sb.append(" group by info.sourceid");
//        addCondition(reportsParamVO,sb);

        List<Map<String, Object>> dstgGoldDataReports = jdbcTemplate.queryForList(sb.toString(),
                new Object[]{reportsParamVO.getCompanyId(),
                        reportsParamVO.getStart(),
                        reportsParamVO.getEnd()});

        // 处理数据
        List<DstgZxStyleReportsVO> dstgGoldDataReportsBak = new LinkedList<>();
        for (Map<String, Object> dstgGoldDataReport : dstgGoldDataReports) {
            DstgZxStyleReportsVO DstgZxStyleReportsVO = new DstgZxStyleReportsVO();
            DstgZxStyleReportsVO.setZxStyle((String) dstgGoldDataReport.get("srcname"));
            DstgZxStyleReportsVO.setZxStyleCode(String.valueOf(dstgGoldDataReport.get("sourceid").toString()));
            DstgZxStyleReportsVO.setInValidClientCount(Integer.parseInt(Long.toString((Long) dstgGoldDataReport.get("client_count"))));
            dstgGoldDataReportsBak.add(DstgZxStyleReportsVO);
        }

        for (DstgZxStyleReportsVO DstgZxStyleReportsVO : DstgZxStyleReportsVOS) {
            for (DstgZxStyleReportsVO dstgGoldDataReport : dstgGoldDataReportsBak) {
                if (DstgZxStyleReportsVO.getZxStyleCode().equalsIgnoreCase(dstgGoldDataReport.getZxStyleCode())) {
                    DstgZxStyleReportsVO.setInValidClientCount(dstgGoldDataReport.getInValidClientCount());
                    break;
                }
            }
        }
    }




    /**
     * 入店量
     * */
    private void getComeShopClientCount(ReportsParamVO reportsParamVO, List<DstgZxStyleReportsVO> DstgZxStyleReportsVOS){
        StringBuilder sb = new StringBuilder();
        String infoTabName = DBSplitUtil.getInfoTabName(reportsParamVO.getCompanyId());
        String detailTabName = DBSplitUtil.getDetailTabName(reportsParamVO.getCompanyId());
        sb = getCommonsql(sb, infoTabName, detailTabName);
        addConditionByTypeAndZxCodeStyle(reportsParamVO,sb);
        sb.append(" and info.COMESHOPTIME BETWEEN ? AND ?");
        sb.append(" group by info.sourceid");
//        addCondition(reportsParamVO,sb);
        List<Map<String, Object>> dstgGoldDataReports = jdbcTemplate.queryForList(sb.toString(),
                new Object[]{reportsParamVO.getCompanyId(),
                        reportsParamVO.getStart(),
                        reportsParamVO.getEnd()});

        // 处理数据
        List<DstgZxStyleReportsVO> dstgGoldDataReportsBak = new LinkedList<>();
        for (Map<String, Object> dstgGoldDataReport : dstgGoldDataReports) {
            DstgZxStyleReportsVO DstgZxStyleReportsVO = new DstgZxStyleReportsVO();
            DstgZxStyleReportsVO.setZxStyle((String) dstgGoldDataReport.get("srcname"));
            DstgZxStyleReportsVO.setZxStyleCode(String.valueOf(dstgGoldDataReport.get("sourceid").toString()));
            DstgZxStyleReportsVO.setComeShopClientCount(Integer.parseInt(Long.toString((Long) dstgGoldDataReport.get("client_count"))));
            dstgGoldDataReportsBak.add(DstgZxStyleReportsVO);
        }

        for (DstgZxStyleReportsVO DstgZxStyleReportsVO : DstgZxStyleReportsVOS) {
            for (DstgZxStyleReportsVO dstgGoldDataReport : dstgGoldDataReportsBak) {
                if (DstgZxStyleReportsVO.getZxStyleCode().equalsIgnoreCase(dstgGoldDataReport.getZxStyleCode())) {
                    DstgZxStyleReportsVO.setComeShopClientCount(dstgGoldDataReport.getComeShopClientCount());
                    break;
                }
            }
        }
    }


    /**
     * 成交量
     * */
    private void getSuccessClientCount(ReportsParamVO reportsParamVO, List<DstgZxStyleReportsVO> DstgZxStyleReportsVOS){
        StringBuilder sb = new StringBuilder();
        String infoTabName = DBSplitUtil.getInfoTabName(reportsParamVO.getCompanyId());
        String detailTabName = DBSplitUtil.getDetailTabName(reportsParamVO.getCompanyId());
        sb = getCommonsql(sb, infoTabName, detailTabName);
        addConditionByTypeAndZxCodeStyle(reportsParamVO,sb);
        sb.append(" and info.SUCCESSTIME BETWEEN ? AND ?");
        sb.append(" group by info.sourceid");
//        addCondition(reportsParamVO,sb);

        List<Map<String, Object>> dstgGoldDataReports = jdbcTemplate.queryForList(sb.toString(),
                new Object[]{reportsParamVO.getCompanyId(),
                        reportsParamVO.getStart(),
                        reportsParamVO.getEnd()});

        // 处理数据
        List<DstgZxStyleReportsVO> dstgGoldDataReportsBak = new LinkedList<>();
        for (Map<String, Object> dstgGoldDataReport : dstgGoldDataReports) {
            DstgZxStyleReportsVO DstgZxStyleReportsVO = new DstgZxStyleReportsVO();
            DstgZxStyleReportsVO.setZxStyle((String) dstgGoldDataReport.get("srcname"));
            DstgZxStyleReportsVO.setZxStyleCode(String.valueOf(dstgGoldDataReport.get("sourceid").toString()));
            DstgZxStyleReportsVO.setSuccessClientCount(Integer.parseInt(Long.toString((Long) dstgGoldDataReport.get("client_count"))));
            dstgGoldDataReportsBak.add(DstgZxStyleReportsVO);
        }

        for (DstgZxStyleReportsVO DstgZxStyleReportsVO : DstgZxStyleReportsVOS) {
            for (DstgZxStyleReportsVO dstgGoldDataReport : dstgGoldDataReportsBak) {
                if (DstgZxStyleReportsVO.getZxStyleCode().equalsIgnoreCase(dstgGoldDataReport.getZxStyleCode())) {
                    DstgZxStyleReportsVO.setSuccessClientCount(dstgGoldDataReport.getSuccessClientCount());
                    break;
                }
            }
        }
    }


    /**
     * 成交均价
     * */
    private void getAvgAmount(ReportsParamVO reportsParamVO, List<DstgZxStyleReportsVO> DstgZxStyleReportsVOS){
        StringBuilder sb = new StringBuilder();
        String infoTabName = DBSplitUtil.getInfoTabName(reportsParamVO.getCompanyId());
        String detailTabName = DBSplitUtil.getDetailTabName(reportsParamVO.getCompanyId());
        sb.append(" select  ifnull(detail.zxstyle,0) zxstyle,info.sourceid,source.srcname,avg(detail.AMOUNT) as avg_amount ");
        sb.append(" from");
        sb.append(infoTabName + " info ");
        sb.append(" left join "+detailTabName+" detail on info.kzid = detail.kzid");
        sb.append(" left join hm_crm_source source on source.id = info.sourceid");
        sb.append(" where");
        sb.append("  info.isdel = 0");
        sb.append(" and (info.srctype = 1 or info.srctype = 2)");
        sb.append(" and info.companyid = ?");
        addConditionByTypeAndZxCodeStyle(reportsParamVO,sb);
        sb.append(" and info.SUCCESSTIME BETWEEN ? AND ?");
        sb.append(" group by info.sourceid");
//        addCondition(reportsParamVO,sb);

        List<Map<String, Object>> dstgGoldDataReports = jdbcTemplate.queryForList(sb.toString(),
                new Object[]{reportsParamVO.getCompanyId(),
                        reportsParamVO.getStart(),
                        reportsParamVO.getEnd()});

        // 处理数据
        List<DstgZxStyleReportsVO> dstgGoldDataReportsBak = new LinkedList<>();
        for (Map<String, Object> dstgGoldDataReport : dstgGoldDataReports) {
            DstgZxStyleReportsVO DstgZxStyleReportsVO = new DstgZxStyleReportsVO();
            DstgZxStyleReportsVO.setZxStyle((String) dstgGoldDataReport.get("srcname"));
            DstgZxStyleReportsVO.setZxStyleCode(String.valueOf(dstgGoldDataReport.get("sourceid").toString()));
            DstgZxStyleReportsVO.setAvgAmount(((BigDecimal) dstgGoldDataReport.get("avg_amount")).doubleValue());
            dstgGoldDataReportsBak.add(DstgZxStyleReportsVO);
        }

        for (DstgZxStyleReportsVO DstgZxStyleReportsVO : DstgZxStyleReportsVOS) {
            for (DstgZxStyleReportsVO dstgGoldDataReport : dstgGoldDataReportsBak) {
                if (DstgZxStyleReportsVO.getZxStyleCode().equalsIgnoreCase(dstgGoldDataReport.getZxStyleCode())) {
                    DstgZxStyleReportsVO.setAvgAmount(dstgGoldDataReport.getAvgAmount());
                    break;
                }
            }
        }
    }

    /**
     * 成交总价
     * */
    private void getAmount(ReportsParamVO reportsParamVO, List<DstgZxStyleReportsVO> DstgZxStyleReportsVOS){
        StringBuilder sb = new StringBuilder();
        String infoTabName = DBSplitUtil.getInfoTabName(reportsParamVO.getCompanyId());
        String detailTabName = DBSplitUtil.getDetailTabName(reportsParamVO.getCompanyId());
        sb.append(" select  ifnull(detail.zxstyle,0) zxstyle,info.sourceid,source.srcname,sum(detail.AMOUNT) as sum_amount ");
        sb.append(" from");
        sb.append(infoTabName + " info ");
        sb.append(" left join "+detailTabName+" detail on info.kzid = detail.kzid");
        sb.append(" left join hm_crm_source source on source.id = info.sourceid");
        sb.append(" where");
        sb.append("  info.isdel = 0");
        sb.append(" and info.companyid = ?");
        sb.append(" and (info.srctype = 1 or info.srctype = 2)");
        addConditionByTypeAndZxCodeStyle(reportsParamVO,sb);
        sb.append(" and info.SUCCESSTIME BETWEEN ? AND ?");
        sb.append(" group by info.sourceid");
//        addCondition(reportsParamVO,sb);
        List<Map<String, Object>> dstgGoldDataReports = jdbcTemplate.queryForList(sb.toString(),
                new Object[]{reportsParamVO.getCompanyId(),
                        reportsParamVO.getStart(),
                        reportsParamVO.getEnd()});

        // 处理数据
        List<DstgZxStyleReportsVO> dstgGoldDataReportsBak = new LinkedList<>();
        for (Map<String, Object> dstgGoldDataReport : dstgGoldDataReports) {
            DstgZxStyleReportsVO DstgZxStyleReportsVO = new DstgZxStyleReportsVO();
            DstgZxStyleReportsVO.setZxStyle((String) dstgGoldDataReport.get("srcname"));
            DstgZxStyleReportsVO.setZxStyleCode(String.valueOf(dstgGoldDataReport.get("sourceid").toString()));
            DstgZxStyleReportsVO.setAmount(((BigDecimal) dstgGoldDataReport.get("sum_amount")).doubleValue());
            dstgGoldDataReportsBak.add(DstgZxStyleReportsVO);
        }
        for (DstgZxStyleReportsVO DstgZxStyleReportsVO : DstgZxStyleReportsVOS) {
            for (DstgZxStyleReportsVO dstgGoldDataReport : dstgGoldDataReportsBak) {
                if (DstgZxStyleReportsVO.getZxStyleCode().equalsIgnoreCase(dstgGoldDataReport.getZxStyleCode())) {
                    DstgZxStyleReportsVO.setAmount(dstgGoldDataReport.getAmount());
                    break;
                }
            }
        }
    }

    /**
     *  计算Rate
     */
    private void computerRate(ReportsParamVO reportsParamVO, List<DstgZxStyleReportsVO> DstgZxStyleReportsVOS,DsInvalidVO invalidConfig){
        for (DstgZxStyleReportsVO DstgZxStyleReportsVO:DstgZxStyleReportsVOS){
            //有效量
            if(invalidConfig.getDdIsValid()){
                DstgZxStyleReportsVO.setValidClientCount(DstgZxStyleReportsVO.getAllClientCount()-DstgZxStyleReportsVO.getInValidClientCount()-DstgZxStyleReportsVO.getFilterInClientCount()-DstgZxStyleReportsVO.getFilterInValidClientCount()-DstgZxStyleReportsVO.getFilterPendingClientCount());
            }else{
                DstgZxStyleReportsVO.setValidClientCount(DstgZxStyleReportsVO.getAllClientCount()-DstgZxStyleReportsVO.getPendingClientCount()-DstgZxStyleReportsVO.getInValidClientCount()-DstgZxStyleReportsVO.getFilterInClientCount()-DstgZxStyleReportsVO.getFilterInValidClientCount()-DstgZxStyleReportsVO.getFilterPendingClientCount());
            }
            //客资量(总客资-筛选待定-筛选中-筛选无效)
            DstgZxStyleReportsVO.setClientCount(DstgZxStyleReportsVO.getAllClientCount()-DstgZxStyleReportsVO.getFilterPendingClientCount()-DstgZxStyleReportsVO.getFilterInValidClientCount()-DstgZxStyleReportsVO.getFilterInClientCount());
            //有效率
            double validRate = (double) DstgZxStyleReportsVO.getValidClientCount() / DstgZxStyleReportsVO.getClientCount();
            DstgZxStyleReportsVO.setValidRate(parseDouble(((Double.isNaN(validRate) || Double.isInfinite(validRate)) ? 0.0 : validRate) * 100));
            //无效率
            double invalidRate = (double) DstgZxStyleReportsVO.getInValidClientCount() / DstgZxStyleReportsVO.getClientCount();
            DstgZxStyleReportsVO.setInValidRate(parseDouble(((Double.isNaN(invalidRate) || Double.isInfinite(invalidRate)) ? 0.0 : invalidRate) * 100));
            //待定率
            double waitRate = (double) DstgZxStyleReportsVO.getPendingClientCount() / DstgZxStyleReportsVO.getClientCount();
            DstgZxStyleReportsVO.setWaitRate(parseDouble(((Double.isNaN(waitRate) || Double.isInfinite(waitRate)) ? 0.0 : waitRate) * 100));
            //毛客资入店率
            double clientComeShopRate = (double) DstgZxStyleReportsVO.getComeShopClientCount() / DstgZxStyleReportsVO.getClientCount();
            DstgZxStyleReportsVO.setClientComeShopRate(parseDouble(((Double.isNaN(clientComeShopRate) || Double.isInfinite(clientComeShopRate)) ? 0.0 : clientComeShopRate) * 100));
            //有效客资入店率
            double validComeShopRate = (double) DstgZxStyleReportsVO.getComeShopClientCount() / DstgZxStyleReportsVO.getValidClientCount();
            DstgZxStyleReportsVO.setValidClientComeShopRate(parseDouble(((Double.isNaN(validComeShopRate) || Double.isInfinite(validComeShopRate)) ? 0.0 : validComeShopRate) * 100));
            //毛客资成交率
            double successRate = (double) DstgZxStyleReportsVO.getSuccessClientCount() / DstgZxStyleReportsVO.getClientCount();
            DstgZxStyleReportsVO.setClientSuccessRate(parseDouble(((Double.isNaN(successRate) || Double.isInfinite(successRate)) ? 0.0 : successRate) * 100));
            //有效客资成交率
            double validSuccessRate = (double) DstgZxStyleReportsVO.getSuccessClientCount() / DstgZxStyleReportsVO.getValidClientCount();
            DstgZxStyleReportsVO.setValidClientSuccessRate(parseDouble(((Double.isNaN(validSuccessRate) || Double.isInfinite(validSuccessRate)) ? 0.0 : validSuccessRate) * 100));
            //入店成交率
            double comeShopSuccessRate = (double) DstgZxStyleReportsVO.getSuccessClientCount() / DstgZxStyleReportsVO.getComeShopClientCount();
            DstgZxStyleReportsVO.setComeShopSuccessRate(parseDouble(((Double.isNaN(comeShopSuccessRate) || Double.isInfinite(comeShopSuccessRate)) ? 0.0 : comeShopSuccessRate) * 100));
        }
    }

    /**
     * 计算总计
     * */
    private void computerTotal(ReportsParamVO reportsParamVO, List<DstgZxStyleReportsVO> DstgZxStyleReportsVOS){
        DstgZxStyleReportsVO dstgReportsTotal = new DstgZxStyleReportsVO();
        dstgReportsTotal.setZxStyle("合计");
        for (DstgZxStyleReportsVO dstgReportsVO : DstgZxStyleReportsVOS) {
            dstgReportsTotal.setAllClientCount(dstgReportsVO.getAllClientCount() + dstgReportsTotal.getAllClientCount());
            dstgReportsTotal.setClientCount(dstgReportsVO.getClientCount() + dstgReportsTotal.getClientCount());
            dstgReportsTotal.setValidClientCount(dstgReportsVO.getValidClientCount() + dstgReportsTotal.getValidClientCount());
            dstgReportsTotal.setPendingClientCount(dstgReportsVO.getPendingClientCount() + dstgReportsTotal.getPendingClientCount());
            dstgReportsTotal.setInValidClientCount(dstgReportsVO.getInValidClientCount() + dstgReportsTotal.getInValidClientCount());
            dstgReportsTotal.setComeShopClientCount(dstgReportsVO.getComeShopClientCount() + dstgReportsTotal.getComeShopClientCount());
            dstgReportsTotal.setSuccessClientCount(dstgReportsVO.getSuccessClientCount() + dstgReportsTotal.getSuccessClientCount());
            dstgReportsTotal.setAmount(dstgReportsVO.getAmount() + dstgReportsTotal.getAmount());
        }
        //客资量(总客资-筛选待定-筛选中-筛选无效)
        dstgReportsTotal.setClientCount(dstgReportsTotal.getAllClientCount()-dstgReportsTotal.getFilterPendingClientCount()-dstgReportsTotal.getFilterInValidClientCount()-dstgReportsTotal.getFilterInClientCount());
        //有效率
        double validRate = (double) dstgReportsTotal.getValidClientCount() / dstgReportsTotal.getClientCount();
        dstgReportsTotal.setValidRate(parseDouble(((Double.isNaN(validRate) || Double.isInfinite(validRate)) ? 0.0 : validRate) * 100));
        //无效率
        double invalidRate = (double) dstgReportsTotal.getInValidClientCount() / dstgReportsTotal.getClientCount();
        dstgReportsTotal.setInValidRate(parseDouble(((Double.isNaN(invalidRate) || Double.isInfinite(invalidRate)) ? 0.0 : invalidRate) * 100));
        //待定率
        double waitRate = (double) dstgReportsTotal.getPendingClientCount() / dstgReportsTotal.getClientCount();
        dstgReportsTotal.setWaitRate(parseDouble(((Double.isNaN(waitRate) || Double.isInfinite(waitRate)) ? 0.0 : waitRate) * 100));
        //毛客资入店率
        double clientComeShopRate = (double) dstgReportsTotal.getComeShopClientCount() / dstgReportsTotal.getClientCount();
        dstgReportsTotal.setClientComeShopRate(parseDouble(((Double.isNaN(clientComeShopRate) || Double.isInfinite(clientComeShopRate)) ? 0.0 : clientComeShopRate) * 100));
        //有效客资入店率
        double validComeShopRate = (double) dstgReportsTotal.getComeShopClientCount() / dstgReportsTotal.getValidClientCount();
        dstgReportsTotal.setValidClientComeShopRate(parseDouble(((Double.isNaN(validComeShopRate) || Double.isInfinite(validComeShopRate)) ? 0.0 : validComeShopRate) * 100));
        //毛客资成交率
        double successRate = (double) dstgReportsTotal.getSuccessClientCount() / dstgReportsTotal.getClientCount();
        dstgReportsTotal.setClientSuccessRate(parseDouble(((Double.isNaN(successRate) || Double.isInfinite(successRate)) ? 0.0 : successRate) * 100));
        //有效客资成交率
        double validSuccessRate = (double) dstgReportsTotal.getSuccessClientCount() / dstgReportsTotal.getValidClientCount();
        dstgReportsTotal.setValidClientSuccessRate(parseDouble(((Double.isNaN(validSuccessRate) || Double.isInfinite(validSuccessRate)) ? 0.0 : validSuccessRate) * 100));
        //入店成交率
        double comeShopSuccessRate = (double) dstgReportsTotal.getSuccessClientCount() / dstgReportsTotal.getComeShopClientCount();
        dstgReportsTotal.setComeShopSuccessRate(parseDouble(((Double.isNaN(comeShopSuccessRate) || Double.isInfinite(comeShopSuccessRate)) ? 0.0 : comeShopSuccessRate) * 100));
        //成交均价
        double avgAmount = dstgReportsTotal.getAmount() /dstgReportsTotal.getSuccessClientCount();
        dstgReportsTotal.setAvgAmount(parseDouble(((Double.isNaN(avgAmount) || Double.isInfinite(avgAmount)) ? 0.0 : avgAmount)));

        DstgZxStyleReportsVOS.add(0,dstgReportsTotal);
    }

    /**
     * 只保留2位小数
     * */
    public double parseDouble(double result){
        return Double.parseDouble(String.format("%.2f",result));
    }




}
