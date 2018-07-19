package com.qiein.jupiter.web.service;

/**
 * FileName: RoleSourceService
 *
 * @author: yyx
 * @Date: 2018-7-18 18:51
 */
public interface RoleSourceService {
    /**
     * 插入
     * @param roleId
     * @param sourceId
     * @param companyId
     * */
    void insert(Integer roleId, String sourceId,Integer companyId);
}
