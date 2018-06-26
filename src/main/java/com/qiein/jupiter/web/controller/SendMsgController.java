package com.qiein.jupiter.web.controller;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;
import com.mzlion.easyokhttp.HttpClient;
import com.qiein.jupiter.enums.TigMsgEnum;
import com.qiein.jupiter.util.MD5Util;
import com.qiein.jupiter.util.ResultInfo;
import com.qiein.jupiter.util.ResultInfoUtil;
import com.qiein.jupiter.util.SmsUtil;
import com.qiein.jupiter.web.entity.dto.SendMsgDTO;
import com.qiein.jupiter.web.entity.dto.SendMsgToDTO;
import com.qiein.jupiter.web.entity.po.StaffPO;
import com.qiein.jupiter.web.service.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.POST;
import java.util.Map;

/**
 * 短信发送
 * Author xiangliang 2018/6/24
 */
@RestController
@RequestMapping("/send_msg")
@Validated
public class SendMsgController extends BaseController{

    @Autowired
    private ShopService shopService;
    /**
     * 短信发送（门店预约）
     */
    @PostMapping("send_msg")
    public ResultInfo sendMsg(@RequestBody SendMsgDTO sendMsgDTO){
        StaffPO staff=getCurrentLoginStaff();
        sendMsgDTO.setCompanyId(staff.getCompanyId());
        sendMsgDTO.getMap().put("address",shopService.findShop(Integer.parseInt(sendMsgDTO.getMap().get("shopId"))));
        SendMsgToDTO sendMsgToDTO=new SendMsgToDTO();
        sendMsgToDTO.setParams(sendMsgDTO);
        String json=JSON.toJSONString(sendMsgToDTO);
        String sign=MD5Util.getApolloMd5(json);
        String url="http://114.55.249.156:8286/send_msg/send_msg";
        String back=HttpClient
                // 请求方式和请求url
                .textBody(url)
                // post提交json
                .json(json)
                .queryString("sign",sign)
                .asString();
        return ResultInfoUtil.success(TigMsgEnum.SEND_SUCCESS);
    }

    /**
     * 获取模板样式
     */
    @PostMapping("get_template")
    public ResultInfo getTemplate(@RequestBody SendMsgDTO sendMsgDTO){
        StaffPO staff=getCurrentLoginStaff();
        sendMsgDTO.setCompanyId(staff.getCompanyId());
        String url="http://114.55.249.156:8286/send_msg/find_company_template";
        String templateText=HttpClient
                .get(url)
                .queryString("templateNum", sendMsgDTO.getTemplateId())
                .queryString("companyId",sendMsgDTO.getCompanyId())
                .asString();
        JSONObject json=JSONObject.parseObject(templateText);
        templateText=(String)json.get("data");
        Map<String,String> map=sendMsgDTO.getMap();
        map.put("address",shopService.findShop(Integer.parseInt(sendMsgDTO.getMap().get("shopId"))));
        map.put("code","cs101");

        for(String key:map.keySet()){
            templateText=templateText.replace("${"+key+"}",map.get(key));
        }
        return ResultInfoUtil.success(templateText);
    }

}
