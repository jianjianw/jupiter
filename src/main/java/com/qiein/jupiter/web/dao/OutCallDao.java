package com.qiein.jupiter.web.dao;

import com.qiein.jupiter.web.entity.dto.OutCallUserDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 外呼系统
 *
 * @Author: shiTao
 */
public interface OutCallDao {
    /**
     * 根据企业ID 获取绑定的账户
     *
     * @param companyId
     * @return
     */
    OutCallUserDTO getAdminByCompanyId(int companyId);

    /**
     * 新增用户
     *
     * @return
     */
    int addUserRela(OutCallUserDTO outCallUserDTO);

    /**
     * 删除用户
     */
    int delUserRela(OutCallUserDTO outCallUserDTO);

    /**
     * 获取企业所有CNO 集合
     *
     * @param companyId
     * @return
     */
    List<Integer> getCnoArr(int companyId);

    /**
     * 根据公司ID和员工ID 获取详情
     *
     * @param companyId
     * @param staffId
     * @return
     */
    OutCallUserDTO getUserInfoAndAdmin(@Param("companyId") int companyId, @Param("staffId") int staffId);

    /**
     * 修改绑定手机
     *
     * @param outCallUserDTO
     * @return
     */
    int updateBindTel(OutCallUserDTO outCallUserDTO);

    /**
     * 新增管理员
     */
    int addAdmin(OutCallUserDTO outCallUserDTO);

    /**
     * 修改管理员账号
     */
    int updateAdmin(OutCallUserDTO outCallUserDTO);

    /**
     * 获取企业外呼员工列表
     */
    List<OutCallUserDTO> getUserList(int companyId);
}
