package com.qiein.jupiter.web.service.impl;

import com.qiein.jupiter.web.dao.CommonTypeDao;
import com.qiein.jupiter.web.entity.po.CommonTypePO;
import com.qiein.jupiter.web.service.CommonTypeSerivce;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;

/**
 * 拍摄类型渠道
 * Author xiangliang 2018/6/15
 */
@Service
public class CommonTypeSerivceImpl implements CommonTypeSerivce {
    @Autowired
    private CommonTypeDao commonTypeDao;
    /**
     * 添加拍摄类型
     * @param commonType
     * @param companyId
     */
    public void addPhotoType(String commonType,Integer companyId){
        commonTypeDao.addPhotoType(commonType,companyId);
    }

    /**
     * 为每个小组分配渠道
     * @param commonTypePO
     */
     public void addTypeChannelGroup(CommonTypePO commonTypePO){
         String[] channelIds=commonTypePO.getChannelIds().split(",");
         String[] groupIds=commonTypePO.getGroupId().split(",");
         String[] groupNames=commonTypePO.getGroupName().split(",");
         List<CommonTypePO> list=new ArrayList<>();
         for(String channelId:channelIds){
             for(int i=0;i<groupIds.length;i++){
                 CommonTypePO type=new CommonTypePO();
                 type.setChannelId(Integer.parseInt(channelId));
                 type.setGroupId(groupIds[i]);
                 type.setGroupName(groupNames[i]);
                 type.setCompanyId(commonTypePO.getCompanyId());
                 type.setWeight(commonTypePO.getWeight());
                 type.setTypeId(commonTypePO.getTypeId());
                 list.add(type);
             }
         }
         commonTypeDao.addTypeChannelGroup(list);
     }
    /**
     * 批量删除
     */
    public void deleteTypeChannelGroup(String ids){
        List<Integer> list=new ArrayList<>();
        for(String id:ids.split(",")){
            list.add(Integer.parseInt(id));
        }
        commonTypeDao.deleteTypeChannelGroup(list);
    }
    /**
     * 修改渠道
     * @param commonTypePO
     */
    public void editTypeChannelGroup( CommonTypePO commonTypePO){
        commonTypeDao.editTypeChannelGroup(commonTypePO);
    }

}
