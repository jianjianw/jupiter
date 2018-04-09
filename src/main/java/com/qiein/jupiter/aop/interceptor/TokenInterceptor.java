package com.qiein.jupiter.aop.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.qiein.jupiter.constant.CommonConstants;
import com.qiein.jupiter.constant.RedisConstants;
import com.qiein.jupiter.exception.ExceptionEnum;
import com.qiein.jupiter.exception.RException;
import com.qiein.jupiter.util.HttpUtil;
import com.qiein.jupiter.util.JwtUtil;
import com.qiein.jupiter.util.StringUtil;
import com.qiein.jupiter.web.entity.dto.VerifyParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * token拦截器
 */
@Component
public class TokenInterceptor implements HandlerInterceptor {
    /**
     * 当前运行环境
     */
    @Value("${spring.profiles.active}")
    private String active;

    @Autowired
    private ValueOperations<String, String> valueOperations;

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
        //如果是dev环境，不拦截
//        if (CommonConstants.DEV.equalsIgnoreCase(active)) {
//            return true;
//        }
        //获取请求token
        VerifyParam token = getRequestToken(httpServletRequest);
        //从redis中获取token并检测
        checkRedisToken(token, httpServletRequest);
        return true;
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
     * 获取请求的 验证参数
     */
    private VerifyParam getRequestToken(HttpServletRequest request) {
        String token = HttpUtil.getRequestParam(request, CommonConstants.TOKEN);
        String uid = HttpUtil.getRequestParam(request, CommonConstants.UID);
        String cid = HttpUtil.getRequestParam(request, CommonConstants.CID);
        //验证参数不全
        if (StringUtil.isNullStr(token) || StringUtil.isNullStr(uid) || StringUtil.isNullStr(cid)) {
            throw new RException(ExceptionEnum.VERIFY_PARAM_INCOMPLETE);
        }
        //封装验证参数
        VerifyParam verifyParam = new VerifyParam();
        verifyParam.setToken(token);
        verifyParam.setCid(Integer.valueOf(cid));
        verifyParam.setUid(Integer.valueOf(uid));
        return verifyParam;
    }


    /**
     * 检测redis token方案
     *
     * @param verifyParam        token
     * @param httpServletRequest request
     */
    private void checkRedisToken(VerifyParam verifyParam, HttpServletRequest httpServletRequest) {
        String tokenCache = valueOperations.get(RedisConstants.getUserToken(verifyParam.getUid(), verifyParam.getCid()));
        //如果缓存中不存在
        if (StringUtil.isNullStr(tokenCache)) {
            throw new RException(ExceptionEnum.TOKEN_INVALID);
        }
        //验证token是否相等
        if (!StringUtil.ignoreCaseEqual(verifyParam.getToken(), tokenCache)) {
            throw new RException(ExceptionEnum.TOKEN_VERIFY_FAIL);
        }
        //将uid 和 cid 放入request
        httpServletRequest.setAttribute(CommonConstants.UID, verifyParam.getUid());
        httpServletRequest.setAttribute(CommonConstants.CID, verifyParam.getCid());
    }

    /**
     * 检测jwt方案
     *
     * @param verifyParam        参数
     * @param httpServletRequest request
     */
    private void checkJwt(VerifyParam verifyParam, HttpServletRequest httpServletRequest) {
        String jwt;
        try {
            jwt = JwtUtil.decrypt(verifyParam.getToken());
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
        httpServletRequest.setAttribute(CommonConstants.JWT_BODY, jsonObject);
    }
}
