package com.qiein.jupiter.web.dao;

import com.qiein.jupiter.web.entity.dto.EditCreatorDTO;
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
     *
     * @param goldFingerPO
     */
    void insert(GoldFingerPO goldFingerPO);

    /**
     * 修改表单
     *
     * @param goldFingerPO
     */
    void update(GoldFingerPO goldFingerPO);

    /**
     * 删除表单
     *
     * @param id
     */
    void delete(@Param("id") Integer id);

    /**
     * 金数据表单页面显示
     */
    List<GoldFingerPO> select(@Param("companyId") Integer companyId, @Param("formId") String formId, @Param("srcList")List<Integer> srcList, @Param("staffList")List<Integer> staffList);

    /**
     * 管理开关
     */
    void editOpenOrClose(GoldFingerPO goldFingerPO);

    /**
     * 金数据客资日志
     */
    List<GoldCustomerVO> goldCustomerSelect(GoldCustomerDTO goldCustomerDTO);

    /**
     * 插入或者修改数据查重
     */
    List<GoldFingerPO> checkForm(GoldFingerPO goldFingerPO);

    /**
     * 查询表单信息
     */
    GoldFingerPO findForm(@Param("formId")String formId,@Param("companyId")Integer companyId);

    /**
     * 获取金数据表单
     */
    GoldFingerPO getGoldFingerByFormId(@Param(value = "formId") String formId);

    /**
     * 筛选
     */
    void addkzByGoldTemp(Integer id);

    /**
     * 修改表单创建者
     *
     * @param editCreatorDTO
     */
    void editFormCreateor(EditCreatorDTO editCreatorDTO);
}
