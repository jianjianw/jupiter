package com.qiein.jupiter.web.entity.po;

/**
 * @author: yyx
 * @Date: 2018-8-9
 */
public class CallPO {
    /**
     * id
     * */
    private Integer id;

    /**
     * 电话实例id
     * */
    private String instanceId;

    /**
     * 实例电话
     * */
    private String phoneNumber;

    /**
     * 公司id
     * */
    private Integer companyId;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }
}
