package com.qiein.jupiter.web.controller;

import com.github.pagehelper.PageInfo;
import com.qiein.jupiter.util.ResultInfo;
import com.qiein.jupiter.util.ResultInfoUtil;
import com.qiein.jupiter.web.entity.po.StaffPO;
import com.qiein.jupiter.web.service.WxmpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 小程序
 *
 * @Author: shiTao
 */
@RestController
@RequestMapping("/wxmp")
public class WxmpController extends BaseController {

    @Autowired
    private WxmpService wxmpService;

    /**
     * 根据关键词搜索客资
     *
     * @return
     */
    @GetMapping("/search_client_by_key")
    public ResultInfo searchClientInfoByKey(int page, int size, String key) {
        StaffPO staff = getCurrentLoginStaff();
        PageInfo pageInfo = wxmpService.searchClient(staff.getCompanyId(), key, page, size);
        return ResultInfoUtil.success(pageInfo);
    }
}
