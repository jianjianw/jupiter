package com.qiein.jupiter.web.service.impl;

import com.qiein.jupiter.web.dao.ShopDao;
import com.qiein.jupiter.web.entity.vo.ShopVO;
import com.qiein.jupiter.web.service.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 拍摄地业务层
 */
@Service
public class ShopServiceImpl implements ShopService {

    @Autowired
    private ShopDao shopDao;//拍摄地持久层

    /**
     * 获取企业所有拍摄地列表
     *
     * @param companyId
     * @return
     */
    public List<ShopVO> getCompanyShopList(int companyId) {
        return shopDao.getCompanyShopList(companyId);
    }
}
