package com.qiein.jupiter.web.entity.vo;

/**
 * Created by Tt on 2018/5/15 0015.
 */
public class ClientStatusVO {
    private Integer ID;
    private String kzId;
    private Integer weFlag;
    private Integer sex;

    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
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
