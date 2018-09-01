package com.qiein.jupiter.web.entity.po;

/**
 * 场地
 *
 * @author: yyx
 * @Date: 2018-8-31
 */
public class SitePO {
    /**
     * id
     */
    private Integer id;

    /**
     * 场地名称
     * */
    private String name;

    /**
     * 是否显示
     * */
    private Boolean isShow;

    /**
     * 公司id
     * */
    private Integer companyId;

    /**
     * 创建时间
     * */
    private Integer createTime;

    /**
     * 更新时间
     * */
    private Integer updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getShow() {
        return isShow;
    }

    public void setShow(Boolean show) {
        isShow = show;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Integer getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Integer createTime) {
        this.createTime = createTime;
    }

    public Integer getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Integer updateTime) {
        this.updateTime = updateTime;
    }
}
