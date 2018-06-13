package com.qiein.jupiter.web.service.impl;

import com.qiein.jupiter.web.dao.GoldDataDao;
import com.qiein.jupiter.web.entity.po.GoldFingerPO;
import com.qiein.jupiter.web.service.GoldDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 金手指表单
 * Author xiangliang 2018/6/13
 */
@Service
public class GoldDataServiceImpl implements GoldDataService {

    @Autowired
    private GoldDataDao goldDataDao;
    /**
     * 添加表单
     * @param goldFingerPO
     */
    public void insert(GoldFingerPO goldFingerPO){
        goldDataDao.insert(goldFingerPO);
    }
    /**
     * 修改表单
     * @param goldFingerPO
     */
    public void update(GoldFingerPO goldFingerPO){
        goldDataDao.update(goldFingerPO);
    }
    /**
     * 删除表单
     * @param id
     */
    public void delete(Integer id){
        goldDataDao.delete(id);
    }
    /**
     * 金数据表单页面显示
     * @param companyId
     * @return
     */
    public List<GoldFingerPO> select(Integer companyId){
        return goldDataDao.select(companyId);

    }
}
