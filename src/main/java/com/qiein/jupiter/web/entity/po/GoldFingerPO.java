package com.qiein.jupiter.web.entity.po;

import java.util.Map;
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
    //渠道
    private Integer  srcId;
    //渠道名称
    private String srcName;
    //类型
    private Integer  typeId;
    //类型名称
    private String typeName;
    //咨询方式
    private Integer  collecterId;
    //咨询方式名称
    private String collecterName;
    //广告ID
    private String  adId;
    //广告着陆页
    private String  adAddress;
    //金数据字段解析前
    private Map<String,String> map;
    //金数据字段解析后
    private String  remark;
    //是否启用
    private Integer enabled;
    //是否推送消息
    private Integer isSend;
    //是否过滤
    private Integer isFilter;
    //异地拦截
    private Integer isIntercept;
    //POST地址
    private String postAddress;
    //备注
    private String memo;

    public String getSrcName() {
        return srcName;
    }

    public void setSrcName(String srcName) {
        this.srcName = srcName;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getCollecterName() {
        return collecterName;
    }

    public void setCollecterName(String collecterName) {
        this.collecterName = collecterName;
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Integer getSrcId() {
        return srcId;
    }

    public void setSrcId(Integer srcId) {
        this.srcId = srcId;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public Integer getCollecterId() {
        return collecterId;
    }

    public void setCollecterId(Integer collecterId) {
        this.collecterId = collecterId;
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

    public Map<String, String> getMap() {
        return map;
    }

    public void setMap(Map<String, String> map) {
        this.map = map;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getEnabled() {
        return enabled;
    }

    public void setEnabled(Integer enabled) {
        this.enabled = enabled;
    }

    public Integer getIsSend() {
        return isSend;
    }

    public void setIsSend(Integer isSend) {
        this.isSend = isSend;
    }

    public Integer getIsFilter() {
        return isFilter;
    }

    public void setIsFilter(Integer isFilter) {
        this.isFilter = isFilter;
    }

    public Integer getIsIntercept() {
        return isIntercept;
    }

    public void setIsIntercept(Integer isIntercept) {
        this.isIntercept = isIntercept;
    }

    public String getPostAddress() {
        return postAddress;
    }

    public void setPostAddress(String postAddress) {
        this.postAddress = postAddress;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
}
