package com.qiein.jupiter.web.entity.vo;

import java.io.Serializable;

public class ChannelGroupVO implements Serializable {
    private static final long serialVersionUID = -4468909903389314083L;
    /**
     * 拍摄地，渠道，小组关联ID
     */
    private int relaId;
    /**
     * 小组名
     */
    private String groupName;

    /**
     * 权重
     */
    private int weight;
    /**
     * 小组ID
     */
    private String groupId;

    public int getRelaId() {
        return relaId;
    }

    public void setRelaId(int relaId) {
        this.relaId = relaId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
}
