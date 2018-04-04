package com.qiein.jupiter.web.service.serviceImpl;

import com.github.pagehelper.ISelect;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.qiein.jupiter.exception.ExceptionEnum;
import com.qiein.jupiter.exception.RRException;
import com.qiein.jupiter.web.dao.StaffDao;
import com.qiein.jupiter.web.entity.po.StaffPO;
import com.qiein.jupiter.web.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StaffServiceImpl implements StaffService<StaffPO> {

    @Autowired
    private StaffDao staffDao;

    @Override
    public Page<StaffPO> get()  {
        try {
            return PageHelper.startPage(1, 5).doSelectPage(new ISelect() {
                @Override
                public void doSelect() {
                    staffDao.findList(null);
                }
            });
        } catch (Exception e) {
            throw new RRException(ExceptionEnum.USER_NOT_FIND);
        }

    }
}
