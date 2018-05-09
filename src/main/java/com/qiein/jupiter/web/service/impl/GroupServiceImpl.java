package com.qiein.jupiter.web.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.qiein.jupiter.constant.*;
import com.qiein.jupiter.web.dao.*;
import com.qiein.jupiter.web.entity.po.ChannelPO;
import com.qiein.jupiter.web.entity.po.SourcePO;
import com.qiein.jupiter.web.entity.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.qiein.jupiter.exception.ExceptionEnum;
import com.qiein.jupiter.exception.RException;
import com.qiein.jupiter.util.CollectionUtils;
import com.qiein.jupiter.util.StringUtil;
import com.qiein.jupiter.web.entity.po.GroupPO;
import com.qiein.jupiter.web.entity.po.StaffPO;
import com.qiein.jupiter.web.service.GroupService;

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
    private ChannelDao channelDao;
    @Autowired
    private SourceDao sourceDao;

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
     * @param type
     * @param companyId
     * @return
     */
    @Override
    public List<GroupsInfoVO> getCompanyDeptListByType(String type,int staffId, int companyId) {
        //获取员工权限
        List<Integer> roleList = rolePermissionDao.getStaffPmsList(companyId,staffId);
        //根据类型获取公司的部门和小组
        List<GroupsInfoVO> list = groupDao.getCompanyDeptListByType(type,companyId);
        //获取员工所在小组列表
        List<String> groupList = groupDao.getGroupByStaffAndType(companyId, staffId, type);
        //获取员工所在部门列表
        List<String> deptList = groupDao.getDeptByTypeAndStaff(companyId, staffId, type);

        Map<String,Object> deptMap = new HashMap<>();
        for (GroupsInfoVO giv : list){  //获得部门map
            deptMap.put(giv.getGroupId(),giv);
        }


        if (roleList.contains(111)){    //查看所有  所有的都显示
            for (GroupsInfoVO giv:list){
                for (GroupsInfoVO sgiv:giv.getGroupList()){
                    sgiv.setShowFlag(true);
                }
                giv.setShowFlag(true);
            }
        }else if (roleList.contains(124)){  //查看部门
            for (GroupsInfoVO giv :list){
                if (deptList.contains(giv.getGroupId())){
                    giv.setShowFlag(true);
                    for (GroupsInfoVO sgiv:giv.getGroupList()){
                        sgiv.setShowFlag(true);
                    }
                }
            }
        }else if (roleList.contains(89) || roleList.contains(90)){  //查看小组
            boolean flag =false;
            for (GroupsInfoVO giv:list){    //遍历部门
                for (GroupsInfoVO sgiv:giv.getGroupList()){ //遍历小组
                    if (groupList.contains(sgiv.getGroupId())){ //如果是所在的小组  标记为可见
                        sgiv.setShowFlag(true);
                        flag=true;
                    }
                }
                giv.setShowFlag(flag);
                flag=false;
            }
        }else {
            list= null;
        }
        return list;
//
//        //获取员工权限
//        List<Integer> roleList = rolePermissionDao.getStaffPmsList(companyId,staffId);
//        //根据当前权限和类型获取公司的部门和小组
//        List<GroupsInfoVO> list = groupDao.getCompanyDeptListByType(type,companyId);
//        //获取员工所在小组列表
//        List<String> groupList = groupDao.getGroupByStaffAndType(companyId, staffId, type);
//        //获取员工所在部门列表
//        List<String> deptList = groupDao.getDeptByTypeAndStaff(companyId, staffId, type);
//        if (roleList.contains(111)){    //查看所有  所有的都显示
//            for (GroupsInfoVO giv:list){
//                for (GroupsInfoVO sgiv:giv.getGroupList()){
//                    sgiv.setShowFlag(true);
//                }
//                giv.setShowFlag(true);
//            }
//        }else if (roleList.contains(124)){  //查看部门
//            for (GroupsInfoVO giv :list){
//                if (deptList.contains(giv.getGroupId())){
//                    giv.setShowFlag(true);
//                    for (GroupsInfoVO sgiv:giv.getGroupList()){
//                        sgiv.setShowFlag(true);
//                    }
//                }
//            }
//        }else if (roleList.contains(89) || roleList.contains(90)){  //查看小组
//            boolean flag =false;
//            for (GroupsInfoVO giv:list){    //遍历部门
//                for (GroupsInfoVO sgiv:giv.getGroupList()){ //遍历小组
//                    if (groupList.contains(sgiv.getGroupId())){ //如果是所在的小组  标记为可见
//                        sgiv.setShowFlag(true);
//                        flag=true;
//                    }
//                }
//                giv.setShowFlag(flag);
//                flag=false;
//            }
//        }else {
//            list= null;
//        }
//        return list;
        //TODO
//        SELECT        未写完的一条sql
//        dept.*,grp.*,stf.*
//                FROM
//        hm_pub_group grp
//        LEFT JOIN
//        hm_pub_group dept ON grp.COMPANYID = dept.COMPANYID
//        AND grp.PARENTID = dept.GROUPID
//        LEFT JOIN
//        hm_pub_group_staff grpstf ON grpstf.COMPANYID = grp.COMPANYID
//        AND grpstf.GROUPID = grp.GROUPID
//        LEFT JOIN
//        hm_pub_staff stf ON stf.ID = grpstf.STAFFID
//        AND stf.COMPANYID = grpstf.COMPANYID
//        LEFT JOIN
//        (SELECT COUNT(1) FROM hm_pub_staff WHERE COMPANYID = 1 AND STATUSFLAG = 1) s ON
//        WHERE
//        dept.PARENTID = '0' AND dept.COMPANYID = 1 AND dept.GROUPTYPE = 'dsyy'

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
        GroupPO old = groupDao.getGroupById(groupPO.getCompanyId(), groupPO.getGroupId());
        if (old == null) {
            throw new RException(ExceptionEnum.GROUP_NOT_EXIT);
        }
        GroupPO groupDB = groupDao.getByName(groupPO.getGroupName(), groupPO.getCompanyId());
        // 验证是否存在相同的部门名称
        if (groupDB != null && groupDB.getId() != groupPO.getId()) {
            throw new RException(ExceptionEnum.GROUP_NAME_REPEAT);
        }
        // 判断是否需要设置主管姓名
        checkSetChiefsName(groupPO);
        groupDao.update(groupPO);
        // 把下属的组的类别也更新
        List<GroupPO> byParentId = groupDao.getByParentId(groupPO.getGroupId(), groupPO.getCompanyId());
        if (CollectionUtils.isNotEmpty(byParentId)) {
            for (GroupPO po : byParentId) {
                po.setGroupType(groupPO.getGroupType());
            }
            groupDao.batchUpdateGroupType(byParentId);
        }
        //更新名字，同步转介绍名字
        if (NumberConstant.DEFAULT_STRING_ZERO.equals(groupPO.getParentId())) {
            //获取渠道信息
            ChannelPO exist = channelDao.getChannelByNameAndType(groupPO.getCompanyId(), old.getGroupName(), ChannelConstant.STAFF_ZJS);
            if (exist != null) {
                exist.setChannelName(groupPO.getGroupName());
                channelDao.update(exist);
            }
        } else {
            SourcePO exist = sourceDao.getSourceByNameAndType(groupPO.getCompanyId(), old.getGroupName(), ChannelConstant.STAFF_ZJS);
            if (exist != null) {
                exist.setSrcName(groupPO.getGroupName());
                sourceDao.update(exist);
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
    public int delete(int id, int companyId) {
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
        //删除部门，同步删除渠道
        if (NumberConstant.DEFAULT_STRING_ZERO.equals(groupPO.getParentId())) {
            //获取渠道信息
            ChannelPO exist = channelDao.getChannelByNameAndType(groupPO.getCompanyId(), groupPO.getGroupName(), ChannelConstant.STAFF_ZJS);
            //TODO 查询该渠道下有没有客资
//            exist.setIsShow(false);
//            channelDao.update(exist);
            if (exist != null) {
                channelDao.deleteByIdAndCid(exist.getId(), exist.getCompanyId());
            }
        } else {
            //删除小组，同步删除来源
            SourcePO exist = sourceDao.getSourceByNameAndType(groupPO.getCompanyId(), groupPO.getGroupName(), ChannelConstant.STAFF_ZJS);
            //TODO 查询该来源下有没有客资
//            exist.setIsShow(false);
//            sourceDao.update(exist);
            if (exist != null) {
                sourceDao.deleteByIdAndCid(exist.getId(), exist.getCompanyId());
            }
        }
        return groupDao.delete(id);
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
        GroupPO groupDB = groupDao.getByName(groupPO.getGroupName(), groupPO.getCompanyId());
        // 验证是否存在相同的部门名称
        if (groupDB != null) {
            throw new RException(ExceptionEnum.GROUP_NAME_REPEAT);
        }
        // 判断是否需要设置主管姓名
        checkSetChiefsName(groupPO);
        // 新增
        groupDao.insert(groupPO);
        // 把组id 设置为父类Id+ 分隔符 + 新增的id
        groupPO.setGroupId(groupPO.getParentId() + CommonConstant.ROD_SEPARATOR + groupPO.getId());
        groupDao.update(groupPO);
        //新增转介绍渠道，若存在，开启，不存在新增
        if (NumberConstant.DEFAULT_STRING_ZERO.equals(groupPO.getParentId())) {
            ChannelPO exist = channelDao.getChannelByNameAndType(groupPO.getCompanyId(), groupPO.getGroupName(), ChannelConstant.STAFF_ZJS);
            if (exist == null) {
                //新增渠道
                ChannelPO channelPO = new ChannelPO(groupPO.getGroupName(), ChannelConstant.STAFF_ZJS, 0, groupPO.getCompanyId(), true);
                channelDao.insert(channelPO);
            } else if (!exist.getShowFlag()) {
                //开启渠道
                exist.setShowFlag(true);
                channelDao.update(exist);
            }
        } else {
            SourcePO exist = sourceDao.getSourceByNameAndType(groupPO.getCompanyId(), groupPO.getGroupName(), ChannelConstant.STAFF_ZJS);
            if (exist == null) {
                //新增来源
                //获取渠道ID
                ChannelPO channel = channelDao.getZjsChannelByDeptId(groupPO.getCompanyId(), groupPO.getParentId());
                SourcePO sourcePO = new SourcePO(groupPO.getGroupName(), ChannelConstant.STAFF_ZJS,
                        channel.getId(), channel.getChannelName(), groupPO.getCompanyId(), true, false);
                sourceDao.insert(sourcePO);
            } else {
                //开启来源
                exist.setIsShow(true);
                sourceDao.update(exist);
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
        }
    }

    /**
     * 根据不同角色，获取对应小组人员
     *
     * @param companyId
     * @param staffId
     * @return
     */
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
}
