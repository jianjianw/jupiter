package com.qiein.jupiter.web.controller;

import com.alibaba.druid.Constants;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;
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
import com.qiein.jupiter.web.entity.vo.GoldFingerShowVO;
import com.qiein.jupiter.web.service.GoldDataService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
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
        if(StringUtil.haveEmpty(goldFingerPO.getFormId(),goldFingerPO.getFormName(),goldFingerPO.getSrcName(),goldFingerPO.getTypeName(),goldFingerPO.getZxStyle())){
            throw new RException(ExceptionEnum.LOSE_FILED);
        }
        StaffPO staff=getCurrentLoginStaff();
        goldFingerPO.setStaffId(staff.getId());
        goldFingerPO.setCreateorName(staff.getNickName());
        goldFingerPO.setCompanyId(staff.getCompanyId());
        goldDataService.insert(goldFingerPO);

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
        if(StringUtil.haveEmpty(goldFingerPO.getFormId(),goldFingerPO.getFormName(),goldFingerPO.getSrcName(),goldFingerPO.getTypeName(),goldFingerPO.getZxStyle())){
            throw new RException(ExceptionEnum.LOSE_FILED);
        }
        StaffPO staff=getCurrentLoginStaff();
        goldFingerPO.setStaffId(staff.getId());
        goldFingerPO.setCompanyId(staff.getCompanyId());
        goldDataService.update(goldFingerPO);
        return ResultInfoUtil.success(TigMsgEnum.UPDATE_SUCCESS);
    }

    /**
     * 金数据表单页面显示
     */
    @GetMapping("/select")
    public ResultInfo select(){
        StaffPO staff=getCurrentLoginStaff();
        List<GoldFingerPO> list = goldDataService.select(staff.getCompanyId());
        HttpServletRequest request = ((ServletRequestAttributes)      RequestContextHolder.getRequestAttributes()).getRequest();
        String postUrl = request.getRequestURL().substring(0, request.getRequestURL().lastIndexOf("/")) + "/add_client_info";
        GoldFingerShowVO goldFingerShowVO=new GoldFingerShowVO();
        goldFingerShowVO.setList(list);
        goldFingerShowVO.setPostUrl(postUrl);
        return ResultInfoUtil.success(goldFingerShowVO);
    }

    /**
     *修改开关
     */
    @PostMapping("/edit_open_or_close")
    public ResultInfo editOpenOrClose(@RequestBody GoldFingerPO goldFingerPO){
        goldDataService.editOpenOrClose(goldFingerPO);
        return ResultInfoUtil.success(TigMsgEnum.UPDATE_SUCCESS);
    }

    /**
     * 获取日志列表
     * @param params
     * @return
     */
    @PostMapping("/gold_customer_select")
    public ResultInfo goldCustomerSelect( @RequestBody JSONObject params){
        QueryMapDTO queryMapDTO=JSONObject.parseObject(params.getJSONObject("queryMapDTO").toJSONString(),QueryMapDTO.class) ;
         GoldCustomerDTO goldCustomerDTO=JSONObject.parseObject(params.getJSONObject("goldCustomerDTO").toJSONString(),GoldCustomerDTO.class) ;
        return ResultInfoUtil.success(goldDataService.goldCustomerSelect(queryMapDTO,goldCustomerDTO));

    }

    /**
     * 金数据表单回调
     * */
    @PostMapping("/receive_gold_data_form")
    public ResultInfo receiveGoldDataForm(@RequestBody JSONObject jsonObject){
        //TODO 获取到数据，存储temp与plugSetting
        goldDataService.receiveGoldDataForm(jsonObject,getCurrentLoginStaff());
        return ResultInfoUtil.success();
    }


    /**
     * 筛选
     */
    @GetMapping("/addkz_by_gold_temp")
    public ResultInfo addkzByGoldTemp(@RequestParam Integer id){
        //goldDataService.addkzByGoldTemp(id);
        return  ResultInfoUtil.success(TigMsgEnum.UPDATE_SUCCESS);
    }
}
