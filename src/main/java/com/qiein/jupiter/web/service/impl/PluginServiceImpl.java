package com.qiein.jupiter.web.service.impl;

import com.qiein.jupiter.web.dao.PluginDao;
import com.qiein.jupiter.web.entity.po.CompanyPluginPO;
import com.qiein.jupiter.web.entity.po.PluginPO;
import com.qiein.jupiter.web.service.PluginService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by Tt
 * on 2018/5/26 0026.
 */
@Service
public class PluginServiceImpl implements PluginService{

    @Resource
    private PluginDao pluginDao;

    /**
     * 获取该公司插件列表
     * @param companyId
     * @return
     */
    @Override
    public List<PluginPO> getListByCompanyId(int companyId) {
        return pluginDao.getListByCompanyId(companyId);
    }

    @Override
    public List<PluginPO> getListAll() {
        return pluginDao.getListAll();
    }

    @Override
    public void addPlugin(CompanyPluginPO companyPluginPO) {
        pluginDao.addPlugin(companyPluginPO);
    }

    @Override
    public void delPlugin(String pluginIds, int companyId) {
        pluginDao.delPlugin(pluginIds.split(","),companyId);
    }

}
