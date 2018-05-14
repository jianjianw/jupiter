package com.qiein.jupiter.web.service.impl;

import com.qiein.jupiter.exception.ExceptionEnum;
import com.qiein.jupiter.exception.RException;
import com.qiein.jupiter.web.dao.SourceDao;
import com.qiein.jupiter.web.entity.po.SourcePO;
import com.qiein.jupiter.web.entity.vo.SourceVO;
import com.qiein.jupiter.web.service.SourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SourceServiceImpl implements SourceService {
    @Autowired
    private SourceDao sourceDao;

    /**
     * 新增来源
     *
     * @param sourcePO
     */
    @Override
    public void createSource(SourcePO sourcePO) {
        //先检查是否重名
        if (sourceDao.checkSource(sourcePO.getSrcName(), sourcePO.getChannelId(), sourcePO.getCompanyId()) >= 1)
            throw new RException(ExceptionEnum.SOURCE_NAME_REPEAT);
        sourceDao.insert(sourcePO);
    }

    /**
     * 批量编辑来源
     *
     * @param sourceVO
     */
    @Override
    public void editSource(SourceVO sourceVO) {
        //先检查是否有多个id
        String[] ids = sourceVO.getIds().split(",");
        if (ids.length == 1) {   //则说明只有一个id
            sourceVO.setId(Integer.valueOf(ids[0]));
            //先检查是否重名
            //先根据id去获取来源信息
            SourcePO s = sourceDao.getByIdAndCid(sourceVO.getId(), sourceVO.getCompanyId());

            if (s != null) {   //如果不为空说明存在
                if (!s.getSrcName().equals(sourceVO.getSrcName())) { //名字是否改过
                    if (sourceDao.checkSource(sourceVO.getSrcName(), s.getChannelId(), sourceVO.getCompanyId()) >= 1)
                        throw new RException(ExceptionEnum.SOURCE_NAME_REPEAT);
                }
            } else {
                throw new RException(ExceptionEnum.SOURCE_NOT_FOUND);
            }
        } else {  //  批量编辑
            sourceDao.datUpdate(sourceVO, ids);
        }

        sourceDao.update(sourceVO);
    }

    /**
     * 来源拖拽排序
     *
     * @param fPriority
     * @param sPriority
     * @param id
     * @param companyId
     */
    @Override
    @Transactional
    public void editSourcePriority(Integer fPriority, Integer sPriority, Integer id, Integer companyId) {
        if (fPriority > sPriority) {   //向上拖拽，所有+1
            sourceDao.updateUpPriority(sPriority, fPriority, companyId);
        } else { //向下拖拽，所有-1
            sourceDao.updateDownPriority(fPriority, sPriority, companyId);
        }
        sourceDao.updatePriority(id, sPriority, companyId);
    }

    /**
     * 删除来源
     *
     * @param id
     * @param companyId
     */
    @Override
    public void delSourceById(Integer id, Integer companyId) {
        //TODO  删除前需要检查来源下是否存在客资,为空才可删除
        sourceDao.deleteByIdAndCid(id, companyId);
    }

    /**
     * 根据来源编号批量删除来源
     *
     * @param ids
     * @param companyId
     */
    @Override
    public void datDelSrc(String ids, Integer companyId) {
        //TODO  删除前需要检查来源下是否存在客资,为空才可删除
        String[] idArr = ids.split(",");
        sourceDao.datDelete(idArr, companyId);
    }

    /**
     * 根据渠道编号获取下属来源列表
     *
     * @param channelId
     * @param companyId
     * @return
     */
    @Override
    public List<SourcePO> getSourceListByChannelId(Integer channelId, Integer companyId) {
        return sourceDao.getSourceListByChannelId(channelId, companyId);
    }

    /**
     * 获取公司所有的来源Map key 为id  value 为图片地址
     *
     * @param companyId
     * @return
     */
    @Override
    public Map<String, String> getSourcePageMap(int companyId) {
        //获取所有
        List<SourcePO> allSourceList = sourceDao.getAllSourceList(companyId);
        Map<String, String> pageDictMap = new HashMap<>();
        for (SourcePO sourcePO : allSourceList) {
            //Id,图片地址
            pageDictMap.put(String.valueOf(sourcePO.getId()), sourcePO.getSrcImg());
        }
        return pageDictMap;
    }
}
