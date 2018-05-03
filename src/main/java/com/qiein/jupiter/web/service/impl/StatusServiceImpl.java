package com.qiein.jupiter.web.service.impl;

import com.qiein.jupiter.web.dao.StatusDao;
import com.qiein.jupiter.web.entity.po.StatusPO;
import com.qiein.jupiter.web.service.StatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatusServiceImpl implements StatusService {

    @Autowired
    private StatusDao statusDao;

    /**
     * 获取企业状态列表
     *
     * @param companyId
     * @return
     */
    public List<StatusPO> getCompanyStatusList(int companyId) {
        return statusDao.getCompanyStatusList(companyId);
    }

    /**
     * 编辑状态
     *
     * @param statusPO
     * @return
     */
    public void editStatus(StatusPO statusPO) {
        statusDao.editStatus(statusPO);
    }
}
