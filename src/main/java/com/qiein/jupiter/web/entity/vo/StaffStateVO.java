package com.qiein.jupiter.web.entity.vo;

import java.io.Serializable;

/**
 * 批量编辑员工状态的VO
 * Created by Administrator on 2018/4/25 0025.
 */
public class StaffStateVO implements Serializable {
    private static final long serialVersionUID = -7122419973591307115L;
    /**
     * 批量id字符串，由,分隔
     */
    private String ids;

    /**
     * isShow字段，避免is开头，显示标识
     */
    private Boolean show;

    /**
     * isLock字段，避免is开头，锁定标识
     */
    private Boolean lock;

    /**
     * isDel字段，避免is开头，删除标识
     */
    private Boolean del;

    /**
     *  所属公司
     */
    private Integer companyId;

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }

    public Boolean getShow() {
        return show;
    }

    public void setShow(Boolean show) {
        this.show = show;
    }

    public Boolean getLock() {
        return lock;
    }

    public void setLock(Boolean lock) {
        this.lock = lock;
    }

    public Boolean getDel() {
        return del;
    }

    public void setDel(Boolean del) {
        this.del = del;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }
}
