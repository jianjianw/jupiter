package com.qiein.jupiter.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.qiein.jupiter.aop.validate.annotation.NotEmptyStr;
import com.qiein.jupiter.constant.ClientZjsMenuConst;
import com.qiein.jupiter.constant.CompanyConst;
import com.qiein.jupiter.enums.TipMsgEnum;
import com.qiein.jupiter.exception.ExceptionEnum;
import com.qiein.jupiter.util.*;
import com.qiein.jupiter.web.entity.dto.DsinvalDTO;
import com.qiein.jupiter.web.entity.dto.RequestInfoDTO;
import com.qiein.jupiter.web.entity.po.*;
import com.qiein.jupiter.web.service.CompanyService;
import com.qiein.jupiter.web.service.SystemLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 公司
 */
@RestController
@RequestMapping("/company")
public class CompanyController extends BaseController {

    @Resource
    private CompanyService companyService;

    @Autowired
    private SystemLogService logService;

    /**
     * 编辑企业信息
     *
     * @param companyPO
     * @return
     */
    @PostMapping("/edit")
    public ResultInfo editCompanyInfo(@RequestBody CompanyPO companyPO) {
        companyService.update(companyPO);
        return ResultInfoUtil.success(TipMsgEnum.EDIT_SUCCESS);
    }

    /**
     * 同一个账号不能再不同电脑登录
     *
     * @param flag
     * @return
     */
    @GetMapping("/ssolimit")
    public ResultInfo editCompanySsolimit(@NotEmptyStr @RequestParam("flag") String flag) {
        companyService.updateFlag(getCurrentLoginStaff().getCompanyId(), CompanyConst.COLUMN_SSOLIMIT,
                Boolean.valueOf(flag));
        return ResultInfoUtil.success(TipMsgEnum.EDIT_SUCCESS);
    }

    /**
     * 客服领取客资倒计时时间
     *
     * @return
     */
    @GetMapping("/overtime")
    public ResultInfo editCompanyOvertime(@NotEmptyStr @RequestParam("num") Integer num) {
        companyService.updateRange(getCurrentLoginStaff().getCompanyId(), CompanyConst.COLUMN_OVERTIME, num);
        return ResultInfoUtil.success(TipMsgEnum.EDIT_SUCCESS);
    }

    /**
     * 客服每日领单默认限额
     */
    @GetMapping("/limitdefault")
    public ResultInfo editCompanyLimitdefault(@NotEmptyStr @RequestParam("num") Integer num) {
        companyService.updateRange(getCurrentLoginStaff().getCompanyId(), CompanyConst.COLUMN_LIMITDEFAULT, num);
        return ResultInfoUtil.success(TipMsgEnum.EDIT_SUCCESS);
    }

    /**
     * 客服领取客资指定时间不能再领取下一个
     */
    @GetMapping("/kzinterval")
    public ResultInfo editCompanyKzinterval(@NotEmptyStr @RequestParam("num") Integer num) {
        companyService.updateRange(getCurrentLoginStaff().getCompanyId(), CompanyConst.COLUMN_KZINTERVAL, num);
        return ResultInfoUtil.success(TipMsgEnum.EDIT_SUCCESS);
    }

    /**
     * 员工个人不能自己操作上线离线
     *
     * @param flag
     * @return
     */
    @GetMapping("/notselfblind")
    public ResultInfo editCompanyNotselfblind(@NotEmptyStr @RequestParam("flag") String flag) {
        companyService.updateFlag(getCurrentLoginStaff().getCompanyId(), CompanyConst.COLUMN_NOTSELFBLIND,
                Boolean.valueOf(flag));
        return ResultInfoUtil.success(TipMsgEnum.EDIT_SUCCESS);
    }

    /**
     * 非个人客资信息脱敏显示
     *
     * @param flag
     * @return
     */
    @GetMapping("/unableselfline")
    public ResultInfo editCompanyUnableselfline(@NotEmptyStr @RequestParam("flag") String flag) {
        companyService.updateFlag(getCurrentLoginStaff().getCompanyId(), CompanyConst.COLUMN_UNABLESELFLINE,
                Boolean.valueOf(flag));
        return ResultInfoUtil.success(TipMsgEnum.EDIT_SUCCESS);
    }

    /**
     * 电商客资录入时不能直接指定客服
     *
     * @return
     */
    @GetMapping("/unableappointor")
    public ResultInfo editCompanyUnableappointor(@NotEmptyStr @RequestParam("flag") String flag) {
        companyService.updateFlag(getCurrentLoginStaff().getCompanyId(), CompanyConst.COLUMN_UNABLEAPPOINTOR,
                Boolean.valueOf(flag));
        return ResultInfoUtil.success(TipMsgEnum.EDIT_SUCCESS);
    }

    /**
     * 电商客资录入指定时间不能反无效
     *
     * @return
     */
    @GetMapping("/unableinvalidrange")
    public ResultInfo editCompanyUnableinvalidrange(@NotEmptyStr @RequestParam("num") Integer num) {
        companyService.updateRange(getCurrentLoginStaff().getCompanyId(), CompanyConst.COLUMN_UNABLEINVALIDRANGE, num);
        return ResultInfoUtil.success(TipMsgEnum.EDIT_SUCCESS);
    }

    /**
     * 获取企业信息
     *
     * @return
     */
    @GetMapping("/info")
    public ResultInfo getCompanyInfo() {
        return ResultInfoUtil.success(TipMsgEnum.SUCCESS,
                companyService.getById(getCurrentLoginStaff().getCompanyId()));
    }

    /**
     * 更改咨询类型(客资校验是否忽略咨询类型)
     *
     * @return
     */
    @GetMapping("/editTypeRepeat")
    public ResultInfo editTypeRepeat(@RequestParam("typeRepeat") boolean typeRepeat) {

        companyService.editTypeRepeat(typeRepeat, getCurrentLoginStaff().getCompanyId());

        return ResultInfoUtil.success(typeRepeat ? TipMsgEnum.OPEN_SUCCESS : TipMsgEnum.CLOSE_SUCCESS);
    }

    /**
     * 更改渠道类型(客资校验是否忽略渠道类型)
     *
     * @return
     */
    @GetMapping("/editSrcRepeat")
    public ResultInfo editSrcRepeat(@RequestParam("srcRepeat") boolean srcRepeat) {
        companyService.editTypeSrcRepeat(srcRepeat, getCurrentLoginStaff().getCompanyId());
        return ResultInfoUtil.success(srcRepeat ? TipMsgEnum.OPEN_SUCCESS : TipMsgEnum.CLOSE_SUCCESS);
    }

    /**
     * 更改客资录入时间和最后操作时间,客资状态是否可以重复录
     *
     * @return
     */
    @GetMapping("/editKZday")
    public ResultInfo editKZday(@RequestParam("statusIgnore") String statusIgnore, @RequestParam("timeTypeIgnore") String timeTypeIgnore, @RequestParam("dayIgnore") int dayIgnore) {
        if ((StringUtil.isEmpty(timeTypeIgnore) && NumUtil.isValid(dayIgnore)) || (StringUtil.isNotEmpty(timeTypeIgnore) && NumUtil.isInValid(dayIgnore))) {
            return ResultInfoUtil.error(ExceptionEnum.REPEAT_TIME_ERRPR);
        }
        companyService.editKZday(statusIgnore, timeTypeIgnore, dayIgnore, getCurrentLoginStaff().getCompanyId());
        return ResultInfoUtil.success(TipMsgEnum.SUCCESS);
    }

    /**
     * 查询哪些客资重复被拦截
     *
     * @return
     */
    @GetMapping("/selectAll")
    @ResponseBody
    public ResultInfo selectAll() {
        CompanyPO companyPo = companyService.selectAll(getCurrentLoginStaff().getCompanyId());
        return ResultInfoUtil.success(TipMsgEnum.SUCCESS, companyPo);
    }

    /**
     * 修改无效状态以及待跟踪意向等级
     *
     * @param dsinvalDTO
     */
    @PostMapping("edit_ds_invalid")
    public ResultInfo editDsinvalId(@RequestBody DsinvalDTO dsinvalDTO) {
        StaffPO staff = getCurrentLoginStaff();
        dsinvalDTO.setCompanyId(staff.getCompanyId());
        companyService.editDsInvalid(dsinvalDTO);
        return ResultInfoUtil.success(TipMsgEnum.EDIT_SUCCESS);
    }

    /**
     * 修改待定是否计入有效
     */
    @GetMapping("edit_dd_is_valid")
    public ResultInfo editDsinvalId(@RequestParam("ddIsValid") boolean ddIsValid) {
        StaffPO staff = getCurrentLoginStaff();
        companyService.editDdIsValid(ddIsValid, staff.getCompanyId());
        return ResultInfoUtil.success(ddIsValid ? TipMsgEnum.OPERATE_SUCCESS : TipMsgEnum.CLOSE_SUCCESS);
    }

    /**
     * 搜索无效状态以及跟踪意向等级 转介绍有效指标定义
     *
     * @return
     */
    @GetMapping("find_dsinval_id")
    public ResultInfo findDsinvalId() {
        StaffPO staff = getCurrentLoginStaff();
        return ResultInfoUtil.success(companyService.findDsinvalId(staff.getCompanyId()));
    }

    /**
     * 修改转介绍有效指标定义
     */
    @GetMapping("/edit_zjs_valid_status")
    public ResultInfo editZjsValidStatus(@RequestParam String zjsValidStatus) {
        StaffPO staff = getCurrentLoginStaff();
        companyService.editZjsValidStatus(staff.getCompanyId(), zjsValidStatus);
        return ResultInfoUtil.success(TipMsgEnum.EDIT_SUCCESS);
    }


    /**
     * 功能描述:
     * 获取转介绍录入自定义菜单
     *
     * @auther: Tt(yehuawei)
     * @date:
     * @param:
     * @return:
     */
    @GetMapping("/zjs_menu")
    public ResultInfo getZjsMenu() {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("qy_zjsMenu", ClientZjsMenuConst.QY_ZJS_MENU);
        resultMap.put("lk_zjsMenu", ClientZjsMenuConst.LK_ZJS_MENU);
        resultMap.put("companySet", companyService.getCompanyZjsSet(getCurrentLoginStaff().getCompanyId()));
        return ResultInfoUtil.success(resultMap);
    }

    /**
     * 功能描述:
     * 公司钉钉转介绍提报自定义设置
     *
     * @auther: Tt(yehuawei)
     * @date:
     * @param:
     * @return:
     */
    @PostMapping("/edit_zjs_set")
    public ResultInfo editCompanyZJSSet(@RequestBody JSONObject jsonObject) {
        companyService.editCompanyZJSSet(jsonObject.getString("old"), jsonObject.getString("qy"), getCurrentLoginStaff().getCompanyId());
        return ResultInfoUtil.success();
    }

    /**
     * 客服编辑接待结果
     */
    @GetMapping("/edit_kf_edit_jd_rst")
    public ResultInfo editKfEditJdRst(@RequestParam boolean kfEditJdRst) {
        StaffPO staff = getCurrentLoginStaff();
        companyService.editKfEditJdRst(kfEditJdRst, staff.getCompanyId());
        return ResultInfoUtil.success(kfEditJdRst ? TipMsgEnum.OPERATE_SUCCESS : TipMsgEnum.CLOSE_SUCCESS);
    }

    /**
     * 修改电商待定自定义状态
     *
     * @param dsddStatus
     * @return
     */
    @GetMapping("/edit_dsdd_status")
    public ResultInfo editDsddStatus(@RequestParam String dsddStatus) {
        companyService.editDsddStatus(dsddStatus, getCurrentLoginStaff().getCompanyId());
        return ResultInfoUtil.success();
    }

    /**
     * 编辑公司报表设置
     *
     * @param reportsConfig
     * @return
     */
    @GetMapping("/edit_reports_config")
    public ResultInfo editReportsConfig(String reportsConfig) {
        companyService.editReportsConfig(getCurrentLoginStaff().getCompanyId(), reportsConfig);
        return ResultInfoUtil.success();
    }

    /**
     * 是否可以修改电话和微信的权限
     */
    @GetMapping("/edit_phone_and_wechat")
    public ResultInfo editPhoneAndWechat(@RequestParam boolean editPhoneAndWechat) {
        companyService.editPhoneAndWechat(editPhoneAndWechat, getCurrentLoginStaff().getCompanyId());
        return ResultInfoUtil.success(TipMsgEnum.EDIT_SUCCESS);
    }

    /**
     * 编辑公司报表设置
     */
    @GetMapping("/edit_config")
    public ResultInfo editConfig(String config) {
        // 获取当前登录用户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        companyService.editConfig(getCurrentLoginStaff().getCompanyId(), config);
        //添加日志
        RequestInfoDTO requestInfo = getRequestInfo();
        try {
            Map<String, String> editMap = new HashMap<>();
            editMap.put(companyService.getCompanyVO(currentLoginStaff.getCompanyId()).getConfig(), config);
            SystemLog log = new SystemLog(SysLogUtil.LOG_TYPE_COMPANY_CONFIG, requestInfo.getIp(),
                    requestInfo.getUrl(), currentLoginStaff.getId(),
                    currentLoginStaff.getNickName(), SysLogUtil.getEditLog(SysLogUtil.LOG_SUP_COMPANY_CONFIG,
                    config, editMap),
                    currentLoginStaff.getCompanyId());
            logService.addLog(log);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultInfoUtil.success(TipMsgEnum.SAVE_SUCCESS);
        }
        return ResultInfoUtil.success();
    }

    /**
     * 获取公司配置
     */
    @GetMapping("/get_config")
    public ResultInfo getCompanyConfig() {
        return ResultInfoUtil.success(companyService.getCompanyConfig(getCurrentLoginStaff().getCompanyId()));
    }

    /**
     * 获取权限
     */
    @GetMapping("/get_permission")
    public ResultInfo getPermission(String phone, int companyId) {
        List<DatavPermissionPo> permission = companyService.getPermission(phone, companyId);
        for (DatavPermissionPo datavPermissionPo : permission) {
            if ("201".equals(datavPermissionPo.getPermissionId())) {
                return ResultInfoUtil.success(true);
            }
        }
        return ResultInfoUtil.error(9999, "该账户没有大屏权限");
    }

    /**
     * 获取大屏数据
     */
    @GetMapping("/get_datav")
    public ResultInfo getDatav(int companyId) {
        List<Datav> datav = companyService.getDatav(companyId);
        return ResultInfoUtil.success(datav);
    }

    /**
     * 获取公司报表配置
     */
    @GetMapping("/get_report_config")
    public ResultInfo getCompanyReportConfig() {
        int companyId = getCurrentLoginStaff().getCompanyId();
        return ResultInfoUtil.success(companyService.getCompanyReportConfig(companyId));
    }

}