package com.qiein.jupiter.web.service.impl;

import com.qiein.jupiter.web.dao.ShopChannelGroupDao;
import com.qiein.jupiter.web.entity.vo.ShopChannelGroupVO;
import com.qiein.jupiter.web.service.ShopChannelGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShopChannelGroupServiceImpl implements ShopChannelGroupService {

    @Autowired
    private ShopChannelGroupDao shopChannelGroupDao;//拍摄地，渠道，小组关联

    /**
     * 获取拍摄地，渠道，小组关联集合
     *
     * @param companyId
     * @param shopId
     * @return
     */
    public List<ShopChannelGroupVO> getShopChannelGroupList(int companyId, int shopId) {
        return shopChannelGroupDao.getShopChannelGroupList(companyId, shopId);
    }
}
