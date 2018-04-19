package com.qiein.jupiter.web.controller;

import com.qiein.jupiter.aop.annotation.NotEmpty;
import com.qiein.jupiter.constant.TipMsgConstant;
import com.qiein.jupiter.exception.ExceptionEnum;
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

    @GetMapping("/edit_group_weight")
    public ResultInfo editGroupWeight(@NotEmpty @RequestParam("relaId") int relaId, @NotEmpty @RequestParam("weight") int weight) {
        if (weight > 20 || weight < 0) {
            ResultInfoUtil.error(ExceptionEnum.WEIGHT_ERROR);
        }
        //获取当前登录用户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        shopChannelGroupService.editGroupWeight(currentLoginStaff.getCompanyId(), relaId, weight);
        return ResultInfoUtil.success(TipMsgConstant.EDIT_SUCCESS);
    }

    @GetMapping("/delete_group")
    public ResultInfo deleteGroup(@NotEmpty @RequestParam("relaIds") String relaIds) {
        //获取当前登录用户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        shopChannelGroupService.batchDeleteGroup(currentLoginStaff.getCompanyId(), relaIds);
        return ResultInfoUtil.success(TipMsgConstant.DELETE_SUCCESS);
    }

    @GetMapping("/delete_channel_rela_list")
    public ResultInfo deleteChannelRelaList(@NotEmpty @RequestParam("channelId") int channelId, @NotEmpty @RequestParam("shopId") int shopId) {
        //获取当前登录用户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        shopChannelGroupService.deleteChannelList(currentLoginStaff.getCompanyId(), channelId, shopId);
        return ResultInfoUtil.success(TipMsgConstant.DELETE_SUCCESS);
    }

    @GetMapping("/add_channel_rela_list")
    public ResultInfo addChannelRelaList(@NotEmpty @RequestParam("shopId") int shopId, @NotEmpty @RequestParam("weight") int weight,
                                         @NotEmpty @RequestParam("channelIds") String channelIds, @NotEmpty @RequestParam("groupIds") String groupIds) {
        //获取当前登录用户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        shopChannelGroupService.batchAddChannelList(currentLoginStaff.getCompanyId(), shopId, weight, channelIds, groupIds);
        return ResultInfoUtil.success(TipMsgConstant.SAVE_SUCCESS);
    }

    @GetMapping("/edit_channel_group")
    public ResultInfo editChannelGroup(@NotEmpty @RequestParam("relaId") int relaId, @NotEmpty @RequestParam("groupId") String groupId,
                                       @NotEmpty @RequestParam("shopId") int shopId, @NotEmpty @RequestParam("channelId") int channelId) {
        //获取当前登录用户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        shopChannelGroupService.editChannelGroup(relaId, currentLoginStaff.getCompanyId(), channelId, shopId, groupId);
        return ResultInfoUtil.success(TipMsgConstant.EDIT_SUCCESS);
    }

}
