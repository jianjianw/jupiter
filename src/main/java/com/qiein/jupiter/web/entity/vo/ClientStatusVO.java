package com.qiein.jupiter.web.entity.vo;

import java.io.Serializable;

/**
 * Created by Tt on 2018/5/15 0015.
 */
public class ClientStatusVO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer id;
    private String kzId;
    private Integer weFlag;
    private Integer sex;
    private Integer companyId;
    private Integer operaId;
    private String operaName;

    public Integer getOperaId() {
        return operaId;
    }

    public void setOperaId(Integer operaId) {
        this.operaId = operaId;
    }

    public String getOperaName() {
        return operaName;
    }

    public void setOperaName(String operaName) {
        this.operaName = operaName;
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

    public String getKzId() {
        return kzId;
    }

    public void setKzId(String kzId) {
        this.kzId = kzId;
    }

    public Integer getWeFlag() {
        return weFlag;
    }

    public void setWeFlag(Integer weFlag) {
        this.weFlag = weFlag;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }
}
