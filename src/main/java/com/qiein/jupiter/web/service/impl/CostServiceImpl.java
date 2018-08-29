package com.qiein.jupiter.web.service.impl;

import com.qiein.jupiter.constant.CommonConstant;
import com.qiein.jupiter.enums.TableEnum;
import com.qiein.jupiter.exception.ExceptionEnum;
import com.qiein.jupiter.exception.RException;
import com.qiein.jupiter.util.DBSplitUtil;
import com.qiein.jupiter.web.dao.CostDao;
import com.qiein.jupiter.web.dao.SourceDao;
import com.qiein.jupiter.web.entity.po.CostLogPO;
import com.qiein.jupiter.web.entity.po.CostPO;
import com.qiein.jupiter.web.entity.po.SourcePO;
import com.qiein.jupiter.web.entity.vo.CostShowVO;
import com.qiein.jupiter.web.entity.vo.ZjsKzOfMonthOutVO;
import com.qiein.jupiter.web.repository.ZjskzOfMonthDao;
import com.qiein.jupiter.web.service.CostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CostServiceImpl implements CostService {
    @Autowired
    private CostDao costDao;
    @Autowired
    private ZjskzOfMonthDao zjskzOfMonthDao;
    @Autowired
    private SourceDao sourceDao;

    /**
     * 获取花费页面信息
     *
     * @param month
     * @param companyId
     * @return
     */
    public List<CostShowVO> costList(String month, Integer companyId, Integer staffId) {
        List<Map<String, Object>> dayList = zjskzOfMonthDao.getDayOfMonth(Integer.parseInt(month.split(CommonConstant.FILE_SEPARATOR)[0]), Integer.parseInt(month.split(CommonConstant.FILE_SEPARATOR)[1]), DBSplitUtil.getTable(TableEnum.info, companyId));
        List<SourcePO> srcList = sourceDao.findSourceByRole(companyId, staffId);
        List<CostShowVO> costShowVOS = new ArrayList<>();
        //将已知渠道先加入到对象中 构建对应数量的集合
        for (SourcePO sourcePO : srcList) {
            CostShowVO costShowVO = new CostShowVO();
            costShowVO.setSrcId(sourcePO.getId());
            costShowVO.setSrcImg(sourcePO.getSrcImg());
            costShowVO.setSrcName(sourcePO.getSrcName());
            costShowVOS.add(costShowVO);
        }
        //为每个集合构建对应空的 时间为key的map
        for (CostShowVO costShowVO : costShowVOS) {
            Map<String, BigDecimal> costMap = new HashMap<>();
            Map<String, BigDecimal> beforeCostMap = new HashMap<>();
            for (Map<String, Object> map : dayList) {
                beforeCostMap.put((String) map.get("day"), new BigDecimal(0));
                costMap.put((String) map.get("day"), new BigDecimal(0));
            }
            costShowVO.setBeforeCostMap(beforeCostMap);
            costShowVO.setCostMap(costMap);
        }
        //从数据库获取数据
        List<CostPO> list = costDao.costList(month, companyId, staffId);
        //将从数据库获取的数据根据对应的渠道和日期插入到实体类中
        for (CostShowVO costShowVO : costShowVOS) {
            for (CostPO costPO : list) {
                if (costPO.getSrcId().equals(costShowVO.getSrcId())) {
                    costShowVO.getBeforeCostMap().put(costPO.getCostTime(), costPO.getBeforeCost());
                    costShowVO.getCostMap().put(costPO.getCostTime(), costPO.getAfterCost());
                }
            }
        }
        CostShowVO hjcostShow = new CostShowVO();
        hjcostShow.setSrcName("合计");
        Map<String, BigDecimal> costMap = new HashMap<>();
        Map<String, BigDecimal> beforeCostMap = new HashMap<>();
        for (Map<String, Object> map : dayList) {
            beforeCostMap.put((String) map.get("day"), new BigDecimal(0));
            costMap.put((String) map.get("day"), new BigDecimal(0));
        }
        hjcostShow.setCostMap(costMap);
        hjcostShow.setBeforeCostMap(beforeCostMap);
        for (String costTime : costMap.keySet()) {
            BigDecimal cost = new BigDecimal(0);
            BigDecimal beforeCost = new BigDecimal(0);
            for (CostShowVO costShowVO : costShowVOS) {
                cost=cost.add(costShowVO.getCostMap().get(costTime));
                beforeCost=beforeCost.add(costShowVO.getBeforeCostMap().get(costTime));
            }
            hjcostShow.getCostMap().put(costTime, cost);
            hjcostShow.getBeforeCostMap().put(costTime, beforeCost);
        }
        costShowVOS.add(0,hjcostShow);
        for (CostShowVO costShowVO : costShowVOS) {
            BigDecimal cost = new BigDecimal(0);
            for (String costTime : costShowVO.getCostMap().keySet()) {
                cost=cost.add(costShowVO.getCostMap().get(costTime));
            }
            costShowVO.getCostMap().put("hj", cost);
            BigDecimal beforeCost = new BigDecimal(0);
            for (String costTime : costShowVO.getBeforeCostMap().keySet()) {
                beforeCost=beforeCost.add(costShowVO.getBeforeCostMap().get(costTime));
            }
            costShowVO.getBeforeCostMap().put("hj", beforeCost);
        }
        return costShowVOS;
    }

    /**
     * 添加花费
     *
     * @param costPO
     */
    public int insert(CostPO costPO) {
        int i = 0;
        try {
            costDao.insert(costPO);
        } catch (Exception e) {
            throw new RException(ExceptionEnum.EDIT_FAIL);
        }
        return i;
    }

    /**
     * 修改花费
     */
    public void editCost(CostPO costPO) {
        costDao.editCost(costPO);
    }

    /**
     * 添加花费日志
     */
    public void createCostLog(CostLogPO costLogPO) {
        costDao.createCostLog(costLogPO);
    }
}
