package com.qiein.jupiter.web.controller;

import com.qiein.jupiter.enums.TigMsgEnum;
import com.qiein.jupiter.util.ResultInfo;
import com.qiein.jupiter.util.ResultInfoUtil;
import com.qiein.jupiter.web.entity.po.CommonTypePO;
import com.qiein.jupiter.web.entity.po.StaffPO;
import com.qiein.jupiter.web.entity.vo.CommonTypeChannelShowVO;
import com.qiein.jupiter.web.entity.vo.CommonTypeChannelVO;
import com.qiein.jupiter.web.service.CommonTypeSerivce;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 拍摄类型渠道
 * Author xiangliang 2018/6/15
 */
@RestController
@RequestMapping("/common_type")
@Validated
public class CommonTypeController extends BaseController{
    @Autowired
    private CommonTypeSerivce commonTypeSerivce;
    /**
     * 添加拍摄类型
     */
    @GetMapping("/add_photo_type")
    public ResultInfo addPhotoType(@RequestParam String commonType){
        StaffPO staff=getCurrentLoginStaff();
        commonTypeSerivce.addPhotoType(commonType,staff.getCompanyId());
        return ResultInfoUtil.success(TigMsgEnum.SAVE_SUCCESS);
    }

    /**
     * 批量加入拍摄类型渠道
     */
    @PostMapping("/add_type_channel_group")
    public ResultInfo addTypeChannelGroup(@RequestBody CommonTypePO commonTypePO){
        StaffPO staff=getCurrentLoginStaff();
        commonTypePO.setCompanyId(staff.getCompanyId());
        commonTypeSerivce.addTypeChannelGroup(commonTypePO);
        return ResultInfoUtil.success();
    }

    /**
     * 批量删除
     */
    @GetMapping("/delete_type_channel_group")
    public ResultInfo deleteTypeChannelGroup(@RequestParam String ids){
        commonTypeSerivce.deleteTypeChannelGroup(ids);
        return ResultInfoUtil.success(TigMsgEnum.DELETE_SUCCESS);
    }

    /**
    * 修改具体属性
     */
    @PostMapping("/edit_type_channel_group")
    public ResultInfo editTypeChannelGroup(@RequestBody CommonTypePO commonTypePO){
        StaffPO staff=getCurrentLoginStaff();
        commonTypePO.setCompanyId(staff.getCompanyId());
        commonTypeSerivce.editTypeChannelGroup(commonTypePO);
        return ResultInfoUtil.success(TigMsgEnum.UPDATE_SUCCESS);
    }

    /**
     * 获取拍摄方式渠道的页面
     */
    @GetMapping("/find_common_type_channel")
    public ResultInfo findCommonTypeChannel(@RequestParam String typeId){
        StaffPO staffPO=getCurrentLoginStaff();
        if(typeId.equals("")){
            CommonTypeChannelShowVO commonTypeChannelShowVO =commonTypeSerivce.findChannelGroupFirst(staffPO.getCompanyId());
            return ResultInfoUtil.success(commonTypeChannelShowVO);
        }
        List<CommonTypeChannelVO> list =commonTypeSerivce.findChannelGroup(Integer.parseInt(typeId),staffPO.getCompanyId());
        return ResultInfoUtil.success(list);
    }

    /**
     * 根据来源id删除
     */
    @GetMapping("/delete_by_channel_id")
    public ResultInfo deleteByChannelId(@RequestParam Integer channelId,@RequestParam Integer typeId){
        StaffPO staffPO=getCurrentLoginStaff();
        commonTypeSerivce.deleteByChannelId(channelId,typeId,staffPO.getCompanyId());
        return  ResultInfoUtil.success(TigMsgEnum.DELETE_SUCCESS);
    }

    @GetMapping("/search_by_channel_id")
    public ResultInfo searchByChannelId(@RequestParam Integer channelId,@RequestParam Integer typeId,@RequestParam String groupName){
        StaffPO staffPO=getCurrentLoginStaff();

        return ResultInfoUtil.success(commonTypeSerivce.searchByChannelId(channelId,typeId,staffPO.getCompanyId(),groupName));
    }

}
