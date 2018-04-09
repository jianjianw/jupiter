package com.qiein.jupiter.web.controller;

import com.qiein.jupiter.constant.CommonConstants;
import com.qiein.jupiter.util.HttpUtil;
import com.qiein.jupiter.web.entity.dto.VerifyParamDTO;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 基础Controller
 */
@RestController
public class BaseController {

    /**
     * 获取session参数
     *
     * @param request
     * @param attrName
     * @return
     */
    Object getSessionAttr(HttpServletRequest request, String attrName) {
        return request.getSession().getAttribute(attrName);
    }

    /**
     * 设置Session参数
     *
     * @param request
     * @param attrName
     * @param obj
     */
    void setSessionAttr(HttpServletRequest request, String attrName, Object obj) {
        request.getSession().setAttribute(attrName, obj);
    }

    /**
     * 获取用户验证参数
     *
     * @param request
     * @return
     */
    VerifyParamDTO getVerifyParam(HttpServletRequest request) {
        return (VerifyParamDTO) request.getAttribute(CommonConstants.VERIFY_PARAM);
    }

}
