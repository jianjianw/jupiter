package com.qiein.jupiter.web.controller;

import com.qiein.jupiter.util.ObjectUtil;
import com.qiein.jupiter.util.ResultInfo;
import com.qiein.jupiter.web.entity.po.ChannelPo;
import com.qiein.jupiter.web.entity.po.StaffPO;
import com.qiein.jupiter.web.service.ChannelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 渠道Controller
 */
@RestController
@RequestMapping("/channel")
public class ChannelController extends BaseController{

    @Autowired
    private ChannelService channelService;

    /**
     * 新增渠道
     * @param channelPo
     * @return
     */
    public ResultInfo insert(@RequestBody @Validated ChannelPo channelPo){
        //获取当前登录用户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        //设置cid
        channelPo.setCompanyId(currentLoginStaff.getCompanyId());
        //对象参数去空
        ObjectUtil.objectStrParamTrim(channelPo);

        channelService.createChannel(channelPo);
        return null;
    }

}
