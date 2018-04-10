package com.qiein.jupiter.util;

import com.github.pagehelper.util.StringUtil;
import com.qiein.jupiter.constant.CommonConstant;
import com.qiein.jupiter.exception.ExceptionEnum;
import com.qiein.jupiter.exception.RException;
import com.qiein.jupiter.web.entity.dto.VerifyParamDTO;

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
}
