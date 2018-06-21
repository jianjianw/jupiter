package com.qiein.jupiter.web.dao;

import com.qiein.jupiter.web.entity.dto.GoldCustomerDTO;
import com.qiein.jupiter.web.entity.po.GoldFingerPO;
import com.qiein.jupiter.web.entity.vo.GoldCustomerVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 金手指表单
 * Author xiangliang 2018/6/13
 */
public interface GoldDataDao {
    /**
     * 添加表单
     * @param goldFingerPO
     */
    void insert(GoldFingerPO goldFingerPO);
    /**
     * 修改表单
     * @param goldFingerPO
     */
    void update(GoldFingerPO goldFingerPO);
    /**
     * 删除表单
     * @param id
     */
    void delete(@Param("id") Integer id);

    /**
     * 金数据表单页面显示
     * @param companyId
     * @return List<GoldFingerPO>
     */
    List<GoldFingerPO> select(@Param("companyId") Integer companyId);
    /**
     * 管理开关
     * @param goldFingerPO
     */
    void editOpenOrClose(GoldFingerPO goldFingerPO);
    /**
     * 金数据客资日志
     * @param goldCustomerDTO
     * @return
     */
    List<GoldCustomerVO> goldCustomerSelect( GoldCustomerDTO goldCustomerDTO);

    /**
     * 插入或者修改数据查重
     * @param goldFingerPO
     * @return
     */
    List<GoldFingerPO> checkForm(GoldFingerPO goldFingerPO);

    /**
     * 查询表单信息
     * @param formId
     * @return
     */
    GoldFingerPO findForm(String formId);

    /**
     * 获取金数据表单
     * @param formId
     * @param formName
     * */
    GoldFingerPO getGoldFingerByFormIdAndFormName(@Param(value="formId") String formId,@Param(value="formName") String formName);

    /**
     * 筛选
     * @param id
     */
    void addkzByGoldTemp(Integer id);
}
