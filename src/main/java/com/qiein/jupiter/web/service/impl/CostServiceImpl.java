package com.qiein.jupiter.web.service.impl;

import com.qiein.jupiter.exception.ExceptionEnum;
import com.qiein.jupiter.exception.RException;
import com.qiein.jupiter.web.dao.CostDao;
import com.qiein.jupiter.web.entity.po.CostLogPO;
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
    public  List<CostShowVO> costList(String month, Integer companyId,Integer staffId){
        return costDao.costList(month,companyId,staffId);
    }

    /**
     * 添加花费
     * @param costPO
     */
    public int insert(CostPO costPO){
        int i=0;
        try{
            costDao.insert(costPO);
        }catch (Exception e){
            throw new RException(ExceptionEnum.EDIT_FAIL);
        }
        return i;
    }

    /**
     * 修改花费
     */
    public void editCost(CostPO costPO){
        costDao.editCost(costPO);
    }
    /**
     * 添加花费日志
     */
    public void createCostLog(CostLogPO costLogPO){
        costDao.createCostLog(costLogPO);
    }
}
