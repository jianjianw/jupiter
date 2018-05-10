package com.qiein.jupiter.web.service;

import com.qiein.jupiter.web.entity.dto.StaffMarsDTO;

/**
 * 网销排班Service
 * Created by Administrator on 2018/5/10 0010.
 */
public interface StaffMarsService {
    /**
     * 编辑员工状态
     * @param staffMarsDTO
     */
    void editStaffMars(StaffMarsDTO staffMarsDTO);
}
