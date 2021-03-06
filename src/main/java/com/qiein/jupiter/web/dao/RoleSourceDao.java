package com.qiein.jupiter.web.dao;

import com.qiein.jupiter.web.entity.po.RoleSourcePO;
import com.qiein.jupiter.web.entity.vo.RoleSourceVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * FileName: RoleSourceDao
 *
 * @author: yyx
 * @Date: 2018-7-18 15:47
 */
public interface RoleSourceDao extends BaseDao<RoleSourcePO>{
    /**
     * 批量录入角色来源
     * */
    void batchAddRoleSource(@Param(value="roleId") Integer roleId,@Param(value="sourceIds") List<String> sourceIds,@Param(value="companyId") Integer companyId);

    /**
     * 根据公司id获取 花费权限渠道
     * @param companyId
     * @return
     */
    List<RoleSourceVO> getByCompanyId(@Param("companyId")Integer companyId);

}
