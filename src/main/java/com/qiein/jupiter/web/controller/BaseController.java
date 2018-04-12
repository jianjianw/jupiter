package com.qiein.jupiter.web.controller;

import com.qiein.jupiter.constant.CommonConstant;
import com.qiein.jupiter.web.entity.po.StaffPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 基础Controller
 */
@RestController
public class BaseController {

    @Autowired
    private HttpServletRequest request;


    /**
     * 获取session参数
     *
     * @param attrName
     * @return
     */
    Object getSessionAttr(String attrName) {
        return request.getSession().getAttribute(attrName);
    }

    /**
     * 设置Session参数
     *
     * @param attrName
     * @param obj
     */
    void setSessionAttr(String attrName, Object obj) {
        request.getSession().setAttribute(attrName, obj);
    }

    /**
     * 获取当前登录用户
     *
     * @return
     */
    StaffPO getCurrentLoginStaff() {
        return (StaffPO) request.getAttribute(CommonConstant.CURRENT_LOGIN_STAFF);
    }

}