package com.qiein.jupiter.web.dao;

import com.qiein.jupiter.web.entity.vo.StaffVO;

import java.util.List;

public interface StaffDao {

    /**
     * 获取
     * @return
     */
    List<StaffVO> getAll();
}
