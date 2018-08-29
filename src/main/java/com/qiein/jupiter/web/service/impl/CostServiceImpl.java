package com.qiein.jupiter.web.service.impl;

import com.qiein.jupiter.constant.CommonConstant;
import com.qiein.jupiter.enums.TableEnum;
import com.qiein.jupiter.exception.ExceptionEnum;
import com.qiein.jupiter.exception.RException;
import com.qiein.jupiter.util.DBSplitUtil;
import com.qiein.jupiter.util.TimeUtil;
import com.qiein.jupiter.web.dao.CostDao;
import com.qiein.jupiter.web.dao.SourceDao;
import com.qiein.jupiter.web.entity.po.CostLogPO;
import com.qiein.jupiter.web.entity.po.CostPO;
import com.qiein.jupiter.web.entity.po.ForDayPO;
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
                beforeCostMap.put((String) map.get("dayKey"), new BigDecimal(0));
                costMap.put((String) map.get("dayKey"), new BigDecimal(0));
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
                    costShowVO.getBeforeCostMap().put(costPO.getDayKey(), costPO.getBeforeCost());
                    costShowVO.getCostMap().put(costPO.getDayKey(), costPO.getAfterCost());
                }
            }
        }
        //获取合计类
        CostShowVO hjcostShow = new CostShowVO();
        hjcostShow.setSrcName("合计");
        hjcostShow.setSrcId(0);
        Map<String, BigDecimal> costMap = new HashMap<>();
        Map<String, BigDecimal> beforeCostMap = new HashMap<>();
        for (Map<String, Object> map : dayList) {
            beforeCostMap.put((String) map.get("dayKey"), new BigDecimal(0));
            costMap.put((String) map.get("dayKey"), new BigDecimal(0));
        }
        hjcostShow.setCostMap(costMap);
        hjcostShow.setBeforeCostMap(beforeCostMap);
        //合计计算
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
        //各个类的合计计算
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
     * 修改花费的返利率
     * @param srcIds
     * @param start
     * @param end
     * @param rate
     * @param companyId
     */
    public void editRate(String srcIds, Integer start, Integer end, BigDecimal rate,Integer companyId){
        costDao.editRate(srcIds,start,end,rate,companyId);
        List<ForDayPO> dayList=costDao.getDay(start,end);
        List<ForDayPO> forDayPOS=new ArrayList<>();
        for(ForDayPO forDayPO:dayList){
            for(String srcId:srcIds.split(CommonConstant.STR_SEPARATOR)){
                ForDayPO forDayPO1=new ForDayPO();
                forDayPO1.setDay(forDayPO.getDay());
                forDayPO1.setSrcId(srcId);
                forDayPO1.setRate(rate);
                forDayPO1.setCompanyId(companyId);
                forDayPOS.add(forDayPO1);
            }
        }
        List<ForDayPO> checkList=costDao.getSrcByDay(srcIds,start,end,companyId);
        for(int i=0;i<forDayPOS.size();i++){
            for(ForDayPO forDayPO:checkList){
                if(forDayPO.getDay().equals(forDayPOS.get(i).getDay())&&forDayPO.getSrcId().equals(forDayPOS.get(i).getSrcId())){
                    forDayPOS.remove(i);
                }
            }
        }
        costDao.insertRate(forDayPOS);

    }
    /**
     * 修改花费
     */
    public void editCost(CostPO costPO,CostLogPO costLogPO) {
        List<CostPO> list = costDao.getCostByDayAndSrc(costPO);
        if (list.size() == 0) {
            costPO.setRate(new BigDecimal(0));
            Integer id=costDao.insert(costPO);
            costLogPO.setCostId(id);
            costLogPO.setMemo("新增了"+ TimeUtil.intMillisToTimeStr(costPO.getTime(),TimeUtil.ymdSDF_)+"花费为："+costPO.getAfterCost() );
            costDao.createCostLog(costLogPO);
        } else {
            costPO.setId(list.get(0).getId());
            costDao.editCost(costPO);
            costLogPO.setMemo("修改了"+TimeUtil.intMillisToTimeStr(costPO.getTime(),TimeUtil.ymdSDF_)+"花费为："+costPO.getAfterCost() );
            costLogPO.setCostId(costPO.getId());
            costDao.createCostLog(costLogPO);
        }
    }
}
