package com.qiein.jupiter.web.controller;

import com.qiein.jupiter.aop.validate.annotation.Id;
import com.qiein.jupiter.aop.validate.annotation.NotEmptyStr;
import com.qiein.jupiter.constant.DictionaryConstant;
import com.qiein.jupiter.enums.TigMsgEnum;
import com.qiein.jupiter.exception.ExceptionEnum;
import com.qiein.jupiter.util.NumUtil;
import com.qiein.jupiter.util.ResultInfo;
import com.qiein.jupiter.util.ResultInfoUtil;
import com.qiein.jupiter.util.StringUtil;
import com.qiein.jupiter.web.entity.po.DictionaryPO;
import com.qiein.jupiter.web.entity.po.StaffPO;
import com.qiein.jupiter.web.entity.vo.DictionaryVO;
import com.qiein.jupiter.web.service.DictionaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 字典数据
 */
@RestController
@RequestMapping("/dictionary")
@Validated
public class DictionaryController extends BaseController {

    @Autowired
    private DictionaryService dictionaryService;

    /**
     * 获取企业自己配置的无效原因列表，自定义
     *
     * @return
     */
    @GetMapping("/get_invalid_reason_list")
    public ResultInfo getInvalidReasonList() {
        // 获取当前登录账户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        return ResultInfoUtil.success(dictionaryService.getCompanyDicByType(currentLoginStaff.getCompanyId(),
                DictionaryConstant.INVALID_REASON));
    }

    /**
     * 添加无效原因
     *
     * @param dictionaryPO
     * @return
     */
    @PostMapping("/add_invalid_reason")
    public ResultInfo addInvalidReason(@RequestBody DictionaryPO dictionaryPO) {
        if (StringUtil.isEmpty(dictionaryPO.getSpare())) {
            return ResultInfoUtil.error(ExceptionEnum.INVALID_REASON_TYPE_NULL);
        }
        // 获取当前登录账户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        dictionaryPO.setCompanyId(currentLoginStaff.getCompanyId());
        dictionaryService.addInvalidReason(dictionaryPO);
        return ResultInfoUtil.success(TigMsgEnum.SAVE_SUCCESS);
    }

    /**
     * 编辑无效原因
     *
     * @param dictionaryPO
     * @return
     */
    @PostMapping("/edit_invalid_reason")
    public ResultInfo editInvalidReason(@RequestBody DictionaryPO dictionaryPO) {
        if (StringUtil.isEmpty(dictionaryPO.getSpare())) {
            return ResultInfoUtil.error(ExceptionEnum.INVALID_REASON_TYPE_NULL);
        }
        if (NumUtil.isNull(dictionaryPO.getId())) {
            return ResultInfoUtil.error(ExceptionEnum.ID_IS_NULL);
        }
        // 获取当前登录账户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        dictionaryPO.setCompanyId(currentLoginStaff.getCompanyId());
        dictionaryService.editInvalidReason(dictionaryPO);
        return ResultInfoUtil.success(TigMsgEnum.EDIT_SUCCESS);
    }

    /**
     * 删除无效原因
     *
     * @param ids
     * @return
     */
    @GetMapping("/delete_invalid_reason")
    public ResultInfo deleteInvalidReason(@NotEmptyStr @RequestParam("ids") String ids) {
        // 获取当前登录账户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        dictionaryService.batchDeleteByIds(currentLoginStaff.getCompanyId(), ids);
        return ResultInfoUtil.success(TigMsgEnum.DELETE_SUCCESS);
    }

    /**
     * 获取企业自己配置的流失原因列表，自定义
     *
     * @return
     */
    @GetMapping("/get_run_off_reason_list")
    public ResultInfo getRunOffReasonList() {
        // 获取当前登录账户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        return ResultInfoUtil.success(dictionaryService.getCompanyDicByType(currentLoginStaff.getCompanyId(),
                DictionaryConstant.RUN_OFF_REASON));
    }

    /**
     * 添加流失原因
     *
     * @param dicName
     * @return
     */
    @GetMapping("/add_run_off_reason")
    public ResultInfo addRunoffReason(@NotEmptyStr @RequestParam("dicName") String dicName) {
        // 获取当前登录账户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        dictionaryService.addRunoffReason(currentLoginStaff.getCompanyId(), dicName);
        return ResultInfoUtil.success(TigMsgEnum.SAVE_SUCCESS);
    }

    /**
     * 编辑流失原因
     *
     * @param dicName
     * @param id
     * @return
     */
    @GetMapping("/edit_run_off_reason")
    public ResultInfo editRunoffReason(@NotEmptyStr @RequestParam("dicName") String dicName,
                                       @Id @RequestParam("id") int id) {
        // 获取当前登录账户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        dictionaryService.editRunoffReason(currentLoginStaff.getCompanyId(), id, dicName);
        return ResultInfoUtil.success(TigMsgEnum.EDIT_SUCCESS);
    }

    /**
     * 删除流失原因
     *
     * @param ids
     * @return
     */
    @GetMapping("/delete_run_off_reason")
    public ResultInfo deleteRunoffReason(@NotEmptyStr @RequestParam("ids") String ids) {
        // 获取当前登录账户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        dictionaryService.batchDeleteByIds(currentLoginStaff.getCompanyId(), ids);
        return ResultInfoUtil.success(TigMsgEnum.DELETE_SUCCESS);
    }

    /**
     * 获取拍摄类型列表，没有自定义则获取公用的
     *
     * @return
     */
    @GetMapping("/get_common_type_list")
    public ResultInfo getCommonTypeList() {
        // 获取当前登录账户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        return ResultInfoUtil.success(
                dictionaryService.getCommonDicByType(currentLoginStaff.getCompanyId(), DictionaryConstant.COMMON_TYPE));
    }

    /**
     * 获取咨询类型列表，没有自定义则获取公用的
     *
     * @return
     */
    @GetMapping("/get_zx_style_list")
    public ResultInfo getZxStyleList() {
        // 获取当前登录账户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        return ResultInfoUtil.success(
                dictionaryService.getCommonDicByType(currentLoginStaff.getCompanyId(), DictionaryConstant.ZX_STYLE));
    }

    /**
     * 新增字典
     *
     * @return
     */
    @PostMapping("/{dicType}")
    public ResultInfo createDict(@PathVariable("dicType") String dicType, @RequestBody DictionaryPO dictionaryPO) {
        dictionaryPO.setCompanyId(getCurrentLoginStaff().getCompanyId());
        dictionaryPO.setDicType(dicType);
        dictionaryService.createDict(dictionaryPO);
        return ResultInfoUtil.success();
    }

    /**
     * 删除字典
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResultInfo delDict(@PathVariable("id") int id) {
        dictionaryService.delDict(id, getCurrentLoginStaff().getCompanyId());
        return ResultInfoUtil.success();
    }

    /**
     * 新增咨询类型
     *
     * @param dictionaryVO
     * @return
     */
    @PostMapping("/add_common_type")
    public ResultInfo addCommonType(@RequestBody DictionaryVO dictionaryVO) {
        dictionaryVO.setCompanyId(getCurrentLoginStaff().getCompanyId());
        dictionaryService.addCommonType(dictionaryVO);
        return ResultInfoUtil.success();
    }

    /**
     * 编辑字典排序
     *
     * @param id1
     * @param priority1
     * @param id2
     * @param priority2
     * @return
     */
    @GetMapping("/edit_priority/{id1}/{priority1}/{id2}/{priority2}")
    public ResultInfo editDictPriority(@PathVariable("id1") int id1, @PathVariable("priority1") int priority1,
                                       @PathVariable("id2") int id2, @PathVariable("priority2") int priority2) {
        dictionaryService.editDictPriority(id1, priority1, id2, priority2, getCurrentLoginStaff().getCompanyId());
        return ResultInfoUtil.success();
    }

    /**
     * 获取意向等级
     *
     * @return
     */
    @GetMapping("/get_yx_level_list")
    public ResultInfo getYxLevelList() {
        // 获取当前登录账户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        return ResultInfoUtil.success(
                dictionaryService.getCommonDicByType(currentLoginStaff.getCompanyId(), DictionaryConstant.YX_RANK));
    }


    @PostMapping("/edit_name")
    public ResultInfo editName(@RequestBody DictionaryPO dictionaryPO) {
        // 获取当前登录账户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        dictionaryPO.setCompanyId(currentLoginStaff.getCompanyId());
        dictionaryService.editDictName(dictionaryPO);
        return ResultInfoUtil.success(TigMsgEnum.EDIT_SUCCESS);
    }

    @PostMapping("/edit_show_flag")
    public ResultInfo editShowFlag(@RequestBody DictionaryPO dictionaryPO) {
        // 获取当前登录账户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        dictionaryPO.setCompanyId(currentLoginStaff.getCompanyId());
        dictionaryService.editDictShowFlag(dictionaryPO);
        return ResultInfoUtil.success(TigMsgEnum.OPERATE_SUCCESS);
    }


}
