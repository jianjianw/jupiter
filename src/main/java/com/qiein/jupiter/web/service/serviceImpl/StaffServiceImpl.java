package com.qiein.jupiter.web.service.serviceImpl;

import com.github.pagehelper.ISelect;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.qiein.jupiter.web.dao.StaffDao;
import com.qiein.jupiter.web.entity.vo.Staff;
import com.qiein.jupiter.web.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StaffServiceImpl implements StaffService<Staff> {

    @Autowired
    private StaffDao staffDao;

    @Override
    public Page<Staff> get() {
        return PageHelper.startPage(1, 5).doSelectPage(new ISelect() {
            @Override
            public void doSelect() {
                staffDao.getAll();
            }
        });
    }
}
