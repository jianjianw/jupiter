package com.qiein.jupiter.web.service.impl;

import com.qiein.jupiter.exception.ExceptionEnum;
import com.qiein.jupiter.exception.RException;
import com.qiein.jupiter.web.dao.ChannelDao;
import com.qiein.jupiter.web.entity.po.ChannelPO;
import com.qiein.jupiter.web.service.ChannelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChannelServiceImpl implements ChannelService {

    @Autowired
    private ChannelDao channelDao;

    /**
     * 新增渠道
     *
     * @param channelPO
     */
    @Override
    public void createChannel(ChannelPO channelPO) {
        //检查是否存在同名渠道
        if (channelDao.checkChannel(channelPO.getChannelName(), channelPO.getCompanyId()) >= 1) {
            throw new RException(ExceptionEnum.CHANNEL_NAME_REPEAT);
        }
        channelDao.insert(channelPO);
    }

    /**
     * 编辑渠道
     *
     * @param channelPO
     */
    @Override
    public void editChannel(ChannelPO channelPO) {
        //检查是否存在同名渠道
        if (channelDao.checkChannel(channelPO.getChannelName(), channelPO.getCompanyId()) >= 1) {
            throw new RException(ExceptionEnum.CHANNEL_NAME_REPEAT);
        }
        channelDao.update(channelPO);
    }

    /**
     * 删除渠道
     *
     * @param id        渠道编号
     * @param companyId 所属公司编号
     */
    @Override
    public void delChannel(Integer id, Integer companyId) {
        //删除前需要检查渠道下属是否还存在来源
        //TODO
        channelDao.deleteByIdAndCid(id, companyId);
    }

    /**
     * 获取渠道列表
     *
     * @param typeIds
     * @param companyId
     * @return
     */
    @Override
    public List<ChannelPO> getChannelList(List<Integer> typeIds, Integer companyId) {
        return channelDao.getChannelList(companyId, typeIds);
    }

    /**
     * 根据渠道细分类型获取渠道
     * 1:纯电商，2:电商转介绍，3:员工转介绍，4:指名转介绍，5:外部转介绍，6:自然入客，7:门店外展
     *
     * @param typeId
     * @param companyId
     * @return
     */
    @Override
    public List<ChannelPO> getListByType(Integer typeId, Integer companyId) {
        return channelDao.getListByType(companyId, typeId);
    }

}
