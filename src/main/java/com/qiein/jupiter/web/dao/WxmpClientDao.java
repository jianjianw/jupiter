package com.qiein.jupiter.web.dao;

import com.qiein.jupiter.web.entity.vo.ClientVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 小程序
 *
 * @Author: shiTao
 */
public interface WxmpClientDao {

    /**
     * 按名称 搜索客资
     *
     * @param infoTableName
     * @param detailTableName
     * @param companyId
     * @param searchKey
     * @return
     */
    List<ClientVO> searchClient(@Param("infoTableName") String infoTableName,
                                @Param("detailTableName") String detailTableName,
                                @Param("companyId") int companyId,
                                @Param("searchKey") String searchKey);
}
