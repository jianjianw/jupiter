package com.qiein.jupiter.web.controller;

import javax.annotation.Resource;

import com.qiein.jupiter.exception.ExceptionEnum;
import com.qiein.jupiter.util.NumUtil;
import com.qiein.jupiter.util.StringUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.qiein.jupiter.aop.validate.annotation.NotEmptyStr;
import com.qiein.jupiter.enums.TigMsgEnum;
import com.qiein.jupiter.util.ResultInfo;
import com.qiein.jupiter.util.ResultInfoUtil;
import com.qiein.jupiter.web.entity.po.CompanyPO;
import com.qiein.jupiter.web.service.CompanyService;

/**
 * 公司
 */
@RestController
@RequestMapping("/company")
public class CompanyController extends BaseController {

    @Resource
    private CompanyService companyService;

    /**
     * 编辑企业信息
     *
     * @param companyPO
     * @return
     */
    @PostMapping("/edit")
    public ResultInfo editCompanyInfo(@RequestBody CompanyPO companyPO) {
        companyService.update(companyPO);
        return ResultInfoUtil.success(TigMsgEnum.EDIT_SUCCESS);
    }

    /**
     * 同一个账号不能再不同电脑登录
     *
     * @param flag
     * @return
     */
    @GetMapping("/ssolimit")
    public ResultInfo editCompanySsolimit(@NotEmptyStr @RequestParam("flag") String flag) {
        companyService.updateFlag(getCurrentLoginStaff().getCompanyId(), CompanyPO.COLUMN_SSOLIMIT,
                Boolean.valueOf(flag));
        return ResultInfoUtil.success(TigMsgEnum.EDIT_SUCCESS);
    }

    /**
     * 客服领取客资倒计时时间
     *
     * @param flag
     * @return
     */
    @GetMapping("/overtime")
    public ResultInfo editCompanyOvertime(@NotEmptyStr @RequestParam("num") Integer num) {
        companyService.updateRange(getCurrentLoginStaff().getCompanyId(), CompanyPO.COLUMN_OVERTIME, num);
        return ResultInfoUtil.success(TigMsgEnum.EDIT_SUCCESS);
    }

    /**
     * 客服每日领单默认限额
     *
     * @param flag
     * @return
     */
    @GetMapping("/limitdefault")
    public ResultInfo editCompanyLimitdefault(@NotEmptyStr @RequestParam("num") Integer num) {
        companyService.updateRange(getCurrentLoginStaff().getCompanyId(), CompanyPO.COLUMN_LIMITDEFAULT, num);
        return ResultInfoUtil.success(TigMsgEnum.EDIT_SUCCESS);
    }

    /**
     * 客服领取客资指定时间不能再领取下一个
     *
     * @param flag
     * @return
     */
    @GetMapping("/kzinterval")
    public ResultInfo editCompanyKzinterval(@NotEmptyStr @RequestParam("num") Integer num) {
        companyService.updateRange(getCurrentLoginStaff().getCompanyId(), CompanyPO.COLUMN_KZINTERVAL, num);
        return ResultInfoUtil.success(TigMsgEnum.EDIT_SUCCESS);
    }

    /**
     * 员工个人不能自己操作上线离线
     *
     * @param flag
     * @return
     */
    @GetMapping("/notselfblind")
    public ResultInfo editCompanyNotselfblind(@NotEmptyStr @RequestParam("flag") String flag) {
        companyService.updateFlag(getCurrentLoginStaff().getCompanyId(), CompanyPO.COLUMN_NOTSELFBLIND,
                Boolean.valueOf(flag));
        return ResultInfoUtil.success(TigMsgEnum.EDIT_SUCCESS);
    }

    /**
     * 非个人客资信息脱敏显示
     *
     * @param flag
     * @return
     */
    @GetMapping("/unableselfline")
    public ResultInfo editCompanyUnableselfline(@NotEmptyStr @RequestParam("flag") String flag) {
        companyService.updateFlag(getCurrentLoginStaff().getCompanyId(), CompanyPO.COLUMN_UNABLESELFLINE,
                Boolean.valueOf(flag));
        return ResultInfoUtil.success(TigMsgEnum.EDIT_SUCCESS);
    }

    /**
     * 电商客资录入时不能直接指定客服
     *
     * @return
     */
    @GetMapping("/unableappointor")
    public ResultInfo editCompanyUnableappointor(@NotEmptyStr @RequestParam("flag") String flag) {
        companyService.updateFlag(getCurrentLoginStaff().getCompanyId(), CompanyPO.COLUMN_UNABLEAPPOINTOR,
                Boolean.valueOf(flag));
        return ResultInfoUtil.success(TigMsgEnum.EDIT_SUCCESS);
    }

    /**
     * 电商客资录入指定时间不能反无效
     *
     * @return
     */
    @GetMapping("/unableinvalidrange")
    public ResultInfo editCompanyUnableinvalidrange(@NotEmptyStr @RequestParam("num") Integer num) {
        companyService.updateRange(getCurrentLoginStaff().getCompanyId(), CompanyPO.COLUMN_UNABLEINVALIDRANGE, num);
        return ResultInfoUtil.success(TigMsgEnum.EDIT_SUCCESS);
    }

    /**
     * 获取企业信息
     *
     * @return
     */
    @GetMapping("/info")
    public ResultInfo getCompanyInfo() {
        return ResultInfoUtil.success(TigMsgEnum.SUCCESS,
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
        return ResultInfoUtil.success(TigMsgEnum.SUCCESS);
    }

    /**
     * 更改渠道类型(客资校验是否忽略渠道类型)
     *
     * @return
     */
    @GetMapping("/editSrcRepeat")
    public ResultInfo editSrcRepeat(@RequestParam("srcRepeat") boolean typeRepeat) {

        companyService.editTypeSrcRepeat(typeRepeat, getCurrentLoginStaff().getCompanyId());
        return ResultInfoUtil.success(TigMsgEnum.SUCCESS);
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
        return ResultInfoUtil.success(TigMsgEnum.SUCCESS);
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
        return ResultInfoUtil.success(TigMsgEnum.SUCCESS, companyPo);
    }
}