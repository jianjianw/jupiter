package com.qiein.jupiter.web.service.impl;

import com.qiein.jupiter.exception.ExceptionEnum;
import com.qiein.jupiter.exception.RException;
import com.qiein.jupiter.web.dao.BrandDao;
import com.qiein.jupiter.web.entity.po.BrandPO;
import com.qiein.jupiter.web.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 品牌Service
 * Created by Administrator on 2018/4/24 0024.
 */
@Service
public class BrandServiceImpl implements BrandService {

    @Autowired
    private BrandDao brandDao;

    /**
     * 创建品牌
     *
     * @param brandPO
     */
    @Override
    public void createBrand(BrandPO brandPO) {
        if (brandDao.checkBrandName(brandPO.getBrandName(), brandPO.getCompanyId()) > 0)
            throw new RException(ExceptionEnum.BRAND_NAME_REPEAT);
        brandDao.insert(brandPO);
    }

    /**
     * 删除品牌
     *
     * @param id
     */
    @Override
    public void delBrand(Integer id, Integer companyId) {
        if (true) { //TODO 如果存在下属渠道则不能删除
            brandDao.deleteByIdAndCid(id,companyId);
        }
    }

    /**
     * 编辑品牌
     *
     * @param brandPO
     */
    @Override
    @Transactional
    public void editBrand(BrandPO brandPO) {
        BrandPO old = brandDao.getByIdAndCid(brandPO.getId(), brandPO.getCompanyId());
        if (old != null){
            if (!old.getBrandName().equals(brandPO.getBrandName())) {
                if (brandDao.checkBrandName(brandPO.getBrandName(), brandPO.getCompanyId()) > 0)
                    throw new RException(ExceptionEnum.BRAND_NAME_REPEAT);
                //TODO  更改下属渠道和来源的品牌
                brandDao.update(brandPO);
            }
        }else {
            throw new RException(ExceptionEnum.BRAND_NOT_FOUND);
        }
    }

    /**
     * 获取品牌列表
     *
     * @param companyId
     * @return
     */
    @Override
    public List<BrandPO> getBrandList(Integer companyId) {
        return brandDao.getBrandList(companyId);
    }


}
