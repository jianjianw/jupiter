package com.qiein.jupiter.web.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.qiein.jupiter.constant.*;
import com.qiein.jupiter.enums.StaffStatusEnum;
import com.qiein.jupiter.exception.ExceptionEnum;
import com.qiein.jupiter.exception.RException;
import com.qiein.jupiter.msg.goeasy.GoEasyUtil;
import com.qiein.jupiter.msg.goeasy.MessageConts;
import com.qiein.jupiter.msg.websocket.WebSocketMsgUtil;
import com.qiein.jupiter.util.*;
import com.qiein.jupiter.util.wechat.WeChatPushMsgDTO;
import com.qiein.jupiter.util.wechat.WeChatPushUtil;
import com.qiein.jupiter.web.dao.*;
import com.qiein.jupiter.web.entity.dto.ClientGoEasyDTO;
import com.qiein.jupiter.web.entity.dto.ClientPushDTO;
import com.qiein.jupiter.web.entity.dto.GroupKzNumToday;
import com.qiein.jupiter.web.entity.dto.StaffPushDTO;
import com.qiein.jupiter.web.entity.po.*;
import com.qiein.jupiter.web.service.ClientPushService;
import com.qiein.jupiter.web.service.StaffService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sun.awt.image.OffScreenImage;

import javax.management.relation.Role;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 客资推送
 *
 * @author JingChenglong 2018/05/08 10:58
 */
@Service
public class ClientPushServiceImpl implements ClientPushService {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private ClientInfoDao clientInfoDao;
    @Autowired
    private ClientLogDao clientLogDao;
    @Autowired
    private ClientAllotLogDao clientAllotLogDao;
    @Autowired
    private ShopChannelGroupDao shopChannelGroupDao;
    @Autowired
    private GroupKzNumTodayDao groupKzNumTodayDao;
    @Autowired
    private StaffDao staffDao;
    @Autowired
    private StaffStatusLogDao statusLogDao;
    @Autowired
    private NewsDao newsDao;
    @Autowired
    private ClientTimerDao clientTimerDao;
    @Autowired
    private CompanyDao companyDao;

    @Autowired
    private StaffService staffService;

    @Autowired
    private WebSocketMsgUtil webSocketMsgUtil;

    /**
     * 根据拍摄地和渠道维度推送客资
     */
    @Transactional
    @Override
    public synchronized void pushLp(int rule, int companyId, String kzId, int srcType, int overTime, int interval, int srcId) {

        if (NumUtil.haveInvalid(rule, companyId, srcType) || StringUtil.isEmpty(kzId)) {
            return;
        }

//        if (overTime == 0) {
//            overTime = CommonConstant.DEFAULT_OVERTIME;
//        }
//        if (interval == 0) {
//            interval = CommonConstant.DEFAULT_INTERVAL;
//        }

        // 区分渠道类型，电商渠道，转介绍渠道
        String type = "";
        //电商
        if (ChannelConstant.DS_TYPE_LIST.contains(srcType)) {
            type = RoleConstant.DSYY;
        } else if (ChannelConstant.ZJS_TYPE_LIST.contains(srcType)) {
            //转介绍
            type = RoleConstant.ZJSYY;
        }
        // 分配目标客服
        StaffPushDTO appointer = null;
        AllotLogPO allotLog = null;

        // 获取客资信息
        ClientPushDTO clientDTO = clientInfoDao.getClientPushDTOById(kzId, DBSplitUtil.getInfoTabName(companyId),
                DBSplitUtil.getDetailTabName(companyId));

        // 判断客资当前状态-限定客资最后推送时间已经超过分配间隔  //多加5秒防止特殊情况
        if (clientDTO == null || (clientDTO.getPushInterval() != 0 && clientDTO.getPushInterval() <= (overTime + 5))) {
            return;
        }
        // 判定是否已经预选客服
        if (NumUtil.isValid(clientDTO.getAppointorId())
                && clientDTO.getStatusId() == ClientStatusConst.BE_HAVE_MAKE_ORDER) {
            ClientGoEasyDTO infoDTO = clientInfoDao.getClientGoEasyDTOById(clientDTO.getKzId(),
                    DBSplitUtil.getInfoTabName(companyId), DBSplitUtil.getDetailTabName(companyId));
            GoEasyUtil.pushInfoComed(companyId, clientDTO.getAppointorId(), infoDTO, newsDao, staffDao);
            GoEasyUtil.pushInfoRefresh(companyId, clientDTO.getAppointorId(), webSocketMsgUtil);
            return;
        }
        // 限定客资状态为分配中，可领取，未接入
        if (clientDTO.getStatusId() != ClientStatusConst.BE_ALLOTING
                && clientDTO.getStatusId() != ClientStatusConst.BE_WAIT_MAKE_ORDER) {
            return;
        }

        switch (rule) {
//            case ChannelConstant.PUSH_RULE_AVG_ALLOT:
//                // 1：小组+员工-指定承接小组依据权重比自动分配 - <无需领取>
//                appointer = getStaffGroupStaffAvg(companyId, kzId, typeId, channelId, channelTypeId, overTime, interval);
//                if (appointer == null) {
//                    return;
//                }
//                // 生成分配日志
//                allotLog = addAllotLog(kzId, appointer.getStaffId(), appointer.getStaffName(), appointer.getGroupId(),
//                        appointer.getGroupName(), ClientConst.ALLOT_SYSTEM_AUTO, companyId);
//
//                // 客资分配客服
//                doPushAvgAllot(companyId, kzId, appointer, allotLog.getId(), overTime);
//                break;
//            case ChannelConstant.PUSH_RULE_AVG_RECEIVE:
//                // 11：小组+员工-指定承接小组依据权重比自动分配 - <客户端领取>
//                // 校验之前的客服是否连续怠工
//                if (NumUtil.isValid(clientDTO.getAppointorId())) {
//                    checkOffLine(clientDTO.getAppointorId(), companyId, overTime);
//                }
//                appointer = getStaffGroupStaffAvg(companyId, kzId, typeId, channelId, channelTypeId, overTime, interval);
//                if (appointer == null) {
//                    return;
//                }
//                // 生成分配日志
//                allotLog = addAllotLog(kzId, appointer.getStaffId(), appointer.getStaffName(), appointer.getGroupId(),
//                        appointer.getGroupName(), ClientConst.ALLOT_SYSTEM_AUTO, companyId);
//
//                // 客资分配客服
//                doPushAvgReceive(companyId, kzId, appointer, allotLog.getId(), overTime);
//                break;
            case ChannelConstant.PUSH_RULE_SELF:
                // 5：回馈个人-谁录分给谁
                if (NumUtil.isValid(clientDTO.getCollectorId())) {
                    appointer = staffDao.getPushDTOByCidAndUid(clientDTO.getCollectorId(), companyId, type);
                    if (appointer == null) {
                        return;
                    }
                    // 生成分配日志
                    allotLog = addAllotLog(kzId, appointer.getStaffId(), appointer.getStaffName(), appointer.getGroupId(),
                            appointer.getGroupName(), ClientConst.ALLOT_SYSTEM_AUTO, companyId);

                    // 客资分配客服
                    doBlindSelfReceive(companyId, kzId, appointer, allotLog.getId(), overTime);
                    break;
                }
            case ChannelConstant.PUSH_RULE_ASSIGN_APPOINT:
                //12.指定客服
                if (NumUtil.isInValid(srcId)) {
                    return;
                }
                appointer = getAssginAppoint(companyId, srcId, type, interval);
                if (appointer == null) {
                    return;
                }
                // 生成分配日志
                allotLog = addAllotLog(kzId, appointer.getStaffId(), appointer.getStaffName(), appointer.getGroupId(),
                        appointer.getGroupName(), ClientConst.ALLOT_SYSTEM_AUTO, companyId);
                doAssignAppoint(companyId, kzId, appointer, allotLog.getId(), overTime);
                break;
            case ChannelConstant.PUSH_RULE_EVERYONE_CAN_GET:
                //13:自由领取
                List<StaffPushDTO> yyList = staffDao.getYyStaffListByRole(companyId, type, srcId);
                if (CollectionUtils.isEmpty(yyList)) {
                    return;
                }
                //推送给所有邀约人员
                pushAllYyStaff(kzId, companyId, yyList);
                break;
            case ChannelConstant.PUSH_RULE_GROUP_AVG:
                //14.小组平均
                if (NumUtil.isInValid(srcId)) {
                    return;
                }
                appointer = getGroupAvg(companyId, srcId, type, interval);
                if (appointer == null) {
                    return;
                }
                // 生成分配日志
                allotLog = addAllotLog(kzId, appointer.getStaffId(), appointer.getStaffName(), appointer.getGroupId(),
                        appointer.getGroupName(), ClientConst.ALLOT_SYSTEM_AUTO, companyId);
                doAssignAppoint(companyId, kzId, appointer, allotLog.getId(), overTime);
                break;
            case ChannelConstant.PUSH_RULE_GROUP_WHEEL:
                //20 全员轮单
                if (NumUtil.isInValid(srcId)) {
                    return;
                }
                appointer = getGroupWheel(companyId, type);
                if (appointer == null) {
                    return;
                }
                // 生成分配日志
                allotLog = addAllotLog(kzId, appointer.getStaffId(), appointer.getStaffName(), appointer.getGroupId(),
                        appointer.getGroupName(), ClientConst.ALLOT_SYSTEM_AUTO, companyId);
                doAssignAppoint(companyId, kzId, appointer, allotLog.getId(), overTime);
                //重置员工轮单标志为已分配
                staffService.updateStaffWheelFlag(companyId, appointer.getStaffId(), PushRoleConst.WHEEL_FLAG_YES);
                break;
            default:
                break;
        }
    }

    /**
     * 小组 轮单
     *
     * @return
     */
    private StaffPushDTO getGroupWheel(int companyId, String type) {
        //先从队列中找到
        //获取当前可分配的小组
        List<StaffPushDTO> wheelStaffList = staffService.getWheelStaffList(companyId, PushRoleConst.WHEEL_FLAG_WANT, type);
        //如果为空,则重置接单
        if (CollectionUtils.isEmpty(wheelStaffList)) {
            log.info("当前没有可以轮单的人员,重置");
            staffService.findWheelStaffListAndResetFlag(companyId, type);
            return null;
        }

//        log.info("当前轮单人员列表:" + JSONObject.toJSONString(wheelStaffList));
        log.info("当前轮单人数" + wheelStaffList.size());

        StaffPushDTO staffPushDTO = null;
        for (StaffPushDTO pushDTO : wheelStaffList) {
            //如果员工在线
            if (pushDTO.getStatusFlag() == StaffStatusEnum.OnLine.getStatusId()) {
                staffPushDTO = pushDTO;
                break;
            } else {
                log.info(pushDTO.getStaffName() + "不在线，不分了");
            }
        }

        return staffPushDTO;
    }


    /**
     * 获取指定客服，本轮次的客服
     *
     * @return
     */
    private StaffPushDTO getAssginAppoint(int companyId, int srcId, String type, int interval) {
        //1.获取可以领取的客服集合
        List<StaffPushDTO> staffOnlineList = staffDao.getAssginAppointList(companyId, srcId, type, interval);
        if (CollectionUtils.isEmpty(staffOnlineList)) {
            return null;
        }
        int calcRange = companyDao.getAvgDefaultTime(companyId);
        if (NumUtil.isInValid(calcRange)) {
            calcRange = CommonConstant.ALLOT_RANGE_DEFAULT;
        }
        // 2.获取从当前时间往前退一个小时内，所有指定客服的领取情况
        List<StaffPushDTO> appointerList = staffDao.getPushAppointByRole(DBSplitUtil.getInfoTabName(companyId), companyId, srcId, calcRange, staffOnlineList);

        while (calcRange <= CommonConstant.ALLOT_RANGE_MAX
                && (appointerList == null || appointerList.size() != staffOnlineList.size())) {
            calcRange += CommonConstant.ALLOT_RANGE_INTERVAL;
            appointerList = staffDao.getPushAppointByRole(DBSplitUtil.getInfoTabName(companyId), companyId,
                    srcId, calcRange, staffOnlineList);
        }
        // 值匹配，差比分析
        double maxDiffPid = doAppointDiffCalc(staffOnlineList, appointerList);

        // 取出差比分析后差比值最大的小组即为要分配的客服组
        return getCurrentAppointor(staffOnlineList, maxDiffPid);
    }

    /**
     * 小组平均，获取本轮次的客服
     *
     * @return
     */
    private StaffPushDTO getGroupAvg(int companyId, int srcId, String type, int interval) {
        List<String> appointGroups = getGroupAvgGroup(companyId, srcId, type);

        StaffPushDTO appoint = null;
        while (CollectionUtils.isNotEmpty(appointGroups)) {
            appoint = getGroupAvgAppoint(companyId, srcId, type, appointGroups.get(0), interval);
            if (appoint == null) {
                appointGroups.remove(0);
            } else {
                return appoint;
            }
        }
        return null;
    }

    /**
     * 获取小组平均，领取的小组ID
     *
     * @param companyId
     * @param srcId
     * @param type
     * @return
     */
    private List<String> getGroupAvgGroup(int companyId, int srcId, String type) {
        int calcRange = companyDao.getAvgDefaultTime(companyId);
        if (NumUtil.isInValid(calcRange)) {
            calcRange = CommonConstant.ALLOT_RANGE_DEFAULT;
        }
        //1.获取可以领取的小组集合
//        Map<String, Integer> todayTimeInterval = TimeUtil.getTodayTimeInterval();
        List<String> groupIdList = staffDao.getGroupAvgGroupList(companyId, srcId, type);
//        log.info("time:" + JSONObject.toJSONString(todayTimeInterval));
        log.info("可以领取的小组：" + JSONObject.toJSONString(groupIdList));
        //2.获取从当前时间往前退一个小时内，所有指定小组的领取情况
        List<String> appointGroups = staffDao.getGroupAvgReceive(DBSplitUtil.getInfoTabName(companyId), companyId, srcId, calcRange, groupIdList);
        log.info("往前退的小组：" + JSONObject.toJSONString(appointGroups));
        while (calcRange <= CommonConstant.ALLOT_RANGE_MAX
                && (appointGroups == null || appointGroups.size() != groupIdList.size())) {
            calcRange += CommonConstant.ALLOT_RANGE_INTERVAL;
            appointGroups = staffDao.getGroupAvgReceive(DBSplitUtil.getInfoTabName(companyId), companyId, srcId, calcRange, groupIdList);
        }
        if (CollectionUtils.isEmpty(appointGroups)) {
            return groupIdList;
        }
        List<String> result = new LinkedList<>();
        //小组排序
        for (String appointGrp : appointGroups) {
            Iterator<String> it = groupIdList.iterator();
            while (it.hasNext()) {
                String groupId = it.next();
                if (appointGrp.equals(groupId)) {
                    result.add(appointGrp);
                    it.remove();
                }
            }
        }
        if (CollectionUtils.isNotEmpty(groupIdList)) {
            result.addAll(0, groupIdList);
        }
        return result;
    }

    /**
     * 小组平均，获取指定小组的可以领取的客服
     *
     * @param companyId
     * @param srcId
     * @param type
     * @param groupId
     * @return
     */
    private StaffPushDTO getGroupAvgAppoint(int companyId, int srcId, String type, String groupId, int interval) {
        int calcRange = companyDao.getAvgDefaultTime(companyId);
        if (NumUtil.isInValid(calcRange)) {
            calcRange = CommonConstant.ALLOT_RANGE_DEFAULT;
        }
        //1.获取可以领取的客服集合
        List<StaffPushDTO> staffOnlineList = staffDao.getGroupAvgAppointList(companyId, type, groupId, interval, srcId);
        if (CollectionUtils.isEmpty(staffOnlineList)) {
            return null;
        }
        // 2.获取从当前时间往前退一个小时内，所有指定客服的领取情况
        List<StaffPushDTO> appointerList = staffDao.getPushAppointByRole(DBSplitUtil.getInfoTabName(companyId), companyId, srcId, calcRange, staffOnlineList);
        while (calcRange <= CommonConstant.ALLOT_RANGE_MAX
                && (appointerList == null || appointerList.size() != staffOnlineList.size())) {
            calcRange += CommonConstant.ALLOT_RANGE_INTERVAL;
            appointerList = staffDao.getPushAppointByRole(DBSplitUtil.getInfoTabName(companyId), companyId,
                    srcId, calcRange, staffOnlineList);
        }
        // 值匹配，差比分析
        double maxDiffPid = doAppointDiffCalc(staffOnlineList, appointerList);

        // 取出差比分析后差比值最大的小组即为要分配的客服组
        return getCurrentAppointor(staffOnlineList, maxDiffPid);
    }

    /**
     * 连续怠工三次自动下线
     *
     * @param appointId
     * @param companyId
     * @param overTime
     */
    private void checkOffLine(int appointId, int companyId, int overTime) {
        int checkNum = staffDao.getSaboteurCheckNum(DBSplitUtil.getAllotLogTabName(companyId), companyId, appointId,
                overTime);
        if (0 == checkNum) {
            // 连续三次怠工，强制下线
            int i = staffDao.editStatusFlagOffLine(companyId, appointId, StaffStatusEnum.OffLine.getStatusId());
            if (1 == i) {
                // 记录下线日志
                statusLogDao.insert(new StaffStatusLog(appointId, StaffStatusEnum.OffLine.getStatusId(),
                        CommonConstant.SYSTEM_OPERA_ID, CommonConstant.SYSTEM_OPERA_NAME, companyId,
                        ClientLogConst.CONTINUOUS_SABOTEUR_DONW));
                // 推送状态重载消息
                GoEasyUtil.pushStatusRefresh(companyId, appointId, webSocketMsgUtil);
                // 推送连续三次怠工下线消息
                GoEasyUtil.pushOffLineAuto(companyId, appointId, newsDao, staffDao);
            }
        }
    }

    /**
     * 客资依据权重设置平均分配
     *
     * @param companyId
     * @param kzId
     * @param appointer
     * @param allotLogId
     * @param overTime
     */
    private void doPushAvgAllot(int companyId, String kzId, StaffPushDTO appointer, int allotLogId, int overTime) {

        // 修改客资信息
        updateInfoWhenReceive(companyId, kzId, allotLogId, appointer);

        // 重载客服今日领取客资数
        staffService.resizeTodayNum(companyId, appointer.getStaffId());

        // 推送消息
        ClientGoEasyDTO infoDTO = clientInfoDao.getClientGoEasyDTOById(kzId, DBSplitUtil.getInfoTabName(companyId),
                DBSplitUtil.getDetailTabName(companyId));
        GoEasyUtil.pushInfoComed(companyId, appointer.getStaffId(), infoDTO, newsDao, staffDao);
        GoEasyUtil.pushInfoRefresh(companyId, appointer.getStaffId(), webSocketMsgUtil);
    }

    /**
     * 客资领取时修改客资信息
     *
     * @param companyId
     * @param kzId
     * @param allotLogId
     * @param appointer
     */
    private void updateInfoWhenReceive(int companyId, String kzId, int allotLogId, StaffPushDTO appointer) {
        // 客资绑定客服，修改客资状态，客资客服ID，客服名，客资分类，客资客服组信息，最后操作时间，客资最后推送时间
        int updateRstNum = clientInfoDao.updateClientInfoWhenAllot(companyId, DBSplitUtil.getInfoTabName(companyId),
                kzId, ClientStatusConst.KZ_CLASS_NEW, ClientStatusConst.BE_HAVE_MAKE_ORDER, appointer.getStaffId(),
                appointer.getGroupId(), ClientConst.ALLOT_SYSTEM_AUTO);
        if (1 != updateRstNum) {
            throw new RException(ExceptionEnum.INFO_STATUS_EDIT_ERROR);
        }

        updateRstNum = clientInfoDao.updateClientDetailWhenAllot(companyId, DBSplitUtil.getDetailTabName(companyId),
                kzId, appointer.getStaffName(), appointer.getGroupName());
        if (1 != updateRstNum) {
            throw new RException(ExceptionEnum.INFO_EDIT_ERROR);
        }

        // 修改指定分配日志状态为已领取
        updateRstNum = clientAllotLogDao.updateAllogLog(DBSplitUtil.getAllotLogTabName(companyId), companyId, kzId,
                allotLogId, ClientConst.ALLOT_LOG_STATUS_YES, "now");
        if (1 != updateRstNum) {
            throw new RException(ExceptionEnum.INFO_STATUS_EDIT_ERROR);
        }

        // 修改客资的领取时间
        updateRstNum = clientInfoDao.updateClientInfoAfterAllot(companyId, DBSplitUtil.getInfoTabName(companyId), kzId);
        if (1 != updateRstNum) {
            throw new RException(ExceptionEnum.INFO_EDIT_ERROR);
        }

        // 修改客服最后推送时间
        updateRstNum = staffDao.updateStaffLastPushTime(companyId, appointer.getStaffId());
        if (1 != updateRstNum) {
            throw new RException(ExceptionEnum.STAFF_EDIT_ERROR);
        }

        // 客资日志记录
        updateRstNum = clientLogDao.addInfoLog(DBSplitUtil.getInfoLogTabName(companyId),
                new ClientLogPO(kzId,
                        ClientLogConst.getAutoAllotLog(appointer.getGroupName(), appointer.getStaffName()),
                        ClientLogConst.INFO_LOGTYPE_ALLOT, companyId));
        if (1 != updateRstNum) {
            throw new RException(ExceptionEnum.LOG_ERROR);
        }
    }

    /**
     * 客资依据权重设置平均分配--领取
     *
     * @param companyId
     * @param kzId
     * @param appointer
     * @param allotLogId
     * @param overTime
     */
    private void doPushAvgReceive(int companyId, String kzId, StaffPushDTO appointer, int allotLogId, int overTime) {

        // 客资绑定客服，修改客资状态，客资客服ID，客服名，客资分类，客资客服组信息，最后操作时间，客资最后推送时间
        int updateRstNum = clientInfoDao.updateClientInfoWhenAllot(companyId, DBSplitUtil.getInfoTabName(companyId),
                kzId, ClientStatusConst.KZ_CLASS_NEW, ClientStatusConst.BE_ALLOTING, appointer.getStaffId(),
                appointer.getGroupId(), ClientConst.ALLOT_SYSTEM_AUTO);
        if (1 != updateRstNum) {
            throw new RException(ExceptionEnum.INFO_STATUS_EDIT_ERROR);
        }

        updateRstNum = clientInfoDao.updateClientDetailWhenAllot(companyId, DBSplitUtil.getDetailTabName(companyId),
                kzId, appointer.getStaffName(), appointer.getGroupName());
        if (1 != updateRstNum) {
            throw new RException(ExceptionEnum.INFO_EDIT_ERROR);
        }

        // 修改客服最后推送时间
        updateRstNum = staffDao.updateStaffLastPushTime(companyId, appointer.getStaffId());
        if (1 != updateRstNum) {
            throw new RException(ExceptionEnum.STAFF_EDIT_ERROR);
        }

        // 客资日志记录
        updateRstNum = clientLogDao.addInfoLog(DBSplitUtil.getInfoLogTabName(companyId),
                new ClientLogPO(kzId,
                        ClientLogConst.getAutoReceiveLog(appointer.getGroupName(), appointer.getStaffName()),
                        ClientLogConst.INFO_LOGTYPE_ALLOT, companyId));
        if (1 != updateRstNum) {
            throw new RException(ExceptionEnum.LOG_ERROR);
        }


        // 推送消息
        GoEasyUtil.pushAppInfoReceive(companyId, appointer.getStaffId(), 1, kzId, String.valueOf(allotLogId), overTime);
    }

    /**
     * 客资绑定客服为自己
     *
     * @param companyId
     * @param kzId
     * @param appointer
     * @param allotLogId
     * @param overTime
     */
    private void doBlindSelfReceive(int companyId, String kzId, StaffPushDTO appointer, int allotLogId, int overTime) {

        // 客资绑定客服，修改客资状态，客资客服ID，客服名，客资分类，客资客服组信息，最后操作时间，客资最后推送时间
        int updateRstNum = clientInfoDao.updateClientInfoWhenAllot(companyId, DBSplitUtil.getInfoTabName(companyId),
                kzId, ClientStatusConst.KZ_CLASS_NEW, ClientStatusConst.BE_HAVE_MAKE_ORDER, appointer.getStaffId(),
                appointer.getGroupId(), ClientConst.ALLOT_SYSTEM_AUTO);
        if (1 != updateRstNum) {
            throw new RException(ExceptionEnum.INFO_STATUS_EDIT_ERROR);
        }

        updateRstNum = clientInfoDao.updateClientDetailWhenAllot(companyId, DBSplitUtil.getDetailTabName(companyId),
                kzId, appointer.getStaffName(), appointer.getGroupName());
        if (1 != updateRstNum) {
            throw new RException(ExceptionEnum.INFO_EDIT_ERROR);
        }

        // 修改指定分配日志状态为已领取
        updateRstNum = clientAllotLogDao.updateAllogLog(DBSplitUtil.getAllotLogTabName(companyId), companyId, kzId,
                allotLogId, ClientConst.ALLOT_LOG_STATUS_YES, "now");
        if (1 != updateRstNum) {
            throw new RException(ExceptionEnum.INFO_STATUS_EDIT_ERROR);
        }

        // 修改客资的领取时间
        updateRstNum = clientInfoDao.updateClientInfoAfterAllot(companyId, DBSplitUtil.getInfoTabName(companyId), kzId);
        if (1 != updateRstNum) {
            throw new RException(ExceptionEnum.INFO_EDIT_ERROR);
        }

        // 推送消息
        ClientGoEasyDTO infoDTO = clientInfoDao.getClientGoEasyDTOById(kzId, DBSplitUtil.getInfoTabName(companyId),
                DBSplitUtil.getDetailTabName(companyId));
        GoEasyUtil.pushInfoComed(companyId, appointer.getStaffId(), infoDTO, newsDao, staffDao);
        GoEasyUtil.pushInfoRefresh(companyId, appointer.getStaffId(), webSocketMsgUtil);

        // 客资日志记录
        updateRstNum = clientLogDao.addInfoLog(DBSplitUtil.getInfoLogTabName(companyId),
                new ClientLogPO(kzId,
                        ClientLogConst.getAutoAllotLog(appointer.getGroupName(), appointer.getStaffName()),
                        ClientLogConst.INFO_LOGTYPE_ALLOT, companyId));
        if (1 != updateRstNum) {
            throw new RException(ExceptionEnum.LOG_ERROR);
        }
    }


    /**
     * 客资指定客服
     *
     * @param companyId
     * @param kzId
     * @param appointer
     * @param allotLogId
     * @param overTime
     */
    private void doAssignAppoint(int companyId, String kzId, StaffPushDTO appointer, int allotLogId, int overTime) {

        // 客资绑定客服，修改客资状态，客资客服ID，客服名，客资分类，客资客服组信息，最后操作时间，客资最后推送时间
        int updateRstNum = clientInfoDao.updateClientInfoWhenAllot(companyId, DBSplitUtil.getInfoTabName(companyId),
                kzId, ClientStatusConst.KZ_CLASS_NEW, ClientStatusConst.BE_HAVE_MAKE_ORDER, appointer.getStaffId(),
                appointer.getGroupId(), ClientConst.ALLOT_SYSTEM_AUTO);
        if (1 != updateRstNum) {
            throw new RException(ExceptionEnum.INFO_STATUS_EDIT_ERROR);
        }
        //修改客资领取时间
        clientInfoDao.updateClientInfoAfterAllot(companyId, DBSplitUtil.getInfoTabName(companyId), kzId);

        updateRstNum = clientInfoDao.updateClientDetailWhenAllot(companyId, DBSplitUtil.getDetailTabName(companyId),
                kzId, appointer.getStaffName(), appointer.getGroupName());
        if (1 != updateRstNum) {
            throw new RException(ExceptionEnum.INFO_EDIT_ERROR);
        }

        // 修改指定分配日志状态为已领取
        updateRstNum = clientAllotLogDao.updateAllogLog(DBSplitUtil.getAllotLogTabName(companyId), companyId, kzId,
                allotLogId, ClientConst.ALLOT_LOG_STATUS_YES, "now");
        if (1 != updateRstNum) {
            throw new RException(ExceptionEnum.INFO_STATUS_EDIT_ERROR);
        }
        //修改员工最后推送时间
        staffDao.updateStaffLastPushTime(companyId, appointer.getStaffId());
        // 重载客服今日领取客资数
        staffService.resizeTodayNum(companyId, appointer.getStaffId());

        try {
            // 推送消息
            ClientGoEasyDTO infoDTO = clientInfoDao.getClientGoEasyDTOById(kzId, DBSplitUtil.getInfoTabName(companyId),
                    DBSplitUtil.getDetailTabName(companyId));
            GoEasyUtil.pushInfoComed(companyId, appointer.getStaffId(), infoDTO, newsDao, staffDao);
            GoEasyUtil.pushInfoRefresh(companyId, appointer.getStaffId(), webSocketMsgUtil);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 客资日志记录
        updateRstNum = clientLogDao.addInfoLog(DBSplitUtil.getInfoLogTabName(companyId),
                new ClientLogPO(kzId,
                        ClientLogConst.getAutoAllotLog(appointer.getGroupName(), appointer.getStaffName()),
                        ClientLogConst.INFO_LOGTYPE_ALLOT, companyId));
        if (1 != updateRstNum) {
            throw new RException(ExceptionEnum.LOG_ERROR);
        }
    }

    /**
     * 自由领取，推送给所有邀约人员
     *
     * @param kzId
     * @param companyId
     * @param yyList
     */
    private void pushAllYyStaff(String kzId, int companyId, List<StaffPushDTO> yyList) {
        // 获取客资信息
        ClientGoEasyDTO infoDTO = clientInfoDao.getClientGoEasyDTOById(kzId, DBSplitUtil.getInfoTabName(companyId),
                DBSplitUtil.getDetailTabName(companyId));
        for (StaffPushDTO sf : yyList) {
            GoEasyUtil.pushClientReceive(companyId, sf.getStaffId(), infoDTO, newsDao, staffDao, webSocketMsgUtil);
        }
    }

    /**
     * 生成客资分配日志
     *
     * @param kzId
     * @param staffId
     * @param staffName
     * @param groupId
     * @param groupName
     * @param allotType
     * @param companyId
     * @return
     */
    private AllotLogPO addAllotLog(String kzId, int staffId, String staffName, String groupId, String groupName,
                                   int allotType, int companyId) {

        // 生成分配日志
        AllotLogPO allotLog = new AllotLogPO(kzId, staffId, staffName, groupId, groupName, allotType, companyId,
                CommonConstant.SYSTEM_OPERA_ID, CommonConstant.SYSTEM_OPERA_NAME);

        // 记录分配日志
        clientAllotLogDao.addClientAllogLog(DBSplitUtil.getAllotLogTabName(companyId), allotLog);

        if (NumUtil.isInValid(allotLog.getId())) {
            throw new RException(ExceptionEnum.ALLOT_LOG_ERROR);
        }

        return allotLog;
    }

    /**
     * 小组权重设置分配规则拣选客服
     *
     * @param companyId
     * @param kzId
     * @param typeId
     * @param channelId
     * @param channelTypeId
     * @param overTime
     * @param interval
     * @return
     */
    public StaffPushDTO getStaffGroupStaffAvg(int companyId, String kzId, int typeId, int channelId, int channelTypeId,
                                              int overTime, int interval) {

        // 根据拍摄地ID，渠道ID获取要分配的小组ID集合
        List<ShopChannelGroupPO> shopChannelGroupRelaList = shopChannelGroupDao.listShopChannelGroupRela(companyId,
                typeId, channelId, DBSplitUtil.getInfoTabName(companyId));

        if (CollectionUtils.isEmpty(shopChannelGroupRelaList)) {
            return null;
        }

        // 统计当天该拍摄地和渠道，每个小组的客资分配情况
        List<GroupKzNumToday> groupKzNumTodayList = groupKzNumTodayDao.getGroupKzNumTodayByShopChannelId(typeId,
                channelId, companyId, DBSplitUtil.getInfoTabName(companyId));

        // 要分配的目标客服
        StaffPushDTO appointor = null;

        // 值匹配，差比分析
        double maxDiffPid = doGroupDiffCalc(shopChannelGroupRelaList, groupKzNumTodayList);

        while (CollectionUtils.isNotEmpty(shopChannelGroupRelaList)) {

            if (maxDiffPid == Double.MAX_VALUE) {
                maxDiffPid = doGroupDiffCalc(shopChannelGroupRelaList);
            }

            // 取出差比分析后,差比值最大的小组即为要分配的客服组
            ShopChannelGroupPO thisGroup = getCurrentGroup(shopChannelGroupRelaList, maxDiffPid);

            // 从当前组找出要可以领取该渠道和拍摄地客资的客服
            appointor = getAppointorByAllotRule(thisGroup.getGroupId(), companyId, kzId, typeId, channelId,
                    channelTypeId, interval);

            // 找不到分配客服就移除改组的分配拣选，重新选组
            if (appointor == null) {
                shopChannelGroupRelaList.remove(thisGroup);
                // 差比重置
                maxDiffPid = Double.MAX_VALUE;
            } else {
                appointor.setGroupId(thisGroup.getGroupId());
                appointor.setGroupName(thisGroup.getGroupName());
                return appointor;
            }
        }
        return appointor;
    }

    /**
     * 从当前组找出要可以领取该渠道和拍摄地客资的客服
     *
     * @param groupId
     * @param companyId
     * @param kzId
     * @param typeId
     * @param channelId
     * @param channelTypeId
     * @param interval
     * @return
     */
    private StaffPushDTO getAppointorByAllotRule(String groupId, int companyId, String kzId, int typeId, int channelId,
                                                 int channelTypeId, int interval) {

        // 获取可以领取该渠道和拍摄地的在线的客服集合
        List<StaffPushDTO> staffOnlineList = staffDao.listStaffPushDTOByTypeIdAndChannelId(companyId, groupId,
                channelId, typeId, interval);

        if (CollectionUtils.isEmpty(staffOnlineList)) {
            return null;
        }

        int calcRange = companyDao.getAvgDefaultTime(companyId);
        if (NumUtil.isInValid(calcRange)) {
            calcRange = CommonConstant.ALLOT_RANGE_DEFAULT;
        }
        // 获取从当前时间往前退一个小时内所有客服对该渠道和拍摄地的客资的领取情况
        List<StaffPushDTO> staffAllotList = staffDao.listStaffPushDTOByAlloted(DBSplitUtil.getInfoTabName(companyId),
                companyId, channelId, typeId, calcRange, staffOnlineList);

        while (calcRange <= CommonConstant.ALLOT_RANGE_MAX
                && (staffAllotList == null || staffAllotList.size() != staffOnlineList.size())) {
            calcRange += CommonConstant.ALLOT_RANGE_INTERVAL;
            staffAllotList = staffDao.listStaffPushDTOByAlloted(DBSplitUtil.getInfoTabName(companyId), companyId,
                    channelId, typeId, calcRange, staffOnlineList);
        }

        // 值匹配，差比分析
        double maxDiffPid = doAppointDiffCalc(staffOnlineList, staffAllotList);

        // 取出差比分析后差比值最大的小组即为要分配的客服组
        return getCurrentAppointor(staffOnlineList, maxDiffPid);
    }

    /**
     * 取出差比分析后差比值最大的小组即为要分配的客服组
     *
     * @param shopChannelGroupRelaList
     * @param maxDiffPid
     * @return
     */
    private ShopChannelGroupPO getCurrentGroup(List<ShopChannelGroupPO> shopChannelGroupRelaList, double maxDiffPid) {

        for (ShopChannelGroupPO thisGroup : shopChannelGroupRelaList) {
            if (thisGroup.getDiffPid() == maxDiffPid) {
                return thisGroup;
            }
        }

        return null;
    }

    /**
     * 取出差比分析后差比值最大的客服即为要分配的客服
     *
     * @param staffOnlineList
     * @param maxDiffPid
     * @return
     */
    private StaffPushDTO getCurrentAppointor(List<StaffPushDTO> staffOnlineList, double maxDiffPid) {

        for (StaffPushDTO thisAppointor : staffOnlineList) {
            if (thisAppointor.getDiffPid() == maxDiffPid) {
                return thisAppointor;
            }
        }

        return null;
    }

    /**
     * 客服值匹配，差比分析
     *
     * @param staffOnlineList
     * @param staffAllotList
     * @return
     */
    private double doAppointDiffCalc(List<StaffPushDTO> staffOnlineList, List<StaffPushDTO> staffAllotList) {

        if (CollectionUtils.isEmpty(staffAllotList)) {
            return Double.MAX_VALUE;
        }

        double maxDiffPid = -Double.MAX_VALUE;

        // 值匹配
        for (StaffPushDTO appointor : staffOnlineList) {
            for (StaffPushDTO todayNum : staffAllotList) {
                if (appointor.getStaffId() == todayNum.getStaffId()) {
                    appointor.setTodayNum(todayNum.getTodayNum());
                    appointor.doCalculateAllotNumDiffPID();
                    break;
                }
            }
            if (appointor.getDiffPid() > maxDiffPid) {
                maxDiffPid = appointor.getDiffPid();
            }
        }

        return maxDiffPid;
    }

    /**
     * 客服组值匹配，差比分析
     *
     * @param shopChannelGroupRelaList
     * @param groupKzNumTodayList
     */
    private double doGroupDiffCalc(List<ShopChannelGroupPO> shopChannelGroupRelaList,
                                   List<GroupKzNumToday> groupKzNumTodayList) {

        if (CollectionUtils.isEmpty(groupKzNumTodayList)) {
            return Double.MAX_VALUE;
        }

        double maxDiffPid = -Double.MAX_VALUE;

        // 值匹配
        for (ShopChannelGroupPO rela : shopChannelGroupRelaList) {
            for (GroupKzNumToday todayNum : groupKzNumTodayList) {
                if (rela.getGroupId().equals(todayNum.getGroupId())) {
                    rela.setTodayNum(todayNum.getKzNum());
                    rela.doCalculateAllotNumDiffPID();
                    break;
                }
            }
            if (rela.getDiffPid() > maxDiffPid) {
                maxDiffPid = rela.getDiffPid();
            }
        }

        return maxDiffPid;
    }

    /**
     * 取差比最大值
     *
     * @param shopChannelGroupRelaList
     * @return
     */
    private double doGroupDiffCalc(List<ShopChannelGroupPO> shopChannelGroupRelaList) {

        double maxDiffPid = -Double.MAX_VALUE;

        // 值匹配
        for (ShopChannelGroupPO rela : shopChannelGroupRelaList) {
            if (rela.getDiffPid() > maxDiffPid) {
                maxDiffPid = rela.getDiffPid();
            }
        }

        return maxDiffPid;
    }

    /**
     * 获取需要推送的客资信息
     *
     * @param companyId
     * @return
     */
    public List<ClientPushDTO> getInfoListBeReadyPush(int companyId, int overTime) {
        try {
            return clientInfoDao.getInfoListBeReadyPush(DBSplitUtil.getInfoTabName(companyId), companyId, overTime);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取企业需要分配的筛选中的客资列表
     *
     * @param companyId
     * @return
     */
    public List<ClientPushDTO> getSkInfoList(int companyId, int overTime) {
        try {
            return clientInfoDao.getSkInfoList(DBSplitUtil.getInfoTabName(companyId), companyId, overTime);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 客资批量分配给客服
     */
    @Override
    public void pushLp(String kzIds, String staffIds, int companyId, int operaId, String operaName, String role) {
        if (StringUtil.isEmpty(kzIds) || StringUtil.isEmpty(staffIds) || NumUtil.isInValid(companyId)) {
            throw new RException(ExceptionEnum.ALLOT_ERROR);
        }
        // 查询所选客资里面没有分出去的客资
        List<ClientPushDTO> infoList = clientInfoDao.listClientsInStrKzids(kzIds, companyId,
                DBSplitUtil.getInfoTabName(companyId));
        if (CollectionUtils.isEmpty(infoList)) {
            throw new RException(ExceptionEnum.ALLOTED_ERROR);
        }
        int kzNum = infoList.size();
        // 查询所选客服集合
        List<StaffPushDTO> staffList = staffDao.listStaffInstrIds(companyId, staffIds, role);
        if (staffList == null || staffList.size() == 0) {
            throw new RException(ExceptionEnum.APPOINTOR_ERROR);
        }
        while (infoList.size() != 0) {
            for (StaffPushDTO staff : staffList) {
                if (infoList.size() > 0) {
                    // 客资修改最后消息推送时间为当前系统时间，绑定客服，修改状态为分配中
                    int updateRstNum = clientInfoDao.updateClientInfoWhenAllot(companyId,
                            DBSplitUtil.getInfoTabName(companyId), infoList.get(0).getKzId(),
                            ClientStatusConst.KZ_CLASS_NEW, ClientStatusConst.BE_HAVE_MAKE_ORDER, staff.getStaffId(),
                            staff.getGroupId(), ClientConst.ALLOT_HANDLER);
                    if (1 != updateRstNum) {
                        throw new RException(ExceptionEnum.INFO_STATUS_EDIT_ERROR);
                    }
                    // 客资修改客资的客服组ID，和客服组名称
                    clientInfoDao.updateClientDetailWhenAllot(companyId, DBSplitUtil.getDetailTabName(companyId),
                            infoList.get(0).getKzId(), staff.getStaffName(), staff.getGroupName());
                    staff.doAddKzIdsWill(infoList.get(0).getKzId());
                    infoList.remove(0);
                } else {
                    break;
                }
            }
        }
        if (kzNum < staffList.size()) {
            for (int i = 0; i < kzNum; i++) {
                push(companyId, staffList.get(i).getWillHaveKzidsStrBf(), staffList.get(i), operaId, operaName);
            }
        } else {
            for (StaffPushDTO staff : staffList) {
                push(companyId, staff.getWillHaveKzidsStrBf(), staff, operaId, operaName);
            }
        }
    }

    /**
     * 客资批量分配给门市
     *
     * @param kzIds
     * @param staffIds
     * @param companyId
     */
    public void allotToMsjd(String kzIds, String staffIds, int companyId, int operaId, String operaName) {
        if (StringUtil.isEmpty(kzIds) || StringUtil.isEmpty(staffIds) || NumUtil.isInValid(companyId)) {
            throw new RException(ExceptionEnum.ALLOT_ERROR);
        }
        // 查询所选客资里面没有分出去的客资
        List<ClientPushDTO> infoList = clientInfoDao.listClientsInStrKzids4Msjd(kzIds, companyId,
                DBSplitUtil.getInfoTabName(companyId));
        if (CollectionUtils.isEmpty(infoList)) {
            throw new RException(ExceptionEnum.ALLOTED_ERROR);
        }
        int kzNum = infoList.size();
        // 查询所选门市集合
        List<StaffPushDTO> staffList = staffDao.listStaffInstrIdsMsjd(companyId, staffIds, RoleConstant.MSJD);
        if (staffList == null || staffList.size() == 0) {
            throw new RException(ExceptionEnum.APPOINTOR_ERROR);
        }
        while (infoList.size() != 0) {
            for (StaffPushDTO staff : staffList) {
                if (infoList.size() > 0) {
                    ClientPushDTO info = infoList.get(0);
                    // 客资修改最后消息推送时间为当前系统时间，绑定客服，修改状态为分配中
                    int updateRstNum = clientInfoDao.updateClientInfoWhenAllotMsjd(companyId,
                            DBSplitUtil.getInfoTabName(companyId), info.getKzId(),
                            ClientStatusConst.BE_COMFIRM == info.getStatusId() ? ClientStatusConst.BE_HAVE_RECEPTOR : info.getStatusId(), staff.getShopId(), staff.getStaffId(), ClientConst.ALLOT_HANDLER);
                    if (1 != updateRstNum) {
                        throw new RException(ExceptionEnum.INFO_STATUS_EDIT_ERROR);
                    }
                    // 客资修改客资的接待人姓名，门店名
                    clientInfoDao.updateClientDetailWhenAllotMsjd(companyId, DBSplitUtil.getDetailTabName(companyId),
                            infoList.get(0).getKzId(), staff.getShopName(), staff.getStaffName());
                    staff.doAddKzIdsWill(infoList.get(0).getKzId());
                    infoList.remove(0);
                } else {
                    break;
                }
            }
        }
        if (kzNum < staffList.size()) {
            for (int i = 0; i < kzNum; i++) {
                pushMsjd(companyId, staffList.get(i).getWillHaveKzidsStrBf(), staffList.get(i), operaId, operaName);
            }
        } else {
            for (StaffPushDTO staff : staffList) {
                pushMsjd(companyId, staff.getWillHaveKzidsStrBf(), staff, operaId, operaName);
            }
        }
    }

    public void push(int companyId, String kzIds, StaffPushDTO appoint, int operaId, String operaName) {
        // 根据每个客资生成对应的分配日志
        String[] kzIdsArr = kzIds.split(",");
        String[] allogIdsArr = new String[kzIdsArr.length];
        for (int i = 0; i < kzIdsArr.length; i++) {
            // 生成分配日志,已领取
            AllotLogPO allotLog = new AllotLogPO(kzIdsArr[i], appoint.getStaffId(), appoint.getStaffName(),
                    appoint.getGroupId(), appoint.getGroupName(), ClientConst.ALLOT_HANDLER, companyId, operaId,
                    operaName, ClientConst.ALLOT_LOG_STATUS_YES);

            // 记录分配日志
            clientAllotLogDao.addClientAllogLog(DBSplitUtil.getAllotLogTabName(companyId), allotLog);

            // 客资日志记录
            clientLogDao
                    .addInfoLog(DBSplitUtil.getInfoLogTabName(companyId),
                            new ClientLogPO(
                                    kzIdsArr[i], operaId, operaName, ClientLogConst.getAllotLog(appoint.getGroupName(),
                                    appoint.getStaffName()),
                                    ClientLogConst.INFO_LOGTYPE_ALLOT, companyId));
        }
        // 推送消息
        GoEasyUtil.pushAllotMsg(companyId, appoint.getStaffId(), kzIdsArr, newsDao, staffDao, clientInfoDao);
    }

    private void pushMsjd(int companyId, String kzIds, StaffPushDTO appoint, int operaId, String operaName) {
        // 根据每个客资生成对应的分配日志
        String[] kzIdsArr = kzIds.split(",");
        String[] allogIdsArr = new String[kzIdsArr.length];
        for (int i = 0; i < kzIdsArr.length; i++) {
            // 客资日志记录
            clientLogDao
                    .addInfoLog(DBSplitUtil.getInfoLogTabName(companyId),
                            new ClientLogPO(
                                    kzIdsArr[i], operaId, operaName, ClientLogConst.getAllotLogMsjd(appoint.getShopName(),
                                    appoint.getStaffName()),
                                    ClientLogConst.INFO_LOGTYPE_ALLOT, companyId));
        }
        // 推送消息
        GoEasyUtil.pushAllotMsg(companyId, appoint.getStaffId(), kzIdsArr, newsDao, staffDao, clientInfoDao);
    }

    public static String arrToStr(String[] arr) {
        if (arr == null || arr.length == 0) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < arr.length; i++) {
            if (StringUtil.isEmpty(arr[i])) {
                continue;
            }
            sb.append(arr[i]);
            sb.append(",");
        }
        String str = sb.toString();
        str = str.substring(0, str.length() - 1);
        return str;
    }

    /**
     * 定时推送消息，需要追踪的客资
     */
    @Override
    public void pushClientNoticeInfo() {
        List<ClientTimerPO> allClientTimerList = clientTimerDao.getAll();
        if (CollectionUtils.isNotEmpty(allClientTimerList)) {
            // 每个公司一个List
            List<NewsPO> newsPOList = new ArrayList<>();
            for (ClientTimerPO clientTimerPO : allClientTimerList) {
                // 推送消息
                clientTimerDao.delAready(clientTimerPO.getId());
                //TODO
                GoEasyUtil.pushWarnTimer(clientTimerPO.getCompanyId(), clientTimerPO.getStaffId(),
                        clientTimerPO.getKzId(), clientTimerPO.getMsg(), staffDao);
                // 新加一条消息
                NewsPO news = new NewsPO();
                news.setStaffId(clientTimerPO.getStaffId());
                news.setType(MessageConts.MSG_TYPE_NOTICE);
                news.setCompanyId(clientTimerPO.getCompanyId());
                news.setHead(MessageConts.TO_BE_TRACKED_HEAD);
                news.setMsg(clientTimerPO.getMsg());
                news.setKzid(clientTimerPO.getKzId());
                //添加List
                newsPOList.add(news);
            }
            // 添加消息记录
            newsDao.batchInsertNews(newsPOList);
        }
    }

    /**
     * 筛客平均
     */
    @Transactional
    @Override
    public synchronized void pushSk(int companyId, String kzId, int overTime, int interval, int srcType) {
        String role = "";
        if (ChannelConstant.DS_TYPE_LIST.contains(srcType)) {
            role = RoleConstant.DSSX;
        } else if (ChannelConstant.ZJS_TYPE_LIST.contains(srcType)) {
            role = RoleConstant.ZJSSX;
        }
        //1.获取电商筛选人员，领取时间间隔排除
        StaffPushDTO staff = staffDao.getAvgDssxStaff(companyId, interval, role, DBSplitUtil.getInfoTabName(companyId));
        if (staff == null) {
            return;
        }
        //2.修改客资筛选人员ID，姓名，更新时间
        int updateRstNum = clientInfoDao.updateSkInfoWhenAllot(DBSplitUtil.getInfoTabName(companyId), staff.getStaffId(), kzId, companyId);
        if (1 != updateRstNum) {
            throw new RException(ExceptionEnum.INFO_EDIT_ERROR);
        }
        updateRstNum = clientInfoDao.updateSkDetailWhenAllot(DBSplitUtil.getDetailTabName(companyId), staff.getStaffName(), kzId, companyId);
        if (1 != updateRstNum) {
            throw new RException(ExceptionEnum.INFO_EDIT_ERROR);
        }
        //3.更新员工推送时间，今日接单数
        //修改员工最后推送时间
        staffDao.updateStaffLastPushTime(companyId, staff.getStaffId());

        // 推送消息
        ClientGoEasyDTO infoDTO = clientInfoDao.getClientGoEasyDTOById(kzId, DBSplitUtil.getInfoTabName(companyId),
                DBSplitUtil.getDetailTabName(companyId));
        GoEasyUtil.pushInfoComed(companyId, staff.getStaffId(), infoDTO, newsDao, staffDao);
        GoEasyUtil.pushInfoRefresh(companyId, staff.getStaffId(), webSocketMsgUtil);

        // 客资日志记录
        updateRstNum = clientLogDao.addInfoLog(DBSplitUtil.getInfoLogTabName(companyId),
                new ClientLogPO(kzId, ClientLogConst.getAutoAllotLogSk(staff.getGroupName(), staff.getStaffName()),
                        ClientLogConst.INFO_LOGTYPE_ALLOT, companyId));
        if (1 != updateRstNum) {
            throw new RException(ExceptionEnum.LOG_ERROR);
        }

    }
}