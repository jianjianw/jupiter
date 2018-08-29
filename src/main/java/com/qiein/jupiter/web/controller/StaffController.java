package com.qiein.jupiter.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import com.fasterxml.jackson.databind.annotation.JsonAppend;
import com.qiein.jupiter.constant.CommonConstant;
import com.qiein.jupiter.msg.websocket.WebSocketMsgUtil;
import com.qiein.jupiter.util.*;
import com.qiein.jupiter.web.entity.dto.DingBindUserDTO;
import com.qiein.jupiter.web.entity.dto.RequestInfoDTO;
import com.qiein.jupiter.web.entity.po.SystemLog;
import com.qiein.jupiter.web.entity.vo.*;
import com.qiein.jupiter.web.service.SystemLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.qiein.jupiter.aop.aspect.annotation.LoginLog;
import com.qiein.jupiter.aop.validate.annotation.Bool;
import com.qiein.jupiter.aop.validate.annotation.Id;
import com.qiein.jupiter.aop.validate.annotation.NotEmptyStr;
import com.qiein.jupiter.enums.TipMsgEnum;
import com.qiein.jupiter.exception.ExceptionEnum;
import com.qiein.jupiter.exception.RException;
import com.qiein.jupiter.http.CrmBaseApi;
import com.qiein.jupiter.web.entity.dto.QueryMapDTO;
import com.qiein.jupiter.web.entity.dto.StaffPasswordDTO;
import com.qiein.jupiter.web.entity.po.StaffPO;
import com.qiein.jupiter.web.service.StaffService;

/**
 * 员工 Controller
 */
@RestController
@RequestMapping("/staff")
@Validated
public class StaffController extends BaseController {

    @Autowired
    private StaffService staffService;

    @Autowired
    private CrmBaseApi crmBaseApi;

    @Autowired
    private WebSocketMsgUtil webSocketMsgUtil;

    @Autowired
    private SystemLogService logService;

    /**
     * 获取列表
     *
     * @param queryMapDTO
     * @return
     */
    @PostMapping("/findList")
    public ResultInfo getAll(@RequestBody QueryMapDTO queryMapDTO) {
        return ResultInfoUtil.success(staffService.findList(queryMapDTO));
    }

    /**
     * 插入
     *
     * @param staffVO
     * @return
     */
    @PostMapping("/insert")
    @LoginLog
    public ResultInfo insert(@RequestBody @Validated StaffVO staffVO) {
        // 获取当前登录用户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        if (!RegexUtil.checkMobile(staffVO.getPhone())) {
            return ResultInfoUtil.error(ExceptionEnum.PHONE_ERROR);
        }
        // 设置cid
        staffVO.setCompanyId(currentLoginStaff.getCompanyId());
        // 对象参数trim
        ObjectUtil.objectStrParamTrim(staffVO);
        staffService.insert(staffVO);
        //添加日志
        RequestInfoDTO requestInfo = getRequestInfo();
        try {
            SystemLog log = new SystemLog(SysLogUtil.LOG_TYPE_STAFF, requestInfo.getIp(), requestInfo.getUrl(), currentLoginStaff.getId(),
                    currentLoginStaff.getNickName(), SysLogUtil.getAddLog(SysLogUtil.LOG_SUP_STAFF, staffVO.getNickName()),
                    currentLoginStaff.getCompanyId());
            logService.addLog(log);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultInfoUtil.success(TipMsgEnum.SAVE_SUCCESS);
        }
        return ResultInfoUtil.success(TipMsgEnum.SAVE_SUCCESS);
    }

    /**
     * 更新员工信息
     *
     * @param staffVO
     * @return
     */
    @PostMapping("/update_staff")
    public ResultInfo updateStaff(@RequestBody @Validated StaffVO staffVO) {
        // 对象参数trim
        ObjectUtil.objectStrParamTrim(staffVO);
        if (staffVO.getId() == 0) {
            return ResultInfoUtil.error(ExceptionEnum.STAFF_ID_NULL);
        }
        if (!RegexUtil.checkMobile(staffVO.getPhone())) {
            return ResultInfoUtil.error(ExceptionEnum.PHONE_ERROR);
        }
        if (StringUtil.isEmpty(staffVO.getGroupId())) {
            return ResultInfoUtil.error(ExceptionEnum.GROUP_IS_NULL);
        }
        // 获取当前登录用户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        // 设置cid
        staffVO.setCompanyId(currentLoginStaff.getCompanyId());
        staffService.update(staffVO);
        return ResultInfoUtil.success(TipMsgEnum.SAVE_SUCCESS);
    }

    /**
     * 删除标记
     *
     * @param id
     * @return
     */
    @GetMapping("/delete_flag")
    public ResultInfo deleteFlag(@Id Integer id) {
        // 获取当前登录账户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        staffService.logicDelete(id, currentLoginStaff.getCompanyId());
        return ResultInfoUtil.success();
    }

    /**
     * 获取组员工列表
     *
     * @param groupId
     * @return
     */
    @GetMapping("/get_group_staff_list")
    public ResultInfo getGroupStaffList(@NotEmptyStr @RequestParam("groupId") String groupId) {
        // 获取当前登录账户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        List<StaffVO> staffList = staffService.getGroupStaffs(currentLoginStaff.getCompanyId(), groupId);
        return ResultInfoUtil.success(staffList);
    }

    /**
     * 批量编辑员工信息
     *
     * @return
     */
    @GetMapping("/batch_edit_staff")
    public ResultInfo batchEditStaff(@NotEmptyStr @RequestParam("staffIds") String staffIds,
                                     @RequestParam("roleIds") String roleIds, @RequestParam("password") String password,
                                     @NotEmptyStr @RequestParam("groupId") String groupId) {
        // 获取当前登录用户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        List<SearchStaffVO> staffOld = staffService.getGroupById(staffIds, currentLoginStaff.getCompanyId());
        staffService.batchEditStaff(currentLoginStaff.getCompanyId(), staffIds, roleIds, password, groupId);
        List<SearchStaffVO> staffNew = staffService.getGroupById(staffIds, currentLoginStaff.getCompanyId());
        Map<String, String> map = new HashMap<>();
        String old = CommonConstant.NULL_STR;
        for (SearchStaffVO staff : staffOld) {
            old += staff.getNickName() + " " + staff.getGroupName() + CommonConstant.STR_SEPARATOR;
        }
        map.put(old, staffNew.get(0).getGroupName());
        RequestInfoDTO requestInfo = getRequestInfo();
        try {
            // 日志记录
            logService.addLog(new SystemLog(SysLogUtil.LOG_TYPE_STAFF, requestInfo.getIp(), requestInfo.getUrl(), currentLoginStaff.getId(),
                    currentLoginStaff.getNickName(), SysLogUtil.getEditLog(SysLogUtil.LOG_SUP_STAFF, SysLogUtil.LOG_SUP_STAFF, map),
                    currentLoginStaff.getCompanyId()));

        } catch (Exception e) {
            e.printStackTrace();
            return ResultInfoUtil.success(TipMsgEnum.SAVE_SUCCESS);
        }
        return ResultInfoUtil.success(TipMsgEnum.SAVE_SUCCESS);
    }

    /**
     * 搜索员工信息
     *
     * @param searchKey
     * @return
     */
    @GetMapping("/search_staff")
    public ResultInfo searchStaff(@NotEmptyStr @RequestParam("searchKey") String searchKey) {
        // 获取当前登录用户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        List<SearchStaffVO> staffList = staffService.getStaffListBySearchKey(currentLoginStaff.getCompanyId(),
                searchKey);
        return ResultInfoUtil.success(staffList);
    }

    /**
     * 查询小组员工详情
     *
     * @param staffId
     * @return
     */
    @GetMapping("/get_group_staff_by_id")
    public ResultInfo getGroupStaffById(@Id @RequestParam("staffId") int staffId, @RequestParam("groupId") String groupId) {
        // 获取当前登录用户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        return ResultInfoUtil.success(staffService.getGroupStaffById(currentLoginStaff.getCompanyId(), staffId, groupId));
    }

    /**
     * 获取员工详情
     *
     * @return
     */
    @GetMapping("/detail")
    public ResultInfo getDetailInfo() {
        // 获取当前登录账户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        return ResultInfoUtil
                .success(staffService.getById(currentLoginStaff.getId(), currentLoginStaff.getCompanyId()));
    }

    /**
     * 删除指定员工
     *
     * @param ids
     * @return
     */
    @GetMapping("/del_staff")
    public ResultInfo deleteStaff(@NotEmptyStr @RequestParam("staffId") String ids) {
        // 获取当前登录账户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        // 如果是批量删除则不校验
        if (ids.split(",").length == 1) {
            Map<String, Object> map = new HashMap<>();
            map.put("companyid", getCurrentLoginStaff().getCompanyId());
            map.put("oldstaffid", ids);
            String back = crmBaseApi.doService(map, "staffCanBeDelete");
            JSONObject jsInfo = JsonFmtUtil.strInfoToJsonObj(back);
            JSONObject jsCent = JsonFmtUtil.strContentToJsonObj(back);
            if (!"100000".equals(jsInfo.getString("code")))
                throw new RException(jsInfo.getString("msg"));
            if (!jsCent.getBoolean("result"))
                throw new RException(ExceptionEnum.STAFF_CAN_NOT_DEL);
        }

        StaffStateVO staffStateVO = new StaffStateVO();
        staffStateVO.setCompanyId(currentLoginStaff.getCompanyId());
        staffStateVO.setIds(ids);
        staffStateVO.setDel(true);
        // staffService.batUpdateStaffState(staffStateVO);
        String staffName = CommonConstant.NULL_STR;
        List<StaffPO> staff = staffService.getByIds(ids, currentLoginStaff.getCompanyId());
        for (StaffPO staffPO : staff) {
            staffName += staffPO.getNickName() + CommonConstant.STR_SEPARATOR;
        }
        staffService.batDelStaff(staffStateVO);
        RequestInfoDTO requestInfo = getRequestInfo();
        try {
            // 日志记录
            logService.addLog(new SystemLog(SysLogUtil.LOG_TYPE_STAFF, requestInfo.getIp(), requestInfo.getUrl(), currentLoginStaff.getId(),
                    currentLoginStaff.getNickName(), SysLogUtil.getRemoveLog(SysLogUtil.LOG_SUP_STAFF, staffName),
                    currentLoginStaff.getCompanyId()));

        } catch (Exception e) {
            e.printStackTrace();
            return ResultInfoUtil.success();
        }
        return ResultInfoUtil.success();
    }

    /**
     * 检查员工是否可删除。 如果员工剩余未邀约客资为0则可删除，不为零则不可删除，需要交接
     *
     * @return
     */
    @GetMapping("/del_staff_check")
    public ResultInfo DelStaffCheck(@NotEmptyStr @RequestParam("staffId") String ids) {
        // 如果多选删除，直接让删除，不校验
        if (ids.split(",").length > 1) {
            return ResultInfoUtil.success(true);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("companyid", getCurrentLoginStaff().getCompanyId());
        map.put("oldstaffid", ids);
        String back = crmBaseApi.doService(map, "staffCanBeDelete");
        JSONObject jsInfo = JsonFmtUtil.strInfoToJsonObj(back);
        JSONObject jsCent = JsonFmtUtil.strContentToJsonObj(back);
        if (!"100000".equals(jsInfo.getString("code")))
            throw new RException(jsInfo.getString("msg"));
        if (!jsCent.getBoolean("result"))
            throw new RException(ExceptionEnum.STAFF_CAN_NOT_DEL);

        return ResultInfoUtil.success(true);
    }

    /**
     * 锁定员工
     *
     * @param staffId staffId 被锁定的员工编号
     * @param isLock  锁定标识
     * @return
     */
    @GetMapping("/lock_staff")
    public ResultInfo LockStaff(@Id @RequestParam("staffId") Integer staffId,
                                @Bool @RequestParam("isLock") Boolean isLock) {
        // 获取当前登录账户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        // 获取操作用户所属公司
        Integer companyId = currentLoginStaff.getCompanyId();
        // 锁定状态
        StaffPO staff = staffService.getById(staffId, currentLoginStaff.getCompanyId());
        staffService.setLockState(staffId, companyId, isLock);
        Map<String, String> map = new HashMap<>();
        map.put(staff.getNickName(), CommonConstant.NULL_STR);
        RequestInfoDTO requestInfo = getRequestInfo();
        try {
            // 日志记录
            logService.addLog(new SystemLog(SysLogUtil.LOG_TYPE_STAFF, requestInfo.getIp(), requestInfo.getUrl(), currentLoginStaff.getId(),
                    currentLoginStaff.getNickName(), SysLogUtil.getEditLog(SysLogUtil.LOG_SUP_LOCK, SysLogUtil.LOG_SUP_LOCK, map),
                    currentLoginStaff.getCompanyId()));

        } catch (Exception e) {
            e.printStackTrace();
            return ResultInfoUtil.success(TipMsgEnum.OPERATE_SUCCESS);
        }
        return ResultInfoUtil.success(TipMsgEnum.OPERATE_SUCCESS);
    }

    /**
     * 交接接口
     *
     * @param staffChangeVO
     * @return
     */
    @PostMapping("/change_staff")
    public ResultInfo ChangeStaff(@RequestBody StaffChangeVO staffChangeVO) {
        // 获取当前登录账户
        StaffPO staffPO = getCurrentLoginStaff();

        Map<String, Object> map = new HashMap<>();
        map.put("operaid", staffPO.getId());
        map.put("operaname", staffPO.getNickName());
        map.put("companyid", staffPO.getCompanyId());
        map.put("tostaffid", staffChangeVO.getToStaffId());
        map.put("tostaffname", staffChangeVO.getToStaffName());
        map.put("groupid", staffChangeVO.getGroupId());
        map.put("groupname", staffChangeVO.getGroupName());
        map.put("oldstaffid", staffChangeVO.getOldStaffId());
        map.put("oldstaffname", staffChangeVO.getOldStaffName());

        // 直接转发到平台
        String json = crmBaseApi.doService(map, "clientMoveLp");
        if (StringUtil.isEmpty(json) || !"100000".equalsIgnoreCase(JSONObject.parseObject(json).getJSONObject("response").getJSONObject("info").getString("code"))) {
            return ResultInfoUtil.error(ExceptionEnum.UNKNOW_ERROR.getCode(), JSONObject.parseObject(json).getJSONObject("response").getJSONObject("info").getString("msg"));
        }
        Map<String, String> editMap = new HashMap<>();
        editMap.put(staffChangeVO.getOldStaffName(), staffChangeVO.getToStaffName());
        RequestInfoDTO requestInfo = getRequestInfo();
        try {
            // 日志记录
            logService.addLog(new SystemLog(SysLogUtil.LOG_TYPE_STAFF, requestInfo.getIp(), requestInfo.getUrl(), staffPO.getId(),
                    staffPO.getNickName(), SysLogUtil.getEditLog(SysLogUtil.LOG_SUP_GIVE, SysLogUtil.LOG_SUP_GIVE, editMap),
                    staffPO.getCompanyId()));

        } catch (Exception e) {
            e.printStackTrace();
            return ResultInfoUtil.success();
        }
        return ResultInfoUtil.success();
    }

    /**
     * 获取电商邀约小组人员列表
     *
     * @return
     */
    @GetMapping("/change_list")
    public ResultInfo getChangeList() {
        // 获取当前登录账户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        // 获取操作用户所属公司
        Integer companyId = currentLoginStaff.getCompanyId();

        return ResultInfoUtil.success(TipMsgEnum.SUCCESS, staffService.getChangeList(companyId));
    }

    /**
     * 更新员工基础信息，不包含权限等
     *
     * @param staffDetailVO
     * @return
     */
    @PostMapping("/update_base_info")
    public ResultInfo updateBaseInfo(@RequestBody @Valid StaffDetailVO staffDetailVO) {
        // 对象参数trim
        ObjectUtil.objectStrParamTrim(staffDetailVO);
        if (staffDetailVO.getId() == 0) {
            return ResultInfoUtil.error(ExceptionEnum.STAFF_ID_NULL);
        }
        // 获取当前登录用户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        // 设置cid
        staffDetailVO.setCompanyId(currentLoginStaff.getCompanyId());
        staffService.update(staffDetailVO);
        return ResultInfoUtil.success(TipMsgEnum.SAVE_SUCCESS);
    }

    /**
     * 更新员工密码
     *
     * @param staffPasswordDTO
     * @return
     */
    @PostMapping("/update_password")
    // todo 增加密码安全度校验
    public ResultInfo updatePassword(@Validated @RequestBody StaffPasswordDTO staffPasswordDTO) {
        // 获取当前登录用户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        staffPasswordDTO.setId(currentLoginStaff.getId());
        staffPasswordDTO.setCompanyId(currentLoginStaff.getCompanyId());
        staffService.updatePassword(staffPasswordDTO);
        return ResultInfoUtil.success(TipMsgEnum.UPDATE_SUCCESS);
    }

    /**
     * 是否是原来正确的密码
     *
     * @param oldPassword
     * @return
     */
    @GetMapping("/right_password")
    public boolean rightPassword(@RequestParam("password") String oldPassword) {
        // 获取当前登录用户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        return staffService.isRightPassword(currentLoginStaff.getId(), oldPassword, currentLoginStaff.getCompanyId());
    }

    /**
     * 根据类型获取小组及人员信息
     *
     * @param type
     * @return
     */
    @GetMapping("/get_group_staff_by_type")
    public ResultInfo getGroupStaffByType(@NotEmptyStr String type) {
        // 获取当前登录账户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        // 获取操作用户所属公司
        Integer companyId = currentLoginStaff.getCompanyId();

        return ResultInfoUtil.success(TipMsgEnum.SUCCESS, staffService.getGroupStaffByType(companyId, type));
    }

    /**
     * 获取已离职的员工列表
     *
     * @param queryMapDTO
     * @return
     */
    @GetMapping("/get_del_staff_list")
    public ResultInfo getDelStaffList(QueryMapDTO queryMapDTO) {
        // 获取当前登录账户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        HashMap<String, Object> condition = new HashMap<>();
        condition.put("companyId", currentLoginStaff.getCompanyId());
        condition.put("delFlag", true);
        queryMapDTO.setCondition(condition);
        return ResultInfoUtil.success(TipMsgEnum.SUCCESS, staffService.getDelStaffList(queryMapDTO));
    }

    /**
     * 恢复离职员工
     *
     * @param staffVO
     * @return
     */
    @PostMapping("/restore_del_staff")
    public ResultInfo restoreDelStaff(@RequestBody StaffVO staffVO) {
        if (NumUtil.isNull(staffVO.getId())) {
            return ResultInfoUtil.error(ExceptionEnum.STAFF_ID_NULL);
        }
        if (StringUtil.isEmpty(staffVO.getRoleIds())) {
            return ResultInfoUtil.error(ExceptionEnum.ROLE_IS_NULL);
        }
        if (StringUtil.isEmpty(staffVO.getNickName())) {
            return ResultInfoUtil.error(ExceptionEnum.NICKNAME_IS_NULL);
        }
        if (StringUtil.isEmpty(staffVO.getPhone())) {
            return ResultInfoUtil.error(ExceptionEnum.PHONE_IS_NULL);
        }
        if (!RegexUtil.checkMobile(staffVO.getPhone())) {
            return ResultInfoUtil.error(ExceptionEnum.PHONE_ERROR);
        }
        if (StringUtil.isEmpty(staffVO.getUserName())) {
            return ResultInfoUtil.error(ExceptionEnum.USERNAME_IS_NULL);
        }
        // 获取当前登录账户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        staffVO.setCompanyId(currentLoginStaff.getCompanyId());
        staffService.restoreDelStaff(staffVO);
        return ResultInfoUtil.success(TipMsgEnum.RESOTRE_SUCCESS);
    }

    /**
     * 批量恢复员工
     *
     * @return
     */
    @GetMapping("/batch_restore_staff")
    public ResultInfo batchRestoreStaff(@NotEmptyStr @RequestParam("staffIds") String staffIds,
                                        @NotEmptyStr @RequestParam("roleIds") String roleIds, @RequestParam("password") String password,
                                        @NotEmptyStr @RequestParam("groupId") String groupId) {
        // 获取当前登录用户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        staffService.batchRestoreStaff(currentLoginStaff.getCompanyId(), staffIds, roleIds, password, groupId);
        return ResultInfoUtil.success(TipMsgEnum.RESOTRE_SUCCESS);
    }

    /**
     * 搜索离职员工
     *
     * @param searchKey
     * @return
     */
    @GetMapping("/search_del_staff_list")
    public ResultInfo searchDelStaffList(@NotEmptyStr @RequestParam("searchKey") String searchKey) {
        // 获取当前登录用户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        return ResultInfoUtil
                .success(staffService.getDelStaffListBySearchKey(currentLoginStaff.getCompanyId(), searchKey));
    }

    /**
     * 员工自己设置在线状态
     *
     * @param status
     * @return
     */
    @GetMapping("/set_status")
    public ResultInfo setStaffStatus(@RequestParam("status") int status) {
        // 获取当前登录用户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        staffService.updateStatusFlag(currentLoginStaff.getId(), currentLoginStaff.getCompanyId(), status,
                currentLoginStaff.getId(), currentLoginStaff.getNickName());
        //推送刷新状态
        webSocketMsgUtil.pushBaseInfoFresh(currentLoginStaff.getCompanyId(), currentLoginStaff.getId());
        if (status == 0) {
            // 上线
            return ResultInfoUtil.success(TipMsgEnum.OFFLINE_SUCCESS);
        } else {
            // 下线
            return ResultInfoUtil.success(TipMsgEnum.ONLINE_SUCCESS);
        }
    }

    /**
     * 获取员工状态日志
     */
    @GetMapping("/get_staff_status_log")
    public ResultInfo getStaffStatusLog(@RequestParam("id") int id) {
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        return ResultInfoUtil.success(staffService.getStaffStatusLogById(currentLoginStaff.getCompanyId(), id));
    }

    /**
     * 根据员工id获取员工名片
     *
     * @param staffId
     * @return
     */
    @GetMapping("/card")
    public ResultInfo getStaffCard(int staffId) {
        return ResultInfoUtil.success(staffService.getStaffCard(staffId, getCurrentLoginStaff().getCompanyId()));
    }

    /**
     * 用户退出登录
     */
    @GetMapping("/logout")
    public ResultInfo logout() {
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        staffService.logout(currentLoginStaff.getCompanyId(), currentLoginStaff.getId(),
                currentLoginStaff.getNickName());
        return ResultInfoUtil.success();
    }

    /**
     * 更新员工心跳，同时检测IP
     */
    @GetMapping("/heart_beat")
    public ResultInfo staffHeartBeat() {
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        boolean b = staffService.heartBeat(currentLoginStaff, getIp());
        // 计算今日领取客资数并校验是否满限
        staffService.resizeTodayNum(currentLoginStaff.getCompanyId(), currentLoginStaff.getId());
        if (b) {
            return ResultInfoUtil.success();
        } else {
            return ResultInfoUtil.error(ExceptionEnum.IP_NOT_IN_SAFETY);
        }
    }

    /**
     * 绑定微信
     */
    @GetMapping("/binding_wechat")
    public ResultInfo bindingWeChat(String code) {
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        staffService.bindingWeChat(currentLoginStaff.getId(), currentLoginStaff.getCompanyId(), code);
        return ResultInfoUtil.success();
    }

    /**
     * 绑定钉钉
     */
    @GetMapping("/binding_ding")
    public ResultInfo bindingDing(String code) {
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        staffService.bindingDing(currentLoginStaff.getId(), currentLoginStaff.getCompanyId(), code);
        return ResultInfoUtil.success();
    }

    @PostMapping("/set_msg")
    public ResultInfo staffMsgSet(@RequestBody StaffMsg staffMsg) {
        staffMsg.setCompanyId(getCurrentLoginStaff().getCompanyId());
        staffService.editMsgSet(staffMsg);
        return ResultInfoUtil.success();
    }

    /**
     * 功能描述:
     * 获取未绑定钉钉的用户
     *
     * @auther: Tt(yehuawei)
     * @date:
     * @param:
     * @return:
     */
    @GetMapping("/get_ding_not_bind")
    public ResultInfo getNotBindDingUser() {
        Map<String, Object> map = new HashMap<>();
        List<DingBindUserDTO> list = staffService.getNotBindDingUser(getCurrentLoginStaff().getCompanyId());
        map.put("count", list == null ? 0 : list.size());
        map.put("list", list);
        return ResultInfoUtil.success(map);
    }

    /**
     * 员工切换企业
     */
    @GetMapping("/change_company")
    public ResultInfo changeCompany(int companyId) {
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        return ResultInfoUtil.success(staffService.getStaffByChangeCid(companyId,
                currentLoginStaff.getId(), currentLoginStaff.getCompanyId()));
    }

    /**
     * 根据关键字搜索员工
     *
     * @return
     */
    @GetMapping("/search_by_key")
    public ResultInfo searchByKey(String key) {
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        return ResultInfoUtil.success(staffService.searchStaffByKey(currentLoginStaff.getCompanyId(), key));
    }

    /**
     * 根据关键字搜索员工
     *
     * @return
     */
    @GetMapping("/get_staff_msg_set")
    public ResultInfo getStaffMsgSet() {
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        String msgSet = staffService.getMsgSetByStaffId(currentLoginStaff.getCompanyId(), currentLoginStaff.getId());
        HashMap<String, Boolean> result = new HashMap<>();
        result.put("explode", msgSet.indexOf("/1/") != -1);
        return ResultInfoUtil.success();
    }
}
