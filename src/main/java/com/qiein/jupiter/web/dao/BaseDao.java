package com.qiein.jupiter.web.dao;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * DAO支持类实现
 */
public interface BaseDao<T> {

    /**
     * 获取单条数据
     *
     * @param id
     * @return
     */
    T getById(Integer id);

    /**
     * 查询数据列表，如果需要分页，请设置分页对象，如：entity.setPage(new Page<T>());
     *
     * @param paramMap
     * @return
     */
    List<T> findList(Map paramMap);

    /**
     * 插入数据
     *
     * @param entity
     * @return
     */
    int insert(T entity);

    /**
     * 更新数据
     *
     * @param entity
     * @return
     */
    int update(T entity);

    /**
     * 删除数据（一般为物理删除）
     *
     * @param id
     * @return
     * @see public int delete(Integer id)
     */
    int delete(Integer id);

    /**
     * 删除数据（一般为逻辑删除，更新isDel字段为1）
     *
     * @param id
     * @return
     */
    int flagDelete(@Param("id") Integer id);

}