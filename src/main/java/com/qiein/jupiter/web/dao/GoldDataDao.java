package com.qiein.jupiter.web.dao;

import com.qiein.jupiter.web.entity.po.GoldFingerPO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 金手指表单
 * Author xiangliang 2018/6/13
 */
public interface GoldDataDao {
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
    void delete(@Param("id") Integer id);

    /**
     * 金数据表单页面显示
     * @param companyId
     * @return List<GoldFingerPO>
     */
    List<GoldFingerPO> select(@Param("companyId") Integer companyId);
}
