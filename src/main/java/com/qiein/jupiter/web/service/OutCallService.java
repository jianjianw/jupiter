package com.qiein.jupiter.web.service;

import com.alibaba.fastjson.JSONObject;
import com.qiein.jupiter.web.entity.dto.OutCallUserDTO;

import java.util.Map;

/**
 * 外呼系统接口
 *
 * @Author: shiTao
 */
public interface OutCallService {

    /**
     * 坐席 上下线
     */
    JSONObject userOnlineOffline(int companyId, int staffId, boolean online);

    /**
     * 创建外呼账户
     */
    JSONObject createCallAccount(int companyId, int staffId, String tel);

    /**
     * 删除外呼账户
     */
    JSONObject delCallAccount(int companyId, int staffId);


    /**
     * 修改外呼账号的绑定手机
     */
    JSONObject updateCallAccountPhone(int companyId, int staffId, String tel);

    /**
     * 删除外呼账户的绑定手机
     */
    JSONObject delCallAccountPhone(int companyId, int staffId);

    /**
     * 拨打电话
     */
    JSONObject callPhone(int companyId, int staffId, String tel);

    /**
     * 主动挂断
     */
    JSONObject hangupPhone(OutCallUserDTO outCallUserDTO);

    /**
     * 获取通话记录
     */
    JSONObject getCallRecord(OutCallUserDTO outCallUserDTO);

    /**
     * 根据企业ID 获取绑定的管理员账号
     *
     * @param companyId
     * @return
     */
    OutCallUserDTO getAdminByCompanyId(int companyId);

    /**
     * 根据企业ID 和员工ID 获取员工和管理员的详情
     *
     * @param companyId
     * @param staffId
     * @return
     */
    OutCallUserDTO getUserInfoAndAdmin(int companyId, int staffId);

    /**
     * post 发送数据
     *
     * @return
     */
    JSONObject postToNet(Map<String, String> paramMap, String url, boolean isPost);

    /**
     * 生产CNO
     *
     * @return
     */
    int generateCno(int companyId, String enterpriseId);

    /**
     * 验证白名单并获取语音验证码
     */
    JSONObject getValidateCode(int companyId, int staffId, String phone);

    /**
     * 添加到白名单
     */
    JSONObject addToWhite(int companyId, int staffId, String tel, String key, String validateCode);

}
