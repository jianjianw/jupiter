package com.qiein.jupiter.web.controller;

import com.qiein.jupiter.constant.TipMsgConstant;
import com.qiein.jupiter.util.OSSUtil;
import com.qiein.jupiter.util.ResultInfo;
import com.qiein.jupiter.util.ResultInfoUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Administrator on 2018/4/26 0026.
 */
@RestController
@RequestMapping("/oss")
public class OSSController extends BaseController{

    @GetMapping("/get_info")
    public ResultInfo getPolicyAndCallback(){
        //TODO 获取Policy和回调设置
        OSSUtil.getOssUtil();
        return ResultInfoUtil.success(TipMsgConstant.SUCCESS,null);
    }
}
