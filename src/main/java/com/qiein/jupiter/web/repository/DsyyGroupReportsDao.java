package com.qiein.jupiter.web.repository;

import com.alibaba.fastjson.JSONArray;
import com.qiein.jupiter.constant.ClientStatusConst;
import com.qiein.jupiter.constant.CommonConstant;
import com.qiein.jupiter.util.DBSplitUtil;
import com.qiein.jupiter.web.entity.vo.DsInvalidVO;
import com.qiein.jupiter.web.entity.vo.DsyyReportsVO;
import com.qiein.jupiter.web.entity.vo.ReportsParamVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 电商客服统计报表
 * author:yyx
 * cover:xiangliang
 */
@Repository
public class DsyyGroupReportsDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    public List<DsyyReportsVO> getDsyyGroupReports(ReportsParamVO reportsParamVO, DsInvalidVO dsInvalidVO) {
        List<DsyyReportsVO> dsyyReportsVOS = new ArrayList<>();
        getAllGroup(reportsParamVO,dsyyReportsVOS);
        //获取所有客资
        getAllClientCount(reportsParamVO, dsyyReportsVOS);
        //获取网销转介绍
        getNetTurnClientCount(reportsParamVO, dsyyReportsVOS);
        //获取待定客资
        getPendingClientCount(reportsParamVO, dsyyReportsVOS,dsInvalidVO);
        //获取筛选待定客资
        getWaitClientCount(reportsParamVO, dsyyReportsVOS);
        //获取筛选中客资
        getInWaitClientCount(reportsParamVO, dsyyReportsVOS);
        //获取筛选无效客资
        getWaitInvalidClientCount(reportsParamVO, dsyyReportsVOS);
        //获取无效客资
        getInvalidClientCount(reportsParamVO, dsyyReportsVOS,dsInvalidVO);
        //获取入店量
        getComeShopClientCount(reportsParamVO, dsyyReportsVOS);
        //获取成交量
        getSuccessClientCount(reportsParamVO, dsyyReportsVOS);
        //获取入店成交量
        getComeShopSuccessClientCount(reportsParamVO, dsyyReportsVOS);
        //获取在线成交量
        getOnLineSuccessClientCount(reportsParamVO, dsyyReportsVOS);
        //获取花费
        getCost(reportsParamVO, dsyyReportsVOS);
        //获取总收入
        getSumAmount(reportsParamVO, dsyyReportsVOS);
        //获取已收金额
        getStayAmount(reportsParamVO,dsyyReportsVOS);
        //获取平均收入
        getAvgAmount(reportsParamVO, dsyyReportsVOS);
        //获取实收金额
        getRealAmount(reportsParamVO,dsyyReportsVOS);
        //补款
        getBkAmount(reportsParamVO,dsyyReportsVOS);
        //获取周末入店量
        getWeekComeShopClientCount(reportsParamVO, dsyyReportsVOS);
        //获取成功微信数
        getSuccessWechat(reportsParamVO,dsyyReportsVOS);
        //计算总计
        computerTotal(dsyyReportsVOS);
        //计算
        computerRate(dsyyReportsVOS,dsInvalidVO);
        //获取各类客资量进行逻辑操作
        //SQL见Navicat脚本文件
        return dsyyReportsVOS;
    }

    /**
     * 获取所有分组
     * */
    public Integer getAllGroup(ReportsParamVO reportsParamVO, List<DsyyReportsVO> dsyyReportsVOS){
        StringBuilder sb = new StringBuilder();
        sb.append("select gp.groupid groupId,gp.GROUPNAME groupName from hm_pub_group gp ");
        sb.append(" left join ");
        sb.append(DBSplitUtil.getInfoTabName(reportsParamVO.getCompanyId()) + " info on info.GroupId = gp.GROUPID ");
        sb.append(" where gp.GROUPTYPE  = 'dsyy'   and gp.COMPANYID = ?  and gp.parentId != '0'");
        sb.append(" and (info.srctype = 1 or info.srctype = 2)");
        if(StringUtils.isNotEmpty(reportsParamVO.getGroupId())){
            StringBuilder paramsSb = new StringBuilder();
            String[] params = reportsParamVO.getGroupId().split(CommonConstant.STR_SEPARATOR);
            for (String str:params){
                paramsSb.append("'").append(str).append("'").append(",");
            }
            sb.append(" and gp.GROUPID in( "+paramsSb.substring(0, paramsSb.toString().length() - 1)+" )");
        }
        sb.append(" group by gp.groupid");
        sb.append(" order by gp.groupName");
        addCondition(reportsParamVO,sb);
        List<Map<String, Object>> dsyyGroupReports = jdbcTemplate.queryForList(sb.toString(),
                new Object[]{reportsParamVO.getCompanyId()});

        for(Map<String,Object> map:dsyyGroupReports){
            DsyyReportsVO dsyyReportsVO = new DsyyReportsVO();
            dsyyReportsVO.setGroupId(String.valueOf(map.get("groupId")));
            dsyyReportsVO.setGroupName(String.valueOf(map.get("groupName")));
            dsyyReportsVOS.add(dsyyReportsVO);
        }
        return null;
    }


    /**
     * 获取总客资
     */
    public Integer getAllClientCount(ReportsParamVO reportsParamVO, List<DsyyReportsVO> dsyyReportsVOS)  {
        //TODO 获取总客资
        StringBuilder sb = new StringBuilder();
        sb.append("select gp.groupid groupId,gp.GROUPNAME groupName,count(info.GROUPID)  as count from hm_pub_group gp ");
        sb.append(" left join ");
        sb.append(DBSplitUtil.getInfoTabName(reportsParamVO.getCompanyId()) + " info on info.GroupId = gp.GROUPID    ");
        sb.append(" where gp.GROUPTYPE  = 'dsyy' and info.ISDEL = 0 and info.CREATETIME BETWEEN ? AND ? and gp.COMPANYID = ?  and gp.parentId != '0'");
        sb.append(" and (info.srctype = 1 or info.srctype = 2)");
        //添加条件筛选
        addCondition(reportsParamVO, sb);
        sb.append(" group by gp.groupid");
        sb.append(" order by gp.groupName");

        List<Map<String, Object>> dsyyGroupReports = jdbcTemplate.queryForList(sb.toString(),
                new Object[]{reportsParamVO.getStart(),reportsParamVO.getEnd(),reportsParamVO.getCompanyId()});
        List<DsyyReportsVO> dsyyReportsVOBak=new ArrayList<>();
        for(Map<String,Object> map:dsyyGroupReports){
            DsyyReportsVO dsyyReportsVO = new DsyyReportsVO();
            dsyyReportsVO.setGroupId(String.valueOf(map.get("groupId").toString()));
            dsyyReportsVO.setGroupName(String.valueOf(map.get("groupName").toString()));
            dsyyReportsVO.setAllClientCount(Integer.parseInt(String.valueOf(map.get("count").toString())));
            dsyyReportsVOBak.add(dsyyReportsVO);
        }
        for(DsyyReportsVO dsyyReportsVO:dsyyReportsVOS){
            for(DsyyReportsVO dsyyReportsVO1:dsyyReportsVOBak){
                if(dsyyReportsVO.getGroupId().equals(dsyyReportsVO1.getGroupId())){
                    dsyyReportsVO.setAllClientCount(dsyyReportsVO1.getAllClientCount());
                }
            }
        }

        return null;
    }

    /**
     * 获取网销转介绍客资
     * */
    public Integer getNetTurnClientCount(ReportsParamVO reportsParamVO, List<DsyyReportsVO> dsyyReportsVOS)  {
        //TODO 获取总客资
        StringBuilder sb = new StringBuilder();
        sb.append("select gp.groupid groupId,gp.GROUPNAME groupName,count(info.GROUPID) as count from hm_pub_group gp ");
        sb.append(" left join ");
        sb.append(DBSplitUtil.getInfoTabName(reportsParamVO.getCompanyId()) + " info on info.GroupId = gp.GROUPID   ");
        sb.append(" where gp.GROUPTYPE  = 'dsyy' and info.ISDEL = 0 and info.CREATETIME BETWEEN ? AND ? and info.CHANNELID in (select ID from hm_crm_channel channel where channel.TYPEID = 2 and channel.companyid= ?)  and gp.COMPANYID = ?  and gp.parentId != '0'");
        sb.append("  and (info.srctype = 1 or info.srctype = 2)");
        //添加条件筛选
        addCondition(reportsParamVO, sb);
        sb.append(" group by gp.groupid");
        sb.append(" order by gp.groupName");

        List<Map<String, Object>> dsyyGroupReports = jdbcTemplate.queryForList(sb.toString(),
                new Object[]{reportsParamVO.getStart(),reportsParamVO.getEnd(),reportsParamVO.getCompanyId(),reportsParamVO.getCompanyId()});
        List<DsyyReportsVO> dsyyReportsVOBak=new ArrayList<>();
        for(Map<String,Object> map:dsyyGroupReports){
            DsyyReportsVO dsyyReportsVO = new DsyyReportsVO();
            dsyyReportsVO.setGroupId(String.valueOf(map.get("groupId").toString()));
            dsyyReportsVO.setGroupName(String.valueOf(map.get("groupName").toString()));
            dsyyReportsVO.setNetTurnClientCount(Integer.parseInt(String.valueOf(map.get("count").toString())));
            dsyyReportsVOBak.add(dsyyReportsVO);
        }
        for(DsyyReportsVO dsyyReportsVO:dsyyReportsVOS){
            for(DsyyReportsVO dsyyReportsVO1:dsyyReportsVOBak){
                if(dsyyReportsVO.getGroupId().equals(dsyyReportsVO1.getGroupId())){
                    dsyyReportsVO.setNetTurnClientCount(dsyyReportsVO1.getNetTurnClientCount());
                }
            }
        }

        return null;
    }

    /**
     * 获取筛选待定客资
     */
    public Integer getWaitClientCount(ReportsParamVO reportsParamVO, List<DsyyReportsVO> dsyyReportsVOS){
        StringBuilder sb = new StringBuilder();
        sb.append("select gp.groupid groupId,gp.GROUPNAME groupName,IFNULL(count(info.GROUPID),0) as count from "+DBSplitUtil.getInfoTabName(reportsParamVO.getCompanyId())+" info ");
        sb.append(" left join");
        sb.append(" hm_pub_group gp on gp.GROUPID = info.GroupId ");
        sb.append(" left join hm_pub_company company on company.id = gp.companyId");
        sb.append(" where IF(company.DDISVALID != '0' , 1!=1 ");
        sb.append(" , gp.GROUPTYPE  = 'dsyy' and gp.COMPANYID = ? and info.ISDEL = 0");
        sb.append(" and info.CREATETIME BETWEEN ? AND ?");
        sb.append(" and info.CLASSID = 1 and info.STATUSID = 98 )");
        sb.append("  and (info.srctype = 1 or info.srctype = 2)");
        //添加条件筛选
        addCondition(reportsParamVO, sb);
        sb.append(" group by gp.groupid");

        List<Map<String, Object>> dsyyGroupReports = jdbcTemplate.queryForList(sb.toString(),
                new Object[]{reportsParamVO.getCompanyId(),reportsParamVO.getStart(),reportsParamVO.getEnd()});
        List<DsyyReportsVO> dsyyReportsVOBak=new ArrayList<>();
        for(Map<String,Object> map:dsyyGroupReports){
            DsyyReportsVO dsyyReportsVO = new DsyyReportsVO();
            dsyyReportsVO.setGroupId(String.valueOf(map.get("groupId").toString()));
            dsyyReportsVO.setGroupName(String.valueOf(map.get("groupName").toString()));
            dsyyReportsVO.setFilterWaitClientCount(Integer.parseInt(String.valueOf(map.get("count").toString())));
            dsyyReportsVOBak.add(dsyyReportsVO);
        }
        for(DsyyReportsVO dsyyReportsVO:dsyyReportsVOS){
            for(DsyyReportsVO dsyyReportsVO1:dsyyReportsVOBak){
                if(dsyyReportsVO.getGroupId().equals(dsyyReportsVO1.getGroupId())){
                    dsyyReportsVO.setFilterWaitClientCount(dsyyReportsVO1.getFilterWaitClientCount());
                }
            }
        }

        return null;
    }

    /**
     * 获取筛选无效客资
     */
    public Integer getWaitInvalidClientCount(ReportsParamVO reportsParamVO, List<DsyyReportsVO> dsyyReportsVOS)  {
        StringBuilder sb = new StringBuilder();
        sb.append("select gp.groupid groupId,gp.GROUPNAME groupName,IFNULL(count(info.GROUPID),0) as count from "+DBSplitUtil.getInfoTabName(reportsParamVO.getCompanyId())+" info ");
        sb.append(" left join");
        sb.append(" hm_pub_group gp on gp.GROUPID = info.GroupId ");
        sb.append(" where gp.GROUPTYPE  = 'dsyy' and gp.COMPANYID = ? and info.ISDEL = 0");
        sb.append(" and info.CREATETIME BETWEEN ? AND ?");
        sb.append(" and info.CLASSID = 6 and info.STATUSID = 99");
        sb.append("  and (info.srctype = 1 or info.srctype = 2)");
        //添加条件筛选
        addCondition(reportsParamVO, sb);
        sb.append(" group by gp.groupid");

        List<Map<String, Object>> dsyyGroupReports = jdbcTemplate.queryForList(sb.toString(),
                new Object[]{reportsParamVO.getCompanyId(),reportsParamVO.getStart(),reportsParamVO.getEnd()});
        List<DsyyReportsVO> dsyyReportsVOBak=new ArrayList<>();
        for(Map<String,Object> map:dsyyGroupReports){
            DsyyReportsVO dsyyReportsVO = new DsyyReportsVO();
            dsyyReportsVO.setGroupId(String.valueOf(map.get("groupId").toString()));
            dsyyReportsVO.setGroupName(String.valueOf(map.get("groupName").toString()));
            dsyyReportsVO.setInvalidFilterClientCount(Integer.parseInt(String.valueOf(map.get("count").toString())));
            dsyyReportsVOBak.add(dsyyReportsVO);
        }
        for(DsyyReportsVO dsyyReportsVO:dsyyReportsVOS){
            for(DsyyReportsVO dsyyReportsVO1:dsyyReportsVOBak){
                if(dsyyReportsVO.getGroupId().equals(dsyyReportsVO1.getGroupId())){
                    dsyyReportsVO.setInvalidFilterClientCount(dsyyReportsVO1.getInvalidFilterClientCount());
                }
            }
        }
        return null;
    }

    /**
     * 获取无效量
     */
    public Integer getInvalidClientCount(ReportsParamVO reportsParamVO, List<DsyyReportsVO> dsyyReportsVOS,DsInvalidVO dsInvalidVO ) {
        StringBuilder sb = new StringBuilder();
        sb.append("select gp.groupid groupId,gp.GROUPNAME groupName,IFNULL(count(info.GROUPID),0) as count from "+DBSplitUtil.getInfoTabName(reportsParamVO.getCompanyId())+" info ");
        sb.append(" left join");
        sb.append(" hm_pub_group gp on gp.GROUPID = info.GroupId ");
        sb.append(" left JOIN");
        sb.append(DBSplitUtil.getDetailTabName(reportsParamVO.getCompanyId()) + " dtl on info.KZID = dtl.kzID");
        sb.append(" where");
        sb.append("  gp.GROUPTYPE = 'dsyy' and gp.COMPANYID =  ? and info.isdel = 0 and info.CREATETIME BETWEEN ? AND ? ");

        if(StringUtils.isNotEmpty(dsInvalidVO.getDsInvalidStatus()) && StringUtils.isNotEmpty(dsInvalidVO.getDsInvalidLevel())){
            sb.append(" and (info.STATUSID in("+ dsInvalidVO.getDsInvalidStatus()+") or");
            sb.append("   dtl.YXLEVEL IN("+ dsInvalidVO.getDsInvalidLevel()  +") )");
        }
        if(StringUtils.isNotEmpty(dsInvalidVO.getDsInvalidStatus()) && StringUtils.isEmpty(dsInvalidVO.getDsInvalidLevel())){
            sb.append(" and info.STATUSID in ("+ dsInvalidVO.getDsInvalidStatus()+")");
        }
        if(StringUtils.isNotEmpty(dsInvalidVO.getDsInvalidLevel()) && StringUtils.isEmpty(dsInvalidVO.getDsInvalidStatus())){
            sb.append(" and dtl.YXLEVEL IN("+ dsInvalidVO.getDsInvalidLevel()  +") ");
        }
        sb.append("   and (info.srctype = 1 or info.srctype = 2)");
        //添加条件筛选
        addCondition(reportsParamVO, sb);
        sb.append(" GROUP BY gp.GROUPID");
        List<Map<String, Object>> dsyyGroupReports = jdbcTemplate.queryForList(sb.toString(),
                new Object[]{reportsParamVO.getCompanyId(),reportsParamVO.getStart(),reportsParamVO.getEnd()});
        List<DsyyReportsVO> dsyyReportsVOBak=new ArrayList<>();
        for(Map<String,Object> map:dsyyGroupReports){
            DsyyReportsVO dsyyReportsVO = new DsyyReportsVO();
            dsyyReportsVO.setGroupId(String.valueOf(map.get("groupId").toString()));
            dsyyReportsVO.setGroupName(String.valueOf(map.get("groupName").toString()));
            dsyyReportsVO.setInvalidClientCount(Integer.parseInt(String.valueOf(map.get("count").toString())));
            dsyyReportsVOBak.add(dsyyReportsVO);
        }
        for(DsyyReportsVO dsyyReportsVO:dsyyReportsVOS){
            for(DsyyReportsVO dsyyReportsVO1:dsyyReportsVOBak){
                if(dsyyReportsVO.getGroupId().equals(dsyyReportsVO1.getGroupId())){
                    dsyyReportsVO.setInvalidClientCount(dsyyReportsVO1.getInvalidClientCount());
                }
            }
        }
        return null;

    }

    /**
     * 获取筛选中客资
     */
    public Integer getInWaitClientCount(ReportsParamVO reportsParamVO, List<DsyyReportsVO> dsyyReportsVOS)  {
        StringBuilder sb = new StringBuilder();
        sb.append("select gp.groupid groupId,gp.GROUPNAME groupName,IFNULL(count(info.GROUPID),0) as count from "+DBSplitUtil.getInfoTabName(reportsParamVO.getCompanyId())+" info ");
        sb.append(" left join");
        sb.append(" hm_pub_group gp on gp.GROUPID = info.GroupId ");
        sb.append(" where gp.GROUPTYPE  = 'dsyy' and gp.COMPANYID = ? and info.ISDEL = 0");
        sb.append(" and info.CREATETIME BETWEEN ? AND ?");
        sb.append(" and info.CLASSID = 1 and info.STATUSID = 0");
        sb.append("  and (info.srctype = 1 or info.srctype = 2)");
        //添加条件筛选
        addCondition(reportsParamVO, sb);
        sb.append(" group by gp.groupid");


        List<Map<String, Object>> dsyyGroupReports = jdbcTemplate.queryForList(sb.toString(),
                new Object[]{reportsParamVO.getCompanyId(),reportsParamVO.getStart(),reportsParamVO.getEnd()});
        List<DsyyReportsVO> dsyyReportsVOBak=new ArrayList<>();
        for(Map<String,Object> map:dsyyGroupReports){
            DsyyReportsVO dsyyReportsVO = new DsyyReportsVO();
            dsyyReportsVO.setGroupId(String.valueOf(map.get("groupId").toString()));
            dsyyReportsVO.setGroupName(String.valueOf(map.get("groupName").toString()));
            dsyyReportsVO.setInFilterClientCount(Integer.parseInt(String.valueOf(map.get("count").toString())));
            dsyyReportsVOBak.add(dsyyReportsVO);
        }
        for(DsyyReportsVO dsyyReportsVO:dsyyReportsVOS){
            for(DsyyReportsVO dsyyReportsVO1:dsyyReportsVOBak){
                if(dsyyReportsVO.getGroupId().equals(dsyyReportsVO1.getGroupId())){
                    dsyyReportsVO.setInFilterClientCount(dsyyReportsVO1.getInvalidClientCount());
                }
            }
        }


        return null;
    }

    /**
     * 获取待定量
     */
    public Integer getPendingClientCount(ReportsParamVO reportsParamVO, List<DsyyReportsVO> dsyyReportsVOS,DsInvalidVO dsInvalidVO)  {
        StringBuilder sb = new StringBuilder();
        sb.append("select gp.groupid groupId,gp.GROUPNAME groupName,IFNULL(count(info.GROUPID),0) as count from "+DBSplitUtil.getInfoTabName(reportsParamVO.getCompanyId())+" info ");
        sb.append(" left join");
        sb.append(" hm_pub_group gp on gp.GROUPID = info.GroupId ");
        sb.append(" where gp.GROUPTYPE  = 'dsyy' and gp.COMPANYID = ? and info.ISDEL = 0");
        sb.append(" and info.CREATETIME BETWEEN ? AND ?");
        if(StringUtils.isNotEmpty(dsInvalidVO.getDsDdStatus())){
            sb.append(" and info.statusid in ("+dsInvalidVO.getDsDdStatus() +")");
        }
        sb.append("  and (info.srctype = 1 or info.srctype = 2)");
        //添加条件筛选
        addCondition(reportsParamVO, sb);
        sb.append(" group by gp.groupid");

        List<Map<String, Object>> dsyyGroupReports = jdbcTemplate.queryForList(sb.toString(),
                new Object[]{reportsParamVO.getCompanyId(),reportsParamVO.getStart(),reportsParamVO.getEnd()});
        List<DsyyReportsVO> dsyyReportsVOBak=new ArrayList<>();
        for(Map<String,Object> map:dsyyGroupReports){
            DsyyReportsVO dsyyReportsVO = new DsyyReportsVO();
            dsyyReportsVO.setGroupId(String.valueOf(map.get("groupId").toString()));
            dsyyReportsVO.setGroupName(String.valueOf(map.get("groupName").toString()));
            dsyyReportsVO.setWaitClientCount(Integer.parseInt(String.valueOf(map.get("count").toString())));
            dsyyReportsVOBak.add(dsyyReportsVO);
        }
        for(DsyyReportsVO dsyyReportsVO:dsyyReportsVOS){
            for(DsyyReportsVO dsyyReportsVO1:dsyyReportsVOBak){
                if(dsyyReportsVO.getGroupId().equals(dsyyReportsVO1.getGroupId())){
                    dsyyReportsVO.setWaitClientCount(dsyyReportsVO1.getWaitClientCount());
                }
            }
        }

        return null;
    }


    /**
     * 获取入店量
     */
    public Integer getComeShopClientCount(ReportsParamVO reportsParamVO, List<DsyyReportsVO> dsyyReportsVOS)  {
        StringBuilder sb = new StringBuilder();
        sb.append("select gp.groupid groupId,gp.GROUPNAME groupName,IFNULL(count(info.GROUPID),0) as count from "+DBSplitUtil.getInfoTabName(reportsParamVO.getCompanyId())+" info ");
        sb.append(" left join");
        sb.append(" hm_pub_group gp on gp.GROUPID = info.GroupId ");
        sb.append(" where gp.GROUPTYPE  = 'dsyy' and gp.COMPANYID = ? and info.ISDEL = 0");
        sb.append(" and info.COMESHOPTIME BETWEEN ? AND ?");
//        sb.append(" AND INSTR( ?, CONCAT(',',info.STATUSID + '',',')) != 0");
        sb.append("  and (info.srctype = 1 or info.srctype = 2)");
        //添加条件筛选
        addCondition(reportsParamVO, sb);
        sb.append(" group by gp.groupid");

        List<Map<String, Object>> dsyyGroupReports = jdbcTemplate.queryForList(sb.toString(),
                new Object[]{reportsParamVO.getCompanyId(),reportsParamVO.getStart(),reportsParamVO.getEnd()});
        List<DsyyReportsVO> dsyyReportsVOBak=new ArrayList<>();
        for(Map<String,Object> map:dsyyGroupReports){
            DsyyReportsVO dsyyReportsVO = new DsyyReportsVO();
            dsyyReportsVO.setGroupId(String.valueOf(map.get("groupId").toString()));
            dsyyReportsVO.setGroupName(String.valueOf(map.get("groupName").toString()));
            dsyyReportsVO.setComeShopClientCount(Integer.parseInt(String.valueOf(map.get("count").toString())));
            dsyyReportsVOBak.add(dsyyReportsVO);
        }
        for(DsyyReportsVO dsyyReportsVO:dsyyReportsVOS){
            for(DsyyReportsVO dsyyReportsVO1:dsyyReportsVOBak){
                if(dsyyReportsVO.getGroupId().equals(dsyyReportsVO1.getGroupId())){
                    dsyyReportsVO.setComeShopClientCount(dsyyReportsVO1.getWaitClientCount());
                }
            }
        }
        return null;
    }

    /**
     * 获取周末入店量
     */
    public Integer getWeekComeShopClientCount(ReportsParamVO reportsParamVO, List<DsyyReportsVO> dsyyReportsVOS)  {
        StringBuilder sb = new StringBuilder();
        sb.append("select gp.groupid groupId,gp.GROUPNAME groupName,IFNULL(count(info.GROUPID),0) as count from "+DBSplitUtil.getInfoTabName(reportsParamVO.getCompanyId())+" info ");
        sb.append(" left join");
        sb.append(" hm_pub_group gp on gp.GROUPID = info.GroupId ");
        sb.append(" where gp.GROUPTYPE  = 'dsyy' and gp.COMPANYID = ? and info.ISDEL = 0");
        sb.append(" and info.COMESHOPTIME BETWEEN ? AND ? and DAYOFWEEK(from_unixtime(info.COMESHOPTIME)) IN(1,7)");
        sb.append("  and (info.srctype = 1 or info.srctype = 2)");
//        sb.append(" AND INSTR( ?, CONCAT(',',info.STATUSID + '',',')) != 0");
        //添加条件筛选
        addCondition(reportsParamVO, sb);
        sb.append(" group by gp.groupid");

//        fieldList.add(ClientStatusConst.IS_COME_SHOP);
        List<Map<String, Object>> dsyyGroupReports = jdbcTemplate.queryForList(sb.toString(),
                new Object[]{reportsParamVO.getCompanyId(),reportsParamVO.getStart(),reportsParamVO.getEnd()});
        List<DsyyReportsVO> dsyyReportsVOBak=new ArrayList<>();
        for(Map<String,Object> map:dsyyGroupReports){
            DsyyReportsVO dsyyReportsVO = new DsyyReportsVO();
            dsyyReportsVO.setGroupId(String.valueOf(map.get("groupId").toString()));
            dsyyReportsVO.setGroupName(String.valueOf(map.get("groupName").toString()));
            dsyyReportsVO.setWeekComeShopClientCount(Integer.parseInt(String.valueOf(map.get("count").toString())));
            dsyyReportsVOBak.add(dsyyReportsVO);
        }
        for(DsyyReportsVO dsyyReportsVO:dsyyReportsVOS){
            for(DsyyReportsVO dsyyReportsVO1:dsyyReportsVOBak){
                if(dsyyReportsVO.getGroupId().equals(dsyyReportsVO1.getGroupId())){
                    dsyyReportsVO.setWeekComeShopClientCount(dsyyReportsVO1.getWeekComeShopClientCount());
                }
            }
        }



        return null;
    }

    /**
     * 获取成交量
     */
    public Integer getSuccessClientCount(ReportsParamVO reportsParamVO, List<DsyyReportsVO> dsyyReportsVOS)  {
        StringBuilder sb = new StringBuilder();
        sb.append("select gp.groupid groupId,gp.GROUPNAME groupName,IFNULL(count(info.GROUPID),0) as count from "+DBSplitUtil.getInfoTabName(reportsParamVO.getCompanyId())+" info ");
        sb.append(" left join");
        sb.append(" hm_pub_group gp on gp.GROUPID = info.GroupId ");
        sb.append(" where gp.GROUPTYPE  = 'dsyy' and gp.COMPANYID = ? and info.ISDEL = 0");
        sb.append(" and info.SUCCESSTIME BETWEEN ? AND ?");
        sb.append(" AND INSTR( ?, CONCAT(',',info.STATUSID + '',',')) != 0");
        sb.append("  and (info.srctype = 1 or info.srctype = 2)");
        //添加条件筛选
        addCondition(reportsParamVO, sb);
        sb.append(" group by gp.groupid");

        List<Map<String, Object>> dsyyGroupReports = jdbcTemplate.queryForList(sb.toString(),
                new Object[]{reportsParamVO.getCompanyId(),reportsParamVO.getStart(),reportsParamVO.getEnd(),ClientStatusConst.IS_SUCCESS});
        List<DsyyReportsVO> dsyyReportsVOBak=new ArrayList<>();
        for(Map<String,Object> map:dsyyGroupReports){
            DsyyReportsVO dsyyReportsVO = new DsyyReportsVO();
            dsyyReportsVO.setGroupId(String.valueOf(map.get("groupId").toString()));
            dsyyReportsVO.setGroupName(String.valueOf(map.get("groupName").toString()));
            dsyyReportsVO.setSuccessClientCount(Integer.parseInt(String.valueOf(map.get("count").toString())));
            dsyyReportsVOBak.add(dsyyReportsVO);
        }
        for(DsyyReportsVO dsyyReportsVO:dsyyReportsVOS){
            for(DsyyReportsVO dsyyReportsVO1:dsyyReportsVOBak){
                if(dsyyReportsVO.getGroupId().equals(dsyyReportsVO1.getGroupId())){
                    dsyyReportsVO.setSuccessClientCount(dsyyReportsVO1.getSuccessClientCount());
                }
            }
        }


        return null;
    }
    /**
     * 获取入店成交量
     */
    public Integer getComeShopSuccessClientCount(ReportsParamVO reportsParamVO, List<DsyyReportsVO> dsyyReportsVOS)  {
        StringBuilder sb = new StringBuilder();
        sb.append("select gp.groupid groupId,gp.GROUPNAME groupName,IFNULL(count(info.GROUPID),0) as count from "+DBSplitUtil.getInfoTabName(reportsParamVO.getCompanyId())+" info ");
        sb.append(" left join");
        sb.append(" hm_pub_group gp on gp.GROUPID = info.GroupId ");
        sb.append(" where gp.GROUPTYPE  = 'dsyy' and gp.COMPANYID = ? and info.ISDEL = 0");
        sb.append(" and info.SUCCESSTIME BETWEEN ? AND ?");
        sb.append(" AND INSTR( ?, CONCAT(',',info.STATUSID + '',',')) != 0");
        sb.append("  and (info.srctype = 1 or info.srctype = 2)");
        sb.append(" and info.statusid in (9,30)");
        //添加条件筛选
        addCondition(reportsParamVO, sb);
        sb.append(" group by gp.groupid");

        List<Map<String, Object>> dsyyGroupReports = jdbcTemplate.queryForList(sb.toString(),
                new Object[]{reportsParamVO.getCompanyId(),reportsParamVO.getStart(),reportsParamVO.getEnd(),ClientStatusConst.IS_SUCCESS});
        List<DsyyReportsVO> dsyyReportsVOBak=new ArrayList<>();
        for(Map<String,Object> map:dsyyGroupReports){
            DsyyReportsVO dsyyReportsVO = new DsyyReportsVO();
            dsyyReportsVO.setGroupId(String.valueOf(map.get("groupId").toString()));
            dsyyReportsVO.setGroupName(String.valueOf(map.get("groupName").toString()));
            dsyyReportsVO.setComeShopSuccessClientCount(Integer.parseInt(String.valueOf(map.get("count").toString())));
            dsyyReportsVOBak.add(dsyyReportsVO);
        }
        for(DsyyReportsVO dsyyReportsVO:dsyyReportsVOS){
            for(DsyyReportsVO dsyyReportsVO1:dsyyReportsVOBak){
                if(dsyyReportsVO.getGroupId().equals(dsyyReportsVO1.getGroupId())){
                    dsyyReportsVO.setComeShopSuccessClientCount(dsyyReportsVO1.getComeShopSuccessClientCount());
                }
            }
        }

        return null;
    }
    /**
     * 获取在线成交量
     */
    public Integer getOnLineSuccessClientCount(ReportsParamVO reportsParamVO, List<DsyyReportsVO> dsyyReportsVOS)  {
        StringBuilder sb = new StringBuilder();
        sb.append("select gp.groupid,gp.GROUPNAME,IFNULL(count(info.GROUPID),0) as count from "+DBSplitUtil.getInfoTabName(reportsParamVO.getCompanyId())+" info ");
        sb.append(" left join");
        sb.append(" hm_pub_group gp on gp.GROUPID = info.GroupId ");
        sb.append(" where gp.GROUPTYPE  = 'dsyy' and gp.COMPANYID = ? and info.ISDEL = 0");
        sb.append(" and info.SUCCESSTIME BETWEEN ? AND ?");
        sb.append(" AND INSTR( ?, CONCAT(',',info.STATUSID + '',',')) != 0");
        sb.append("  and (info.srctype = 1 or info.srctype = 2)");
        sb.append(" and info.statusid in (40)");
        //添加条件筛选
        addCondition(reportsParamVO, sb);
        sb.append(" group by gp.groupid");


        List<Map<String, Object>> dsyyGroupReports = jdbcTemplate.queryForList(sb.toString(),
                new Object[]{reportsParamVO.getCompanyId(),reportsParamVO.getStart(),reportsParamVO.getEnd(),ClientStatusConst.IS_SUCCESS});
        List<DsyyReportsVO> dsyyReportsVOBak=new ArrayList<>();
        for(Map<String,Object> map:dsyyGroupReports){
            DsyyReportsVO dsyyReportsVO = new DsyyReportsVO();
            dsyyReportsVO.setGroupId(String.valueOf(map.get("groupId").toString()));
            dsyyReportsVO.setGroupName(String.valueOf(map.get("groupName").toString()));
            dsyyReportsVO.setOnLineSuccessClientCount(Integer.parseInt(String.valueOf(map.get("count").toString())));
            dsyyReportsVOBak.add(dsyyReportsVO);
        }
        for(DsyyReportsVO dsyyReportsVO:dsyyReportsVOS){
            for(DsyyReportsVO dsyyReportsVO1:dsyyReportsVOBak){
                if(dsyyReportsVO.getGroupId().equals(dsyyReportsVO1.getGroupId())){
                    dsyyReportsVO.setOnLineSuccessClientCount(dsyyReportsVO1.getOnLineSuccessClientCount());
                }
            }
        }

        return null;
    }
    /**
     * 获取营业额
     */
    public Integer getSumAmount(ReportsParamVO reportsParamVO, List<DsyyReportsVO> dsyyReportsVOS)  {
        StringBuilder sb = new StringBuilder();
        sb.append("select gp.groupid groupId,gp.GROUPNAME groupName,sum(dtl.AMOUNT) as sum_amount from "+DBSplitUtil.getInfoTabName(reportsParamVO.getCompanyId())+" info ");
        sb.append(" left join");
        sb.append(" hm_pub_group gp on gp.GROUPID = info.GroupId ");
        sb.append(" left join ");
        sb.append(DBSplitUtil.getDetailTabName(reportsParamVO.getCompanyId()) + " dtl on info.KZID = dtl.KZID ");
        sb.append(" where gp.GROUPTYPE  = 'dsyy' and gp.COMPANYID = ? and info.isdel = 0");
        sb.append(" and info.SUCCESSTIME BETWEEN ? AND ?");
        sb.append("  and (info.srctype = 1 or info.srctype = 2)");
        //添加条件筛选
        addCondition(reportsParamVO, sb);
        sb.append(" group by gp.groupid");

        List<Map<String, Object>> dsyyGroupReports = jdbcTemplate.queryForList(sb.toString(),
                new Object[]{reportsParamVO.getCompanyId(),reportsParamVO.getStart(),reportsParamVO.getEnd()});
        List<DsyyReportsVO> dsyyReportsVOBak=new ArrayList<>();
        for(Map<String,Object> map:dsyyGroupReports){
            DsyyReportsVO dsyyReportsVO = new DsyyReportsVO();
            dsyyReportsVO.setGroupId(String.valueOf(map.get("groupId").toString()));
            dsyyReportsVO.setGroupName(String.valueOf(map.get("groupName").toString()));
            dsyyReportsVO.setSumAmount(((BigDecimal) map.get("sum_amount")).doubleValue());
            dsyyReportsVOBak.add(dsyyReportsVO);
        }
        for(DsyyReportsVO dsyyReportsVO:dsyyReportsVOS){
            for(DsyyReportsVO dsyyReportsVO1:dsyyReportsVOBak){
                if(dsyyReportsVO.getGroupId().equals(dsyyReportsVO1.getGroupId())){
                    dsyyReportsVO.setSumAmount(dsyyReportsVO1.getSumAmount());
                }
            }
        }


        return null;
    }

    public Integer getStayAmount(ReportsParamVO reportsParamVO,List<DsyyReportsVO> dsyyReportsVOS) {
        StringBuilder sb = new StringBuilder();
        sb.append("select gp.groupid,gp.GROUPNAME,sum(dtl.STAYAMOUNT) as sum_amount from "+DBSplitUtil.getInfoTabName(reportsParamVO.getCompanyId())+" info ");
        sb.append(" left join");
        sb.append(" hm_pub_group gp on gp.GROUPID = info.GroupId ");
        sb.append(" left join ");
        sb.append(DBSplitUtil.getDetailTabName(reportsParamVO.getCompanyId()) + " dtl on info.KZID = dtl.KZID ");
        sb.append(" where gp.GROUPTYPE  = 'dsyy' and gp.COMPANYID = ? and info.isdel = 0");
        sb.append(" and info.SUCCESSTIME BETWEEN ? AND ?");
        sb.append("  and (info.srctype = 1 or info.srctype = 2)");
        //添加条件筛选
        addCondition(reportsParamVO, sb);
        sb.append(" group by gp.groupid");

        List<Map<String, Object>> dsyyGroupReports = jdbcTemplate.queryForList(sb.toString(),
                new Object[]{reportsParamVO.getStart(),reportsParamVO.getEnd(),reportsParamVO.getCompanyId()});
        List<DsyyReportsVO> dsyyReportsVOBak=new ArrayList<>();
        for(Map<String,Object> map:dsyyGroupReports){
            DsyyReportsVO dsyyReportsVO = new DsyyReportsVO();
            dsyyReportsVO.setGroupId(String.valueOf(map.get("groupId").toString()));
            dsyyReportsVO.setGroupName(String.valueOf(map.get("groupName").toString()));
            dsyyReportsVO.setStayAmount(((BigDecimal) map.get("sum_amount")).doubleValue());
            dsyyReportsVOBak.add(dsyyReportsVO);
        }
        for(DsyyReportsVO dsyyReportsVO:dsyyReportsVOS){
            for(DsyyReportsVO dsyyReportsVO1:dsyyReportsVOBak){
                if(dsyyReportsVO.getGroupId().equals(dsyyReportsVO1.getGroupId())){
                    dsyyReportsVO.setStayAmount(dsyyReportsVO1.getStayAmount());
                }
            }
        }
        return null;
    }
    public void getRealAmount(ReportsParamVO reportsParamVO, List<DsyyReportsVO> dsyyReportsVOS)  {
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT ");
        sb.append(" gp.GROUPID groupId,");
        sb.append(" gp.GROUPNAME groupName,");
        sb.append(" sum(cash.AMOUNT) sum_amount");
        sb.append(" FROM hm_crm_cash_log cash");
        sb.append(" LEFT JOIN hm_crm_client_info info ON info.KZID = cash.KZID AND info.COMPANYID = cash.COMPANYID");
        sb.append(" left join hm_pub_group gp on gp.GROUPID=info.GROUPID and info.COMPANYID=gp.COMPANYID");
        sb.append(" WHERE cash.PAYMENTTIME BETWEEN ? AND ?");
        sb.append(" AND cash.COMPANYID = ?");
        sb.append(" and gp.GROUPTYPE='dsyy'");
        sb.append(" and gp.GROUPID is not null");
        sb.append("  and (info.srctype = 1 or info.srctype = 2)");
        //添加条件筛选
        addCondition(reportsParamVO, sb);
        sb.append(" group by gp.groupid");
        List<Map<String, Object>> dsyyGroupReports = jdbcTemplate.queryForList(sb.toString(),
                new Object[]{reportsParamVO.getStart(),reportsParamVO.getEnd(),reportsParamVO.getCompanyId()});
        List<DsyyReportsVO> dsyyReportsVOBak=new ArrayList<>();
        for(Map<String,Object> map:dsyyGroupReports){
            DsyyReportsVO dsyyReportsVO = new DsyyReportsVO();
            dsyyReportsVO.setGroupId(String.valueOf(map.get("groupId").toString()));
            dsyyReportsVO.setGroupName(String.valueOf(map.get("groupName").toString()));
            dsyyReportsVO.setRealAmount(((BigDecimal) map.get("sum_amount")).doubleValue());
            dsyyReportsVOBak.add(dsyyReportsVO);
        }
        for(DsyyReportsVO dsyyReportsVO:dsyyReportsVOS){
            for(DsyyReportsVO dsyyReportsVO1:dsyyReportsVOBak){
                if(dsyyReportsVO.getGroupId().equals(dsyyReportsVO1.getGroupId())){
                    dsyyReportsVO.setRealAmount(dsyyReportsVO1.getRealAmount());
                }
            }
        }


    }
    public void getBkAmount(ReportsParamVO reportsParamVO, List<DsyyReportsVO> dsyyReportsVOS)  {
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT ");
        sb.append(" gp.GROUPID groupId,");
        sb.append(" gp.GROUPNAME groupName,");
        sb.append(" sum(cash.AMOUNT) sum_amount");
        sb.append(" FROM hm_crm_cash_log cash");
        sb.append(" LEFT JOIN hm_crm_client_info info ON info.KZID = cash.KZID AND info.COMPANYID = cash.COMPANYID");
        sb.append(" left join hm_pub_group gp on gp.GROUPID=info.GROUPID and info.COMPANYID=gp.COMPANYID");
        sb.append(" WHERE cash.PAYMENTTIME BETWEEN ? AND ?");
        sb.append(" AND cash.COMPANYID = ?");
        sb.append(" and gp.GROUPTYPE='dsyy'");
        sb.append(" and gp.GROUPID is not null");
        sb.append("  and (info.srctype = 1 or info.srctype = 2)");
        sb.append(" and cash.typeid=2");
        //添加条件筛选
        addCondition(reportsParamVO, sb);
        sb.append(" group by gp.groupid");
        List<Map<String, Object>> dsyyGroupReports = jdbcTemplate.queryForList(sb.toString(),
                new Object[]{reportsParamVO.getStart(),reportsParamVO.getEnd(),reportsParamVO.getCompanyId()});
        List<DsyyReportsVO> dsyyReportsVOBak=new ArrayList<>();
        for(Map<String,Object> map:dsyyGroupReports){
            DsyyReportsVO dsyyReportsVO = new DsyyReportsVO();
            dsyyReportsVO.setGroupId(String.valueOf(map.get("groupId").toString()));
            dsyyReportsVO.setGroupName(String.valueOf(map.get("groupName").toString()));
            dsyyReportsVO.setBkAmount(((BigDecimal) map.get("sum_amount")).doubleValue());
            dsyyReportsVOBak.add(dsyyReportsVO);
        }
        for(DsyyReportsVO dsyyReportsVO:dsyyReportsVOS){
            for(DsyyReportsVO dsyyReportsVO1:dsyyReportsVOBak){
                if(dsyyReportsVO.getGroupId().equals(dsyyReportsVO1.getGroupId())){
                    dsyyReportsVO.setBkAmount(dsyyReportsVO1.getBkAmount());
                }
            }
        }


    }
    /**
     * 获取成交均价
     */
    public Integer getAvgAmount(ReportsParamVO reportsParamVO, List<DsyyReportsVO> dsyyReportsVOS)  {
        StringBuilder sb = new StringBuilder();
        sb.append("select gp.groupid,gp.GROUPNAME,avg(dtl.AMOUNT) as sum_amount from "+DBSplitUtil.getInfoTabName(reportsParamVO.getCompanyId())+" info ");
        sb.append(" left join");
        sb.append(" hm_pub_group gp on gp.GROUPID = info.GroupId ");
        sb.append(" left join ");
        sb.append(DBSplitUtil.getDetailTabName(reportsParamVO.getCompanyId()) + " dtl on info.KZID = dtl.KZID ");
        sb.append(" where gp.GROUPTYPE  = 'dsyy' and gp.COMPANYID = ? and info.isdel = 0");
        sb.append(" and info.SUCCESSTIME BETWEEN ? AND ?");
        sb.append("  and (info.srctype = 1 or info.srctype = 2)");
        //添加条件筛选
        addCondition(reportsParamVO, sb);
        sb.append(" group by gp.groupid");

        List<Map<String, Object>> dsyyGroupReports = jdbcTemplate.queryForList(sb.toString(),
                new Object[]{reportsParamVO.getCompanyId(),reportsParamVO.getStart(),reportsParamVO.getEnd()});
        List<DsyyReportsVO> dsyyReportsVOBak=new ArrayList<>();
        for(Map<String,Object> map:dsyyGroupReports){
            DsyyReportsVO dsyyReportsVO = new DsyyReportsVO();
            dsyyReportsVO.setGroupId(String.valueOf(map.get("groupId").toString()));
            dsyyReportsVO.setGroupName(String.valueOf(map.get("groupName").toString()));
            dsyyReportsVO.setAvgAmount(((BigDecimal) map.get("sum_amount")).doubleValue());
            dsyyReportsVOBak.add(dsyyReportsVO);
        }
        for(DsyyReportsVO dsyyReportsVO:dsyyReportsVOS){
            for(DsyyReportsVO dsyyReportsVO1:dsyyReportsVOBak){
                if(dsyyReportsVO.getGroupId().equals(dsyyReportsVO1.getGroupId())){
                    dsyyReportsVO.setAvgAmount(dsyyReportsVO1.getAvgAmount());
                }
            }
        }

        return null;
    }


    /**
     * 获取花费
     */
    public Integer getCost(ReportsParamVO reportsParamVO, List<DsyyReportsVO> dsyyReportsVOS)  {
        StringBuilder sb = new StringBuilder();
        sb.append(" select group_source.groupid groupId,gp.groupName groupName,ifnull(sum(avg_cost.avg_cost * group_source.group_count),0) as  sum_amount from ");
        sb.append(" (select sourceid,groupid,count(groupid) group_count from "+DBSplitUtil.getInfoTabName(reportsParamVO.getCompanyId())+" info");
        sb.append(" where info.groupid is not null and info.groupid != '' and info.isdel = 0  ");
        if(StringUtils.isNotEmpty(reportsParamVO.getGroupId())){
            StringBuilder paramsSb = new StringBuilder();
            String[] params = reportsParamVO.getGroupId().split(CommonConstant.STR_SEPARATOR);
            for (String str:params){
                paramsSb.append("'").append(str).append("'").append(",");
            }
            sb.append(" and info.GROUPID in( "+paramsSb.substring(0, paramsSb.toString().length() - 1)+" )");
        }
        sb.append(" and info.COMPANYID = ? and info.createtime between ? and ? ");
        sb.append("  and (info.srctype = 1 or info.srctype = 2)");
        sb.append(" group by groupid,sourceid ");
        sb.append(" order by groupid,sourceid ) group_source");
        sb.append(" left join ");
        sb.append(" (select id,srcName,info_cost,info_count,(info_cost/info_count) as avg_cost  from ");
        sb.append(" (select source.id,source.srcName,sum(cost.cost) info_cost from hm_crm_source source ");
        sb.append(" left join hm_crm_cost cost on source.id = cost.SRCID and cost.COMPANYID = ? ");
        sb.append(" where cost.costTime between ? and ? ");
        sb.append(" group by source.id) sum_cost ");
        sb.append(" left join ");
        sb.append(" (select sourceid,count(1) info_count from "+ DBSplitUtil.getInfoTabName(reportsParamVO.getCompanyId())+" info where isdel = 0 and info.COMPANYID = ? and info.createtime between ? and ?   group by sourceid) info_sum ");
        sb.append(" on sum_cost.id = info_sum.sourceid) avg_cost ");
        sb.append(" on group_source.sourceid = avg_cost.id ");
        sb.append(" left join hm_pub_group gp on gp.GROUPID = group_source.groupid");
        sb.append(" where gp.GROUPTYPE  = 'dsyy' and gp.GROUPID != '0' ");
        sb.append(" group by groupid ");
        List<Map<String, Object>> dsyyGroupReports = jdbcTemplate.queryForList(sb.toString(),
                new Object[]{reportsParamVO.getCompanyId(),reportsParamVO.getStart(),reportsParamVO.getEnd(),
                        reportsParamVO.getCompanyId(),reportsParamVO.getStart(),reportsParamVO.getEnd(),
                        reportsParamVO.getCompanyId(),reportsParamVO.getStart(),reportsParamVO.getEnd()});
        List<DsyyReportsVO> dsyyReportsVOBak=new ArrayList<>();
        for(Map<String,Object> map:dsyyGroupReports){
            DsyyReportsVO dsyyReportsVO = new DsyyReportsVO();
            dsyyReportsVO.setGroupId(String.valueOf(map.get("groupId").toString()));
            dsyyReportsVO.setGroupName(String.valueOf(map.get("groupName").toString()));
            dsyyReportsVO.setClientCost(parseDouble(((BigDecimal) map.get("sum_amount")).doubleValue()));
            dsyyReportsVOBak.add(dsyyReportsVO);
        }
        for(DsyyReportsVO dsyyReportsVO:dsyyReportsVOS){
            for(DsyyReportsVO dsyyReportsVO1:dsyyReportsVOBak){
                if(dsyyReportsVO.getGroupId().equals(dsyyReportsVO1.getGroupId())){
                    dsyyReportsVO.setClientCost(dsyyReportsVO1.getClientCost());
                }
            }
        }


        return null;
    }

    /**
     *  获取添加微信数
     * */
    private void getSuccessWechat(ReportsParamVO reportsParamVO, List<DsyyReportsVO> dsyyReportsVOS){
        //TODO 添加微信数
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT info.groupid groupId,gp.GROUPNAME groupName,count(WEFLAG) count  ");
        sb.append(" FROM "+ DBSplitUtil.getInfoTabName(reportsParamVO.getCompanyId())+" info");
        sb.append(" left join hm_pub_group gp on gp.GROUPID = info.GROUPID");
        sb.append(" WHERE info.weflag = 1 and  info.isdel = 0 AND info.companyid = ?");
        sb.append(" and info.groupid is not null");
        sb.append(" and info.CREATETIME between ? and ?");
        sb.append(" and gp.GROUPTYPE  = 'dsyy' and gp.GROUPID != '0' ");
        addCondition(reportsParamVO,sb);
        sb.append(" group by info.GROUPID");

        List<Map<String, Object>> dsyyGroupReports = jdbcTemplate.queryForList(sb.toString(),
                new Object[]{reportsParamVO.getCompanyId(),reportsParamVO.getStart(),reportsParamVO.getEnd()});
        List<DsyyReportsVO> dsyyReportsVOBak=new ArrayList<>();
        for(Map<String,Object> map:dsyyGroupReports){
            DsyyReportsVO dsyyReportsVO = new DsyyReportsVO();
            dsyyReportsVO.setGroupId(String.valueOf(map.get("groupId").toString()));
            dsyyReportsVO.setGroupName(String.valueOf(map.get("groupName").toString()));
            dsyyReportsVO.setSuccessWeChat(Integer.parseInt(String.valueOf(map.get("count").toString())));
            dsyyReportsVOBak.add(dsyyReportsVO);
        }
        for(DsyyReportsVO dsyyReportsVO:dsyyReportsVOS){
            for(DsyyReportsVO dsyyReportsVO1:dsyyReportsVOBak){
                if(dsyyReportsVO.getGroupId().equals(dsyyReportsVO1.getGroupId())){
                    dsyyReportsVO.setSuccessWeChat(dsyyReportsVO1.getComeShopSuccessClientCount());
                }
            }
        }
    }

    /**
     * 获取无效指标定义规则
     *
     * @param companyId
     * @return

    private DsInvalidVO getInvalidConfig(int companyId) {
        StringBuilder sb = new StringBuilder();
        DsInvalidVO dsInvalidVO = new DsInvalidVO();
        if (IntegerUtils.isInValid(companyId)) {
            return null;
        }
//        sb.append(" SELECT comp.DSINVALIDSTATUS, comp.DSINVALIDLEVEL, comp.DDISVALID, comp.DSDDSTATUS  FROM hm_pub_company comp WHERE comp.ID = ? AND comp.ISDEL = 0 ");
        String config = "";
        sb.append(" SELECT comp.REPORTSCONFIG  FROM hm_pub_company comp WHERE comp.ID = ? AND comp.ISDEL = 0 ");
        fieldList.clear();
        fieldList.add(companyId);

        RowSet rs = null;
        try {
            rs = dbSession.executeQuery(sb.toString(), fieldList);
            if (RowSetUtils.isNotEmpty(rs)) {
                RowSetUtils.first(rs);
                config = RowSetUtils.getString(rs, "REPORTSCONFIG");
            }
        } catch (Exception e) {

        }
        JSONObject configObj = JSONObject.parseObject(config);
        //筛选无效的意向等级
        JSONArray yxLevelArr = configObj.getJSONObject(ReportsConfigConst.WX_SET).getJSONArray(ReportsConfigConst.DZZ_YXDJ);
        //筛选无效状态
        JSONArray statusArr = configObj.getJSONObject(ReportsConfigConst.WX_SET).getJSONArray(ReportsConfigConst.WX_STATUS);
        //待定状态
        JSONArray xkzArr = configObj.getJSONObject(ReportsConfigConst.DS_DD_SET).getJSONArray(ReportsConfigConst.XKZ_CLASS);
        JSONArray yyyArr = configObj.getJSONObject(ReportsConfigConst.DS_DD_SET).getJSONArray(ReportsConfigConst.YYY_CLASS);
        JSONArray dzzArr = configObj.getJSONObject(ReportsConfigConst.DS_DD_SET).getJSONArray(ReportsConfigConst.DZZ_CLASS);
        JSONArray yjdArr = configObj.getJSONObject(ReportsConfigConst.DS_DD_SET).getJSONArray(ReportsConfigConst.YJD_CLASS);
        JSONArray yddArr = configObj.getJSONObject(ReportsConfigConst.DS_DD_SET).getJSONArray(ReportsConfigConst.YDD_CLASS);
        JSONArray wxArr = configObj.getJSONObject(ReportsConfigConst.DS_DD_SET).getJSONArray(ReportsConfigConst.WX_CLASS);
        JSONArray dsDdStatusArr = new JSONArray();
        dsDdStatusArr.addAll(xkzArr);
        dsDdStatusArr.addAll(yyyArr);
        dsDdStatusArr.addAll(dzzArr);
        dsDdStatusArr.addAll(yjdArr);
        dsDdStatusArr.addAll(yddArr);
        dsDdStatusArr.addAll(wxArr);


        dsInvalidVO.setDsInvalidStatus(arrToStr(statusArr));
        dsInvalidVO.setDsInvalidLevel(arrToStr(yxLevelArr));
        dsInvalidVO.setDdIsValid(configObj.getJSONObject(ReportsConfigConst.YX_SET).getBoolean(ReportsConfigConst.DD_IS_YX));
        dsInvalidVO.setDsDdStatus(arrToStr(dsDdStatusArr));

        return dsInvalidVO;
    }
     */
    /**
     * 数组转字符串
     * */
    private String arrToStr(JSONArray jsonArray){
        String str = "";
        if(!jsonArray.isEmpty()){
            str = jsonArray.toString().substring(1, jsonArray.toString().length() - 1);
        }
        return str;
    }

    //添加条件
    private void addCondition(ReportsParamVO reportsParamVO, StringBuilder sb) {
        if(StringUtils.isNotEmpty(reportsParamVO.getType())){
            sb.append(" and info.typeid in ( "+reportsParamVO.getType()+" )");
        }
        if(StringUtils.isNotEmpty(reportsParamVO.getSourceIds())){
            sb.append(" and info.sourceid in ("+reportsParamVO.getSourceIds()+")");
        }
        if(StringUtils.isNotEmpty(reportsParamVO.getGroupId())){
            StringBuilder paramsSb = new StringBuilder();
            String[] params = reportsParamVO.getGroupId().split(CommonConstant.STR_SEPARATOR);
            for (String str:params){
                paramsSb.append("'").append(str).append("'").append(",");
            }
            sb.append(" and gp.GROUPID in( "+paramsSb.substring(0, paramsSb.toString().length() - 1)+" )");
        }
    }


    /**
     * 计算客资量
     */
    public Integer computerRate(List<DsyyReportsVO> dsyyReportsVOS,DsInvalidVO dsInvalidVO) {
            for (DsyyReportsVO dsyyReportsVO : dsyyReportsVOS) {
                //非周末入店量
                dsyyReportsVO.setNotWeekComeShopClientCount(dsyyReportsVO.getComeShopClientCount() - dsyyReportsVO.getWeekComeShopClientCount());
                //有效量
                if(dsInvalidVO.getDdIsValid()){
                    dsyyReportsVO.setValidClientCount(dsyyReportsVO.getAllClientCount()-dsyyReportsVO.getInvalidClientCount()-dsyyReportsVO.getFilterWaitClientCount()-dsyyReportsVO.getInFilterClientCount()-dsyyReportsVO.getInvalidFilterClientCount());
                }else{
                    dsyyReportsVO.setValidClientCount(dsyyReportsVO.getAllClientCount()-dsyyReportsVO.getInvalidClientCount()-dsyyReportsVO.getFilterWaitClientCount()-dsyyReportsVO.getInFilterClientCount()-dsyyReportsVO.getInvalidFilterClientCount()-dsyyReportsVO.getWaitClientCount());
                }
                //客资量
                dsyyReportsVO.setClientCount(dsyyReportsVO.getAllClientCount() -dsyyReportsVO.getInvalidFilterClientCount()-dsyyReportsVO.getFilterWaitClientCount()-dsyyReportsVO.getInFilterClientCount());
                //有效率
                double validRate = (double)dsyyReportsVO.getValidClientCount() / dsyyReportsVO.getClientCount() ;
                dsyyReportsVO.setValidRate(parseDouble(((Double.isNaN(validRate)||Double.isInfinite(validRate))?0.0:validRate)*100));
                //计算毛客资入店率
                double clientComeShopRate = (double)dsyyReportsVO.getComeShopClientCount() / dsyyReportsVO.getClientCount()  ;
                dsyyReportsVO.setClientComeShopRate(parseDouble(((Double.isNaN(clientComeShopRate)||Double.isInfinite(clientComeShopRate))?0.0:clientComeShopRate)*100));
                //计算有效客资入店率
                double validClientComeShopRate =(double) dsyyReportsVO.getComeShopClientCount() / dsyyReportsVO.getValidClientCount() ;
                dsyyReportsVO.setValidComeShopRate(parseDouble(((Double.isNaN(validClientComeShopRate)||Double.isInfinite(validClientComeShopRate))?0.0:validClientComeShopRate)*100));
                //计算毛客资成交率
                double clientSuccessRate =(double) dsyyReportsVO.getSuccessClientCount()  / dsyyReportsVO.getClientCount() ;
                dsyyReportsVO.setClientSuccessRate(parseDouble(((Double.isNaN(clientSuccessRate)||Double.isInfinite(clientSuccessRate))?0.0:clientSuccessRate)*100));
                //有效客资成交率
                double validClientSuccessRate =(double) dsyyReportsVO.getSuccessClientCount() / dsyyReportsVO.getValidClientCount() ;
                dsyyReportsVO.setSuccessValidRate(parseDouble(((Double.isNaN(validClientSuccessRate)||Double.isInfinite(validClientSuccessRate))?0.0:validClientSuccessRate)*100));
                //入店成交率
                double comeShopSuccessRate =(double) dsyyReportsVO.getSuccessClientCount()  / dsyyReportsVO.getComeShopClientCount() ;
                dsyyReportsVO.setComeShopSuccessRate(parseDouble(((Double.isNaN(comeShopSuccessRate)||Double.isInfinite(comeShopSuccessRate))?0.0:comeShopSuccessRate)*100));
                //非周末入店占比
                double notWeekComeShopRate =(double) dsyyReportsVO.getNotWeekComeShopClientCount()  / dsyyReportsVO.getAllClientCount() ;
                dsyyReportsVO.setNotWeekComeShopRate(parseDouble(((Double.isNaN(notWeekComeShopRate)||Double.isInfinite(notWeekComeShopRate))?0.0:notWeekComeShopRate)*100));
                //毛客资成本
                double clientCost = dsyyReportsVO.getClientCost();
                dsyyReportsVO.setGroupClientCost(parseDouble((Double.isNaN(clientCost)||Double.isInfinite(clientCost))?0.0:clientCost));
                //入店成本
                double comeShopCost=(double)dsyyReportsVO.getClientCost()/dsyyReportsVO.getComeShopClientCount();
                dsyyReportsVO.setComeShopCost(parseDouble((Double.isNaN(comeShopCost)||Double.isInfinite(comeShopCost))?0.0:comeShopCost));
                //入店成本
                double successCost=(double)dsyyReportsVO.getClientCost()/dsyyReportsVO.getSuccessClientCount();
                dsyyReportsVO.setComeShopCost(parseDouble((Double.isNaN(successCost)||Double.isInfinite(successCost))?0.0:successCost));
                //ROI
                double roi =  dsyyReportsVO.getSumAmount()  / dsyyReportsVO.getClientCost() ;
                dsyyReportsVO.setRoi(parseDouble(((Double.isNaN(roi)||Double.isInfinite(roi))?0.0:roi)));
                //计算成交均价
                double avgAmount = dsyyReportsVO.getSumAmount() /dsyyReportsVO.getSuccessClientCount();
                dsyyReportsVO.setAvgAmount(parseDouble((Double.isNaN(avgAmount)||Double.isInfinite(avgAmount))?0.0:avgAmount));
            }
        return null;
    }

    /**
     * 计算合计
     * */
    public void computerTotal(List<DsyyReportsVO> dsyyReportsVOS){
        DsyyReportsVO dsyyTotal = new DsyyReportsVO();
        for (DsyyReportsVO dsyyReportsVO:dsyyReportsVOS){
            dsyyTotal.setGroupName("合计");
            //网转客资量
            dsyyTotal.setAvgAmount(dsyyReportsVO.getAvgAmount()+dsyyTotal.getAvgAmount());
            dsyyTotal.setNetTurnClientCount(dsyyReportsVO.getNetTurnClientCount()+dsyyTotal.getNetTurnClientCount());
            dsyyTotal.setAllClientCount(dsyyReportsVO.getAllClientCount()+dsyyTotal.getAllClientCount());
            dsyyTotal.setClientCount(dsyyReportsVO.getClientCount()+dsyyTotal.getClientCount());
            dsyyTotal.setValidClientCount(dsyyReportsVO.getValidClientCount()+dsyyTotal.getValidClientCount());
            dsyyTotal.setInvalidClientCount(dsyyReportsVO.getInvalidClientCount()+dsyyTotal.getInvalidClientCount());
            dsyyTotal.setWaitClientCount(dsyyReportsVO.getWaitClientCount()+dsyyTotal.getWaitClientCount());
            dsyyTotal.setComeShopClientCount(dsyyReportsVO.getComeShopClientCount()+dsyyTotal.getComeShopClientCount());
            dsyyTotal.setSuccessClientCount(dsyyReportsVO.getSuccessClientCount()+dsyyTotal.getSuccessClientCount());
            dsyyTotal.setComeShopSuccessClientCount(dsyyReportsVO.getComeShopSuccessClientCount()+dsyyTotal.getComeShopSuccessClientCount());
            dsyyTotal.setOnLineSuccessClientCount(dsyyReportsVO.getOnLineSuccessClientCount()+dsyyTotal.getOnLineSuccessClientCount());
            dsyyTotal.setWeekComeShopClientCount(dsyyReportsVO.getWeekComeShopClientCount()+dsyyTotal.getWeekComeShopClientCount());
            dsyyTotal.setNotWeekComeShopClientCount(dsyyReportsVO.getNotWeekComeShopClientCount()+dsyyTotal.getNotWeekComeShopClientCount());
            dsyyTotal.setSumAmount(dsyyReportsVO.getSumAmount()+dsyyTotal.getSumAmount());
            dsyyTotal.setStayAmount(dsyyReportsVO.getStayAmount()+dsyyTotal.getStayAmount());
            dsyyTotal.setClientCost(dsyyReportsVO.getClientCost()+dsyyTotal.getClientCost());
            dsyyTotal.setSuccessWeChat(dsyyReportsVO.getSuccessWeChat()+dsyyTotal.getSuccessWeChat());
            dsyyTotal.setRealAmount(dsyyReportsVO.getRealAmount()+dsyyTotal.getRealAmount());
            dsyyTotal.setBkAmount(dsyyReportsVO.getBkAmount()+dsyyTotal.getBkAmount());
        }
        dsyyReportsVOS.add(0,dsyyTotal);
    }


    /**
     * 只保留2位小数
     * */
    public double parseDouble(double result){
        return Double.parseDouble(String.format("%.2f",result));
    }
}
