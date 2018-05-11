package com.qiein.jupiter.web.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.qiein.jupiter.constant.DictionaryConstant;
import com.qiein.jupiter.constant.NumberConstant;
import com.qiein.jupiter.enums.DictEnum;
import com.qiein.jupiter.enums.RoleEnum;
import com.qiein.jupiter.util.CollectionUtils;
import com.qiein.jupiter.util.StringUtil;
import com.qiein.jupiter.web.dao.DictionaryDao;
import com.qiein.jupiter.web.dao.GroupDao;
import com.qiein.jupiter.web.dao.PageConfigDao;
import com.qiein.jupiter.web.entity.dto.PageFilterDTO;
import com.qiein.jupiter.web.entity.po.DictionaryPO;
import com.qiein.jupiter.web.entity.po.PageConfig;
import com.qiein.jupiter.web.entity.vo.FilterMapVO;
import com.qiein.jupiter.web.entity.vo.GroupsInfoVO;
import com.qiein.jupiter.web.service.DictionaryService;
import com.qiein.jupiter.web.service.PageConfigService;
import org.apache.commons.collections4.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
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


    /**
     * 根据供公司Id 和 角色获取配置
     *
     * @param companyId
     * @param role
     * @return
     */
    @Override
    public List<PageConfig> listPageConfigByCidAndRole(int companyId, String role) {
        List<PageConfig> pageConfigs = pageConfigDao.listPageConfigByCidAndRole(companyId, role);
        //判断是否为空
        if (CollectionUtils.isEmpty(pageConfigs)) {
            //取默认公司
            pageConfigs = pageConfigDao.listPageConfigByCidAndRole(DictionaryConstant.COMMON_COMPANYID, role);
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
    @Override
    public FilterMapVO getPageFilterMap(int companyId, String role) {
        FilterMapVO pageFilterMap = new FilterMapVO();
        Map<String, List<DictionaryPO>> dictMapByCid = dictionaryService.getDictMapByCid(companyId);
        //todo
        //状态

        //渠道

        //品牌

        //咨询方式
        List<PageFilterDTO> zxModeFilters = getDictFilter(DictEnum.zx_mode, dictMapByCid);
        pageFilterMap.setZxMode(zxModeFilters);
        //意向等级
        List<PageFilterDTO> yxRankFilters = getDictFilter(DictEnum.yx_rank, dictMapByCid);
        pageFilterMap.setYsRange(yxRankFilters);
        //婚期简述
        List<PageFilterDTO> marryRangeFilters = getDictFilter(DictEnum.marry_range, dictMapByCid);
        pageFilterMap.setMarryRange(marryRangeFilters);
        //咨询类型
        List<PageFilterDTO> zxTypeFilters = getDictFilter(DictEnum.zx_type, dictMapByCid);
        pageFilterMap.setZxType(zxTypeFilters);
        //预拍时间简述
        List<PageFilterDTO> ypRangeFilters = getDictFilter(DictEnum.yp_range, dictMapByCid);
        pageFilterMap.setYpRange(ypRangeFilters);
        //预算范围
        List<PageFilterDTO> ysRangeFilters = getDictFilter(DictEnum.ys_range, dictMapByCid);
        pageFilterMap.setYsRange(ysRangeFilters);
        //无效原因
        List<PageFilterDTO> invalidReasonFilters = getDictFilter(DictEnum.invalid_reason, dictMapByCid);
        pageFilterMap.setInvalidReason(invalidReasonFilters);
        //流失原因
        List<PageFilterDTO> runoffReasonFilters = getDictFilter(DictEnum.runoff_reason, dictMapByCid);
        pageFilterMap.setRunoffReason(runoffReasonFilters);
        //客服小组，根据不同的角色
        List<GroupsInfoVO> companyDeptListByType = groupDao.getCompanyDeptListByType(role, companyId);
        List<PageFilterDTO> companyDeptListByTypeFilters = new ArrayList<>();
        for (GroupsInfoVO groupsInfoVO : companyDeptListByType) {
            PageFilterDTO PageFilterDTO = new PageFilterDTO();
            PageFilterDTO.setLabel(groupsInfoVO.getGroupName());
            PageFilterDTO.setValue(groupsInfoVO.getGroupId());
            companyDeptListByTypeFilters.add(PageFilterDTO);
        }
        pageFilterMap.setDept(companyDeptListByTypeFilters);
        //推广

        //筛选

        //客服

        //门市

        //门店

        return pageFilterMap;
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
            //JSON 就是当前表头
            //TODO magic变量等
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
    private List<PageFilterDTO> getDictFilter(DictEnum type, Map<String, List<DictionaryPO>> dictMapByCid) {
        //遍历生成
        List<PageFilterDTO> list = new ArrayList<>();
        List<DictionaryPO> dictionaryPOS = dictMapByCid.get(type.name());
        //空判断
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
