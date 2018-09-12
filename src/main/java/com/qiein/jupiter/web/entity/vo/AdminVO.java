package com.qiein.jupiter.web.entity.vo;

import com.qiein.jupiter.web.entity.po.AdminLogPO;

import java.util.List;

/**
 * author xiangliang
 * 管理员页面以及日志页面展示vo
 */
public class AdminVO {
    List<AdminShowVO> list;
    List<AdminLogPO> logList;
    private Integer count;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
    public List<AdminShowVO> getList() {
        return list;
    }

    public void setList(List<AdminShowVO> list) {
        this.list = list;
    }

    public List<AdminLogPO> getLogList() {
        return logList;
    }

    public void setLogList(List<AdminLogPO> logList) {
        this.logList = logList;
    }
}
