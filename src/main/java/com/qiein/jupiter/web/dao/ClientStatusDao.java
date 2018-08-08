package com.qiein.jupiter.web.dao;

import com.qiein.jupiter.web.entity.po.ClientStatusPO;
import com.qiein.jupiter.web.entity.po.StatusPO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

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
    /**
     * 修改客资状态
     * @param showFlag
     * @param id
     */
    void editClientStatus(@Param(value="showFlag")boolean showFlag,@Param(value="id")int id);
    /**
     * 获取企业状态列表
     *
     * @param companyId
     * @return
     */
    List<StatusPO> getCompanyStatusList(@Param("companyId") int companyId);

    /**
     * 编辑状态
     *
     * @param statusPO
     * @return
     */
    int editStatus(StatusPO statusPO);

    /**
     * 恢复默认状态
     *
     * @param statusPO
     * @return
     */
    int editStatusDefault(StatusPO statusPO);

    /**
     * 获取状态信息
     *
     * @return
     */
    StatusPO getStatusById(@Param("companyId") int companyId, @Param("id") int id);

    /**
     * 获取状态信息
     *
     * @return
     */
    StatusPO getStatusByStatusId(@Param("companyId") int companyId, @Param("statusId") int statusId);

    /**
     * 根据CLASS  ID  和 statusid 编辑状态名称
     */
    int editNameByClassIdAndStatusId(StatusPO statusPO);
}
