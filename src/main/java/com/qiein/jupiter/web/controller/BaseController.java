package com.qiein.jupiter.web.controller;

import com.qiein.jupiter.constant.CommonConstant;
import com.qiein.jupiter.exception.ExceptionEnum;
import com.qiein.jupiter.exception.RException;
import com.qiein.jupiter.util.HttpUtil;
import com.qiein.jupiter.web.entity.dto.RequestInfoDTO;
import com.qiein.jupiter.web.entity.po.StaffPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 基础Controller
 */
@RestController
public class BaseController {

    @Autowired
    private HttpServletRequest request;

    /**
     * 系统启动
     *
     * @return
     */
    @RequestMapping("/")
    public String initSys() {
        return "jupiter is running success!";
    }

    /**
     * 获取当前登录IP
     */
    @GetMapping("/ip")
    String getIp() {
        return HttpUtil.getIpAddr(request);
    }

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
        StaffPO attribute = (StaffPO) request.getAttribute(CommonConstant.CURRENT_LOGIN_STAFF);
        if (attribute == null) {
            throw new RException(ExceptionEnum.CAN_NOT_FIND_USER_FROM_REQ);
        }
        return attribute;
    }

    /**
     * 获取当前登录信息
     */
    RequestInfoDTO getRequestInfo() {
        return (RequestInfoDTO) request.getAttribute(CommonConstant.REQUEST_INFO);
    }

}
