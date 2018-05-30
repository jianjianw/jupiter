package com.qiein.jupiter.web.dao;

import com.qiein.jupiter.web.entity.po.ClientTimerPO;

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
    int batchDelAready(Integer[] ids);
}
