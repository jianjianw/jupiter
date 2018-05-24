package com.qiein.jupiter.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import com.alibaba.fastjson.JSONObject;
import com.qiein.jupiter.http.CrmBaseApi;
import com.qiein.jupiter.util.*;
import com.qiein.jupiter.web.entity.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.qiein.jupiter.aop.aspect.annotation.LoginLog;
import com.qiein.jupiter.aop.validate.annotation.Bool;
import com.qiein.jupiter.aop.validate.annotation.Id;
import com.qiein.jupiter.aop.validate.annotation.NotEmptyStr;
import com.qiein.jupiter.enums.TigMsgEnum;
import com.qiein.jupiter.exception.ExceptionEnum;
import com.qiein.jupiter.exception.RException;
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
    private ValueOperations<String, String> valueOperations;

    @Autowired
    private CrmBaseApi crmBaseApi;

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
        return ResultInfoUtil.success(TigMsgEnum.SAVE_SUCCESS);
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
        return ResultInfoUtil.success(TigMsgEnum.SAVE_SUCCESS);
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
        staffService.batchEditStaff(currentLoginStaff.getCompanyId(), staffIds, roleIds, password, groupId);
        return ResultInfoUtil.success(TigMsgEnum.SAVE_SUCCESS);
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
        List<SearchStaffVO> staffList = staffService.getStaffListBySearchKey(currentLoginStaff.getCompanyId(), searchKey);
        return ResultInfoUtil.success(staffList);
    }

    /**
     * 查询小组员工详情
     *
     * @param staffId
     * @return
     */
    @GetMapping("/get_group_staff_by_id")
    public ResultInfo getGroupStaffById(@Id @RequestParam("staffId") int staffId) {
        // 获取当前登录用户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        return ResultInfoUtil.success(staffService.getGroupStaffById(currentLoginStaff.getCompanyId(), staffId));
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

        // 检查是否可删除
        // 先检查是否为客服
        Map<String , Object> map = new HashMap<>();
        String addRstStr = crmBaseApi.doService(map, "clientMoveLp");
        JSONObject jsInfo = JsonFmtUtil.strInfoToJsonObj(addRstStr);
        if (!"100000".equals(jsInfo.getString("code")))
            throw new RException(jsInfo.getString("msg"));

        // 是客服则检查是否存在未邀约客资
        // TODO 等待客资内容写完继续写删除
        StaffStateVO staffStateVO = new StaffStateVO();
        staffStateVO.setCompanyId(currentLoginStaff.getCompanyId());
        staffStateVO.setIds(ids);
        staffStateVO.setDel(true);
        // staffService.batUpdateStaffState(staffStateVO);
        staffService.batDelStaff(staffStateVO);
        return ResultInfoUtil.success("删除成功");
    }

    /**
     * 检查员工是否可删除。 如果员工剩余未邀约客资为0则可删除，不为零则不可删除，需要交接
     *
     * @return
     */
    @GetMapping("/del_staff_check")
    public ResultInfo DelStaffCheck(@NotEmptyStr @RequestParam("staffId") String ids) {
        Map<String,Object> map = new HashMap<>();
        map.put("companyid",getCurrentLoginStaff().getCompanyId());
        map.put("oldstaffid",ids);
        String addRstStr = crmBaseApi.doService(map, "staffCanBeDelete");
        JSONObject jsInfo = JsonFmtUtil.strInfoToJsonObj(addRstStr);
        if (!"100000".equals(jsInfo.getString("code"))){
            return ResultInfoUtil.success(jsInfo.get("content.result"));
        }

        return ResultInfoUtil.success();
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
        staffService.setLockState(staffId, companyId, isLock);
        return ResultInfoUtil.success(TigMsgEnum.OPERATE_SUCCESS);
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

        Map<String,Object> map = new HashMap<>();
        map.put("operaid",staffPO.getId());
        map.put("operaname",staffPO.getNickName());
        map.put("companyid",staffPO.getCompanyId());
        map.put("tostaffid",staffChangeVO.getToStaffId());
        map.put("tostaffname",staffChangeVO.getToStaffName());
        map.put("groupid",staffChangeVO.getGroupId());
        map.put("groupname",staffChangeVO.getGroupName());
        map.put("oldstaffid",staffChangeVO.getOldStaffId());
        map.put("oldstaffname",staffChangeVO.getOldStaffName());

        //直接转发到平台
        String addRstStr = crmBaseApi.doService(map, "clientMoveLp");
        JSONObject jsInfo = JsonFmtUtil.strInfoToJsonObj(addRstStr);
        if (!"100000".equals(jsInfo.getString("code")))
            throw new RException(jsInfo.getString("msg"));
        return ResultInfoUtil.success("交接成功");
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

        return ResultInfoUtil.success(TigMsgEnum.SUCCESS, staffService.getChangeList(companyId));
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
        return ResultInfoUtil.success(TigMsgEnum.SAVE_SUCCESS);
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
        return ResultInfoUtil.success(TigMsgEnum.UPDATE_SUCCESS);
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

        return ResultInfoUtil.success(TigMsgEnum.SUCCESS, staffService.getGroupStaffByType(companyId, type));
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
        return ResultInfoUtil.success(TigMsgEnum.SUCCESS, staffService.getDelStaffList(queryMapDTO));
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
        return ResultInfoUtil.success(TigMsgEnum.RESOTRE_SUCCESS);
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
        return ResultInfoUtil.success(TigMsgEnum.RESOTRE_SUCCESS);
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
        staffService.updateStatusFlag(currentLoginStaff.getId(), currentLoginStaff.getCompanyId()
                , status, currentLoginStaff.getId(), currentLoginStaff.getNickName());
        if (status == 0) {
            //上线
            return ResultInfoUtil.success(TigMsgEnum.OFFLINE_SUCCESS);
        } else {
            //下线
            return ResultInfoUtil.success(TigMsgEnum.ONLINE_SUCCESS);
        }
    }

    /**
     * 获取员工状态日志
     */
    @GetMapping("/get_staff_status_log")
    public ResultInfo getStaffStatusLog(@RequestParam("id") int id) {
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        return ResultInfoUtil.success(
                staffService.getStaffStatusLogById(currentLoginStaff.getCompanyId(), id));
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
        staffService.logout(currentLoginStaff.getCompanyId(),
                currentLoginStaff.getId(), currentLoginStaff.getNickName());
        return ResultInfoUtil.success();
    }


    /**
     * 更新员工心跳，同时检测IP
     */
    @GetMapping("/heart_beat")
    public ResultInfo staffHeartBeat() {
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        boolean b = staffService.heartBeat(currentLoginStaff.getId(), currentLoginStaff.getCompanyId(), getIp());
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
        staffService.bindingWeChat(currentLoginStaff.getId(), currentLoginStaff.getCompanyId(),
                code);
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
}
