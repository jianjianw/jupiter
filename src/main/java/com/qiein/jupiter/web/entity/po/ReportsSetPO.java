package com.qiein.jupiter.web.entity.po;

/**
 * 报表设置
 *
 * @Author: shiTao
 */
public class ReportsSetPO {
    /**
     * 公司编号
     */
    private int companyId;
    /**
     * 报表定义设置
     */
    private String defineSet;
    /**
     * 电商推广来源数据统计  表头设置
     */
    private String r1ShowTitleSet;


    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    public String getDefineSet() {
        return defineSet;
    }

    public void setDefineSet(String defineSet) {
        this.defineSet = defineSet;
    }

    public String getR1ShowTitleSet() {
        return r1ShowTitleSet;
    }

    public void setR1ShowTitleSet(String r1ShowTitleSet) {
        this.r1ShowTitleSet = r1ShowTitleSet;
    }
}
