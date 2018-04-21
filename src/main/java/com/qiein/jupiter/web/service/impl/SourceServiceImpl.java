package com.qiein.jupiter.web.service.impl;

import com.qiein.jupiter.exception.ExceptionEnum;
import com.qiein.jupiter.exception.RException;
import com.qiein.jupiter.web.dao.SourceDao;
import com.qiein.jupiter.web.entity.po.SourcePO;
import com.qiein.jupiter.web.service.SourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
            throw new RException(ExceptionEnum.CHANNEL_NAME_REPEAT);
        sourceDao.insert(sourcePO);
    }

    /**
     * 编辑来源
     *
     * @param sourcePO
     */
    @Override
    public void editSource(SourcePO sourcePO) {
        //先检查是否重名
        if (sourceDao.getByIdAndCid(sourcePO.getId(), sourcePO.getCompanyId()) != null && !sourceDao.getByIdAndCid(sourcePO.getId(), sourcePO.getCompanyId())
                .getSrcName().equals(sourcePO.getSrcName())) {   //如果名字改了，则校验重名
            if (sourceDao.checkSource(sourcePO.getSrcName(), sourcePO.getChannelId(), sourcePO.getCompanyId()) >= 1)
                throw new RException(ExceptionEnum.SOURCE_NAME_REPEAT);
        }else {
            throw new RException(ExceptionEnum.SOURCE_NOT_FOUND);
        }
        sourceDao.update(sourcePO);
    }

    /**
     * 删除来源
     *
     * @param id
     * @param companyId
     */
    @Override
    public void delSourceById(Integer id, Integer companyId) {
        //删除前需要检查来源下是否存在客资,为空才可删除
        //TODO
        sourceDao.deleteByIdAndCid(id, companyId);
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
}
