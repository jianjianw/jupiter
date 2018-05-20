package com.qiein.jupiter.web.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qiein.jupiter.exception.ExceptionEnum;
import com.qiein.jupiter.exception.RException;
import com.qiein.jupiter.util.NumUtil;
import com.qiein.jupiter.web.dao.CompanyDao;
import com.qiein.jupiter.web.entity.po.CompanyPO;
import com.qiein.jupiter.web.service.CompanyService;

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
    public List<CompanyPO> findList(Map<String, Object> map) {
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
        if (companyPO.getId() == 0)
            throw new RException(ExceptionEnum.COMPANY_ID_NULL);
        return companyDao.update(companyPO);
    }

    /**
     * 编辑自定义设置
     *
     * @param column
     * @param flag
     * @return
     */
    @Override
    public int updateFlag(int companyId, String column, boolean flag) {
        if (NumUtil.isInValid(companyId)) {
            throw new RException(ExceptionEnum.COMPANY_ID_NULL);
        }
        return companyDao.updateFlag(companyId, column, flag);
    }

    /**
     * 编辑自定义范围
     *
     * @param column
     * @param num
     * @return
     */
    @Override
    public int updateRange(int companyId, String column, int num) {
        if (NumUtil.isInValid(companyId)) {
            throw new RException(ExceptionEnum.COMPANY_ID_NULL);
        }
        return companyDao.updateRange(companyId, column, num);
    }

    /**
     * 获取iplimt
     *
     * @param companyId
     * @return
     */
    @Override
    public int getIpLimit(int companyId) {
        return companyDao.getIpLimit(companyId);
    }

    /**
     * 修改iplimt
     *
     * @param ipLimit * @param companyId
     * @return
     */
    @Override
    public void editIpLimit(Integer ipLimit, int companyId) {
        companyDao.editIpLimit(ipLimit, companyId);
    }
}