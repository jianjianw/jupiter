package com.qiein.jupiter.web.service;

import com.qiein.jupiter.web.entity.po.GoldFingerPO;
import org.springframework.stereotype.Service;
/**
 * 金手指表单
 * Author xiangliang 2018/6/13
 */
@Service
public interface GoldFingerService {
    /**
     * 添加表单
     * @param goldFingerPO
     */
    void insert(GoldFingerPO goldFingerPO);

    /**
     * 修改表单
     * @param goldFingerPO
     */
    void update(GoldFingerPO goldFingerPO);
    /**
     * 删除表单
     * @param id
     */
    void delete(Integer id);
}
