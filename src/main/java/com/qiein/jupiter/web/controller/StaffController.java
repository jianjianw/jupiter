package com.qiein.jupiter.web.controller;

import com.qiein.jupiter.constant.CommonConstants;
import com.qiein.jupiter.constant.RedisConstants;
import com.qiein.jupiter.exception.ExceptionEnum;
import com.qiein.jupiter.exception.RRException;
import com.qiein.jupiter.util.ResultInfo;
import com.qiein.jupiter.util.ResultInfoUtil;
import com.qiein.jupiter.util.StringUtil;
import com.qiein.jupiter.util.VerifyCodeUtil;
import com.qiein.jupiter.web.entity.po.StaffPO;
import com.qiein.jupiter.web.entity.vo.LoginUserVO;
import com.qiein.jupiter.web.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
        //校验用户名密码
        String userName = loginUserVO.getUserName();
        String password = loginUserVO.getPassword();
        String verifyCode = loginUserVO.getVerifyCode();
        if (null == userName) {
            throw new RRException(ExceptionEnum.USERNAME_NULL);
        } else {
            userName = StringUtil.nullToStrTrim(userName);
        }
        if (null == password) {
            throw new RRException(ExceptionEnum.PASSWORD_NULL);
        } else {
            password = StringUtil.nullToStrTrim(password);
        }
        //判断是否需要验证码以及验证码正确性
        if (needVerityCode(request, userName)) {
            //验证码为空
            if (null == verifyCode) {
                throw new RRException(ExceptionEnum.VERIFY_NULL);
            } else {
                String verifyCodeTrue = (String) getSessionAttr(request, CommonConstants.USER_VERIFY_CODE);
                if (!verifyCode.trim().equals(verifyCodeTrue)) {
                    //验证码错误
                    throw new RRException(ExceptionEnum.VERIFY_ERROR);
                }
            }
        }
        //返回结果
        try {
            return ResultInfoUtil.success(staffService.Login(userName, password, 1));
        } catch (RRException e) {
            //将session中的错误次数+1
            Integer errNum = (Integer) getSessionAttr(request, CommonConstants.USER_LOGIN_ERR_NUM);
            setSessionAttr(request, CommonConstants.USER_LOGIN_ERR_NUM, errNum + 1);
            return ResultInfoUtil.error(e.getCode(), e.getMsg());
        }
    }

    /**
     * 是否需要验证码
     *
     * @param userName 用户名
     */
    @GetMapping("/need_verity_code")
    public boolean needVerityCode(HttpServletRequest request, String userName) {
        //判断是否需要验证码
        Integer errNum = (Integer) getSessionAttr(request, CommonConstants.USER_LOGIN_ERR_NUM);
//        String userLoginErrNum =valueOperations.get(RedisConstants.getVerifyCode(userName));
        if (null == errNum) {
            //如果没有查询到，说明是第一次，设置默认值
            setSessionAttr(request, CommonConstants.USER_LOGIN_ERR_NUM, 0);
            return false;
        } else return errNum >= CommonConstants.ALLOW_USER_LOGIN_ERR_NUM;
    }


    /**
     * 请求验证码
     *
     * @param response
     */
    @GetMapping("/verify_code")
    public void loginCode(HttpServletResponse response, String userName) {
        String code = VerifyCodeUtil.execute(response);
        valueOperations.set(userName, code);
    }
}
