package com.qiein.jupiter.web.controller;

import javax.servlet.http.HttpServletRequest;

import com.qiein.jupiter.util.*;
import com.qiein.jupiter.web.service.ApolloService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qiein.jupiter.enums.TipMsgEnum;
import com.qiein.jupiter.exception.ExceptionEnum;
import com.qiein.jupiter.exception.RException;

/**
 * apollo SOA 接口
 *
 * @author JingChenglong 2018/05/23 14:13
 */
@RestController
@RequestMapping("/apollo")
public class ApolloController extends BaseController {

    @Autowired
    private ApolloService apolloService;

    /**
     * 心跳检测
     *
     * @return
     */
    @PostMapping("/heart")
    public ResultInfo heart(HttpServletRequest request) {
        int companyId = 0;
        String accessKey = "";
        String sign = "";

        checkAccessKey(companyId, accessKey);
        checkSign(companyId, accessKey, sign);

        return ResultInfoUtil.success(TipMsgEnum.EDIT_SUCCESS);
    }

    // lock

    // get_staff_statistics

    // get_clientinfo_statistics

    public static void checkAccessKey(int companyId, String accessKey) {
        if (NumUtil.isInValid(companyId) || StringUtil.isEmpty(accessKey)) {
            throw new RException(ExceptionEnum.ACCESSKEY_ERROR);
        }

        boolean checkAccesskey = accessKey.equalsIgnoreCase(MD5Util.getApolloMd5(String.valueOf(companyId)));
        if (!checkAccesskey) {
            throw new RException(ExceptionEnum.ACCESSKEY_ERROR);
        }
    }

    public static void checkSign(int companyId, String accessKey, String sign) {
        if (StringUtils.isEmpty(sign) || !sign.equalsIgnoreCase(MD5Util.getMD5(companyId + accessKey))) {
            throw new RException(ExceptionEnum.SIGN_ERROR);
        }
    }

    /**
     * 根据用户Uid cid获取
     *
     * @return
     */
    @GetMapping("/get_user_info")
    public ResultInfo getUserInfoByUidCid(HttpServletRequest request, int companyId, int staffId) {
        return ResultInfoUtil.success(apolloService.getUserVerifyInfoByUidCid(companyId, staffId, HttpUtil.getIpAddr(request)));
    }
}