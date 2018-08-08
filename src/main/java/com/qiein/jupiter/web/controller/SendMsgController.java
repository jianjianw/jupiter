package com.qiein.jupiter.web.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mzlion.easyokhttp.HttpClient;
import com.qiein.jupiter.constant.CommonConstant;
import com.qiein.jupiter.enums.TipMsgEnum;
import com.qiein.jupiter.exception.ExceptionEnum;
import com.qiein.jupiter.exception.RException;
import com.qiein.jupiter.util.*;
import com.qiein.jupiter.web.entity.dto.SendMsgDTO;
import com.qiein.jupiter.web.entity.dto.SendMsgToDTO;
import com.qiein.jupiter.web.entity.po.ShopPO;
import com.qiein.jupiter.web.entity.po.StaffPO;
import com.qiein.jupiter.web.entity.vo.MsgTemplateVO;
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
    //apollo发送短信接口
    @Value("${apollo.sendMsg}")
    private String sendMsgUrl;
    //apollo获取模板接口
    @Value("${apollo.findCompanyTemplate}")
    private String findCompanyTemplateUrl;
    //apollo获取用户短信页面管理
    @Value(("${apollo.msgTemplateLog}"))
    private String msgTemplateLogUrl;
    //apollo获取短信发送记录页面
    @Value(("${apollo.findSendMsg}"))
    private String findSendMsgUrl;

    @Autowired
    private ClientService clientService;
    /**
     * 短信发送（门店预约）
     */
    @PostMapping("send_msg")
    public ResultInfo sendMsg(@RequestBody SendMsgDTO sendMsgDTO){
        StaffPO staff=getCurrentLoginStaff();
        sendMsgDTO.setCompanyId(staff.getCompanyId());
        sendMsgDTO.setStaffId(staff.getId());
        sendMsgDTO.setStaffName(staff.getNickName());
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
        return ResultInfoUtil.success(TipMsgEnum.SEND_SUCCESS);
    }

    /**
     * 获取模板样式
     */
    @PostMapping("get_template")
    public ResultInfo getTemplate(@RequestBody SendMsgDTO sendMsgDTO){
        StaffPO staff = getCurrentLoginStaff();
        sendMsgDTO.setCompanyId(staff.getCompanyId());
        String msgTemlate = HttpClient
                .get(findCompanyTemplateUrl)
                .queryString("templateType", CommonConstant.YYJD)
                .queryString("companyId", sendMsgDTO.getCompanyId())
                .asString();
        JSONObject json = JSONObject.parseObject(msgTemlate);
        MsgTemplateVO msgTemplateVO = JSONObject.parseObject(json.getString("data"), MsgTemplateVO.class);
        String templateText = msgTemplateVO.getTemplateText();
        Integer backcode = (Integer) json.get("code");
        if (backcode != 100000) {
            throw new RException(ExceptionEnum.TEMPLATE_LOSE);
        }
        Map<String, String> map = sendMsgDTO.getMap();
        ShopPO shopPO = shopService.findShop(Integer.parseInt(sendMsgDTO.getMap().get("shopId")));
        map.put("address", shopPO.getAddress());
        if (msgTemplateVO.getIsSelf() != CommonConstant.SELF) {
            if (StringUtil.isEmpty(shopPO.getServicePhone())) {
                map.put("telno", "");
            } else {
                map.put("telno", shopPO.getServicePhone());
            }
        } else {
            if (StringUtil.isEmpty(staff.getPhone())) {
                map.put("telno", "");
            } else {
                map.put("telno", staff.getPhone());
            }
        }
        Integer id = clientService.findId(map.get("kzId"), staff.getCompanyId());
        map.put("code",  id+CommonConstant.NULL_STR);
        String date_string = TimeUtil.intMillisToTimeStr(Integer.parseInt(map.get("time")));
        map.put("time", date_string);
        for (String key : map.keySet()) {
            templateText = templateText.replace("${" + key + "}", map.get(key));
        }
        return ResultInfoUtil.success(templateText);
    }

    /**
     * 短信账号记录
     */
    @GetMapping("/msg_template_log")
    public ResultInfo msgTemplateLog(@RequestParam String startTime,@RequestParam String endTime) {
        StaffPO staff=getCurrentLoginStaff();
        String templateText=HttpClient
                .get(msgTemplateLogUrl)
                .queryString("startTime", startTime)
                .queryString("endTime", endTime)
                .queryString("companyId",staff.getCompanyId())
                .asString();
        JSONObject json=JSONObject.parseObject(templateText);
        return ResultInfoUtil.success(json);
    }

    /**
     * 短信发送记录
     */
    @GetMapping("/find_send_msg")
    public ResultInfo findSendMsg(@RequestParam String startTime,@RequestParam String endTime,@RequestParam String phone,@RequestParam String type,@RequestParam Integer pageNum,@RequestParam Integer pageSize){
        StaffPO staff=getCurrentLoginStaff();
        String templateText=HttpClient
                .get(findSendMsgUrl)
                .queryString("startTime", startTime)
                .queryString("endTime", endTime)
                .queryString("companyId",staff.getCompanyId())
                .queryString("phone", phone)
                .queryString("type", type)
                .queryString("pageNum", pageNum)
                .queryString("pageSize", pageSize)
                .asString();

        JSONObject json=JSONObject.parseObject(templateText);
        return  ResultInfoUtil.success(json);
    }

}
