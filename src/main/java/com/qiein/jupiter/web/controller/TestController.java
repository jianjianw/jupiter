package com.qiein.jupiter.web.controller;

import com.qiein.jupiter.util.ResultInfo;
import com.qiein.jupiter.util.ResultInfoUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {


    @GetMapping("/success")
    public ResultInfo success(){
        return ResultInfoUtil.success( "success");
    }

    @GetMapping("/err")
    public ResultInfo error(){
        return ResultInfoUtil.error( 100,"err");
    }
}
