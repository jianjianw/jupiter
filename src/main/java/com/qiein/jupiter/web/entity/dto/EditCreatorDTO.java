package com.qiein.jupiter.web.entity.dto;

import java.util.List;

public class EditCreatorDTO {
    private List<Integer> list;
    private String ids;
    private Integer createorId;
    private String createorName;

    public List<Integer> getList() {
        return list;
    }

    public void setList(List<Integer> list) {
        this.list = list;
    }

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }

    public Integer getCreateorId() {
        return createorId;
    }

    public void setCreateorId(Integer createorId) {
        this.createorId = createorId;
    }

    public String getCreateorName() {
        return createorName;
    }

    public void setCreateorName(String createorName) {
        this.createorName = createorName;
    }
}
