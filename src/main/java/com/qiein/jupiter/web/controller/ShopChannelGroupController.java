package com.qiein.jupiter.web.controller;

import com.qiein.jupiter.aop.annotation.NotEmpty;
import com.qiein.jupiter.util.ResultInfo;
import com.qiein.jupiter.util.ResultInfoUtil;
import com.qiein.jupiter.web.entity.po.StaffPO;
import com.qiein.jupiter.web.service.ShopChannelGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 拍摄地，渠道，小组关联管理
 */
@RestController
@RequestMapping("/shop_channel_group")
public class ShopChannelGroupController extends BaseController {

    @Autowired
    private ShopChannelGroupService shopChannelGroupService;//拍摄地，渠道，小组关联业务层

    @GetMapping("/get_channel_group_list")
    public ResultInfo getChannelGroupList(@NotEmpty @RequestParam("shopId") int shopId) {
        //获取当前登录用户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        return ResultInfoUtil.success(shopChannelGroupService.getShopChannelGroupList(currentLoginStaff.getCompanyId(), shopId));
    }
}
