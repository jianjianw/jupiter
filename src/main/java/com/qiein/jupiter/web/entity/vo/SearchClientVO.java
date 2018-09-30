package com.qiein.jupiter.web.entity.vo;

/**
 * @Author: shiTao
 */
public class SearchClientVO {
    private static final long serialVersionUID = 1L;
    /**
     * 企业ID
     */
    private int companyId;

    /**
     * 关键词
     */
    private String key;

    /**
     * 客资ID
     */
    private String kzId;

    /**
     * 客资姓名
     */
    private String kzName;

    /**
     * 客资电话
     */
    private String kzPhone;

    /**
     * 客资微信
     */
    private String kzWeChat;

    /**
     * 客资旺旺
     */
    private String kzWw;

    /**
     * 客资QQ
     */
    private String kzQq;

    /**
     * 录入时间
     */
    private int createTime;

    /**
     * 状态ID
     */
    private int statusId;

    /**
     * 渠道ID
     */
    private int sourceId;

    /**
     * 客服姓名
     */
    private String appointName;

    /**
     * 客服组
     */
    private String groupName;

    /**
     * 配偶姓名
     */
    private String mateName;

    /**
     * 配偶电话
     */
    private String matePhone;

    /**
     * 配偶微信
     */
    private String mateWeChat;

    /**
     * 配偶QQ
     */
    private String mateQq;

    /**
     * 编号
     */
    private String id;


    /**
     * 字母编号
     */
    private String letterId;

    /**
     * 门市
     */
    private String receptorName;

    /**
     * 门店
     */
    private String shopName;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getReceptorName() {
        return receptorName;
    }

    public void setReceptorName(String receptorName) {
        this.receptorName = receptorName;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getLetterId() {
        return letterId;
    }

    public void setLetterId(String letterId) {
        this.letterId = letterId;
    }

    /**
     * 查询内容拼接
     */
    private String param;

    public String getMateName() {
        return mateName;
    }

    public void setMateName(String mateName) {
        this.mateName = mateName;
    }

    public String getMatePhone() {
        return matePhone;
    }

    public void setMatePhone(String matePhone) {
        this.matePhone = matePhone;
    }

    public String getMateWeChat() {
        return mateWeChat;
    }

    public void setMateWeChat(String mateWeChat) {
        this.mateWeChat = mateWeChat;
    }

    public String getMateQq() {
        return mateQq;
    }

    public void setMateQq(String mateQq) {
        this.mateQq = mateQq;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public String getKzId() {
        return kzId;
    }

    public void setKzId(String kzId) {
        this.kzId = kzId;
    }

    public String getKzName() {
        return kzName;
    }

    public void setKzName(String kzName) {
        this.kzName = kzName;
    }

    public String getKzPhone() {
        return kzPhone;
    }

    public void setKzPhone(String kzPhone) {
        this.kzPhone = kzPhone;
    }

    public String getKzWeChat() {
        return kzWeChat;
    }

    public void setKzWeChat(String kzWeChat) {
        this.kzWeChat = kzWeChat;
    }

    public String getKzWw() {
        return kzWw;
    }

    public void setKzWw(String kzWw) {
        this.kzWw = kzWw;
    }

    public String getKzQq() {
        return kzQq;
    }

    public void setKzQq(String kzQq) {
        this.kzQq = kzQq;
    }

    public int getCreateTime() {
        return createTime;
    }

    public void setCreateTime(int createTime) {
        this.createTime = createTime;
    }

    public int getStatusId() {
        return statusId;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    public int getSourceId() {
        return sourceId;
    }

    public void setSourceId(int sourceId) {
        this.sourceId = sourceId;
    }

    public String getAppointName() {
        return appointName;
    }

    public void setAppointName(String appointName) {
        this.appointName = appointName;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
