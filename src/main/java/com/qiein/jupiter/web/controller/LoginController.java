package com.qiein.jupiter.web.controller;

import com.qiein.jupiter.aop.interceptor.TokenInterceptor;
import com.qiein.jupiter.aop.validate.annotation.NotEmptyStr;
import com.qiein.jupiter.constant.CommonConstant;
import com.qiein.jupiter.constant.RedisConstant;
import com.qiein.jupiter.exception.ExceptionEnum;
import com.qiein.jupiter.exception.RException;
import com.qiein.jupiter.util.*;
import com.qiein.jupiter.web.entity.dto.VerifyParamDTO;
import com.qiein.jupiter.web.entity.po.CompanyPO;
import com.qiein.jupiter.web.entity.po.StaffPO;
import com.qiein.jupiter.web.entity.vo.LoginUserVO;
import com.qiein.jupiter.web.service.LoginService;
import com.qiein.jupiter.web.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Author: shiTao
 */
@RestController
@RequestMapping("/login")
@Validated
public class LoginController extends BaseController {

    @Autowired
    private LoginService loginService;

    @Autowired
    private ValueOperations<String, String> valueOperations;

    @Autowired
    private TokenInterceptor tokenInterceptor;

    @Autowired
    private StaffService staffService;

    /**
     * 获取用户所在所有企业信息
     *
     * @param loginUserVO
     * @return
     */
    @PostMapping("/get_company_list_by_phone")
    public ResultInfo getCompanyListByPhone(@RequestBody @Validated LoginUserVO loginUserVO) {
        // 对象参数trim
        ObjectUtil.objectStrParamTrim(loginUserVO);
        // 校验用户验证码
        checkLoginVerifyCode(loginUserVO);
        try {
            // 返回结果
            List<CompanyPO> companyList = loginService.getCompanyListByPhone(loginUserVO.getUserName(),
                    loginUserVO.getPassword());
            return ResultInfoUtil.success(companyList);
        } catch (RException e) {
            // 将错误次数+1
            valueOperations.increment(RedisConstant.getUserLoginErrNumKey(loginUserVO.getUserName()),
                    CommonConstant.LOGIN_ERROR_ADD_NUM);
            return ResultInfoUtil.error(e.getCode(), e.getMsg());
        }
    }

    /**
     * 校验用户验证码
     *
     * @param loginUserVO
     */
    private void checkLoginVerifyCode(LoginUserVO loginUserVO) {
        // 对象参数trim
        ObjectUtil.objectStrParamTrim(loginUserVO);
        String userName = loginUserVO.getUserName();
        String verifyCode = loginUserVO.getVerifyCode();
        // 判断是否需要验证码以及验证码正确性
        if (needVerityCode(userName)) {
            // 验证码为空
            if (StringUtil.isEmpty(verifyCode)) {
                throw new RException(ExceptionEnum.VERIFY_NULL);
            } else {
                // 从缓存获取key并判断
                String verifyCodeTrue = valueOperations.get(RedisConstant.getVerifyCodeKey(userName));
                if (!StringUtil.ignoreCaseEqual(verifyCode, verifyCodeTrue)) {
                    // 验证码错误
                    throw new RException(ExceptionEnum.VERIFY_ERROR);
                }
            }
        }
    }

    /**
     * 请求验证码
     *
     * @param response
     */
    @GetMapping("/verify_code")
    public void loginCode(HttpServletResponse response, @RequestParam("phone") String userName) {
        if (StringUtil.isEmpty(userName) || !RegexUtil.checkMobile(userName)) {
            return;
        }
        // 生成验证码并放入缓存
        String code = VerifyCodeUtil.execute(response);
        valueOperations.set(RedisConstant.getVerifyCodeKey(userName), code);
    }

    /**
     * 是否需要验证码
     *
     * @param userName 用户名
     */
    @GetMapping("/need_verity_code")
    public boolean needVerityCode(@NotEmptyStr @RequestParam("phone") String userName) {
        // 判断是否需要验证码
        String userLoginErrNum = valueOperations.get(RedisConstant.getUserLoginErrNumKey(userName));
        if (userLoginErrNum == null) {
            // 如果没有查询到，说明是第一次，设置默认值0,过期时间为1小时
            valueOperations.set(RedisConstant.getUserLoginErrNumKey(userName),
                    String.valueOf(CommonConstant.DEFAULT_ZERO), CommonConstant.LOGIN_ERROR_NUM_EXPIRE_TIME,
                    TimeUnit.HOURS);
            return false;
        } else {
            // 是否大于允许的错误最大值
            int errNum = Integer.valueOf(userLoginErrNum);
            return errNum >= CommonConstant.ALLOW_USER_LOGIN_ERR_NUM;
        }
    }

    /**
     * 根据公司ID登录
     *
     * @param loginUserVO
     * @return
     */
    @PostMapping("/login_by_phone")
    public ResultInfo loginWithCompanyIdByPhone(@RequestBody @Validated LoginUserVO loginUserVO) {
        // 对象参数trim
        ObjectUtil.objectStrParamTrim(loginUserVO);
        // 校验用户验证码
        checkLoginVerifyCode(loginUserVO);
        String userName = loginUserVO.getUserName();
        String password = loginUserVO.getPassword();
        // 校验公司Id
        if (loginUserVO.getCompanyId() == 0) {
            throw new RException(ExceptionEnum.COMPANYID_NULL);
        }
        try {
            // 返回结果
            StaffPO staffPO = loginService.loginWithCompanyIdByPhone(
                    userName, password, loginUserVO.getCompanyId(), getIp());
            return ResultInfoUtil.success(staffPO);
        } catch (RException e) {
            // 登录失败，将错误次数+1
            valueOperations.increment(RedisConstant.getUserLoginErrNumKey(userName), 1);
            return ResultInfoUtil.error(e.getCode(), e.getMsg());
        }
    }


    /**
     * 微信登录 获取公司列表
     */
    @GetMapping("/get_company_list_by_wechat")
    public ResultInfo weChatGetCompanyList(String code) {
        return ResultInfoUtil.success(loginService.getCompanyListByWeChat(code));
    }

    /**
     * 微信登录
     */
    @GetMapping("/login_by_wechat")
    public ResultInfo weChatLogin(String code, int cid) {
        return ResultInfoUtil.success(loginService.loginWithCompanyIdByWeChat(code, cid, getIp()));
    }

    /**
     * 首页获取基础信息
     */
    @GetMapping("/base_info")
    public ResultInfo getBaseInfo() {
        // 获取当前登录账户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        return ResultInfoUtil
                .success(loginService.getBaseInfo(currentLoginStaff.getId(), currentLoginStaff.getCompanyId()));
    }

    /**
     * 钉钉 获取公司列表
     */
    @GetMapping("/get_company_list_by_ding")
    public ResultInfo dingGetCompanyList(String code) {
        return ResultInfoUtil.success(loginService.getCompanyListByDing(code));
    }

    /**
     * 钉钉登录
     */
    @GetMapping("/login_by_ding")
    public ResultInfo dingLogin(String code, int cid) {
        return ResultInfoUtil.success(loginService.loginWithCompanyIdByDing(code, cid, getIp()));
    }

    /**
     * 校验token
     *
     * @return
     */
    @GetMapping("/check_token")
    public ResultInfo checkToken(HttpServletRequest request) {
        VerifyParamDTO requestToken = HttpUtil.getRequestToken(request);
        StaffPO staff = staffService.getById(requestToken.getUid(), requestToken.getCid());
        //校验用户信息
        tokenInterceptor.checkTokenUserInfo(requestToken, staff);
        return ResultInfoUtil.success(true);
    }


}
