package com.qiein.jupiter.aop.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.qiein.jupiter.constant.CommonConstants;
import com.qiein.jupiter.constant.RedisConstants;
import com.qiein.jupiter.exception.ExceptionEnum;
import com.qiein.jupiter.exception.RException;
import com.qiein.jupiter.util.HttpUtil;
import com.qiein.jupiter.util.JwtUtil;
import com.qiein.jupiter.util.StringUtil;
import com.qiein.jupiter.web.entity.dto.VerifyParamDTO;
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
        if (CommonConstants.DEV.equalsIgnoreCase(active)) {
            httpServletRequest.setAttribute(CommonConstants.VERIFY_PARAM, HttpUtil.getRequestToken(httpServletRequest));
            return true;
        }
        //获取请求token
        VerifyParamDTO token = HttpUtil.getRequestToken(httpServletRequest);
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
     * 检测redis token方案
     *
     * @param verifyParamDTO     token
     * @param httpServletRequest request
     */
    private void checkRedisToken(VerifyParamDTO verifyParamDTO, HttpServletRequest httpServletRequest) {
        String tokenCache = valueOperations.get(RedisConstants.getUserToken(verifyParamDTO.getUid(), verifyParamDTO.getCid()));
        //如果缓存中不存在
        if (StringUtil.isNullStr(tokenCache)) {
            throw new RException(ExceptionEnum.TOKEN_INVALID);
        }
        //验证token是否相等
        if (!StringUtil.ignoreCaseEqual(verifyParamDTO.getToken(), tokenCache)) {
            throw new RException(ExceptionEnum.TOKEN_VERIFY_FAIL);
        }
        //将uid 和 cid 放入request
        httpServletRequest.setAttribute(CommonConstants.VERIFY_PARAM, verifyParamDTO);
    }

    /**
     * 检测jwt方案
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
        httpServletRequest.setAttribute(CommonConstants.JWT_BODY, jsonObject);
    }
}
