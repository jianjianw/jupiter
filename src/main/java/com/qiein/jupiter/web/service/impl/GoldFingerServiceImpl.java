package com.qiein.jupiter.web.service.impl;

import com.qiein.jupiter.web.dao.GoldFingerDao;
import com.qiein.jupiter.web.entity.po.GoldFingerPO;
import com.qiein.jupiter.web.service.GoldFingerService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 金手指表单
 * Author xiangliang 2018/6/13
 */
public class GoldFingerServiceImpl implements GoldFingerService {

    @Autowired
    private GoldFingerDao goldFingerDao;
    /**
     * 添加表单
     * @param goldFingerPO
     */
    public void insert(GoldFingerPO goldFingerPO){
        goldFingerDao.insert(goldFingerPO);
    }
    /**
     * 修改表单
     * @param goldFingerPO
     */
    public void update(GoldFingerPO goldFingerPO){
        goldFingerDao.update(goldFingerPO);
    }
    /**
     * 删除表单
     * @param id
     */
    public void delete(Integer id){
        goldFingerDao.delete(id);
    }
    /**
     * 金数据表单页面显示
     * @param companyId
     * @return
     */
    public List<GoldFingerPO> select(Integer companyId){
        return goldFingerDao.select(companyId);

    }
}
