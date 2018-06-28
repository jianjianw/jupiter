package com.qiein.jupiter.web.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mzlion.easyokhttp.HttpClient;
import com.qiein.jupiter.constant.CommonConstant;
import com.qiein.jupiter.enums.TigMsgEnum;
import com.qiein.jupiter.exception.ExceptionEnum;
import com.qiein.jupiter.exception.RException;
import com.qiein.jupiter.util.*;
import com.qiein.jupiter.web.entity.dto.SendMsgDTO;
import com.qiein.jupiter.web.entity.dto.SendMsgToDTO;
import com.qiein.jupiter.web.entity.po.ShopPO;
import com.qiein.jupiter.web.entity.po.StaffPO;
import com.qiein.jupiter.web.service.ClientService;
import com.qiein.jupiter.web.service.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 短信发送
 * Author xiangliang 2018/6/24
 */
@RestController
@RequestMapping("/send_msg")
@Validated
@PropertySource({"classpath:application-dev.properties"})
public class SendMsgController extends BaseController{

    @Autowired
    private ShopService shopService;

    @Value("${apollo.sendMsg}")
    private String sendMsgUrl;

    @Value("${apollo.findCompanyTemplate}")
    private String findCompanyTemplateUrl;

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
        map.put("address",shopPO.getAddress());
        //判断门店电话是否为空

        if( StringUtil.isEmpty(shopPO.getServicePhone())){
            map.put("telno","");
        }else{
            map.put("telno",shopPO.getServicePhone());
        }
        //获取时间
        String date_string=TimeUtil.intMillisToTimeStr(Integer.parseInt(map.get("time")));
        map.put("time",date_string);
        Integer id=clientService.findId(map.get("kzId"),staff.getCompanyId());
        map.put("code",CommonConstant.YYJD+id);
        sendMsgDTO.setTemplateType(CommonConstant.YYJD);
        SendMsgToDTO sendMsgToDTO=new SendMsgToDTO();
        sendMsgToDTO.setParams(sendMsgDTO);
        String json=JSON.toJSONString(sendMsgToDTO);
        String sign=MD5Util.getApolloMd5(json);
        String back=HttpClient
                // 请求方式和请求url
                .textBody(sendMsgUrl)
                // post提交json
                .json(json)
                .queryString("sign",sign)
                .asString();
        JSONObject getBack=JSONObject.parseObject(back);
        Integer code=(Integer)getBack.get("code");
        if(code!=100000){
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
        String templateText=HttpClient
                .get(findCompanyTemplateUrl)
                .queryString("templateType", CommonConstant.YYJD)
                .queryString("companyId",sendMsgDTO.getCompanyId())
                .asString();
        JSONObject json=JSONObject.parseObject(templateText);
        templateText=(String)json.get("data");
        Integer backcode=(Integer)json.get("code");
        if(backcode!=100000){
            throw new RException(ExceptionEnum.TEMPLATE_LOSE);
        }
        Map<String,String> map=sendMsgDTO.getMap();
        ShopPO shopPO=shopService.findShop(Integer.parseInt(sendMsgDTO.getMap().get("shopId")));
        map.put("address",shopPO.getAddress());
        if( StringUtil.isEmpty(shopPO.getServicePhone())){
            map.put("telno","");
        }else{
            map.put("telno",shopPO.getServicePhone());
        }
        Integer id=clientService.findId(map.get("kzId"),staff.getCompanyId());
        map.put("code",CommonConstant.YYJD+id);
        String date_string=TimeUtil.intMillisToTimeStr(Integer.parseInt(map.get("time")));
        map.put("time",date_string);
        for(String key:map.keySet()){
            templateText=templateText.replace("${"+key+"}",map.get(key));
        }
        return ResultInfoUtil.success(templateText);
    }

}
