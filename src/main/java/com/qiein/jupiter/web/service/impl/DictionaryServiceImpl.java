package com.qiein.jupiter.web.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.qiein.jupiter.util.StringUtil;
import com.qiein.jupiter.web.entity.vo.DictionaryVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qiein.jupiter.constant.CommonConstant;
import com.qiein.jupiter.constant.DictionaryConstant;
import com.qiein.jupiter.exception.ExceptionEnum;
import com.qiein.jupiter.exception.RException;
import com.qiein.jupiter.util.CollectionUtils;
import com.qiein.jupiter.web.dao.DictionaryDao;
import com.qiein.jupiter.web.entity.po.DictionaryPO;
import com.qiein.jupiter.web.service.DictionaryService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 字典业务层
 */
@Service
public class DictionaryServiceImpl implements DictionaryService {
    @Autowired
    private DictionaryDao dictionaryDao;

    /**
     * 根绝类型,企业自定义地点数据
     *
     * @param companyId
     * @param dicType
     * @return
     */
    public List<DictionaryPO> getCompanyDicByType(int companyId, String dicType) {
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
        int dicCode = dictionaryDao.getMaxDicCode(dictionaryPO.getCompanyId(), dictionaryPO.getDicType());
        dictionaryPO.setDicCode(dicCode + 1);
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

    /**
     * 添加流失原因
     *
     * @param companyId
     * @param dicName
     */
    public void addRunoffReason(int companyId, String dicName) {
        int dicCode = dictionaryDao.getMaxDicCode(companyId, DictionaryConstant.RUN_OFF_REASON);
        DictionaryPO dictionaryPO = new DictionaryPO(DictionaryConstant.RUN_OFF_REASON, dicCode + 1, dicName, companyId);
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
     * @param companyId
     * @param id
     * @param dicName
     */
    public void editRunoffReason(int companyId, int id, String dicName) {
        DictionaryPO dictionaryPO = new DictionaryPO(DictionaryConstant.RUN_OFF_REASON, dicName, companyId, id);
        //1.去重
        DictionaryPO exist = dictionaryDao.getDicByTypeAndName(dictionaryPO.getCompanyId(), dictionaryPO.getDicType(), dictionaryPO.getDicName());
        if (exist != null && exist.getId() != dictionaryPO.getId()) {
            throw new RException(ExceptionEnum.INVALID_REASON_EXIST);
        }
        //2.修改
        dictionaryDao.update(dictionaryPO);
    }

    /**
     * 获取自定义地点数据，没有则获取共有的数据
     *
     * @param companyId
     * @param dicType
     * @return
     */
    public List<DictionaryPO> getCommonDicByType(int companyId, String dicType) {
        List<DictionaryPO> list = dictionaryDao.getDicByType(companyId, dicType);
        if (CollectionUtils.isEmpty(list)) {
            list = dictionaryDao.getDicByType(DictionaryConstant.COMMON_COMPANYID, dicType);
        }
        return list;
    }

    /**
     * 将一个公司的字典遍历成map
     * key 为type  value为集合
     *
     * @param companyId
     * @return
     */
    @Override
    public Map<String, List<DictionaryPO>> getDictMapByCid(int companyId) {
        List<DictionaryPO> dictByCompanyId = dictionaryDao.getDictByCompanyId(companyId);
        Map<String, List<DictionaryPO>> map = new HashMap<>();
        //遍历
        for (DictionaryPO dictionaryPO : dictByCompanyId) {
            List<DictionaryPO> dictList;
            //判断是否存在该key
            String key = dictionaryPO.getDicType();
            //转换为驼峰
            String type = StringUtil.camelCaseName(key);
            if (map.get(type) == null) {
                dictList = new ArrayList<>();
                dictList.add(dictionaryPO);
                map.put(type, dictList);
            } else {
                dictList = map.get(type);
                dictList.add(dictionaryPO);
            }
        }
        return map;
    }

    /**
     * 新增字典
     * @param dictionaryPO
     */
    @Override
    public void createDict(DictionaryPO dictionaryPO) {
        if (dictionaryDao.createDict(dictionaryPO)!=1){
            throw new RException(ExceptionEnum.ADD_FAIL);
        }
    }

    @Override
    public void createCommonType(DictionaryVO dictionaryVO) {
        //TODO


    }

//    /**
//     * 编辑字典
//     * @param dictionaryPO
//     */
//    @Override
//    public void editDict(DictionaryPO dictionaryPO) {
//        if(dictionaryDao.editDict(dictionaryPO)!=1){
//            throw new RException(ExceptionEnum.EDIT_FAIL);
//        }
//    }

    /**
     * 删除字典
     * @param id
     * @param companyId
     */
    @Override
    public void delDict(int id, int companyId) {
        dictionaryDao.delDict(id,companyId);
    }

    /**
     * 编辑咨询类型
     * @param dictionaryVO
     */
    @Override
    public void editCommonType(DictionaryVO dictionaryVO) {
        //TODO dao层
    }

    /**
     * 编辑字典排序
     * @param id1
     * @param priority1
     * @param id2
     * @param priority2
     * @param companyId
     */
    @Override
    @Transactional
    public void editDictPriority(int id1, int priority1, int id2, int priority2, int companyId) {
        dictionaryDao.editDictPriority(id1,priority1,companyId);
        dictionaryDao.editDictPriority(id2,priority2,companyId);
    }

}
