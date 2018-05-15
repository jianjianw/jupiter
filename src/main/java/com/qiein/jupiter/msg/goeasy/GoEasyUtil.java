package com.qiein.jupiter.msg.goeasy;

import com.alibaba.fastjson.JSONObject;
import com.qiein.jupiter.util.PropertiesUtil;
import com.qiein.jupiter.util.StringUtil;

import io.goeasy.GoEasy;
import io.goeasy.publish.GoEasyError;
import io.goeasy.publish.PublishListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * GoEasy消息推送
 *
 * @author JingChenglong 2018/04/17 15:54
 */
@Component
public class GoEasyUtil {

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

//	GoEasyUtil (@Value("${goeasy.key}")String goeasyKey,
//					  @Value("${goeasy.linkaddr}")String goeasyLinkAddr,
//					  @Value("${goeasy.hmcrmchannelsuffix}")String hmCrmChannelSuffix,
//					  @Value("${goeasy.hmappchannelsuffix}")String hmAppChannelSuffix) {
//		GoEasyUtil.goeasyKey = goeasyKey;
//		GoEasyUtil.goeasyLinkAddr = goeasyLinkAddr;
//		GoEasyUtil.hmCrmChannelSuffix = hmCrmChannelSuffix;
//		GoEasyUtil.hmAppChannelSuffix = hmAppChannelSuffix;
//        goeasyInstance = new GoEasy(goeasyLinkAddr, goeasyKey);
//	}

	// TODO 该方法在系统停止时调用
	public static void destroy() {
		goeasyKey = "";
		goeasyLinkAddr = "";
		hmCrmChannelSuffix = "";

		goeasyInstance = null;
		// TODO LogUtils.log()-->GoEasy消息组件停止服务
	}

	/**
	 * GoEasy消息推送
	 *
	 * @param channel
	 * @param content
	 */
	private static synchronized void push(String channel, String content) {

		if (goeasyInstance == null) {
			// TODO LogUtils.log()-->GoEasy实例初始化失败
			return;
		}
		goeasyInstance.publish(channel, content, new PublishListener() {
			public void onSuccess() {
				// TODO LogUtils.log()-->消息发送成功--[channel : channel ; content :
				// content.toString]
			}

			public void onFailed(GoEasyError error) {
				// TODO LogUtils.log()-->消息发送失败-->"错误编码："- error.getCode()
				// "错误信息:"
				// -error.getContent()
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
		System.out.println(type + " " + companyId + " " + staffId + " " + content);
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
		System.out.println(type + " " + companyId + " " + staffId + " " + content);
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
	public static synchronized void pushAppInfoReceive(int companyId, int staffId, int kzNum, String kzId, Object logId,
			int overTime) {
		contentJson = new JSONObject();
		contentJson.put("kzid", kzId);
		contentJson.put("logid", logId);
		contentJson.put("kznum", kzNum);
		contentJson.put("overtime", overTime);

		pushApp(MessageConts.MSG_APP_INFO_REVEIVE, companyId, staffId, contentJson);
	}

	public static void main(String[] args) {
		int cid = 9999;
		int uid = 108;
		int kzNum = 11;
		String kzId = "123123123";
		String logId = "12";
		int overTime = 120;
		pushAppInfoReceive(cid, uid, kzNum, kzId, logId, overTime);
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
	private static synchronized void pushError(int companyId, int staffId, String head, String msg) {

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
	public static void pushStaffRefresh(int companyId, int staffId) {

		contentJson = new JSONObject();
		pushWeb(MessageConts.MSG_TYPE_STAFF_REFRESH, companyId, staffId, contentJson);
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
	 * @param kzNum
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
		System.out.println(companyId + " " + staffId + " " + info + " " + reason + " " + comeShopTime + " " + shopName
				+ " " + receptorName);

		StringBuffer sb = new StringBuffer();
		sb.append(" 您的客人在 ");
		sb.append(StringUtil.nullToStrTrim(shopName));
		sb.append(" 流失 ");
		String header = sb.toString();

		sb.delete(0, sb.length());
		sb.append("<br/><br/>姓名：");
		sb.append(StringUtil.nullToStrTrim(info.getKzName()));
		sb.append("<br/>手机：" + StringUtil.nullToStrTrim(info.getKzPhone()));
		sb.append("<br/>微信：" + StringUtil.nullToStrTrim(info.getKzWeChat()));
		sb.append("<br/><br/>接待门店：" + StringUtil.nullToStrTrim(shopName));
		sb.append("<br/>接待门市：" + StringUtil.nullToStrTrim(receptorName));
		sb.append("<br/><br/>到店时间：" + StringUtil.nullToStrTrim(comeShopTime));
		sb.append("<br/>流失原因：" + reason);
		String msg = sb.toString();
		pushCommon(companyId, staffId, header, msg);
	}

	/**
	 * 在线订单通知
	 *
	 * @param companyId
	 * @param staffId
	 * @param info
	 * @param successTime
	 * @param amount
	 */
	public static void pushSuccessOnline(int companyId, int staffId, ClientDTO info, String successTime,
			String amount) {

		StringBuffer sb = new StringBuffer();
		sb.append("恭喜您，您的客户在线订单啦");
		String header = sb.toString();

		sb.delete(0, sb.length());
		sb.append("<br/><br/>姓名：");
		sb.append(StringUtil.nullToStrTrim(info.getKzName()));
		sb.append("<br/>手机：" + StringUtil.nullToStrTrim(info.getKzPhone()));
		sb.append("<br/>微信：" + StringUtil.nullToStrTrim(info.getKzWeChat()));
		sb.append("<br/>渠道：" + StringUtil.nullToStrTrim(info.getChannelSource()));
		sb.append("<br/><br/>推广：" + StringUtil.nullToStrTrim(info.getCollector()));
		sb.append("<br/>客服：" + StringUtil.nullToStrTrim(info.getAppointor()));
		sb.append("<br/><br/>成交套系： ¥" + amount);
		sb.append("<br/>订单时间：" + StringUtil.nullToStrTrim(successTime));

		String msg = sb.toString();
		pushSuccess(companyId, staffId, header, msg);
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
		sb.append("<br/>手机：" + StringUtil.nullToStrTrim(info.getKzPhone()));
		sb.append("<br/>微信：" + StringUtil.nullToStrTrim(info.getKzWeChat()));
		sb.append("<br/>渠道：" + StringUtil.nullToStrTrim(info.getChannelSource()));
		sb.append("<br/><br/>接待门店：" + StringUtil.nullToStrTrim(shopName));
		sb.append("<br/>接待门市：" + StringUtil.nullToStrTrim(receptorName));
		sb.append("<br/><br/>成交套系： ¥" + amount);
		sb.append("<br/>订单时间：" + StringUtil.nullToStrTrim(successTime));

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
		sb.append("<br/>手机：" + StringUtil.nullToStrTrim(info.getKzPhone()));
		sb.append("<br/>微信：" + StringUtil.nullToStrTrim(info.getKzWeChat()));
		sb.append("<br/><br/>渠道：" + StringUtil.nullToStrTrim(info.getChannelSource()));

		String msg = sb.toString();
		pushWarn(companyId, staffId, header, msg);
	}

	/**
	 * 邀约无效驳回消息
	 *
	 * @param companyId
	 * @param staffId
	 * @param info
	 * @param reason
	 */
	public static void pushYyValidReject(int companyId, int staffId, ClientDTO info, String reason) {

		StringBuffer sb = new StringBuffer();
		sb.append("无效驳回");
		String header = sb.toString();

		sb.append("<br/><br/>姓名：");
		sb.append(StringUtil.nullToStrTrim(info.getKzName()));
		sb.append("<br/>手机：" + StringUtil.nullToStrTrim(info.getKzPhone()));
		sb.append("<br/>微信：" + StringUtil.nullToStrTrim(info.getKzWeChat()));
		sb.append("<br/><br/>您判定的无效客资被驳回，请重新邀约。");
		sb.append("<br/><br/>备注：" + reason);

		String msg = sb.toString();
		pushError(companyId, staffId, header, msg);
	}

	// public static void main(String[] args) {
	// int uid = 4618;
	// int cid = 42;
	// ClientDTO info = new ClientDTO();
	// info.setKzName("井成龙");
	// info.setKzPhone("18703882312");
	// info.setKzWeChat("xiangsiyu521");
	// info.setKzQq("260768009");
	//
	// pushShopMeetRunOff(cid, uid, info, "想去其他家看看", "2018-02-01 23:59:59",
	// "国际店", "萱萱");
	// // pushError(cid,uid,"错误","巴拉巴拉");
	// // pushSuccess(cid,uid,"成功","巴拉巴拉");
	// // pushWarn(cid,uid,"警告","巴拉巴拉");
	// }
}