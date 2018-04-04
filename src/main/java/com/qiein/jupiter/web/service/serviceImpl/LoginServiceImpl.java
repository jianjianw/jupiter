package com.qiein.jupiter.web.service.serviceImpl;

import com.qiein.jupiter.web.dao.StaffDao;
import com.qiein.jupiter.web.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

/**
 * 登录服务实现类
 */
@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private ValueOperations<String,String> valueOperations;


    /**
     * 登录
     * @param userName 用户名
     * @param password 密码
     * @return
     */
    @Override
    public String login(String userName, String password) {
        return "token:success";
    }

    /**
     * 登出
     * @return
     */
    @Override
    public String logout() {
        return null;
    }
}
