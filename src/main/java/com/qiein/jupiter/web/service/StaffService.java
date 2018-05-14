package com.qiein.jupiter.web.service;

import com.github.pagehelper.PageInfo;
import com.qiein.jupiter.web.entity.dto.QueryMapDTO;
import com.qiein.jupiter.web.entity.dto.StaffMarsDTO;
import com.qiein.jupiter.web.entity.dto.StaffPasswordDTO;
import com.qiein.jupiter.web.entity.po.CompanyPO;
import com.qiein.jupiter.web.entity.po.PermissionPO;
import com.qiein.jupiter.web.entity.po.StaffPO;
import com.qiein.jupiter.web.entity.vo.*;

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
     * 设置状态
     *
     * @param id
     * @param statusFlag
     */
    StaffPO updateStatusFlag(int id, int companyId, int statusFlag);

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
     * 批量删除员工，将isShow字段改为true，然后硬删除员工对应的角色和员工所属小组
     *
     * @param staffStateVO
     */
    void batDelStaff(StaffStateVO staffStateVO);

    /**
     * 批量编辑员工状态
     *
     * @param staffStateVO
     */
    void batUpdateStaffState(StaffStateVO staffStateVO);

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
     * 修改员工的详细信息
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
    PageInfo<?> findList(QueryMapDTO queryMapDTO);

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
    StaffPO loginWithCompanyId(String userName, String password, int companyId, String ip);

    /**
     * 心跳更新
     */
    void heartBeatUpdate(int id, int companyId);

    /**
     * 获取小组人员
     */
    List<StaffVO> getGroupStaffs(int companyId, String groupId);

    /**
     * 获取小组人员详情
     *
     * @param companyId
     * @param groupId
     * @return
     */
    List<StaffMarsDTO> getGroupStaffsDetail(int companyId, String groupId);

    /**
     * 获取各小组内人员的接单数和在线人数
     *
     * @param companyId
     * @return
     */
    List<GroupsInfoVO> getStaffMarsInfo(int companyId);

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
    List<SearchStaffVO> getStaffListBySearchKey(int companyId, String searchKey);

    /**
     * 根据员工id获取员工信息及权限信息
     *
     * @param staffId
     * @param companyId
     */
    List<PermissionPO> getStaffPermissionById(int staffId, int companyId);

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
     * 校验用户的密码是否正确
     *
     * @param id
     * @param password
     * @param companyId
     * @return
     */
    boolean isRightPassword(int id, String password, int companyId);

    /**
     * 修改员工的基础信息
     *
     * @param staffPO
     * @return
     */
    StaffPO update(StaffDetailVO staffPO);

    /**
     * 更新密码
     *
     * @return
     */
    int updatePassword(StaffPasswordDTO staffPasswordDTO);

    /**
     * 根据类型获取小组及人员信息
     *
     * @param companyId
     * @param type
     * @return
     */
    List<GroupStaffVO> getGroupStaffByType(int companyId, String type);

    /**
     * 获取离职员工列表
     *
     * @param queryMapDTO
     * @return
     */

    PageInfo<?> getDelStaffList(QueryMapDTO queryMapDTO);

    /**
     * 恢复离职员工
     *
     * @param staffVO
     */
    void restoreDelStaff(StaffVO staffVO);

    /**
     * 批量恢复员工
     *
     * @param companyId
     * @param staffIds
     * @param roleIds
     * @param password
     * @param groupId
     */
    void batchRestoreStaff(int companyId, String staffIds, String roleIds, String password, String groupId);

    /**
     * 搜索离职员工
     *
     * @param companyId
     * @param searchKey
     * @return
     */
    List<StaffPO> getDelStaffListBySearchKey(int companyId, String searchKey);

    /**
     * 导出
     *
     * @return
     */
    List<StaffPO> exportStaff();
}
