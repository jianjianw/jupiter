package com.qiein.jupiter.web.service.impl;

import com.qiein.jupiter.web.dao.StaffMarsDao;
import com.qiein.jupiter.web.entity.dto.StaffMarsDTO;
import com.qiein.jupiter.web.service.StaffMarsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2018/5/10 0010.
 */
@Service
public class StaffMarsServiceImpl implements StaffMarsService {

    @Autowired
    private StaffMarsDao staffMarsDao;

    @Override
    public void editStaffMars(StaffMarsDTO staffMarsDTO) {
        //TODO 检查今日接单和限额的关系来改变状态
        staffMarsDao.update(staffMarsDTO);
    }
}
