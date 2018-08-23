package com.qiein.jupiter.web.repository;

import com.qiein.jupiter.util.NumUtil;
import com.qiein.jupiter.web.entity.vo.DsInvalidVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author: yyx
 * @Date: 2018-8-10
 */
@Repository
public class CommonReportsDao {
    @Autowired
    JdbcTemplate jdbcTemplate;

    /**
     * 获取无效等指标定义
     */
    public DsInvalidVO getInvalidConfig(int companyId) {
        StringBuilder sb = new StringBuilder();

        if (NumUtil.isInValid(companyId)) {
            return null;
        }
        sb.append(" SELECT comp.DSINVALIDSTATUS, comp.DSINVALIDLEVEL, comp.DDISVALID, comp.DSDDSTATUS ,comp.ZJSVALIDSTATUS FROM hm_pub_company comp WHERE comp.ID = ? AND comp.ISDEL = 0 ");
        DsInvalidVO dsInvalidVO = jdbcTemplate.queryForObject(sb.toString(),
                new Object[]{companyId},
                new RowMapper<DsInvalidVO>() {
                    @Override
                    public DsInvalidVO mapRow(ResultSet rs, int i) throws SQLException {
                        DsInvalidVO dsInvalidVO = new DsInvalidVO();
                        dsInvalidVO.setDsInvalidStatus(rs.getString("DSINVALIDSTATUS"));
                        dsInvalidVO.setDsInvalidLevel(rs.getString("DSINVALIDLEVEL"));
                        dsInvalidVO.setDdIsValid(rs.getBoolean("DDISVALID"));
                        dsInvalidVO.setDsDdStatus(rs.getString("DSDDSTATUS"));
                        dsInvalidVO.setZjsValidStatus(rs.getString("ZJSVALIDSTATUS"));
                        return dsInvalidVO;
                    }
                });
        return dsInvalidVO;
    }


}
