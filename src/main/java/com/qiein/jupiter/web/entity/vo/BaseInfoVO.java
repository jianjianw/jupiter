package com.qiein.jupiter.web.entity.vo;


import com.qiein.jupiter.web.entity.dto.PageDictDTO;
import com.qiein.jupiter.web.entity.po.PermissionPO;
import com.qiein.jupiter.web.entity.po.PluginPO;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 员工基础信息
 */
public class BaseInfoVO implements Serializable {

    private static final long serialVersionUID = 9076352385052021856L;
    /**
     * 员工权限信息
     */
    private List<PermissionPO> permission;
    /**
     * 员工详细信息
     */
    private StaffDetailVO staffDetail;
    /**
     * 公司
     */
    private CompanyVO company;
    /**
     * 页面字典
     */
    private PageDictDTO pageDict;
    /**
     * 消息
     */
    private NewsTotalAmountAndFlag news;
    /**
     * 权限map
     */
    private Map<String, String> permissionMap;
    /**
     * 插件列表
     */
    private List<PluginVO> pluginList;

    public List<PluginVO> getPluginList() {
        return pluginList;
    }

    public void setPluginList(List<PluginVO> pluginList) {
        this.pluginList = pluginList;
    }

    public Map<String, String> getPermissionMap() {
        return permissionMap;
    }

    public void setPermissionMap(Map<String, String> permissionMap) {
        this.permissionMap = permissionMap;
    }

    public NewsTotalAmountAndFlag getNews() {
        return news;
    }

    public void setNews(NewsTotalAmountAndFlag news) {
        this.news = news;
    }

    public PageDictDTO getPageDict() {
        return pageDict;
    }

    public void setPageDict(PageDictDTO pageDict) {
        this.pageDict = pageDict;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public List<PermissionPO> getPermission() {
        return permission;
    }

    public void setPermission(List<PermissionPO> permission) {
        this.permission = permission;
    }

    public StaffDetailVO getStaffDetail() {
        return staffDetail;
    }

    public void setStaffDetail(StaffDetailVO staffDetail) {
        this.staffDetail = staffDetail;
    }

    public CompanyVO getCompany() {
        return company;
    }

    public void setCompany(CompanyVO company) {
        this.company = company;
    }
}
