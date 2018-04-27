package com.qiein.jupiter.web.service.impl;

import com.qiein.jupiter.constant.CommonConstant;
import com.qiein.jupiter.constant.DictionaryConstant;
import com.qiein.jupiter.exception.ExceptionEnum;
import com.qiein.jupiter.exception.RException;
import com.qiein.jupiter.util.CollectionUtils;
import com.qiein.jupiter.web.dao.DictionaryDao;
import com.qiein.jupiter.web.entity.po.DictionaryPO;
import com.qiein.jupiter.web.entity.vo.MenuVO;
import com.qiein.jupiter.web.service.DictionaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.plaf.ListUI;
import java.util.Collections;
import java.util.List;

/**
 * 字典业务层
 */
@Service
public class DictionaryServiceImpl implements DictionaryService {
    @Autowired
    private DictionaryDao dictionaryDao;

    /**
     * 根绝类型,企业ID获取字典数据
     *
     * @param companyId
     * @param dicType
     * @return
     */
    public List<DictionaryPO> getDicByType(int companyId, String dicType) {
        List<DictionaryPO> list = dictionaryDao.getDicByType(companyId, dicType);
        return list;
    }

    /**
     * 新增流失原因
     *
     * @param dictionaryPO
     */
    public void addInvalidReason(DictionaryPO dictionaryPO) {
        dictionaryPO.setDicType(DictionaryConstant.INVALID_REASON);
        dictionaryPO.setDicCode(DictionaryConstant.COMMON_CODE);
        //1.去重
        DictionaryPO exist = dictionaryDao.getDicByTypeAndName(dictionaryPO.getCompanyId(), dictionaryPO.getDicType(), dictionaryPO.getDicName());
        if (exist != null) {
            throw new RException(ExceptionEnum.INVALID_REASON_EXIST);
        }
        //2.新增
        dictionaryDao.insert(dictionaryPO);
    }

    /**
     * 编辑流失原因
     *
     * @param dictionaryPO
     */
    public void editInvalidReason(DictionaryPO dictionaryPO) {
        //1.去重
        DictionaryPO exist = dictionaryDao.getDicByTypeAndName(dictionaryPO.getCompanyId(), DictionaryConstant.INVALID_REASON, dictionaryPO.getDicName());
        if (exist != null && exist.getId() != dictionaryPO.getId()) {
            throw new RException(ExceptionEnum.INVALID_REASON_EXIST);
        }
        //2.修改
        dictionaryDao.update(dictionaryPO);
    }

    /**
     * 批量删除字典数据
     *
     * @param companyId
     * @param ids
     */
    public void batchDeleteByIds(int companyId, String ids) {
        String[] idArr = ids.split(CommonConstant.STR_SEPARATOR);
        dictionaryDao.batchDeleteByIds(companyId, idArr);
    }

}
