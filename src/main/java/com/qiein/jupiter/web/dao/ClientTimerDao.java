package com.qiein.jupiter.web.dao;

import com.qiein.jupiter.web.entity.po.ClientTimerPO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 客资追踪提醒
 *
 * @Author: shiTao
 */
public interface ClientTimerDao {
    /**
     * 获取所有要提醒的列表
     *
     * @return
     */
    List<ClientTimerPO> getAll();

    /**
     * 批量删除已经提醒过的
     */
    int batchDelAready(@Param("ids") Integer[] ids);

    /**
     * 单独删除
     *
     * @param id
     * @return
     */
    int delAready(@Param("id") Integer id);
}
