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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 拍摄地业务层
 */
@Service
public class ShopServiceImpl implements ShopService {

    @Autowired
    private ShopDao shopDao;

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

    /**
     * 编辑拍摄地
     *
     * @param shopPO
     */
    public void editShop(ShopPO shopPO) {
        //1.查重
        ShopPO exist = shopDao.getShopByName(shopPO.getCompanyId(), shopPO.getShopName());
        if (exist != null && exist.getId() != shopPO.getId()) {
            throw new RException(ExceptionEnum.SHOP_EXIST);
        }
        //2.修改
        shopDao.update(shopPO);
    }

    /**
     * 编辑拍摄地排序
     * @param fId
     * @param fPriority
     * @param sId
     * @param sPriority
     * @param companyId
     */
    @Override
    @Transactional
    public void editPriority(Integer fId, Integer fPriority, Integer sId, Integer sPriority, Integer companyId) {
        shopDao.updatePriority(fId, fPriority, companyId);
        shopDao.updatePriority(sId, sPriority, companyId);
    }

    /**
     * 编辑拍摄地开关
     *
     * @param companyId
     * @param id
     * @param showFlag
     * @return
     */
    public void editShopShowFlag(int companyId, int id, boolean showFlag) {
        shopDao.editShopShowFlag(companyId, id, showFlag);
    }

    /**
     * 删除拍摄地
     *
     * @param companyId
     * @param id
     */
    public void deleteShop(int companyId, int id) {
        //TODO 校验是否有该拍摄地的客资
        shopDao.deleteShop(companyId, id);
    }

    /**
     * 获取企业显示的拍摄地列表
     *
     * @param companyId
     * @return
     */
    public List<ShopVO> getShowShopList(int companyId) {
        return shopDao.getShowShopList(companyId);
    }

}
