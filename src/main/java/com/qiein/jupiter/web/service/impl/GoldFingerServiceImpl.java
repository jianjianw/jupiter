package com.qiein.jupiter.web.service.impl;

import com.alibaba.druid.support.json.JSONUtils;
import com.qiein.jupiter.web.dao.GoldFingerDao;
import com.qiein.jupiter.web.entity.po.GoldFingerPO;
import com.qiein.jupiter.web.service.GoldFingerService;
import org.springframework.beans.factory.annotation.Autowired;

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
        goldFingerPO.setRemark(JSONUtils.toJSONString(goldFingerPO.getMap()));
        goldFingerDao.insert(goldFingerPO);
    }
    /**
     * 修改表单
     * @param goldFingerPO
     */
    public void update(GoldFingerPO goldFingerPO){
        goldFingerPO.setRemark(JSONUtils.toJSONString(goldFingerPO.getMap()));
        goldFingerDao.update(goldFingerPO);
    }
    /**
     * 删除表单
     * @param id
     */
    public void delete(Integer id){
        goldFingerDao.delete(id);
    }
}
