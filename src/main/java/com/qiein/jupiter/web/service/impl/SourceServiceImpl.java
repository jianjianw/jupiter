package com.qiein.jupiter.web.service.impl;

import com.qiein.jupiter.exception.ExceptionEnum;
import com.qiein.jupiter.exception.RException;
import com.qiein.jupiter.web.dao.SourceDao;
import com.qiein.jupiter.web.entity.po.SourcePO;
import com.qiein.jupiter.web.entity.vo.SourceVO;
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
     * 根据来源编号批量删除来源
     *
     * @param ids
     * @param companyId
     */
    @Override
    public void datDelSrc(String ids, Integer companyId) {
        //删除前需要检查来源下是否存在客资,为空才可删除
        //TODO
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
}
