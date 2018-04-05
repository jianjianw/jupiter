package com.qiein.jupiter.web.dao;

import com.qiein.jupiter.web.entity.po.StaffPO;
import org.apache.ibatis.annotations.Mapper;
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
    StaffPO login(String userName, String password, int companyId);
}
