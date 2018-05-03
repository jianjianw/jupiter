package com.qiein.jupiter.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.qiein.jupiter.constant.TipMsgConstant;
import com.qiein.jupiter.util.OSSUtil;
import com.qiein.jupiter.util.ResultInfo;
import com.qiein.jupiter.util.ResultInfoUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 阿里OSSController
 * Created by Administrator on 2018/4/26 0026.
 */
@RestController
@RequestMapping("/oss")
public class OSSController extends BaseController{

    /**
     * 获取Web直传Policy
     * @return
     */
    @GetMapping("/get_policy")
    public ResultInfo getPolicyAndCallback(){
        return ResultInfoUtil.success(TipMsgConstant.SUCCESS,OSSUtil.getPolicy(60));
    }


}
