package com.qiein.jupiter.web.controller;

import com.qiein.jupiter.util.ResultInfo;
import com.qiein.jupiter.util.ResultInfoUtil;
import com.qiein.jupiter.web.entity.po.PageConfig;
import com.qiein.jupiter.web.entity.po.StaffPO;
import com.qiein.jupiter.web.service.PageConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 页面配置
 */
@RestController
@RequestMapping("/page_config")
public class PageConfigController extends BaseController {

    @Autowired
    private PageConfigService pageConfigService;

    /**
     * @param role
     * @return
     */
    @GetMapping("/get_by_cid_and_role")
    public ResultInfo getPageConfigByCidAndRole(@RequestParam String role) {
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        List<PageConfig> pageConfigs =
                pageConfigService.listPageConfigByCidAndRole(currentLoginStaff.getCompanyId(), role);
        return ResultInfoUtil.success(pageConfigs);
    }

    /**
     * 获取页面所有的filters map
     *
     * @return
     */
    @GetMapping("/get_all_page_filter_map")
    public ResultInfo getAllPageFilterMap() {
        return null;
    }
}
