package com.qiein.jupiter.web.controller;

import com.qiein.jupiter.enums.TipMsgEnum;
import com.qiein.jupiter.util.ObjectUtil;
import com.qiein.jupiter.util.ResultInfo;
import com.qiein.jupiter.util.ResultInfoUtil;
import com.qiein.jupiter.web.entity.po.PageConfig;
import com.qiein.jupiter.web.entity.po.StaffPO;
import com.qiein.jupiter.web.service.PageConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 页面table 表头的配置
 */
@RestController
@RequestMapping("/page_config")
public class PageConfigController extends BaseController {

    @Autowired
    private PageConfigService pageConfigService;

    /**
     * 根据公司和当前角色获取表头配置
     *
     * @param role
     * @return
     */
    @GetMapping("/get_by_cid_and_role")
    public ResultInfo getPageConfigByCidAndRole(@RequestParam String role, @RequestParam boolean showFlag) {
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        List<PageConfig> pageConfigs =
                pageConfigService.listPageConfigByCidAndRole(currentLoginStaff.getCompanyId(), role, showFlag);
        return ResultInfoUtil.success(pageConfigs);
    }

    /**
     * 获取页面所有的filters map
     *
     * @return
     */
    @GetMapping("/get_page_filter_map")
    public ResultInfo getAllPageFilterMap(@RequestParam String role) {
        StaffPO curStaff = getCurrentLoginStaff();
        return ResultInfoUtil.success(
                pageConfigService.getPageFilterMap(curStaff.getCompanyId(), role)
        );
    }

    /**
     * 修改页面配置
     *
     * @param pageConfig
     * @return
     */
    @PostMapping("/save_page_config")
    public ResultInfo savePageConfig(@RequestBody PageConfig pageConfig) {
        ObjectUtil.objectStrParamTrim(pageConfig);
        StaffPO curStaff = getCurrentLoginStaff();
        pageConfig.setCompanyId(curStaff.getCompanyId());
        pageConfigService.updatePageConfig(pageConfig);
        return ResultInfoUtil.success(TipMsgEnum.UPDATE_SUCCESS);
    }
}
