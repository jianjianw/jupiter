package com.qiein.jupiter.web.service;

import java.util.List;

import com.github.pagehelper.PageInfo;
import com.qiein.jupiter.web.entity.dto.*;
import com.qiein.jupiter.web.entity.po.CompanyPO;
import com.qiein.jupiter.web.entity.po.PermissionPO;
import com.qiein.jupiter.web.entity.po.StaffPO;
import com.qiein.jupiter.web.entity.po.StaffStatusLog;
import com.qiein.jupiter.web.entity.vo.*;
import org.apache.ibatis.annotations.Param;

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
    StaffPO updateStatusFlag(int id, int companyId, int statusFlag, Integer operatorId, String operatorName);

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
     * 交接客资
     *
     * @param staffChangeVO 交接客服基本信息
     */
    void changeStaff(StaffChangeVO staffChangeVO);

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
     * 根据员工ID，获取小组员工信息
     *
     * @param companyId
     * @param staffId
     * @return
     */
    List<StaffVO> getGroupStaffById(int companyId, int staffId, String groupId);

    /**
     * 获取员工上下线日志
     */
    List<StaffStatusLog> getStaffStatusLogById(int companyId, int staffId);

    /**
     * 退出登录
     */
    void logout(int companyId, int staffId, String staffName);

    /**
     * 获取个人名片
     *
     * @param staffId
     * @param companyId
     * @return
     */
    StaffDetailVO getStaffCard(int staffId, int companyId);

    /**
     * 批量添加到白名单
     *
     * @param staffIds
     */
    void addIpWhite(String staffIds);

    /**
     * 从白名单删除
     *
     * @param staffId
     */
    void delIpWhite(int staffId);

    /**
     * 批量从白名单删除
     *
     * @param ids
     */
    void delListIpWhite(List<Integer> ids);

    /**
     * 员工心跳
     */
    boolean heartBeat(StaffPO staffPO, String ip);

    /**
     * 计算员工今日客资数，并校验是否满限状态
     *
     * @param companyId
     * @param staffId
     */
    void resizeTodayNum(int companyId, int staffId);

    /**
     * 绑定微信
     */
    boolean bindingWeChat(int staffId, int companyId, String code);

    /**
     * 绑定钉钉
     */
    boolean bindingDing(int staffId, int companyId, String code);

    /**
     * 获取员工信息-不加缓存
     *
     * @param id
     * @param companyId
     * @return
     */
    StaffPO getByIdWithoutCache(int id, int companyId);

    /**
     * 检查是否绑定成功，如果绑定成功返回微信公众号用户的所有信息
     *
     * @param companyId
     * @param staffId
     */
    WeChatUserDTO checkWXBind(Integer companyId, Integer staffId);

    /**
     * 获取微信个人中心员工数据
     *
     * @param staffId
     * @return
     */
    StaffTodayInfoDTO getWXStaffInfo(Integer companyId, Integer staffId);

    /**
     * 每天晚上初始化员工分客资信息
     */
    void taskClockInit();

    /**
     * 功能描述:
     * 获取未绑定钉钉用户
     *
     * @auther: Tt(yehuawei)
     * @date:
     * @param:
     * @return:
     */
    List<DingBindUserDTO> getNotBindDingUser(Integer companyId);

    /**
     * 根据ids批量查找员工
     *
     * @param ids
     * @param companyId
     * @return
     */
    List<StaffPO> getByIds(String ids, Integer companyId);

    /**
     * 根据ids批量查找员工小组
     *
     * @param ids
     * @param companyId
     * @return
     */
    List<SearchStaffVO> getGroupById(String ids, Integer companyId);

    /**
     * 根据  员工切换公司
     *
     * @return
     */
    StaffPO getStaffByChangeCid(int changeCompanyId, int staffId, int companyId);

    /**
     * 批量获取员工姓名
     *
     * @param staffIds
     * @return
     */
    List<String> getStaffNames(String staffIds);

    /**
     * 根据关键字获取员工的信息
     */
    List<SimpleStaffVO> searchStaffByKey(int companyId, String key);

    /**
     * 获取爆彩消息设置
     *
     * @param companyId
     * @param staffId
     * @return
     */
    StaffMsgSetDTO getMsgSetByStaffId(int companyId, int staffId);

    /**
     * 根据员工姓名获取员工id
     *
     * @param name
     * @return
     */
    Integer getStaffIdByName(String name, Integer companyId);

    /**
     * 获取客资数量
     *
     * @param currentLoginStaff
     */
    Integer getClientCountById(StaffPO currentLoginStaff, Integer staffId);

    /**
     * 公司员工离线
     *
     * @return
     */
    int companyStaffOffLine(int companyId);

    /**
     * 获取所有的电商客服或者转介绍小组员工，按照最后登录时间排序
     */
    List<StaffPushDTO> getWheelStaffList(int companyId, int wheelFlag, String groupType);


    /**
     * 重置客服接单轮单,设置新一批的可以分配的
     */
    int findWheelStaffListAndResetFlag(int companyId, String groupType);

    /**
     * 更新轮单标志
     *
     * @param companyId
     * @param staffId
     * @param flag
     * @return
     */
    int updateStaffWheelFlag(int companyId, int staffId, int flag);


    /**
     * 修改员工消息配置
     */
    int updateStaffMsgSet(int companyId, int staffId, String msgSet);

    /**
     * 修改员工通用配置
     */
    int updateSettings(int companyId, int staffId, String settings);

    /**
     * 重新计算公司每个人今日接单数目
     */
    int updateCompanyTodayNum(int companyId);

}