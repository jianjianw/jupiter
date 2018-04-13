package com.qiein.jupiter.web.dao;

import com.qiein.jupiter.web.entity.po.CompanyPO;
import com.qiein.jupiter.web.entity.po.StaffPO;
import com.qiein.jupiter.web.entity.vo.StaffVO;
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
     * @return
     */
    List<CompanyPO> getCompanyList(@Param("userName") String userName,
                                   @Param("password") String password);

    /**
     * 根据公司Id登录
     *
     * @param userName
     * @param password
     * @return
     */
    StaffPO loginWithCompanyId(@Param("userName") String userName,
                               @Param("password") String password,
                               @Param("companyId") int companyId);

    /**
     * 获取小组人员
     * @param companyId
     * @param groupId
     * @return
     */
    public List<StaffVO> getGroupStaffs(@Param("companyId") int companyId, @Param("groupId") String groupId);


}
