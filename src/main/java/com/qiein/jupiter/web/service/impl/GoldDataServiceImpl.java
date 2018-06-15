package com.qiein.jupiter.web.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qiein.jupiter.exception.ExceptionEnum;
import com.qiein.jupiter.exception.RException;
import com.qiein.jupiter.web.dao.GoldDataDao;
import com.qiein.jupiter.web.entity.dto.GoldCustomerDTO;
import com.qiein.jupiter.web.entity.dto.QueryMapDTO;
import com.qiein.jupiter.web.entity.po.GoldFingerPO;
import com.qiein.jupiter.web.entity.po.StaffPO;
import com.qiein.jupiter.web.entity.vo.GoldCustomerShowVO;
import com.qiein.jupiter.web.entity.vo.GoldCustomerVO;
import com.qiein.jupiter.web.service.GoldDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 金手指表单
 * Author xiangliang 2018/6/13
 */
@Service
public class GoldDataServiceImpl implements GoldDataService {

    @Autowired
    private GoldDataDao goldDataDao;
    /**
     * 添加表单
     * @param goldFingerPO
     */
    public void insert(GoldFingerPO goldFingerPO){
        List<GoldFingerPO>  list =goldDataDao.checkForm(goldFingerPO);
        if(list.size()==0){
            goldDataDao.insert(goldFingerPO);
        }
       else{
            throw new RException(ExceptionEnum.FORM_WAS_IN);
        }
    }
    /**
     * 修改表单
     * @param goldFingerPO
     */
    public void update(GoldFingerPO goldFingerPO){
        List<GoldFingerPO>  list =goldDataDao.checkForm(goldFingerPO);
        if(list.size()==0){
        goldDataDao.update(goldFingerPO);
        }
        else{
            throw new RException(ExceptionEnum.FORM_WAS_IN);
        }
    }
    /**
     * 删除表单
     * @param id
     */
    public void delete(Integer id){
        goldDataDao.delete(id);
    }
    /**
     * 金数据表单页面显示
     * @param companyId
     * @return
     */
    public List<GoldFingerPO> select(Integer companyId){
        return goldDataDao.select(companyId);

    }
    /**
     * 管理开关
     * @param goldFingerPO
     */
    public void editOpenOrClose(GoldFingerPO goldFingerPO){
        goldDataDao.editOpenOrClose(goldFingerPO);
    }

    /**
     * 金数据客资日志
     * @param goldCustomerDTO
     * @return
     */
    public GoldCustomerShowVO goldCustomerSelect(QueryMapDTO queryMapDTO, GoldCustomerDTO goldCustomerDTO)
    {
        PageHelper.startPage(queryMapDTO.getPageNum(), queryMapDTO.getPageSize());
        List<GoldCustomerVO> list=goldDataDao.goldCustomerSelect(goldCustomerDTO);
        GoldCustomerShowVO showVO=new GoldCustomerShowVO();
        showVO.setPageInfo(new PageInfo<>(list));
        GoldFingerPO goldFingerPO=goldDataDao.findForm(goldCustomerDTO.getFormId());
        showVO.setGoldFingerPO(goldFingerPO);
        return showVO;
    }
}
