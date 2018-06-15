package com.qiein.jupiter.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.qiein.jupiter.enums.TigMsgEnum;
import com.qiein.jupiter.exception.ExceptionEnum;
import com.qiein.jupiter.exception.RException;
import com.qiein.jupiter.util.NumUtil;
import com.qiein.jupiter.util.ResultInfo;
import com.qiein.jupiter.util.ResultInfoUtil;
import com.qiein.jupiter.util.StringUtil;
import com.qiein.jupiter.web.entity.dto.GoldCustomerDTO;
import com.qiein.jupiter.web.entity.dto.QueryMapDTO;
import com.qiein.jupiter.web.entity.po.GoldFingerPO;
import com.qiein.jupiter.web.entity.po.StaffPO;
import com.qiein.jupiter.web.entity.vo.GoldCustomerVO;
import com.qiein.jupiter.web.service.GoldDataService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 金手指表单
 * Author xiangliang 2018/6/13
 */
@RestController
@RequestMapping("/gold_data")
@Validated
public class GoldDataController extends BaseController{
    @Autowired
    private GoldDataService goldDataService;

    /**
     * 增加金数据表单
     * @param goldFingerPO
     * @return
     */
    @PostMapping("/insert")
    public ResultInfo insert(@RequestBody GoldFingerPO goldFingerPO){
        if(StringUtil.haveEmpty(goldFingerPO.getFormId(),goldFingerPO.getFormName(),goldFingerPO.getPostURL(),goldFingerPO.getSrcName(),goldFingerPO.getTypeName(),goldFingerPO.getZxStyle())){
            throw new RException(ExceptionEnum.LOSE_FILED);
        }
        StaffPO staff=getCurrentLoginStaff();
        goldFingerPO.setStaffId(staff.getId());
        goldFingerPO.setCreateorName(staff.getNickName());
        goldFingerPO.setCompanyId(staff.getCompanyId());
        try {
            goldDataService.insert(goldFingerPO);
        }catch (Exception e){
            throw new RException(ExceptionEnum.ADD_FAIL);
        }
        return ResultInfoUtil.success(TigMsgEnum.SAVE_SUCCESS);
    }

    /**
     * 删除金数据表单
     * @param id
     * @return
     */
    @GetMapping("/delete")
    public ResultInfo delete(@RequestParam Integer id){
        try {
            goldDataService.delete(id);
        }catch (Exception e){
            throw new RException(ExceptionEnum.DELETE_FAIL);
        }
        return ResultInfoUtil.success(TigMsgEnum.DELETE_SUCCESS);
    }

    /**
     * 更新金数据表单
     * @param goldFingerPO
     * @return
     */
    @PostMapping("/update")
    public ResultInfo update(@RequestBody GoldFingerPO goldFingerPO){
        if(StringUtil.haveEmpty(goldFingerPO.getFormId(),goldFingerPO.getFormName(),goldFingerPO.getPostURL(),goldFingerPO.getSrcName(),goldFingerPO.getTypeName(),goldFingerPO.getZxStyle())){
            throw new RException(ExceptionEnum.LOSE_FILED);
        }
        StaffPO staff=getCurrentLoginStaff();
        goldFingerPO.setStaffId(staff.getId());
        goldFingerPO.setCompanyId(staff.getCompanyId());
        try{
        goldDataService.update(goldFingerPO);
        }catch (Exception e){
            throw new RException(ExceptionEnum.EDIT_FAIL);
        }
        return ResultInfoUtil.success(TigMsgEnum.UPDATE_SUCCESS);
    }

    /**
     * 金数据表单页面显示
     */
    @GetMapping("/select")
    public ResultInfo select(){
        StaffPO staff=getCurrentLoginStaff();
        List<GoldFingerPO> list = goldDataService.select(staff.getCompanyId());
        return ResultInfoUtil.success(list);
    }

    /**
     *修改开关
     */
    @PostMapping("/edit_open_or_close")
    public ResultInfo editOpenOrClose(@RequestBody GoldFingerPO goldFingerPO){
        goldDataService.editOpenOrClose(goldFingerPO);
        return ResultInfoUtil.success(TigMsgEnum.UPDATE_SUCCESS);
    }

    @PostMapping("/gold_customer_select")
    public ResultInfo goldCustomerSelect( @RequestBody JSONObject params){
        QueryMapDTO queryMapDTO=JSONObject.parseObject(params.getJSONObject("queryMapDTO").toJSONString(),QueryMapDTO.class) ;
         GoldCustomerDTO goldCustomerDTO=JSONObject.parseObject(params.getJSONObject("goldCustomerDTO").toJSONString(),GoldCustomerDTO.class) ;
        return ResultInfoUtil.success(goldDataService.goldCustomerSelect(queryMapDTO,goldCustomerDTO));
    }
}
