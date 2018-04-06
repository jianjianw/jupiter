package com.qiein.jupiter.web.dao;

import com.qiein.jupiter.web.entity.po.StaffPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.mybatis.spring.annotation.MapperScan;

import java.util.List;

/**
 * 员工Dao
 */
public interface StaffDao extends BaseDao<StaffPO> {

    /**
     * 登录
     *
     * @param userName
     * @param password
     * @param companyId
     * @return
     */
    StaffPO login(@Param("userName") String userName,
                  @Param("password") String password,
                  @Param("companyId") int companyId);
}
