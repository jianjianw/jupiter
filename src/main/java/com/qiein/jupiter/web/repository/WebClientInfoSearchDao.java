package com.qiein.jupiter.web.repository;

import com.qiein.jupiter.web.entity.vo.ClientVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * 页面查询客资信息
 *
 * @Author: shiTao
 */
@Repository
public class WebClientInfoSearchDao {
    @Autowired
    private NamedParameterJdbcOperations namedJdbc;

    /**
     * 页面全局搜索客资 根据key
     *
     * @return
     */
    public List<ClientVO> searchClientInfoBySearchKey(int companyId, String key) {
        List<ClientVO> clientVOList = new ArrayList<>();
        StringBuilder sql = new StringBuilder()
                .append("  SELECT info.ID, info.KZID, info.KZNAME, info.KZPHONE, info.KZWECHAT, info.KZQQ, info.KZWW, info.CREATETIME, ")
                .append("info.STATUSID, info.SOURCEID, det.APPOINTNAME,det.MATENAME, det.MATEWECHAT,")
                .append(" det.MATEPHONE, det.MATEQQ, det.GROUPNAME FROM ");
        return null;
    }
}
