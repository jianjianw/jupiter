package com.qiein.jupiter.web.service;

import com.qiein.jupiter.web.entity.po.StatusPO;

import java.util.List;
import java.util.Map;

/**
 * 状态管理
 */
public interface StatusService {

    /**
     * 获取企业状态列表
     *
     * @param companyId
     * @return
     */
    List<StatusPO> getCompanyStatusList(int companyId);

    /**
     * 编辑状态
     *
     * @param statusPO
     * @return
     */
    void editStatus(StatusPO statusPO);

    /**
     * 修改状态颜色为默认值
     *
     * @param companyId
     * @param statusId
     * @param column
     */
    void editColorToDefault(int companyId, int statusId, String column);

    /**
     * 获取企业状态字典
     */
    Map<String, StatusPO> getStatusDictMap(int companyId);

    /**
     * 根据CLASS  ID  和 statusid 编辑状态名称
     */
    int editNameByClassIdAndStatusId(StatusPO statusPO);

    /**
     * 修改客资状态
     *
     * @param showFlag
     * @param id
     */
    void editClientStatus(boolean showFlag, int id);

    /**
     * 修改手机是否已加状态
     *
     * @param kzId
     * @param kzphoneFlag
     */
    void editKzphoneFlag(String kzId, Integer kzphoneFlag);
}
