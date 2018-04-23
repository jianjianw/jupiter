package com.qiein.jupiter.web.controller;

import com.qiein.jupiter.aop.validate.annotation.Id;
import com.qiein.jupiter.aop.aspect.annotation.LoginLog;
import com.qiein.jupiter.aop.validate.annotation.NotEmptyStr;
import com.qiein.jupiter.constant.NumberConstant;
import com.qiein.jupiter.constant.RedisConstant;
import com.qiein.jupiter.constant.TipMsgConstant;
import com.qiein.jupiter.exception.ExceptionEnum;
import com.qiein.jupiter.exception.RException;
import com.qiein.jupiter.util.*;
import com.qiein.jupiter.web.entity.dto.QueryMapDTO;
import com.qiein.jupiter.web.entity.dto.StaffPasswordDTO;
import com.qiein.jupiter.web.entity.po.CompanyPO;
import com.qiein.jupiter.web.entity.po.StaffPO;
import com.qiein.jupiter.web.entity.vo.LoginUserVO;
import com.qiein.jupiter.web.entity.vo.StaffVO;
import com.qiein.jupiter.web.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
        //获取当前登录用户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        if (!RegexUtil.checkMobile(staffVO.getPhone())) {
            return ResultInfoUtil.error(ExceptionEnum.PHONE_ERROR);
        }
        //设置cid
        staffVO.setCompanyId(currentLoginStaff.getCompanyId());
        //对象参数trim
        ObjectUtil.objectStrParamTrim(staffVO);
        staffService.insert(staffVO);
        return ResultInfoUtil.success(TipMsgConstant.SAVE_SUCCESS);
    }

    /**
     * 更新员工信息
     *
     * @param staffVO
     * @return
     */
    @PostMapping("/update_staff")
    public ResultInfo updateStaff(@RequestBody @Validated StaffVO staffVO) {
        //对象参数trim
        ObjectUtil.objectStrParamTrim(staffVO);
        if (staffVO.getId() == 0) {
            return ResultInfoUtil.error(ExceptionEnum.STAFF_ID_NULL);
        }
        if (!RegexUtil.checkMobile(staffVO.getPhone())) {
            return ResultInfoUtil.error(ExceptionEnum.PHONE_ERROR);
        }
        //获取当前登录用户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        //设置cid
        staffVO.setCompanyId(currentLoginStaff.getCompanyId());
        staffService.update(staffVO);
        return ResultInfoUtil.success(TipMsgConstant.SAVE_SUCCESS);
    }

    /**
     * 删除标记
     *
     * @param id
     * @return
     */
    @GetMapping("/delete_flag")
    public ResultInfo deleteFlag(@Id int id) {
        //获取当前登录账户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        staffService.logicDelete(id, currentLoginStaff.getCompanyId());
        return ResultInfoUtil.success();
    }


    /**
     * 获取用户所在所有企业信息
     *
     * @param loginUserVO
     * @return
     */
    @PostMapping("/get_company_list")
    public ResultInfo getCompanyList(@RequestBody @Validated LoginUserVO loginUserVO) {
        //对象参数trim
        ObjectUtil.objectStrParamTrim(loginUserVO);
        //校验用户验证码
        checkLoginVerifyCode(loginUserVO);
        try {
            //返回结果
            List<CompanyPO> companyList = staffService.getCompanyList(loginUserVO.getUserName(), loginUserVO.getPassword());
            return ResultInfoUtil.success(companyList);
        } catch (RException e) {
            //将错误次数+1
            valueOperations.increment(RedisConstant.getUserLoginErrNumKey(loginUserVO.getUserName()),
                    NumberConstant.LOGIN_ERROR_ADD_NUM);
            return ResultInfoUtil.error(e.getCode(), e.getMsg());
        }
    }


    /**
     * 根据公司ID登录
     *
     * @param loginUserVO
     * @return
     */
    @PostMapping("/login_with_company_id")
    public ResultInfo loginWithCompanyId(@RequestBody @Validated LoginUserVO loginUserVO) {
        //对象参数trim
        ObjectUtil.objectStrParamTrim(loginUserVO);
        //校验用户验证码
        checkLoginVerifyCode(loginUserVO);
        String userName = loginUserVO.getUserName();
        String password = loginUserVO.getPassword();
        //校验公司Id
        if (loginUserVO.getCompanyId() == 0) {
            throw new RException(ExceptionEnum.COMPANYID_NULL);
        }
        try {
            //返回结果
            StaffPO staffPO = staffService.loginWithCompanyId(userName, password, loginUserVO.getCompanyId());
            return ResultInfoUtil.success(staffPO);
        } catch (RException e) {
            //登录失败，将错误次数+1
            valueOperations.increment(RedisConstant.getUserLoginErrNumKey(userName), 1);
            return ResultInfoUtil.error(e.getCode(), e.getMsg());
        }
    }

    /**
     * 是否需要验证码
     *
     * @param userName 用户名
     */
    @GetMapping("/need_verity_code")
    public boolean needVerityCode(@NotEmptyStr @RequestParam("phone") String userName) {
        //判断是否需要验证码
        String userLoginErrNum = valueOperations.get(RedisConstant.getUserLoginErrNumKey(userName));
        if (userLoginErrNum == null) {
            //如果没有查询到，说明是第一次，设置默认值0,过期时间为1小时
            valueOperations.set(
                    RedisConstant.getUserLoginErrNumKey(userName),
                    String.valueOf(NumberConstant.DEFAULT_ZERO),
                    NumberConstant.LOGIN_ERROR_NUM_EXPIRE_TIME,
                    TimeUnit.HOURS);
            return false;
        } else {
            //是否大于允许的错误最大值
            int errNum = Integer.valueOf(userLoginErrNum);
            return errNum >= NumberConstant.ALLOW_USER_LOGIN_ERR_NUM;
        }
    }

    /**
     * 请求验证码
     *
     * @param response
     */
    @GetMapping("/verify_code")
    public void loginCode(HttpServletResponse response, @RequestParam("phone") String userName) {
        if (StringUtil.isNullStr(userName) || !RegexUtil.checkMobile(userName)) {
            return;
        }
        //生成验证码并放入缓存
        String code = VerifyCodeUtil.execute(response);
        valueOperations.set(RedisConstant.getVerifyCodeKey(userName), code);
    }

    @GetMapping("/get_group_staff_list")
    public ResultInfo getGroupStaffList(@NotEmptyStr @RequestParam("groupId") String groupId) {
        //获取当前登录账户
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
    public ResultInfo batchEditStaff(@NotEmptyStr @RequestParam("staffIds") String staffIds, @RequestParam("roleIds") String roleIds,
                                     @RequestParam("password") String password, @NotEmptyStr @RequestParam("groupId") String groupId) {
        //获取当前登录用户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        staffService.batchEditStaff(currentLoginStaff.getCompanyId(), staffIds, roleIds, password, groupId);
        return ResultInfoUtil.success(TipMsgConstant.SAVE_SUCCESS);
    }

    /**
     * 搜索员工信息
     *
     * @param searchKey
     * @return
     */
    @GetMapping("/search_staff")
    public ResultInfo searchStaff(@NotEmptyStr @RequestParam("searchKey") String searchKey) {
        //获取当前登录用户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        List<StaffVO> staffList = staffService.getStaffListBySearchKey(currentLoginStaff.getCompanyId(), searchKey);
        return ResultInfoUtil.success(staffList);
    }

    /**
     * 校验用户验证码
     *
     * @param loginUserVO
     */
    private void checkLoginVerifyCode(LoginUserVO loginUserVO) {
        //对象参数trim
        ObjectUtil.objectStrParamTrim(loginUserVO);
        String userName = loginUserVO.getUserName();
        String verifyCode = loginUserVO.getVerifyCode();
        //判断是否需要验证码以及验证码正确性
        if (needVerityCode(userName)) {
            //验证码为空
            if (StringUtil.isNullStr(verifyCode)) {
                throw new RException(ExceptionEnum.VERIFY_NULL);
            } else {
                //从缓存获取key并判断
                String verifyCodeTrue = valueOperations.get(RedisConstant.getVerifyCodeKey(userName));
                if (!StringUtil.ignoreCaseEqual(verifyCode, verifyCodeTrue)) {
                    //验证码错误
                    throw new RException(ExceptionEnum.VERIFY_ERROR);
                }
            }
        }
    }

    /**
     * 获取员工详情
     *
     * @return
     */
    @GetMapping("/detail")
    public ResultInfo getDetailInfo() {
        //获取当前登录账户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        return ResultInfoUtil.success(staffService.getById(currentLoginStaff.getId(),
                currentLoginStaff.getCompanyId()));
    }

    /**
     * 首页获取基础信息
     */
    @GetMapping("/base_info")
    public ResultInfo getBaseInfo() {
        //获取当前登录账户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        return ResultInfoUtil.success(staffService.getStaffBaseInfo(currentLoginStaff.getId(),
                currentLoginStaff.getCompanyId()));
    }

    /**
     * 删除指定员工
     *
     * @param ids
     * @return
     */
    @GetMapping("/del_staff")
    public ResultInfo deleteStaff(@NotEmptyStr @RequestParam("staffId") String ids) {
        //获取当前登录账户
        StaffPO currentLoginStaff = getCurrentLoginStaff();

        //获取操作用户所属公司
        Integer companyId = currentLoginStaff.getCompanyId();

        //待被删除的员工数组
        String[] array = ids.split(",");

        //检查是否可删除
        //先检查是否为客服
        //是客服则检查是否存在未邀约客资
        //TODO 等待客资内容写完继续写删除
        staffService.batDelete(array, companyId);

        return ResultInfoUtil.success("删除成功");
    }

    /**
     * 检查员工是否可删除。
     * 如果员工剩余未邀约客资为0则可删除，不为零则不可删除，需要交接
     *
     * @return
     */
    @GetMapping("/del_staff_check")
    public ResultInfo DelStaffCheck(@NotEmptyStr @RequestParam("staffId") String ids) {
        //获取当前登录账户
        StaffPO currentLoginStaff = getCurrentLoginStaff();

        //获取操作用户所属公司
        Integer companyId = currentLoginStaff.getCompanyId();

        //待被检查是否可删除的员工数组
        String[] array = ids.split(",");

        //检查是否可删除
        //先检查是否为客服
        String msg;
        if (true) {
            //TODO 等待客资内容写完继续写删除
            msg = staffService.checkBatDelete(array, companyId);
        } else {
        }
        //是客服则检查是否存在未邀约客资
        return ResultInfoUtil.success(msg, true);
    }


    /**
     * 锁定员工
     *
     * @param staffId staffId 被锁定的员工编号
     * @param isLock  锁定标识
     * @return
     */
    @GetMapping("/lock_staff")
    public ResultInfo LockStaff(@NotEmptyStr @RequestParam("staffId") Integer staffId, @NotEmptyStr @RequestParam("isLock") Boolean isLock) {
        //获取当前登录账户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        //获取操作用户所属公司
        Integer companyId = currentLoginStaff.getCompanyId();

        try {
            //锁定状态
            staffService.setLockState(staffId, companyId, isLock);
        } catch (Exception e) {
            e.printStackTrace();
            ResultInfoUtil.error(ExceptionEnum.UNKNOW_ERROR);
        }

        return ResultInfoUtil.success("操作成功");
    }

    /**
     * 交接接口
     *
     * @param staffId   交接的员工id
     * @param beStaffId 被转移的员工id
     * @return
     */
    @GetMapping("/change_staff")
    public ResultInfo ChangeStaff(@NotEmptyStr @RequestParam("staffId") Integer staffId,
                                  @NotEmptyStr @RequestParam("beStaffId") Integer beStaffId) {
        //获取当前登录账户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        //获取操作用户所属公司
        Integer companyId = currentLoginStaff.getCompanyId();

        try {
            //TODO 等待客资内容写完继续写删除
        } catch (Exception e) {
            e.printStackTrace();
            return ResultInfoUtil.error(ExceptionEnum.UNKNOW_ERROR);
        }
        return ResultInfoUtil.success("交接成功");
    }

    /**
     * 获取电商邀约小组人员列表
     *
     * @return
     */
    @GetMapping("/change_list")
    public ResultInfo getChangeList() {
        //获取当前登录账户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        //获取操作用户所属公司
        Integer companyId = currentLoginStaff.getCompanyId();


        return ResultInfoUtil.success(TipMsgConstant.SUCCESS, staffService.getChangeList(companyId));
    }

    /**
     * 更新员工基础信息，不包含权限等
     *
     * @param staffPO
     * @return
     */
    @PostMapping("/update_base_info")
    public ResultInfo updateBaseInfo(@RequestBody @Valid StaffPO staffPO) {
        //对象参数trim
        ObjectUtil.objectStrParamTrim(staffPO);
        if (staffPO.getId() == 0) {
            return ResultInfoUtil.error(ExceptionEnum.STAFF_ID_NULL);
        }
        //获取当前登录用户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        //设置cid
        staffPO.setCompanyId(currentLoginStaff.getCompanyId());
        staffService.update(staffPO);
        return ResultInfoUtil.success(TipMsgConstant.SAVE_SUCCESS);
    }

    @PostMapping("/update_password")
    //todo 增加密码安全度校验
    public ResultInfo updatePassword(@Validated @RequestBody StaffPasswordDTO staffPasswordDTO) {
        //获取当前登录用户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        staffPasswordDTO.setId(currentLoginStaff.getId());
        staffPasswordDTO.setCompanyId(currentLoginStaff.getCompanyId());
        staffService.updatePassword(staffPasswordDTO);
        return ResultInfoUtil.success(TipMsgConstant.UPDATE_SUCCESS);
    }

    /**
     * 是否是原来正确的密码
     *
     * @param oldPassword
     * @return
     */
    @GetMapping("/right_password")
    public boolean rightPassword(@RequestParam("password") String oldPassword) {
        //获取当前登录用户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        return staffService.isRightPassword(currentLoginStaff.getId(),
                oldPassword, currentLoginStaff.getCompanyId());
    }
}
