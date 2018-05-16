package com.qiein.jupiter.web.service.impl;

import com.qiein.jupiter.web.dao.LazyWorkDao;
import com.qiein.jupiter.web.entity.vo.LazyWorkVO;
import com.qiein.jupiter.web.service.LazyWorkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Tt on 2018/5/16 0016.
 */
@Service
public class LazyWorkServiceImpl implements LazyWorkService {

    @Autowired
    private LazyWorkDao lazyWorkDao;

    /**
     * 根据员工编号获取员工怠工日志
     * @param staffId
     * @param companyId
     * @return
     */
    @Override
    public List<LazyWorkVO> getLazyWorkListByStaffId(int staffId, int companyId) {
        return lazyWorkDao.getLazyWorkListByStaffId(staffId, companyId);
    }
}
