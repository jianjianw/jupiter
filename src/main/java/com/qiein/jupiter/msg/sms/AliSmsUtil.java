// package com.qiein.jupiter.msg.sms;
//
// import com.alibaba.fastjson.JSONObject;
// import com.qiein.jupiter.util.StringUtil;
//
/// **
// * 阿里大于短信工具类
// *
// * @author JingChenglong 2018/04/19 10:46
// *
// */
// public class AliSmsUtil {
//
// /**
// * 调用地址
// */
// private static String url = "";
//
// /**
// * 阿里大于应用APPKey
// */
// private static String appkey = "";
//
// /**
// * 阿里大于应用秘钥
// */
// private static String secret = "";
//
// /**
// * 大于产品名
// */
// private static String product = "";
//
// /**
// * 短息发送客户端
// */
// private static TaobaoClient client = null;
//
// /**
// * 短信参数体封装
// */
// private static JSONObject jsonParam = new JSONObject();
//
// private static AlibabaAliqinFcSmsNumSendRequest req = null;
// private static AlibabaAliqinFcSmsNumSendResponse rsp = null;
//
// // TODO 系统初始化调用
// public static void init() {
// url = "http://gw.api.taobao.com/router/rest";
// appkey = "23453403";
// secret = "ef084b61ae7155445d12bebcf4383644";
// product = "清莹网CRM管理中心";
// client = new DefaultTaobaoClient(url, appkey, secret);
// // TODO LogUtil.log()-- > 阿里大于短信组件启动成功
// }
//
// // TODO 系统停止时调用
// public static void destroy() {
// url = "";
// appkey = "";
// secret = "";
// product = "";
// client = null;
// // TODO LogUtil.log()--> 阿里大于短信组件停止服务
// }
//
// /**
// * 发送短息
// *
// * @param telNo-短信接收者手机号
// * @param smsDto-短信信息封装体
// *
// * @throws EduException
// */
// public static synchronized void send(String telNo, SmsDTO smsDto) throws
// EduException {
// if (StringUtil.isEmpty(telNo)) {
// throw new EduException("短信接收手机号不能为空");
// }
// if (!smsDto.isNotEmpty()) {
// throw new EduException("短信内容不能为空");
// }
//
// smsDto.getContent().put("product", product);
//
// req = new AlibabaAliqinFcSmsNumSendRequest();
// req.setSmsType("normal");// 短信类型
// req.setSmsFreeSignName(StringUtil.nullToStrTrim(smsDto.getSign()));//
// 短信签名，传入的短信签名必须是在阿里大鱼“管理中心-短信签名管理”中的可用签名。
// req.setSmsParamString(smsDto.getContent().toString());//
// 短信模板变量，传参规则{"key":"value"}，key的名字须和申请模板中的变量名一致，多个变量之间以逗号隔开。
// req.setRecNum(telNo);//
// 短信接收号码（短信接收号码。支持单个或多个手机号码，传入号码为11位手机号码，不能加0或+86。群发短信需传入多个号码，以英文逗号分隔，一次调用最多传入200个号码。）
// req.setSmsTemplateCode(StringUtil.nullToStrTrim(smsDto.getTemplateId()));//
// 短信模板ID
// try {
// rsp = client.execute(req);
// String rspRst = rsp.getBody();
// JSONObject rstJson = JSONObject.parseObject(rspRst);
// if
// (!"0".equals(rstJson.getJSONObject("alibaba_aliqin_fc_sms_num_send_response").getJSONObject("result")
// .getString("err_code"))) {
// throw new EduException("短信发送失败，请稍后重试");
// // TODO LogUtil.log ---> 阿里大于短信发送失败：[ telNo : , sign: ,
// // template: ,
// // content: ]+e.getMessage + rstJson
// }
// } catch (ApiException e) {
// throw new EduException("短信发送失败，请稍后重试");
// // TODO LogUtil.log ---> 阿里大于短信发送失败：[ telNo : , sign: , template: ,
// // content: ]+e.getMessage
// }
//
// // TODO LogUtil.log()---> 短信发送日志记录：
// }
//
// /**
// * 客资预约进店短信通知
// *
// * @param sign-签名
// * @param template-短信模板
// * @param telNo-接受者手机号
// * @param code-预约进店编码
// * @param appointTime-预约进店时间
// * @param address-门店地址
// * @param shopTelNo-门店接待人联系方式
// *
// * @throws EduException
// */
// public static synchronized void sendClientYyShop(String sign, String
// template, String telNo, String code,
// String appointTime, String address, String shopTelNo) throws EduException {
//
// jsonParam.clear();
// jsonParam.put("code", StringUtil.nullToStrTrim(code));
// jsonParam.put("time", StringUtil.nullToStrTrim(appointTime));
// jsonParam.put("address", StringUtil.nullToStrTrim(address));
// jsonParam.put("telno", StringUtil.nullToStrTrim(shopTelNo));
// send(telNo, new SmsDTO(sign, template));
// }
// }