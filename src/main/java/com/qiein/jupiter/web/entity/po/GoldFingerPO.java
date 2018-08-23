package com.qiein.jupiter.web.entity.po;

import java.sql.Date;
/**
 * 金手指表单 添加PO
 * Author xiangliang 2018/6/13
 */
public class GoldFingerPO {
    //主键id
    private Integer id;
    //员工id
    private Integer staffId;
    //公司id
    private Integer companyId;
    //表单号
    private String formId;
    //表单名称
    private String formName;
    //姓名字段名
    private String kzNameField;
    //电话字段名
    private String kzPhoneField;
    //微信字段名
    private String kzWechatField;
    //其他字段代表
    private String fieldKey;
    //其他字段名的名称
    private String fieldValue;
    //渠道
    private Integer  srcId;
    //渠道名称
    private String srcName;
    //类型
    private Integer  typeId;
    //类型名称
    private String typeName;
    //咨询方式
    private String  zxStyle;
    //广告ID
    private String  adId;
    //广告着陆页
    private String  adAddress;
    //是否启用
    private Boolean isShow;
    //是否推送消息
    private Boolean pushNews;
    //是否过滤
    private Boolean isFilter;
    //异地拦截
    private Boolean areaLimit;
    //POST地址
    private String postURL;
    //备注
    private String memo;
    //创建者id
    private Integer createorId;
    //创建者名称
    private String createorName;

    private String createTime;
    //老客姓名
    private String oldKzName;
    //老客电话
    private String oldKzPhone;

    public String getOldKzName() {
        return oldKzName;
    }

    public void setOldKzName(String oldKzName) {
        this.oldKzName = oldKzName;
    }

    public String getOldKzPhone() {
        return oldKzPhone;
    }

    public void setOldKzPhone(String oldKzPhone) {
        this.oldKzPhone = oldKzPhone;
    }

    public Integer getCreateorId() {
        return createorId;
    }

    public void setCreateorId(Integer createorId) {
        this.createorId = createorId;
    }

    public Boolean getIsShow() {
        return isShow;
    }

    public void setIsShow(Boolean isShow) {
        this.isShow = isShow;
    }

    public String getCreateorName() {
        return createorName;
    }

    public void setCreateorName(String createorName) {
        this.createorName = createorName;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getStaffId() {
        return staffId;
    }

    public void setStaffId(Integer staffId) {
        this.staffId = staffId;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public String getFormId() {
        return formId;
    }

    public void setFormId(String formId) {
        this.formId = formId;
    }

    public String getFormName() {
        return formName;
    }

    public void setFormName(String formName) {
        this.formName = formName;
    }

    public String getKzNameField() {
        return kzNameField;
    }

    public void setKzNameField(String kzNameField) {
        this.kzNameField = kzNameField;
    }

    public String getKzPhoneField() {
        return kzPhoneField;
    }

    public void setKzPhoneField(String kzPhoneField) {
        this.kzPhoneField = kzPhoneField;
    }

    public String getKzWechatField() {
        return kzWechatField;
    }

    public void setKzWechatField(String kzWechatField) {
        this.kzWechatField = kzWechatField;
    }

    public String getFieldKey() {
        return fieldKey;
    }

    public void setFieldKey(String fieldKey) {
        this.fieldKey = fieldKey;
    }

    public String getFieldValue() {
        return fieldValue;
    }

    public void setFieldValue(String fieldValue) {
        this.fieldValue = fieldValue;
    }

    public Integer getSrcId() {
        return srcId;
    }

    public void setSrcId(Integer srcId) {
        this.srcId = srcId;
    }

    public String getSrcName() {
        return srcName;
    }

    public void setSrcName(String srcName) {
        this.srcName = srcName;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getZxStyle() {
        return zxStyle;
    }

    public void setZxStyle(String zxStyle) {
        this.zxStyle = zxStyle;
    }

    public String getAdId() {
        return adId;
    }

    public void setAdId(String adId) {
        this.adId = adId;
    }

    public String getAdAddress() {
        return adAddress;
    }

    public void setAdAddress(String adAddress) {
        this.adAddress = adAddress;
    }

    public Boolean getEnabled() {
        return isShow;
    }

    public void setEnabled(Boolean isShow) {
        this.isShow = isShow;
    }

    public Boolean getPushNews() {
        return pushNews;
    }

    public void setPushNews(Boolean pushNews) {
        this.pushNews = pushNews;
    }

    public Boolean getIsFilter() {
        return isFilter;
    }

    public void setIsFilter(Boolean isFilter) {
        this.isFilter = isFilter;
    }

    public Boolean getAreaLimit() {
        return areaLimit;
    }

    public void setAreaLimit(Boolean areaLimit) {
        this.areaLimit = areaLimit;
    }

    public String getPostURL() {
        return postURL;
    }

    public void setPostURL(String postURL) {
        this.postURL = postURL;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
}
