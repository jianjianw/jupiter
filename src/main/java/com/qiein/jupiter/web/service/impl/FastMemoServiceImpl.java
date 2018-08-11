package com.qiein.jupiter.web.service.impl;

import com.qiein.jupiter.web.dao.FastMemoDao;
import com.qiein.jupiter.web.service.FastMemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 快捷备注
 *
 * @author gaoxiaoli 2018/8/11
 */

@Service
public class FastMemoServiceImpl implements FastMemoService {
    @Autowired
    private FastMemoDao fastMemoDao;

    /**
     * 获取个人快捷备注列表
     *
     * @param staffId
     * @param companyId
     * @return
     */
    @Override
    public List<String> getMemoListById(int staffId, int companyId) {
        return fastMemoDao.getMemoListById(staffId, companyId);
    }

    /**
     * 添加快捷备注
     *
     * @param staffId
     * @param companyId
     * @param memo
     * @return
     */
    public void addFastMemo(int staffId, int companyId, String memo) {
        fastMemoDao.addFastMemo(staffId, companyId, memo);
    }
}
