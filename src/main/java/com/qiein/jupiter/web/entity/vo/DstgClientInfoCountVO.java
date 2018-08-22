package com.qiein.jupiter.web.entity.vo;

/**
 * 电商推广的vo对象
 *
 * @author sT
 */
public class DstgClientInfoCountVO {

    private int id;
    /**
     * 小组编号
     */
    private String groupId;
    /**
     * 小组名称
     */
    private String groupName;
    /**
     * 公司ID
     */
    private int companyId;
    /**
     * 每行数据
     */
    private NumVO numVO;

    public DstgClientInfoCountVO() {
    }

    public DstgClientInfoCountVO(String type, int companyId) {
        if ("total".equalsIgnoreCase(type)) {
            this.id = -1;
            this.groupName = "合计";
            this.companyId = companyId;
            this.numVO = new NumVO(type);
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public NumVO getNumVO() {
        return numVO;
    }

    public void setNumVO(NumVO numVO) {
        this.numVO = numVO;
    }

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

}
