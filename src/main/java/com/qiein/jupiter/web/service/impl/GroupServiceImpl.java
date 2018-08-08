package com.qiein.jupiter.web.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.qiein.jupiter.constant.ChannelConstant;
import com.qiein.jupiter.constant.CommonConstant;
import com.qiein.jupiter.constant.PmsConstant;
import com.qiein.jupiter.constant.RoleConstant;
import com.qiein.jupiter.enums.TableEnum;
import com.qiein.jupiter.exception.ExceptionEnum;
import com.qiein.jupiter.exception.RException;
import com.qiein.jupiter.util.CollectionUtils;
import com.qiein.jupiter.util.DBSplitUtil;
import com.qiein.jupiter.util.NumUtil;
import com.qiein.jupiter.util.StringUtil;
import com.qiein.jupiter.web.dao.*;
import com.qiein.jupiter.web.entity.po.ChannelPO;
import com.qiein.jupiter.web.entity.po.GroupPO;
import com.qiein.jupiter.web.entity.po.SourcePO;
import com.qiein.jupiter.web.entity.po.StaffPO;
import com.qiein.jupiter.web.entity.vo.*;
import com.qiein.jupiter.web.service.GroupService;
import io.jsonwebtoken.lang.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.channels.Channel;
import java.security.acl.Group;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 部门小组实现类
 */
@Service
public class GroupServiceImpl implements GroupService {

    @Autowired
    private GroupDao groupDao;
    @Autowired
    private GroupStaffDao groupStaffDao;
    @Autowired
    private StaffDao staffDao;
    @Autowired
    private RolePermissionDao rolePermissionDao;
    @Autowired
    private ShopChannelGroupDao shopChannelGroupDao;
    @Autowired
    private ChannelDao channelDao;
    @Autowired
    private SourceDao sourceDao;
    @Autowired
    private ShopDao shopDao;
    @Autowired
    private ClientDao clientDao;

    /**
     * @param companyId
     * @return
     */
    // @Cacheable(value = "dept", key = "'dept'+':'+#companyId")
    public List<GroupVO> getCompanyAllDeptList(int companyId) {
        return groupDao.getCompanyAllDeptList(companyId);
    }

    /**
     * 根据当前权限和类型获取公司的部门和小组
     *
     * @param type
     * @param companyId
     * @return
     */
    @Override
    public List<GroupsInfoVO> getStaffMarsInfo(String type, int staffId, int companyId) {
        // 获取员工权限
        List<Integer> roleList = rolePermissionDao.getStaffPmsList(companyId, staffId);
        // 根据类型获取公司的部门和小组
        List<GroupsInfoVO> list = groupDao.getCompanyDeptListByType(type, companyId);
        // 获取员工所在小组列表
        List<String> groupList = groupDao.getGroupByStaffAndType(companyId, staffId, type);
        // 获取员工所在部门列表
        List<String> deptList = groupDao.getDeptByTypeAndStaff(companyId, staffId, type);
        // 获取各小组内人员的接单数和在线人数
        List<GroupsInfoVO> infoList = groupStaffDao.getStaffMarsInfo(companyId);

        if (roleList.contains(111)) { // 查看所有 所有的都显示
            for (GroupsInfoVO giv : list) {
                for (GroupsInfoVO sgiv : giv.getGroupList()) {
                    sgiv.setShowFlag(true);
                    for (GroupsInfoVO svo : infoList) { // 给对应的小组附上在线人数和订单数
                        if (svo.getGroupId().equals(sgiv.getGroupId())) {
                            sgiv.setLineNum(svo.getLineNum() == null ? 0 : svo.getLineNum());
                            sgiv.setOrderNum(svo.getOrderNum() == null ? 0 : svo.getOrderNum());
                        }
                    }
                }
                giv.setShowFlag(true);
            }
        } else if (roleList.contains(124)) { // 查看部门
            for (GroupsInfoVO giv : list) {
                if (Arrays.asList(giv.getChiefIds().split(",")).contains(staffId + "")) {// 如果是该部门的主管
                    for (GroupsInfoVO sgiv : giv.getGroupList()) {
                        for (GroupsInfoVO svo : infoList) { // 给对应的小组附上在线人数和订单数
                            if (svo.getGroupId().equals(sgiv.getGroupId())) {
                                sgiv.setLineNum(svo.getLineNum() == null ? 0 : svo.getLineNum());
                                sgiv.setOrderNum(svo.getOrderNum() == null ? 0 : svo.getOrderNum());
                            }
                        }
                        sgiv.setShowFlag(true);
                    }

                } else if (deptList.contains(giv.getGroupId())) { // 如果在该部门，权限为查看本部门
                    giv.setShowFlag(true);
                    for (GroupsInfoVO sgiv : giv.getGroupList()) {
                        for (GroupsInfoVO svo : infoList) { // 给对应的小组附上在线人数和订单数
                            if (svo.getGroupId().equals(sgiv.getGroupId())) {
                                sgiv.setLineNum(svo.getLineNum() == null ? 0 : svo.getLineNum());
                                sgiv.setOrderNum(svo.getOrderNum() == null ? 0 : svo.getOrderNum());
                            }
                        }
                        sgiv.setShowFlag(true);
                    }
                }
            }
        } else if (roleList.contains(89) || roleList.contains(90)) { // 查看小组
            boolean flag = false;
            for (GroupsInfoVO giv : list) { // 遍历部门
                if (Arrays.asList(giv.getChiefIds().split(",")).contains(staffId + "")) {// 如果是该部门的主管
                    for (GroupsInfoVO sgiv : giv.getGroupList()) {
                        for (GroupsInfoVO svo : infoList) { // 给对应的小组附上在线人数和订单数
                            if (svo.getGroupId().equals(sgiv.getGroupId())) {
                                sgiv.setLineNum(svo.getLineNum() == null ? 0 : svo.getLineNum());
                                sgiv.setOrderNum(svo.getOrderNum() == null ? 0 : svo.getOrderNum());
                            }
                        }
                        sgiv.setShowFlag(true);
                    }
                    giv.setShowFlag(true);
                } else {
                    for (GroupsInfoVO sgiv : giv.getGroupList()) { // 遍历小组
                        if (groupList.contains(sgiv.getGroupId())) { // 如果是所在的小组
                            // 标记为可见
                            sgiv.setShowFlag(true);
                            flag = true;
                        }
                        for (GroupsInfoVO svo : infoList) { // 给对应的小组附上在线人数和订单数
                            if (svo.getGroupId().equals(sgiv.getGroupId())) {
                                sgiv.setLineNum(svo.getLineNum() == null ? 0 : svo.getLineNum());
                                sgiv.setOrderNum(svo.getOrderNum() == null ? 0 : svo.getOrderNum());
                            }
                        }
                    }
                    giv.setShowFlag(flag);
                    flag = false;
                }
            }
        } else {
            list = null;
        }

        if (list != null) { // 计算部门总在线人数和今日接单数
            int lineNum = 0;
            int orderNum = 0;
            for (GroupsInfoVO giv : list) {
                for (GroupsInfoVO sgiv : giv.getGroupList()) {
                    lineNum += sgiv.getLineNum() == null ? 0 : sgiv.getLineNum();
                    orderNum += sgiv.getOrderNum() == null ? 0 : sgiv.getOrderNum();
                }
                giv.setLineNum(lineNum);
                giv.setOrderNum(orderNum);
                lineNum = 0;
                orderNum = 0;
            }
        }
        return list;

        /*
         * SELECT base.GROUPID , a.orderNum , b.lineNum FROM hm_pub_group_staff
         * base LEFT JOIN (SELECT gs.GROUPID aid, SUM(stf.TODAYNUM) orderNum
         * FROM hm_pub_staff stf JOIN hm_pub_group_staff gs ON stf.COMPANYID =
         * gs.COMPANYID AND stf.ID = gs.STAFFID WHERE stf.COMPANYID = 1 GROUP BY
         * gs.GROUPID) a ON a.aid = base.GROUPID LEFT JOIN (SELECT gs.GROUPID
         * bid, COUNT(stf.STATUSFLAG=1) lineNum FROM hm_pub_staff stf JOIN
         * hm_pub_group_staff gs ON stf.COMPANYID = gs.COMPANYID AND stf.ID =
         * gs.STAFFID WHERE stf.COMPANYID = 1 AND stf.STATUSFLAG = 1 GROUP BY
         * gs.GROUPID) b ON b.bid = base.GROUPID WHERE base.COMPANYID = 1 GROUP
         * BY base.GROUPID
         */

        //
        // //获取员工权限
        // List<Integer> roleList =
        // rolePermissionDao.getStaffPmsList(companyId,staffId);
        // //根据当前权限和类型获取公司的部门和小组
        // List<GroupsInfoVO> list =
        // groupDao.getCompanyDeptListByType(type,companyId);
        // //获取员工所在小组列表
        // List<String> groupList = groupDao.getGroupByStaffAndType(companyId,
        // staffId, type);
        // //获取员工所在部门列表
        // List<String> deptList = groupDao.getDeptByTypeAndStaff(companyId,
        // staffId, type);
        // if (roleList.contains(111)){ //查看所有 所有的都显示
        // for (GroupsInfoVO giv:list){
        // for (GroupsInfoVO sgiv:giv.getGroupList()){
        // sgiv.setShowFlag(true);
        // }
        // giv.setShowFlag(true);
        // }
        // }else if (roleList.contains(124)){ //查看部门
        // for (GroupsInfoVO giv :list){
        // if (deptList.contains(giv.getGroupId())){
        // giv.setShowFlag(true);
        // for (GroupsInfoVO sgiv:giv.getGroupList()){
        // sgiv.setShowFlag(true);
        // }
        // }
        // }
        // }else if (roleList.contains(89) || roleList.contains(90)){ //查看小组
        // boolean flag =false;
        // for (GroupsInfoVO giv:list){ //遍历部门
        // for (GroupsInfoVO sgiv:giv.getGroupList()){ //遍历小组
        // if (groupList.contains(sgiv.getGroupId())){ //如果是所在的小组 标记为可见
        // sgiv.setShowFlag(true);
        // flag=true;
        // }
        // }
        // giv.setShowFlag(flag);
        // flag=false;
        // }
        // }else {
        // list= null;
        // }
        // return list;
        // TODO
        // SELECT 未写完的一条sql
        // dept.*,grp.*,stf.*
        // FROM
        // hm_pub_group grp
        // LEFT JOIN
        // hm_pub_group dept ON grp.COMPANYID = dept.COMPANYID
        // AND grp.PARENTID = dept.GROUPID
        // LEFT JOIN
        // hm_pub_group_staff grpstf ON grpstf.COMPANYID = grp.COMPANYID
        // AND grpstf.GROUPID = grp.GROUPID
        // LEFT JOIN
        // hm_pub_staff stf ON stf.ID = grpstf.STAFFID
        // AND stf.COMPANYID = grpstf.COMPANYID
        // LEFT JOIN
        // (SELECT COUNT(1) FROM hm_pub_staff WHERE COMPANYID = 1 AND STATUSFLAG
        // = 1) s ON
        // WHERE
        // dept.PARENTID = '0' AND dept.COMPANYID = 1 AND dept.GROUPTYPE =
        // 'dsyy'

    }

    /**
     * 修改部门信息
     *
     * @param groupPO
     * @return
     */
    @Override
    @Transactional
    public GroupPO update(GroupPO groupPO) {
        if (groupPO == null) {
            throw new RException(ExceptionEnum.UNKNOW_ERROR);
        }
        GroupPO old = groupDao.getGroupById(groupPO.getCompanyId(), groupPO.getGroupId());
        if (old == null) {
            throw new RException(ExceptionEnum.GROUP_NOT_EXIT);
        }
        GroupPO groupDB = groupDao.getByName(groupPO.getGroupName(), groupPO.getCompanyId());
        // 验证是否存在相同的部门名称
        if (groupDB != null && groupDB.getId() != groupPO.getId()) {
            throw new RException(ExceptionEnum.GROUP_NAME_REPEAT);
        }
        if (RoleConstant.MSJD.equalsIgnoreCase(groupPO.getGroupType()) && !CommonConstant.DEFAULT_STRING_ZERO.equalsIgnoreCase(groupPO.getParentId())) {
            if (!NumUtil.isValid(groupPO.getShopId())) {
                throw new RException(ExceptionEnum.SHOP_ID_NULL);
            }
            ShopVO shopVO = shopDao.getShowShopById(groupPO.getCompanyId(), groupPO.getShopId());
            if (null == shopVO) {
                throw new RException(ExceptionEnum.SHOP_ID_NULL);
            }
        }
        // 判断是否需要设置主管姓名
        checkSetChiefsName(groupPO);
        groupDao.update(groupPO);
        // 把下属的组的类别也更新
        groupDao.batchUpdateGroupType(groupPO.getGroupType(), groupPO.getGroupId(), groupPO.getCompanyId());
        //节点不存在
        if (CommonConstant.DEFAULT_STRING_ZERO.equalsIgnoreCase(groupPO.getParentId())) {
            //根节点
            ChannelPO channelPO = channelDao.getChannelByGroupName(old.getGroupName(), old.getCompanyId());
            if (null != channelPO) {
                //是根节点
                channelPO.setCompanyId(groupPO.getCompanyId());
                channelPO.setChannelName(groupPO.getGroupName());
                channelPO.setShowFlag(true);
                channelDao.update(channelPO);
            }
        } else {
            ChannelPO channelPO = channelDao.getChannelByGroupParentId(groupPO.getParentId(), old.getCompanyId());
            if (channelPO != null) {
                SourcePO sourcePO = sourceDao.getSourceBySrcname(old.getGroupName(), old.getCompanyId(), channelPO.getId());
                if (null != sourcePO) {
                    sourcePO.setSrcName(groupPO.getGroupName());
                    sourcePO.setChannelId(channelPO.getId());
                    sourcePO.setCompanyId(channelPO.getCompanyId());
                    sourcePO.setIsShow(true);
                    sourceDao.update(sourcePO);
                }
            }
        }
        return groupPO;
    }

    /**
     * 删除部门信息
     *
     * @param companyId
     * @return
     */
    @Override
    @Transactional
    public GroupPO delete(int id, int companyId) {
        // 先判断是否有下属部门
        GroupPO groupPO = groupDao.getById(id);
        if (groupPO == null) {
            throw new RException(ExceptionEnum.GROUP_NOT_EXIT);
        }
        List<GroupPO> byParentId = groupDao.getByParentId(groupPO.getGroupId(), companyId);
        if (CollectionUtils.isNotEmpty(byParentId)) {
            throw new RException(ExceptionEnum.GROUP_HAVE_CHILD_GROUP);
        }
        // 是否有下属员工
        List<StaffVO> groupStaffs = groupStaffDao.getGroupStaffs(companyId, groupPO.getGroupId());
        if (CollectionUtils.isNotEmpty(groupStaffs)) {
            throw new RException(ExceptionEnum.GROUP_HAVE_STAFF);
        }
        //删除
        if (1 != groupDao.delete(id)) {
            throw new RException(ExceptionEnum.DELETE_FAIL);
        }
        //删除拍摄地-渠道-组关联表中的
        shopChannelGroupDao.delByGroupId(companyId, groupPO.getGroupId());

        //TODO dstj dsyy dssx
        //节点不存在
        if (CommonConstant.DEFAULT_STRING_ZERO.equalsIgnoreCase(groupPO.getParentId())) {
            ChannelPO channelPO = channelDao.getChannelByGroupName(groupPO.getGroupName(), groupPO.getCompanyId());
            if (null != channelPO) {
                List<SourcePO> sourcePOS = sourceDao.getSourceListByChannelId(channelPO.getId(), channelPO.getCompanyId());
                if (CollectionUtils.isNotEmpty(sourcePOS)) {
                    sourceDao.updateIsShowByChannelId(channelPO.getId(), channelPO.getCompanyId());
                    channelPO.setShowFlag(false);
                    channelDao.update(channelPO);
                } else {
                    channelDao.deleteByIdAndCid(channelPO.getId(), channelPO.getCompanyId());
                }
            }
        } else {
            ChannelPO channelPO = channelDao.getChannelByGroupParentId(groupPO.getParentId(), groupPO.getCompanyId());
            if (null != channelPO) {
                SourcePO sourcePO = sourceDao.getSourceBySrcname(groupPO.getGroupName(), groupPO.getCompanyId(), channelPO.getId());
                if (null != sourcePO) {
                    if (channelPO.getTypeId().equals(ChannelConstant.STAFF_ZJS)) {
                        //如果没有客资就删除
                        Integer kzNum = clientDao.getKzNumBySourceId(DBSplitUtil.getTable(TableEnum.info, sourcePO.getCompanyId()), sourcePO.getId(), sourcePO.getCompanyId());
                        if (kzNum > 0) {
                            sourcePO.setIsShow(false);
                            sourceDao.update(sourcePO);
                        } else {
                            sourceDao.deleteByIdAndCid(sourcePO.getId(), sourcePO.getCompanyId());
                        }
                    } else {
                        sourcePO.setIsShow(false);
                        sourceDao.update(sourcePO);
                    }
                }
            }
        }
        return groupPO;
    }

    /**
     * 新增部门信息
     *
     * @param groupPO
     * @return
     */
    @Override
    @Transactional
    public GroupPO insert(GroupPO groupPO) {
        if (null == groupPO) {
            throw new RException(ExceptionEnum.HTTP_PARAMETER_ERROR);
        }
        GroupPO groupDB = groupDao.getByName(groupPO.getGroupName(), groupPO.getCompanyId());
        // 验证是否存在相同的部门名称
        if (groupDB != null) {
            throw new RException(ExceptionEnum.GROUP_NAME_REPEAT);
        }
        if (RoleConstant.MSJD.equalsIgnoreCase(groupPO.getGroupType()) && !CommonConstant.DEFAULT_STRING_ZERO.equalsIgnoreCase(groupPO.getParentId())) {
            if (!NumUtil.isValid(groupPO.getShopId())) {
                throw new RException(ExceptionEnum.SHOP_ID_NULL);
            }
            ShopVO shopVO = shopDao.getShowShopById(groupPO.getCompanyId(), groupPO.getShopId());
            if (null == shopVO) {
                throw new RException(ExceptionEnum.SHOP_ID_NULL);
            }
        }
        // 判断是否需要设置主管姓名
        checkSetChiefsName(groupPO);
        // 新增
        groupDao.insert(groupPO);
        // 把组id 设置为父类Id+ 分隔符 + 新增的id
        groupPO.setGroupId(groupPO.getParentId() + CommonConstant.ROD_SEPARATOR + groupPO.getId());
        groupDao.update(groupPO);

        if( !(RoleConstant.DSYY.equalsIgnoreCase(groupPO.getGroupType()) || RoleConstant.DSSX.equalsIgnoreCase(groupPO.getGroupType()) || RoleConstant.DSCJ.equalsIgnoreCase(groupPO.getGroupType()))){
            //同步转介绍渠道小组
            ChannelPO channelPO = channelDao.getChannelByGroupName(groupPO.getGroupName(), groupPO.getCompanyId());
            //渠道不存在
            if (null == channelPO) {
                //根节点
                if (CommonConstant.DEFAULT_STRING_ZERO.equalsIgnoreCase(groupPO.getParentId())) {
                    channelPO = new ChannelPO();
                    channelPO.setTypeId(ChannelConstant.STAFF_ZJS);
                    channelPO.setCompanyId(groupPO.getCompanyId());
                    channelPO.setChannelName(groupPO.getGroupName());
                    channelPO.setPriority(CommonConstant.DEFAULT_ZERO);
                    channelPO.setPushRule(CommonConstant.DEFAULT_ZERO);
                    channelPO.setShowFlag(true);
                    channelPO.setFilterFlag(false);
                    channelDao.insert(channelPO);
                } else {
                    //非根节点
                    channelPO = channelDao.getChannelByGroupParentId(groupPO.getParentId(), groupPO.getCompanyId());
                    if (null == channelPO) {
                        //渠道不存在
                        GroupPO groupPOBak = groupDao.getGroupById(groupPO.getCompanyId(), groupPO.getParentId());
                        channelPO = new ChannelPO();
                        channelPO.setTypeId(ChannelConstant.STAFF_ZJS);
                        channelPO.setCompanyId(groupPO.getCompanyId());
                        channelPO.setChannelName(groupPOBak.getGroupName());
                        channelPO.setPriority(CommonConstant.DEFAULT_ZERO);
                        channelPO.setPushRule(CommonConstant.DEFAULT_ZERO);
                        channelPO.setShowFlag(true);
                        channelPO.setFilterFlag(false);
                        channelDao.insert(channelPO);
                        //来源
                        SourcePO sourcePO = new SourcePO();
                        sourcePO.setTypeId(ChannelConstant.STAFF_ZJS);
                        sourcePO.setSrcName(groupPO.getGroupName());
                        sourcePO.setCompanyId(groupPO.getCompanyId());
                        sourcePO.setChannelId(channelPO.getId());
                        sourcePO.setChannelName(channelPO.getChannelName());
                        sourcePO.setPushRule(CommonConstant.DEFAULT_ZERO);
                        sourcePO.setIsShow(true);
                        sourcePO.setIsFilter(false);
                        sourceDao.insert(sourcePO);
                    } else {
                        //渠道存在
                        //FIXME review code
                        //TODO 增加迁移功能
                        SourcePO source = sourceDao.getSourceByNameAndType(groupPO.getGroupName(),groupPO.getCompanyId(),ChannelConstant.STAFF_ZJS);
                        //判断来源时候需要迁移
                        if(source != null  &&  !(source.getChannelId().equals(channelPO.getId()))){
                            //迁移客资到新的渠道
                            clientDao.updateKzChannelId(DBSplitUtil.getInfoTabName(channelPO.getCompanyId()),channelPO.getId(),source.getId(),channelPO.getCompanyId());
                            //迁移来源到新的渠道
                            source.setChannelId(channelPO.getId());
                            source.setIsShow(true);
                            sourceDao.update(source);
                        }else{
                            SourcePO sourcePO = sourceDao.getSourceBySrcname(groupPO.getGroupName(), groupPO.getCompanyId(), channelPO.getId());
                            if (!channelPO.getShowFlag()) {
                                channelPO.setShowFlag(true);
                                channelDao.update(channelPO);
                            }
                            //来源不存在
                            if (null == sourcePO) {
                                sourcePO = new SourcePO();
                                sourcePO.setSrcName(groupPO.getGroupName());
                                sourcePO.setTypeId(ChannelConstant.STAFF_ZJS);
                                sourcePO.setCompanyId(groupPO.getCompanyId());
                                sourcePO.setChannelId(channelPO.getId());
                                sourcePO.setChannelName(channelPO.getChannelName());
                                sourcePO.setBrandId(channelPO.getBrandId());
                                sourcePO.setBrandName(channelPO.getBrandName());
                                sourcePO.setPushRule(CommonConstant.DEFAULT_ZERO);
                                sourcePO.setIsShow(channelPO.getShowFlag());
                                sourcePO.setIsFilter(false);
                                sourceDao.insert(sourcePO);
                            } else {
                                //来源存在
                                if (!sourcePO.getIsShow()) {
                                    sourcePO.setIsShow(true);
                                    sourceDao.update(sourcePO);
                                }
                            }
                        }
                    }
                }
            } else {
                if (!channelPO.getShowFlag()) {
                    channelPO.setShowFlag(true);
                    channelDao.update(channelPO);
                }
            }
        }
        return groupPO;
    }

    /**
     * 获取所有部门和员工
     *
     * @param companyId
     * @return
     */
    @Override
    public List<GroupStaffVO> getAllDeptAndStaff(int companyId) {
        return groupStaffDao.getAllDeptAndStaff(companyId);
    }

    /**
     * 设置主管姓名
     */
    private void checkSetChiefsName(GroupPO groupPO) {
        String chiefIds = groupPO.getChiefIds();
        // 如果id不为空
        if (StringUtil.isNotEmpty(chiefIds)) {
            // 根据ids获取员工数组
            String[] split = chiefIds.split(CommonConstant.STR_SEPARATOR);
            List<StaffPO> staffPOS = staffDao.batchGetByIds(split, groupPO.getCompanyId());
            StringBuilder chiefNames = new StringBuilder();
            // 遍历追加姓名
            for (int i = 0; i < staffPOS.size(); i++) {
                chiefNames.append(staffPOS.get(i).getNickName());
                if (i < (staffPOS.size() - 1)) {
                    chiefNames.append(CommonConstant.STR_SEPARATOR + " ");
                }
            }
            // 设置
            groupPO.setChiefNames(chiefNames.toString());
        } else {
            //id为空 清空chilefname
            groupPO.setChiefNames("");
        }
    }

    /**
     * 根据不同角色，获取对应小组人员
     *
     * @param companyId
     * @param staffId
     * @return
     */
    @Override
    public List<GroupBaseStaffVO> getGroupStaffByType(int companyId, int staffId, String role) {
        // 1、查询所有小组人员
        List<GroupBaseStaffVO> groupList = groupStaffDao.getGroupStaffByRole(companyId, role);
        // 2.查询员工权限
        List<Integer> pmsList = rolePermissionDao.getStaffPmsList(companyId, staffId);

        List<String> groupIdList = null;
        Integer id = null;
        boolean all = false;
        if (pmsList.contains(PmsConstant.SEE_MYSELF)) {
            // 只看自己
            id = staffId;
        } else if (pmsList.contains(PmsConstant.SEE_MY_GROUP)) {
            // 只看本组
            groupIdList = groupDao.getGroupByStaffAndType(companyId, staffId, role);
        } else if (pmsList.contains(PmsConstant.SEE_MY_DEPT)) {
            // 查看部门
            groupIdList = groupDao.getDeptByStaffAndType(companyId, staffId, role);
        } else if (pmsList.contains(PmsConstant.SEE_ALL)) {
            // 查看所有
            all = true;
        }
        for (GroupBaseStaffVO grp : groupList) {
            // 只看自己
            if (id != null && CollectionUtils.isNotEmpty(grp.getStaffList())) {
                for (BaseStaffVO staff : grp.getStaffList()) {
                    if (staff.getStaffId() == id.intValue()) {
                        staff.setSelectFlag(true);
                    }
                }
                continue;
            }
            // 只看本组或只看部门
            if (CollectionUtils.isNotEmpty(groupIdList) && groupIdList.contains(grp.getGroupId())
                    && CollectionUtils.isNotEmpty(grp.getStaffList())) {
                grp.setSelectFlag(true);
                for (BaseStaffVO staff : grp.getStaffList()) {
                    staff.setSelectFlag(true);
                }
                continue;
            }
            // 查看所有
            if (all && CollectionUtils.isNotEmpty(grp.getStaffList())) {
                grp.setSelectFlag(true);
                for (BaseStaffVO staff : grp.getStaffList()) {
                    staff.setSelectFlag(true);
                }
            }

        }
        return groupList;
    }

    /**
     * 获取邀约客服小组及人员
     *
     * @param companyId
     * @return
     */
    @Override
    public List<GroupBaseStaffVO> getDsyyGroupStaffList(int companyId) {
        List<GroupBaseStaffVO> groupList = groupStaffDao.getGroupStaffByRole(companyId, RoleConstant.DSYY);
        if (CollectionUtils.isNotEmpty(groupList)) {
            for (GroupBaseStaffVO group : groupList) {
                if (CollectionUtils.isNotEmpty(group.getStaffList())) {
                    for (BaseStaffVO vo : group.getStaffList()) {
                        if (vo.getStatusFlag() == 1 && !vo.getLockFlag()) {
                            vo.setSelectFlag(true);
                        }
                    }
                }
            }
        }
        return groupList;
    }

    /**
     * 根据角色，获取对应的全部小组人员
     *
     * @param companyId
     * @return
     */
    @Override
    public List<GroupBaseStaffVO> getAllGroupStaffByRole(int companyId, String role) {
        return groupStaffDao.getGroupStaffByRole(companyId, role);
    }

    /**
     * 获取门市下面的所有人员列表
     *
     * @param companyId
     * @return
     */
    public List<BaseStaffVO> getMsjdStaffList(int companyId) {
        return groupStaffDao.getMsjdStaffList(companyId);
    }

    /**
     * 根据角色，获取在线小组人员，用于分配
     *
     * @param companyId
     * @return
     */
    public List<GroupBaseStaffVO> getOnLineStaffListByRole(int companyId, String role) {
        return groupStaffDao.getOnLineStaffListByRole(companyId, role);
    }

    /**
     * 获取全公司所有小组人员列表
     *
     * @param companyId
     * @return
     */
    public List<GroupBaseStaffVO> getAllGroupStaff(int companyId) {
        return groupStaffDao.getAllGroupStaff(companyId);
    }

    /**
     * 根据类型获取企业小组列表
     *
     * @param companyId
     * @param type
     * @return
     */
    public List<GroupPO> getGroupListByType(int companyId, String type) {
        return groupDao.getGroupListByType(companyId, type);
    }

    @Override
    public List<GroupVO> getDepartGroupListByType(int companyId, String role) {
        return groupDao.getDepartGroupListByType(companyId, role);
    }

    @Override
    @Transactional
    public void editProiority(Integer fId, Integer fPriority, Integer sId, Integer sPriority, Integer companyId) {
        groupDao.updatePriority(fId, fPriority, companyId);
        groupDao.updatePriority(sId, sPriority, companyId);
    }
    /**
     * 获取小组名称
     * @param groupId
     * @return
     */
    public String getGroupName(String groupId,Integer companyId){
        return groupDao.getGroupName(groupId,companyId);
    }
}
