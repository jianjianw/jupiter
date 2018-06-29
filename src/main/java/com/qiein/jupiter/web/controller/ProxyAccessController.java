package com.qiein.jupiter.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.mzlion.easyokhttp.HttpClient;
import com.qiein.jupiter.util.ResultInfo;
import com.qiein.jupiter.util.ResultInfoUtil;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @Author: shiTao
 */
@RequestMapping("/proxy_access")
@RestController
public class ProxyAccessController {

    /**
     * post跨越代理
     *
     * @return
     */
    @PostMapping("/post")
    public ResultInfo post(String url, @RequestBody JSONObject param) {
        String s = HttpClient.textBody(url).json(param.toString()).asString();
        return ResultInfoUtil.success(JSONObject.parse(s));
    }

    /**
     * post跨越代理
     *
     * @return
     */
    @GetMapping("/get")
    public ResultInfo get(String url, @RequestBody Map<String, String> param) {
        String s =HttpClient.get(url).queryString(param).asString();
        return ResultInfoUtil.success(JSONObject.parse(s));
    }
}
