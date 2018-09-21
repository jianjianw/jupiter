package com.qiein.jupiter.web.repository;

import com.qiein.jupiter.util.StringUtil;
import com.qiein.jupiter.web.entity.vo.DsInvalidVO;
import com.qiein.jupiter.web.entity.vo.DstgChannelReportsOrderBySrcVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author: xiangliang
 */
@Repository
public class DstgChannelReportsOrderBySrcDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    /**
     * 获取电商推广广告信息汇总报表
     */
    public List<DstgChannelReportsOrderBySrcVO> getDstgChannelReportsOrderBySrc(String groupId,Integer companyId,String start,String end,String sourceIds,String typeIds,DsInvalidVO invalidConfig) {
        //TODO 缺少添加条件
        List<DstgChannelReportsOrderBySrcVO> dstgChannelReportsOrderBySrcVOS = new ArrayList<>();
        getSrc(groupId,companyId,start,end,sourceIds,typeIds,dstgChannelReportsOrderBySrcVOS);
        // 获取总客资
        getAllClientCount(groupId,companyId,start,end,sourceIds,typeIds,dstgChannelReportsOrderBySrcVOS);
        //获取待定量
        getPendingClientCount(groupId,companyId,start,end,sourceIds,typeIds,dstgChannelReportsOrderBySrcVOS,invalidConfig);
        //获取筛选待定
        getFilterWaitClientCount(groupId,companyId,start,end,sourceIds,typeIds,dstgChannelReportsOrderBySrcVOS);
        //获取筛选中
        getFilterInClientCount(groupId,companyId,start,end,sourceIds,typeIds,dstgChannelReportsOrderBySrcVOS);
        //获取筛选无效
        getFilterInValidClientCount(groupId,companyId,start,end,sourceIds,typeIds,dstgChannelReportsOrderBySrcVOS);
        //获取无效量
        getInValidClientCount(groupId,companyId,start,end,sourceIds,typeIds,dstgChannelReportsOrderBySrcVOS,invalidConfig);
        //入店量,
        getComeShopClientCount(groupId,companyId,start,end,sourceIds,typeIds,dstgChannelReportsOrderBySrcVOS);
        //成交量
        getSuccessClientCount(groupId,companyId,start,end,sourceIds,typeIds,dstgChannelReportsOrderBySrcVOS);
        //成交均价
        getAvgAmount(groupId,companyId,start,end,sourceIds,typeIds,dstgChannelReportsOrderBySrcVOS);
        //成交总价
        getAmount(groupId,companyId,start,end,sourceIds,typeIds,dstgChannelReportsOrderBySrcVOS);
        //计算
        computerRate(dstgChannelReportsOrderBySrcVOS,invalidConfig);
        //计算合计
        computerTotal(dstgChannelReportsOrderBySrcVOS);
        return dstgChannelReportsOrderBySrcVOS;
    }
    /**
     * 获取所有渠道集合
     * */
    private void getSrc(String groupId,Integer companyId,String start,String end,String sourceIds,String typeIds,List<DstgChannelReportsOrderBySrcVO> dstgChannelReportsOrderBySrcVOS){
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT info.SOURCEID srcId,");
        sb.append(" src.srcname srcname");
        sb.append(" FROM hm_crm_client_info info");
        sb.append(" LEFT JOIN hm_crm_client_detail detail ON info.KZID = detail.KZID AND info.COMPANYID = detail.COMPANYID");
        sb.append(" LEFT JOIN hm_crm_source src ON src.id = info.SOURCEID");
        sb.append(" WHERE info.COLLECTORID in ( select distinct  gp_staff.staffid from hm_pub_group gp");
        sb.append(" left join hm_pub_group_staff gp_staff on gp.groupid = gp_staff.groupid  ");
        sb.append(" where gp.groupid = ? and gp.grouptype = 'dscj') ");
      //  sb.append(" and info.sourceid in ("+sourceIds+")");
      //  sb.append(" and info.typeid in ("+typeIds+")");
        sb.append(" AND src.SRCNAME IS NOT NULL");
        sb.append(" and info.companyid=?");
        sb.append(" and info.createtime between  ? and  ?");
        sb.append(" GROUP BY info.SOURCEID");

        List<Map<String, Object>> dstgGoldDataReports = jdbcTemplate.queryForList(sb.toString(),
                new Object[]{groupId,companyId,start,end});

        for (Map<String, Object> dstgGoldDataReport : dstgGoldDataReports) {
            DstgChannelReportsOrderBySrcVO dstgChannelReportsOrderBySrcVO = new DstgChannelReportsOrderBySrcVO();
            dstgChannelReportsOrderBySrcVO.setSrcId(Integer.parseInt(String.valueOf(dstgGoldDataReport.get("srcId")) ));
            dstgChannelReportsOrderBySrcVO.setSrcName((String) dstgGoldDataReport.get("srcname"));
            dstgChannelReportsOrderBySrcVOS.add(dstgChannelReportsOrderBySrcVO);
        }
    }
    /**
     * 获取总客资量
     */
    private void getAllClientCount(String groupId,Integer companyId,String start,String end,String sourceIds,String typeIds,List<DstgChannelReportsOrderBySrcVO> dstgChannelReportsOrderBySrcVOS) {
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT info.SOURCEID srcId,");
        sb.append(" count(1) count,");
        sb.append(" src.srcname srcname");
        sb.append(" FROM hm_crm_client_info info");
        sb.append(" LEFT JOIN hm_crm_client_detail detail ON info.KZID = detail.KZID AND info.COMPANYID = detail.COMPANYID");
        sb.append(" LEFT JOIN hm_crm_source src ON src.id = info.SOURCEID");
        sb.append(" WHERE info.COLLECTORI in ( select distinct  gp_staff.staffid from hm_pub_group gp");
        sb.append(" left join hm_pub_group_staff gp_staff on gp.groupid = gp_staff.groupid  ");
        sb.append(" where gp.groupid = ? and gp.grouptype = 'dscj') ");
        //  sb.append(" and info.sourceid in ("+sourceIds+")");
        //  sb.append(" and info.typeid in ("+typeIds+")");
        sb.append(" AND src.SRCNAME IS NOT NULL");
        sb.append(" and info.companyid=?");
        sb.append(" and info.createtime between  ? and  ?");
        sb.append(" GROUP BY info.SOURCEID");

        List<Map<String, Object>> dstgGoldDataReports = jdbcTemplate.queryForList(sb.toString(),
                new Object[]{groupId,companyId,start,end});
        List<DstgChannelReportsOrderBySrcVO> dstgChannelReportsOrderBySrcBak=new ArrayList<>();
        for (Map<String, Object> dstgGoldDataReport : dstgGoldDataReports) {
            DstgChannelReportsOrderBySrcVO dstgChannelReportsOrderBySrcVO = new DstgChannelReportsOrderBySrcVO();
            dstgChannelReportsOrderBySrcVO.setSrcId(Integer.parseInt(String.valueOf(dstgGoldDataReport.get("srcId"))));
            dstgChannelReportsOrderBySrcVO.setSrcName((String) dstgGoldDataReport.get("srcname"));
            dstgChannelReportsOrderBySrcVO.setAllClientCount(Integer.parseInt(String.valueOf(dstgGoldDataReport.get("count"))));
            dstgChannelReportsOrderBySrcBak.add(dstgChannelReportsOrderBySrcVO);
        }

        for (DstgChannelReportsOrderBySrcVO dstgChannelReportsOrderBySrcVO : dstgChannelReportsOrderBySrcVOS) {
            for (DstgChannelReportsOrderBySrcVO dstgChannelReportsOrderBySrcVO1 : dstgChannelReportsOrderBySrcBak) {
                if (dstgChannelReportsOrderBySrcVO.getSrcId()==dstgChannelReportsOrderBySrcVO1.getSrcId()) {
                    dstgChannelReportsOrderBySrcVO.setAllClientCount(dstgChannelReportsOrderBySrcVO1.getAllClientCount());
                    break;
                }
            }
        }

    }
    /**
     * 获取待定量
     */
    private void getPendingClientCount(String groupId,Integer companyId,String start,String end,String sourceIds,String typeIds,List<DstgChannelReportsOrderBySrcVO> dstgChannelReportsOrderBySrcVOS,DsInvalidVO dsInvalidVO) {
        if(StringUtil.isEmpty(dsInvalidVO.getDsDdStatus())){
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT info.SOURCEID srcId,");
        sb.append(" count(1) count,");
        sb.append(" src.srcname srcname");
        sb.append(" FROM hm_crm_client_info info");
        sb.append(" LEFT JOIN hm_crm_client_detail detail ON info.KZID = detail.KZID AND info.COMPANYID = detail.COMPANYID");
        sb.append(" LEFT JOIN hm_crm_source src ON src.id = info.SOURCEID");
        sb.append(" WHERE info.COLLECTORI in ( select distinct  gp_staff.staffid from hm_pub_group gp");
        sb.append(" left join hm_pub_group_staff gp_staff on gp.groupid = gp_staff.groupid  ");
        sb.append(" where gp.groupid = ? and gp.grouptype = 'dscj') ");
        //  sb.append(" and info.sourceid in ("+sourceIds+")");
        //  sb.append(" and info.typeid in ("+typeIds+")");
        sb.append(" AND src.SRCNAME IS NOT NULL");
        sb.append(" and info.companyid=?");
        sb.append(" and info.createtime between  ? and  ?");
//        sb.append(" and info.STATUSID in (?)");
        sb.append(" and info.STATUSID in ("+dsInvalidVO.getDsDdStatus()+")");
        sb.append(" GROUP BY info.SOURCEID");

        List<Map<String, Object>> dstgGoldDataReports = jdbcTemplate.queryForList(sb.toString(),
                new Object[]{groupId,companyId,start,end});
        List<DstgChannelReportsOrderBySrcVO> dstgChannelReportsOrderBySrcBak=new ArrayList<>();
        for (Map<String, Object> dstgGoldDataReport : dstgGoldDataReports) {
            DstgChannelReportsOrderBySrcVO dstgChannelReportsOrderBySrcVO = new DstgChannelReportsOrderBySrcVO();
            dstgChannelReportsOrderBySrcVO.setSrcId(Integer.parseInt(String.valueOf(dstgGoldDataReport.get("srcId"))));
            dstgChannelReportsOrderBySrcVO.setSrcName((String) dstgGoldDataReport.get("srcname"));
            dstgChannelReportsOrderBySrcVO.setPendingClientCount(Integer.parseInt(String.valueOf(dstgGoldDataReport.get("count"))));
            dstgChannelReportsOrderBySrcBak.add(dstgChannelReportsOrderBySrcVO);
        }

        for (DstgChannelReportsOrderBySrcVO dstgChannelReportsOrderBySrcVO : dstgChannelReportsOrderBySrcVOS) {
            for (DstgChannelReportsOrderBySrcVO dstgChannelReportsOrderBySrcVO1 : dstgChannelReportsOrderBySrcBak) {
                if (dstgChannelReportsOrderBySrcVO.getSrcId()==dstgChannelReportsOrderBySrcVO1.getSrcId()) {
                    dstgChannelReportsOrderBySrcVO.setPendingClientCount(dstgChannelReportsOrderBySrcVO1.getPendingClientCount());
                    break;
                }
            }
        }

    }

    /**
     * 获取筛选待定
     * */
    private void getFilterWaitClientCount(String groupId,Integer companyId,String start,String end,String sourceIds,String typeIds,List<DstgChannelReportsOrderBySrcVO> dstgChannelReportsOrderBySrcVOS){
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT info.SOURCEID srcId,");
        sb.append(" count(1) count,");
        sb.append(" src.srcname srcname");
        sb.append(" FROM hm_crm_client_info info");
        sb.append(" LEFT JOIN hm_crm_client_detail detail ON info.KZID = detail.KZID AND info.COMPANYID = detail.COMPANYID");
        sb.append(" LEFT JOIN hm_crm_source src ON src.id = info.SOURCEID");
        sb.append(" WHERE info.COLLECTORI in ( select distinct  gp_staff.staffid from hm_pub_group gp");
        sb.append(" left join hm_pub_group_staff gp_staff on gp.groupid = gp_staff.groupid  ");
        sb.append(" where gp.groupid = ? and gp.grouptype = 'dscj') ");
        //  sb.append(" and info.sourceid in ("+sourceIds+")");
        //  sb.append(" and info.typeid in ("+typeIds+")");
        sb.append(" AND src.SRCNAME IS NOT NULL");
        sb.append(" and info.companyid=?");
        sb.append(" and info.createtime between  ? and  ?");
        sb.append(" and  info.STATUSID = 98 ");
        sb.append(" GROUP BY info.SOURCEID");

        List<Map<String, Object>> dstgGoldDataReports = jdbcTemplate.queryForList(sb.toString(),
                new Object[]{groupId,companyId,start,end});
        List<DstgChannelReportsOrderBySrcVO> dstgChannelReportsOrderBySrcBak=new ArrayList<>();
        for (Map<String, Object> dstgGoldDataReport : dstgGoldDataReports) {
            DstgChannelReportsOrderBySrcVO dstgChannelReportsOrderBySrcVO = new DstgChannelReportsOrderBySrcVO();
            dstgChannelReportsOrderBySrcVO.setSrcId(Integer.parseInt(String.valueOf(dstgGoldDataReport.get("srcId"))));
            dstgChannelReportsOrderBySrcVO.setSrcName((String) dstgGoldDataReport.get("srcname"));
            dstgChannelReportsOrderBySrcVO.setFilterPendingClientCount(Integer.parseInt(String.valueOf(dstgGoldDataReport.get("count"))));
            dstgChannelReportsOrderBySrcBak.add(dstgChannelReportsOrderBySrcVO);
        }

        for (DstgChannelReportsOrderBySrcVO dstgChannelReportsOrderBySrcVO : dstgChannelReportsOrderBySrcVOS) {
            for (DstgChannelReportsOrderBySrcVO dstgChannelReportsOrderBySrcVO1 : dstgChannelReportsOrderBySrcBak) {
                if (dstgChannelReportsOrderBySrcVO.getSrcId()==dstgChannelReportsOrderBySrcVO1.getSrcId()) {
                    dstgChannelReportsOrderBySrcVO.setFilterPendingClientCount(dstgChannelReportsOrderBySrcVO1.getFilterPendingClientCount());
                    break;
                }
            }
        }
    }

    /**
     * 获取筛选无效量
     * */
    private void getFilterInValidClientCount(String groupId,Integer companyId,String start,String end,String sourceIds,String typeIds,List<DstgChannelReportsOrderBySrcVO> dstgChannelReportsOrderBySrcVOS){
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT info.SOURCEID srcId,");
        sb.append(" count(1) count,");
        sb.append(" src.srcname srcname");
        sb.append(" FROM hm_crm_client_info info");
        sb.append(" LEFT JOIN hm_crm_client_detail detail ON info.KZID = detail.KZID AND info.COMPANYID = detail.COMPANYID");
        sb.append(" LEFT JOIN hm_crm_source src ON src.id = info.SOURCEID");
        sb.append(" WHERE info.COLLECTORI in ( select distinct  gp_staff.staffid from hm_pub_group gp");
        sb.append(" left join hm_pub_group_staff gp_staff on gp.groupid = gp_staff.groupid  ");
        sb.append(" where gp.groupid = ? and gp.grouptype = 'dscj') ");
        //  sb.append(" and info.sourceid in ("+sourceIds+")");
        //  sb.append(" and info.typeid in ("+typeIds+")");
        sb.append(" AND src.SRCNAME IS NOT NULL");
        sb.append(" and  info.STATUSID = 99");
        sb.append(" and info.companyid=?");
        sb.append(" and info.createtime between  ? and  ?");
        sb.append(" GROUP BY info.SOURCEID");

        List<Map<String, Object>> dstgGoldDataReports = jdbcTemplate.queryForList(sb.toString(),
                new Object[]{groupId,companyId,start,end});
        List<DstgChannelReportsOrderBySrcVO> dstgChannelReportsOrderBySrcBak=new ArrayList<>();
        for (Map<String, Object> dstgGoldDataReport : dstgGoldDataReports) {
            DstgChannelReportsOrderBySrcVO dstgChannelReportsOrderBySrcVO = new DstgChannelReportsOrderBySrcVO();
            dstgChannelReportsOrderBySrcVO.setSrcId(Integer.parseInt(String.valueOf(dstgGoldDataReport.get("srcId"))));
            dstgChannelReportsOrderBySrcVO.setSrcName((String) dstgGoldDataReport.get("srcname"));
            dstgChannelReportsOrderBySrcVO.setFilterInValidClientCount(Integer.parseInt(String.valueOf(dstgGoldDataReport.get("count"))));
            dstgChannelReportsOrderBySrcBak.add(dstgChannelReportsOrderBySrcVO);
        }

        for (DstgChannelReportsOrderBySrcVO dstgChannelReportsOrderBySrcVO : dstgChannelReportsOrderBySrcVOS) {
            for (DstgChannelReportsOrderBySrcVO dstgChannelReportsOrderBySrcVO1 : dstgChannelReportsOrderBySrcBak) {
                if (dstgChannelReportsOrderBySrcVO.getSrcId()==dstgChannelReportsOrderBySrcVO1.getSrcId()) {
                    dstgChannelReportsOrderBySrcVO.setFilterInValidClientCount(dstgChannelReportsOrderBySrcVO1.getFilterInValidClientCount());
                    break;
                }
            }
        }

    }

    /**
     * 获取筛选中
     * */
    private void getFilterInClientCount(String groupId,Integer companyId,String start,String end,String sourceIds,String typeIds,List<DstgChannelReportsOrderBySrcVO> dstgChannelReportsOrderBySrcVOS){
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT info.SOURCEID srcId,");
        sb.append(" count(1) count,");
        sb.append(" src.srcname srcname");
        sb.append(" FROM hm_crm_client_info info");
        sb.append(" LEFT JOIN hm_crm_client_detail detail ON info.KZID = detail.KZID AND info.COMPANYID = detail.COMPANYID");
        sb.append(" LEFT JOIN hm_crm_source src ON src.id = info.SOURCEID");
        sb.append(" WHERE info.COLLECTORI in ( select distinct  gp_staff.staffid from hm_pub_group gp");
        sb.append(" left join hm_pub_group_staff gp_staff on gp.groupid = gp_staff.groupid  ");
        sb.append(" where gp.groupid = ? and gp.grouptype = 'dscj') ");
        //  sb.append(" and info.sourceid in ("+sourceIds+")");
        //  sb.append(" and info.typeid in ("+typeIds+")");
        sb.append(" AND src.SRCNAME IS NOT NULL");
        sb.append(" and  info.STATUSID = 0");
        sb.append(" and info.companyid=?");
        sb.append(" and info.createtime between  ? and  ?");
        sb.append(" GROUP BY info.SOURCEID");

        List<Map<String, Object>> dstgGoldDataReports = jdbcTemplate.queryForList(sb.toString(),
                new Object[]{groupId,companyId,start,end});
        List<DstgChannelReportsOrderBySrcVO> dstgChannelReportsOrderBySrcBak=new ArrayList<>();
        for (Map<String, Object> dstgGoldDataReport : dstgGoldDataReports) {
            DstgChannelReportsOrderBySrcVO dstgChannelReportsOrderBySrcVO = new DstgChannelReportsOrderBySrcVO();
            dstgChannelReportsOrderBySrcVO.setSrcId(Integer.parseInt(String.valueOf(dstgGoldDataReport.get("srcId"))));
            dstgChannelReportsOrderBySrcVO.setSrcName((String) dstgGoldDataReport.get("srcname"));
            dstgChannelReportsOrderBySrcVO.setFilterInClientCount(Integer.parseInt(String.valueOf(dstgGoldDataReport.get("count"))));
            dstgChannelReportsOrderBySrcBak.add(dstgChannelReportsOrderBySrcVO);
        }

        for (DstgChannelReportsOrderBySrcVO dstgChannelReportsOrderBySrcVO : dstgChannelReportsOrderBySrcVOS) {
            for (DstgChannelReportsOrderBySrcVO dstgChannelReportsOrderBySrcVO1 : dstgChannelReportsOrderBySrcBak) {
                if (dstgChannelReportsOrderBySrcVO.getSrcId()==dstgChannelReportsOrderBySrcVO1.getSrcId()) {
                    dstgChannelReportsOrderBySrcVO.setFilterInClientCount(dstgChannelReportsOrderBySrcVO1.getFilterInClientCount());
                    break;
                }
            }
        }

    }

    /**
     * 无效量
     * */
    public void getInValidClientCount(String groupId,Integer companyId,String start,String end,String sourceIds,String typeIds,List<DstgChannelReportsOrderBySrcVO> dstgChannelReportsOrderBySrcVOS,DsInvalidVO dsInvalidVO){
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT info.SOURCEID srcId,");
        sb.append(" count(1) count,");
        sb.append(" src.srcname srcname");
        sb.append(" FROM hm_crm_client_info info");
        sb.append(" LEFT JOIN hm_crm_client_detail detail ON info.KZID = detail.KZID AND info.COMPANYID = detail.COMPANYID");
        sb.append(" LEFT JOIN hm_crm_source src ON src.id = info.SOURCEID");
        sb.append(" WHERE info.COLLECTORI in ( select distinct  gp_staff.staffid from hm_pub_group gp");
        sb.append(" left join hm_pub_group_staff gp_staff on gp.groupid = gp_staff.groupid  ");
        sb.append(" where gp.groupid = ? and gp.grouptype = 'dscj') ");
        //  sb.append(" and info.sourceid in ("+sourceIds+")");
        //  sb.append(" and info.typeid in ("+typeIds+")");
        sb.append(" AND src.SRCNAME IS NOT NULL");
        sb.append(" and info.companyid=?");
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
        sb.append(" and info.createtime between  ? and  ?");
        sb.append(" GROUP BY info.SOURCEID");

        List<Map<String, Object>> dstgGoldDataReports = jdbcTemplate.queryForList(sb.toString(),
                new Object[]{groupId,companyId,start,end});
        List<DstgChannelReportsOrderBySrcVO> dstgChannelReportsOrderBySrcBak=new ArrayList<>();
        for (Map<String, Object> dstgGoldDataReport : dstgGoldDataReports) {
            DstgChannelReportsOrderBySrcVO dstgChannelReportsOrderBySrcVO = new DstgChannelReportsOrderBySrcVO();
            dstgChannelReportsOrderBySrcVO.setSrcId(Integer.parseInt(String.valueOf(dstgGoldDataReport.get("srcId"))));
            dstgChannelReportsOrderBySrcVO.setSrcName((String) dstgGoldDataReport.get("srcname"));
            dstgChannelReportsOrderBySrcVO.setInValidClientCount(Integer.parseInt(String.valueOf(dstgGoldDataReport.get("count"))));
            dstgChannelReportsOrderBySrcBak.add(dstgChannelReportsOrderBySrcVO);
        }

        for (DstgChannelReportsOrderBySrcVO dstgChannelReportsOrderBySrcVO : dstgChannelReportsOrderBySrcVOS) {
            for (DstgChannelReportsOrderBySrcVO dstgChannelReportsOrderBySrcVO1 : dstgChannelReportsOrderBySrcBak) {
                if (dstgChannelReportsOrderBySrcVO.getSrcId()==dstgChannelReportsOrderBySrcVO1.getSrcId()) {
                    dstgChannelReportsOrderBySrcVO.setInValidClientCount(dstgChannelReportsOrderBySrcVO1.getInValidClientCount());
                    break;
                }
            }
        }
    }




    /**
     * 入店量
     * */
    private void getComeShopClientCount(String groupId,Integer companyId,String start,String end,String sourceIds,String typeIds,List<DstgChannelReportsOrderBySrcVO> dstgChannelReportsOrderBySrcVOS){
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT info.SOURCEID srcId,");
        sb.append(" count(1) count,");
        sb.append(" src.srcname srcname");
        sb.append(" FROM hm_crm_client_info info");
        sb.append(" LEFT JOIN hm_crm_client_detail detail ON info.KZID = detail.KZID AND info.COMPANYID = detail.COMPANYID");
        sb.append(" LEFT JOIN hm_crm_source src ON src.id = info.SOURCEID");
        sb.append(" WHERE info.COLLECTORI in ( select distinct  gp_staff.staffid from hm_pub_group gp");
        sb.append(" left join hm_pub_group_staff gp_staff on gp.groupid = gp_staff.groupid  ");
        sb.append(" where gp.groupid = ? and gp.grouptype = 'dscj') ");
        //  sb.append(" and info.sourceid in ("+sourceIds+")");
        //  sb.append(" and info.typeid in ("+typeIds+")");
        sb.append(" AND src.SRCNAME IS NOT NULL");
        sb.append(" and info.companyid=?");
        sb.append(" and info.COMESHOPTIME between  ? and  ?");
        sb.append(" GROUP BY info.SOURCEID");

        List<Map<String, Object>> dstgGoldDataReports = jdbcTemplate.queryForList(sb.toString(),
                new Object[]{groupId,companyId,start,end});
        List<DstgChannelReportsOrderBySrcVO> dstgChannelReportsOrderBySrcBak=new ArrayList<>();
        for (Map<String, Object> dstgGoldDataReport : dstgGoldDataReports) {
            DstgChannelReportsOrderBySrcVO dstgChannelReportsOrderBySrcVO = new DstgChannelReportsOrderBySrcVO();
            dstgChannelReportsOrderBySrcVO.setSrcId(Integer.parseInt(String.valueOf(dstgGoldDataReport.get("srcId"))));
            dstgChannelReportsOrderBySrcVO.setSrcName((String) dstgGoldDataReport.get("srcname"));
            dstgChannelReportsOrderBySrcVO.setComeShopClientCount(Integer.parseInt(String.valueOf(dstgGoldDataReport.get("count"))));
            dstgChannelReportsOrderBySrcBak.add(dstgChannelReportsOrderBySrcVO);
        }

        for (DstgChannelReportsOrderBySrcVO dstgChannelReportsOrderBySrcVO : dstgChannelReportsOrderBySrcVOS) {
            for (DstgChannelReportsOrderBySrcVO dstgChannelReportsOrderBySrcVO1 : dstgChannelReportsOrderBySrcBak) {
                if (dstgChannelReportsOrderBySrcVO.getSrcId()==dstgChannelReportsOrderBySrcVO1.getSrcId()) {
                    dstgChannelReportsOrderBySrcVO.setComeShopClientCount(dstgChannelReportsOrderBySrcVO1.getComeShopClientCount());
                    break;
                }
            }
        }
    }


    /**
     * 成交量
     * */
    private void getSuccessClientCount(String groupId,Integer companyId,String start,String end,String sourceIds,String typeIds,List<DstgChannelReportsOrderBySrcVO> dstgChannelReportsOrderBySrcVOS){
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT info.SOURCEID srcId,");
        sb.append(" count(1) count,");
        sb.append(" src.srcname srcname");
        sb.append(" FROM hm_crm_client_info info");
        sb.append(" LEFT JOIN hm_crm_client_detail detail ON info.KZID = detail.KZID AND info.COMPANYID = detail.COMPANYID");
        sb.append(" LEFT JOIN hm_crm_source src ON src.id = info.SOURCEID");
        sb.append(" WHERE info.COLLECTORI in ( select distinct  gp_staff.staffid from hm_pub_group gp");
        sb.append(" left join hm_pub_group_staff gp_staff on gp.groupid = gp_staff.groupid  ");
        sb.append(" where gp.groupid = ? and gp.grouptype = 'dscj') ");
        //  sb.append(" and info.sourceid in ("+sourceIds+")");
        //  sb.append(" and info.typeid in ("+typeIds+")");
        sb.append(" AND src.SRCNAME IS NOT NULL");
        sb.append(" and info.companyid=?");
        sb.append(" and info.SUCCESSTIME between  ? and  ?");
        sb.append(" GROUP BY info.SOURCEID");

        List<Map<String, Object>> dstgGoldDataReports = jdbcTemplate.queryForList(sb.toString(),
                new Object[]{groupId,companyId,start,end});
        List<DstgChannelReportsOrderBySrcVO> dstgChannelReportsOrderBySrcBak=new ArrayList<>();
        for (Map<String, Object> dstgGoldDataReport : dstgGoldDataReports) {
            DstgChannelReportsOrderBySrcVO dstgChannelReportsOrderBySrcVO = new DstgChannelReportsOrderBySrcVO();
            dstgChannelReportsOrderBySrcVO.setSrcId(Integer.parseInt(String.valueOf(dstgGoldDataReport.get("srcId"))));
            dstgChannelReportsOrderBySrcVO.setSrcName((String) dstgGoldDataReport.get("srcname"));
            dstgChannelReportsOrderBySrcVO.setSuccessClientCount(Integer.parseInt(String.valueOf(dstgGoldDataReport.get("count"))));
            dstgChannelReportsOrderBySrcBak.add(dstgChannelReportsOrderBySrcVO);
        }

        for (DstgChannelReportsOrderBySrcVO dstgChannelReportsOrderBySrcVO : dstgChannelReportsOrderBySrcVOS) {
            for (DstgChannelReportsOrderBySrcVO dstgChannelReportsOrderBySrcVO1 : dstgChannelReportsOrderBySrcBak) {
                if (dstgChannelReportsOrderBySrcVO.getSrcId()==dstgChannelReportsOrderBySrcVO1.getSrcId()) {
                    dstgChannelReportsOrderBySrcVO.setSuccessClientCount(dstgChannelReportsOrderBySrcVO1.getSuccessClientCount());
                    break;
                }
            }
        }
    }


    /**
     * 成交均价
     * */
    private void getAvgAmount(String groupId,Integer companyId,String start,String end,String sourceIds,String typeIds,List<DstgChannelReportsOrderBySrcVO> dstgChannelReportsOrderBySrcVOS){
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT info.SOURCEID srcId,");
        sb.append(" AVG( detail.AMOUNT) count,");
        sb.append(" src.srcname srcname");
        sb.append(" FROM hm_crm_client_info info");
        sb.append(" LEFT JOIN hm_crm_client_detail detail ON info.KZID = detail.KZID AND info.COMPANYID = detail.COMPANYID");
        sb.append(" LEFT JOIN hm_crm_source src ON src.id = info.SOURCEID");
        sb.append(" WHERE info.COLLECTORI in ( select distinct  gp_staff.staffid from hm_pub_group gp");
        sb.append(" left join hm_pub_group_staff gp_staff on gp.groupid = gp_staff.groupid  ");
        sb.append(" where gp.groupid = ? and gp.grouptype = 'dscj') ");
        //  sb.append(" and info.sourceid in ("+sourceIds+")");
        //  sb.append(" and info.typeid in ("+typeIds+")");
        sb.append(" AND src.SRCNAME IS NOT NULL");
        sb.append(" and info.companyid=?");
        sb.append(" and info.createtime between  ? and  ?");
        sb.append(" GROUP BY info.SOURCEID");

        List<Map<String, Object>> dstgGoldDataReports = jdbcTemplate.queryForList(sb.toString(),
                new Object[]{groupId,companyId,start,end});
        List<DstgChannelReportsOrderBySrcVO> dstgChannelReportsOrderBySrcBak=new ArrayList<>();
        for (Map<String, Object> dstgGoldDataReport : dstgGoldDataReports) {
            DstgChannelReportsOrderBySrcVO dstgChannelReportsOrderBySrcVO = new DstgChannelReportsOrderBySrcVO();
            dstgChannelReportsOrderBySrcVO.setSrcId(Integer.parseInt(String.valueOf(dstgGoldDataReport.get("srcId"))));
            dstgChannelReportsOrderBySrcVO.setSrcName((String) dstgGoldDataReport.get("srcname"));
            dstgChannelReportsOrderBySrcVO.setAvgAmount(Integer.parseInt(String.valueOf(dstgGoldDataReport.get("count"))));
            dstgChannelReportsOrderBySrcBak.add(dstgChannelReportsOrderBySrcVO);
        }

        for (DstgChannelReportsOrderBySrcVO dstgChannelReportsOrderBySrcVO : dstgChannelReportsOrderBySrcVOS) {
            for (DstgChannelReportsOrderBySrcVO dstgChannelReportsOrderBySrcVO1 : dstgChannelReportsOrderBySrcBak) {
                if (dstgChannelReportsOrderBySrcVO.getSrcId()==dstgChannelReportsOrderBySrcVO1.getSrcId()) {
                    dstgChannelReportsOrderBySrcVO.setAvgAmount(dstgChannelReportsOrderBySrcVO1.getAvgAmount());
                    break;
                }
            }
        }
    }

    /**
     * 成交总价
     * */
    private void getAmount(String groupId,Integer companyId,String start,String end,String sourceIds,String typeIds,List<DstgChannelReportsOrderBySrcVO> dstgChannelReportsOrderBySrcVOS){
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT info.SOURCEID srcId,");
        sb.append(" SUM( detail.AMOUNT) count,");
        sb.append(" src.srcname srcname");
        sb.append(" FROM hm_crm_client_info info");
        sb.append(" LEFT JOIN hm_crm_client_detail detail ON info.KZID = detail.KZID AND info.COMPANYID = detail.COMPANYID");
        sb.append(" LEFT JOIN hm_crm_source src ON src.id = info.SOURCEID");
        sb.append(" WHERE info.COLLECTORI in ( select distinct  gp_staff.staffid from hm_pub_group gp");
        sb.append(" left join hm_pub_group_staff gp_staff on gp.groupid = gp_staff.groupid  ");
        sb.append(" where gp.groupid = ? and gp.grouptype = 'dscj') ");
        //  sb.append(" and info.sourceid in ("+sourceIds+")");
        //  sb.append(" and info.typeid in ("+typeIds+")");
        sb.append(" AND src.SRCNAME IS NOT NULL");
        sb.append(" and info.companyid=?");
        sb.append(" and info.createtime between  ? and  ?");
        sb.append(" GROUP BY info.SOURCEID");

        List<Map<String, Object>> dstgGoldDataReports = jdbcTemplate.queryForList(sb.toString(),
                new Object[]{groupId,companyId,start,end});
        List<DstgChannelReportsOrderBySrcVO> dstgChannelReportsOrderBySrcBak=new ArrayList<>();
        for (Map<String, Object> dstgGoldDataReport : dstgGoldDataReports) {
            DstgChannelReportsOrderBySrcVO dstgChannelReportsOrderBySrcVO = new DstgChannelReportsOrderBySrcVO();
            dstgChannelReportsOrderBySrcVO.setSrcId(Integer.parseInt(String.valueOf(dstgGoldDataReport.get("srcId"))));
            dstgChannelReportsOrderBySrcVO.setSrcName((String) dstgGoldDataReport.get("srcname"));
            dstgChannelReportsOrderBySrcVO.setAmount(Integer.parseInt(String.valueOf(dstgGoldDataReport.get("count")) ));
            dstgChannelReportsOrderBySrcBak.add(dstgChannelReportsOrderBySrcVO);
        }

        for (DstgChannelReportsOrderBySrcVO dstgChannelReportsOrderBySrcVO : dstgChannelReportsOrderBySrcVOS) {
            for (DstgChannelReportsOrderBySrcVO dstgChannelReportsOrderBySrcVO1 : dstgChannelReportsOrderBySrcBak) {
                if (dstgChannelReportsOrderBySrcVO.getSrcId()==dstgChannelReportsOrderBySrcVO1.getSrcId()) {
                    dstgChannelReportsOrderBySrcVO.setAmount(dstgChannelReportsOrderBySrcVO1.getAmount());
                    break;
                }
            }
        }
    }

    /**
     *  计算Rate
     */
    private void computerRate(List<DstgChannelReportsOrderBySrcVO> dstgChannelReportsOrderBySrcVOS,DsInvalidVO invalidConfig){
        for (DstgChannelReportsOrderBySrcVO dstgGoldDataReportsVO:dstgChannelReportsOrderBySrcVOS){
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
    private void computerTotal(List<DstgChannelReportsOrderBySrcVO> dstgChannelReportsOrderBySrcVOS){
        DstgChannelReportsOrderBySrcVO dstgReportsTotal = new DstgChannelReportsOrderBySrcVO();
        dstgReportsTotal.setSrcName("合计");
        for (DstgChannelReportsOrderBySrcVO dstgReportsVO : dstgChannelReportsOrderBySrcVOS) {
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

        dstgChannelReportsOrderBySrcVOS.add(0,dstgReportsTotal);
    }

    /**
     * 只保留2位小数
     * */
    public double parseDouble(double result){
        return Double.parseDouble(String.format("%.2f",result));
    }
}
