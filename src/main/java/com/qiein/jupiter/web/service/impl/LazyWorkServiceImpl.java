package com.qiein.jupiter.web.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qiein.jupiter.util.DBSplitUtil;
import com.qiein.jupiter.util.StringUtil;
import com.qiein.jupiter.web.dao.LazyWorkDao;
import com.qiein.jupiter.web.entity.vo.LazyWorkVO;
import com.qiein.jupiter.web.service.LazyWorkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 怠工
 * Created by Tt on 2018/5/16 0016.
 */
@Service
public class LazyWorkServiceImpl implements LazyWorkService {

    @Autowired
    private LazyWorkDao lazyWorkDao;

    /**
     * 根据员工编号获取员工怠工日志
     *
     * @param staffId
     * @param companyId
     * @return
     */
    @Override
    public List<LazyWorkVO> getLazyWorkListByStaffId(int staffId, int companyId) {
        return lazyWorkDao.getLazyWorkListByStaffId(staffId, companyId, DBSplitUtil.getAllotLogTabName(companyId), DBSplitUtil.getInfoTabName(companyId));
    }

    @Override
    public PageInfo<LazyWorkVO> getLazyWorkList(LazyWorkVO lazyWorkVO) {
        PageHelper.startPage(lazyWorkVO.getPageNum(), lazyWorkVO.getPageSize());
        List<LazyWorkVO> list = lazyWorkDao.getLazyWorkListByUWant(
                lazyWorkVO,
                StringUtil.isEmpty(lazyWorkVO.getStaffIds()) ? null : lazyWorkVO.getStaffIds().split(","),
                StringUtil.isEmpty(lazyWorkVO.getSourceIds()) ? null : lazyWorkVO.getSourceIds().split(","),
                DBSplitUtil.getAllotLogTabName(lazyWorkVO.getCompanyId()),
                DBSplitUtil.getInfoTabName(lazyWorkVO.getCompanyId()));
        return new PageInfo<>(list);
    }
}
