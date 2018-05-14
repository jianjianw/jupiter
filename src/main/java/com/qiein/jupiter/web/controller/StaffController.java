package com.qiein.jupiter.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.qiein.jupiter.constant.CommonConstant;
import com.qiein.jupiter.web.entity.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.qiein.jupiter.aop.aspect.annotation.LoginLog;
import com.qiein.jupiter.aop.validate.annotation.Bool;
import com.qiein.jupiter.aop.validate.annotation.Id;
import com.qiein.jupiter.aop.validate.annotation.NotEmptyStr;
import com.qiein.jupiter.constant.RedisConstant;
import com.qiein.jupiter.enums.TigMsgEnum;
import com.qiein.jupiter.exception.ExceptionEnum;
import com.qiein.jupiter.exception.RException;
import com.qiein.jupiter.util.NumUtil;
import com.qiein.jupiter.util.ObjectUtil;
import com.qiein.jupiter.util.RegexUtil;
import com.qiein.jupiter.util.ResultInfo;
import com.qiein.jupiter.util.ResultInfoUtil;
import com.qiein.jupiter.util.StringUtil;
import com.qiein.jupiter.util.VerifyCodeUtil;
import com.qiein.jupiter.web.entity.dto.QueryMapDTO;
import com.qiein.jupiter.web.entity.dto.StaffPasswordDTO;
import com.qiein.jupiter.web.entity.po.CompanyPO;
import com.qiein.jupiter.web.entity.po.StaffPO;
import com.qiein.jupiter.web.service.StaffService;

/**
 * 员工 Controller
 */
@RestController
@RequestMapping("/staff")
@Validated
public class StaffController extends BaseController {

	@Autowired
	private StaffService staffService;

	@Autowired
	private ValueOperations<String, String> valueOperations;

	/**
	 * 获取列表
	 *
	 * @param queryMapDTO
	 * @return
	 */
	@PostMapping("/findList")
	public ResultInfo getAll(@RequestBody QueryMapDTO queryMapDTO) {
		return ResultInfoUtil.success(staffService.findList(queryMapDTO));
	}

	/**
	 * 插入
	 *
	 * @param staffVO
	 * @return
	 */
	@PostMapping("/insert")
	@LoginLog
	public ResultInfo insert(@RequestBody @Validated StaffVO staffVO) {
		// 获取当前登录用户
		StaffPO currentLoginStaff = getCurrentLoginStaff();
		if (!RegexUtil.checkMobile(staffVO.getPhone())) {
			return ResultInfoUtil.error(ExceptionEnum.PHONE_ERROR);
		}
		// 设置cid
		staffVO.setCompanyId(currentLoginStaff.getCompanyId());
		// 对象参数trim
		ObjectUtil.objectStrParamTrim(staffVO);
		staffService.insert(staffVO);
		return ResultInfoUtil.success(TigMsgEnum.SAVE_SUCCESS);
	}

	/**
	 * 更新员工信息
	 *
	 * @param staffVO
	 * @return
	 */
	@PostMapping("/update_staff")
	public ResultInfo updateStaff(@RequestBody @Validated StaffVO staffVO) {
		// 对象参数trim
		ObjectUtil.objectStrParamTrim(staffVO);
		if (staffVO.getId() == 0) {
			return ResultInfoUtil.error(ExceptionEnum.STAFF_ID_NULL);
		}
		if (!RegexUtil.checkMobile(staffVO.getPhone())) {
			return ResultInfoUtil.error(ExceptionEnum.PHONE_ERROR);
		}
		if (StringUtil.isEmpty(staffVO.getGroupId())) {
			return ResultInfoUtil.error(ExceptionEnum.GROUP_IS_NULL);
		}
		// 获取当前登录用户
		StaffPO currentLoginStaff = getCurrentLoginStaff();
		// 设置cid
		staffVO.setCompanyId(currentLoginStaff.getCompanyId());
		staffService.update(staffVO);
		return ResultInfoUtil.success(TigMsgEnum.SAVE_SUCCESS);
	}

	/**
	 * 删除标记
	 *
	 * @param id
	 * @return
	 */
	@GetMapping("/delete_flag")
	public ResultInfo deleteFlag(@Id Integer id) {
		// 获取当前登录账户
		StaffPO currentLoginStaff = getCurrentLoginStaff();
		staffService.logicDelete(id, currentLoginStaff.getCompanyId());
		return ResultInfoUtil.success();
	}

	/**
	 * 获取用户所在所有企业信息
	 *
	 * @param loginUserVO
	 * @return
	 */
	@PostMapping("/get_company_list")
	public ResultInfo getCompanyList(@RequestBody @Validated LoginUserVO loginUserVO) {
		// 对象参数trim
		ObjectUtil.objectStrParamTrim(loginUserVO);
		// 校验用户验证码
		checkLoginVerifyCode(loginUserVO);
		try {
			// 返回结果
			List<CompanyPO> companyList = staffService.getCompanyList(loginUserVO.getUserName(),
					loginUserVO.getPassword());
			return ResultInfoUtil.success(companyList);
		} catch (RException e) {
			// 将错误次数+1
			valueOperations.increment(RedisConstant.getUserLoginErrNumKey(loginUserVO.getUserName()),
					CommonConstant.LOGIN_ERROR_ADD_NUM);
			return ResultInfoUtil.error(e.getCode(), e.getMsg());
		}
	}

	/**
	 * 根据公司ID登录
	 *
	 * @param loginUserVO
	 * @return
	 */
	@PostMapping("/login_with_company_id")
	public ResultInfo loginWithCompanyId(@RequestBody @Validated LoginUserVO loginUserVO) {
		// 对象参数trim
		ObjectUtil.objectStrParamTrim(loginUserVO);
		// 校验用户验证码
		checkLoginVerifyCode(loginUserVO);
		String userName = loginUserVO.getUserName();
		String password = loginUserVO.getPassword();
		// 校验公司Id
		if (loginUserVO.getCompanyId() == 0) {
			throw new RException(ExceptionEnum.COMPANYID_NULL);
		}
		try {
			// 返回结果
			StaffPO staffPO = staffService.loginWithCompanyId(userName, password, loginUserVO.getCompanyId(), getIp());
			return ResultInfoUtil.success(staffPO);
		} catch (RException e) {
			// 登录失败，将错误次数+1
			valueOperations.increment(RedisConstant.getUserLoginErrNumKey(userName), 1);
			return ResultInfoUtil.error(e.getCode(), e.getMsg());
		}
	}

	/**
	 * 是否需要验证码
	 *
	 * @param userName
	 *            用户名
	 */
	@GetMapping("/need_verity_code")
	public boolean needVerityCode(@NotEmptyStr @RequestParam("phone") String userName) {
		// 判断是否需要验证码
		String userLoginErrNum = valueOperations.get(RedisConstant.getUserLoginErrNumKey(userName));
		if (userLoginErrNum == null) {
			// 如果没有查询到，说明是第一次，设置默认值0,过期时间为1小时
			valueOperations.set(RedisConstant.getUserLoginErrNumKey(userName),
					String.valueOf(CommonConstant.DEFAULT_ZERO), CommonConstant.LOGIN_ERROR_NUM_EXPIRE_TIME,
					TimeUnit.HOURS);
			return false;
		} else {
			// 是否大于允许的错误最大值
			int errNum = Integer.valueOf(userLoginErrNum);
			return errNum >= CommonConstant.ALLOW_USER_LOGIN_ERR_NUM;
		}
	}

	/**
	 * 请求验证码
	 *
	 * @param response
	 */
	@GetMapping("/verify_code")
	public void loginCode(HttpServletResponse response, @RequestParam("phone") String userName) {
		if (StringUtil.isEmpty(userName) || !RegexUtil.checkMobile(userName)) {
			return;
		}
		// 生成验证码并放入缓存
		String code = VerifyCodeUtil.execute(response);
		valueOperations.set(RedisConstant.getVerifyCodeKey(userName), code);
	}

	/**
	 * 获取组员工列表
	 *
	 * @param groupId
	 * @return
	 */
	@GetMapping("/get_group_staff_list")
	public ResultInfo getGroupStaffList(@NotEmptyStr @RequestParam("groupId") String groupId) {
		// 获取当前登录账户
		StaffPO currentLoginStaff = getCurrentLoginStaff();
		List<StaffVO> staffList = staffService.getGroupStaffs(currentLoginStaff.getCompanyId(), groupId);
		return ResultInfoUtil.success(staffList);
	}

	/**
	 * 批量编辑员工信息
	 *
	 * @return
	 */
	@GetMapping("/batch_edit_staff")
	public ResultInfo batchEditStaff(@NotEmptyStr @RequestParam("staffIds") String staffIds,
			@RequestParam("roleIds") String roleIds, @RequestParam("password") String password,
			@NotEmptyStr @RequestParam("groupId") String groupId) {
		// 获取当前登录用户
		StaffPO currentLoginStaff = getCurrentLoginStaff();
		staffService.batchEditStaff(currentLoginStaff.getCompanyId(), staffIds, roleIds, password, groupId);
		return ResultInfoUtil.success(TigMsgEnum.SAVE_SUCCESS);
	}

	/**
	 * 搜索员工信息
	 *
	 * @param searchKey
	 * @return
	 */
	@GetMapping("/search_staff")
	public ResultInfo searchStaff(@NotEmptyStr @RequestParam("searchKey") String searchKey) {
		// 获取当前登录用户
		StaffPO currentLoginStaff = getCurrentLoginStaff();
		List<SearchStaffVO> staffList = staffService.getStaffListBySearchKey(currentLoginStaff.getCompanyId(), searchKey);
		return ResultInfoUtil.success(staffList);
	}

	/**
	 * 校验用户验证码
	 *
	 * @param loginUserVO
	 */
	private void checkLoginVerifyCode(LoginUserVO loginUserVO) {
		// 对象参数trim
		ObjectUtil.objectStrParamTrim(loginUserVO);
		String userName = loginUserVO.getUserName();
		String verifyCode = loginUserVO.getVerifyCode();
		// 判断是否需要验证码以及验证码正确性
		if (needVerityCode(userName)) {
			// 验证码为空
			if (StringUtil.isEmpty(verifyCode)) {
				throw new RException(ExceptionEnum.VERIFY_NULL);
			} else {
				// 从缓存获取key并判断
				String verifyCodeTrue = valueOperations.get(RedisConstant.getVerifyCodeKey(userName));
				if (!StringUtil.ignoreCaseEqual(verifyCode, verifyCodeTrue)) {
					// 验证码错误
					throw new RException(ExceptionEnum.VERIFY_ERROR);
				}
			}
		}
	}

	/**
	 * 获取员工详情
	 *
	 * @return
	 */
	@GetMapping("/detail")
	public ResultInfo getDetailInfo() {
		// 获取当前登录账户
		StaffPO currentLoginStaff = getCurrentLoginStaff();
		return ResultInfoUtil
				.success(staffService.getById(currentLoginStaff.getId(), currentLoginStaff.getCompanyId()));
	}

	/**
	 * 首页获取基础信息
	 */
	@GetMapping("/base_info")
	public ResultInfo getBaseInfo() {
		// 获取当前登录账户
		StaffPO currentLoginStaff = getCurrentLoginStaff();
		return ResultInfoUtil
				.success(staffService.getStaffBaseInfo(currentLoginStaff.getId(), currentLoginStaff.getCompanyId()));
	}

	/**
	 * 删除指定员工
	 *
	 * @param ids
	 * @return
	 */
	@GetMapping("/del_staff")
	public ResultInfo deleteStaff(@NotEmptyStr @RequestParam("staffId") String ids) {
		// 获取当前登录账户
		StaffPO currentLoginStaff = getCurrentLoginStaff();

		// 检查是否可删除
		// 先检查是否为客服
		// 是客服则检查是否存在未邀约客资
		// TODO 等待客资内容写完继续写删除
		StaffStateVO staffStateVO = new StaffStateVO();
		staffStateVO.setCompanyId(currentLoginStaff.getCompanyId());
		staffStateVO.setIds(ids);
		staffStateVO.setDel(true);
		// staffService.batUpdateStaffState(staffStateVO);
		staffService.batDelStaff(staffStateVO);
		return ResultInfoUtil.success("删除成功");
	}

	/**
	 * 检查员工是否可删除。 如果员工剩余未邀约客资为0则可删除，不为零则不可删除，需要交接
	 *
	 * @return
	 */
	@GetMapping("/del_staff_check")
	public ResultInfo DelStaffCheck(@NotEmptyStr @RequestParam("staffId") String ids) {
		// // 获取当前登录账户
		// StaffPO currentLoginStaff = getCurrentLoginStaff();

		// // 获取操作用户所属公司
		// Integer companyId = currentLoginStaff.getCompanyId();
		//
		// // 待被检查是否可删除的员工数组
		// String[] array = ids.split(",");

		// 检查是否可删除
		// 先检查是否为客服
		// String msg = "";
		// if (true) {
		// TODO 等待客资内容写完继续写删除
		// msg = staffService.checkBatDelete(array, companyId);
		// } else {
		// }
		// 是客服则检查是否存在未邀约客资
		return ResultInfoUtil.success(TigMsgEnum.SUCCESS, true);
	}

	/**
	 * 锁定员工
	 *
	 * @param staffId
	 *            staffId 被锁定的员工编号
	 * @param isLock
	 *            锁定标识
	 * @return
	 */
	@GetMapping("/lock_staff")
	public ResultInfo LockStaff(@Id @RequestParam("staffId") Integer staffId,
			@Bool @RequestParam("isLock") Boolean isLock) {
		// 获取当前登录账户
		StaffPO currentLoginStaff = getCurrentLoginStaff();
		// 获取操作用户所属公司
		Integer companyId = currentLoginStaff.getCompanyId();
		// 锁定状态
		staffService.setLockState(staffId, companyId, isLock);
		return ResultInfoUtil.success(TigMsgEnum.OPERATE_SUCCESS);
	}

	/**
	 * 交接接口
	 *
	 * @param staffId
	 *            交接的员工id
	 * @param beStaffId
	 *            被转移的员工id
	 * @return
	 */
	@GetMapping("/change_staff")
	public ResultInfo ChangeStaff(@Id @RequestParam("staffId") Integer staffId,
			@Id @RequestParam("beStaffId") Integer beStaffId) {
		// // 获取当前登录账户
		// StaffPO currentLoginStaff = getCurrentLoginStaff();
		// // 获取操作用户所属公司
		// Integer companyId = currentLoginStaff.getCompanyId();

		try {
			// TODO 等待客资内容写完继续写删除
		} catch (Exception e) {
			e.printStackTrace();
			return ResultInfoUtil.error(ExceptionEnum.UNKNOW_ERROR);
		}
		return ResultInfoUtil.success("交接成功");
	}

	/**
	 * 获取电商邀约小组人员列表
	 *
	 * @return
	 */
	@GetMapping("/change_list")
	public ResultInfo getChangeList() {
		// 获取当前登录账户
		StaffPO currentLoginStaff = getCurrentLoginStaff();
		// 获取操作用户所属公司
		Integer companyId = currentLoginStaff.getCompanyId();

		return ResultInfoUtil.success(TigMsgEnum.SUCCESS, staffService.getChangeList(companyId));
	}

	/**
	 * 更新员工基础信息，不包含权限等
	 *
	 * @param staffDetailVO
	 * @return
	 */
	@PostMapping("/update_base_info")
	public ResultInfo updateBaseInfo(@RequestBody @Valid StaffDetailVO staffDetailVO) {
		// 对象参数trim
		ObjectUtil.objectStrParamTrim(staffDetailVO);
		if (staffDetailVO.getId() == 0) {
			return ResultInfoUtil.error(ExceptionEnum.STAFF_ID_NULL);
		}
		// 获取当前登录用户
		StaffPO currentLoginStaff = getCurrentLoginStaff();
		// 设置cid
		staffDetailVO.setCompanyId(currentLoginStaff.getCompanyId());
		staffService.update(staffDetailVO);
		return ResultInfoUtil.success(TigMsgEnum.SAVE_SUCCESS);
	}

	/**
	 * 更新员工密码
	 *
	 * @param staffPasswordDTO
	 * @return
	 */
	@PostMapping("/update_password")
	// todo 增加密码安全度校验
	public ResultInfo updatePassword(@Validated @RequestBody StaffPasswordDTO staffPasswordDTO) {
		// 获取当前登录用户
		StaffPO currentLoginStaff = getCurrentLoginStaff();
		staffPasswordDTO.setId(currentLoginStaff.getId());
		staffPasswordDTO.setCompanyId(currentLoginStaff.getCompanyId());
		staffService.updatePassword(staffPasswordDTO);
		return ResultInfoUtil.success(TigMsgEnum.UPDATE_SUCCESS);
	}

	/**
	 * 是否是原来正确的密码
	 *
	 * @param oldPassword
	 * @return
	 */
	@GetMapping("/right_password")
	public boolean rightPassword(@RequestParam("password") String oldPassword) {
		// 获取当前登录用户
		StaffPO currentLoginStaff = getCurrentLoginStaff();
		return staffService.isRightPassword(currentLoginStaff.getId(), oldPassword, currentLoginStaff.getCompanyId());
	}

	/**
	 * 根据类型获取小组及人员信息
	 *
	 * @param type
	 * @return
	 */
	@GetMapping("/get_group_staff_by_type")
	public ResultInfo getGroupStaffByType(@NotEmptyStr String type) {
		// 获取当前登录账户
		StaffPO currentLoginStaff = getCurrentLoginStaff();
		// 获取操作用户所属公司
		Integer companyId = currentLoginStaff.getCompanyId();

		return ResultInfoUtil.success(TigMsgEnum.SUCCESS, staffService.getGroupStaffByType(companyId, type));
	}

	/**
	 * 获取已离职的员工列表
	 *
	 * @param queryMapDTO
	 * @return
	 */
	@GetMapping("/get_del_staff_list")
	public ResultInfo getDelStaffList(QueryMapDTO queryMapDTO) {
		// 获取当前登录账户
		StaffPO currentLoginStaff = getCurrentLoginStaff();
		HashMap<String, Object> condition = new HashMap<>();
		condition.put("companyId", currentLoginStaff.getCompanyId());
		condition.put("delFlag", true);
		queryMapDTO.setCondition(condition);
		return ResultInfoUtil.success(TigMsgEnum.SUCCESS, staffService.getDelStaffList(queryMapDTO));
	}

	/**
	 * 恢复离职员工
	 *
	 * @param staffVO
	 * @return
	 */
	@PostMapping("/restore_del_staff")
	public ResultInfo restoreDelStaff(@RequestBody StaffVO staffVO) {
		if (NumUtil.isNull(staffVO.getId())) {
			return ResultInfoUtil.error(ExceptionEnum.STAFF_ID_NULL);
		}
		if (StringUtil.isEmpty(staffVO.getRoleIds())) {
			return ResultInfoUtil.error(ExceptionEnum.ROLE_IS_NULL);
		}
		if (StringUtil.isEmpty(staffVO.getNickName())) {
			return ResultInfoUtil.error(ExceptionEnum.NICKNAME_IS_NULL);
		}
		if (StringUtil.isEmpty(staffVO.getPhone())) {
			return ResultInfoUtil.error(ExceptionEnum.PHONE_IS_NULL);
		}
		if (!RegexUtil.checkMobile(staffVO.getPhone())) {
			return ResultInfoUtil.error(ExceptionEnum.PHONE_ERROR);
		}
		if (StringUtil.isEmpty(staffVO.getUserName())) {
			return ResultInfoUtil.error(ExceptionEnum.USERNAME_IS_NULL);
		}
		// 获取当前登录账户
		StaffPO currentLoginStaff = getCurrentLoginStaff();
		staffVO.setCompanyId(currentLoginStaff.getCompanyId());
		staffService.restoreDelStaff(staffVO);
		return ResultInfoUtil.success(TigMsgEnum.RESOTRE_SUCCESS);
	}

	/**
	 * 批量恢复员工
	 *
	 * @return
	 */
	@GetMapping("/batch_restore_staff")
	public ResultInfo batchRestoreStaff(@NotEmptyStr @RequestParam("staffIds") String staffIds,
			@NotEmptyStr @RequestParam("roleIds") String roleIds, @RequestParam("password") String password,
			@NotEmptyStr @RequestParam("groupId") String groupId) {
		// 获取当前登录用户
		StaffPO currentLoginStaff = getCurrentLoginStaff();
		staffService.batchRestoreStaff(currentLoginStaff.getCompanyId(), staffIds, roleIds, password, groupId);
		return ResultInfoUtil.success(TigMsgEnum.RESOTRE_SUCCESS);
	}

	/**
	 * 搜索离职员工
	 *
	 * @param searchKey
	 * @return
	 */
	@GetMapping("/search_del_staff_list")
	public ResultInfo searchDelStaffList(@NotEmptyStr @RequestParam("searchKey") String searchKey) {
		// 获取当前登录用户
		StaffPO currentLoginStaff = getCurrentLoginStaff();
		return ResultInfoUtil
				.success(staffService.getDelStaffListBySearchKey(currentLoginStaff.getCompanyId(), searchKey));
	}

	/**
	 * 员工自己设置在线状态
	 *
	 * @param status
	 * @return
	 */
	@GetMapping("/set_status")
	public ResultInfo setStaffStatus(@RequestParam("status") int status) {
		// 获取当前登录用户
		StaffPO currentLoginStaff = getCurrentLoginStaff();
		staffService.updateStatusFlag(currentLoginStaff.getId(), currentLoginStaff.getCompanyId(), status);
		if (status == 0) {
			return ResultInfoUtil.success(TigMsgEnum.OFFLINE_SUCCESS);
		} else {
			return ResultInfoUtil.success(TigMsgEnum.ONLINE_SUCCESS);
		}
	}

}
