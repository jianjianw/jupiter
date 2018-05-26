package com.qiein.jupiter.web.service;

import com.qiein.jupiter.web.entity.po.CompanyPluginPO;
import com.qiein.jupiter.web.entity.po.PluginPO;

import java.util.List;

/**
 * 插件Service
 * Created by Tt
 * on 2018/5/26 0026.
 */
public interface PluginService {

    /**
     * 获取公司插件列表
     *
     * @param companyId
     * @return
     */
    List<PluginPO> getListByCompanyId(int companyId);

    /**
     * 获取所有插件列表
     * @return
     */
    List<PluginPO> getListAll();

    /**
     * 添加插件
     *
     * @param companyPluginPO
     */
    void addPlugin(CompanyPluginPO companyPluginPO);

    /**
     * 删除插件
     *
     * @param pluginIds
     * @param companyId
     */
    void delPlugin(String pluginIds, int companyId);
}
