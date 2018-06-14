package com.qiein.jupiter.web.dao;

import com.qiein.jupiter.web.entity.po.ClientRemarkPO;
import org.apache.ibatis.annotations.Param;

/**
 * FileName: ClientRemarkDao
 *
 * @author: yyx
 * @Date: 2018-6-14 17:21
 */
public interface ClientRemarkDao {
    /**
     * 增加客资备注
     * @param tabName
     * @param clientRemarkPO
     */
    void insert(@Param("tabName") String tabName, @Param(value="clientRemarkPO")ClientRemarkPO clientRemarkPO);

    /**
     * 修改客资备注
     * @param tabName
     * @param clientRemarkPO
     * */
    void update(@Param(value="tabName") String tabName,@Param(value="clientRemarkPO")ClientRemarkPO clientRemarkPO);

    /**
     * 根据客资id查询备注
     * */
    ClientRemarkPO getById(@Param(value="tabName")String tabName,@Param(value="clientRemarkPO")ClientRemarkPO clientRemarkPO);

}
