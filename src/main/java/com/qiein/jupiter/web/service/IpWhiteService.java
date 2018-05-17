package com.qiein.jupiter.web.service;

import java.util.List;

import com.qiein.jupiter.web.entity.po.IpWhitePO;
import com.qiein.jupiter.web.entity.vo.IpWhiteStaffVo;

/**
 * ip白名单
 *
 * @author XiangLiang 2018/05/16
 **/
public interface IpWhiteService {

    /**
     * 新增
     *
     * @param ipWhitePo
     * @return
     */
    void insert(IpWhitePO ipWhitePo);

    /**
     * 删除
     *
     * @param id
     * @return
     */
    void delete(int id);

    /**
     * 修改
     *
     * @param ipWhitePo
     * @return
     */
    void update(IpWhitePO ipWhitePo);

    /**
     * 显示ip页面
     *
     * @param companyId
     * @return List<IpWhitePO>
     */
    List<IpWhitePO> getAllIpByCompanyId(int companyId);
    /**
     * 显示ip页面
     * @param companyId 
     *
     * @param companyId
     * @return List<IpWhiteStaffVo>
     */
	List<IpWhiteStaffVo> FindIpWhite(int companyId);

}
