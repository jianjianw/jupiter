package com.qiein.jupiter.web.controller;

import com.qiein.jupiter.constant.CommonConstants;
import com.qiein.jupiter.constant.RedisConstants;
import com.qiein.jupiter.exception.ExceptionEnum;
import com.qiein.jupiter.exception.RException;
import com.qiein.jupiter.util.*;
import com.qiein.jupiter.web.entity.vo.LoginUserVO;
import com.qiein.jupiter.web.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

/**
 * 员工 Controller
 */
@RestController
@RequestMapping("/staff")
public class StaffController extends BaseController {

    @Autowired
    private StaffService staffService;

    @Autowired
    private ValueOperations<String, String> valueOperations;

    @GetMapping("/all")
    public ResultInfo getAll() {
        return ResultInfoUtil.success(staffService.findList(null));
    }


    /**
     * 用户登录
     *
     * @param request
     * @param loginUserVO
     * @return
     */
    @PostMapping("/login")
    public ResultInfo login(HttpServletRequest request, @RequestBody LoginUserVO loginUserVO) {
        String userName = loginUserVO.getUserName();
        String password = loginUserVO.getPassword();
        String verifyCode = loginUserVO.getVerifyCode();
        //校验用户名
        if (null == userName) {
            //用户名空
            throw new RException(ExceptionEnum.USERNAME_NULL);
        } else {
            userName = StringUtil.nullToStrTrim(userName);
            //手机号码格式错误
            if (!RegexUtils.checkMobile(userName)) {
                throw new RException(ExceptionEnum.PHONE_FOMAT_ERROR);
            }
        }
        //校验密码
        if (null == password) {
            //密码空
            throw new RException(ExceptionEnum.PASSWORD_NULL);
        } else {
            password = StringUtil.nullToStrTrim(password);
        }
        //判断是否需要验证码以及验证码正确性
        if (needVerityCode(userName)) {
            //验证码为空
            if (null == verifyCode) {
                throw new RException(ExceptionEnum.VERIFY_NULL);
            } else {
                String verifyCodeTrue = RedisConstants.getVerifyCode(userName);
                if (!verifyCode.trim().equals(verifyCodeTrue)) {
                    //验证码错误
                    throw new RException(ExceptionEnum.VERIFY_ERROR);
                }
            }
        }
        //返回结果
        try {
            return ResultInfoUtil.success(staffService.Login(userName, password, 1));
        } catch (RException e) {
            //将错误次数+1
            valueOperations.increment(RedisConstants.getUserLoginErrNum(userName), 1);
            return ResultInfoUtil.error(e.getCode(), e.getMsg());
        }
    }

    /**
     * 是否需要验证码
     *
     * @param userName 用户名
     */
    @GetMapping("/need_verity_code")
    public boolean needVerityCode(String userName) {
        //判断是否需要验证码
        String userLoginErrNum = valueOperations.get(RedisConstants.getUserLoginErrNum(userName));
        if (null == userLoginErrNum) {
            //如果没有查询到，说明是第一次，设置默认值0,过期时间为1小时
            valueOperations.set(RedisConstants.getUserLoginErrNum(userName), "0", 1, TimeUnit.HOURS);
            return false;
        } else {
            //是否大于允许的错误最大值
            int errNum = Integer.valueOf(userLoginErrNum);
            return errNum >= CommonConstants.ALLOW_USER_LOGIN_ERR_NUM;
        }
    }


    /**
     * 请求验证码
     *
     * @param response
     */
    @GetMapping("/verify_code")
    public void loginCode(HttpServletResponse response, String userName) {
        if (null == userName || !RegexUtils.checkMobile(userName)) {
            return;
        }
        String code = VerifyCodeUtil.execute(response);
        valueOperations.set(RedisConstants.getVerifyCode(userName), code);
    }
}
