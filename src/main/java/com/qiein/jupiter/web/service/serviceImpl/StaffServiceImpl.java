package com.qiein.jupiter.web.service.serviceImpl;

import com.github.pagehelper.ISelect;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.qiein.jupiter.web.dao.StaffDao;
import com.qiein.jupiter.web.entity.vo.StaffVO;
import com.qiein.jupiter.web.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StaffServiceImpl implements StaffService<StaffVO> {

    @Autowired
    private StaffDao staffDao;

    @Override
    public Page<StaffVO> get() {
        return PageHelper.startPage(1, 5).doSelectPage(new ISelect() {
            @Override
            public void doSelect() {
                staffDao.getAll();
            }
        });
    }
}
