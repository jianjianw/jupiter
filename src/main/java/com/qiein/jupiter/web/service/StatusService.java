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
    Map<String, Object> getStatusDictMap(int companyId);
}
