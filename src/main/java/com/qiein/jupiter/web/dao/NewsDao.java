package com.qiein.jupiter.web.dao;

import com.qiein.jupiter.web.entity.po.NewsPO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface NewsDao extends BaseDao<NewsPO> {
    /**
     * 批量新增消息
     *
     * @param newsPOList
     * @return
     */
    int batchInsertNews(@Param("tableName") String tableName, @Param("list") List<NewsPO> newsPOList);

    /**
     * 获取员工的所有消息
     *
     * @return
     */
    List<NewsPO> getAllByStaffIdAndCid(NewsPO newsPO);

    /**
     * 获取员工的所有未读消息
     *
     * @return
     */
    List<NewsPO> getNotReadByStaffIdAndCid(NewsPO newsPO);

    /**
     * 批量更新消息为已读
     *
     * @return
     */
    int batchUpdateNewsReadFlag(@Param("tableName") String tableName,
                                @Param("newIds") String[] msgIds, @Param("companyId") int companyId);

    /**
     * 获取不同的消息的数量
     *
     * @return
     */
    int getDiffTypeMsgAmount(NewsPO newsPO);
}
