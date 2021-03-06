package com.qiein.jupiter.msg.goeasy;

import com.qiein.jupiter.constant.CommonConstant;
import com.qiein.jupiter.msg.websocket.WebSocketMsgUtil;
import com.qiein.jupiter.util.*;
import com.qiein.jupiter.util.ding.DingMsgSendUtil;
import com.qiein.jupiter.util.wechat.WeChatPushMsgDTO;
import com.qiein.jupiter.util.wechat.WeChatPushUtil;
import com.qiein.jupiter.web.dao.ClientInfoDao;
import com.qiein.jupiter.web.dao.StaffDao;
import com.qiein.jupiter.web.entity.dto.ClientPushDTO;
import com.qiein.jupiter.web.entity.po.CompanyPO;
import com.qiein.jupiter.web.entity.po.StaffPO;
import com.qiein.jupiter.web.service.CompanyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.qiein.jupiter.constant.ClientLogConst;
import com.qiein.jupiter.web.dao.NewsDao;
import com.qiein.jupiter.web.entity.dto.ClientGoEasyDTO;
import com.qiein.jupiter.web.entity.po.NewsPO;

import io.goeasy.GoEasy;
import io.goeasy.publish.GoEasyError;
import io.goeasy.publish.PublishListener;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * GoEasy消息推送
 *
 * @author JingChenglong 2018/04/17 15:54
 */
@Component
public class GoEasyUtil {

    private final static Logger log = LoggerFactory.getLogger(GoEasyUtil.class);

    private static CompanyService companyService;

    /**
     * GoEasy对接平台KEY
     */
    private static String goeasyKey;

    /**
     * GoEasy实例地址
     */
    private static String goeasyLinkAddr;

    /**
     * GoEasy-WEB消息频道前缀
     */
    private static String hmCrmChannelSuffix;

    /**
     * GoEasy-客户端消息频道前缀
     */
    private static String hmAppChannelSuffix;

    /**
     * 当前服务器的ip地址
     */
    public static String serverAddress;


    /**
     * 初始化GoEasy实例
     */
    private static GoEasy goeasyInstance = null;


    /**
     * 消息内容体封装
     */
//    private static JSONObject messageJson = null;
//    private static JSONObject contentJson = null;

    static {
        goeasyKey = PropertiesUtil.getValue("goeasy.key");
        goeasyLinkAddr = PropertiesUtil.getValue("goeasy.linkaddr");
        hmCrmChannelSuffix = PropertiesUtil.getValue("goeasy.hmcrmchannelsuffix");
        hmAppChannelSuffix = PropertiesUtil.getValue("goeasy.hmappchannelsuffix");

        goeasyInstance = new GoEasy(goeasyLinkAddr, goeasyKey);
    }

    public static void destroy() {
        goeasyKey = "";
        goeasyLinkAddr = "";
        hmCrmChannelSuffix = "";

        goeasyInstance = null;
        log.info("GoEasy消息组件停止服务");
    }


    @Autowired
    public void setCompanyService(CompanyService companyService) {
        GoEasyUtil.companyService = companyService;
    }

    /**
     * GoEasy消息推送
     *
     * @param channel
     * @param content
     */
    private static synchronized void push(String channel, String content) {
        try {
            if (goeasyInstance == null) {
                log.info("LogUtils.log()-->GoEasy实例初始化失败");
                return;
            }
            goeasyInstance.publish(channel, content, new PublishListener() {
                public void onSuccess() {
                    // log.info("LogUtils.log()-->消息发送成功--[channel : " + "" + " ;
                    // content :" + "" + channel);
                }

                public void onFailed(GoEasyError error) {
                    log.error("goeasy 发送失败：" + error.getCode() + error.getContent());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Web消息推送封装
     *
     * @param type
     * @param companyId
     * @param staffId
     * @param content
     */
    private static synchronized void pushWeb(String type, int companyId, int staffId, JSONObject content) {
        JSONObject messageJson = new JSONObject();
        messageJson.put("cid", companyId);
        messageJson.put("uid", staffId);
        messageJson.put("type", type);
        messageJson.put("content", content);

        String channel = hmCrmChannelSuffix;
        channel += companyId;
        channel += "_";
        channel += staffId;

        push(channel, messageJson.toString());
    }

    /**
     * 客户端消息推送封装
     *
     * @param type
     * @param companyId
     * @param staffId
     * @param content
     */
    private static synchronized void pushApp(String type, int companyId, int staffId, JSONObject content) {
        JSONObject messageJson = new JSONObject();
        messageJson.put("cid", companyId);
        messageJson.put("uid", staffId);
        messageJson.put("type", type);
        messageJson.put("content", content);

        String channel = hmAppChannelSuffix;
        channel += companyId;
        channel += "_";
        channel += staffId;

        push(channel, messageJson.toString());
    }

    /**
     * 客户端推送客资领取消息
     *
     * @param companyId
     * @param staffId
     * @param kzNum
     * @param kzId
     * @param logId
     * @param overTime
     */
    public static synchronized void pushAppInfoReceive(int companyId, int staffId, int kzNum, String kzId, String logId,
                                                       int overTime) {
        JSONObject contentJson = new JSONObject();
        contentJson.put("kzid", kzId);
        contentJson.put("logid", logId);
        contentJson.put("kznum", kzNum);
        contentJson.put("overtime", overTime);

        //TODO
        String url = "http://crm-jupiter.oss-cn-hangzhou.aliyuncs.com/wechat/index.html?kzId=" + kzId + "&logId=" + logId + "&uid=" + staffId + "&cid=" + companyId;
        CompanyPO companyPO = companyService.getById(companyId);
        WeChatPushUtil.pushNewClientMsg(new WeChatPushMsgDTO(companyId, companyPO.getCompanyName(), staffId, url, "保密", "保密",
                new SimpleDateFormat("YYYY-MM-DD HH:mm").format(new Date()), kzId, logId));

        pushApp(MessageConts.MSG_APP_INFO_REVEIVE, companyId, staffId, contentJson);
    }

    /**
     * 推送通用消息
     *
     * @param companyId
     * @param staffId
     * @param head
     * @param msg
     */
    private static synchronized void pushCommon(int companyId, int staffId, String head, String msg) {

        JSONObject contentJson = new JSONObject();
        contentJson.put("head", head);
        contentJson.put("msg", msg);

        pushWeb(MessageConts.MSG_TYPE_COMMON, companyId, staffId, contentJson);
    }

    /**
     * 推送成功消息
     *
     * @param companyId
     * @param staffId
     * @param head
     * @param msg
     */
    private static synchronized void pushSuccess(int companyId, int staffId, String head, String msg) {

        JSONObject contentJson = new JSONObject();
        contentJson.put("head", head);
        contentJson.put("msg", msg);

        pushWeb(MessageConts.MSG_TYPE_SUCCESS, companyId, staffId, contentJson);
    }

    /**
     * 推送警告消息
     *
     * @param companyId
     * @param staffId
     * @param head
     * @param msg
     */
    private static synchronized void pushWarn(int companyId, int staffId, String head, String msg) {

        JSONObject contentJson = new JSONObject();
        contentJson.put("head", head);
        contentJson.put("msg", msg);

        pushWeb(MessageConts.MSG_TYPE_WARN, companyId, staffId, contentJson);
    }

    /**
     * 推送错误消息
     *
     * @param companyId
     * @param staffId
     * @param head
     * @param msg
     */
    public static synchronized void pushError(int companyId, int staffId, String head, String msg) {

        JSONObject contentJson = new JSONObject();
        contentJson.put("head", head);
        contentJson.put("msg", msg);

        pushWeb(MessageConts.MSG_TYPE_ERROR, companyId, staffId, contentJson);
    }

    /**
     * 推送客资领取消息
     *
     * @param companyId
     * @param staffId
     * @param head
     */
    public static synchronized void pushReceive(int companyId, int staffId, String head, ClientGoEasyDTO info) {

        JSONObject contentJson = new JSONObject();
        contentJson.put("head", head);
        contentJson.put("kz", info);
        contentJson.put("contact", StringUtil.isNotEmpty(info.getKzPhone()) ? info.getKzPhone() :
                StringUtil.isNotEmpty(info.getKzWechat()) ? info.getKzWechat() :
                        StringUtil.isNotEmpty(info.getKzQq()) ? info.getKzQq() : info.getKzWw());
        pushWeb(MessageConts.MSG_TYPE_RECEIVE, companyId, staffId, contentJson);
    }

    /**
     * 通知网页客资主动重载
     *
     * @param companyId
     * @param staffId
     */
    public static void pushInfoRefresh(int companyId, int staffId, WebSocketMsgUtil webSocketMsgUtil) {
        webSocketMsgUtil.pushClientInfoRefresh(companyId, staffId);
//        JSONObject contentJson = new JSONObject();
//        pushWeb(MessageConts.MSG_TYPE_INFO_REFRESH, companyId, staffId, contentJson);
    }

    /**
     * 通知网页主动重验证身份信息
     *
     * @param companyId
     * @param staffId
     */
    public static void pushStaffRefresh(int companyId, int staffId, String ip, String address) {

        JSONObject contentJson = new JSONObject();
        contentJson.put("ip", ip);
        contentJson.put("address", address);
        pushWeb(MessageConts.MSG_TYPE_STAFF_REFRESH, companyId, staffId, contentJson);
    }

    /**
     * 状态重载消息
     *
     * @param companyId
     * @param staffId
     */
    public static void pushStatusRefresh(int companyId, int staffId, WebSocketMsgUtil webSocketMsgUtil) {
        webSocketMsgUtil.pushBaseInfoFresh(companyId, staffId);
//        JSONObject contentJson = new JSONObject();
//        pushWeb(MessageConts.MSG_TYPE_STATUS_REFRESH, companyId, staffId, contentJson);
    }

    /**
     * 发送闪信
     *
     * @param companyId
     * @param barStaffId
     * @param senderId
     * @param senderName
     * @param kzName
     * @param kzPhone
     * @param kzChannel
     * @param kzSource
     * @param kzMemo
     * @param msg
     */
    public static void pushFlashMsgForInfo(int companyId, int barStaffId, int senderId, String senderName,
                                           String kzName, String kzPhone, String id, String kzId, String kzChannel, String kzSource, String kzMemo, String msg, String createTime) {
        JSONObject contentJson = new JSONObject();
        contentJson.put("senderId", senderId);
        contentJson.put("senderName", senderName);
        contentJson.put("kzName", kzName);
        contentJson.put("kzPhone", kzPhone);
        contentJson.put("id", id);
        contentJson.put("createtime", createTime);
        contentJson.put("kzId", kzId);
        contentJson.put("kzChannel", kzChannel);
        contentJson.put("kzSource", kzSource);
        contentJson.put("kzMemo", kzMemo);
        contentJson.put("msg", msg);
        pushWeb(MessageConts.MSG_TYPE_FLASH, companyId, barStaffId, contentJson);
    }

    /**
     * 推送客资点击领取消息
     *
     * @param companyId
     * @param staffId
     * @param info
     */
    public static synchronized void pushInfoReceive(int companyId, int staffId, ClientDTO info) {

        JSONObject contentJson = new JSONObject();
        contentJson.put("info", info);
        pushWeb(MessageConts.MSG_TYPE_RECEIVE, companyId, staffId, contentJson);
    }

    /**
     * 门店流失通知
     *
     * @param companyId
     * @param staffId
     * @param info
     * @param reason
     * @param comeShopTime
     * @param shopName
     * @param receptorName
     */
    public static void pushShopMeetRunOff(int companyId, int staffId, ClientDTO info, String reason,
                                          String comeShopTime, String shopName, String receptorName) {

        StringBuffer sb = new StringBuffer();
        sb.append(" 您的客人在 ");
        sb.append(StringUtil.nullToStrTrim(shopName));
        sb.append(" 流失 ");
        String header = sb.toString();

        sb.delete(0, sb.length());
        sb.append("<br/><br/>姓名：");
        sb.append(StringUtil.nullToStrTrim(info.getKzName()));
        sb.append("<br/>手机：").append(StringUtil.nullToStrTrim(info.getKzPhone()));
        sb.append("<br/>微信：").append(StringUtil.nullToStrTrim(info.getKzWeChat()));
        sb.append("<br/><br/>接待门店：").append(StringUtil.nullToStrTrim(shopName));
        sb.append("<br/>接待门市：").append(StringUtil.nullToStrTrim(receptorName));
        sb.append("<br/><br/>到店时间：").append(StringUtil.nullToStrTrim(comeShopTime));
        sb.append("<br/>流失原因：").append(reason);
        String msg = sb.toString();
        pushCommon(companyId, staffId, header, msg);
    }

    /**
     * 在线订单通知
     *
     * @param companyId
     * @param staffId
     * @param info
     * @param newsDao
     */
    public static void pushSuccessOnline(int companyId, int staffId, ClientGoEasyDTO info, NewsDao newsDao, StaffDao staffDao) {
        String head = "恭喜您，您的客户在线订单啦";
        StringBuffer sb = new StringBuffer();
        sb.append("编号：").append(info.getLetterId()).append("<br/>");
        if (StringUtil.isNotEmpty(info.getKzName())) {
            sb.append("姓名：").append(StringUtil.nullToStrTrim(info.getKzName())).append("<br/>");
        }
        if (StringUtil.isNotEmpty(info.getKzPhone())) {
            sb.append("电话：").append(StringUtil.nullToStrTrim(info.getKzPhone())).append("<br/>");
        }
        if (StringUtil.isNotEmpty(info.getKzWechat())) {
            sb.append("微信：").append(StringUtil.nullToStrTrim(info.getKzWechat())).append("<br/>");
        }
        if (StringUtil.isNotEmpty(info.getKzQq())) {
            sb.append("QQ：").append(StringUtil.nullToStrTrim(info.getKzQq())).append("<br/>");
        }
        if (StringUtil.isNotEmpty(info.getKzWw())) {
            sb.append("旺旺：").append(StringUtil.nullToStrTrim(info.getKzWw())).append("<br/>");
        }
        sb.append("渠道：").append(StringUtil.nullToStrTrim(info.getChannelName())).append("<br/>");
        sb.append("来源：").append(StringUtil.nullToStrTrim(info.getSourceName())).append("<br/>");
        sb.append("客服：").append(StringUtil.nullToStrTrim(info.getAppointName())).append("<br/>");
        sb.append("成交套系： ¥").append(info.getAmount()).append("<br/>");
        sb.append("订单时间：").append(TimeUtil.intMillisToTimeStr(info.getSuccessTime()));
        String msg = sb.toString();
        pushSuccess(companyId, staffId, head, msg);
        newsDao.insert(new NewsPO(MessageConts.MSG_TYPE_SUCCESS, head, msg.replaceAll("<br/>", "；"), info.getKzId(),
                staffId, companyId));
        DingMsgSendUtil.sendDingMsg(head + "<br/>" + sb.toString(), companyId, staffId, staffDao);

        String firstText = "恭喜您，您的客户在线订单啦";
        sb = new StringBuffer();
        sb.append("编号：").append(info.getLetterId()).append("\r\n");
        if (StringUtil.isNotEmpty(info.getKzWechat())) {
            sb.append("微信：").append(StringUtil.nullToStrTrim(info.getKzWechat())).append("\r\n");
        }
        if (StringUtil.isNotEmpty(info.getKzQq())) {
            sb.append("QQ：").append(StringUtil.nullToStrTrim(info.getKzQq())).append("\r\n");
        }
        if (StringUtil.isNotEmpty(info.getKzWw())) {
            sb.append("旺旺：").append(StringUtil.nullToStrTrim(info.getKzWw())).append("\r\n");
        }
        sb.append("渠道：").append(StringUtil.nullToStrTrim(info.getChannelName())).append("\r\n");
        sb.append("来源：").append(StringUtil.nullToStrTrim(info.getSourceName())).append("\r\n");
        sb.append("客服：").append(StringUtil.nullToStrTrim(info.getAppointName())).append("\r\n");
        sb.append("成交套系： ¥").append(info.getAmount()).append("\r\n");
        sb.append("订单时间：").append(TimeUtil.intMillisToTimeStr(info.getSuccessTime()));
        String remarkText = sb.toString();
        String companyName = staffDao.getCompanyNameByStaffId(staffId);

        WeChatPushUtil.pushMsg(new WeChatPushMsgDTO(firstText,remarkText,companyId,companyName,staffId,StringUtil.nullToStrTrim(info.getKzName()),StringUtil.nullToStrTrim(info.getKzPhone()),new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date())));
    }


    /**
     * 到店订单通知
     *
     * @param companyId
     * @param staffId
     * @param info
     * @param newsDao
     */
    public static void pushSuccessShop(int companyId, int staffId, ClientGoEasyDTO info, NewsDao newsDao, StaffDao staffDao) {
        String head = "恭喜您，您的客户在门店成功订单啦";
        StringBuffer sb = new StringBuffer();
        sb.append("编号：").append(info.getLetterId()).append("<br/>");
        if (StringUtil.isNotEmpty(info.getKzName())) {
            sb.append("姓名：").append(StringUtil.nullToStrTrim(info.getKzName())).append("<br/>");
        }
        if (StringUtil.isNotEmpty(info.getKzPhone())) {
            sb.append("电话：").append(StringUtil.nullToStrTrim(info.getKzPhone())).append("<br/>");
        }
        if (StringUtil.isNotEmpty(info.getKzWechat())) {
            sb.append("微信：").append(StringUtil.nullToStrTrim(info.getKzWechat())).append("<br/>");
        }
        if (StringUtil.isNotEmpty(info.getKzQq())) {
            sb.append("QQ：").append(StringUtil.nullToStrTrim(info.getKzQq())).append("<br/>");
        }
        if (StringUtil.isNotEmpty(info.getKzWw())) {
            sb.append("旺旺：").append(StringUtil.nullToStrTrim(info.getKzWw())).append("<br/>");
        }
        sb.append("渠道：").append(StringUtil.nullToStrTrim(info.getChannelName())).append("<br/>");
        sb.append("来源：").append(StringUtil.nullToStrTrim(info.getSourceName())).append("<br/>");
        sb.append("接待门店：").append(StringUtil.nullToStrTrim(info.getShopName())).append("<br/>");
        sb.append("接待门市：").append(StringUtil.nullToStrTrim(info.getReceptorName())).append("<br/>");
        sb.append("成交套系： ¥").append(info.getAmount()).append("<br/>");
        sb.append("订单时间：").append(TimeUtil.intMillisToTimeStr(info.getSuccessTime()));
        String msg = sb.toString();
        pushSuccess(companyId, staffId, head, msg);
        newsDao.insert(new NewsPO(MessageConts.MSG_TYPE_SUCCESS, head, msg.replaceAll("<br/>", "；"), info.getKzId(),
                staffId, companyId));
        DingMsgSendUtil.sendDingMsg(head + "<br/>" + sb.toString(), companyId, staffId, staffDao);

        //微信消息推送
        String firstText = "恭喜您，您的客户在门店成功订单啦";
        sb = new StringBuffer();
        sb.append("编号：").append(info.getLetterId()).append("\r\n");
        if (StringUtil.isNotEmpty(info.getKzWechat())) {
            sb.append("微信：").append(StringUtil.nullToStrTrim(info.getKzWechat())).append("\r\n");
        }
        if (StringUtil.isNotEmpty(info.getKzQq())) {
            sb.append("QQ：").append(StringUtil.nullToStrTrim(info.getKzQq())).append("\r\n");
        }
        if (StringUtil.isNotEmpty(info.getKzWw())) {
            sb.append("旺旺：").append(StringUtil.nullToStrTrim(info.getKzWw())).append("\r\n");
        }
        sb.append("渠道：").append(StringUtil.nullToStrTrim(info.getChannelName())).append("\r\n");
        sb.append("来源：").append(StringUtil.nullToStrTrim(info.getSourceName())).append("\r\n");
        sb.append("接待门店：").append(StringUtil.nullToStrTrim(info.getShopName())).append("\r\n");
        sb.append("接待门市：").append(StringUtil.nullToStrTrim(info.getReceptorName())).append("\r\n");
        sb.append("成交套系： ¥").append(info.getAmount()).append("\r\n");
        sb.append("订单时间：").append(TimeUtil.intMillisToTimeStr(info.getSuccessTime()));
        String remarkText = sb.toString();
        String companyName = staffDao.getCompanyNameByStaffId(staffId);

        WeChatPushUtil.pushMsg(new WeChatPushMsgDTO(firstText,remarkText,companyId,companyName,staffId,StringUtil.nullToStrTrim(info.getKzName()),StringUtil.nullToStrTrim(info.getKzPhone()),new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date())));
    }

    /**
     * 连续三次怠工自动下线
     *
     * @param companyId
     * @param staffId
     * @param newsDao
     */
    public static void pushOffLineAuto(int companyId, int staffId, NewsDao newsDao, StaffDao staffDao) {
        String head = ClientLogConst.CONTINUOUS_SABOTEUR_DONW;
        String msg = "连续三次怠工被系统自动下线<br/>请重新上线或重新登录系统";
        pushError(companyId, staffId, head, msg);
        newsDao.insert(new NewsPO(MessageConts.MSG_TYPE_SYSTEM, head, msg.replaceAll("<br/>", "；"), "", staffId,
                companyId));
        DingMsgSendUtil.sendDingMsg(head + "<br/>", companyId, staffId, staffDao);
    }

    /**
     * 到店订单通知
     *
     * @param companyId
     * @param staffId
     * @param info
     * @param successTime
     * @param amount
     * @param shopName
     * @param receptorName
     */
    public static void pushSuccessShop(int companyId, int staffId, ClientDTO info, String successTime, String amount,
                                       String shopName, String receptorName, StaffDao staffDao) {
        StringBuffer sb = new StringBuffer();
        sb.append("恭喜您，您的客户在 ");
        sb.append(StringUtil.nullToStrTrim(shopName));
        sb.append(" 订单啦");
        String header = sb.toString();

        sb.delete(0, sb.length());
        sb.append("<br/><br/>姓名：");
        sb.append(StringUtil.nullToStrTrim(info.getKzName()));
        sb.append("<br/>手机：").append(StringUtil.nullToStrTrim(info.getKzPhone()));
        sb.append("<br/>微信：").append(StringUtil.nullToStrTrim(info.getKzWeChat()));
        sb.append("<br/>渠道：").append(StringUtil.nullToStrTrim(info.getChannelSource()));
        sb.append("<br/><br/>接待门店：").append(StringUtil.nullToStrTrim(shopName));
        sb.append("<br/>接待门市：").append(StringUtil.nullToStrTrim(receptorName));
        sb.append("<br/><br/>成交套系： ¥").append(amount);
        sb.append("<br/>订单时间：").append(StringUtil.nullToStrTrim(successTime));

        String msg = sb.toString();
        pushSuccess(companyId, staffId, header, msg);
        DingMsgSendUtil.sendDingMsg(header + "<br/>" + sb.toString(), companyId, staffId, staffDao);

        //微信消息推送
        String firstText = "恭喜您，您的客户在 "+StringUtil.nullToStrTrim(shopName)+" 订单啦";
        sb = new StringBuffer();

            sb.append("微信：").append(StringUtil.nullToStrTrim(info.getKzWeChat())).append("\r\n");

        sb.append("渠道：").append(StringUtil.nullToStrTrim(info.getChannelName())).append("\r\n");
        sb.append("接待门店：").append(StringUtil.nullToStrTrim(shopName)).append("\r\n");
        sb.append("接待门市：").append(StringUtil.nullToStrTrim(receptorName)).append("\r\n");
        sb.append("成交套系： ¥").append(amount).append("\r\n");
        sb.append("订单时间：").append(StringUtil.nullToStrTrim(successTime));
        String remarkText = sb.toString();
        String companyName = staffDao.getCompanyNameByStaffId(staffId);

        WeChatPushUtil.pushMsg(new WeChatPushMsgDTO(firstText,remarkText,companyId,companyName,staffId,StringUtil.nullToStrTrim(info.getKzName()),StringUtil.nullToStrTrim(info.getKzPhone()),new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date())));
    }

    /**
     * 电商推广需要审批无效客资提醒
     *
     * @param companyId
     * @param staffId
     * @param info
     * @param invalidReason
     */
    public static void pushBeValidCheck(int companyId, int staffId, ClientDTO info, String invalidReason, StaffDao staffDao) {

        StringBuffer sb = new StringBuffer();
        sb.append("无效待审批 - ");
        sb.append(invalidReason);
        String header = sb.toString();

        sb.delete(0, sb.length());
        sb.append("<br/><br/>姓名：");
        sb.append(StringUtil.nullToStrTrim(info.getKzName()));
        sb.append("<br/>手机：").append(StringUtil.nullToStrTrim(info.getKzPhone()));
        sb.append("<br/>微信：").append(StringUtil.nullToStrTrim(info.getKzWeChat()));
        sb.append("<br/><br/>渠道：").append(StringUtil.nullToStrTrim(info.getChannelSource()));

        String msg = sb.toString();
        pushWarn(companyId, staffId, header, msg);
        DingMsgSendUtil.sendDingMsg(header + "<br/>" + sb.toString(), companyId, staffId, staffDao);

        //微信消息推送
        String firstText = "无效待审批 - "+invalidReason;
        sb = new StringBuffer();

        sb.append("微信：").append(StringUtil.nullToStrTrim(info.getKzWeChat())).append("\r\n");

        sb.append("渠道：").append(StringUtil.nullToStrTrim(info.getChannelName())).append("\r\n");
        String remarkText = sb.toString();
        String companyName = staffDao.getCompanyNameByStaffId(staffId);

        WeChatPushUtil.pushMsg(new WeChatPushMsgDTO(firstText,remarkText,companyId,companyName,staffId,StringUtil.nullToStrTrim(info.getKzName()),StringUtil.nullToStrTrim(info.getKzPhone()),new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date())));
    }

    /**
     * 邀约无效驳回消息
     *
     * @param companyId
     * @param staffId
     * @param info
     * @param newsDao
     */
    public static void pushYyValidReject(int companyId, int staffId, ClientGoEasyDTO info, NewsDao newsDao, StaffDao staffDao) {
        String head = "您录入的客资被判为无效";
        StringBuffer sb = new StringBuffer();
        sb.append("编号：").append(info.getLetterId()).append("<br/>");
        if (StringUtil.isNotEmpty(info.getKzName())) {
            sb.append("姓名：").append(StringUtil.nullToStrTrim(info.getKzName())).append("<br/>");
        }
        if (StringUtil.isNotEmpty(info.getKzPhone())) {
            sb.append("电话：").append(StringUtil.nullToStrTrim(info.getKzPhone())).append("<br/>");
        }
        if (StringUtil.isNotEmpty(info.getKzWechat())) {
            sb.append("微信：").append(StringUtil.nullToStrTrim(info.getKzWechat())).append("<br/>");
        }
        if (StringUtil.isNotEmpty(info.getKzQq())) {
            sb.append("QQ：").append(StringUtil.nullToStrTrim(info.getKzQq())).append("<br/>");
        }
        if (StringUtil.isNotEmpty(info.getKzWw())) {
            sb.append("旺旺：").append(StringUtil.nullToStrTrim(info.getKzWw())).append("<br/>");
        }
        sb.append("渠道：").append(StringUtil.nullToStrTrim(info.getChannelName())).append("<br/>");
        sb.append("来源：").append(StringUtil.nullToStrTrim(info.getSourceName())).append("<br/>");
        sb.append("无效原因：").append(StringUtil.nullToStrTrim(info.getInvalidLabel()));
        String msg = sb.toString();
        pushWarn(companyId, staffId, head, msg);
        newsDao.insert(new NewsPO(MessageConts.MSG_TYPE_WARN, head, msg.replaceAll("<br/>", "；"), info.getKzId(),
                staffId, companyId));
        DingMsgSendUtil.sendDingMsg(head + "<br/>" + sb.toString(), companyId, staffId, staffDao);

        //微信消息推送
        String firstText = "您录入的客资被判为无效";
        sb = new StringBuffer();
        sb.append("编号：").append(info.getLetterId()).append("\r\n");
        if (StringUtil.isNotEmpty(info.getKzWechat())) {
            sb.append("微信：").append(StringUtil.nullToStrTrim(info.getKzWechat())).append("\r\n");
        }
        if (StringUtil.isNotEmpty(info.getKzQq())) {
            sb.append("QQ：").append(StringUtil.nullToStrTrim(info.getKzQq())).append("\r\n");
        }
        if (StringUtil.isNotEmpty(info.getKzWw())) {
            sb.append("旺旺：").append(StringUtil.nullToStrTrim(info.getKzWw())).append("\r\n");
        }
        sb.append("渠道：").append(StringUtil.nullToStrTrim(info.getChannelName())).append("\r\n");
        sb.append("来源：").append(StringUtil.nullToStrTrim(info.getSourceName())).append("\r\n");
        sb.append("无效原因：").append(StringUtil.nullToStrTrim(info.getInvalidLabel()));
        String remarkText = sb.toString();

        String companyName = staffDao.getCompanyNameByStaffId(staffId);

        WeChatPushUtil.pushMsg(new WeChatPushMsgDTO(firstText,remarkText,companyId,companyName,staffId,StringUtil.nullToStrTrim(info.getKzName()),StringUtil.nullToStrTrim(info.getKzPhone()),new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date())));
    }

    /**
     * 新分配的客资消息
     *
     * @param companyId
     * @param staffId
     * @param info
     * @param newsDao
     */
    public static void pushInfoComed(int companyId, int staffId, ClientGoEasyDTO info, NewsDao newsDao, StaffDao staffDao) {
        String head = "新客资来啦^_^";
        StringBuffer sb = new StringBuffer();
        sb.append("编号：").append(info.getLetterId()).append("<br/>");
        if (StringUtil.isNotEmpty(info.getKzName())) {
            sb.append("姓名：").append(StringUtil.nullToStrTrim(info.getKzName())).append("<br/>");
        }
        if (StringUtil.isNotEmpty(info.getKzPhone())) {
            sb.append("电话：").append(StringUtil.nullToStrTrim(info.getKzPhone())).append("<br/>");
        }
        if (StringUtil.isNotEmpty(info.getKzWechat())) {
            sb.append("微信：").append(StringUtil.nullToStrTrim(info.getKzWechat())).append("<br/>");
        }
        if (StringUtil.isNotEmpty(info.getKzQq())) {
            sb.append("QQ：").append(StringUtil.nullToStrTrim(info.getKzQq())).append("<br/>");
        }
        if (StringUtil.isNotEmpty(info.getKzWw())) {
            sb.append("旺旺：").append(StringUtil.nullToStrTrim(info.getKzWw())).append("<br/>");
        }
        sb.append("<br/>渠道：").append(StringUtil.nullToStrTrim(info.getChannelName())).append("<br/>");
        sb.append("来源：").append(StringUtil.nullToStrTrim(info.getSourceName())).append("<br/><br/>");
        sb.append("提报人：").append(StringUtil.nullToStrTrim(info.getCollectorName())).append("<br/><br/>");
        if (StringUtil.isNotEmpty(info.getMemo())) {
            String memo = StringUtil.replaceAllHTML(info.getMemo());
            if (memo.length() >= 30) {
                memo = memo.substring(0, 30);
            }
            sb.append("备注：").append(memo);
        }
        String msg = sb.toString();
        pushCommon(companyId, staffId, head, msg);
        newsDao.insert(new NewsPO(MessageConts.MSG_TYPE_COMMON, head, msg.replaceAll("<br/>", "；"), info.getKzId(),
                staffId, companyId));

        DingMsgSendUtil.sendDingMsg(head + "<br/>" + sb.toString(), companyId, staffId, staffDao);

        //微信消息推送
        String firstText = "新客资来啦^_^";
        sb = new StringBuffer();
        sb.append("编号：").append(info.getLetterId()).append("\r\n");
        if (StringUtil.isNotEmpty(info.getKzWechat())) {
            sb.append("微信：").append(StringUtil.nullToStrTrim(info.getKzWechat())).append("\r\n");
        }
        if (StringUtil.isNotEmpty(info.getKzQq())) {
            sb.append("QQ：").append(StringUtil.nullToStrTrim(info.getKzQq())).append("\r\n");
        }
        if (StringUtil.isNotEmpty(info.getKzWw())) {
            sb.append("旺旺：").append(StringUtil.nullToStrTrim(info.getKzWw())).append("\r\n");
        }
        sb.append("渠道：").append(StringUtil.nullToStrTrim(info.getChannelName())).append("\r\n");
        sb.append("来源：").append(StringUtil.nullToStrTrim(info.getSourceName())).append("\r\n");
        sb.append("无效原因：").append(StringUtil.nullToStrTrim(info.getInvalidLabel()));
        String remarkText = sb.toString();

        String companyName = staffDao.getCompanyNameByStaffId(staffId);

        WeChatPushUtil.pushMsg(new WeChatPushMsgDTO(firstText,remarkText,companyId,companyName,staffId,StringUtil.nullToStrTrim(info.getKzName()),StringUtil.nullToStrTrim(info.getKzPhone()),new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date())));

    }

    /**
     * 推送定时提醒消息
     *
     * @param companyId
     * @param staffId
     * @param kzId
     * @param msg
     */
    public static void pushWarnTimer(int companyId, int staffId, String kzId, String msg, StaffDao staffDao) {
        pushCommon(companyId, staffId, MessageConts.TO_BE_TRACKED_HEAD, msg);
        DingMsgSendUtil.sendDingMsg(MessageConts.TO_BE_TRACKED_HEAD + "\n" + msg, companyId, staffId, staffDao);

        //微信消息推送
        String firstText = "推送定时提醒消息";
        String remarkText = MessageConts.TO_BE_TRACKED_HEAD + "\r\n" + msg;

        String companyName = staffDao.getCompanyNameByStaffId(staffId);

        WeChatPushUtil.pushMsg(new WeChatPushMsgDTO(firstText,remarkText,companyId,companyName,staffId,"","",new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date())));
    }

    /**
     * 推广备注被修改，推送给客服
     *
     * @param companyId
     * @param staffId
     * @param msg
     * @param kzId
     * @param newsDao
     */
    public static void pushRemark(int companyId, int staffId, String msg, String kzId, NewsDao newsDao, StaffDao staffDao) {
        if (NumUtil.isNull(staffId) || NumUtil.isNull(companyId)) {
            return;
        }
        String head = "推广备注被修改";
        pushWarn(companyId, staffId, head, msg);
        newsDao.insert(new NewsPO(MessageConts.MSG_TYPE_WARN, head, msg, kzId, staffId, companyId));
        DingMsgSendUtil.sendDingMsg(head + "\n" + msg, companyId, staffId, staffDao);

        //微信消息推送
        String firstText = "推广备注被修改";
        String remarkText = head + "\n" + msg;

        String companyName = staffDao.getCompanyNameByStaffId(staffId);

        WeChatPushUtil.pushMsg(new WeChatPushMsgDTO(firstText,remarkText,companyId,companyName,staffId,"","",new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date())));
    }

    /**
     * 客资转移消息
     *
     * @param staffPO
     * @param toStaffId
     * @param kzIds
     * @param newsDao
     * @param clientInfoDao
     */

    public static void pushTransfer(StaffPO staffPO, int toStaffId, String kzIds, NewsDao newsDao,
                                    ClientInfoDao clientInfoDao, StaffDao staffDao) {
        if (StringUtil.isEmpty(kzIds)) {
            return;
        }
        String[] kzArr = kzIds.split(CommonConstant.STR_SEPARATOR);
        String head = staffPO.getNickName() + " 转移给您" + kzArr.length + "个客资";
        StringBuffer sb = new StringBuffer();
        if (kzArr.length == 1) {
            ClientGoEasyDTO info = clientInfoDao.getClientGoEasyDTOById(kzArr[0],
                    DBSplitUtil.getInfoTabName(staffPO.getCompanyId()),
                    DBSplitUtil.getDetailTabName(staffPO.getCompanyId()));
            sb.append("编号：").append(info.getLetterId()).append("<br/>");
            if (StringUtil.isNotEmpty(info.getKzName())) {
                sb.append("姓名：").append(StringUtil.nullToStrTrim(info.getKzName())).append("<br/>");
            }
            if (StringUtil.isNotEmpty(info.getKzPhone())) {
                sb.append("电话：").append(StringUtil.nullToStrTrim(info.getKzPhone())).append("<br/>");
            }
            if (StringUtil.isNotEmpty(info.getKzWechat())) {
                sb.append("微信：").append(StringUtil.nullToStrTrim(info.getKzWechat())).append("<br/>");
            }
            if (StringUtil.isNotEmpty(info.getKzQq())) {
                sb.append("QQ：").append(StringUtil.nullToStrTrim(info.getKzQq())).append("<br/>");
            }
            if (StringUtil.isNotEmpty(info.getKzWw())) {
                sb.append("旺旺：").append(StringUtil.nullToStrTrim(info.getKzWw())).append("<br/>");
            }
            sb.append("<br/>渠道：").append(StringUtil.nullToStrTrim(info.getChannelName())).append("<br/>");
            sb.append("来源：").append(StringUtil.nullToStrTrim(info.getSourceName())).append("<br/><br/>");

            pushCommon(staffPO.getCompanyId(), toStaffId, head, sb.toString());
            newsDao.insert(new NewsPO(MessageConts.MSG_TYPE_COMMON, head, sb.toString().replaceAll("<br/>", "；"),
                    info.getKzId(), toStaffId, staffPO.getCompanyId()));
            DingMsgSendUtil.sendDingMsg(head + "<br/>" + sb.toString(), staffPO.getCompanyId(), toStaffId, staffDao);

            //微信消息推送
            sb = new StringBuffer();
            sb.append("编号：").append(info.getLetterId()).append("\r\n");
            if (StringUtil.isNotEmpty(info.getKzWechat())) {
                sb.append("微信：").append(StringUtil.nullToStrTrim(info.getKzWechat())).append("\r\n");
            }
            if (StringUtil.isNotEmpty(info.getKzQq())) {
                sb.append("QQ：").append(StringUtil.nullToStrTrim(info.getKzQq())).append("\r\n");
            }
            if (StringUtil.isNotEmpty(info.getKzWw())) {
                sb.append("旺旺：").append(StringUtil.nullToStrTrim(info.getKzWw())).append("\r\n");
            }
            sb.append("渠道：").append(StringUtil.nullToStrTrim(info.getChannelName())).append("\r\n");
            sb.append("来源：").append(StringUtil.nullToStrTrim(info.getSourceName())).append("\r\n");
            String remarkText = sb.toString();

            String companyName = staffDao.getCompanyNameByStaffId(staffPO.getId());

            WeChatPushUtil.pushMsg(new WeChatPushMsgDTO(head,remarkText,staffPO.getCompanyId(),companyName,toStaffId,StringUtil.nullToStrTrim(info.getKzName()),StringUtil.nullToStrTrim(info.getKzPhone()),new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date())));
        } else {
            pushCommon(staffPO.getCompanyId(), toStaffId, head, "");
            newsDao.insert(new NewsPO(MessageConts.MSG_TYPE_COMMON, head, "", null, toStaffId, staffPO.getCompanyId()));
            DingMsgSendUtil.sendDingMsg(head, staffPO.getCompanyId(), toStaffId, staffDao);

            String companyName = staffDao.getCompanyNameByStaffId(staffPO.getId());
            WeChatPushUtil.pushMsg(new WeChatPushMsgDTO(head,"",staffPO.getCompanyId(),companyName,toStaffId,"多个客资","多个客资",new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date())));
        }
    }

    /**
     * 账号异地登录提醒
     *
     * @param companyId
     * @param staffId
     * @param ip
     * @param newsDao
     */
    public static void pushRemoteLogin(int companyId, int staffId, String ip, String address, NewsDao newsDao) {
        if (NumUtil.isNull(staffId) || NumUtil.isNull(companyId)) {
            return;
        }
        String head = "账号异常";
        String msg = "您的账号在" + address + "登录<br/>IP：" + ip + "<br/>如非本人操作，请及时修改密码";
        pushWarn(companyId, staffId, head, msg);
        newsDao.insert(new NewsPO(MessageConts.MSG_TYPE_SYSTEM, head, msg.replaceAll("<br/>", "；"), null, staffId, companyId));
    }

    /**
     * 推送客资被删除消息
     *
     * @param companyId
     * @param staffId
     * @param info
     * @param type
     * @param newsDao
     */
    public static void pushRemove(int companyId, int staffId, ClientGoEasyDTO info, int num, String type, String operaName, NewsDao newsDao, StaffDao staffDao) {
        if (NumUtil.isNull(staffId) || NumUtil.isNull(companyId)) {
            return;
        }
        String head = "删除提醒";
        StringBuffer sb = new StringBuffer();
        if (num == 1) {
            sb.append("您好，您有1个" + type + "的客资被 " + operaName + " 删除 <br/>");
            sb.append("编号：").append(info.getLetterId()).append("<br/>");
            if (StringUtil.isNotEmpty(info.getKzName())) {
                sb.append("姓名：").append(StringUtil.nullToStrTrim(info.getKzName())).append("<br/>");
            }
            if (StringUtil.isNotEmpty(info.getKzPhone())) {
                sb.append("电话：").append(StringUtil.nullToStrTrim(info.getKzPhone())).append("<br/>");
            }
            if (StringUtil.isNotEmpty(info.getKzWechat())) {
                sb.append("微信：").append(StringUtil.nullToStrTrim(info.getKzWechat())).append("<br/>");
            }
            if (StringUtil.isNotEmpty(info.getKzQq())) {
                sb.append("QQ：").append(StringUtil.nullToStrTrim(info.getKzQq())).append("<br/>");
            }
            if (StringUtil.isNotEmpty(info.getKzWw())) {
                sb.append("旺旺：").append(StringUtil.nullToStrTrim(info.getKzWw())).append("<br/>");
            }
            sb.append("渠道：").append(StringUtil.nullToStrTrim(info.getChannelName())).append("<br/>");
            sb.append("来源：").append(StringUtil.nullToStrTrim(info.getSourceName())).append("<br/><br/>");

            //微信消息推送
            String firstText = "删除提醒\r\n您好，您有1个" + type + "的客资被 " + operaName + " 删除";
            StringBuffer dsb = new StringBuffer();
            dsb.append("编号：").append(info.getLetterId()).append("\r\n");
            if (StringUtil.isNotEmpty(info.getKzWechat())) {
                dsb.append("微信：").append(StringUtil.nullToStrTrim(info.getKzWechat())).append("\r\n");
            }
            if (StringUtil.isNotEmpty(info.getKzQq())) {
                dsb.append("QQ：").append(StringUtil.nullToStrTrim(info.getKzQq())).append("\r\n");
            }
            if (StringUtil.isNotEmpty(info.getKzWw())) {
                dsb.append("旺旺：").append(StringUtil.nullToStrTrim(info.getKzWw())).append("\r\n");
            }
            dsb.append("渠道：").append(StringUtil.nullToStrTrim(info.getChannelName())).append("\r\n");
            dsb.append("来源：").append(StringUtil.nullToStrTrim(info.getSourceName())).append("\r\n");
            String remarkText = dsb.toString();

            String companyName = staffDao.getCompanyNameByStaffId(staffId);

            WeChatPushUtil.pushMsg(new WeChatPushMsgDTO(firstText,remarkText,companyId,companyName,staffId,StringUtil.nullToStrTrim(info.getKzName()),StringUtil.nullToStrTrim(info.getKzPhone()),new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date())));

        } else {
            sb.append("您好，您有" + num + "个" + type + "的客资被 " + operaName + " 删除 ");
            String companyName = staffDao.getCompanyNameByStaffId(staffId);

            //微信消息发送
            String firstText = "删除提醒\r\n您好，您有" + num + "个" + type + "的客资被 " + operaName + " 删除 ";
            WeChatPushUtil.pushMsg(new WeChatPushMsgDTO(firstText,"",companyId,companyName,staffId,"多个客资","多个客资",new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date())));
        }
        pushWarn(companyId, staffId, head, sb.toString());
        newsDao.insert(new NewsPO(MessageConts.MSG_TYPE_WARN, head, sb.toString().replaceAll("<br/>", "；"), null, staffId, companyId));
        DingMsgSendUtil.sendDingMsg(head + "<br/>" + sb.toString(), companyId, staffId, staffDao);
    }

    /**
     * 推送催一次崔二次闪讯
     *
     * @param companyId
     * @param staffId
     * @param info
     * @param operaId
     * @param operaName
     * @param msg
     * @param newsDao
     */
    public static void pushFlash(int companyId, int staffId, ClientGoEasyDTO info, int operaId, String operaName, String msg, NewsDao newsDao) {
        if (NumUtil.isNull(staffId) || NumUtil.isNull(companyId) || info == null) {
            return;
        }
        String head = "来自 " + operaName + " 的客资消息";
        StringBuffer sb = new StringBuffer();
        sb.append("编号：").append(info.getLetterId()).append("<br/>");
        if (StringUtil.isNotEmpty(info.getKzName())) {
            sb.append("姓名：").append(StringUtil.nullToStrTrim(info.getKzName())).append("<br/>");
        }
        if (StringUtil.isNotEmpty(info.getKzPhone())) {
            sb.append("电话：").append(StringUtil.nullToStrTrim(info.getKzPhone())).append("<br/>");
        }
        if (StringUtil.isNotEmpty(info.getKzWechat())) {
            sb.append("微信：").append(StringUtil.nullToStrTrim(info.getKzWechat())).append("<br/>");
        }
        if (StringUtil.isNotEmpty(info.getKzQq())) {
            sb.append("QQ：").append(StringUtil.nullToStrTrim(info.getKzQq())).append("<br/>");
        }
        if (StringUtil.isNotEmpty(info.getKzWw())) {
            sb.append("旺旺：").append(StringUtil.nullToStrTrim(info.getKzWw())).append("<br/>");
        }
        sb.append("渠道：").append(StringUtil.nullToStrTrim(info.getChannelName())).append("<br/>");
        sb.append("来源：").append(StringUtil.nullToStrTrim(info.getSourceName())).append("<br/>");
        sb.append("录入时间：" + TimeUtil.intMillisToTimeStr(info.getCreateTime()) + "<br/>");
        sb.append("消息：" + msg + "<br/>");
        pushFlashMsgForInfo(companyId, staffId, operaId, operaName, StringUtil.nullToStrTrim(info.getKzName()),
                StringUtil.nullToStrTrim(StringUtil.isNotEmpty(info.getKzPhone()) ? info.getKzPhone() : StringUtil.isNotEmpty(info.getKzWechat()) ? info.getKzWechat() : info.getKzQq()),
                info.getLetterId(),
                info.getKzId(), StringUtil.nullToStrTrim(info.getChannelName()), StringUtil.nullToStrTrim(info.getSourceName()), StringUtil.nullToStrTrim(info.getMemo()), msg, TimeUtil.intMillisToTimeStr(info.getCreateTime()));
        newsDao.insert(new NewsPO(MessageConts.MSG_TYPE_FLASH, head, sb.toString().replaceAll("<br/>", "；"), info.getKzId(), staffId, companyId));
    }


    /**
     * 推送无效客资提醒
     *
     * @param companyId
     * @param staffId
     * @param info
     * @param invalidReason
     */
    public static void pushInvalidKz(int companyId, int staffId, ClientDTO info, String invalidReason, NewsDao newsDao, StaffDao staffDao) {
        if (NumUtil.isNull(staffId) || NumUtil.isNull(companyId) || info == null) {
            return;
        }
        StringBuffer sb = new StringBuffer();
        sb.append("无效待审批 ");
        String head = sb.toString();

        sb.delete(0, sb.length());
        sb.append("<br/>无效原因：").append(invalidReason);

        if (StringUtil.isNotEmpty(info.getKzName())) {
            sb.append("姓名：").append(StringUtil.nullToStrTrim(info.getKzName())).append("<br/>");
        }
        if (StringUtil.isNotEmpty(info.getKzPhone())) {
            sb.append("电话：").append(StringUtil.nullToStrTrim(info.getKzPhone())).append("<br/>");
        }
        if (StringUtil.isNotEmpty(info.getKzWeChat())) {
            sb.append("微信：").append(StringUtil.nullToStrTrim(info.getKzWeChat())).append("<br/>");
        }
        if (StringUtil.isNotEmpty(info.getKzQq())) {
            sb.append("QQ：").append(StringUtil.nullToStrTrim(info.getKzQq())).append("<br/>");
        }
        sb.append("渠道：").append(StringUtil.nullToStrTrim(info.getChannelName())).append("<br/>");
        sb.append("来源：").append(StringUtil.nullToStrTrim(info.getSrcName())).append("<br/>");

        String msg = sb.toString();
        pushWarn(companyId, staffId, head, msg);
        newsDao.insert(new NewsPO(MessageConts.MSG_TYPE_WARN, head, sb.toString().replaceAll("<br/>", "；"), info.getKzId(), staffId, companyId));
        DingMsgSendUtil.sendDingMsg(head + "<br/>" + sb.toString(), companyId, staffId, staffDao);

        //微信消息推送
        String firstText = "无效待审批\r\n无效原因："+invalidReason;
        sb = new StringBuffer();
        if (StringUtil.isNotEmpty(info.getKzWeChat())) {
            sb.append("微信：").append(StringUtil.nullToStrTrim(info.getKzWeChat())).append("\r\n");
        }
        if (StringUtil.isNotEmpty(info.getKzQq())) {
            sb.append("QQ：").append(StringUtil.nullToStrTrim(info.getKzQq())).append("\r\n");
        }
        sb.append("渠道：").append(StringUtil.nullToStrTrim(info.getChannelName())).append("\r\n");
        sb.append("来源：").append(StringUtil.nullToStrTrim(info.getSrcName())).append("\r\n");
        String remarkText = sb.toString();

        String companyName = staffDao.getCompanyNameByStaffId(staffId);

        WeChatPushUtil.pushMsg(new WeChatPushMsgDTO(firstText,remarkText,companyId,companyName,staffId,StringUtil.nullToStrTrim(info.getKzName()),StringUtil.nullToStrTrim(info.getKzPhone()),new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date())));

    }


    /**
     * 金数据录入客资消息
     */
    public static void pushGoldDataKz(int companyId, int staffId, ClientDTO info, NewsDao newsDao, StaffDao staffDao) {
        if (NumUtil.isNull(staffId) || NumUtil.isNull(companyId) || info == null) {
            return;
        }
        StringBuffer sb = new StringBuffer();
        sb.append("金数据客资录入成功");
        String head = sb.toString();
        sb.delete(0, sb.length());

        if (StringUtil.isNotEmpty(info.getKzName())) {
            sb.append("姓名：").append(StringUtil.nullToStrTrim(info.getKzName())).append("<br/>");
        }
        if (StringUtil.isNotEmpty(info.getKzPhone())) {
            sb.append("电话：").append(StringUtil.nullToStrTrim(info.getKzPhone())).append("<br/>");
        }
        if (StringUtil.isNotEmpty(info.getKzWeChat())) {
            sb.append("微信：").append(StringUtil.nullToStrTrim(info.getKzWeChat())).append("<br/>");
        }
        if (StringUtil.isNotEmpty(info.getKzQq())) {
            sb.append("QQ：").append(StringUtil.nullToStrTrim(info.getKzQq())).append("<br/>");
        }

        sb.append("渠道：").append(StringUtil.nullToStrTrim(info.getChannelName())).append("<br/>");
        sb.append("来源：").append(StringUtil.nullToStrTrim(info.getSrcName())).append("<br/>");

        pushSuccess(companyId, staffId, head, sb.toString());
        newsDao.insert(new NewsPO(MessageConts.MSG_TYPE_SUCCESS, head, sb.toString().replaceAll("<br/>", "；"), info.getKzId(), staffId, companyId));
        DingMsgSendUtil.sendDingMsg(head + "<br/>" + sb.toString(), companyId, staffId, staffDao);

        //微信消息推送
        String firstText = "金数据客资录入成功";
        sb.delete(0,sb.length());
        if (StringUtil.isNotEmpty(info.getKzWeChat())) {
            sb.append("微信：").append(StringUtil.nullToStrTrim(info.getKzWeChat())).append("\r\n");
        }
        if (StringUtil.isNotEmpty(info.getKzQq())) {
            sb.append("QQ：").append(StringUtil.nullToStrTrim(info.getKzQq())).append("\r\n");
        }
        sb.append("渠道：").append(StringUtil.nullToStrTrim(info.getChannelName())).append("\r\n");
        sb.append("来源：").append(StringUtil.nullToStrTrim(info.getSrcName())).append("\r\n");
        String remarkText = sb.toString();

        String companyName = staffDao.getCompanyNameByStaffId(staffId);

        WeChatPushUtil.pushMsg(new WeChatPushMsgDTO(firstText,remarkText,companyId,companyName,staffId,StringUtil.nullToStrTrim(info.getKzName()),StringUtil.nullToStrTrim(info.getKzPhone()),new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date())));

    }


    /**
     * 分配客资成功推送
     */
    public static void pushAllotMsg(int companyId, int staffId, String[] kzIdsArr, NewsDao newsDao, StaffDao staffDao, ClientInfoDao clientInfoDao) {
        if (NumUtil.isNull(staffId) || NumUtil.isNull(companyId)) {
            return;
        }
        StringBuffer sb = new StringBuffer();
        String head = "主管分配给您" + kzIdsArr.length + "个新的客资";
        if (kzIdsArr.length == 1) {
            ClientGoEasyDTO info = clientInfoDao.getClientGoEasyDTOById(kzIdsArr[0],
                    DBSplitUtil.getInfoTabName(companyId),
                    DBSplitUtil.getDetailTabName(companyId));
            sb.append("编号：").append(info.getLetterId()).append("<br/>");
            if (StringUtil.isNotEmpty(info.getKzName())) {
                sb.append("姓名：").append(StringUtil.nullToStrTrim(info.getKzName())).append("<br/>");
            }
            if (StringUtil.isNotEmpty(info.getKzPhone())) {
                sb.append("电话：").append(StringUtil.nullToStrTrim(info.getKzPhone())).append("<br/>");
            }
            if (StringUtil.isNotEmpty(info.getKzWechat())) {
                sb.append("微信：").append(StringUtil.nullToStrTrim(info.getKzWechat())).append("<br/>");
            }
            if (StringUtil.isNotEmpty(info.getKzQq())) {
                sb.append("QQ：").append(StringUtil.nullToStrTrim(info.getKzQq())).append("<br/>");
            }
            if (StringUtil.isNotEmpty(info.getKzWw())) {
                sb.append("旺旺：").append(StringUtil.nullToStrTrim(info.getKzWw())).append("<br/>");
            }
            sb.append("<br/>渠道：").append(StringUtil.nullToStrTrim(info.getChannelName())).append("<br/>");
            sb.append("来源：").append(StringUtil.nullToStrTrim(info.getSourceName())).append("<br/><br/>");
            pushSuccess(companyId, staffId, head, sb.toString());
            newsDao.insert(new NewsPO(MessageConts.MSG_TYPE_SUCCESS, head, sb.toString().replaceAll("<br/>", "；"), info.getKzId(), staffId, companyId));
            DingMsgSendUtil.sendDingMsg(head + "<br/>" + sb.toString(), companyId, staffId, staffDao);

			//微信消息推送
            sb = new StringBuffer();
            sb.append("编号：").append(info.getLetterId()).append("\r\n");
            if (StringUtil.isNotEmpty(info.getKzWechat())) {
                sb.append("微信：").append(StringUtil.nullToStrTrim(info.getKzWechat())).append("\r\n");
            }
            if (StringUtil.isNotEmpty(info.getKzQq())) {
                sb.append("QQ：").append(StringUtil.nullToStrTrim(info.getKzQq())).append("\r\n");
            }
            if (StringUtil.isNotEmpty(info.getKzWw())) {
                sb.append("旺旺：").append(StringUtil.nullToStrTrim(info.getKzWw())).append("\r\n");
            }
            sb.append("渠道：").append(StringUtil.nullToStrTrim(info.getChannelName())).append("\r\n");
            sb.append("来源：").append(StringUtil.nullToStrTrim(info.getSourceName())).append("\r\n");
            String remarkText = sb.toString();

            String companyName = staffDao.getCompanyNameByStaffId(staffId);

            WeChatPushUtil.pushMsg(new WeChatPushMsgDTO(head,remarkText,companyId,companyName,staffId,StringUtil.nullToStrTrim(info.getKzName()),StringUtil.nullToStrTrim(info.getKzPhone()),new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date())));

        } else {
            pushSuccess(companyId, staffId, head, sb.toString());
            newsDao.insert(new NewsPO(MessageConts.MSG_TYPE_SUCCESS, head, null, "", staffId, companyId));
            DingMsgSendUtil.sendDingMsg(head, companyId, staffId, staffDao);

            String companyName = staffDao.getCompanyNameByStaffId(staffId);
            WeChatPushUtil.pushMsg(new WeChatPushMsgDTO(head,"",companyId,companyName,staffId,"多个客资","多个客资",new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date())));

        }
    }

    /*-- 重复客资邀约员提醒消息 --*/
    public static void pushRepeatClient(int companyId, int staffId, ClientGoEasyDTO info, String operaName, NewsDao newsDao, StaffDao staffDao) {
        if (NumUtil.isNull(staffId) || NumUtil.isNull(companyId) || info == null) {
            return;
        }
        String head = "您的客资被" + StringUtil.nullToStrTrim(operaName) + "重复提报，<br/>请及时联系该客资";
        StringBuffer sb = new StringBuffer();
        sb.append("编号：").append(info.getLetterId()).append("<br/>");
        if (StringUtil.isNotEmpty(info.getKzName())) {
            sb.append("姓名：").append(StringUtil.nullToStrTrim(info.getKzName())).append("<br/>");
        }
        if (StringUtil.isNotEmpty(info.getKzPhone())) {
            sb.append("电话：").append(StringUtil.nullToStrTrim(info.getKzPhone())).append("<br/>");
        }
        if (StringUtil.isNotEmpty(info.getKzWechat())) {
            sb.append("微信：").append(StringUtil.nullToStrTrim(info.getKzWechat())).append("<br/>");
        }
        if (StringUtil.isNotEmpty(info.getKzQq())) {
            sb.append("QQ：").append(StringUtil.nullToStrTrim(info.getKzQq())).append("<br/>");
        }
        if (StringUtil.isNotEmpty(info.getKzWw())) {
            sb.append("旺旺：").append(StringUtil.nullToStrTrim(info.getKzWw())).append("<br/>");
        }
        sb.append("渠道：").append(StringUtil.nullToStrTrim(info.getChannelName())).append("<br/>");
        sb.append("来源：").append(StringUtil.nullToStrTrim(info.getSourceName())).append("<br/>");

        String msg = sb.toString();
        pushWarn(companyId, staffId, head, msg);
        newsDao.insert(new NewsPO(MessageConts.MSG_TYPE_WARN, head, sb.toString().replaceAll("<br/>", "；"), info.getKzId(), staffId, companyId));
        DingMsgSendUtil.sendDingMsg(head + "<br/>" + sb.toString(), companyId, staffId, staffDao);

        //微信消息推送
        sb = new StringBuffer();
        sb.append("编号：").append(info.getLetterId()).append("\r\n");
        if (StringUtil.isNotEmpty(info.getKzWechat())) {
            sb.append("微信：").append(StringUtil.nullToStrTrim(info.getKzWechat())).append("\r\n");
        }
        if (StringUtil.isNotEmpty(info.getKzQq())) {
            sb.append("QQ：").append(StringUtil.nullToStrTrim(info.getKzQq())).append("\r\n");
        }
        if (StringUtil.isNotEmpty(info.getKzWw())) {
            sb.append("旺旺：").append(StringUtil.nullToStrTrim(info.getKzWw())).append("\r\n");
        }
        sb.append("渠道：").append(StringUtil.nullToStrTrim(info.getChannelName())).append("\r\n");
        sb.append("来源：").append(StringUtil.nullToStrTrim(info.getSourceName())).append("\r\n");
        String remarkText = sb.toString();

        String companyName = staffDao.getCompanyNameByStaffId(staffId);
        WeChatPushUtil.pushMsg(new WeChatPushMsgDTO(head,remarkText,companyId,companyName,staffId,StringUtil.nullToStrTrim(info.getKzName()),StringUtil.nullToStrTrim(info.getKzPhone()),new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date())));

    }

    /*-- 客资领取消息 --*/
    public static void pushClientReceive(int companyId, int staffId, ClientGoEasyDTO info, NewsDao newsDao, StaffDao staffDao, WebSocketMsgUtil webSocketMsgUtil) {
        if (NumUtil.isNull(staffId) || NumUtil.isNull(companyId) || info == null) {
            return;
        }
        if (newsDao.getNewsCountByType(staffId, companyId, info.getKzId()) > 0) {
            return;
        }
        String head = "您有新的客资待领取";
        //goeasy消息
//        pushReceive(companyId, staffId, head, info);
        //使用原生领取客资
        webSocketMsgUtil.pushReceiveClient(companyId, staffId, head, info);
        StringBuffer sb = new StringBuffer();
        sb.append(head).append("<br/>");
        sb.append("编号：").append(info.getLetterId()).append("<br/>");
        if (StringUtil.isNotEmpty(info.getKzName())) {
            sb.append("姓名：").append(StringUtil.nullToStrTrim(info.getKzName())).append("<br/>");
        }
        if (StringUtil.isNotEmpty(info.getKzPhone())) {
            sb.append("电话：").append(StringUtil.nullToStrTrim(info.getKzPhone())).append("<br/>");
        }
        if (StringUtil.isNotEmpty(info.getKzWechat())) {
            sb.append("微信：").append(StringUtil.nullToStrTrim(info.getKzWechat())).append("<br/>");
        }
        if (StringUtil.isNotEmpty(info.getKzWw())) {
            sb.append("旺旺：").append(StringUtil.nullToStrTrim(info.getKzWw())).append("<br/>");
        }
        sb.append("渠道：").append(StringUtil.nullToStrTrim(info.getChannelName())).append("<br/>");
        sb.append("来源：").append(StringUtil.nullToStrTrim(info.getSourceName())).append("<br/>");
        DingMsgSendUtil.sendDingMsg(sb.toString(), companyId, staffId, staffDao);
        newsDao.insert(new NewsPO(MessageConts.MSG_TYPE_RECEIVE, head, null, info.getKzId(), staffId, companyId));

        //微信消息推送
        sb = new StringBuffer();
        sb.append("编号：").append(info.getLetterId()).append("\r\n");
        if (StringUtil.isNotEmpty(info.getKzWechat())) {
            sb.append("微信：").append(StringUtil.nullToStrTrim(info.getKzWechat())).append("\r\n");
        }
        if (StringUtil.isNotEmpty(info.getKzWw())) {
            sb.append("旺旺：").append(StringUtil.nullToStrTrim(info.getKzWw())).append("\r\n");
        }
        sb.append("渠道：").append(StringUtil.nullToStrTrim(info.getChannelName())).append("\r\n");
        sb.append("来源：").append(StringUtil.nullToStrTrim(info.getSourceName())).append("\r\n");
        String remarkText = sb.toString();

        String companyName = staffDao.getCompanyNameByStaffId(staffId);

        WeChatPushUtil.pushMsg(new WeChatPushMsgDTO(head,remarkText,companyId,companyName,staffId,StringUtil.nullToStrTrim(info.getKzName()),StringUtil.nullToStrTrim(info.getKzPhone()),new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date())));

    }

    /**
     * 客资驳回消息
     */
    public static void pushReject(int companyId, int toStaffId, ClientGoEasyDTO info, NewsDao newsDao, StaffDao staffDao) {
        if (NumUtil.isInValid(toStaffId) || NumUtil.isInValid(companyId)) {
            return;
        }
        String head = "您有一个客资被无效驳回";
        StringBuffer sb = new StringBuffer();
        sb.append("编号：").append(info.getLetterId()).append("<br/>");
        if (StringUtil.isNotEmpty(info.getKzName())) {
            sb.append("姓名：").append(StringUtil.nullToStrTrim(info.getKzName())).append("<br/>");
        }
        if (StringUtil.isNotEmpty(info.getKzPhone())) {
            sb.append("电话：").append(StringUtil.nullToStrTrim(info.getKzPhone())).append("<br/>");
        }
        if (StringUtil.isNotEmpty(info.getKzWechat())) {
            sb.append("微信：").append(StringUtil.nullToStrTrim(info.getKzWechat())).append("<br/>");
        }
        if (StringUtil.isNotEmpty(info.getKzQq())) {
            sb.append("QQ：").append(StringUtil.nullToStrTrim(info.getKzQq())).append("<br/>");
        }
        if (StringUtil.isNotEmpty(info.getKzWw())) {
            sb.append("旺旺：").append(StringUtil.nullToStrTrim(info.getKzWw())).append("<br/>");
        }
        sb.append("<br/>渠道：").append(StringUtil.nullToStrTrim(info.getChannelName())).append("<br/>");
        sb.append("来源：").append(StringUtil.nullToStrTrim(info.getSourceName())).append("<br/><br/>");

        pushWarn(companyId, toStaffId, head, sb.toString());
        newsDao.insert(new NewsPO(MessageConts.MSG_TYPE_COMMON, head, sb.toString().replaceAll("<br/>", "；"),
                info.getKzId(), toStaffId, companyId));
        DingMsgSendUtil.sendDingMsg(head + "<br/>" + sb.toString(), companyId, toStaffId, staffDao);

        //微信消息推送
        sb = new StringBuffer();
        sb.append("编号：").append(info.getLetterId()).append("\r\n");
        if (StringUtil.isNotEmpty(info.getKzWechat())) {
            sb.append("微信：").append(StringUtil.nullToStrTrim(info.getKzWechat())).append("\r\n");
        }
        if (StringUtil.isNotEmpty(info.getKzQq())) {
            sb.append("QQ：").append(StringUtil.nullToStrTrim(info.getKzQq())).append("\r\n");
        }
        if (StringUtil.isNotEmpty(info.getKzWw())) {
            sb.append("旺旺：").append(StringUtil.nullToStrTrim(info.getKzWw())).append("\r\n");
        }
        sb.append("渠道：").append(StringUtil.nullToStrTrim(info.getChannelName())).append("\r\n");
        sb.append("来源：").append(StringUtil.nullToStrTrim(info.getSourceName())).append("\r\n");
        String remarkText = sb.toString();

        String companyName = staffDao.getCompanyNameByStaffId(toStaffId);

        WeChatPushUtil.pushMsg(new WeChatPushMsgDTO(head,remarkText,companyId,companyName,toStaffId,StringUtil.nullToStrTrim(info.getKzName()),StringUtil.nullToStrTrim(info.getKzPhone()),new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date())));

    }

    /**
     * 进店未定
     *
     * @param companyId
     * @param staffId
     * @param info
     * @param newsDao
     */
    public static void pushComeNotSuccess(int companyId, int staffId, ClientGoEasyDTO info, NewsDao newsDao, StaffDao staffDao) {
        String head = "您的客资进店未定";
        StringBuffer sb = new StringBuffer();
        sb.append("编号：").append(info.getLetterId()).append("<br/>");
        if (StringUtil.isNotEmpty(info.getKzName())) {
            sb.append("姓名：").append(StringUtil.nullToStrTrim(info.getKzName())).append("<br/>");
        }
        if (StringUtil.isNotEmpty(info.getKzPhone())) {
            sb.append("电话：").append(StringUtil.nullToStrTrim(info.getKzPhone())).append("<br/>");
        }
        if (StringUtil.isNotEmpty(info.getKzWechat())) {
            sb.append("微信：").append(StringUtil.nullToStrTrim(info.getKzWechat())).append("<br/>");
        }
        if (StringUtil.isNotEmpty(info.getKzWw())) {
            sb.append("旺旺：").append(StringUtil.nullToStrTrim(info.getKzWw())).append("<br/>");
        }
        sb.append("渠道：").append(StringUtil.nullToStrTrim(info.getChannelName())).append("<br/>");
        sb.append("来源：").append(StringUtil.nullToStrTrim(info.getSourceName())).append("<br/>");
        String msg = sb.toString();
        pushWarn(companyId, staffId, head, msg);
        newsDao.insert(new NewsPO(MessageConts.MSG_TYPE_WARN, head, msg.replaceAll("<br/>", "；"), info.getKzId(),
                staffId, companyId));
        DingMsgSendUtil.sendDingMsg(head + "<br/>" + sb.toString(), companyId, staffId, staffDao);

        //微信消息推送
        sb = new StringBuffer();
        sb.append("编号：").append(info.getLetterId()).append("\r\n");
        if (StringUtil.isNotEmpty(info.getKzWechat())) {
            sb.append("微信：").append(StringUtil.nullToStrTrim(info.getKzWechat())).append("\r\n");
        }
        if (StringUtil.isNotEmpty(info.getKzWw())) {
            sb.append("旺旺：").append(StringUtil.nullToStrTrim(info.getKzWw())).append("\r\n");
        }
        sb.append("渠道：").append(StringUtil.nullToStrTrim(info.getChannelName())).append("\r\n");
        sb.append("来源：").append(StringUtil.nullToStrTrim(info.getSourceName())).append("\r\n");
        String remarkText = sb.toString();

        String companyName = staffDao.getCompanyNameByStaffId(staffId);

        WeChatPushUtil.pushMsg(new WeChatPushMsgDTO(head,remarkText,companyId,companyName,staffId,StringUtil.nullToStrTrim(info.getKzName()),StringUtil.nullToStrTrim(info.getKzPhone()),new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date())));

    }

    /**
     * 预约到店
     *
     * @param companyId
     * @param staffId
     * @param info
     * @param newsDao
     */
    public static void pushAppointShop(int companyId, int staffId, ClientGoEasyDTO info, NewsDao newsDao, StaffDao staffDao) {
        String head = "您的客资预约到店了";
        StringBuffer sb = new StringBuffer();
        sb.append("编号：").append(info.getLetterId()).append("<br/>");
        if (StringUtil.isNotEmpty(info.getKzName())) {
            sb.append("姓名：").append(StringUtil.nullToStrTrim(info.getKzName())).append("<br/>");
        }
        if (StringUtil.isNotEmpty(info.getKzPhone())) {
            sb.append("电话：").append(StringUtil.nullToStrTrim(info.getKzPhone())).append("<br/>");
        }
        if (StringUtil.isNotEmpty(info.getKzWechat())) {
            sb.append("微信：").append(StringUtil.nullToStrTrim(info.getKzWechat())).append("<br/>");
        }
        if (StringUtil.isNotEmpty(info.getKzWw())) {
            sb.append("旺旺：").append(StringUtil.nullToStrTrim(info.getKzWw())).append("<br/>");
        }
        sb.append("渠道：").append(StringUtil.nullToStrTrim(info.getChannelName())).append("<br/>");
        sb.append("来源：").append(StringUtil.nullToStrTrim(info.getSourceName())).append("<br/>");
        sb.append("门店：").append(StringUtil.nullToStrTrim(info.getShopName())).append("<br/>");
        String msg = sb.toString();
        pushCommon(companyId, staffId, head, msg);
        newsDao.insert(new NewsPO(MessageConts.MSG_TYPE_WARN, head, msg.replaceAll("<br/>", "；"), info.getKzId(),
                staffId, companyId));
        DingMsgSendUtil.sendDingMsg(head + "<br/>" + sb.toString(), companyId, staffId, staffDao);

        //微信消息推送
        sb = new StringBuffer();
        sb.append("编号：").append(info.getLetterId()).append("\r\n");
        if (StringUtil.isNotEmpty(info.getKzWechat())) {
            sb.append("微信：").append(StringUtil.nullToStrTrim(info.getKzWechat())).append("\r\n");
        }
        if (StringUtil.isNotEmpty(info.getKzWw())) {
            sb.append("旺旺：").append(StringUtil.nullToStrTrim(info.getKzWw())).append("\r\n");
        }
        sb.append("渠道：").append(StringUtil.nullToStrTrim(info.getChannelName())).append("\r\n");
        sb.append("来源：").append(StringUtil.nullToStrTrim(info.getSourceName())).append("\r\n");
        sb.append("门店：").append(StringUtil.nullToStrTrim(info.getShopName())).append("\r\n");
        String remarkText = sb.toString();

        String companyName = staffDao.getCompanyNameByStaffId(staffId);

        WeChatPushUtil.pushMsg(new WeChatPushMsgDTO(head,remarkText,companyId,companyName,staffId,StringUtil.nullToStrTrim(info.getKzName()),StringUtil.nullToStrTrim(info.getKzPhone()),new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date())));

    }

}