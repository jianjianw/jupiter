package com.qiein.jupiter.web.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.qiein.jupiter.enums.TigMsgEnum;
import com.qiein.jupiter.exception.ExceptionEnum;
import com.qiein.jupiter.util.ResultInfo;
import com.qiein.jupiter.util.ResultInfoUtil;
import com.qiein.jupiter.web.entity.po.StaffPO;

/**
 * 客户端
 * 
 * @author JingChenglong 2018/05/09 15:51
 *
 */
@RestController
@RequestMapping("/app")
@Validated
public class AppController extends BaseController {

	/**
	 * 客资领取
	 * 
	 * @param kzId
	 * @param logId
	 * @return
	 */
	@GetMapping("/receive")
	public ResultInfo reveice(@RequestParam String kzId, @RequestParam String logId) {
		// 获取当前登录用户
		StaffPO currentLoginStaff = getCurrentLoginStaff();
		// TODO
		System.out.println(kzId);
		System.out.println(logId);
		System.out.println(currentLoginStaff.getId());
		System.out.println(currentLoginStaff.getCompanyId());
		if (kzId.length() < 32) {
			ResultInfoUtil.error(ExceptionEnum.INFO_OVERTIME_ERROR);
		}
		return ResultInfoUtil.success(TigMsgEnum.INFO_RECEIVE_SUCCESS);
	}

	/**
	 * 客资拒接
	 * 
	 * @param kzId
	 * @param logId
	 * @return
	 */
	@GetMapping("/refuse")
	public ResultInfo refuse(@RequestParam String kzId, @RequestParam String logId) {
		// 获取当前登录用户
		StaffPO currentLoginStaff = getCurrentLoginStaff();
		// TODO
		System.out.println(kzId);
		System.out.println(logId);
		System.out.println(currentLoginStaff.getId());
		System.out.println(currentLoginStaff.getCompanyId());
		return ResultInfoUtil.success(TigMsgEnum.INFO_REFUSE_SUCCESS);
	}
}
