package com.qiein.jupiter.web.service.impl;

import com.qiein.jupiter.enums.ShopTypeEnum;
import com.qiein.jupiter.exception.ExceptionEnum;
import com.qiein.jupiter.exception.RException;
import com.qiein.jupiter.web.dao.ClientInfoDao;
import com.qiein.jupiter.web.dao.ShopDao;
import com.qiein.jupiter.web.entity.po.ShopPO;
import com.qiein.jupiter.web.entity.vo.ShopDictVO;
import com.qiein.jupiter.web.entity.vo.ShopVO;
import com.qiein.jupiter.web.service.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 拍摄地业务层
 */
@Service
public class ShopServiceImpl implements ShopService {

    @Autowired
    private ShopDao shopDao;
    @Autowired
    private ClientInfoDao clientInfoDao;

    /**
     * 获取企业所有拍摄地列表
     *
     * @param companyId
     * @return
     */
    public List<ShopPO> getCompanyShopList(int companyId) {
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
        shopPO.setTypeId(ShopTypeEnum.SHOP.getShopType());
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
     *
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
     * 校验拍摄地是否可删除
     *
     * @param companyId
     * @param id
     */
    public boolean shopCanDelete(int companyId, int id) {
        int shopKzNum = clientInfoDao.getKzNumByShopId(companyId, id);
        int filmingCodeKzNum = clientInfoDao.getKzNumByFilmingCode(companyId, id);
        return (shopKzNum == 0 && filmingCodeKzNum == 0) ? true : false;
    }

    /**
     * 删除拍摄地
     *
     * @param companyId
     * @param id
     */
    public void deleteShop(int companyId, int id) {

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

    /**
     * 获取渠道的字典
     *
     * @param companyId
     * @return
     */
    @Override
    public Map<String, ShopDictVO> getShopDictByCid(int companyId) {
        List<ShopPO> companyShopList = shopDao.getCompanyShopList(companyId);
        Map<String, ShopDictVO> shopMap = new HashMap<>();
        for (ShopPO shopVO : companyShopList) {
            ShopDictVO shopDictVO = new ShopDictVO();
            shopDictVO.setId(shopVO.getId());
            //名称
            shopDictVO.setShopName(shopVO.getShopName());
            //是否显示
            shopDictVO.setShowFlag(shopVO.isShowFlag());
            shopMap.put(String.valueOf(shopVO.getId()), shopDictVO);
        }
        return shopMap;
    }

    /**
     * 获取所在小组承接拍摄地
     *
     * @param companyId
     * @param groupId
     * @return
     */
    @Override
    public List<ShopPO> getShopListByStaffGroup(int companyId, String groupId) {
        return shopDao.getShopListByStaffGroup(companyId, groupId);
    }


    /**
     * 根据id获取门店信息
     *
     * @param shopId
     * @return
     */
    public ShopPO findShop(Integer shopId) {
        return shopDao.findShop(shopId);
    }

    /**
     * 获取门店门市列表
     *
     * @param companyId
     * @return
     */
    public List<ShopVO> getShopAndStaffList(int companyId) {
        return shopDao.getShopAndStaffList(companyId);
    }
}
