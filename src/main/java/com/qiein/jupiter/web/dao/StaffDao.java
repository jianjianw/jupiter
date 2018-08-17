package com.qiein.jupiter.web.dao;

import java.util.List;

import com.qiein.jupiter.web.entity.dto.DingBindUserDTO;
import com.qiein.jupiter.web.entity.dto.SendDingMsgDTO;
import com.qiein.jupiter.web.entity.dto.StaffTodayInfoDTO;
import com.qiein.jupiter.web.entity.vo.*;
import org.apache.ibatis.annotations.Param;

import com.qiein.jupiter.web.entity.dto.StaffPushDTO;
import com.qiein.jupiter.web.entity.po.CompanyPO;
import com.qiein.jupiter.web.entity.po.StaffDetailPO;
import com.qiein.jupiter.web.entity.po.StaffPO;

/**
 * 员工Dao
 */
public interface StaffDao extends BaseDao<StaffPO> {

    /**
     * 登录
     *
     * @param userName
     * @param password
     * @return
     */
    List<CompanyPO> getCompanyList(@Param("userName") String userName, @Param("password") String password);

    /**
     * 根据公司Id登录
     *
     * @param userName
     * @param password
     * @return
     */
    StaffPO loginWithCompanyId(@Param("userName") String userName, @Param("password") String password,
                               @Param("companyId") int companyId);

    /**
     * 根据手机号获取员工信息
     *
     * @param companyId
     * @param phone
     * @return
     */
    StaffPO getStaffByPhone(@Param("companyId") int companyId, @Param("phone") String phone);

    /**
     * 根据艺名，全名获取员工信息
     *
     * @param companyId
     * @param name
     * @return
     */
    StaffPO getStaffByNames(@Param("companyId") int companyId, @Param("name") String name);

    /**
     * 添加员工
     *
     * @param staffVO
     */
    void addStaffVo(StaffVO staffVO);

    /**
     * 编辑员工信息
     *
     * @param staffVO
     */
    void updateStaff(StaffVO staffVO);

    /**
     * 批量编辑员工密码
     *
     * @param companyId
     * @param staffIdArr
     * @param password
     */
    void batchEditStaffPwd(@Param("companyId") int companyId, @Param("staffIdArr") String[] staffIdArr,
                           @Param("password") String password);

    /**
     * 批量编辑员工状态 显示 锁定 删除
     *
     * @param staffStateVO
     */
    void batUpdateStaffState(@Param("ssv") StaffStateVO staffStateVO, @Param("ids") String[] ids);

    /**
     * 根据id数组批量删除所属公司的员工
     *
     * @param ids
     * @param companyId
     * @return
     */
    void batDelByIdsAndCid(@Param("ids") String[] ids, @Param("companyId") int companyId);

    void checkBatDelete();

    /**
     * 根据小组类型获取小组人员列表
     *
     * @param type
     * @param companyId
     * @return
     */
    List<GroupStaffVO> getListByType(@Param("type") String type, @Param("companyId") int companyId);

    /**
     * 搜索员工
     *
     * @param companyId
     * @param searchKey
     * @return
     */
    List<SearchStaffVO> getStaffListBySearchKey(@Param("companyId") int companyId,
                                                @Param("searchKey") String searchKey);

    /**
     * 根据id 数组批量获取员工信息
     *
     * @return
     */
    List<StaffPO> batchGetByIds(@Param("ids") String[] ids, @Param("companyId") int companyId);

    /**
     * 插入员工详细细信息
     *
     * @return
     */
    int insertStaffDetail(StaffDetailPO staffDetailPO);

    /**
     * 修改员工详细信息
     */
    int updateStaffDetail(StaffDetailPO staffDetailPO);

    /**
     * 删除员工详细信息
     *
     * @param id
     * @param companyId
     * @return
     */
    int deleteStaffDetail(@Param("id") int id, @Param("companyId") int companyId);

    /**
     * 获取员工详细信息
     *
     * @param id
     * @param companyId
     * @return
     */
    StaffDetailVO getStaffDetailVO(@Param("id") int id, @Param("companyId") int companyId);

    /**
     * 更新token
     *
     * @param staffPO
     * @return
     */
    int updateToken(StaffPO staffPO);

    /**
     * 更新在线状态
     *
     * @param staffPO
     * @return
     */
    int updateStatusFlag(StaffPO staffPO);

    /**
     * 更新锁定状态
     *
     * @param staffPO
     * @return
     */
    int updateLockFlag(StaffPO staffPO);

    /**
     * 更新删除标志
     *
     * @param staffPO
     * @return
     */
    int updateDelFlag(StaffPO staffPO);

    /**
     * 更新员工的上线情况 Ip 时间
     *
     * @param staffDetailPO
     * @return
     */
    int updateStaffLoginInfo(StaffDetailPO staffDetailPO);

    /**
     * 更新员工的下线情况 IP 时间
     *
     * @param staffDetailPO
     * @return
     */
    int updateStaffLogoutInfo(StaffDetailPO staffDetailPO);

    /**
     * 批量恢复员工
     *
     * @param companyId
     * @param staffIdArr
     * @param password
     */
    void batchRestoreStaff(@Param("companyId") int companyId, @Param("staffIdArr") String[] staffIdArr,
                           @Param("password") String password);

    /**
     * 搜索离职员工
     */
    List<StaffPO> getDelStaffListBySearchKey(@Param("companyId") int companyId, @Param("searchKey") String searchKey);

    /**
     * 根据客资的渠道ID和拍摄类型ID以及小组ID获取可分配的客服集合
     */
    List<StaffPushDTO> listStaffPushDTOByTypeIdAndChannelId(@Param("companyId") int companyId,
                                                            @Param("groupId") String groupId,
                                                            @Param("channelId") int channelId,
                                                            @Param("typeId") int typeId,
                                                            @Param("interval") int interval);

    /**
     * 获取客服指定时间内渠道和拍摄类型客资的领取情况
     */
    List<StaffPushDTO> listStaffPushDTOByAlloted(@Param("infoTabName") String infoTabName,
                                                 @Param("companyId") int companyId,
                                                 @Param("channelId") int channelId,
                                                 @Param("typeId") int typeId,
                                                 @Param("calcRange") int calcRange,
                                                 @Param("staffList") List<StaffPushDTO> staffList);

    /**
     * 修改员工最后客资推送时间为当前时间
     */
    int updateStaffLastPushTime(@Param("companyId") int companyId, @Param("id") int id);

    /**
     * 根据员工ID，获取小组员工信息
     */
    List<StaffVO> getGroupStaffById(@Param("companyId") int companyId, @Param("staffId") int staffId,
                                    @Param("groupId") String groupId);

    /**
     * 根据员工编号获取员工名片
     *
     * @param staffId
     * @param companyId
     * @return
     */
    StaffDetailVO getStaffCard(@Param("staffId") int staffId, @Param("companyId") int companyId);

    /**
     * 添加到白名单
     *
     * @param ids
     * @return
     */
    void addIpWhite(@Param("ids") String[] ids);

    /**
     * 从白名单删除
     *
     * @param staffId
     * @return
     */
    void delIpWhite(@Param("staffId") int staffId);

    /**
     * 批量从白名单删除
     *
     * @param ids
     * @return
     */
    void delListIpWhite(@Param("ids") List<Integer> ids);

    /**
     * 更新员工心跳时间
     *
     * @param staffId
     * @param companyId
     */
    void updateStaffHeartTime(@Param("staffId") int staffId, @Param("companyId") int companyId);

    /**
     * 绑定微信
     *
     * @param staffDetailVO
     */
    void bindingWeChat(StaffDetailVO staffDetailVO);

    /**
     * 绑定钉钉
     *
     * @param staffDetailVO
     */
    void bindingDing(StaffDetailVO staffDetailVO);

    /**
     * 获取员工今日分配的客资数
     *
     * @param companyId
     * @param staffId
     * @return
     */
    int getTodayKzNum(@Param("companyId") int companyId, @Param("staffId") int staffId,
                      @Param("infoTabName") String infoTabName);

    /**
     * 修改员工今日领取客资数
     *
     * @param companyId
     * @param staffId
     * @param num
     * @return
     */
    int updateTodatKzNum(@Param("companyId") int companyId, @Param("staffId") int staffId, @Param("num") int num);

    /**
     * 计算是否满限
     *
     * @param companyId
     * @param staffId
     * @return
     */
    int checkOverFlowToday(@Param("companyId") int companyId, @Param("staffId") int staffId);

    /**
     * 校验是否有上线后连续三次怠工
     */
    int getSaboteurCheckNum(@Param("logTabName") String logTabName, @Param("companyId") int companyId,
                            @Param("staffId") int staffId, @Param("overTime") int overTime);

    /**
     * 修改员工当前状态
     */
    int editStatusFlagOffLine(@Param("companyId") int companyId, @Param("staffId") int staffId,
                              @Param("statusFlag") int statusFlag);

    /**
     * 查询所选客服集合
     *
     * @param companyId
     * @param staffIds
     * @return
     */
    List<StaffPushDTO> listStaffInstrIds(@Param("companyId") int companyId, @Param("staffIds") String staffIds,
                                         @Param("type") String type);

    /**
     * 查询所选门市所在的门店
     *
     * @param companyId
     * @param staffIds
     * @param type
     * @return
     */
    List<StaffPushDTO> listStaffInstrIdsMsjd(@Param("companyId") int companyId, @Param("staffIds") String staffIds,
                                             @Param("type") String type);

    /**
     * 修改最后上线时间
     *
     * @param id
     * @param companyId
     */
    void updatLastLoginTime(@Param("id") int id, @Param("companyId") int companyId);

    /**
     * 根据ID和企业ID获取推送员工信息
     *
     * @param id
     * @param companyId
     * @return
     */
    StaffPushDTO getPushDTOByCidAndUid(@Param("id") int id, @Param("companyId") int companyId, @Param("type") String type);

    /**
     * 编辑个人消息设置
     *
     * @param staffMsg
     * @return
     */
    int editMsgSet(StaffMsg staffMsg);

    /**
     * 获取微信个人中心展示数据
     *
     * @param companyId
     * @param staffId
     * @return
     */
    StaffTodayInfoDTO getWXStaffInfo(@Param("companyId") Integer companyId, @Param("staffId") Integer staffId);

    /**
     * 重置员工今日接单数
     */
    void resetTodayNum();

    /**
     * 满限变离线
     */
    void resetOffLineWhenLimit();

    /**
     * 小组平均，获取可以领取的小组
     *
     * @param companyId
     * @param sourceId
     * @param role
     * @return
     */
    List<String> getGroupAvgGroupList(@Param("companyId") Integer companyId, @Param("sourceId") int sourceId,
                                      @Param("role") String role);

    /**
     * 获取小组平均，可以领取的客服集合
     */
    List<StaffPushDTO> getGroupAvgAppointList(@Param("companyId") Integer companyId, @Param("role") String role,
                                              @Param("groupId") String groupId, @Param("interval") int interval);

    /**
     * 获取小组平均,小组领取客资情况
     *
     * @param infoTabName
     * @param companyId
     * @param sourceId
     * @param calcRange
     * @param groupIdList
     * @return
     */
    List<String> getGroupAvgReceive(@Param("infoTabName") String infoTabName, @Param("companyId") Integer companyId,
                                    @Param("sourceId") int sourceId, @Param("calcRange") int calcRange,
                                    @Param("groupIdList") List<String> groupIdList);

    /**
     * 获取指定客服，可以领取客资的客服列表
     *
     * @param companyId
     * @param sourceId
     * @return
     */
    List<StaffPushDTO> getAssginAppointList(@Param("companyId") Integer companyId, @Param("sourceId") int sourceId,
                                            @Param("role") String role, @Param("interval") int interval);

    /**
     * 获取渠道指定员工客服
     *
     * @param companyId
     * @param sourceId
     * @return
     */
    List<StaffPushDTO> getPushAppointByRole(@Param("infoTabName") String infoTabName, @Param("companyId") Integer companyId,
                                            @Param("sourceId") int sourceId, @Param("calcRange") int calcRange,
                                            @Param("staffOnlineList") List<StaffPushDTO> staffOnlineList);

    /**
     * 获取客服人员列表，转介绍客服，电商客服，用于分配自由领取
     *
     * @param companyId
     * @param role
     * @return
     */
    List<StaffPushDTO> getYyStaffListByRole(@Param("companyId") Integer companyId, @Param("role") String role);

    /**
     * 获取小组平均，推送客服
     *
     * @param infoTabName
     * @param companyId
     * @param sourceId
     * @param role
     * @return
     */
    StaffPushDTO getPushAppointByGroupAvg(@Param("infoTabName") String infoTabName,
                                          @Param("companyId") Integer companyId,
                                          @Param("sourceId") int sourceId, @Param("role") String role);

    /**
     * 编辑员工所属公司的CorpId
     *
     * @param corpId
     * @param companyId
     * @return
     */
    int editStaffCorpId(@Param("corpId") String corpId, @Param("companyId") Integer companyId);

    /**
     * 功能描述:
     * 根据员工id和所属公司id获取员工的userId和所在公司corpId
     *
     * @auther: Tt(yehuawei)
     * @date:
     * @param:
     * @return:
     */
    SendDingMsgDTO getStaffUserIdAndCorpId(@Param("companyId") int companyId, @Param("staffId") int staffId);

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
     * 获取筛客平均，电商筛选人员
     *
     * @param companyId
     * @param interval
     * @return
     */
    StaffPushDTO getAvgDssxStaff(@Param("companyId") int companyId, @Param("interval") int interval,
                                 @Param("role") String role, @Param("infoTab") String infoTab);

    /**
     * 根据ids批量查找员工
     *
     * @param companyId
     * @return
     */
    List<StaffPO> getByIds(@Param("list") List<Integer> list, @Param("companyId") Integer companyId);

    /**
     * 根据ids批量查找员工
     *
     * @param companyId
     * @return
     */
    List<SearchStaffVO> getGroupById(@Param("list") List<Integer> list, @Param("companyId") Integer companyId);

    /**
     * 根据手机号 加密的密码 公司ID 获取员工信息
     */
    StaffPO getStaffByPhoneMd5PwdAndCid(@Param("phone") String phone,
                                        @Param("password") String password,
                                        @Param("companyId") int companyId);

    /**
     * 批量获取员工姓名
     *
     * @return
     */
    List<String> getStaffNames(@Param("list") List<String> list);

    /**
     * 根据关键字搜索员工信息
     */
    List<SimpleStaffVO> searchStaffByKey(@Param("companyId") int companyId, @Param("key") String key);

}