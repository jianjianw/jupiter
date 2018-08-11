package com.qiein.jupiter.web.service;

import java.util.List;

/**
 * 快捷备注
 *
 * @author gaoxiaoli 2018/8/11
 */

public interface FastMemoService {

    /**
     * 获取个人快捷备注列表
     *
     * @param staffId
     * @param companyId
     * @return
     */
    List<String> getMemoListById(int staffId, int companyId);

    /**
     * 添加快捷备注
     *
     * @param staffId
     * @param companyId
     * @param memo
     * @return
     */
    void addFastMemo(int staffId, int companyId, String memo);
}
