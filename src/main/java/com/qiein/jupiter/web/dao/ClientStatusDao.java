package com.qiein.jupiter.web.dao;

import com.qiein.jupiter.web.entity.po.ClientStatusPO;
import org.apache.ibatis.annotations.Param;

/**
 * FileName: ClientStatusDao
 *
 * @author: yyx
 * @Date: 2018-6-20 14:35
 */
public interface ClientStatusDao {
    /**
     * 根据状态id获取客资状态
     * @param statusId
     * @param companyId
     * @return
     * */
     ClientStatusPO getClientStatusByStatusId(@Param(value="statusId") Integer statusId,@Param(value="companyId")Integer companyId);
}
