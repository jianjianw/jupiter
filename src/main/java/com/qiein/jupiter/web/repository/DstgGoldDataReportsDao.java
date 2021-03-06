package com.qiein.jupiter.web.repository;

import com.qiein.jupiter.util.DBSplitUtil;
import com.qiein.jupiter.util.NumUtil;
import com.qiein.jupiter.util.StringUtil;
import com.qiein.jupiter.web.entity.vo.DsInvalidVO;
import com.qiein.jupiter.web.entity.vo.DstgGoldDataReportsVO;
import com.qiein.jupiter.web.entity.vo.ReportsParamVO;
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
 * @Date: 2018-8-10
 */
@Repository
public class DstgGoldDataReportsDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 获取电商推广广告信息汇总报表
     */
    public List<DstgGoldDataReportsVO> getDstgGoldDataReprots(ReportsParamVO reportsParamVO,DsInvalidVO invalidConfig) {
        //TODO 缺少添加条件
        List<DstgGoldDataReportsVO> dstgGoldDataReportsVOS = new ArrayList<>();
        getAdidList(reportsParamVO,dstgGoldDataReportsVOS);
        // 获取总客资
        getAllClientCount(reportsParamVO, dstgGoldDataReportsVOS);
        //获取待定量
        getPendingClientCount(reportsParamVO,dstgGoldDataReportsVOS,invalidConfig);
        //获取筛选待定
        getFilterWaitClientCount(reportsParamVO, dstgGoldDataReportsVOS);
        //获取筛选中
        getFilterInClientCount(reportsParamVO, dstgGoldDataReportsVOS);
        //获取筛选无效
        getFilterInValidClientCount(reportsParamVO, dstgGoldDataReportsVOS);
        //获取无效量
        getInValidClientCount(reportsParamVO,dstgGoldDataReportsVOS,invalidConfig);
        //入店量
        getComeShopClientCount(reportsParamVO, dstgGoldDataReportsVOS);
        //成交量
        getSuccessClientCount(reportsParamVO, dstgGoldDataReportsVOS);
        //入店成交量
        getComeShopSuccessClientCount(reportsParamVO, dstgGoldDataReportsVOS);
        //在线成交量
        getOnLineSuccessClientCount(reportsParamVO, dstgGoldDataReportsVOS);
        //成交均价
        getAvgAmount(reportsParamVO, dstgGoldDataReportsVOS);
        //成交总价
        getAmount(reportsParamVO, dstgGoldDataReportsVOS);
        //计算
        computerRate(reportsParamVO, dstgGoldDataReportsVOS,invalidConfig);
        //计算合计
        computerTotal(reportsParamVO, dstgGoldDataReportsVOS);
        return dstgGoldDataReportsVOS;
    }


    /**
     * 获取所有广告集合
     * */
    private void getAdidList(ReportsParamVO reportsParamVO, List<DstgGoldDataReportsVO> dstgGoldDataReportsVOS){
        StringBuilder sb = new StringBuilder();
        String infoTabName = DBSplitUtil.getInfoTabName(reportsParamVO.getCompanyId());
        String detailTabName = DBSplitUtil.getDetailTabName(reportsParamVO.getCompanyId());
        sb.append(" select distinct if((IFNULL(detail.adid,'-') ) = '', '-',IFNULL(detail.adid,'-')  ) as adid");
        sb.append(" from");
        sb.append(infoTabName + " info ," + detailTabName + " detail");
        sb.append(" where");
        sb.append(" info.kzid = detail.kzid");
        sb.append(" and info.isdel = 0");
        sb.append(" and (info.srctype = 1 or info.srctype = 2)");
        sb.append(" and (info.createtime between ? and ? or info.comeshoptime between ? and ? and info.successtime between ? and ? )");
        sb.append(" and info.companyid = ?");
        sb.append(" group by detail.adid");

        List<Map<String, Object>> dstgGoldDataReports = jdbcTemplate.queryForList(sb.toString(),
                new Object[]{reportsParamVO.getStart(),reportsParamVO.getEnd(),reportsParamVO.getStart(),reportsParamVO.getEnd(),reportsParamVO.getStart(),reportsParamVO.getEnd(),reportsParamVO.getCompanyId()});

        for (Map<String, Object> dstgGoldDataReport : dstgGoldDataReports) {
            DstgGoldDataReportsVO dstgGoldDataReportsVO = new DstgGoldDataReportsVO();
            dstgGoldDataReportsVO.setAdId((String) dstgGoldDataReport.get("adid"));
            dstgGoldDataReportsVOS.add(dstgGoldDataReportsVO);
        }

    }

    private StringBuilder getCommonsql(StringBuilder sb,String infoTabName,String detailTabName) {
        sb.append(" select info_detail_bak.adid,sum(info_detail_bak.client_count) client_count from ( " );
        sb.append(" select if((IFNULL(detail.adid,'-') ) = '', '-',IFNULL(detail.adid,'-')  ) as adid,count(detail.kzid) as client_count ");
        sb.append(" from");
        sb.append(infoTabName + " info ," + detailTabName + " detail");
        sb.append(" where");
        sb.append(" info.kzid = detail.kzid");
        sb.append(" and (info.srctype = 1 or info.srctype = 2)");
        sb.append(" and info.isdel = 0");
        sb.append(" and info.companyid = ?");
        return sb;
    }

    /**
     * 新增条件
     * */
    private void addConditionByType(String type,StringBuilder sb){
        if(StringUtil.isNotEmpty(type)){
            sb.append(" and info.typeid in ( "+type+")");
        }
    }


    /**
     * 获取广告总量
     */
    private void getAllClientCount(ReportsParamVO reportsParamVO, List<DstgGoldDataReportsVO> dstgGoldDataReportsVOS) {
        StringBuilder sb = new StringBuilder();
        String infoTabName = DBSplitUtil.getInfoTabName(reportsParamVO.getCompanyId());
        String detailTabName = DBSplitUtil.getDetailTabName(reportsParamVO.getCompanyId());
        sb =  getCommonsql(sb,infoTabName,detailTabName);
        sb.append(" and info.CREATETIME BETWEEN ? AND ?");
        addConditionByType(reportsParamVO.getType(),sb);
        sb.append(" group by detail.adid");
        sb.append(" ) info_detail_bak  group by info_detail_bak.adid ");
        List<Map<String, Object>> dstgGoldDataReports = jdbcTemplate.queryForList(sb.toString(),
                new Object[]{reportsParamVO.getCompanyId(),
                        reportsParamVO.getStart(),
                        reportsParamVO.getEnd()});

        List<DstgGoldDataReportsVO> dstgGoldDataReportsBak = new LinkedList<>();
        // 处理数据
        for (Map<String, Object> dstgGoldDataReport : dstgGoldDataReports) {
            DstgGoldDataReportsVO dstgGoldDataReportsVO = new DstgGoldDataReportsVO();
            dstgGoldDataReportsVO.setAdId((String) dstgGoldDataReport.get("adid"));
            dstgGoldDataReportsVO.setAllClientCount(Integer.parseInt( String.valueOf(dstgGoldDataReport.get("client_count").toString())));
            dstgGoldDataReportsBak.add(dstgGoldDataReportsVO);
        }

        for (DstgGoldDataReportsVO dstgGoldDataReportsVO : dstgGoldDataReportsVOS) {
            for (DstgGoldDataReportsVO dstgGoldDataReport : dstgGoldDataReportsBak) {
                if (dstgGoldDataReportsVO.getAdId().equalsIgnoreCase(dstgGoldDataReport.getAdId())) {
                    dstgGoldDataReportsVO.setAllClientCount(dstgGoldDataReport.getAllClientCount());
                    break;
                }
            }
        }

    }

    /**
     * 获取待定量
     */
    private void getPendingClientCount(ReportsParamVO reportsParamVO, List<DstgGoldDataReportsVO> dstgGoldDataReportsVOS,DsInvalidVO dsInvalidVO) {
        if(StringUtil.isEmpty(dsInvalidVO.getDsDdStatus())){
            return;
        }
        StringBuilder sb = new StringBuilder();
        String infoTabName = DBSplitUtil.getInfoTabName(reportsParamVO.getCompanyId());
        String detailTabName = DBSplitUtil.getDetailTabName(reportsParamVO.getCompanyId());
        sb = getCommonsql(sb, infoTabName, detailTabName);
        sb.append(" and info.CREATETIME BETWEEN ? AND ?");
//        sb.append(" AND INSTR( ?, CONCAT(',',info.STATUSID + '',',')) != 0");
//        sb.append(" and info.STATUSID in (?)");
        sb.append(" and info.STATUSID in ("+dsInvalidVO.getDsDdStatus()+")");
        addConditionByType(reportsParamVO.getType(),sb);
        sb.append(" group by detail.adid");
        sb.append(" ) info_detail_bak  group by info_detail_bak.adid ");
        List<Map<String, Object>> dstgGoldDataReports = jdbcTemplate.queryForList(sb.toString(),
                new Object[]{reportsParamVO.getCompanyId(),
                        reportsParamVO.getStart(),
                        reportsParamVO.getEnd()});

        // 处理数据
        List<DstgGoldDataReportsVO> dstgGoldDataReportsBak = new LinkedList<>();
        for (Map<String, Object> dstgGoldDataReport : dstgGoldDataReports) {
            DstgGoldDataReportsVO dstgGoldDataReportsVO = new DstgGoldDataReportsVO();
            dstgGoldDataReportsVO.setAdId((String) dstgGoldDataReport.get("adid"));
            dstgGoldDataReportsVO.setPendingClientCount(Integer.parseInt( String.valueOf(dstgGoldDataReport.get("client_count").toString())));
            dstgGoldDataReportsBak.add(dstgGoldDataReportsVO);
        }

        for (DstgGoldDataReportsVO dstgGoldDataReportsVO : dstgGoldDataReportsVOS) {
            for (DstgGoldDataReportsVO dstgGoldDataReport : dstgGoldDataReportsBak) {
                if (dstgGoldDataReportsVO.getAdId().equalsIgnoreCase(dstgGoldDataReport.getAdId())) {
                    dstgGoldDataReportsVO.setPendingClientCount(dstgGoldDataReport.getPendingClientCount());
                    break;
                }
            }
        }

    }
    /**
     * 获取已加微信数量
     */
    private void getWechatIsFlag(ReportsParamVO reportsParamVO, List<DstgGoldDataReportsVO> dstgGoldDataReportsVOS){
        StringBuilder sb = new StringBuilder();
        String infoTabName = DBSplitUtil.getInfoTabName(reportsParamVO.getCompanyId());
        String detailTabName = DBSplitUtil.getDetailTabName(reportsParamVO.getCompanyId());
        sb = getCommonsql(sb, infoTabName, detailTabName);
        sb.append(" and info.CREATETIME BETWEEN ? AND ?");
        sb.append(" and info.WEFLAG=1");
        addConditionByType(reportsParamVO.getType(),sb);
        sb.append(" group by detail.adid");
        sb.append(" ) info_detail_bak  group by info_detail_bak.adid ");
        List<Map<String, Object>> dstgGoldDataReports = jdbcTemplate.queryForList(sb.toString(),
                new Object[]{reportsParamVO.getCompanyId(),
                        reportsParamVO.getStart(),
                        reportsParamVO.getEnd()});


        // 处理数据
        List<DstgGoldDataReportsVO> dstgGoldDataReportsBak = new LinkedList<>();
        for (Map<String, Object> dstgGoldDataReport : dstgGoldDataReports) {
            DstgGoldDataReportsVO dstgGoldDataReportsVO = new DstgGoldDataReportsVO();
            dstgGoldDataReportsVO.setAdId((String) dstgGoldDataReport.get("adid"));
            dstgGoldDataReportsVO.setWechatFlagCount(Integer.parseInt( String.valueOf(dstgGoldDataReport.get("client_count").toString())));
            dstgGoldDataReportsBak.add(dstgGoldDataReportsVO);
        }

        for (DstgGoldDataReportsVO dstgGoldDataReportsVO : dstgGoldDataReportsVOS) {
            for (DstgGoldDataReportsVO dstgGoldDataReport : dstgGoldDataReportsBak) {
                if (dstgGoldDataReportsVO.getAdId().equalsIgnoreCase(dstgGoldDataReport.getAdId())) {
                    dstgGoldDataReportsVO.setWechatFlagCount(dstgGoldDataReport.getWechatFlagCount());
                    break;
                }
            }
        }
    }
    /**
     * 获取筛选待定
     * */
    private void getFilterWaitClientCount(ReportsParamVO reportsParamVO, List<DstgGoldDataReportsVO> dstgGoldDataReportsVOS){
        StringBuilder sb = new StringBuilder();
        String infoTabName = DBSplitUtil.getInfoTabName(reportsParamVO.getCompanyId());
        String detailTabName = DBSplitUtil.getDetailTabName(reportsParamVO.getCompanyId());
        sb = getCommonsql(sb, infoTabName, detailTabName);
        sb.append(" and info.CREATETIME BETWEEN ? AND ?");
        sb.append(" and  info.STATUSID = 98 ");
        addConditionByType(reportsParamVO.getType(),sb);
        sb.append(" group by detail.adid");
        sb.append(" ) info_detail_bak  group by info_detail_bak.adid ");
        List<Map<String, Object>> dstgGoldDataReports = jdbcTemplate.queryForList(sb.toString(),
                new Object[]{reportsParamVO.getCompanyId(),
                        reportsParamVO.getStart(),
                        reportsParamVO.getEnd()});


        // 处理数据
        List<DstgGoldDataReportsVO> dstgGoldDataReportsBak = new LinkedList<>();
        for (Map<String, Object> dstgGoldDataReport : dstgGoldDataReports) {
            DstgGoldDataReportsVO dstgGoldDataReportsVO = new DstgGoldDataReportsVO();
            dstgGoldDataReportsVO.setAdId((String) dstgGoldDataReport.get("adid"));
            dstgGoldDataReportsVO.setFilterPendingClientCount(Integer.parseInt( String.valueOf(dstgGoldDataReport.get("client_count").toString())));
            dstgGoldDataReportsBak.add(dstgGoldDataReportsVO);
        }

        for (DstgGoldDataReportsVO dstgGoldDataReportsVO : dstgGoldDataReportsVOS) {
            for (DstgGoldDataReportsVO dstgGoldDataReport : dstgGoldDataReportsBak) {
                if (dstgGoldDataReportsVO.getAdId().equalsIgnoreCase(dstgGoldDataReport.getAdId())) {
                    dstgGoldDataReportsVO.setFilterPendingClientCount(dstgGoldDataReport.getFilterPendingClientCount());
                    break;
                }
            }
        }
    }

    /**
     * 获取筛选无效量
     * */
    private void getFilterInValidClientCount(ReportsParamVO reportsParamVO, List<DstgGoldDataReportsVO> dstgGoldDataReportsVOS){
        StringBuilder sb = new StringBuilder();
        String infoTabName = DBSplitUtil.getInfoTabName(reportsParamVO.getCompanyId());
        String detailTabName = DBSplitUtil.getDetailTabName(reportsParamVO.getCompanyId());
        sb = getCommonsql(sb, infoTabName, detailTabName);
        sb.append(" and info.CREATETIME BETWEEN ? AND ?");
        sb.append(" and  info.STATUSID = 99");
        addConditionByType(reportsParamVO.getType(),sb);
        sb.append(" group by detail.adid");
        sb.append(" ) info_detail_bak  group by info_detail_bak.adid ");
        List<Map<String, Object>> dstgGoldDataReports = jdbcTemplate.queryForList(sb.toString(),
                new Object[]{reportsParamVO.getCompanyId(),
                        reportsParamVO.getStart(),
                        reportsParamVO.getEnd()});


        // 处理数据
        List<DstgGoldDataReportsVO> dstgGoldDataReportsBak = new LinkedList<>();
        for (Map<String, Object> dstgGoldDataReport : dstgGoldDataReports) {
            DstgGoldDataReportsVO dstgGoldDataReportsVO = new DstgGoldDataReportsVO();
            dstgGoldDataReportsVO.setAdId((String) dstgGoldDataReport.get("adid"));
            dstgGoldDataReportsVO.setFilterInValidClientCount(Integer.parseInt( String.valueOf(dstgGoldDataReport.get("client_count").toString())));
            dstgGoldDataReportsBak.add(dstgGoldDataReportsVO);
        }

        for (DstgGoldDataReportsVO dstgGoldDataReportsVO : dstgGoldDataReportsVOS) {
            for (DstgGoldDataReportsVO dstgGoldDataReport : dstgGoldDataReportsBak) {
                if (dstgGoldDataReportsVO.getAdId().equalsIgnoreCase(dstgGoldDataReport.getAdId())) {
                    dstgGoldDataReportsVO.setFilterInValidClientCount(dstgGoldDataReport.getFilterInValidClientCount());
                    break;
                }
            }
        }

    }

    /**
     * 获取筛选中
     * */
    private void getFilterInClientCount(ReportsParamVO reportsParamVO, List<DstgGoldDataReportsVO> dstgGoldDataReportsVOS){
        StringBuilder sb = new StringBuilder();
        String infoTabName = DBSplitUtil.getInfoTabName(reportsParamVO.getCompanyId());
        String detailTabName = DBSplitUtil.getDetailTabName(reportsParamVO.getCompanyId());
        sb = getCommonsql(sb, infoTabName, detailTabName);
        sb.append(" and info.CREATETIME BETWEEN ? AND ?");
        sb.append(" and  info.STATUSID = 0");
        addConditionByType(reportsParamVO.getType(),sb);
        sb.append(" group by detail.adid");
        sb.append(" ) info_detail_bak  group by info_detail_bak.adid ");
        List<Map<String, Object>> dstgGoldDataReports = jdbcTemplate.queryForList(sb.toString(),
                new Object[]{reportsParamVO.getCompanyId(),
                        reportsParamVO.getStart(),
                        reportsParamVO.getEnd()});


        // 处理数据
        List<DstgGoldDataReportsVO> dstgGoldDataReportsBak = new LinkedList<>();
        for (Map<String, Object> dstgGoldDataReport : dstgGoldDataReports) {
            DstgGoldDataReportsVO dstgGoldDataReportsVO = new DstgGoldDataReportsVO();
            dstgGoldDataReportsVO.setAdId((String) dstgGoldDataReport.get("adid"));
            dstgGoldDataReportsVO.setFilterInClientCount(Integer.parseInt( String.valueOf(dstgGoldDataReport.get("client_count").toString())));
            dstgGoldDataReportsBak.add(dstgGoldDataReportsVO);
        }

        for (DstgGoldDataReportsVO dstgGoldDataReportsVO : dstgGoldDataReportsVOS) {
            for (DstgGoldDataReportsVO dstgGoldDataReport : dstgGoldDataReportsBak) {
                if (dstgGoldDataReportsVO.getAdId().equalsIgnoreCase(dstgGoldDataReport.getAdId())) {
                    dstgGoldDataReportsVO.setFilterInClientCount(dstgGoldDataReport.getFilterInClientCount());
                    break;
                }
            }
        }

    }

    /**
     * 无效量
     * */
    public void getInValidClientCount(ReportsParamVO reportsParamVO, List<DstgGoldDataReportsVO> dstgGoldDataReportsVOS,DsInvalidVO dsInvalidVO){
        if(StringUtil.isEmpty(dsInvalidVO.getDsInvalidStatus()) && StringUtil.isEmpty(dsInvalidVO.getDsInvalidLevel())){
            return ;
        }
        StringBuilder sb = new StringBuilder();
        String infoTabName = DBSplitUtil.getInfoTabName(reportsParamVO.getCompanyId());
        String detailTabName = DBSplitUtil.getDetailTabName(reportsParamVO.getCompanyId());
        sb = getCommonsql(sb, infoTabName, detailTabName);
        sb.append(" and info.CREATETIME BETWEEN ? AND ?");
        addConditionByType(reportsParamVO.getType(),sb);
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
        sb.append(" group by detail.adid");
        sb.append(" ) info_detail_bak  group by info_detail_bak.adid ");
        List<Map<String, Object>> dstgGoldDataReports = jdbcTemplate.queryForList(sb.toString(),
                new Object[]{reportsParamVO.getCompanyId(),
                        reportsParamVO.getStart(),
                        reportsParamVO.getEnd()});

        // 处理数据
        List<DstgGoldDataReportsVO> dstgGoldDataReportsBak = new LinkedList<>();
        for (Map<String, Object> dstgGoldDataReport : dstgGoldDataReports) {
            DstgGoldDataReportsVO dstgGoldDataReportsVO = new DstgGoldDataReportsVO();
            dstgGoldDataReportsVO.setAdId((String) dstgGoldDataReport.get("adid"));
            dstgGoldDataReportsVO.setInValidClientCount(Integer.parseInt( String.valueOf(dstgGoldDataReport.get("client_count").toString())));
            dstgGoldDataReportsBak.add(dstgGoldDataReportsVO);
        }

        for (DstgGoldDataReportsVO dstgGoldDataReportsVO : dstgGoldDataReportsVOS) {
            for (DstgGoldDataReportsVO dstgGoldDataReport : dstgGoldDataReportsBak) {
                if (dstgGoldDataReportsVO.getAdId().equalsIgnoreCase(dstgGoldDataReport.getAdId())) {
                    dstgGoldDataReportsVO.setInValidClientCount(dstgGoldDataReport.getInValidClientCount());
                    break;
                }
            }
        }
    }




    /**
     * 入店量
     * */
    private void getComeShopClientCount(ReportsParamVO reportsParamVO, List<DstgGoldDataReportsVO> dstgGoldDataReportsVOS){
        StringBuilder sb = new StringBuilder();
        String infoTabName = DBSplitUtil.getInfoTabName(reportsParamVO.getCompanyId());
        String detailTabName = DBSplitUtil.getDetailTabName(reportsParamVO.getCompanyId());
        sb = getCommonsql(sb, infoTabName, detailTabName);
        sb.append(" and info.COMESHOPTIME BETWEEN ? AND ?");
        addConditionByType(reportsParamVO.getType(),sb);
        sb.append(" group by detail.adid");
        sb.append(" ) info_detail_bak  group by info_detail_bak.adid ");
        List<Map<String, Object>> dstgGoldDataReports = jdbcTemplate.queryForList(sb.toString(),
                new Object[]{reportsParamVO.getCompanyId(),
                        reportsParamVO.getStart(),
                        reportsParamVO.getEnd()});

        // 处理数据
        List<DstgGoldDataReportsVO> dstgGoldDataReportsBak = new LinkedList<>();
        for (Map<String, Object> dstgGoldDataReport : dstgGoldDataReports) {
            DstgGoldDataReportsVO dstgGoldDataReportsVO = new DstgGoldDataReportsVO();
            dstgGoldDataReportsVO.setAdId((String) dstgGoldDataReport.get("adid"));
            dstgGoldDataReportsVO.setComeShopClientCount(Integer.parseInt( String.valueOf(dstgGoldDataReport.get("client_count").toString())));
            dstgGoldDataReportsBak.add(dstgGoldDataReportsVO);
        }

        for (DstgGoldDataReportsVO dstgGoldDataReportsVO : dstgGoldDataReportsVOS) {
            for (DstgGoldDataReportsVO dstgGoldDataReport : dstgGoldDataReportsBak) {
                if (dstgGoldDataReportsVO.getAdId().equalsIgnoreCase(dstgGoldDataReport.getAdId())) {
                    dstgGoldDataReportsVO.setComeShopClientCount(dstgGoldDataReport.getComeShopClientCount());
                    break;
                }
            }
        }
    }


    /**
     * 入店成交量
     * */
    private void getComeShopSuccessClientCount(ReportsParamVO reportsParamVO, List<DstgGoldDataReportsVO> dstgGoldDataReportsVOS){
        StringBuilder sb = new StringBuilder();
        String infoTabName = DBSplitUtil.getInfoTabName(reportsParamVO.getCompanyId());
        String detailTabName = DBSplitUtil.getDetailTabName(reportsParamVO.getCompanyId());
        sb = getCommonsql(sb, infoTabName, detailTabName);
        sb.append(" and info.SUCCESSTIME BETWEEN ? AND ?");
        sb.append(" and info.statusid in (9,30)");
        addConditionByType(reportsParamVO.getType(),sb);
        sb.append(" group by detail.adid");
        sb.append(" ) info_detail_bak  group by info_detail_bak.adid ");
        List<Map<String, Object>> dstgGoldDataReports = jdbcTemplate.queryForList(sb.toString(),
                new Object[]{reportsParamVO.getCompanyId(),
                        reportsParamVO.getStart(),
                        reportsParamVO.getEnd()});

        // 处理数据
        List<DstgGoldDataReportsVO> dstgGoldDataReportsBak = new LinkedList<>();
        for (Map<String, Object> dstgGoldDataReport : dstgGoldDataReports) {
            DstgGoldDataReportsVO dstgGoldDataReportsVO = new DstgGoldDataReportsVO();
            dstgGoldDataReportsVO.setAdId((String) dstgGoldDataReport.get("adid"));
            dstgGoldDataReportsVO.setComeShopSuccessClientCount(Integer.parseInt( String.valueOf(dstgGoldDataReport.get("client_count").toString())));
            dstgGoldDataReportsBak.add(dstgGoldDataReportsVO);
        }

        for (DstgGoldDataReportsVO dstgGoldDataReportsVO : dstgGoldDataReportsVOS) {
            for (DstgGoldDataReportsVO dstgGoldDataReport : dstgGoldDataReportsBak) {
                if (dstgGoldDataReportsVO.getAdId().equalsIgnoreCase(dstgGoldDataReport.getAdId())) {
                    dstgGoldDataReportsVO.setComeShopSuccessClientCount(dstgGoldDataReport.getComeShopSuccessClientCount());
                    break;
                }
            }
        }
    }
    /**
     * 在线成交量
     * */
    private void getOnLineSuccessClientCount(ReportsParamVO reportsParamVO, List<DstgGoldDataReportsVO> dstgGoldDataReportsVOS){
        StringBuilder sb = new StringBuilder();
        String infoTabName = DBSplitUtil.getInfoTabName(reportsParamVO.getCompanyId());
        String detailTabName = DBSplitUtil.getDetailTabName(reportsParamVO.getCompanyId());
        sb = getCommonsql(sb, infoTabName, detailTabName);
        sb.append(" and info.SUCCESSTIME BETWEEN ? AND ?");
        sb.append(" and info.statusid in (40)");
        addConditionByType(reportsParamVO.getType(),sb);
        sb.append(" group by detail.adid");
        sb.append(" ) info_detail_bak  group by info_detail_bak.adid ");
        List<Map<String, Object>> dstgGoldDataReports = jdbcTemplate.queryForList(sb.toString(),
                new Object[]{reportsParamVO.getCompanyId(),
                        reportsParamVO.getStart(),
                        reportsParamVO.getEnd()});

        // 处理数据
        List<DstgGoldDataReportsVO> dstgGoldDataReportsBak = new LinkedList<>();
        for (Map<String, Object> dstgGoldDataReport : dstgGoldDataReports) {
            DstgGoldDataReportsVO dstgGoldDataReportsVO = new DstgGoldDataReportsVO();
            dstgGoldDataReportsVO.setAdId((String) dstgGoldDataReport.get("adid"));
            dstgGoldDataReportsVO.setOnLineSuccessClientCount(Integer.parseInt( String.valueOf(dstgGoldDataReport.get("client_count").toString())));
            dstgGoldDataReportsBak.add(dstgGoldDataReportsVO);
        }

        for (DstgGoldDataReportsVO dstgGoldDataReportsVO : dstgGoldDataReportsVOS) {
            for (DstgGoldDataReportsVO dstgGoldDataReport : dstgGoldDataReportsBak) {
                if (dstgGoldDataReportsVO.getAdId().equalsIgnoreCase(dstgGoldDataReport.getAdId())) {
                    dstgGoldDataReportsVO.setOnLineSuccessClientCount(dstgGoldDataReport.getOnLineSuccessClientCount());
                    break;
                }
            }
        }
    }
    /**
     * 成交量
     * */
    private void getSuccessClientCount(ReportsParamVO reportsParamVO, List<DstgGoldDataReportsVO> dstgGoldDataReportsVOS){
        StringBuilder sb = new StringBuilder();
        String infoTabName = DBSplitUtil.getInfoTabName(reportsParamVO.getCompanyId());
        String detailTabName = DBSplitUtil.getDetailTabName(reportsParamVO.getCompanyId());
        sb = getCommonsql(sb, infoTabName, detailTabName);
        sb.append(" and info.SUCCESSTIME BETWEEN ? AND ?");
        addConditionByType(reportsParamVO.getType(),sb);
        sb.append(" group by detail.adid");
        sb.append(" ) info_detail_bak  group by info_detail_bak.adid ");
        List<Map<String, Object>> dstgGoldDataReports = jdbcTemplate.queryForList(sb.toString(),
                new Object[]{reportsParamVO.getCompanyId(),
                        reportsParamVO.getStart(),
                        reportsParamVO.getEnd()});

        // 处理数据
        List<DstgGoldDataReportsVO> dstgGoldDataReportsBak = new LinkedList<>();
        for (Map<String, Object> dstgGoldDataReport : dstgGoldDataReports) {
            DstgGoldDataReportsVO dstgGoldDataReportsVO = new DstgGoldDataReportsVO();
            dstgGoldDataReportsVO.setAdId((String) dstgGoldDataReport.get("adid"));
            dstgGoldDataReportsVO.setSuccessClientCount(Integer.parseInt( String.valueOf(dstgGoldDataReport.get("client_count").toString())));
            dstgGoldDataReportsBak.add(dstgGoldDataReportsVO);
        }

        for (DstgGoldDataReportsVO dstgGoldDataReportsVO : dstgGoldDataReportsVOS) {
            for (DstgGoldDataReportsVO dstgGoldDataReport : dstgGoldDataReportsBak) {
                if (dstgGoldDataReportsVO.getAdId().equalsIgnoreCase(dstgGoldDataReport.getAdId())) {
                    dstgGoldDataReportsVO.setSuccessClientCount(dstgGoldDataReport.getSuccessClientCount());
                    break;
                }
            }
        }
    }


    /**
     * 成交均价
     * */
    private void getAvgAmount(ReportsParamVO reportsParamVO, List<DstgGoldDataReportsVO> dstgGoldDataReportsVOS){
        StringBuilder sb = new StringBuilder();
        String infoTabName = DBSplitUtil.getInfoTabName(reportsParamVO.getCompanyId());
        String detailTabName = DBSplitUtil.getDetailTabName(reportsParamVO.getCompanyId());
        sb.append(" select info_detail_bak.adid,sum(info_detail_bak.avg_amount) avg_amount from ( " );
        sb.append(" select if((IFNULL(detail.adid,'-') ) = '', '-',IFNULL(detail.adid,'-')  ) as adid,avg(detail.AMOUNT) as avg_amount ");
        sb.append(" from");
        sb.append(infoTabName + " info ," + detailTabName + " detail");
        sb.append(" where");
        sb.append(" info.kzid = detail.kzid");
        sb.append(" and info.isdel = 0");
        sb.append(" and (info.srctype = 1 or info.srctype = 2)");
        sb.append(" and info.companyid = ?");
        sb.append(" and info.SUCCESSTIME BETWEEN ? AND ?");
        addConditionByType(reportsParamVO.getType(),sb);
        sb.append(" group by detail.adid");
        sb.append(" ) info_detail_bak  group by info_detail_bak.adid ");

        List<Map<String, Object>> dstgGoldDataReports = jdbcTemplate.queryForList(sb.toString(),
                new Object[]{reportsParamVO.getCompanyId(),
                        reportsParamVO.getStart(),
                        reportsParamVO.getEnd()});

        // 处理数据
        List<DstgGoldDataReportsVO> dstgGoldDataReportsBak = new LinkedList<>();
        for (Map<String, Object> dstgGoldDataReport : dstgGoldDataReports) {
            DstgGoldDataReportsVO dstgGoldDataReportsVO = new DstgGoldDataReportsVO();
            dstgGoldDataReportsVO.setAdId((String) dstgGoldDataReport.get("adid"));
            Object avg_amount = dstgGoldDataReport.get("avg_amount");
            if(avg_amount == null){
                dstgGoldDataReportsVO.setAvgAmount(0);
            }else{

                dstgGoldDataReportsVO.setAvgAmount(((BigDecimal)avg_amount ).doubleValue());
            }
            dstgGoldDataReportsBak.add(dstgGoldDataReportsVO);
        }

        for (DstgGoldDataReportsVO dstgGoldDataReportsVO : dstgGoldDataReportsVOS) {
            for (DstgGoldDataReportsVO dstgGoldDataReport : dstgGoldDataReportsBak) {
                if (dstgGoldDataReportsVO.getAdId().equalsIgnoreCase(dstgGoldDataReport.getAdId())) {
                    dstgGoldDataReportsVO.setAvgAmount(dstgGoldDataReport.getAvgAmount());
                    break;
                }
            }
        }
    }

    /**
     * 成交总价
     * */
    private void getAmount(ReportsParamVO reportsParamVO, List<DstgGoldDataReportsVO> dstgGoldDataReportsVOS){
        StringBuilder sb = new StringBuilder();
        String infoTabName = DBSplitUtil.getInfoTabName(reportsParamVO.getCompanyId());
        String detailTabName = DBSplitUtil.getDetailTabName(reportsParamVO.getCompanyId());
        sb.append(" select info_detail_bak.adid,sum(info_detail_bak.sum_amount) sum_amount from ( " );
        sb.append(" select if((IFNULL(detail.adid,'-') ) = '', '-',IFNULL(detail.adid,'-')  ) as adid,sum(detail.AMOUNT) as sum_amount ");
        sb.append(" from");
        sb.append(infoTabName + " info ," + detailTabName + " detail");
        sb.append(" where");
        sb.append(" info.kzid = detail.kzid");
        sb.append(" and info.isdel = 0");
        sb.append(" and (info.srctype = 1 or info.srctype = 2)");
        sb.append(" and info.companyid = ?");
        sb.append(" and info.SUCCESSTIME BETWEEN ? AND ?");
        addConditionByType(reportsParamVO.getType(),sb);
        sb.append(" group by detail.adid");
        sb.append(" ) info_detail_bak  group by info_detail_bak.adid ");
        List<Map<String, Object>> dstgGoldDataReports = jdbcTemplate.queryForList(sb.toString(),
                new Object[]{reportsParamVO.getCompanyId(),
                        reportsParamVO.getStart(),
                        reportsParamVO.getEnd()});

        // 处理数据
        List<DstgGoldDataReportsVO> dstgGoldDataReportsBak = new LinkedList<>();
        for (Map<String, Object> dstgGoldDataReport : dstgGoldDataReports) {
            DstgGoldDataReportsVO dstgGoldDataReportsVO = new DstgGoldDataReportsVO();
            dstgGoldDataReportsVO.setAdId((String) dstgGoldDataReport.get("adid"));
            if(dstgGoldDataReport.get("sum_amount") == null){
                dstgGoldDataReportsVO.setAmount(0);
            }else{
                dstgGoldDataReportsVO.setAmount(((BigDecimal) dstgGoldDataReport.get("sum_amount")).doubleValue());
            }
            dstgGoldDataReportsBak.add(dstgGoldDataReportsVO);
        }
        for (DstgGoldDataReportsVO dstgGoldDataReportsVO : dstgGoldDataReportsVOS) {
            for (DstgGoldDataReportsVO dstgGoldDataReport : dstgGoldDataReportsBak) {
                if (dstgGoldDataReportsVO.getAdId().equalsIgnoreCase(dstgGoldDataReport.getAdId())) {
                    dstgGoldDataReportsVO.setAmount(dstgGoldDataReport.getAmount());
                    break;
                }
            }
        }
    }

    /**
     *  计算Rate
     */
    private void computerRate(ReportsParamVO reportsParamVO, List<DstgGoldDataReportsVO> dstgGoldDataReportsVOS,DsInvalidVO invalidConfig){
        for (DstgGoldDataReportsVO dstgGoldDataReportsVO:dstgGoldDataReportsVOS){
            //有效量
            if(invalidConfig.getDdIsValid()){
                dstgGoldDataReportsVO.setValidClientCount(dstgGoldDataReportsVO.getAllClientCount()-dstgGoldDataReportsVO.getInValidClientCount()-dstgGoldDataReportsVO.getFilterInClientCount()-dstgGoldDataReportsVO.getFilterInValidClientCount()-dstgGoldDataReportsVO.getFilterPendingClientCount());
            }else{
                dstgGoldDataReportsVO.setValidClientCount(dstgGoldDataReportsVO.getAllClientCount()-dstgGoldDataReportsVO.getPendingClientCount()-dstgGoldDataReportsVO.getInValidClientCount()-dstgGoldDataReportsVO.getFilterInClientCount()-dstgGoldDataReportsVO.getFilterInValidClientCount()-dstgGoldDataReportsVO.getFilterPendingClientCount());
            }
            //客资量(总客资-筛选待定-筛选中-筛选无效)
            dstgGoldDataReportsVO.setClientCount(dstgGoldDataReportsVO.getAllClientCount()-dstgGoldDataReportsVO.getFilterPendingClientCount()-dstgGoldDataReportsVO.getFilterInValidClientCount()-dstgGoldDataReportsVO.getFilterInClientCount());
            //有效率
            double validRate = (double) dstgGoldDataReportsVO.getValidClientCount() / dstgGoldDataReportsVO.getClientCount();
            dstgGoldDataReportsVO.setValidRate(parseDouble(((Double.isNaN(validRate) || Double.isInfinite(validRate)) ? 0.0 : validRate) * 100));
            //无效率
            double invalidRate = (double) dstgGoldDataReportsVO.getInValidClientCount() / dstgGoldDataReportsVO.getClientCount();
            dstgGoldDataReportsVO.setInValidRate(parseDouble(((Double.isNaN(invalidRate) || Double.isInfinite(invalidRate)) ? 0.0 : invalidRate) * 100));
            //待定率
            double waitRate = (double) dstgGoldDataReportsVO.getPendingClientCount() / dstgGoldDataReportsVO.getClientCount();
            dstgGoldDataReportsVO.setWaitRate(parseDouble(((Double.isNaN(waitRate) || Double.isInfinite(waitRate)) ? 0.0 : waitRate) * 100));
            //毛客资入店率
            double clientComeShopRate = (double) dstgGoldDataReportsVO.getComeShopClientCount() / dstgGoldDataReportsVO.getClientCount();
            dstgGoldDataReportsVO.setClientComeShopRate(parseDouble(((Double.isNaN(clientComeShopRate) || Double.isInfinite(clientComeShopRate)) ? 0.0 : clientComeShopRate) * 100));
            //有效客资入店率
            double validComeShopRate = (double) dstgGoldDataReportsVO.getComeShopClientCount() / dstgGoldDataReportsVO.getValidClientCount();
            dstgGoldDataReportsVO.setValidClientComeShopRate(parseDouble(((Double.isNaN(validComeShopRate) || Double.isInfinite(validComeShopRate)) ? 0.0 : validComeShopRate) * 100));
            //毛客资成交率
            double successRate = (double) dstgGoldDataReportsVO.getSuccessClientCount() / dstgGoldDataReportsVO.getClientCount();
            dstgGoldDataReportsVO.setClientSuccessRate(parseDouble(((Double.isNaN(successRate) || Double.isInfinite(successRate)) ? 0.0 : successRate) * 100));
            //有效客资成交率
            double validSuccessRate = (double) dstgGoldDataReportsVO.getSuccessClientCount() / dstgGoldDataReportsVO.getValidClientCount();
            dstgGoldDataReportsVO.setValidClientSuccessRate(parseDouble(((Double.isNaN(validSuccessRate) || Double.isInfinite(validSuccessRate)) ? 0.0 : validSuccessRate) * 100));
            //入店成交率
            double comeShopSuccessRate = (double) dstgGoldDataReportsVO.getSuccessClientCount() / dstgGoldDataReportsVO.getComeShopClientCount();
            dstgGoldDataReportsVO.setComeShopSuccessRate(parseDouble(((Double.isNaN(comeShopSuccessRate) || Double.isInfinite(comeShopSuccessRate)) ? 0.0 : comeShopSuccessRate) * 100));

        }
    }

    /**
     * 计算总计
     * */
    private void computerTotal(ReportsParamVO reportsParamVO, List<DstgGoldDataReportsVO> dstgGoldDataReportsVOS){
        DstgGoldDataReportsVO dstgReportsTotal = new DstgGoldDataReportsVO();
        dstgReportsTotal.setAdId("合计");
        for (DstgGoldDataReportsVO dstgReportsVO : dstgGoldDataReportsVOS) {
            dstgReportsTotal.setAllClientCount(dstgReportsVO.getAllClientCount() + dstgReportsTotal.getAllClientCount());
            dstgReportsTotal.setClientCount(dstgReportsVO.getClientCount() + dstgReportsTotal.getClientCount());
            dstgReportsTotal.setValidClientCount(dstgReportsVO.getValidClientCount() + dstgReportsTotal.getValidClientCount());
            dstgReportsTotal.setPendingClientCount(dstgReportsVO.getPendingClientCount() + dstgReportsTotal.getPendingClientCount());
            dstgReportsTotal.setInValidClientCount(dstgReportsVO.getInValidClientCount() + dstgReportsTotal.getInValidClientCount());
            dstgReportsTotal.setComeShopClientCount(dstgReportsVO.getComeShopClientCount() + dstgReportsTotal.getComeShopClientCount());
            dstgReportsTotal.setWechatFlagCount(dstgReportsVO.getWechatFlagCount()+dstgReportsTotal.getWechatFlagCount());
            dstgReportsTotal.setSuccessClientCount(dstgReportsVO.getSuccessClientCount() + dstgReportsTotal.getSuccessClientCount());
            dstgReportsTotal.setOnLineSuccessClientCount(dstgReportsVO.getOnLineSuccessClientCount() + dstgReportsTotal.getOnLineSuccessClientCount());
            dstgReportsTotal.setComeShopSuccessClientCount(dstgReportsVO.getComeShopSuccessClientCount() + dstgReportsTotal.getComeShopSuccessClientCount());
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

        dstgGoldDataReportsVOS.add(0,dstgReportsTotal);
    }

    /**
     * 只保留2位小数
     * */
    public double parseDouble(double result){
        return Double.parseDouble(String.format("%.2f",result));
    }

}
