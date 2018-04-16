package com.qiein.jupiter.web.service;

import com.qiein.jupiter.web.entity.vo.GroupVO;

import java.util.List;

/**
 * 部门
 */
public interface GroupService {

    /**
     * 获取公司所有部门和小组
     *
     * @param companyId 公司id
     * @return
     */
    List<GroupVO> getCompanyAllDeptList(Integer companyId);

}
