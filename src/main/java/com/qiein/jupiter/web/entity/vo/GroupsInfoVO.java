package com.qiein.jupiter.web.entity.vo;

/**
 * Created by Administrator on 2018/5/8 0008.
 */
public class GroupsInfoVO extends GroupVO{

    /**
     * 在线人数
     */
    private Integer lineNum;

    /**
     * 订单数
     */
    private Integer orderNum;

    /**
     * 是否有权限查看
     */
    private boolean showFlag;

    public Integer getLineNum() {
        return lineNum;
    }

    public void setLineNum(Integer lineNum) {
        this.lineNum = lineNum;
    }

    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    public boolean isShowFlag() {
        return showFlag;
    }

    public void setShowFlag(boolean showFlag) {
        this.showFlag = showFlag;
    }
}
