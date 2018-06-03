package com.qiein.jupiter.aop.interceptor;

import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.qiein.jupiter.constant.CommonConstant;
import com.qiein.jupiter.constant.RedisConstant;
import com.qiein.jupiter.exception.ExceptionEnum;
import com.qiein.jupiter.exception.RException;
import com.qiein.jupiter.util.HttpUtil;
import com.qiein.jupiter.util.StringUtil;
import com.qiein.jupiter.web.entity.dto.RequestInfoDTO;
import com.qiein.jupiter.web.entity.dto.VerifyParamDTO;
import com.qiein.jupiter.web.entity.po.StaffPO;
import com.qiein.jupiter.web.service.StaffService;

/**
 * token拦截器
 */
@Component
public class TokenInterceptor implements HandlerInterceptor {
	// private final Logger logger = LoggerFactory.getLogger(getClass());

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
     */
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) {
        // 获取校验参数
        VerifyParamDTO requestToken = HttpUtil.getRequestToken(httpServletRequest);
        // 如果是dev环境，不验证
        // if (CommonConstant.DEV.equalsIgnoreCase(active)) {
        // httpServletRequest.setAttribute(CommonConstant.VERIFY_PARAM,
        // requestToken);
        // return true;
        // }
        // 检测ip
        checkIp(httpServletRequest);
        // 从redis中获取token并验证
        return checkRedisToken(requestToken, httpServletRequest);
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o,
                           ModelAndView modelAndView) {
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                Object o, Exception e) {
    }

    /**
     * 检测redis token方案
     *
     * @param verifyParamDTO     token
     * @param httpServletRequest request
     */
    private boolean checkRedisToken(VerifyParamDTO verifyParamDTO, HttpServletRequest httpServletRequest) {
        // 根据uid cid 从缓存中获取当前登录用户
        String userTokenKey = RedisConstant.getStaffKey(verifyParamDTO.getUid(), verifyParamDTO.getCid());
        //校验TOKEN 如果传过去的staff 为空，则从数据库找到并赋值
        StaffPO staffPO = (StaffPO) redisTemplate.opsForValue().get(userTokenKey);
        if (staffPO == null) {
            staffPO = staffService.getById(verifyParamDTO.getUid(), verifyParamDTO.getCid());
        }
        //校验用户信息 比对
        checkTokenUserInfo(verifyParamDTO, staffPO);
        // 验证成功，更新过期时间
        redisTemplate.opsForValue().set(userTokenKey, staffPO, CommonConstant.DEFAULT_EXPIRE_TIME, TimeUnit.HOURS);
        // 将 当前登录用户 放入request
        httpServletRequest.setAttribute(CommonConstant.CURRENT_LOGIN_STAFF, staffPO);
        return true;
    }

    // 校验Ip
    private void checkIp(HttpServletRequest request) {
        String ipAddr = HttpUtil.getIpAddr(request);
        RequestInfoDTO requestInfoDTO = new RequestInfoDTO();
        // 设置请求值
        requestInfoDTO.setUrl(request.getRequestURI());
        requestInfoDTO.setIp(ipAddr);
        //todo 参数
        //放入attr
        request.setAttribute(CommonConstant.REQUEST_INFO, requestInfoDTO);
//        logger.info("访问ip:" + ipAddr);
    }

    /**
     * 校验token
     *
     * @param verifyParamDTO 要校验的对象
     * @param staffPO        要比对的对象，从redis 或数据库找到的
     * @return
     */
    public void checkTokenUserInfo(VerifyParamDTO verifyParamDTO, StaffPO staffPO) {
        // 如果缓存中命中失败,从数据库获取用户信息
        if (staffPO == null) {
            // 验证用户不存在
            throw new RException(ExceptionEnum.VERIFY_USER_NOT_FOUND);
        } else if (StringUtil.isEmpty(staffPO.getToken())) {
            // 如果用户当前没有token，说明没有登录或token过期
            throw new RException(ExceptionEnum.TOKEN_INVALID);
        }
        // 验证是否被锁定
        if (staffPO.isLockFlag()) {
            throw new RException(ExceptionEnum.USER_IS_LOCK);
        }
        // 验证token是否相等
        if (!StringUtil.ignoreCaseEqual(verifyParamDTO.getToken(), staffPO.getToken())) {
            throw new RException(ExceptionEnum.TOKEN_VERIFY_FAIL);
        }
    }
}
