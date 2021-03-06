package com.qiein.jupiter.web.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.qiein.jupiter.constant.DictionaryConstant;
import com.qiein.jupiter.util.CollectionUtils;
import com.qiein.jupiter.util.StringUtil;
import com.qiein.jupiter.web.dao.BrandDao;
import com.qiein.jupiter.web.dao.GroupDao;
import com.qiein.jupiter.web.dao.PageConfigDao;
import com.qiein.jupiter.web.dao.ShopDao;
import com.qiein.jupiter.web.entity.dto.PageFilterDTO;
import com.qiein.jupiter.web.entity.po.BrandPO;
import com.qiein.jupiter.web.entity.po.DictionaryPO;
import com.qiein.jupiter.web.entity.po.PageConfig;
import com.qiein.jupiter.web.entity.vo.FilterMapVO;
import com.qiein.jupiter.web.entity.vo.GroupsInfoVO;
import com.qiein.jupiter.web.entity.vo.ShopVO;
import com.qiein.jupiter.web.service.DictionaryService;
import com.qiein.jupiter.web.service.PageConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 页面配置
 */
@Service
public class PageConfigServiceImpl implements PageConfigService {

    @Autowired
    private PageConfigDao pageConfigDao;

    @Autowired
    private DictionaryService dictionaryService;

    @Autowired
    private GroupDao groupDao;

    @Autowired
    private BrandDao brandDao;

    @Autowired
    private ShopDao shopDao;

    /**
     * 根据供公司Id 和 角色获取配置
     *
     * @param companyId
     * @param role
     * @return
     */
    @Override
    public List<PageConfig> listPageConfigByCidAndRole(int companyId, String role, boolean showFlag) {
        List<PageConfig> pageConfigs;
        //判断是否要获取显示的表头
        if (showFlag) {
            pageConfigs = pageConfigDao.listIsShowPageConfigByCidAndRole(companyId, role);
        } else {
            pageConfigs = pageConfigDao.listPageConfigByCidAndRole(companyId, role);
        }

        if (CollectionUtils.isNotEmpty(pageConfigs)) {
            for (PageConfig pageConfig : pageConfigs) {
                renderTitleFilter(pageConfig);
            }
        }
        return pageConfigs;
    }

    /**
     * 获取所有的筛选集合
     *
     * @param companyId
     * @param role
     * @return
     */
    ///TODO 这个方法暂时不会用到
    @Override
    public FilterMapVO getPageFilterMap(int companyId, String role) {
        FilterMapVO pageFilterMap = new FilterMapVO();
        Map<String, List<DictionaryPO>> dictMapByCid = dictionaryService.getDictMapByCid(companyId);
        // todo
        // 状态

        // 渠道

        // 品牌
        List<PageFilterDTO> brandFilters = new ArrayList<>();
        List<BrandPO> brandList = brandDao.getBrandList(companyId);
        if (CollectionUtils.isNotEmpty(brandList)) {
            for (BrandPO brandPO : brandList) {
                PageFilterDTO PageFilterDTO = new PageFilterDTO();
                // 品牌名称
                PageFilterDTO.setLabel(brandPO.getBrandName());
                // ID
                PageFilterDTO.setValue(String.valueOf(brandPO.getId()));
                brandFilters.add(PageFilterDTO);
            }
        }
        pageFilterMap.setBrand(brandFilters);
        // 咨询方式
        List<PageFilterDTO> zxModeFilters = getDictFilter(DictionaryConstant.ZX_STYLE, dictMapByCid);
        pageFilterMap.setZxMode(zxModeFilters);
        // 意向等级
        List<PageFilterDTO> yxRankFilters = getDictFilter(DictionaryConstant.YX_RANK, dictMapByCid);
        pageFilterMap.setYxRank(yxRankFilters);
        // 婚期简述
        List<PageFilterDTO> marryRangeFilters = getDictFilter(DictionaryConstant.MARRY_TIME, dictMapByCid);
        pageFilterMap.setMarryRange(marryRangeFilters);
        // 咨询类型
        List<PageFilterDTO> zxTypeFilters = getDictFilter(DictionaryConstant.COMMON_TYPE, dictMapByCid);
        pageFilterMap.setZxType(zxTypeFilters);
        // 预拍时间简述
        List<PageFilterDTO> ypRangeFilters = getDictFilter(DictionaryConstant.YP_TIME, dictMapByCid);
        pageFilterMap.setYpRange(ypRangeFilters);
        // 预算范围
        List<PageFilterDTO> ysRangeFilters = getDictFilter(DictionaryConstant.YS_RANGE, dictMapByCid);
        pageFilterMap.setYsRange(ysRangeFilters);
        // 无效原因
        List<PageFilterDTO> invalidReasonFilters = getDictFilter(DictionaryConstant.INVALID_REASON, dictMapByCid);
        pageFilterMap.setInvalidReason(invalidReasonFilters);
        // 流失原因
        List<PageFilterDTO> runoffReasonFilters = getDictFilter(DictionaryConstant.RUN_OFF_REASON, dictMapByCid);
        pageFilterMap.setRunoffReason(runoffReasonFilters);
        // 客服小组，根据不同的角色
        List<GroupsInfoVO> companyDeptListByType = groupDao.getCompanyDeptListByType(role, companyId);
        List<PageFilterDTO> companyDeptListByTypeFilters = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(companyDeptListByTypeFilters)) {
            for (GroupsInfoVO groupsInfoVO : companyDeptListByType) {
                PageFilterDTO PageFilterDTO = new PageFilterDTO();
                PageFilterDTO.setLabel(groupsInfoVO.getGroupName());
                PageFilterDTO.setValue(groupsInfoVO.getGroupId());
                companyDeptListByTypeFilters.add(PageFilterDTO);
            }
        }
        pageFilterMap.setDept(companyDeptListByTypeFilters);
        // 推广

        // 筛选

        // 客服

        // 门市

        // 门店
        List<ShopVO> showShopList = shopDao.getShowShopList(companyId);
        List<PageFilterDTO> shopFilters = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(showShopList)) {
            for (ShopVO shopVO : showShopList) {
                PageFilterDTO PageFilterDTO = new PageFilterDTO();
                PageFilterDTO.setLabel(shopVO.getShopName());
                PageFilterDTO.setValue(String.valueOf(shopVO.getId()));
                companyDeptListByTypeFilters.add(PageFilterDTO);
            }
        }
        pageFilterMap.setShop(shopFilters);

        return pageFilterMap;
    }

    /**
     * 保存页面配置
     */
    @Override
    public int updatePageConfig(PageConfig pageConfig) {
        return pageConfigDao.updatePageConfig(pageConfig);
    }

    /**
     * 渲染表头数据
     *
     * @param pageConfig
     */
    private void renderTitleFilter(PageConfig pageConfig) {
        String titleTxt = pageConfig.getTitleTxt();
        List<JSONObject> jsonArray = JSONObject.parseArray(titleTxt, JSONObject.class);
        for (JSONObject json : jsonArray) {
            // JSON 就是当前表头
            // TODO magic变量等
            if (json.get("filterAble") != null) {
                json.get("filterKey");
            }
        }
    }

    /**
     * 根据字典type 获取筛选下拉
     *
     * @param type
     * @param dictMapByCid
     * @return
     */
    private List<PageFilterDTO> getDictFilter(String type, Map<String, List<DictionaryPO>> dictMapByCid) {
        // 遍历生成
        List<PageFilterDTO> list = new ArrayList<>();
        //转成驼峰的获取
        List<DictionaryPO> dictionaryPOS = dictMapByCid.get(StringUtil.camelCaseName(type));
        // 空判断
        if (CollectionUtils.isNotEmpty(dictionaryPOS)) {
            for (DictionaryPO dictionaryPO : dictionaryPOS) {
                PageFilterDTO pageFilterDTO = new PageFilterDTO();
                pageFilterDTO.setLabel(dictionaryPO.getDicName());
                pageFilterDTO.setValue(String.valueOf(dictionaryPO.getDicCode()));
                list.add(pageFilterDTO);
            }
        }
        return list;
    }
}
