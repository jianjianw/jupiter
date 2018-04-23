package com.qiein.jupiter.web.service;

import com.github.pagehelper.PageInfo;
import com.qiein.jupiter.web.entity.dto.QueryMapDTO;
import com.qiein.jupiter.web.entity.po.CompanyPO;
import com.qiein.jupiter.web.entity.po.StaffPO;
import com.qiein.jupiter.web.entity.vo.GroupStaffVO;
import com.qiein.jupiter.web.entity.vo.StaffBaseInfoVO;
import com.qiein.jupiter.web.entity.vo.StaffPermissionVO;
import com.qiein.jupiter.web.entity.vo.StaffVO;

import java.util.List;

/**
 * 员工
 */
public interface StaffService {

    /**
     * 员工新增
     *
     * @param
     * @return
     */
    StaffVO insert(StaffVO staffVO);

    /**
     * 设置员工锁定状态
     *
     * @param id
     * @param lockFlag
     */
    StaffPO setLockState(int id, int companyId, boolean lockFlag);


    /**
     * 设置在线状态
     *
     * @param id
     * @param showFlag
     */
    StaffPO setOnlineState(int id, int companyId, int showFlag);

    /**
     * 员工删除（物理删除）
     *
     * @param
     * @return
     */
    int delete(int id, int companyId);

    /**
     * 批量删除员工（物理删除）
     *
     * @return
     */
    void batDelete(String[] ids, int companyId);

    /**
     * 批量检查是否可删除
     *
     * @param ids
     * @param companyId
     * @return
     */
    String checkBatDelete(String[] ids, int companyId);

    /**
     * 员工删除(逻辑删除)
     *
     * @param id
     */
    int logicDelete(int id, int companyId);

    /**
     * 修改
     *
     * @param staffVO
     * @return
     */
    StaffVO update(StaffVO staffVO);

    /**
     * 根据ID获取员工
     *
     * @return
     */
    StaffPO getById(int id, int companyId);

    /**
     * 根据条件查询
     *
     * @param queryMapDTO 查询条件
     * @return
     */
    PageInfo findList(QueryMapDTO queryMapDTO);

    /**
     * 获取员工所在的所有的公司集合
     *
     * @param userName 用户名
     * @param password 密码
     * @return 用户所在公司集合
     */
    List<CompanyPO> getCompanyList(String userName, String password);

    /**
     * 根据用户名及密码和公司Id进行登录
     *
     * @param userName
     * @param password
     * @param companyId
     * @return
     */
    StaffPO loginWithCompanyId(String userName, String password, int companyId);

    /**
     * 心跳更新
     */
    void heartBeatUpdate(int id, int companyId);

    /**
     * 获取小组人员
     */
    List<StaffVO> getGroupStaffs(int companyId, String groupId);

    /**
     * 批量编辑员工信息
     *
     * @param companyId
     * @param staffIds
     * @param roleIds
     * @param password
     * @param groupId
     */
    void batchEditStaff(int companyId, String staffIds, String roleIds, String password, String groupId);

    /**
     * 搜索员工
     *
     * @param companyId
     * @param searchKey
     * @return
     */
    List<StaffVO> getStaffListBySearchKey(int companyId, String searchKey);

    /**
     * 根据员工id获取员工信息及权限信息
     *
     * @param staffId
     * @param companyId
     */
    StaffPermissionVO getStaffPermissionById(int staffId, int companyId);

    /**
     * 根据id获取员工基础信息
     *
     * @param staffId
     * @param companyId
     * @return
     */
    StaffBaseInfoVO getStaffBaseInfo(int staffId, int companyId);

    /**
     * 交接客资
     *
     * @param staffId   交接客服编号
     * @param beStaffId 被转移客资客服编号
     */
    void changeStaff(int staffId, int beStaffId);

    /**
     * 获取可交接邀约客服人员列表
     *
     * @param companyId
     * @return
     */
    List<GroupStaffVO> getChangeList(int companyId);

    /**
     * 根据类型获取小组及人员信息
     * @param companyId
     * @param type
     * @return
     */
    List<GroupStaffVO> getGroupStaffByType(int companyId , String type);
}
