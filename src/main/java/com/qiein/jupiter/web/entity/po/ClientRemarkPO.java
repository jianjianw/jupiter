package com.qiein.jupiter.web.entity.po;

import java.io.Serializable;

/**
 * FileName: ClientRemarkPO
 *
 * @author: yyx
 * @Date: 2018-6-14 17:22
 */
public class ClientRemarkPO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Integer id;
    /**
     * kzid
     */
    private String kzId;
    /**
     * 备注
     */
    private String content;
    /**
     * 公司id
     */
    private Integer companyId;

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }
}
