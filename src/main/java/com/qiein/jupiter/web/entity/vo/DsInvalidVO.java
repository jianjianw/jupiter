package com.qiein.jupiter.web.entity.vo;

import java.io.Serializable;

/**
 * 电商无效定义
 *
 * @author gaoxiaoli 2018/7/5
 */

public class DsInvalidVO implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    /**
     * 电商无效状态
     */
    private String dsInvalidStatus;
    /**
     * 电商无效意向等级
     */
    private String dsInvalidLevel;
    /**
     * 待定量是否计为有效
     */
    private boolean ddIsValid;
    /**
     * 电商待定状态
     */
    private String dsDdStatus;
    /**
     * 转介绍有效状态
     */
    private String zjsValidStatus;

    public String getDsInvalidStatus() {
        return dsInvalidStatus;
    }

    public void setDsInvalidStatus(String dsInvalidStatus) {
        this.dsInvalidStatus = dsInvalidStatus;
    }

    public String getDsInvalidLevel() {
        return dsInvalidLevel;
    }

    public void setDsInvalidLevel(String dsInvalidLevel) {
        this.dsInvalidLevel = dsInvalidLevel;
    }

    public boolean getDdIsValid() {
        return ddIsValid;
    }

    public void setDdIsValid(boolean ddIsValid) {
        this.ddIsValid = ddIsValid;
    }

    public String getDsDdStatus() {
        return dsDdStatus;
    }

    public void setDsDdStatus(String dsDdStatus) {
        this.dsDdStatus = dsDdStatus;
    }

    public String getZjsValidStatus() {
        return zjsValidStatus;
    }

    public void setZjsValidStatus(String zjsValidStatus) {
        this.zjsValidStatus = zjsValidStatus;
    }
}
