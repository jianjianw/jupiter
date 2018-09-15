package com.qiein.jupiter.web.controller;

import com.qiein.jupiter.util.ResultInfo;
import com.qiein.jupiter.util.ResultInfoUtil;
import com.qiein.jupiter.web.service.PlatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: shiTao
 */
@RestController
@RequestMapping("/plat")
public class PlatController extends BaseController {
    @Autowired
    private PlatService platService;

    /**
     * 页面搜索 根据Key
     *
     * @return
     */
    @GetMapping("/page_search_by_key")
    public ResultInfo pageSearchByKey(String key) {
        return ResultInfoUtil.success(platService.pageSearch(getCurrentLoginStaff().getCompanyId(), key));
    }
}
