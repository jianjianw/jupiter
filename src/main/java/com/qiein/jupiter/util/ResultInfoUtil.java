package com.qiein.jupiter.util;

import com.qiein.jupiter.constant.CommonConstant;
import com.qiein.jupiter.enums.TipMsgEnum;
import com.qiein.jupiter.exception.ExceptionEnum;

public class ResultInfoUtil {

	/**
	 * 提供给部分不需要出參的接口
	 *
	 * @return
	 */
	public static ResultInfo success() {
		return success(TipMsgEnum.SUCCESS);
	}

	/**
	 * 返回成功的结果
	 *
	 * @param data
	 * @return
	 */
	public static ResultInfo success(Object data) {
		return success(TipMsgEnum.SUCCESS, data);
	}

	/**
	 * 返回成功的结果
	 *
	 * @param msg
	 * @return
	 */
	public static ResultInfo success(TipMsgEnum msg) {
		ResultInfo resultInfo = new ResultInfo();
		resultInfo.setCode(CommonConstant.DEFAULT_SUCCESS_CODE);
		resultInfo.setMsg(msg.toString());
		return resultInfo;
	}

	/**
	 * 返回成功的结果
	 *
	 * @param object
	 * @return
	 */
	public static ResultInfo success(TipMsgEnum msg, Object object) {
		ResultInfo resultInfo = new ResultInfo();
		resultInfo.setCode(CommonConstant.DEFAULT_SUCCESS_CODE);
		resultInfo.setMsg(msg.toString());
		resultInfo.setData(object);
		return resultInfo;
	}

	/**
	 * 自定义错误信息
	 *
	 * @param code
	 * @param msg
	 * @return
	 */
	public static ResultInfo error(Integer code, String msg) {
		ResultInfo result = new ResultInfo();
		result.setCode(code);
		result.setMsg(msg);
		result.setData(null);
		return result;
	}

	/**
	 * 返回异常信息，在已知的范围内
	 *
	 * @param exceptionEnum
	 * @return
	 */
	public static ResultInfo error(ExceptionEnum exceptionEnum) {
		ResultInfo result = new ResultInfo();
		result.setCode(exceptionEnum.getCode());
		result.setMsg(exceptionEnum.getMsg());
		result.setData(null);
		return result;
	}
}