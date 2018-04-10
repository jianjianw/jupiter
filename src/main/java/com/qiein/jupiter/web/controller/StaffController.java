package com.qiein.jupiter.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.qiein.jupiter.aop.annotation.LoginLog;
import com.qiein.jupiter.aop.annotation.NotEmpty;
import com.qiein.jupiter.constant.CommonConstants;
import com.qiein.jupiter.constant.RedisConstants;
import com.qiein.jupiter.exception.ExceptionEnum;
import com.qiein.jupiter.exception.RException;
import com.qiein.jupiter.util.*;
import com.qiein.jupiter.web.entity.dto.QueryMapDTO;
import com.qiein.jupiter.web.entity.dto.VerifyParamDTO;
import com.qiein.jupiter.web.entity.po.CompanyPO;
import com.qiein.jupiter.web.entity.po.StaffPO;
import com.qiein.jupiter.web.entity.vo.LoginUserVO;
import com.qiein.jupiter.web.service.CompanyService;
import com.qiein.jupiter.web.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
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

    @PostMapping("/findList")
    public ResultInfo getAll(@RequestBody QueryMapDTO queryMapDTO) {
        return ResultInfoUtil.success(staffService.findList(queryMapDTO));
    }


    @PostMapping("/insert")
    @LoginLog
    public ResultInfo insert(@RequestBody @Validated StaffPO staffPO) {
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        //设置cid
        staffPO.setCompanyId(currentLoginStaff.getCompanyId());
        //对象参数trim
        ObjectUtil.objectStrParamTrim(staffPO);
        staffService.insert(staffPO);
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
            valueOperations.increment(RedisConstants.getUserLoginErrNumKey(loginUserVO.getUserName()), 1);
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
            valueOperations.increment(RedisConstants.getUserLoginErrNumKey(userName), 1);
            return ResultInfoUtil.error(e.getCode(), e.getMsg());
        }
    }

    /**
     * 是否需要验证码
     *
     * @param userName 用户名
     */
    @GetMapping("/need_verity_code")
    public boolean needVerityCode(@NotEmpty String userName) {
        //判断是否需要验证码
        String userLoginErrNum = valueOperations.get(RedisConstants.getUserLoginErrNumKey(userName));
        if (userLoginErrNum == null) {
            //如果没有查询到，说明是第一次，设置默认值0,过期时间为1小时
            valueOperations.set(
                    RedisConstants.getUserLoginErrNumKey(userName),
                    "0",
                    CommonConstants.LOGIN_ERROR_NUM_EXPIRE_TIME,
                    TimeUnit.HOURS);
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
    public void loginCode(HttpServletResponse response, @RequestParam("phone") String userName) {
        if (null == userName || !RegexUtils.checkMobile(userName)) {
            return;
        }
        //生成验证码并放入缓存
        String code = VerifyCodeUtil.execute(response);
        valueOperations.set(RedisConstants.getVerifyCodeKey(userName), code);
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
            if (verifyCode == null) {
                throw new RException(ExceptionEnum.VERIFY_NULL);
            } else {
                String verifyCodeTrue = RedisConstants.getVerifyCodeKey(userName);
                if (!verifyCode.trim().equals(verifyCodeTrue)) {
                    //验证码错误
                    throw new RException(ExceptionEnum.VERIFY_ERROR);
                }
            }
        }
    }

}
