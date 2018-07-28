package com.qiein.jupiter.web.controller;

import com.qiein.jupiter.aop.validate.annotation.Id;
import com.qiein.jupiter.aop.validate.annotation.NotEmptyStr;
import com.qiein.jupiter.constant.ChannelConstant;
import com.qiein.jupiter.enums.TipMsgEnum;
import com.qiein.jupiter.exception.ExceptionEnum;
import com.qiein.jupiter.exception.RException;
import com.qiein.jupiter.util.*;
import com.qiein.jupiter.web.entity.dto.RequestInfoDTO;
import com.qiein.jupiter.web.entity.po.ChannelPO;
import com.qiein.jupiter.web.entity.po.StaffPO;
import com.qiein.jupiter.web.entity.po.SystemLog;
import com.qiein.jupiter.web.service.ChannelService;
import com.qiein.jupiter.web.service.SourceService;
import com.qiein.jupiter.web.service.SystemLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 渠道Controller
 */
@RestController
@RequestMapping("/channel")
@Validated
public class ChannelController extends BaseController {

    @Autowired
    private ChannelService channelService;

    @Autowired
    private SourceService sourceService;
    @Autowired
    private SystemLogService logService;

    /**
     * 新增渠道
     *
     * @param channelPO
     * @return
     */
    @PostMapping("/add")
    public ResultInfo addChannel(@RequestBody @Validated ChannelPO channelPO) {
        // 获取当前登录用户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        // 设置cid
        channelPO.setCompanyId(currentLoginStaff.getCompanyId());
        // 对象参数去空
        ObjectUtil.objectStrParamTrim(channelPO);
        channelService.createChannel(channelPO);
        //添加日志
        try {
            RequestInfoDTO requestInfo = getRequestInfo();
            // 日志记录
            SystemLog log = new SystemLog(SysLogUtil.LOG_TYPE_CHANNEL, requestInfo.getIp(), requestInfo.getUrl(), currentLoginStaff.getId(),
                    currentLoginStaff.getNickName(), SysLogUtil.getAddLog(SysLogUtil.LOG_SUP_CHANNEL, channelPO.getChannelName()), currentLoginStaff.getCompanyId());
            logService.addLog(log);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultInfoUtil.success(TipMsgEnum.ADD_CHANNEL_SUCCESS);
        }
        return ResultInfoUtil.success(TipMsgEnum.ADD_CHANNEL_SUCCESS);
    }

    /**
     * 编辑渠道
     *
     * @param channelPO
     * @return
     */
    @PostMapping("/edit")
    public ResultInfo editChannel(@RequestBody ChannelPO channelPO) {
        // 获取当前登录用户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        // 设置cid
        channelPO.setCompanyId(currentLoginStaff.getCompanyId());
        // 对象参数去空
        ObjectUtil.objectStrParamTrim(channelPO);
        if (StringUtil.isEmpty(String.valueOf(channelPO.getId())))
            throw new RException(ExceptionEnum.CHANNEL_ID_NULL);
        channelService.editChannel(channelPO);
        return ResultInfoUtil.success(TipMsgEnum.EDIT_CHANNEL_SUCCESS);
    }

    /**
     * 编辑渠道排序
     *
     * @param fId
     * @param fPriority
     * @param sId
     * @param sPriority
     * @return
     */
    @GetMapping("/priority")
    public ResultInfo editPriority(@Id Integer fId, @Id Integer fPriority, @Id Integer sId, @Id Integer sPriority) {
        channelService.editProiority(fId, fPriority, sId, sPriority, getCurrentLoginStaff().getCompanyId());
        return ResultInfoUtil.success(TipMsgEnum.EDIT_SUCCESS);
    }

    /**
     * 删除渠道
     *
     * @param id
     * @return
     */
    @GetMapping("/del")
    public ResultInfo delChannel(@Id Integer id) {
        // 获取当前登录用户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        // 获取所属公司编号
        Integer companyId = currentLoginStaff.getCompanyId();
        channelService.delChannel(id, companyId);
        RequestInfoDTO requestInfo = getRequestInfo();
        try {
            logService.addLog(new SystemLog(SysLogUtil.LOG_TYPE_CHANNEL, requestInfo.getIp(), requestInfo.getUrl(), currentLoginStaff.getId(),
                    currentLoginStaff.getNickName(), SysLogUtil.getRemoveLog(SysLogUtil.LOG_SUP_CHANNEL, id + ""),
                    currentLoginStaff.getCompanyId()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResultInfoUtil.success(TipMsgEnum.DEL_CHANNEL_SUCCESS);
        }
        return ResultInfoUtil.success(TipMsgEnum.DEL_CHANNEL_SUCCESS);
    }

    /**
     * 根据渠道编号获取旗下所有渠道
     *
     * @param id
     * @return
     */
    @GetMapping("/sel_channel")
    public ResultInfo queryChannel(int id) {
        // 获取当前登录用户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        // 获取所属公司编号
        Integer companyId = currentLoginStaff.getCompanyId();
        return ResultInfoUtil.success(TipMsgEnum.SUCCESS, sourceService.getSourceListByChannelId(id, companyId));
    }

    /**
     * 根据渠道细分类型获取渠道列表 1:纯电商，2:电商转介绍，3:员工转介绍，4:指名转介绍，5:外部转介绍，6:自然入客，7:门店外展
     *
     * @return
     */
    @GetMapping("/get_list_by_type")
    public ResultInfo getAuthList(@Id Integer typeId) {
        // 获取当前登录用户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        // 获取所属公司编号
        Integer companyId = currentLoginStaff.getCompanyId();

        return ResultInfoUtil.success(TipMsgEnum.SUCCESS, channelService.getListByType(typeId, companyId));
    }

    /**
     * 获取渠道列表接口 1:纯电商，2:电商转介绍，3:员工转介绍，4:指名转介绍，5:外部转介绍，6:自然入客，7:门店外展
     *
     * @param typeId 0：全部渠道 1：电商 2：转介绍 3：自然入客
     * @return
     */
    @GetMapping("/get_list")
    public ResultInfo getChannelList(@Id Integer typeId) {
        // 获取当前登录用户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        // 获取所属公司编号
        Integer companyId = currentLoginStaff.getCompanyId();
        return ResultInfoUtil.success(TipMsgEnum.SUCCESS, getSrcList(companyId, typeId));
    }

    /**
     * 获取渠道列表方法
     *
     * @param companyId
     * @param iTypeId
     * @return
     */
    private List<ChannelPO> getSrcList(Integer companyId, Integer iTypeId) {
        List<ChannelPO> list = null;
        switch (iTypeId) {
            case 0:
                // 获取全部渠道组列表
                list = channelService.getChannelList(ChannelConstant.DS_TYPE_LIST, companyId);
                break;
            case 1:
                // 获取电商渠道组列表
                list = channelService.getChannelList(ChannelConstant.DS_TYPE_LIST, companyId);
                break;
            case 2:
                // 获取转介绍渠道组列表
                list = channelService.getChannelList(ChannelConstant.ZJS_TYPE_LIST, companyId);
                break;
            case 3:
                // 获取自然渠道组列表
                list = channelService.getChannelList(ChannelConstant.ZR_TYPE_LIST, companyId);
                break;
            default:
                break;
        }
        return list;
    }

    /**
     * 获取企业各角色页面，头部渠道组及渠道下拉框筛选
     *
     * @param role
     * @return
     */
    @GetMapping("/get_channel_source_select_by_role")
    public ResultInfo getChannelSourceSelectByRole(@NotEmptyStr @RequestParam("role") String role) {
        // 获取当前登录用户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        return ResultInfoUtil
                .success(channelService.getChannelSourceListByType(currentLoginStaff.getCompanyId(), role));
    }

    /**
     * 获取员工各角色录入页面，渠道来源下拉框选项，根据个人上月使用频率排序
     *
     * @param role
     * @return
     */
    @GetMapping("/get_my_channel_source_by_role")
    public ResultInfo getMyChannelSourceByRole(@NotEmptyStr @RequestParam("role") String role) {
        // 获取当前登录用户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        return ResultInfoUtil.success(channelService.getChannelSourceListByType(currentLoginStaff.getCompanyId(), role));
    }

    /**
     * 根据员工所在小组获取该组承接的渠道列表
     *
     * @return
     */
    @GetMapping("/get_my_group_channel_list")
    public ResultInfo getChannelListByStaffGroup(String groupId) {
        // 获取当前登录用户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        return ResultInfoUtil.success(channelService.getChannelListByStaffGroup(currentLoginStaff.getCompanyId(), groupId));
    }

    /**
     * 获取企业所有渠道列表
     *
     * @return
     */
    @GetMapping("/get_all_channel_source_select")
    public ResultInfo getAllChannelSourceSelect() {
        // 获取当前登录用户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        return ResultInfoUtil
                .success(channelService.getAllChannelSourceList(currentLoginStaff.getCompanyId()));
    }

    /**
     * 获取详情渠道下拉框，关闭的也显示
     *
     * @param role
     * @return
     */
    @GetMapping("/get_all_source_list_by_role")
    public ResultInfo getAllSourceListByRole(@NotEmptyStr @RequestParam("role") String role) {
        // 获取当前登录用户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        return ResultInfoUtil
                .success(channelService.getAllChannelSourceListByType(currentLoginStaff.getCompanyId(), role));
    }


    /**
     * 获取企业所有电商渠道列表
     */
    @GetMapping("/get_ds_all_channel")
    public ResultInfo getDsAllChannel() {
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        return ResultInfoUtil.success(channelService.getDsAllChannel(currentLoginStaff.getCompanyId()));
    }

    /**
     * 功能描述:
     * 获取外部转介绍渠道及小组
     *
     * @auther: Tt(yehuawei)
     * @date:
     * @param:
     * @return:
     */
    @GetMapping("/get_out_zjs")
    public ResultInfo getCompanyOutZjsChannelAndSource() {
        return ResultInfoUtil.success(channelService.getCompanyOutZjsChannelAndSource(getCurrentLoginStaff().getCompanyId()));
    }
}