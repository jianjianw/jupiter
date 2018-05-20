package com.qiein.jupiter.web.dao;

import com.qiein.jupiter.web.entity.po.CompanyPO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: shiTao
 */
public interface LoginDao {
    /**
     * 根据手机号和密码 获取用户信息
     *
     * @return
     */
    List<CompanyPO> getCompanyListByPhone(@Param("phone") String phone, @Param("phone") String password);
}
