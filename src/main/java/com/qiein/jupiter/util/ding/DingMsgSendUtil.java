package com.qiein.jupiter.util.ding;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mzlion.easyokhttp.HttpClient;
import com.qiein.jupiter.util.TimeUtil;
import com.qiein.jupiter.web.dao.StaffDao;
import com.qiein.jupiter.web.entity.dto.SendDingMsgDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 钉钉企业消息发送工具类
 *
 * @Auther: Tt(yehuawei)
 * @Date: 2018/7/13 16:03
 */
@Component
public class DingMsgSendUtil {

    @Autowired
    private StaffDao staffDao;

    private static Logger logger = LoggerFactory.getLogger(DingMsgSendUtil.class);

    public static final String MSG_TYPE_TEXT = "text";

    public static String dingUrl;

    @Value("${ding.baseUrl}")
    public void setDingUrl(String dingUrl) {
        DingMsgSendUtil.dingUrl = dingUrl;
    }

    /**
     * 功能描述:
     * 发送钉钉企业信息
     *
     * @auther: Tt(yehuawei)
     * @date:
     * @param:
     * @return:
     */
    public static void sendDingMsg(String content, int companyId, int staffId, StaffDao staffDao) {
        content = content.replaceAll("<br/>", "\n");
        content = content + "\n" + "推送时间：" + TimeUtil.getSysTimeClient();
        SendDingMsgDTO sendDingMsgDTO = staffDao.getStaffUserIdAndCorpId(companyId, staffId);
        if (sendDingMsgDTO == null) {
            logger.error("未找到发送对象,companyId: " + companyId + " ,staffId: " + staffId);
            return;
        }
        sendDingMsgDTO.setType(MSG_TYPE_TEXT);
        sendDingMsgDTO.setContent(content);
        sendDingMsg(sendDingMsgDTO);
    }

    /**
     * 发送钉钉企业信息
     *
     * @param sendDingMsgDTO
     */
    public static void sendDingMsg(SendDingMsgDTO sendDingMsgDTO) {
        try {
            String resultJsonStr = HttpClient.textBody(dingUrl + "/send_ding_msg")
                    .json(sendDingMsgDTO)
                    .asString();
            if (JSONObject.parseObject(resultJsonStr).getIntValue("code") != 100000)
                logger.error(JSON.toJSONString(sendDingMsgDTO) + " 》》发送失败");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
