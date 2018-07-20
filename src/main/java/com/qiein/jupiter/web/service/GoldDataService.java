package com.qiein.jupiter.web.service;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.qiein.jupiter.web.entity.dto.EditCreatorDTO;
import com.qiein.jupiter.web.entity.dto.GoldCustomerDTO;
import com.qiein.jupiter.web.entity.dto.QueryMapDTO;
import com.qiein.jupiter.web.entity.po.GoldFingerPO;
import com.qiein.jupiter.web.entity.po.GoldTempPO;
import com.qiein.jupiter.web.entity.po.StaffPO;
import com.qiein.jupiter.web.entity.vo.GoldCustomerShowVO;
import com.qiein.jupiter.web.entity.vo.GoldCustomerVO;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 金手指表单
 * Author xiangliang 2018/6/13
 */
public interface GoldDataService {
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
    void delete(Integer id);

    /**
     * 金数据表单页面显示
     *
     * @param companyId
     * @return
     */
    PageInfo<GoldFingerPO> select(int companyId,int pageNum, int pageSize);

    /**
     * 管理开关
     *
     * @param goldFingerPO
     */
    void editOpenOrClose(GoldFingerPO goldFingerPO);

    /**
     * 金数据客资日志
     *
     * @param goldCustomerDTO
     * @return
     */
    GoldCustomerShowVO goldCustomerSelect(QueryMapDTO queryMapDTO, GoldCustomerDTO goldCustomerDTO);

    /**
     * 接受金数据表单
     *
     * @param jsonObject
     */
    void receiveGoldDataForm(JSONObject jsonObject);

    /**
     * 筛选
     *
     * @param goldTempPO
     */
    void addkzByGoldTemp(GoldTempPO goldTempPO);

    /**
     * 修改表单创建者
     *
     * @param editCreatorDTO
     */
    void editFormCreateor(EditCreatorDTO editCreatorDTO);
}
