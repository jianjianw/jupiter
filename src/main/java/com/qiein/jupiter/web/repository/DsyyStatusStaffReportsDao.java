//package com.qiein.jupiter.web.repository;
//
//import com.alibaba.fastjson.JSONObject;
//import com.qiein.jupiter.util.DBSplitUtil;
//import com.qiein.jupiter.util.StringUtil;
//import com.qiein.jupiter.web.entity.vo.ClientStatusReportsVO;
//import com.qiein.jupiter.web.entity.vo.DsInvalidVO;
//import com.qiein.jupiter.web.entity.vo.DsyyStatusReportsVO;
//import com.qiein.jupiter.web.entity.vo.ReportsParamVO;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.jdbc.core.RowMapper;
//import org.springframework.stereotype.Repository;
//
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * @author: yyx
// * @Date: 2018-8-15
// */
//@Repository
//public class DsyyStatusStaffReportsDao {
//    @Autowired
//    private JdbcTemplate jdbcTemplate;
//
//
//    /**
//     * 获取电商邀约状态报表
//     */
//    public List<DsyyStatusReportsVO> getDsyyStatusReports(ReportsParamVO reportsParamVO, DsInvalidVO invalidConfig) {
//        List<DsyyStatusReportsVO> dsyyStatusReportsVOS = new ArrayList<>();
//        //获取小组员工列表
//        getGroupList(reportsParamVO,dsyyStatusReportsVOS);
//        //获取状态列表
//        getStatusList(reportsParamVO,dsyyStatusReportsVOS);
//        //获取客资数量
//        getStatusClientCount(reportsParamVO,dsyyStatusReportsVOS);
//        return dsyyStatusReportsVOS;
//    }
//
//    /**
//     * 获取小组列表
//     * */
//    private void getGroupList(ReportsParamVO reportsParamVO,List<DsyyStatusReportsVO> dsyyStatusReportsVOS) {
//        StringBuilder sb = new StringBuilder();
//        sb.append("select groupid,GROUPNAME,GROUPTYPE from hm_pub_group where companyid = ? and GROUPTYPE = 'dsyy' and PARENTID != '0' ");
//
//        List<DsyyStatusReportsVO> dsyyStatusReports = jdbcTemplate.query(sb.toString(), new Object[]{reportsParamVO.getCompanyId()},
//                new RowMapper<DsyyStatusReportsVO>() {
//                    @Override
//                    public DsyyStatusReportsVO mapRow(ResultSet rs, int i) throws SQLException {
//                        DsyyStatusReportsVO dsyyStatusReportsVO = new DsyyStatusReportsVO();
//                        dsyyStatusReportsVO.setGroupId(rs.getString("groupid"));
//                        dsyyStatusReportsVO.setGrouoName(rs.getString("GROUPNAME"));
//                        return dsyyStatusReportsVO;
//                    }
//                });
//        dsyyStatusReportsVOS.addAll(dsyyStatusReports);
//    }
//
//
//    /**
//     * 获取状态列表
//     * */
//    public void getStatusList(ReportsParamVO reportsParamVO,List<DsyyStatusReportsVO> dsyyStatusReportsVOS) {
//        StringBuilder sb = new StringBuilder();
//        sb.append(" select distinct client_status.STATUSID,client_status.STATUSNAME from hm_crm_client_status client_status where companyid = ? order by statusid");
//
//        List<ClientStatusReportsVO> clientStatusReportsVOS = jdbcTemplate.query(sb.toString(), new Object[]{reportsParamVO.getCompanyId()},
//                new RowMapper<ClientStatusReportsVO>() {
//                    @Override
//                    public ClientStatusReportsVO mapRow(ResultSet rs, int i) throws SQLException {
//                        ClientStatusReportsVO clientStatusReportsVO = new ClientStatusReportsVO();
//                        clientStatusReportsVO.setStatusId(rs.getInt("STATUSID"));
//                        clientStatusReportsVO.setStatusName(rs.getString("STATUSNAME"));
//                        return clientStatusReportsVO;
//                    }
//                });
//
//        for (DsyyStatusReportsVO dsyyStatusReportsVO :dsyyStatusReportsVOS){
//            // TODO list深拷贝问题，需要查看源码
//            String jsonString = JSONObject.toJSONString(clientStatusReportsVOS);
//            List<ClientStatusReportsVO> clientStatusReports = JSONObject.parseArray(jsonString, ClientStatusReportsVO.class);
//            dsyyStatusReportsVO.setClientStatusReportsVOS(clientStatusReports);
//        }
//    }
//
//    /**
//     * 获取客资数量
//     * */
//    public void getStatusClientCount(ReportsParamVO reportsParamVO, final List<DsyyStatusReportsVO> dsyyStatusReportsVOS){
//        StringBuilder sb = new StringBuilder();
//        String infoTabName = DBSplitUtil.getInfoTabName(reportsParamVO.getCompanyId());
//        String detailTabName = DBSplitUtil.getDetailTabName(reportsParamVO.getCompanyId());
//        sb.append(" select STATUSID,groupid,count(id) client_count from "+ infoTabName +" info  where info.companyid = ? and info.isdel = 0 and info.groupid is not null group by info.STATUSID,info.groupid ");
//        sb.append(" and info.SUCCESSTIME BETWEEN ? AND ?");
//        jdbcTemplate.query(sb.toString(), new Object[]{reportsParamVO.getCompanyId(),reportsParamVO.getStart(),reportsParamVO.getEnd()}, new RowMapper<DsyyStatusReportsVO>() {
//            @Override
//            public DsyyStatusReportsVO mapRow(ResultSet rs, int i) throws SQLException {
//                for (DsyyStatusReportsVO dsyyStatusReports:dsyyStatusReportsVOS){
//                    //遍历组
//                    if(StringUtil.isNotEmpty(rs.getString("groupid")) && dsyyStatusReports.getGroupId().equalsIgnoreCase(rs.getString("groupid"))){
//                        for (ClientStatusReportsVO clientStatusReportsVO:dsyyStatusReports.getClientStatusReportsVOS()){
//                            if(clientStatusReportsVO.getStatusId().equals(rs.getInt("STATUSID"))){
//                                clientStatusReportsVO.setKzNum(rs.getInt("client_count"));
//                                break;
//                            }
//                        }
//                    }
//                }
//                return null;
//            }
//        });
//    }
//
//
//}
