package com.qiein.jupiter.msg.goeasy;

import com.qiein.jupiter.constant.CommonConstant;
import com.qiein.jupiter.util.*;
import com.qiein.jupiter.util.wechat.WeChatPushMsgDTO;
import com.qiein.jupiter.util.wechat.WeChatPushUtil;
import com.qiein.jupiter.web.dao.ClientInfoDao;
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

    @Value("${server.ip.address}")
    public void setServerAddress(String serverAddress) {
        GoEasyUtil.serverAddress = serverAddress;
    }

    /**
     * 初始化GoEasy实例
     */
    private static GoEasy goeasyInstance = null;

    /**
     * 消息内容体封装
     */
    private static JSONObject messageJson = null;
    private static JSONObject contentJson = null;

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
        messageJson = new JSONObject();
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
        messageJson = new JSONObject();
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
        contentJson = new JSONObject();
        contentJson.put("kzid", kzId);
        contentJson.put("logid", logId);
        contentJson.put("kznum", kzNum);
        contentJson.put("overtime", overTime);

        //TODO
        String url ="https://crm-jupiter.oss-cn-hangzhou.aliyuncs.com/wechat/index.html?kzId="+kzId+"&logId="+logId+"&uid="+staffId+"&cid="+companyId+"&url="+serverAddress;
        System.out.println(url);
        CompanyPO companyPO = companyService.getById(companyId);
        WeChatPushUtil.pushMsg(new WeChatPushMsgDTO(companyId,companyPO.getCompanyName(),staffId,url,"保密","保密",
                new SimpleDateFormat("YYYY-MM-DD HH:mm").format(overTime)));
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

        contentJson = new JSONObject();
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

        contentJson = new JSONObject();
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

        contentJson = new JSONObject();
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

        contentJson = new JSONObject();
        contentJson.put("head", head);
        contentJson.put("msg", msg);

        pushWeb(MessageConts.MSG_TYPE_ERROR, companyId, staffId, contentJson);
    }

    /**
     * 通知网页客资主动重载
     *
     * @param companyId
     * @param staffId
     */
    public static void pushInfoRefresh(int companyId, int staffId) {

        contentJson = new JSONObject();
        pushWeb(MessageConts.MSG_TYPE_INFO_REFRESH, companyId, staffId, contentJson);
    }

    /**
     * 通知网页主动重验证身份信息
     *
     * @param companyId
     * @param staffId
     */
    public static void pushStaffRefresh(int companyId, int staffId, String ip, String address) {

        contentJson = new JSONObject();
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
    public static void pushStatusRefresh(int companyId, int staffId) {

        contentJson = new JSONObject();
        pushWeb(MessageConts.MSG_TYPE_STATUS_REFRESH, companyId, staffId, contentJson);
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
    public static synchronized void pushFlashMsgForInfo(int companyId, int barStaffId, int senderId, String senderName,
                                                        String kzName, String kzPhone, String kzId, String kzChannel, String kzSource, String kzMemo, String msg) {

        contentJson = new JSONObject();
        contentJson.put("senderId", senderId);
        contentJson.put("senderName", senderName);
        contentJson.put("kzName", kzName);
        contentJson.put("kzPhone", kzPhone);
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

        contentJson = new JSONObject();
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
    public static void pushSuccessOnline(int companyId, int staffId, ClientGoEasyDTO info, NewsDao newsDao) {
        String head = "恭喜您，您的客户在线订单啦";
        StringBuffer sb = new StringBuffer();
        sb.append("编号：").append(info.getId()).append("<br/>");
        if (StringUtil.isNotEmpty(info.getKzName())) {
            sb.append("姓名：").append(StringUtil.nullToStrTrim(info.getKzName())).append("<br/>");
        }
        if (StringUtil.isNotEmpty(info.getKzPhone())) {
            sb.append("电话：").append(StringUtil.nullToStrTrim(info.getKzPhone())).append("<br/>");
        }
        if (StringUtil.isNotEmpty(info.getKzWechat())) {
            sb.append("微信：").append(StringUtil.nullToStrTrim(info.getKzWechat())).append("<br/>");
        }
        sb.append("渠道：").append(StringUtil.nullToStrTrim(info.getChannelName())).append("<br/>");
        sb.append("来源：").append(StringUtil.nullToStrTrim(info.getSourceName())).append("<br/>");
        sb.append("客服：").append(StringUtil.nullToStrTrim(info.getAppointName())).append("<br/>");
        sb.append("成交套系： ¥").append(info.getAmount()).append("<br/>");
        sb.append("订单时间：").append(TimeUtil.intMillisToTimeStr(info.getSuccessTime()));
        String msg = sb.toString();
        pushSuccess(companyId, staffId, head, msg);
        newsDao.insert(new NewsPO(MessageConts.MSG_TYPE_SUCCESS, head, msg.replaceAll("<br/>", "；"), info.getKzId(),
                staffId, companyId, DBSplitUtil.getNewsTabName(companyId)));
    }

    /**
     * 连续三次怠工自动下线
     *
     * @param companyId
     * @param staffId
     * @param newsDao
     */
    public static void pushOffLineAuto(int companyId, int staffId, NewsDao newsDao) {
        String head = ClientLogConst.CONTINUOUS_SABOTEUR_DONW;
        String msg = "连续三次怠工被系统自动下线<br/>请重新上线或重新登录系统";
        pushError(companyId, staffId, head, msg);
        newsDao.insert(new NewsPO(MessageConts.MSG_TYPE_SYSTEM, head, msg.replaceAll("<br/>", "；"), "", staffId,
                companyId, DBSplitUtil.getNewsTabName(companyId)));
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
                                       String shopName, String receptorName) {
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
    }

    /**
     * 电商推广需要审批无效客资提醒
     *
     * @param companyId
     * @param staffId
     * @param info
     * @param invalidReason
     */
    public static void pushBeValidCheck(int companyId, int staffId, ClientDTO info, String invalidReason) {

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
    }

    /**
     * 邀约无效驳回消息
     *
     * @param companyId
     * @param staffId
     * @param info
     * @param newsDao
     */
    public static void pushYyValidReject(int companyId, int staffId, ClientGoEasyDTO info, NewsDao newsDao) {
        String head = "您录入的客资被判为无效";
        StringBuffer sb = new StringBuffer();
        sb.append("编号：").append(info.getId()).append("<br/>");
        if (StringUtil.isNotEmpty(info.getKzName())) {
            sb.append("姓名：").append(StringUtil.nullToStrTrim(info.getKzName())).append("<br/>");
        }
        if (StringUtil.isNotEmpty(info.getKzPhone())) {
            sb.append("电话：").append(StringUtil.nullToStrTrim(info.getKzPhone())).append("<br/>");
        }
        if (StringUtil.isNotEmpty(info.getKzWechat())) {
            sb.append("微信：").append(StringUtil.nullToStrTrim(info.getKzWechat())).append("<br/>");
        }
        sb.append("渠道：").append(StringUtil.nullToStrTrim(info.getChannelName())).append("<br/>");
        sb.append("来源：").append(StringUtil.nullToStrTrim(info.getSourceName())).append("<br/>");
        sb.append("无效原因：").append(StringUtil.nullToStrTrim(info.getInvalidLabel()));
        String msg = sb.toString();
        pushWarn(companyId, staffId, head, msg);
        newsDao.insert(new NewsPO(MessageConts.MSG_TYPE_WARN, head, msg.replaceAll("<br/>", "；"), info.getKzId(),
                staffId, companyId, DBSplitUtil.getNewsTabName(companyId)));
    }

    /**
     * 新分配的客资消息
     *
     * @param companyId
     * @param staffId
     * @param info
     * @param newsDao
     */
    public static void pushInfoComed(int companyId, int staffId, ClientGoEasyDTO info, NewsDao newsDao) {
        String head = "新客资来啦^_^";
        StringBuffer sb = new StringBuffer();
        sb.append("编号：").append(info.getId()).append("<br/><br/>");
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
        sb.append("备注：").append(StringUtil.nullToStrTrim(info.getMemo()));
        String msg = sb.toString();
        pushCommon(companyId, staffId, head, msg);
        newsDao.insert(new NewsPO(MessageConts.MSG_TYPE_COMMON, head, msg.replaceAll("<br/>", "；"), info.getKzId(),
                staffId, companyId, DBSplitUtil.getNewsTabName(companyId)));
    }

    /**
     * 推送定时提醒消息
     *
     * @param companyId
     * @param staffId
     * @param kzId
     * @param msg
     */
    public static void pushWarnTimer(int companyId, int staffId, String kzId, String msg) {
        pushCommon(companyId, staffId, MessageConts.TO_BE_TRACKED_HEAD, msg);
    }

//    public static void main(String[] args) {
//        // ClientGoEasyDTO info = new ClientGoEasyDTO();
//        // info.setId(668);
//        // info.setKzWechat("xiangsiyu521");
//        // info.setSourceName("杭州微博");
//        // info.setChannelName("微博");
//        // info.setInvalidLabel("分手了");
//        // pushYyValidReject(1, 1, info, null);
//        pushStaffRefresh(2012, 698, "127.0.0.1", "火星");
//    }

    /**
     * 推广备注被修改，推送给客服
     *
     * @param companyId
     * @param staffId
     * @param msg
     * @param kzId
     * @param newsDao
     */
    public static void pushRemark(int companyId, int staffId, String msg, String kzId, NewsDao newsDao) {
        if (NumUtil.isNull(staffId) || NumUtil.isNull(companyId)) {
            return;
        }
        String head = "推广备注被修改";
        pushWarn(companyId, staffId, head, msg);
        newsDao.insert(new NewsPO(MessageConts.MSG_TYPE_WARN, head, msg, kzId, staffId, companyId,
                DBSplitUtil.getNewsTabName(companyId)));
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
                                    ClientInfoDao clientInfoDao) {
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
            sb.append("编号：").append(info.getId()).append("<br/><br/>");
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
                    info.getKzId(), toStaffId, staffPO.getCompanyId(),
                    DBSplitUtil.getNewsTabName(staffPO.getCompanyId())));
        } else {
            pushCommon(staffPO.getCompanyId(), toStaffId, head, "");
            newsDao.insert(new NewsPO(MessageConts.MSG_TYPE_COMMON, head, "", null, toStaffId, staffPO.getCompanyId(),
                    DBSplitUtil.getNewsTabName(staffPO.getCompanyId())));
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
        newsDao.insert(new NewsPO(MessageConts.MSG_TYPE_SYSTEM, head, msg.replaceAll("<br/>", "；"), null, staffId, companyId,
                DBSplitUtil.getNewsTabName(companyId)));
    }

}