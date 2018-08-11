package com.qiein.jupiter.web.entity.po;

import com.qiein.jupiter.web.entity.BaseEntity;

/**
 * 提示消息数据库实体
 */
public class NewsPO extends BaseEntity {

    private static final long serialVersionUID = -7372571135919481573L;
    /**
     * 消息类型
     */
    private String type;
    /**
     * 消息标题
     */
    private String head;
    /**
     * 消息内容
     */
    private String msg;
    /**
     * 客资id
     */
    private String kzid;
    /**
     * 员工id
     */
    private int staffId;
    /**
     * 创建时间
     */
    private int createTime;
    /**
     * 公司id
     */
    private int companyId;
    /**
     * 是否已读
     */
    private boolean readFlag;


    public NewsPO() {
    }

    public NewsPO(String type, String head, String msg, String kzid, int staffId, int companyId) {
        this.type = type;
        this.head = head;
        this.msg = msg;
        this.kzid = kzid;
        this.staffId = staffId;
        this.companyId = companyId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getKzid() {
        return kzid;
    }

    public void setKzid(String kzid) {
        this.kzid = kzid;
    }

    public int getStaffId() {
        return staffId;
    }

    public void setStaffId(int staffId) {
        this.staffId = staffId;
    }

    public int getCreateTime() {
        return createTime;
    }

    public void setCreateTime(int createTime) {
        this.createTime = createTime;
    }

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    public boolean isReadFlag() {
        return readFlag;
    }

    public void setReadFlag(boolean readFlag) {
        this.readFlag = readFlag;
    }
}
