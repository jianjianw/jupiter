package com.qiein.jupiter.util.wechat;

import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * 推送模版的信息
 * @create by Tt(叶华葳) 2018-06-08 17:36
 */
public class WeChatPushMsgDTO {
    /**
     * 公司内员工id
     */
    private Integer cid;
    /**
     * 所属草莓卷公司id
     */
    private Integer uid;
    /**
     * openid
     */
    private String touser;
    /**
     * 模版id
     */
    private String template_id;
    /**
     * 模版跳转url
     */
    private String url;
    /**
     * 跳小程序所需数据，不需跳小程序可不用传该数据
     */
    private HashMap<String,Object> miniprogram;
    /**
     * 客资Id
     */
    private String kzId;
    /**
     * 日志id
     */
    private String logId;

//    /**
//     * 客资姓名
//     */
//    private String kzName;
//    /**
//     * 客资电话
//     */
//    private String kzPhone;
//    /**
//     * 发送时间 格式 2018/6/8 21:34 的字符串
//     */
//    private String time;
    /**
     * 模板数据
     */
    private Map<String,Object> data;

    /**
     *  推送新客资的有参构造器
     * @param cid       员工所属公司id
     * @param cName     员工所属公司名称
     * @param uid       员工id
     * @param url       点击消息跳转的地址
     * @param kzName    客资姓名
     * @param kzPhone   客资电话
     * @param time      发送时间
     */
    public WeChatPushMsgDTO(Integer cid,String cName, Integer uid, String url, String kzName, String kzPhone, String time,String kzId ,String logId) {
        this.data = new HashMap<>();
        this.cid = cid;
        this.uid = uid;
        this.url = url;
        this.kzId=kzId;
        this.logId=logId;
        HashMap<String,Object> first = new HashMap<>();
        first.put("value","您有一个新的客资");
        HashMap<String,Object> remark = new HashMap<>();
        remark.put("value","点击详情即可领取客资,客资来自"+cName);

        HashMap<String,Object> h1 = new HashMap<>();
        h1.put("value",kzName);
        HashMap<String,Object> h2 = new HashMap<>();
        h2.put("value",kzPhone);
        HashMap<String,Object> h3 = new HashMap<>();
        h3.put("value",time);
        this.data.put("first",first);
        this.data.put("keyword1",h1);
        this.data.put("keyword2",h2);
        this.data.put("keyword3",h3);
        this.data.put("remark",remark);
    }

    /**
     * 发送自定义普通客资消息
     * @param firstText
     * @param remarkText
     * @param cid
     * @param cName
     * @param uid
     * @param kzName
     * @param time
     */
    public WeChatPushMsgDTO(String firstText,String remarkText,Integer cid ,String cName,Integer uid,String kzName, String kzPhone,String time){
        this.data= new TreeMap<>();
        this.cid=cid;
        this.uid=uid;
        Map<String,Object> first = new TreeMap<>();
        first.put("value",firstText);
        Map<String,Object> remark = new TreeMap<>();
        remark.put("value",remarkText+"\r\n客资消息来自"+cName);

        Map<String,Object> h1 = new TreeMap<>();
        h1.put("value",kzName);
        Map<String,Object> h2 = new TreeMap<>();
        h2.put("value",kzPhone);
        Map<String,Object> h3 = new TreeMap<>();
        h3.put("value",time);

        data.put("first",first);
        data.put("keyword1",h1);
        data.put("keyword2",h2);
        data.put("keyword3",h3);
        data.put("remark",remark);

    }

    public WeChatPushMsgDTO() {

    }

    public Integer getCid() {
        return cid;
    }

    public void setCid(Integer cid) {
        this.cid = cid;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getTemplate_id() {
        return template_id;
    }

    public void setTemplate_id(String template_id) {
        this.template_id = template_id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    public String getTouser() {
        return touser;
    }

    public void setTouser(String touser) {
        this.touser = touser;
    }

    public HashMap<String, Object> getMiniprogram() {
        return miniprogram;
    }

    public void setMiniprogram(HashMap<String, Object> miniprogram) {
        this.miniprogram = miniprogram;
    }

    public String getKzId() {
        return kzId;
    }

    public void setKzId(String kzId) {
        this.kzId = kzId;
    }

    public String getLogId() {
        return logId;
    }

    public void setLogId(String logId) {
        this.logId = logId;
    }
}
