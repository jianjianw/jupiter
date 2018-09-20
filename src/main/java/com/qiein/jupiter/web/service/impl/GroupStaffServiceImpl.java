package com.qiein.jupiter.web.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.qiein.jupiter.constant.CommonConstant;
import com.qiein.jupiter.exception.ExceptionEnum;
import com.qiein.jupiter.exception.RException;
import com.qiein.jupiter.util.CollectionUtils;
import com.qiein.jupiter.util.NumUtil;
import com.qiein.jupiter.util.ResultInfoUtil;
import com.qiein.jupiter.util.StringUtil;
import com.qiein.jupiter.web.dao.GroupStaffDao;
import com.qiein.jupiter.web.entity.po.GroupStaffPO;
import com.qiein.jupiter.web.entity.po.StaffPO;
import com.qiein.jupiter.web.entity.vo.StaffVO;
import com.qiein.jupiter.web.service.GroupStaffService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * FileName: GroupStaffServiceImpl
 *
 * @author: yyx
 * @Date: 2018-6-27 10:11
 */
@Service
public class GroupStaffServiceImpl implements GroupStaffService {
    @Resource
    private GroupStaffDao groupStaffDao;

    @Override
    public void insert(JSONObject jsonObject, StaffPO staffPO) {
        //参数校验
        if (null == jsonObject) {
            throw new RException(ExceptionEnum.UNKNOW_ERROR);
        }
        String staffIds = jsonObject.getString("staffIds");
        String groupId = jsonObject.getString("groupId");

        if (StringUtil.isEmpty(staffIds) || StringUtil.isEmpty(groupId)) {
            throw new RException(ExceptionEnum.LOSE_FILED);
        }
        //验证groupid
        if (groupId.split(CommonConstant.ROD_SEPARATOR).length != CommonConstant.GROUP_ID_LENGTH) {
            throw new RException(ExceptionEnum.ADD_FAIL);
        }
        //切割
        String[] idsStr = staffIds.split(CommonConstant.STR_SEPARATOR);
        List<String> ids = new ArrayList<>();
        List<Integer> existsIds = groupStaffDao.getGroupStaffIdsStrByCompanyIdAndGroupId(staffPO.getCompanyId(), groupId);

        if (CollectionUtils.isNotEmpty(existsIds)) {
            for (String id : idsStr) {
                if (!existsIds.contains(Integer.parseInt(id))) {
                    ids.add(id);
                }
            }
            if (CollectionUtils.isNotEmpty(ids)) {
                groupStaffDao.batchInsertGroupStaff(staffPO.getCompanyId(), groupId, ids.toArray(new String[]{}));
            } else {
                throw new RException(ExceptionEnum.GROUP_STAFF_EXISTS);
            }
        } else {
            groupStaffDao.batchInsertGroupStaff(staffPO.getCompanyId(), groupId, idsStr);
        }
    }

    @Override
    public void remove(GroupStaffPO groupStaffPO) {
        if (NumUtil.isNull(groupStaffPO.getStaffId()) || StringUtil.isEmpty(groupStaffPO.getGroupId())) {
            throw new RException(ExceptionEnum.LOSE_FILED);
        }
        List<GroupStaffPO> groupStaffPOS = groupStaffDao.getGroupStaffByStaffId(groupStaffPO.getCompanyId(), groupStaffPO.getStaffId());
        //只存在一个组
        if (CollectionUtils.isEmpty(groupStaffPOS) || !(groupStaffPOS.size() > CommonConstant.DEFAULT_ONE)) {
            throw new RException(ExceptionEnum.UNKNOW_ERROR);
        }
        //移除关系
        Integer rows = groupStaffDao.deleteByStaffIdAndGroupId(groupStaffPO.getStaffId(), groupStaffPO.getCompanyId(), groupStaffPO.getGroupId());
        if (!(rows > CommonConstant.DEFAULT_ZERO)) {
            throw new RException(ExceptionEnum.DELETE_FAIL);
        }
    }

}
