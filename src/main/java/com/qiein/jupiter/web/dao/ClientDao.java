package com.qiein.jupiter.web.dao;

import com.qiein.jupiter.web.entity.vo.ClientStatusVO;
import org.apache.ibatis.annotations.Param;

/**
 * Created by Tt on 2018/5/15 0015.
 */
public interface ClientDao {
    /**
     * 编辑客资基本信息，如性别，微信添加状况
     *
     * @param clientStatusVO
     */
    void editClientBaseInfo(@Param("clientStatusVO") ClientStatusVO clientStatusVO, @Param("tabName") String tabName);

    /**
     * 快速添加备注标签
     *
     * @param tabName
     * @param companyId
     * @param kzId
     * @param label
     * @return
     */
    int editClientMemoLabel(@Param("tabName") String tabName, @Param("companyId") int companyId,
                            @Param("kzId") String kzId, @Param("label") String label);

    /**
     * 根据状态筛选客资数量
     *
     * @param statusId
     * @param companyId
     * @param tableName
     * @return
     */
    public int getKzNumByStatusId(@Param("statusId") int statusId, @Param("companyId") Integer companyId,
                                  @Param("tableName") String tableName);
}
