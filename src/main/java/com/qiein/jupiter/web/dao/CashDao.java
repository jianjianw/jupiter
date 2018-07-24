package com.qiein.jupiter.web.dao;

import com.qiein.jupiter.web.entity.dto.CashDTO;
import org.apache.ibatis.annotations.Param;

public interface CashDao extends BaseDao{


    /**
     * 查找原本的付款记录
     * @param id
     * @return
     */
    CashDTO getById(@Param("id") Integer id);

    /**
     * 修改付款记录的状态
     */
    void editStatus(@Param("table") String table, @Param("id") Integer id);

}
