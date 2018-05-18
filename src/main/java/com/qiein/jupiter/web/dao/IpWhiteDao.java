package com.qiein.jupiter.web.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.qiein.jupiter.web.entity.po.IpWhitePO;
import com.qiein.jupiter.web.entity.vo.IpWhiteStaffVO;

/**
 * ip白名单
 *
 * @author XiangLiang 2018/05/16
 **/
public interface IpWhiteDao extends BaseDao<IpWhitePO> {
    /**
     * 删除员工小组关联
     *
     * @param companyId
     * @result List<IpWhitePO>
     */
    List<IpWhitePO> getAllIpByCompanyId(@Param("companyId") int companyId);

    /**
     * 删除员工小组关联
     *
     * @param companyId
     */
    List<IpWhiteStaffVO> findIpWhite(@Param("companyId") int companyId);

    /**
     * 根据公司id 寻找白名单ip
     *
     * @param companyId
     * @return List<String>
     */
    List<String> findIp(int companyId);


}
