package com.qiein.jupiter.web.service.impl;

import com.qiein.jupiter.enums.RoleChannelEnum;
import com.qiein.jupiter.exception.ExceptionEnum;
import com.qiein.jupiter.exception.RException;
import com.qiein.jupiter.util.CollectionUtils;
import com.qiein.jupiter.util.DBSplitUtil;
import com.qiein.jupiter.util.StringUtil;
import com.qiein.jupiter.web.dao.ChannelDao;
import com.qiein.jupiter.web.dao.ShopChannelGroupDao;
import com.qiein.jupiter.web.dao.SourceDao;
import com.qiein.jupiter.web.entity.po.ChannelPO;
import com.qiein.jupiter.web.entity.vo.ChannelVO;
import com.qiein.jupiter.web.entity.vo.SrcListVO;
import com.qiein.jupiter.web.service.ChannelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

@Service
public class ChannelServiceImpl implements ChannelService {

    @Autowired
    private ChannelDao channelDao;

    @Autowired
    private SourceDao sourceDao;

    @Autowired
    private ShopChannelGroupDao shopChannelGroupDao;

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
    @Transactional
    public void editChannel(ChannelPO channelPO) {
        if (StringUtil.isNotEmpty(channelPO.getBrandName())) {   //如果名字为空，可能只是想改显示
            //检查是否存在
            ChannelPO cp = channelDao.getByIdAndCid(channelPO.getId(), channelPO.getCompanyId());
            if (cp == null) {  //如果为空，则该编辑的渠道不存在
                throw new RException(ExceptionEnum.SOURCE_NOT_FOUND);
            } else { //如果存在，则继续判断
                if (!cp.getChannelName().equals(channelPO.getChannelName())) {    //如果得到的名字和编辑的名字不相同则校验同名
                    //检查是否存在同名渠道
                    if (channelDao.checkChannel(channelPO.getChannelName(), channelPO.getCompanyId()) >= 1) {
                        throw new RException(ExceptionEnum.CHANNEL_NAME_REPEAT);
                    }
                    sourceDao.updateChannelName(channelPO.getId(), channelPO.getChannelName(), channelPO.getCompanyId());
                }
            }
        }
        channelDao.update(channelPO);
    }

    /**
     * 交换排序
     *
     * @param fId
     * @param fPriority
     * @param sId
     * @param sPriority
     */
    @Override
    @Transactional
    public void editProiority(Integer fId, Integer fPriority, Integer sId, Integer sPriority, Integer companyId) {
        channelDao.updatePriority(fId, fPriority, companyId);
        channelDao.updatePriority(sId, sPriority, companyId);
    }

    /**
     * 删除渠道
     *
     * @param id        渠道编号
     * @param companyId 所属公司编号
     */
    @Override
    @Transactional
    public void delChannel(Integer id, Integer companyId) {
        //删除前需要检查渠道下属是否还存在来源
        if (channelDao.checkSrcNumById(id, companyId) > 0)
            throw new RException(ExceptionEnum.CHANNEL_HAVE_SOURCE);
        //hm_crm_shop_channel_group_rela删除时关联删除该表信息
        channelDao.deleteByIdAndCid(id, companyId);
        shopChannelGroupDao.delByChannelId(companyId,id);
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

    /**
     * 获取企业各角色页面，头部渠道组及渠道下拉框筛选
     *
     * @param companyId
     * @param role
     * @return
     */
    public List<ChannelVO> getChannelSourceListByType(Integer companyId, String role) {
        return channelDao.getChannelSourceListByType(companyId, RoleChannelEnum.getTypeListByRole(role));
    }

    /**
     * 获取员工各角色录入页面，渠道来源下拉框选项，根据个人上月使用频率排序
     *
     * @param companyId
     * @param role
     * @return
     */
    public List<ChannelVO> getMyChannelSourceByRole(int companyId, int staffId, String role) {
        List<ChannelVO> list = channelDao.getChannelSourceListByType(companyId, RoleChannelEnum.getTypeListByRole(role));
        return getMySortSources(list, companyId, staffId, role);
    }

    // 自定义排序，返回渠道集合，根据上月录入渠道的频率进行倒序
    private List<ChannelVO> getMySortSources(List<ChannelVO> sourceGroups, Integer companyId, Integer staffId, String role) {
        List<ChannelVO> result = new LinkedList<>();
        List<SrcListVO> sourceResult = null;
        List<Integer> channelList = channelDao.getLastMonthChannelSort(DBSplitUtil.getInfoTabName(companyId), companyId, staffId, RoleChannelEnum.getTypeListByRole(role));
        if (CollectionUtils.isNotEmpty(channelList)) {
            // 1.渠道组排序
            for (Integer channelId : channelList) {
                // 循环渠道集合
                Iterator<ChannelVO> it = sourceGroups.iterator();
                while (it.hasNext()) {
                    ChannelVO channelVO = it.next();
                    if (channelVO.getChannelId() == channelId) {
                        result.add(channelVO);
                        it.remove();
                    }
                }
            }
            // 剩余没用到过的渠道全部放入
            result.addAll(sourceGroups);
            // 2.渠道排序
            for (ChannelVO channelVO : result) {
                if (CollectionUtils.isNotEmpty(channelVO.getSrcList())) {
                    List<Integer> srcIdList = sourceDao.getLastMonthSrcSort(DBSplitUtil.getInfoTabName(companyId), companyId, staffId, RoleChannelEnum.getTypeListByRole(role));
                    if (CollectionUtils.isNotEmpty(srcIdList)) {
                        sourceResult = new LinkedList<>();
                        for (Integer srcId : srcIdList) {
                            Iterator<SrcListVO> it = channelVO.getSrcList().iterator();
                            while (it.hasNext()) {
                                SrcListVO src = it.next();
                                if (src.getSrcId() == srcId) {
                                    sourceResult.add(src);
                                    it.remove();
                                }
                            }
                        }
                        sourceResult.addAll(channelVO.getSrcList());
                        channelVO.setSrcList(sourceResult);
                    }
                }
            }
            return result;
        }
        return sourceGroups;
    }


}
