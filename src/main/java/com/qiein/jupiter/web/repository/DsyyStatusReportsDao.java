package com.qiein.jupiter.web.repository;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.qiein.jupiter.util.CollectionUtils;
import com.qiein.jupiter.util.DBSplitUtil;
import com.qiein.jupiter.util.NumUtil;
import com.qiein.jupiter.util.StringUtil;
import com.qiein.jupiter.web.entity.po.GroupReportsVO;
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
public class DsyyStatusReportsDao {
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

        return dsyyStatusReportsHeaderVO;
    }


    /**
     * 添加条件
     * */
    private void addConditionByTypeAndGroupId(ReportsParamVO reportsParamVO,StringBuilder sb){
        if(StringUtil.isNotEmpty(reportsParamVO.getGroupId())){
            sb.append(" and info.groupid = '"+reportsParamVO.getGroupId()+"' ");
        }
        if(NumUtil.isValid(reportsParamVO.getType())){
            sb.append(" and info.typeid =" +reportsParamVO.getType());
        }
    }

    /**
     * 获取小组下客服列表
     * */
    private void getGroupList(ReportsParamVO reportsParamVO,List<DsyyStatusReportsVO> dsyyStatusReportsVOS) {
        StringBuilder sb = new StringBuilder();
        sb.append("select groupid,GROUPNAME,GROUPTYPE from hm_pub_group where companyid = ? and GROUPTYPE = 'dsyy' and PARENTID != '0' ");
        if(StringUtil.isNotEmpty(reportsParamVO.getGroupId())){
            sb.append(" and groupid = '"+reportsParamVO.getGroupId()+"'");
        }
        sb.append(" order by GROUPNAME ");

        List<DsyyStatusReportsVO> dsyyStatusReports = jdbcTemplate.query(sb.toString(), new Object[]{reportsParamVO.getCompanyId()},
                new RowMapper<DsyyStatusReportsVO>() {
                    @Override
                    public DsyyStatusReportsVO mapRow(ResultSet rs, int i) throws SQLException {
                        DsyyStatusReportsVO dsyyStatusReportsVO = new DsyyStatusReportsVO();
                        dsyyStatusReportsVO.setGroupId(rs.getString("groupid"));
                        dsyyStatusReportsVO.setGrouoName(rs.getString("GROUPNAME"));
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
        sb.append(" select STATUSID,groupid,count(id) client_count from "+ infoTabName +" info  where info.companyid = ? and info.isdel = 0 and info.groupid is not null ");
        addConditionByTypeAndGroupId(reportsParamVO,sb);
        sb.append(" and info.CREATETIME BETWEEN ? AND ?");
        sb.append(" group by info.STATUSID,info.groupid ");
        jdbcTemplate.query(sb.toString(), new Object[]{reportsParamVO.getCompanyId(),reportsParamVO.getStart(),reportsParamVO.getEnd()}, new RowMapper<DsyyStatusReportsVO>() {
            @Override
            public DsyyStatusReportsVO mapRow(ResultSet rs, int i) throws SQLException {
                for (DsyyStatusReportsVO dsyyStatusReports:dsyyStatusReportsVOS){
                    //遍历组
                    if(StringUtil.isNotEmpty(rs.getString("groupid")) && dsyyStatusReports.getGroupId().equalsIgnoreCase(rs.getString("groupid"))){
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

}
