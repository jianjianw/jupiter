package com.qiein.jupiter.web.repository;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.qiein.jupiter.constant.CommonConstant;
import com.qiein.jupiter.util.*;
import com.qiein.jupiter.web.entity.po.GroupReportsVO;
import com.qiein.jupiter.util.StringUtil;
import com.qiein.jupiter.web.entity.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * @author: yyx
 * @Date: 2018-8-11
 */
@Repository
public class DsyyStatusStaffReportsDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 获取电商邀约状态报表
     */
    public  DsyyStatusReportsHeaderVO  getDsyyStatusReports(ReportsParamVO reportsParamVO, DsInvalidVO invalidConfig) {
        DsyyStatusReportsHeaderVO dsyyStatusReportsHeaderVO = new DsyyStatusReportsHeaderVO();
        List<DsyyStatusReportsVO> dsyyStatusReportsVOS = new ArrayList<>();

        //获取小组列表
        getGroupList(reportsParamVO,dsyyStatusReportsVOS);
        //获取状态列表
        getStatusList(dsyyStatusReportsHeaderVO,reportsParamVO,dsyyStatusReportsVOS);
        //获取客资数量
        getStatusClientCount(reportsParamVO,dsyyStatusReportsVOS);
        dsyyStatusReportsHeaderVO.setDsyyStatusReportsHeaderVOS(dsyyStatusReportsVOS);
        dataHandle(dsyyStatusReportsHeaderVO);
        computerSumCount(dsyyStatusReportsHeaderVO);
        return dsyyStatusReportsHeaderVO;
    }

    /**
     * 添加条件
     * */
    private void addConditionByTypeAndGroupId(ReportsParamVO reportsParamVO,StringBuilder sb){
        if(StringUtil.isNotEmpty(reportsParamVO.getGroupId())){
            String[] groupIds = reportsParamVO.getGroupId().split(CommonConstant.STR_SEPARATOR);
            StringBuilder groupIdsSb = new StringBuilder();
            for (String id:groupIds){
                groupIdsSb.append("'").append(id).append("'").append(",");
            }
            String groupId = groupIdsSb.substring(0, groupIdsSb.toString().length() - 1);
            System.out.println(groupId);
            sb.append(" and info.groupid in ("+ groupId +")");
        }
        if(StringUtil.isNotEmpty(reportsParamVO.getType())){
            sb.append(" and info.typeid in (" +reportsParamVO.getType()+ ")");
        }
    }

    /**
     * 获取小组下客服列表
     * */
    private void getGroupList(ReportsParamVO reportsParamVO,List<DsyyStatusReportsVO> dsyyStatusReportsVOS) {
        StringBuilder sb = new StringBuilder();
        String infoTabName = DBSplitUtil.getInfoTabName(reportsParamVO.getCompanyId());
        sb.append(" select staff.nickname,info.APPOINTORID from "+infoTabName+" info,hm_pub_staff staff");
        sb.append("  where");
        sb.append(" info.APPOINTORID = staff.id  ");
        sb.append(" and info.isdel = 0  ");
        sb.append(" and info.srctype in (1,2) ");
        sb.append(" and info.companyid = ?");
        sb.append(" group by info.APPOINTORID");

        List<DsyyStatusReportsVO> dsyyStatusReports = jdbcTemplate.query(sb.toString(), new Object[]{reportsParamVO.getCompanyId()},
                new RowMapper<DsyyStatusReportsVO>() {
                    @Override
                    public DsyyStatusReportsVO mapRow(ResultSet rs, int i) throws SQLException {
                        DsyyStatusReportsVO dsyyStatusReportsVO = new DsyyStatusReportsVO();
                        dsyyStatusReportsVO.setGroupId(rs.getString("APPOINTORID"));
                        dsyyStatusReportsVO.setGrouoName(rs.getString("nickname"));
                        return dsyyStatusReportsVO;
                    }
                });
        dsyyStatusReportsVOS.addAll(dsyyStatusReports);
    }

    /**
     * 获取状态列表
     * */
    public void getStatusList(DsyyStatusReportsHeaderVO dsyyStatusReportsHeaderVO,ReportsParamVO reportsParamVO,List<DsyyStatusReportsVO> dsyyStatusReportsVOS) {
        StringBuilder sb = new StringBuilder();
        sb.append(" select distinct client_status.STATUSID,client_status.STATUSNAME from hm_crm_client_status client_status where companyid = ? ");

        List<ClientStatusReportsVO> clientStatusReportsVOS = jdbcTemplate.query(sb.toString(), new Object[]{reportsParamVO.getCompanyId()},
                new RowMapper<ClientStatusReportsVO>() {
                    @Override
                    public ClientStatusReportsVO mapRow(ResultSet rs, int i) throws SQLException {
                        ClientStatusReportsVO clientStatusReportsVO = new ClientStatusReportsVO();
                        clientStatusReportsVO.setStatusId(rs.getInt("STATUSID"));
                        clientStatusReportsVO.setStatusName(rs.getString("STATUSNAME"));
                        return clientStatusReportsVO;
                    }
                });
        //设置表头
        dsyyStatusReportsHeaderVO.setClientStatusReportsVOList(clientStatusReportsVOS);
        for (DsyyStatusReportsVO dsyyStatusReportsVO :dsyyStatusReportsVOS){
            // TODO list深拷贝问题，需要查看源码
            String jsonString = JSONObject.toJSONString(clientStatusReportsVOS);
            List<ClientStatusReportsVO> clientStatusReports = JSONObject.parseArray(jsonString, ClientStatusReportsVO.class);
            dsyyStatusReportsVO.setClientStatusReportsVOS(clientStatusReports);
        }
    }

    /**
     * 获取客资数量
     * */
    public void getStatusClientCount(ReportsParamVO reportsParamVO, final List<DsyyStatusReportsVO> dsyyStatusReportsVOS){
        StringBuilder sb = new StringBuilder();
        String infoTabName = DBSplitUtil.getInfoTabName(reportsParamVO.getCompanyId());
        String detailTabName = DBSplitUtil.getDetailTabName(reportsParamVO.getCompanyId());
        sb.append(" select STATUSID,APPOINTORID,count(id) client_count from "+ infoTabName +" info  where info.companyid = ? and info.isdel = 0 and info.groupid is not null  ");
        sb.append(" and info.srctype in (1,2) ");
        addConditionByTypeAndGroupId(reportsParamVO,sb);
        sb.append(" and info.CREATETIME BETWEEN ? AND ?");
        //TODO 此处需要添加条件
        sb.append(" group by info.STATUSID,info.APPOINTORID");
        jdbcTemplate.query(sb.toString(), new Object[]{reportsParamVO.getCompanyId(),reportsParamVO.getStart(),reportsParamVO.getEnd()}, new RowMapper<DsyyStatusReportsVO>() {
            @Override
            public DsyyStatusReportsVO mapRow(ResultSet rs, int i) throws SQLException {
                for (DsyyStatusReportsVO dsyyStatusReports:dsyyStatusReportsVOS){
                    //遍历组
                    if(StringUtil.isNotEmpty(rs.getString("APPOINTORID")) && dsyyStatusReports.getGroupId().equalsIgnoreCase(rs.getString("APPOINTORID"))){
                        for (ClientStatusReportsVO clientStatusReportsVO:dsyyStatusReports.getClientStatusReportsVOS()){
                            if(clientStatusReportsVO.getStatusId().equals(rs.getInt("STATUSID"))){
                                clientStatusReportsVO.setKzNum(rs.getInt("client_count"));
                                break;
                            }
                        }
                    }
                }
                return null;
            }
        });
    }

    public void dataHandle(DsyyStatusReportsHeaderVO dsyyStatusReportsHeaderVO){
        for (DsyyStatusReportsVO dsyyStatusReportsVO :dsyyStatusReportsHeaderVO.getDsyyStatusReportsHeaderVOS()){
            Map kzNumMap = new HashMap();
            for (ClientStatusReportsVO clientStatusReportsVO:dsyyStatusReportsVO.getClientStatusReportsVOS()){
                kzNumMap.put(String.valueOf(clientStatusReportsVO.getStatusId()),clientStatusReportsVO.getKzNum());
            }
            dsyyStatusReportsVO.setMapList(kzNumMap);
            dsyyStatusReportsVO.setClientStatusReportsVOS(null);
        }
    }

    public void computerSumCount(DsyyStatusReportsHeaderVO dsyyStatusReportsHeaderVO) {
        Map kzNumMap = new HashMap();
        DsyyStatusReportsVO dsyyStatusReportsVO = new DsyyStatusReportsVO();
        dsyyStatusReportsVO.setGrouoName("合计");
        dsyyStatusReportsVO.setMapList(kzNumMap);
        for(DsyyStatusReportsVO dsyyStatusReports:dsyyStatusReportsHeaderVO.getDsyyStatusReportsHeaderVOS()){
            for (ClientStatusReportsVO clientStatusReportsVO : dsyyStatusReports.getClientStatusReportsVOS()) {
                Integer kzNum = dsyyStatusReportsVO.getMapList().get(String.valueOf(clientStatusReportsVO.getStatusId()));
                if(kzNum == null){
                    kzNum = 0;
                }
                kzNumMap.put(String.valueOf(clientStatusReportsVO.getStatusId()), clientStatusReportsVO.getKzNum() + kzNum);
            }
            dsyyStatusReports.setClientStatusReportsVOS(null);
        }
        dsyyStatusReportsHeaderVO.getDsyyStatusReportsHeaderVOS().add(0,dsyyStatusReportsVO);
    }

}
