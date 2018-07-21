package com.qiein.jupiter.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.qiein.jupiter.constant.CommonConstant;
import com.qiein.jupiter.enums.TipMsgEnum;
import com.qiein.jupiter.exception.ExceptionEnum;
import com.qiein.jupiter.exception.RException;
import com.qiein.jupiter.util.ResultInfo;
import com.qiein.jupiter.util.ResultInfoUtil;
import com.qiein.jupiter.util.StringUtil;
import com.qiein.jupiter.web.entity.dto.EditCreatorDTO;
import com.qiein.jupiter.web.entity.dto.GoldCustomerDTO;
import com.qiein.jupiter.web.entity.dto.QueryMapDTO;
import com.qiein.jupiter.web.entity.po.GoldFingerPO;
import com.qiein.jupiter.web.entity.po.GoldTempPO;
import com.qiein.jupiter.web.entity.po.StaffPO;
import com.qiein.jupiter.web.entity.vo.GoldFingerShowVO;
import com.qiein.jupiter.web.service.GoldDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;


/**
 * 金手指表单
 * Author xiangliang 2018/6/13
 */
@RestController
@RequestMapping("/gold_data")
@Validated
public class GoldDataController extends BaseController {
    @Autowired
    private GoldDataService goldDataService;

    /**
     * 增加金数据表单
     *
     * @param goldFingerPO
     * @return
     */
    @PostMapping("/insert")
    public ResultInfo insert(@RequestBody GoldFingerPO goldFingerPO) {
        if (StringUtil.haveEmpty(goldFingerPO.getFormId(), goldFingerPO.getFormName(), goldFingerPO.getSrcName(), goldFingerPO.getTypeName(), goldFingerPO.getZxStyle())) {
            throw new RException(ExceptionEnum.LOSE_FILED);
        }
        StaffPO staff = getCurrentLoginStaff();
        goldFingerPO.setStaffId(staff.getId());
        goldFingerPO.setCreateorName(staff.getNickName());
        goldFingerPO.setCompanyId(staff.getCompanyId());
        goldDataService.insert(goldFingerPO);

        return ResultInfoUtil.success(TipMsgEnum.SAVE_SUCCESS);
    }

    /**
     * 删除金数据表单
     *
     * @param id
     * @return
     */
    @GetMapping("/delete")
    public ResultInfo delete(@RequestParam Integer id) {
        try {
            goldDataService.delete(id);
        } catch (Exception e) {
            throw new RException(ExceptionEnum.DELETE_FAIL);
        }
        return ResultInfoUtil.success(TipMsgEnum.DELETE_SUCCESS);
    }

    /**
     * 更新金数据表单
     *
     * @param goldFingerPO
     * @return
     */
    @PostMapping("/update")
    public ResultInfo update(@RequestBody GoldFingerPO goldFingerPO) {
        if (StringUtil.haveEmpty(goldFingerPO.getFormId(), goldFingerPO.getFormName(), goldFingerPO.getSrcName(), goldFingerPO.getTypeName(), goldFingerPO.getZxStyle())) {
            throw new RException(ExceptionEnum.LOSE_FILED);
        }
        StaffPO staff = getCurrentLoginStaff();
        goldFingerPO.setStaffId(staff.getId());
        goldFingerPO.setCompanyId(staff.getCompanyId());
        goldDataService.update(goldFingerPO);
        return ResultInfoUtil.success(TipMsgEnum.UPDATE_SUCCESS);
    }

    /**
     * 金数据表单页面显示
     */
    @GetMapping("/select")
    public ResultInfo select(int pageNum, int pageSize,String formName) {
        pageSize=100;
//    public ResultInfo select(int pageNum, int pageSize,String formName,String formId,int typeId) {
        StaffPO staff = getCurrentLoginStaff();
        PageInfo<GoldFingerPO> select = goldDataService.select(staff.getCompanyId(), pageNum, pageSize);
        return ResultInfoUtil.success(select);
    }

    /**
     * 获取post地址
     *
     * @param request
     * @return
     */
    @GetMapping("/get_post_url")
    public ResultInfo getPostUrl(HttpServletRequest request) {
        String url = request.getRequestURL().substring(0, request.getRequestURL().lastIndexOf("/")) + "/receive_gold_data_form";
        return ResultInfoUtil.success(url);
    }

    /**
     * 修改开关
     */
    @PostMapping("/edit_open_or_close")
    public ResultInfo editOpenOrClose(@RequestBody GoldFingerPO goldFingerPO) {
        goldDataService.editOpenOrClose(goldFingerPO);
        return ResultInfoUtil.success(TipMsgEnum.UPDATE_SUCCESS);
    }

    /**
     * 获取日志列表
     *
     * @param params
     * @return
     */
    @PostMapping("/gold_customer_select")
    public ResultInfo goldCustomerSelect(@RequestBody JSONObject params) {
        QueryMapDTO queryMapDTO = JSONObject.parseObject(params.getJSONObject("queryMapDTO").toJSONString(), QueryMapDTO.class);
        GoldCustomerDTO goldCustomerDTO = JSONObject.parseObject(params.getJSONObject("goldCustomerDTO").toJSONString(), GoldCustomerDTO.class);
        return ResultInfoUtil.success(goldDataService.goldCustomerSelect(queryMapDTO, goldCustomerDTO));

    }

    /**
     * 金数据表单回调
     */
    @RequestMapping("/receive_gold_data_form")
    public ResultInfo receiveGoldDataForm(@RequestBody JSONObject jsonObject) {
        //TODO 获取到数据，存储temp与plugSetting
        goldDataService.receiveGoldDataForm(jsonObject);
        return ResultInfoUtil.success();
    }


    /**
     * 筛选
     */
    @PostMapping("/addkz_by_gold_temp")
    public ResultInfo addkzByGoldTemp(@RequestBody GoldTempPO goldTempPO) {
        goldDataService.addkzByGoldTemp(goldTempPO);
        return ResultInfoUtil.success(TipMsgEnum.ENTERING_SUNCCESS);
    }


    @PostMapping("/edit_form_createor")
    public ResultInfo editFormCreateor(@RequestBody EditCreatorDTO editCreatorDTO) {
        List<Integer> list = new ArrayList<>();
        String[] ids = editCreatorDTO.getIds().split(CommonConstant.STR_SEPARATOR);
        for (String id : ids) {
            list.add(Integer.parseInt(id));
        }
        editCreatorDTO.setList(list);
        goldDataService.editFormCreateor(editCreatorDTO);
        return ResultInfoUtil.success(TipMsgEnum.TRANSFER_SUCCESS);
    }
}
