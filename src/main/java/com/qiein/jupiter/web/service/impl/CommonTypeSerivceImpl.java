package com.qiein.jupiter.web.service.impl;

import com.qiein.jupiter.constant.CommonConstant;
import com.qiein.jupiter.exception.ExceptionEnum;
import com.qiein.jupiter.exception.RException;
import com.qiein.jupiter.web.dao.CommonTypeDao;
import com.qiein.jupiter.web.entity.po.CommonTypePO;
import com.qiein.jupiter.web.entity.vo.CommonTypeChannelShowVO;
import com.qiein.jupiter.web.entity.vo.CommonTypeChannelVO;
import com.qiein.jupiter.web.entity.vo.CommonTypeVO;
import com.qiein.jupiter.web.service.CommonTypeSerivce;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
         String[] channelIds=commonTypePO.getChannelIds().split(CommonConstant.STR_SEPARATOR);
         String[] groupIds=commonTypePO.getGroupId().split(CommonConstant.STR_SEPARATOR);
         String[] groupNames=commonTypePO.getGroupName().split(CommonConstant.STR_SEPARATOR);
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
         try {
             commonTypeDao.addTypeChannelGroup(list);
         }catch(Exception e){
             throw new RException(ExceptionEnum.GROUP_NAME_REPEAT);
         }
     }
    /**
     * 批量删除
     */
    public void deleteTypeChannelGroup(String ids){
        List<Integer> list=new ArrayList<>();
        for(String id:ids.split(CommonConstant.STR_SEPARATOR)){
            list.add(Integer.parseInt(id));
        }
        commonTypeDao.deleteTypeChannelGroup(list);
    }
    /**
     * 修改渠道
     * @param commonTypePO
     */
    public void editTypeChannelGroup( CommonTypePO commonTypePO){
        try {
            commonTypeDao.editTypeChannelGroup(commonTypePO);
        }catch(Exception e){
            throw new RException(ExceptionEnum.GROUP_NAME_REPEAT);
        }

    }

    /**
     * 获取拍摄地渠道小组分类
     * @param typeId
     * @return
     */
    public List<CommonTypeChannelVO> findChannelGroup(Integer typeId, Integer companyId){
        List<CommonTypeChannelVO>  channelList=commonTypeDao.findChannelGroup(typeId,companyId);
        return channelList;
    }
    /**
     * 查询拍摄类型
     * @param companyId
     * @return
     */
   public List<CommonTypeVO> findCommonType(Integer companyId){
        return commonTypeDao.findCommonType(companyId);
    }

    /**
     * 根据来源id删除
     * @param channelId
     * @param typeId
     * @param companyId
     */
    public void deleteByChannelId(Integer channelId,Integer typeId,Integer companyId){
        commonTypeDao.deleteByChannelId(channelId,typeId,companyId);
    }
    /**
     * 搜索小组
     * @param channelId
     * @param typeId
     * @param companyId
     * @param groupName
     * @return
     */
    public List<CommonTypePO> searchByChannelId(Integer channelId,Integer typeId,Integer companyId,String groupName){
        return commonTypeDao.searchByChannelId(channelId,typeId,companyId,groupName);
    }

    /**
     * 批量修改
     * @param commonTypePO
     */
    public void editWeight(String ids, Integer weight){
        List<Integer> list=new ArrayList<>();
        for(String id:ids.split(CommonConstant.STR_SEPARATOR)){
            list.add(Integer.parseInt(id));
        }
        commonTypeDao.editWeight(list,weight);
    }

}
