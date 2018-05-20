package com.qiein.jupiter.web.dao;

import com.qiein.jupiter.web.entity.po.CompanyPO;
import com.qiein.jupiter.web.entity.po.StaffPO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: shiTao
 */
public interface LoginDao {
    /**
     * 根据手机号和密码获取公司列表
     *
     * @return
     */
    List<CompanyPO> getCompanyListByPhone(@Param("phone") String phone, @Param("password") String password);

    /**
     * 根据手机号和密码 登录公司
     *
     * @return
     */
    StaffPO loginWithCidByPhone(@Param("phone") String phone,
                                @Param("password") String password,
                                @Param("companyId") int companyId);

    /**
     * 微信获取公司列表
     */
    List<CompanyPO> getCompanyListByWeChatUnionId(String unionId);

    /**
     * 微信登录
     */
    StaffPO loginWithCidByWeChatUnionId(@Param("unionId") String unionId, @Param("companyId") int companyId);

    /**
     * 钉钉获取公司列表
     */
    List<CompanyPO> getCompanyListByDingUnionId(String unionId);

    /**
     * 钉钉登录
     */
    StaffPO loginWithCidByDingUnionId(@Param("unionId") String unionId, @Param("companyId") int companyId);
}
