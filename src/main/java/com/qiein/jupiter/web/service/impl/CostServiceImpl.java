package com.qiein.jupiter.web.service.impl;

import com.qiein.jupiter.web.dao.CostDao;
import com.qiein.jupiter.web.entity.po.CostPO;
import com.qiein.jupiter.web.entity.vo.CostShowVO;
import com.qiein.jupiter.web.service.CostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CostServiceImpl implements CostService {
    @Autowired
    private CostDao costDao;
    /**
     * 获取花费页面信息
     * @param month
     * @param companyId
     * @return
     */
    public  List<Map> costList(String month, Integer companyId){
        return costDao.costList(month,companyId);
    }

    /**
     * 添加花费
     * @param costPO
     */
    public void insert(CostPO costPO){
        costDao.insert(costPO);
    }

    /**
     * 修改花费
     */
    public void editCost(CostPO costPO){
        costDao.editCost(costPO);
    }
}
