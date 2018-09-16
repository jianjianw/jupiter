package com.qiein.jupiter.web.service.impl;

import com.qiein.jupiter.constant.CommonConstant;
import com.qiein.jupiter.exception.ExceptionEnum;
import com.qiein.jupiter.exception.RException;
import com.qiein.jupiter.util.CollectionUtils;
import com.qiein.jupiter.util.NumUtil;
import com.qiein.jupiter.util.StringUtil;
import com.qiein.jupiter.web.dao.RoleSourceDao;
import com.qiein.jupiter.web.service.RoleSourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * FileName: RoleSourceServiceImpl
 *
 * @author: yyx
 * @Date: 2018-7-18 18:51
 */
@Service
public class RoleSourceServiceImpl implements RoleSourceService {
    @Autowired
    private RoleSourceDao roleSourceDao;

    @Override
    public void insert(Integer roleId, String sourceId, Integer companyId) {
        //参数校验
        if (NumUtil.isInValid(roleId)) {
            throw new RException(ExceptionEnum.ROLE_ID_IS_NULL);
        }


        List<String> sourceIds = Arrays.asList(sourceId.split(CommonConstant.STR_SEPARATOR));
        if (CollectionUtils.isNotEmpty(sourceIds)) {
            roleSourceDao.delete(roleId);
            roleSourceDao.batchAddRoleSource(roleId, sourceIds, companyId);
        }
    }
}
