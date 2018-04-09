package com.qiein.jupiter.util;

import com.github.pagehelper.util.StringUtil;

import javax.servlet.http.HttpServletRequest;

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
}
