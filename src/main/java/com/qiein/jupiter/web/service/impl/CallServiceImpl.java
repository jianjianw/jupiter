package com.qiein.jupiter.web.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.mzlion.easyokhttp.HttpClient;
import com.qiein.jupiter.constant.AppolloUrlConst;
import com.qiein.jupiter.constant.CallUrlConst;
import com.qiein.jupiter.constant.ClientConst;
import com.qiein.jupiter.constant.ClientLogConst;
import com.qiein.jupiter.exception.ExceptionEnum;
import com.qiein.jupiter.exception.RException;
import com.qiein.jupiter.util.*;
import com.qiein.jupiter.util.alicloud.BatchTokenRetrieval;
import com.qiein.jupiter.util.alicloud.RandomString;
import com.qiein.jupiter.web.dao.*;
import com.qiein.jupiter.web.entity.po.*;
import com.qiein.jupiter.web.entity.vo.AliOauthVO;
import com.qiein.jupiter.web.service.CallService;
import com.qiein.jupiter.web.service.StatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author: yyx
 * @Date: 2018-8-9
 */
@Service
public class CallServiceImpl implements CallService {
    @Value("${apollo.baseUrl}")
    private String appoloBaseUrl;
    @Autowired
    private StatusService statusService;
    @Autowired
    private ClientLogDao clientLogDao;
    @Autowired
    private ClientInfoDao clientInfoDao;
    @Autowired
    private CallCustomerDao callCustomerDao;
    @Autowired
    private StaffDao staffDao;


    @Override
    public void startBack2BackCall(String kzId,String caller, String callee, StaffPO staffPO) {
        if (StringUtil.isEmpty(caller) || StringUtil.isEmpty(callee)) {
            throw new RException(ExceptionEnum.CALLER_OR_CALLEE_IS_NULL);
        }
        //验证手机号是否绑定了客服
        CallCustomerPO callCustomer = callCustomerDao.getCallCustomerByStaffIdAndCompanyId(staffPO.getId(), staffPO.getCompanyId());
        if(null == callCustomer){
            throw new RException(ExceptionEnum.NOT_FOUND_BIND_USER);
        }

        String sign = MD5Util.getApolloMd5(String.valueOf(staffPO.getCompanyId()));
        //Appollo接口获取用户信息
        String usreJson = HttpClient
                // 请求方式和请求url
                .get(appoloBaseUrl.concat(AppolloUrlConst.GET_CALL_USER))
                // post提交json
                .queryString("companyId", staffPO.getCompanyId())
                .queryString("sign", sign)
                .asString();

        //Appollo接口获取intsanceId
        String instaceJson = HttpClient
                // 请求方式和请求url
                .get(appoloBaseUrl.concat(AppolloUrlConst.GET_CALL_INSTANCE))
                // post提交json
                .queryString("companyId", staffPO.getCompanyId())
                .queryString("sign", sign)
                .asString();
        CallPO callPO = JSONObject.parseObject(JSONObject.parseObject(instaceJson).get("data").toString(), CallPO.class);
        CallUserPO callUserPO = JSONObject.parseObject(JSONObject.parseObject(usreJson).get("data").toString(), CallUserPO.class);
        //获取Token
        String token = BatchTokenRetrieval.retrieve(callUserPO.getUsername(), callUserPO.getPassword(), callUserPO.getClientId(), callUserPO.getClientSecret(), callUserPO.getCallBakUrl());
        AliOauthVO aliOauthVO = JSONObject.parseObject(token, AliOauthVO.class);
        //生成时间戳
        StringBuilder timeStamp = new StringBuilder(TimeUtil.format(TimeUtil.localToUTC(TimeUtil.getSysTime()), "yyyy-MM-dd HH:mm:ss")).replace(10, 11, "T").append("Z");
        RandomString randomString = new RandomString(45);
        HttpClient
                .get(CallUrlConst.CALL_BASE_URL)
                .queryString("Action", "StartBack2BackCall")
                .queryString("InstanceId", callPO.getInstanceId())
                .queryString("CallCenterNumber", callPO.getPhoneNumber())
                .queryString("Caller", caller)
                .queryString("Callee", callee)
                //公共参数
                .queryString("Format", "JSON")
                .queryString("Version", "2017-07-05")
                .queryString("Timestamp", timeStamp.toString())
                .queryString("SignatureType", "BEARERTOKEN")
                .queryString("RegionId", "cn-shanghai")
                .queryString("SignatureNonce", randomString.nextString())
                .queryString("BearerToken", aliOauthVO.getAccessToken()).asString();


        //修改客资状态为已拨打
        clientInfoDao.editKzphoneFlag(kzId, ClientConst.DIALED);

        //存储电话日志
        ClientLogPO clientLogPO = new ClientLogPO();
        clientLogPO.setOperaId(staffPO.getId());
        clientLogPO.setOperaName(staffPO.getNickName());
        clientLogPO.setCompanyId(staffPO.getCompanyId());
        clientLogPO.setLogType(ClientLogConst.INFO_LOG_TYPE_CALL);
        clientLogPO.setKzId(kzId);
        clientLogPO.setMemo(ClientLogConst.INFO_LOG_CALL_PHONE);
        clientLogDao.addInfoLog(clientLogPO);

        //TODO 通话记录

    }

    @Override
    public void addCustomer(StaffPO staffPO, CallCustomerPO callCustomerPO) {
        if(NumUtil.isInValid(callCustomerPO.getCallId())){
            throw new RException(ExceptionEnum.CALL_ID_IS_NULL);
        }
        if(StringUtil.isEmpty(callCustomerPO.getPhone())){
            throw new RException(ExceptionEnum.CALL_CONSUMER_PHONE_IS_NULL);
        }
        if(NumUtil.isInValid(callCustomerPO.getStaffId())){
            throw new RException(ExceptionEnum.STAFF_ID_NULL);
        }
        CallCustomerPO callCustomer = callCustomerDao.getCallCustomerByStaffIdAndCompanyId(staffPO.getId(), staffPO.getCompanyId());
        if(null != callCustomer){
            throw new RException(ExceptionEnum.CALL_CONSUMER_IS_EXISTS);
        }
        StaffPO staff = staffDao.getByIdAndCid(callCustomerPO.getStaffId(), staffPO.getCompanyId());
        if(null == staff){
            throw new RException(ExceptionEnum.STAFF_IS_NOT_EXIST);
        }
        callCustomerPO.setStaffId(staff.getId());
        callCustomerPO.setNickName(staff.getNickName());
        callCustomerPO.setCompanyId(staffPO.getCompanyId());
        callCustomerDao.insert(callCustomerPO);
    }

    @Override
    public void editCustomer(StaffPO staffPO, CallCustomerPO callCustomerPO) {
        if(NumUtil.isInValid(callCustomerPO.getId())){
            throw new RException(ExceptionEnum.CALL_CONSUMER_ID_IS_NULL);
        }
        if(NumUtil.isInValid(callCustomerPO.getCallId())){
            throw new RException(ExceptionEnum.CALL_ID_IS_NULL);
        }
        if(StringUtil.isEmpty(callCustomerPO.getPhone())){
            throw new RException(ExceptionEnum.CALL_CONSUMER_PHONE_IS_NULL);
        }
        callCustomerPO.setStaffId(staffPO.getId());
        callCustomerPO.setCompanyId(staffPO.getCompanyId());
        callCustomerDao.update(callCustomerPO);
    }

    @Override
    public List<CallCustomerPO> customerList(StaffPO staffPO) {
        List<CallCustomerPO> callCustomerPOS = callCustomerDao.getCallCustomerListByCompanyId(staffPO.getCompanyId());
        return callCustomerPOS;
    }

    @Override
    public CallPO instanceList(StaffPO staffPO) {
        String sign = MD5Util.getApolloMd5(String.valueOf(staffPO.getCompanyId()));
        //Appollo接口获取用户信息
        String usreJson = HttpClient
                // 请求方式和请求url
                .get(appoloBaseUrl.concat(AppolloUrlConst.GET_CALL_USER))
                // post提交json
                .queryString("companyId", staffPO.getCompanyId())
                .queryString("sign", sign)
                .asString();
        //TODO 此处
        return null;
    }

}
