package com.qiein.jupiter.web.controller;

import com.qiein.jupiter.aop.annotation.NotEmpty;
import com.qiein.jupiter.constant.ChannelConstant;
import com.qiein.jupiter.constant.TipMsgConstant;
import com.qiein.jupiter.util.ObjectUtil;
import com.qiein.jupiter.util.ResultInfo;
import com.qiein.jupiter.util.ResultInfoUtil;
import com.qiein.jupiter.web.entity.po.ChannelPO;
import com.qiein.jupiter.web.entity.po.StaffPO;
import com.qiein.jupiter.web.service.ChannelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 渠道Controller
 */
@RestController
@RequestMapping("/channel")
public class ChannelController extends BaseController{

    @Autowired
    private ChannelService channelService;  //渠道业务层对象

    /**
     * 新增渠道
     * @param channelPO
     * @return
     */
    @PostMapping("/add")
    public ResultInfo addChannel(@RequestBody @Validated ChannelPO channelPO){
        //获取当前登录用户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        //设置cid
        channelPO.setCompanyId(currentLoginStaff.getCompanyId());
        //对象参数去空
        ObjectUtil.objectStrParamTrim(channelPO);

        channelService.createChannel(channelPO);
        return ResultInfoUtil.success(TipMsgConstant.ADD_CHANNEL_SUCCESS);
    }

    /**
     * 编辑渠道
     * @param channelPO
     * @return
     */
    @PostMapping("/edit")
    public ResultInfo editChannel(@RequestParam ChannelPO channelPO){
        //获取当前登录用户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        //设置cid
        channelPO.setCompanyId(currentLoginStaff.getCompanyId());
        //对象参数去空
        ObjectUtil.objectStrParamTrim(channelPO);
        channelService.editChannel(channelPO);
        return ResultInfoUtil.success(TipMsgConstant.EDIT_CHANNEL_SUCCESS);
    }

    /**
     * 删除渠道
     * @param id
     * @return
     */
    @GetMapping("/del")
    public ResultInfo delChannel(@NotEmpty int id){
        //获取当前登录用户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        //获取所属公司编号
        Integer companyId = currentLoginStaff.getCompanyId();
        channelService.delChannel(id,companyId);
        return ResultInfoUtil.success(TipMsgConstant.DEL_CHANNEL_SUCCESS);
    }

    @GetMapping("/sel_channel")
    public ResultInfo queryChannel(){

        return null;
    }

    /**
     * 根据渠道细分类型获取渠道列表
     * 1:纯电商，2:电商转介绍，3:员工转介绍，4:指名转介绍，5:外部转介绍，6:自然入客，7:门店外展
     * @return
     */
    @GetMapping("/get_list_by_type")
    public ResultInfo getAuthList(@NotEmpty int typeId){
        //获取当前登录用户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        //获取所属公司编号
        Integer companyId = currentLoginStaff.getCompanyId();

        return ResultInfoUtil.success(TipMsgConstant.SUCCESS,channelService.getListByType(typeId,companyId));
    }

    /**
     * 获取渠道列表接口
     * 1:纯电商，2:电商转介绍，3:员工转介绍，4:指名转介绍，5:外部转介绍，6:自然入客，7:门店外展
     * @param type_id 0：全部渠道 1：电商 2：转介绍 3：自然入客
     * @return
     */
    @GetMapping("/get_list")
    public ResultInfo getChannelList(@NotEmpty int type_id){
        //获取当前登录用户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        //获取所属公司编号
        Integer companyId = currentLoginStaff.getCompanyId();

        return ResultInfoUtil.success(TipMsgConstant.SUCCESS, getSrcList(companyId,type_id));
    }

    /**
     * 获取渠道列表方法
     * @param companyId
     * @param iTypeId
     * @return
     */
    private List<ChannelPO> getSrcList(Integer companyId, Integer iTypeId) {
        List<ChannelPO> list=null;
        switch (iTypeId) {
            case 0:
                // 获取全部渠道组列表
                list=channelService.getChannelList( ChannelConstant.DS_TYPE_LIST,companyId);
                break;
            case 1:
                // 获取电商渠道组列表
                list=channelService.getChannelList( ChannelConstant.DS_TYPE_LIST,companyId);
                break;
            case 2:
                // 获取转介绍渠道组列表
                list=channelService.getChannelList( ChannelConstant.ZJS_TYPE_LIST,companyId);
                break;
            case 3:
                // 获取自然渠道组列表
                list=channelService.getChannelList( ChannelConstant.ZR_TYPE_LIST,companyId);
                break;
            default:
                break;
        }
        return list;
    }

}
