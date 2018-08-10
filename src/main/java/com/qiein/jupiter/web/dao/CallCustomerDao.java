package com.qiein.jupiter.web.dao;

import com.qiein.jupiter.web.entity.po.CallCustomerPO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author: yyx
 * @Date: 2018-8-10
 */
public interface CallCustomerDao extends BaseDao<CallCustomerPO>{
    /**
     * 根据员工id，公司id获取客户信息
     * @param staffId
     * @param companyId
     * @return
     * */
    CallCustomerPO getCallCustomerByStaffIdAndCompanyId(@Param(value="staffId") int staffId,@Param(value="companyId") int companyId);

    /**
     * 获取客服列表
     * @param companyId
     * @return
     * */
    List<CallCustomerPO> getCallCustomerListByCompanyId(@Param(value="companyId") int companyId);
}
