package com.qiein.jupiter.web.service.impl;

import com.qiein.jupiter.web.dao.CompanyDao;
import com.qiein.jupiter.web.entity.po.CompanyPO;
import com.qiein.jupiter.web.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 公司实现类
 */
@Service
public class CompanyServiceImpl implements CompanyService {
    @Autowired
    private CompanyDao companyDao;

    /**
     * 根据Id获取
     *
     * @param companyId
     * @return
     */
    @Override
    public CompanyPO getById(int companyId) {
        return companyDao.getById(companyId);
    }

    /**
     * 根据查询条件获取集合
     *
     * @param map
     * @return
     */
    @Override
    public List<CompanyPO> findList(Map map) {
        return null;
    }

    /**
     * 删除公司 逻辑删除
     *
     * @param companyId
     * @return
     */
    @Override
    public int deleteFlag(int companyId) {
        return 0;
    }

    /**
     * 插入
     *
     * @param companyPO
     * @return
     */
    @Override
    public CompanyPO insert(CompanyPO companyPO) {
        return null;
    }

    /**
     * 更新
     *
     * @param companyPO
     * @return
     */
    @Override
    public int update(CompanyPO companyPO) {
        return 0;
    }
}