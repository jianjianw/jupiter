package com.qiein.jupiter.web.dao;

import com.qiein.jupiter.web.entity.po.BrandPO;
import com.qiein.jupiter.web.entity.vo.FastMemoVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 快捷备注Dao
 */
public interface FastMemoDao extends BaseDao<BrandPO> {
    /**
     * 获取个人快捷备注列表
     *
     * @param staffId
     * @param companyId
     * @return
     */
    List<FastMemoVO> getMemoListById(@Param("staffId") int staffId, @Param("companyId") int companyId);

    /**
     * 添加快捷备注
     *
     * @param staffId
     * @param companyId
     * @param memo
     * @return
     */
    int addFastMemo(@Param("staffId") int staffId, @Param("companyId") int companyId, @Param("memo") String memo);

    /**
     * 删除快捷备注
     *
     * @param id
     * @return
     */
    int deleteFastMemo(@Param("id") int id);

}
