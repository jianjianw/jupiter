package com.qiein.jupiter.web.controller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
}