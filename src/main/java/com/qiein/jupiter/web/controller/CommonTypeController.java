package com.qiein.jupiter.web.controller;

import com.qiein.jupiter.aop.validate.annotation.Id;
import com.qiein.jupiter.enums.TipMsgEnum;
import com.qiein.jupiter.util.ResultInfo;
import com.qiein.jupiter.util.ResultInfoUtil;
import com.qiein.jupiter.web.entity.po.CommonTypePO;
import com.qiein.jupiter.web.entity.po.StaffPO;
import com.qiein.jupiter.web.entity.vo.CommonTypeChannelVO;
import com.qiein.jupiter.web.entity.vo.CommonTypeVO;
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
public class CommonTypeController extends BaseController {
    @Autowired
    private CommonTypeSerivce commonTypeSerivce;

    /**
     * 添加拍摄类型
     */
    @GetMapping("/add_photo_type")
    public ResultInfo addPhotoType(@RequestParam String commonType) {
        StaffPO staff = getCurrentLoginStaff();
        commonTypeSerivce.addPhotoType(commonType, staff.getCompanyId());
        return ResultInfoUtil.success(TipMsgEnum.SAVE_SUCCESS);
    }

    /**
     * 批量加入拍摄类型渠道
     */
    @PostMapping("/add_type_channel_group")
    public ResultInfo addTypeChannelGroup(@RequestBody CommonTypePO commonTypePO) {
        StaffPO staff = getCurrentLoginStaff();
        commonTypePO.setCompanyId(staff.getCompanyId());
        commonTypeSerivce.addTypeChannelGroup(commonTypePO);
        return ResultInfoUtil.success();
    }

    /**
     * 批量删除
     */
    @GetMapping("/delete_type_channel_group")
    public ResultInfo deleteTypeChannelGroup(@RequestParam String ids) {
        commonTypeSerivce.deleteTypeChannelGroup(ids);
        return ResultInfoUtil.success(TipMsgEnum.DELETE_SUCCESS);
    }

    /**
     * 修改具体属性
     */
    @PostMapping("/edit_type_channel_group")
    public ResultInfo editTypeChannelGroup(@RequestBody CommonTypePO commonTypePO) {
        StaffPO staff = getCurrentLoginStaff();
        commonTypePO.setCompanyId(staff.getCompanyId());
        commonTypeSerivce.editTypeChannelGroup(commonTypePO);
        return ResultInfoUtil.success(TipMsgEnum.UPDATE_SUCCESS);
    }

    /**
     * 获取拍摄方式渠道的页面
     */
    @GetMapping("/find_common_type_channel")
    public ResultInfo findCommonTypeChannel(@Id @RequestParam("typeId") int typeId) {
        StaffPO staffPO = getCurrentLoginStaff();
        return ResultInfoUtil.success(commonTypeSerivce.findChannelGroup(typeId, staffPO.getCompanyId()));
    }

    /**
     * 查询拍摄类型
     */
    @GetMapping("/find_common_type")
    public ResultInfo findCommonType() {
        StaffPO staffPO = getCurrentLoginStaff();
        List<CommonTypeVO> list = commonTypeSerivce.findCommonType(staffPO.getCompanyId());
        return ResultInfoUtil.success(list);
    }


    /**
     * 根据来源id删除
     */
    @GetMapping("/delete_by_channel_id")
    public ResultInfo deleteByChannelId(@RequestParam Integer channelId, @RequestParam Integer typeId) {
        StaffPO staffPO = getCurrentLoginStaff();
        commonTypeSerivce.deleteByChannelId(channelId, typeId, staffPO.getCompanyId());
        return ResultInfoUtil.success(TipMsgEnum.DELETE_SUCCESS);
    }

    /**
     * 根据来源id查找
     *
     * @param channelId
     * @param typeId
     * @param groupName
     * @return
     */
    @GetMapping("/search_by_channel_id")
    public ResultInfo searchByChannelId(@RequestParam Integer channelId, @RequestParam Integer typeId, @RequestParam String groupName) {
        StaffPO staffPO = getCurrentLoginStaff();

        return ResultInfoUtil.success(commonTypeSerivce.searchByChannelId(channelId, typeId, staffPO.getCompanyId(), groupName));
    }


    @GetMapping("edit_weight")
    public ResultInfo editWeight(@RequestParam String ids, @RequestParam Integer weight) {
        commonTypeSerivce.editWeight(ids, weight);
        return ResultInfoUtil.success(TipMsgEnum.EDIT_SUCCESS);
    }
}
