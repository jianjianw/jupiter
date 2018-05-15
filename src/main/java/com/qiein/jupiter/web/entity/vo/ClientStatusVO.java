package com.qiein.jupiter.web.entity.vo;

/**
 * Created by Tt on 2018/5/15 0015.
 */
public class ClientStatusVO {
    private Integer id;
    private String kzId;
    private Integer weFlag;
    private Integer sex;
    private Integer companyId;

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
