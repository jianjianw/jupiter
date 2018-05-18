package com.qiein.jupiter.web.dao;

import com.qiein.jupiter.web.entity.vo.ClientStatusVO;
import org.apache.ibatis.annotations.Param;

/**
 * Created by Tt on 2018/5/15 0015.
 */
public interface ClientDao {
    /**
     * 编辑客资基本信息，如性别，微信添加状况
     * @param clientStatusVO
     */
    void editClientBaseInfo(@Param("clientStatusVO") ClientStatusVO clientStatusVO, @Param("tabName") String tabName);
}