package com.qiein.jupiter.web.entity.vo;

import com.qiein.jupiter.constant.ClientStatusConst;
import com.qiein.jupiter.constant.CommonConstant;
import com.qiein.jupiter.util.NumUtil;
import com.qiein.jupiter.util.StringUtil;

/**
 * @Author: shiTao
 */
public class QueryVO {

    private static final long serialVersionUID = 1L;

    /**
     * 员工ID
     */
    private int uid;

    /**
     * token
     */
    private String sig;

    /**
     * 角色
     */
    private String role;

    /**
     * 客资tab
     */
    private String action;

    /**
     * 客资分类
     */
    private int classId;

    /**
     * 时间范围类型
     */
    private String timeType;

    /**
     * 开始时间
     */
    private int start;

    /**
     * 结束时间
     */
    private int end;

    /**
     * 权限限制
     */
    private int pmsLimit;

    /**
     * 联系方式类型限制
     */
    private String linkLimit;

    /**
     * 品牌ID
     */
    private String channelId;

    /**
     * 渠道ID
     */
    private String sourceId;

    /**
     * 门店-拍摄地ID
     */
    private String shopId;

    /**
     * 人员ID
     */
    private String staffId;

    /**
     * 类型ID
     */
    private String typeId;

    /**
     * 意向等级
     */
    private String yxLevel;

    /**
     * 录入人ID
     */
    private String collectorId = "";

    /**
     * 筛选人ID
     */
    private String promotorId = "";

    /**
     * 邀约人ID
     */
    private String appointorId = "";

    /**
     * 接待人ID
     */
    private String receptorId = "";

    private String spareSql = "";

    private String filterSql = "";

    private String superSql = "";

    /**
     * 客资ID
     */
    private String kzId;

    /**
     * 客资状态
     */
    private String statusId;
    /**
     * 搜索关键字
     */
    private String searchKey;

    private int operationAdminNo = 0;// 执行此次操作的人员编号
    private String operationAdminName = "";// 执行此次操作的人员姓名
    private String requestIp;// 请求IP
    /**
     * ip
     */
    private int companyId;
    /**
     * 页面显示条数
     */
    private int pageSize;
    /**
     * 当前第几页
     */
    private int currentPage;


    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public int getOperationAdminNo() {
        return operationAdminNo;
    }

    public void setOperationAdminNo(int operationAdminNo) {
        this.operationAdminNo = operationAdminNo;
    }

    public String getOperationAdminName() {
        return operationAdminName;
    }

    public void setOperationAdminName(String operationAdminName) {
        this.operationAdminName = operationAdminName;
    }

    public String getRequestIp() {
        return requestIp;
    }

    public void setRequestIp(String requestIp) {
        this.requestIp = requestIp;
    }

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public String getYxLevel() {
        return yxLevel;
    }

    public void setYxLevel(String yxLevel) {
        this.yxLevel = yxLevel;
    }

    public String getStatusId() {
        return statusId;
    }

    public void setStatusId(String statusId) {
        this.statusId = statusId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        if (StringUtil.isNotEmpty(action)) {
            this.classId = ClientStatusConst.getClassByAction(action);
            if (NumUtil.isInValid(this.classId)) {
                // TODO 自定义action
                this.statusId = "";
            }
        }
        this.action = action;
    }

    public String getKzId() {
        return kzId;
    }

    public void setKzId(String kzId) {
        this.kzId = kzId;
    }

    public String getCollectorId() {
        return collectorId;
    }

    public void setCollectorId(String collectorId) {
        this.collectorId = collectorId;
    }

    public void appendCollectorId(int id) {
        this.collectorId += CommonConstant.STR_SEPARATOR;
        this.collectorId += id;
    }

    public void appendPromotorId(int id) {
        this.promotorId += CommonConstant.STR_SEPARATOR;
        this.promotorId += id;
    }

    public void appendAppointorId(int id) {
        this.appointorId += CommonConstant.STR_SEPARATOR;
        this.appointorId += id;
    }

    public void appendReceptorId(int id) {
        this.receptorId += CommonConstant.STR_SEPARATOR;
        this.receptorId += id;
    }

    public String getPromotorId() {
        return promotorId;
    }

    public void setPromotorId(String promotorId) {
        this.promotorId = promotorId;
    }

    public String getAppointorId() {
        return appointorId;
    }

    public void setAppointorId(String appointorId) {
        this.appointorId = appointorId;
    }

    public String getReceptorId() {
        return receptorId;
    }

    public void setReceptorId(String receptorId) {
        this.receptorId = receptorId;
    }

    public String getSpareSql() {
        return spareSql;
    }

    public void setSpareSql(String spareSql) {
        this.spareSql = spareSql;
    }

    public String getFilterSql() {
        return filterSql;
    }

    public void setFilterSql(String filterSql) {
        this.filterSql = filterSql;
    }

    public String getSuperSql() {
        return superSql;
    }

    public void setSuperSql(String superSql) {
        this.superSql = superSql;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getTimeType() {
        return timeType;
    }

    public void setTimeType(String timeType) {
        if (StringUtil.isNotEmpty(timeType) && !timeType.startsWith("info.")) {
            timeType = "info." + timeType;
        }
        this.timeType = timeType;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public int getPmsLimit() {
        return pmsLimit;
    }

    public void setPmsLimit(int pmsLimit) {
        this.pmsLimit = pmsLimit;
    }

    public String getLinkLimit() {
        return linkLimit;
    }

    public void setLinkLimit(String linkLimit) {
        if (StringUtil.isEmpty(linkLimit)) {
            this.linkLimit = "";
        }
        switch (linkLimit.trim()) {
            case "1":
                // 电话客资
                linkLimit = " info.KZPHONE IS NOT NULL AND info.KZPHONE != '' ";
                break;
            case "2":
                // 微信客资
                linkLimit = " info.KZWECHAT IS NOT NULL AND info.KZWECHAT != '' ";
                break;
            case "3":
                // QQ客资
                linkLimit = " info.KZQQ IS NOT NULL AND info.KZQQ != '' ";
                break;
            case "4":
                // 有电话有微信
                linkLimit = " info.KZPHONE IS NOT NULL AND info.KZPHONE != '' AND info.KZWECHAT IS NOT NULL AND info.KZWECHAT != '' ";
                break;
            case "5":
                // 无电话
                linkLimit = " ( info.KZPHONE IS NULL OR info.KZPHONE = '' ) ";
                break;
            case "6":
                // 无微信
                linkLimit = " ( info.KZWECHAT IS NULL OR info.KZWECHAT = '' ) ";
                break;
            case "7":
                // 无电话无微信
                linkLimit = " ( info.KZPHONE IS NULL OR info.KZPHONE = '' ) AND ( info.KZWECHAT IS NULL OR info.KZWECHAT = '' ) ";
                break;
            case "8":
                // 只有电话
                linkLimit = " info.KZPHONE IS NOT NULL AND info.KZPHONE != '' AND ( info.KZWECHAT IS NULL OR info.KZWECHAT = '' ) AND ( info.KZQQ IS NULL OR info.KZQQ = '' ) ";
                break;
            case "10":
                // 只有微信
                linkLimit = " info.KZWECHAT IS NOT NULL AND info.KZWECHAT != '' AND ( info.KZPHONE IS NULL OR info.KZPHONE = '' ) AND ( info.KZQQ IS NULL OR info.KZQQ = '' ) ";
                break;
            case "11":
                // 双客资，必须有电话，QQ，微信有其一
                linkLimit = " info.KZPHONE IS NOT NULL AND info.KZPHONE != '' AND ( info.KZWECHAT IS NOT NULL OR info.KZQQ IS NOT NULL ) ";
                break;
            case "12":
                // 有旺旺
                linkLimit = " info.KZWW IS NOT NULL AND info.KZWW != '' ";
                break;
            default:
                linkLimit = "";
                break;
        }

        this.linkLimit = linkLimit;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getSig() {
        return sig;
    }

    public void setSig(String sig) {
        this.sig = sig;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    public String getSearchKey() {
        return searchKey;
    }

    public void setSearchKey(String searchKey) {
        this.searchKey = searchKey;
    }

    public void appendSuperSql(String sql) {
        if (StringUtil.isNotEmpty(this.superSql)) {
            sql += " AND ";
        }
        this.superSql += sql;
    }
}
