package com.qiein.jupiter.web.controller;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;
import com.mzlion.easyokhttp.HttpClient;
import com.qiein.jupiter.enums.TigMsgEnum;
import com.qiein.jupiter.exception.ExceptionEnum;
import com.qiein.jupiter.exception.RException;
import com.qiein.jupiter.util.MD5Util;
import com.qiein.jupiter.util.ResultInfo;
import com.qiein.jupiter.util.ResultInfoUtil;
import com.qiein.jupiter.util.SmsUtil;
import com.qiein.jupiter.web.entity.dto.SendMsgDTO;
import com.qiein.jupiter.web.entity.dto.SendMsgToDTO;
import com.qiein.jupiter.web.entity.po.ShopPO;
import com.qiein.jupiter.web.entity.po.StaffPO;
import com.qiein.jupiter.web.service.ClientService;
import com.qiein.jupiter.web.service.SendMsgService;
import com.qiein.jupiter.web.service.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.POST;
import java.text.SimpleDateFormat;
import java.util.Date;
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

    @Autowired
    private SendMsgService sendMsgService;

    @Autowired
    private ClientService clientService;
    /**
     * 短信发送（门店预约）
     */
    @PostMapping("send_msg")
    public ResultInfo sendMsg(@RequestBody SendMsgDTO sendMsgDTO){
        StaffPO staff=getCurrentLoginStaff();
        sendMsgDTO.setCompanyId(staff.getCompanyId());
        Map<String,String> map=sendMsgDTO.getMap();
        //获取门店信息
        ShopPO shopPO=shopService.findShop(Integer.parseInt(map.get("shopId")));
        map.put("address",shopPO.getShopName());
        //判断门店电话是否为空
        if(shopPO.getServicePhone()==null||shopPO.getServicePhone()==""){
            map.put("telno","");
        }else{
            map.put("telno",shopPO.getServicePhone());
        }
        String templateId=sendMsgService.getTemplateId("YYJD",staff.getCompanyId());
        sendMsgDTO.setTemplateId(templateId);
        //获取时间
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String date_string = sdf.format(new Date(Long.parseLong(map.get("time"))*1000L));
        map.put("time",date_string);
        Integer id=clientService.findId(map.get("kzId"),staff.getCompanyId());
        map.put("code","YYJD"+id);
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
        JSONObject getBack=JSONObject.parseObject(back);
        String code=(String)getBack.get("code");
        if(code!="100000"){
            throw new RException((String)getBack.get("msg"));
        }
        return ResultInfoUtil.success(TigMsgEnum.SEND_SUCCESS);
    }

    /**
     * 获取模板样式
     */
    @PostMapping("get_template")
    public ResultInfo getTemplate(@RequestBody SendMsgDTO sendMsgDTO){
        StaffPO staff=getCurrentLoginStaff();
        sendMsgDTO.setCompanyId(staff.getCompanyId());
        String templateId=sendMsgService.getTemplateId("YYJD",staff.getCompanyId());
        String url="http://114.55.249.156:8286/send_msg/find_company_template";
        String templateText=HttpClient
                .get(url)
                .queryString("templateNum", templateId)
                .queryString("companyId",sendMsgDTO.getCompanyId())
                .asString();
        JSONObject json=JSONObject.parseObject(templateText);
        templateText=(String)json.get("data");
        String backcode=(String)json.get("code");
        if(backcode!="100000"){
            throw new RException(ExceptionEnum.TEMPLATE_LOSE);
        }
        Map<String,String> map=sendMsgDTO.getMap();
        ShopPO shopPO=shopService.findShop(Integer.parseInt(sendMsgDTO.getMap().get("shopId")));
        map.put("address",shopPO.getShopName());
        if(shopPO.getServicePhone()==null||shopPO.getServicePhone()==""){
            map.put("telno","");
        }else{
            map.put("telno",shopPO.getServicePhone());
        }
        Integer id=clientService.findId(map.get("kzId"),staff.getCompanyId());
        map.put("code","YYJD"+id);
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String date_string = sdf.format(new Date(Long.parseLong(map.get("time"))*1000L));
        map.put("time",date_string);
        for(String key:map.keySet()){
            templateText=templateText.replace("${"+key+"}",map.get(key));
        }
        return ResultInfoUtil.success(templateText);
    }

}
