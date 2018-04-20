package com.qiein.jupiter.web.service.impl;

import com.qiein.jupiter.enums.ShopTypeEnum;
import com.qiein.jupiter.exception.ExceptionEnum;
import com.qiein.jupiter.exception.RException;
import com.qiein.jupiter.web.dao.ShopDao;
import com.qiein.jupiter.web.entity.po.ShopPO;
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

    /**
     * 新增拍摄地
     *
     * @param shopPO
     */
    public void addShop(ShopPO shopPO) {
        //1.查重
        ShopPO exist = shopDao.getShopByName(shopPO.getCompanyId(), shopPO.getShopName());
        if (exist != null) {
            throw new RException(ExceptionEnum.SHOP_EXIST);
        }
        //2.新增
        shopPO.setTypeId(ShopTypeEnum.SHOOTING.getShopType());
        shopPO.setShowFlag(true);
        shopDao.insert(shopPO);
    }
}
