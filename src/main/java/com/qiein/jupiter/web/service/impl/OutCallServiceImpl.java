package com.qiein.jupiter.web.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.mzlion.easyokhttp.HttpClient;
import com.qiein.jupiter.constant.TiOutCallUrlConst;
import com.qiein.jupiter.util.MD5Util;
import com.qiein.jupiter.util.StringUtil;
import com.qiein.jupiter.web.dao.OutCallDao;
import com.qiein.jupiter.web.entity.dto.OutCallUserDTO;
import com.qiein.jupiter.web.entity.po.StaffPO;
import com.qiein.jupiter.web.service.OutCallService;
import com.qiein.jupiter.web.service.StaffService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 天润 外呼系统
 *
 * @Author: shiTao
 */
@Service
public class OutCallServiceImpl implements OutCallService {
    private final Logger log = LoggerFactory.getLogger(getClass().getName());

    @Autowired
    private OutCallDao outCallDao;

    @Autowired
    private StaffService staffService;


    /**
     * 上下线
     *
     * @param companyId
     * @param staffId
     * @return
     */
    @Override
    public JSONObject userOnlineOffline(int companyId, int staffId, boolean online) {
        OutCallUserDTO userInfoAndAdmin = outCallDao.getUserInfoAndAdmin(companyId, staffId);
        //加密一下
        seedUser(userInfoAndAdmin, true);
        Map<String, String> baseMap = getBaseMap(userInfoAndAdmin, true);
        baseMap.put("cno", String.valueOf(userInfoAndAdmin.getCno()));
        //1. 空闲  2. 置忙
        baseMap.put("status", "1");
        if (StringUtil.isNotEmpty(userInfoAndAdmin.getBindTel())) {
            baseMap.put("bindTel", userInfoAndAdmin.getBindTel());
        }
        //电话类型 1. 普通电话 2. 分机 4. 远程座席话机
        baseMap.put("type", "1");
        String url;
        if (online) {
            url = TiOutCallUrlConst.userOnLine;
        } else {
            url = TiOutCallUrlConst.userOffLine;
        }
        return this.postToNet(baseMap, url, false);
    }


    /**
     * 创建外呼账户
     *
     * @return
     */
    @Override
    public JSONObject createCallAccount(int companyId, int staffId, String tel) {
        OutCallUserDTO admin = outCallDao.getAdminByCompanyId(companyId);
        //加密一下
        seedUser(admin, false);
        StaffPO staff = staffService.getById(staffId, companyId);
        if (StringUtil.isEmpty(tel)) {
            tel = staff.getPhone();
        }
        //如果没有cno则生成
//        if (cno == 0) {
//            cno = generateCno(companyId, admin.getEnterpriseId());
//        }
        Map<String, String> baseMap = getBaseMap(admin, false);
        baseMap.put("cno", String.valueOf(staffId));
        baseMap.put("name", staff.getNickName());
        //TODO 区号
        baseMap.put("areaCode", "0571");
        JSONObject jsonObject = postToNet(baseMap, TiOutCallUrlConst.addOrUpdateUser, true);
        //成功则数据库新增
        if (jsonObject.getString("result").equals("success")) {
            admin.setId(jsonObject.getIntValue("id"));
            admin.setCno(staffId);
            admin.setStaffId(staffId);
            outCallDao.addUserRela(admin);
        }
        return jsonObject;
    }

    /**
     * 删除外呼账户
     *
     * @param companyId
     * @param staffId
     * @return
     */
    @Override
    public JSONObject delCallAccount(int companyId, int staffId) {
        OutCallUserDTO admin = outCallDao.getUserInfoAndAdmin(companyId, staffId);
        JSONObject jsonObject = new JSONObject();
        if (admin != null) {
            //加密一下
            seedUser(admin, false);
            Map<String, String> baseMap = getBaseMap(admin, false);
            baseMap.put("id", String.valueOf(admin.getId()));
            jsonObject = postToNet(baseMap, TiOutCallUrlConst.delUser, false);
            if (jsonObject.getString("result").equals("success")) {
                outCallDao.delUserRela(admin);
            }
        }
        return jsonObject;
    }

    /**
     * 修改绑定的手机
     *
     * @param companyId
     * @param staffId
     * @param tel
     * @return
     */
    @Override
    public JSONObject updateCallAccountPhone(int companyId, int staffId, String tel) {
        OutCallUserDTO admin = outCallDao.getUserInfoAndAdmin(companyId, staffId);
        //加密一下
        seedUser(admin, true);
        Map<String, String> baseMap = getBaseMap(admin, true);
        StaffPO staff = staffService.getById(staffId, companyId);
        if (StringUtil.isEmpty(tel)) {
            tel = staff.getPhone();
        }
        baseMap.put("cno", String.valueOf(admin.getCno()));
        baseMap.put("bindTel", tel);

        JSONObject jsonObject = this.postToNet(baseMap, TiOutCallUrlConst.changeBindPhone, false);
        if (jsonObject.getString("result").equals("success")) {
            admin.setBindTel(tel);
            outCallDao.updateBindTel(admin);
        }
        return jsonObject;
    }

    /**
     * 删除绑定的手机
     *
     * @param companyId
     * @param staffId
     * @return
     */
    @Override
    public JSONObject delCallAccountPhone(int companyId, int staffId) {
        return null;
    }


    /**
     * 通话
     *
     * @return
     */
    @Override
    public JSONObject callPhone(int companyId, int staffId, String tel) {
        OutCallUserDTO user = outCallDao.getUserInfoAndAdmin(companyId, staffId);
        //加密一下
        seedUser(user, false);
        Map<String, String> baseMap = getBaseMap(user, false);
        baseMap.put("cno", String.valueOf(user.getCno()));
        baseMap.put("customerNumber", tel);
        //	是否异步调用：1 同步调用 0：异步调用
        baseMap.put("sync", "0");
        JSONObject jsonObject = this.postToNet(baseMap, TiOutCallUrlConst.call, true);
        log.info(jsonObject.toString());
        return jsonObject;
    }


    /**
     * 挂断
     *
     * @param outCallUserDTO
     * @return
     */
    @Override
    public JSONObject hangupPhone(OutCallUserDTO outCallUserDTO) {
        return null;
    }

    @Override
    public JSONObject getCallRecord(OutCallUserDTO outCallUserDTO) {
        return null;
    }

    @Override
    public OutCallUserDTO getAdminByCompanyId(int companyId) {
        return outCallDao.getAdminByCompanyId(companyId);
    }

    @Override
    public OutCallUserDTO getUserInfoAndAdmin(int companyId, int staffId) {
        return outCallDao.getUserInfoAndAdmin(companyId, staffId);
    }

    /**
     * 发送请求
     *
     * @param isPost
     * @return
     */
    @Override
    public JSONObject postToNet(Map<String, String> paramMap, String url, boolean isPost) {
        JSONObject json;
        if (isPost) {
            json = HttpClient.post(url)
                    .param(paramMap)
                    .asBean(JSONObject.class);
        } else {
            json = HttpClient.get(url)
                    .queryString(paramMap)
                    .asBean(JSONObject.class);
        }
        return json;
    }

    /**
     * 生成CNO
     *
     * @param companyId
     * @return
     */
    @Override
    public int generateCno(int companyId, String enterpriseId) {
        List<Integer> cnoArr = outCallDao.getCnoArr(companyId);
        Set<Integer> setNum = new HashSet<>(cnoArr);
        int initNum = 2000;
        int endNum = 9999;
        for (int i = initNum; i <= endNum; i++) {
            if (!setNum.contains(i)) {
                //查询API 判断一下是否存在
                Boolean aBoolean = HttpClient.get(TiOutCallUrlConst.validateCno)
                        .queryString("enterpriseId", enterpriseId)
                        .queryString("cno", i)
                        .asBean(JSONObject.class).getBoolean("result");
                if (!aBoolean) {
                    return i;
                }
            }
        }
        //TODO 如果这里还没有找到，可以确定已经用完了
        return 0;
    }

    @Override
    public JSONObject getValidateCode(int companyId, int staffId, String tel) {
        OutCallUserDTO admin = outCallDao.getUserInfoAndAdmin(companyId, staffId);
        StaffPO staff = staffService.getById(staffId, companyId);
        if (StringUtil.isEmpty(tel)) {
            tel = staff.getPhone();
        }
        //加密一下
        seedUser(admin, true);
        Map<String, String> baseMap = getBaseMap(admin, true);
        //先验证手机号码
        baseMap.put("cno", String.valueOf(admin.getCno()));
        baseMap.put("bindTel", tel);
        //是否进行语音验证。 1进行验证，0不验证。默认为1。
        baseMap.put("getValidateCode", "1");
        JSONObject codeJson = this.postToNet(baseMap, TiOutCallUrlConst.validateAndGetCode, false);
        return codeJson;
    }

    /**
     * 添加到白名单
     */
    @Override
    public JSONObject addToWhite(int companyId, int staffId, String tel, String key, String validateCode) {
        OutCallUserDTO admin = outCallDao.getUserInfoAndAdmin(companyId, staffId);
        //加密一下
        seedUser(admin, true);
        Map<String, String> baseMap = getBaseMap(admin, true);
        StaffPO staff = staffService.getById(staffId, companyId);
        if (StringUtil.isEmpty(tel)) {
            tel = staff.getPhone();
        }
        baseMap.put("cno", String.valueOf(admin.getCno()));
        baseMap.put("bindTel", tel);
        baseMap.put("key", key);
        baseMap.put("validateCode", validateCode);
        JSONObject jsonObject = this.postToNet(baseMap, TiOutCallUrlConst.validateCode, false);
        return jsonObject;
    }


    /**
     * 给用户账号加密
     *
     * @return
     */
    private void seedUser(OutCallUserDTO outCallUserDTO, boolean isSeed) {
        //先获取企业的账户
        OutCallUserDTO adminByCompanyId = outCallDao.getAdminByCompanyId(outCallUserDTO.getCompanyId());
        String username = adminByCompanyId.getUsername();
        String password = adminByCompanyId.getPassword();
        if (StringUtil.isNotEmpty(username) && StringUtil.isNotEmpty(password)) {
            String md5Password = MD5Util.getMD5(password);
            if (isSeed) {
                String seed = MD5Util.generatorRandMd5();
                md5Password = MD5Util.getMD5(md5Password + seed);
                outCallUserDTO.setSeed(seed);
            }
            //设置值
            outCallUserDTO.setEnterpriseId(adminByCompanyId.getEnterpriseId());
            outCallUserDTO.setUsername(adminByCompanyId.getUsername());
            outCallUserDTO.setPassword(md5Password);
        }
    }

    /**
     * 将User转为Map
     *
     * @return
     */
    private Map<String, String> getBaseMap(OutCallUserDTO outCallUserDTO, boolean isSeed) {
        Map<String, String> map = new HashMap<>();
        map.put("enterpriseId", outCallUserDTO.getEnterpriseId());
        map.put("userName", outCallUserDTO.getUsername());
        if (isSeed) {
            map.put("pwd", outCallUserDTO.getPassword());
            map.put("seed", outCallUserDTO.getSeed());
        } else {
            map.put("password", outCallUserDTO.getPassword());
        }
        return map;
    }
}
