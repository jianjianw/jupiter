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
     *
     * @param companyId
     * @param groupId
     * @return
     */
    public List<StaffVO> getGroupStaffs(@Param("companyId") int companyId, @Param("groupId") String groupId);

    /**
     * 根据手机号获取员工信息
     *
     * @param companyId
     * @param phone
     * @return
     */
    public StaffPO getStaffByPhone(@Param("companyId") int companyId, @Param("phone") String phone);

    /**
     * 根据艺名，全名获取员工信息
     *
     * @param companyId
     * @param name
     * @return
     */
    public StaffPO getStaffByNames(@Param("companyId") int companyId, @Param("name") String name);

    /**
     * 添加员工
     *
     * @param staffVO
     */
    public void addStaffVo(StaffVO staffVO);

    /**
     * 编辑员工信息
     *
     * @param staffVO
     */
    public void updateStaff(StaffVO staffVO);

    /**
     * 批量编辑员工密码
     *
     * @param companyId
     * @param staffIdArr
     * @param password
     */
    public void batchEditStafPwd(@Param("companyId") int companyId, @Param("staffIdArr") String[] staffIdArr, @Param("password") String password);

}
