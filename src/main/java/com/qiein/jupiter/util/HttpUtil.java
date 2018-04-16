package com.qiein.jupiter.util;

import com.alibaba.druid.util.StringUtils;
import com.github.pagehelper.util.StringUtil;
import com.qiein.jupiter.constant.CommonConstant;
import com.qiein.jupiter.exception.ExceptionEnum;
import com.qiein.jupiter.exception.RException;
import com.qiein.jupiter.web.entity.dto.VerifyParamDTO;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * http工具类
 */
public class HttpUtil {

    /**
     * 从request中获取参数
     *
     * @param param
     * @return
     */
    public static String getRequestParam(HttpServletRequest httpRequest, String param) {
        //从header中获取参数
        String rParam = httpRequest.getHeader(param);
        //如果header中不存在，则从参数中获取
        if (StringUtil.isEmpty(rParam)) {
            rParam = httpRequest.getParameter(param);
        }
        return rParam;
    }

    /**
     * 获取请求的 验证参数
     */
    public static VerifyParamDTO getRequestToken(HttpServletRequest request) {
        String token = HttpUtil.getRequestParam(request, CommonConstant.TOKEN);
        String uid = HttpUtil.getRequestParam(request, CommonConstant.UID);
        String cid = HttpUtil.getRequestParam(request, CommonConstant.CID);
        //验证参数不全
        if (com.qiein.jupiter.util.StringUtil.isNullStr(token)
                || com.qiein.jupiter.util.StringUtil.isNullStr(uid)
                || com.qiein.jupiter.util.StringUtil.isNullStr(cid)) {
            throw new RException(ExceptionEnum.VERIFY_PARAM_INCOMPLETE);
        }
        //封装验证参数
        VerifyParamDTO verifyParamDTO = new VerifyParamDTO();
        verifyParamDTO.setToken(token);
        verifyParamDTO.setCid(Integer.valueOf(cid));
        verifyParamDTO.setUid(Integer.valueOf(uid));
        return verifyParamDTO;
    }

    /**
     * 获取http
     *
     * @return
     */
    public static HttpServletRequest getHttpServletRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }

    /**
     * 获取IP地址
     * <p>
     * 使用Nginx等反向代理软件， 则不能通过request.getRemoteAddr()获取IP地址
     * 如果使用了多级反向代理的话，X-Forwarded-For的值并不止一个，而是一串IP地址，X-Forwarded-For中第一个非unknown的有效IP字符串，则为真实IP地址
     */
    public static String getIpAddr(HttpServletRequest request) {
        String ip = null;
        try {
            ip = request.getHeader("x-forwarded-for");
            if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
            }
            if (StringUtils.isEmpty(ip) || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
            }
            if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_CLIENT_IP");
            }
            if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            }
            if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

//        //使用代理，则获取第一个IP地址
//        if(StringUtils.isEmpty(ip) && ip.length() > 15) {
//			if(ip.indexOf(",") > 0) {
//				ip = ip.substring(0, ip.indexOf(","));
//			}
//		}

        return ip;
    }

    /**
     * 校验是否是合格IP地址
     * @param ip
     * @return
     */
    public static boolean isIp(String ip) {
        Pattern p = Pattern.compile("^((25[0-5]|2[0-4]\\d|[1]\\d\\d|[1-9]\\d|\\d)($|(?!\\.$)\\.)){4}$");
        return p.matcher(ip).matches();
    }
}
