package com.qiein.jupiter.aop.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.qiein.jupiter.constant.CommonConstant;
import com.qiein.jupiter.constant.NumberConstant;
import com.qiein.jupiter.constant.RedisConstant;
import com.qiein.jupiter.exception.ExceptionEnum;
import com.qiein.jupiter.exception.RException;
import com.qiein.jupiter.util.HttpUtil;
import com.qiein.jupiter.util.JwtUtil;
import com.qiein.jupiter.util.StringUtil;
import com.qiein.jupiter.web.entity.dto.VerifyParamDTO;
import com.qiein.jupiter.web.entity.po.StaffPO;
import com.qiein.jupiter.web.service.StaffService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

/**
 * token拦截器
 */
@Component
public class TokenInterceptor implements HandlerInterceptor {
    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 当前运行环境
     */
    @Value("${spring.profiles.active}")
    private String active;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private StaffService staffService;

    /**
     * 前置拦截器
     *
     * @param httpServletRequest
     * @param httpServletResponse
     * @param o
     * @return
     */
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) {
        //获取校验参数
        VerifyParamDTO requestToken = HttpUtil.getRequestToken(httpServletRequest);
        //如果是dev环境，不验证
//        if (CommonConstant.DEV.equalsIgnoreCase(active)) {
//            httpServletRequest.setAttribute(CommonConstant.VERIFY_PARAM, requestToken);
//            return true;
//        }
        //检测ip
        chckIp(httpServletRequest);
        //从redis中获取token并验证
        return checkRedisToken(requestToken, httpServletRequest);
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object
            o, ModelAndView modelAndView) {
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse
            httpServletResponse, Object o, Exception e) {
    }


    /**
     * 检测redis token方案
     *
     * @param verifyParamDTO     token
     * @param httpServletRequest request
     */
    private boolean checkRedisToken(VerifyParamDTO verifyParamDTO, HttpServletRequest httpServletRequest) {
        //根据uid cid 从缓存中获取当前登录用户
        String userTokenKey = RedisConstant.getStaffKey(verifyParamDTO.getUid(), verifyParamDTO.getCid());
        StaffPO staffPO = (StaffPO) redisTemplate.opsForValue().get(userTokenKey);
        //如果缓存中命中失败,从数据库获取用户信息
        if (staffPO == null) {
            staffPO = staffService.getById(verifyParamDTO.getUid(), verifyParamDTO.getCid());
            if (staffPO == null) {
                //验证用户不存在
                throw new RException(ExceptionEnum.VERIFY_USER_NOT_FOUND);
            } else if (StringUtil.isNullStr(staffPO.getToken())) {
                //如果用户当前没有token，说明没有登录或token过期
                throw new RException(ExceptionEnum.TOKEN_INVALID);
            }
        }
        //验证是否被锁定
        if (staffPO.isLockFlag()) {
            throw new RException(ExceptionEnum.USER_IS_LOCK);
        }
        //验证token是否相等
        if (!StringUtil.ignoreCaseEqual(verifyParamDTO.getToken(), staffPO.getToken())) {
            throw new RException(ExceptionEnum.TOKEN_VERIFY_FAIL);
        }
        //验证成功，更新过期时间
        redisTemplate.opsForValue().set(userTokenKey, staffPO, NumberConstant.DEFAULT_EXPIRE_TIME, TimeUnit.HOURS);
        //将 当前登录用户 放入request
        httpServletRequest.setAttribute(CommonConstant.CURRENT_LOGIN_STAFF, staffPO);
        return true;
    }

    /**
     * 检测jwt方案  不完整
     *
     * @param verifyParamDTO     参数
     * @param httpServletRequest request
     */
    private void checkJwt(VerifyParamDTO verifyParamDTO, HttpServletRequest httpServletRequest) {
        String jwt;
        try {
            jwt = JwtUtil.decrypt(verifyParamDTO.getToken());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RException(ExceptionEnum.TOKEN_VERIFY_FAIL);
        }
        //token失效
        JSONObject jsonObject = JSONObject.parseObject(jwt, JSONObject.class);
        if (jsonObject == null) {
            throw new RException(ExceptionEnum.TOKEN_INVALID);
        }
        //将jwt body放入request
        httpServletRequest.setAttribute(CommonConstant.JWT_BODY, jsonObject);
    }

    //校验Ip
    private void chckIp(HttpServletRequest request) {
        String ipAddr = HttpUtil.getIpAddr(request);
        logger.info("访问ip:" + ipAddr);
    }
}
