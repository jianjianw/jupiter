package com.qiein.jupiter.web.service.impl;

import com.qiein.jupiter.constant.DictionaryConstant;
import com.qiein.jupiter.constant.NumberConstant;
import com.qiein.jupiter.util.CollectionUtils;
import com.qiein.jupiter.web.dao.PageConfigDao;
import com.qiein.jupiter.web.entity.dto.PageFilterDTO;
import com.qiein.jupiter.web.entity.po.PageConfig;
import com.qiein.jupiter.web.service.PageConfigService;
import org.apache.commons.collections4.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

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
        if (CollectionUtils.isNotEmpty(pageConfigs)) {
            //取默认公司
            pageConfigs = pageConfigDao.listPageConfigByCidAndRole(DictionaryConstant.COMMON_COMPANYID, role);
            for (PageConfig pageConfig : pageConfigs) {

            }
        }
        return pageConfigs;
    }

    @Override
    public Map<String, List<PageFilterDTO>> getAllPageFilterMap(int companyId) {
        //状态
        //渠道
        //品牌
        //咨询方式
        //意向等级
        //婚期简述
        //咨询类型
        //预排时间简述
        //客服小组，根据不同的角色
        //推广
        //筛选
        //客服
        //门市
        //门店
        //预算范围
        //无效原因
        //流失原因
        //


        return null;
    }

    private void getTitleFilter(PageConfig pageConfig) {
//        if(){
//
//        }
    }
}
