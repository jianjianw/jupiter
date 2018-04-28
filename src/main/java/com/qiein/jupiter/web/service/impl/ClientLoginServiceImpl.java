package com.qiein.jupiter.web.service.impl;

import com.qiein.jupiter.constant.NumberConstant;
import com.qiein.jupiter.constant.RedisConstant;
import com.qiein.jupiter.exception.ExceptionEnum;
import com.qiein.jupiter.exception.RException;
import com.qiein.jupiter.util.CollectionUtils;
import com.qiein.jupiter.util.JwtUtil;
import com.qiein.jupiter.util.MD5Util;
import com.qiein.jupiter.util.StringUtil;
import com.qiein.jupiter.web.dao.StaffDao;
import com.qiein.jupiter.web.entity.po.CompanyPO;
import com.qiein.jupiter.web.entity.po.StaffDetailPO;
import com.qiein.jupiter.web.entity.po.StaffPO;
import com.qiein.jupiter.web.service.ClientLoginService;
import com.qiein.jupiter.web.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class ClientLoginServiceImpl implements ClientLoginService {
    @Autowired
    private StaffDao staffDao;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private CompanyService companyService;


    @Override
    public List<CompanyPO> getCompanyList(String userName, String password) {
        List<CompanyPO> companyList = staffDao.getCompanyList(userName, MD5Util.getSaltMd5(password));
        if (CollectionUtils.isEmpty(companyList)) {
            // 用户不存在
            throw new RException(ExceptionEnum.USERNAME_OR_PASSWORD_ERROR);
        }
        // 移除错误次数
        removeUserErrorNumber(userName);
        return companyList;
    }

    //todo 上下线日志
    @Override
    public StaffPO loginWithCompanyId(String userName, String password, int companyId, String ip) {
        // 加密码加密
        password = MD5Util.getSaltMd5(password);
        StaffPO staffPO = staffDao.loginWithCompanyId(userName, password, companyId);
        if (staffPO == null) {
            // 用户不存在
            throw new RException(ExceptionEnum.USERNAME_OR_PASSWORD_ERROR);
        } else if (staffPO.isLockFlag()) {
            // 锁定
            throw new RException(ExceptionEnum.USER_IS_LOCK);
        } else if (staffPO.isDelFlag()) {
            // 删除
            throw new RException(ExceptionEnum.USER_IS_DEL);
        }
        // 验证公司属性
        CompanyPO companyPO = companyService.getById(staffPO.getCompanyId());
        // 如果员工没有token，重新生成
        if (StringUtil.isEmpty(staffPO.getToken()) || companyPO.isSsoLimit()) {
            updateStaffToken(staffPO);
        }
        // 移除错误次数
        removeUserErrorNumber(userName);
        // 更新登录时间和IP
        StaffDetailPO staffDetailPO = new StaffDetailPO();
        staffDetailPO.setId(staffPO.getId());
        staffDetailPO.setCompanyId(staffPO.getCompanyId());
        staffDetailPO.setLastLoginIp(ip);
        staffDao.updateStaffLoginInfo(staffDetailPO);
        //上线状态
        StaffPO staffPO1=new StaffPO();
        staffPO1.setId(staffPO.getId());
        staffPO1.setCompanyId(staffPO.getCompanyId());
        staffPO1.setShowFlag(1);
        staffDao.updateShowFlag(staffPO1);
        return staffPO;
    }

    /**
     * 删除缓存中的用户错误次数和验证码
     *
     * @param phone
     */
    private void removeUserErrorNumber(String phone) {
        // 错误次数
        String userLoginErrNum = RedisConstant.getUserLoginErrNumKey(phone);
        redisTemplate.delete(userLoginErrNum);
        // 验证码
        redisTemplate.delete(RedisConstant.getVerifyCodeKey(phone));
    }

    /**
     * 将更新的staff放入redis 并更新到数据库
     *
     * @param staffPO
     */
    private void updateStaffToken(StaffPO staffPO) {
        // 生成token
        String token = JwtUtil.generatorToken();
        staffPO.setToken(token);
        redisTemplate.opsForValue().set(RedisConstant.getStaffKey(staffPO.getId(), staffPO.getCompanyId()), staffPO,
                NumberConstant.DEFAULT_EXPIRE_TIME, TimeUnit.HOURS);
        // 并更新到数据库
        staffDao.updateToken(staffPO);
    }
}
