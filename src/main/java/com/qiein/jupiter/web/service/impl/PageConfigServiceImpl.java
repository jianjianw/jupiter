package com.qiein.jupiter.web.service.impl;

import com.qiein.jupiter.util.CollectionUtils;
import com.qiein.jupiter.web.dao.PageConfigDao;
import com.qiein.jupiter.web.entity.po.PageConfig;
import com.qiein.jupiter.web.service.PageConfigService;
import org.apache.commons.collections4.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 页面配置
 */
@Service
public class PageConfigServiceImpl implements PageConfigService {

    @Autowired
    private PageConfigDao pageConfigDao;


    /**
     * 根据供公司Id 和 角色获取配置
     *
     * @param companyId
     * @param role
     * @return
     */
    @Override
    public List<PageConfig> listPageConfigByCidAndRole(int companyId, String role) {
        //TODO 有没有一种好的思路解决过滤问题
        List<PageConfig> pageConfigs = pageConfigDao.listPageConfigByCidAndRole(companyId, role);
        //判断是否为空
        if(CollectionUtils.isNotEmpty(pageConfigs)){
            for (PageConfig pageConfig : pageConfigs) {

            }
        }
        return pageConfigs;
    }

    private void getTitleFilter(PageConfig pageConfig){
//        if(){
//
//        }
    }
}
