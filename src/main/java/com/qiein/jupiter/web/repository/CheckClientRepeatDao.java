package com.qiein.jupiter.web.repository;

import com.qiein.jupiter.web.entity.dto.RepeatDTO;
import com.qiein.jupiter.web.entity.vo.QueryVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * 客资查重
 *
 * @Author: shiTao
 */
@Repository
public class CheckClientRepeatDao {
    @Autowired
    private NamedParameterJdbcOperations namedJdbc;


    public void check(QueryVO queryVO) {

    }


    /**
     * 检查Info表
     */
    private void checkInfo() {

    }

    /**
     * 检查detail表
     */
    private void checkDetail() {

    }


    /**
     * 获取公司重复配置
     *
     * @param companyId
     */
    public RepeatDTO getCompanyRepeatSet(int companyId) {

        //查询参数
        Map<String, Object> keyMap = new HashMap<>();
        keyMap.put("companyId", companyId);

        final RepeatDTO repeatVO = new RepeatDTO();
        String sql = "SELECT comp.TYPEREPEAT, comp.SRCREPEAT, comp.STATUSIGNORE, comp.TIMETYPEIGNORE, comp.DAYIGNORE FROM hm_pub_company comp WHERE comp.ID = ? ";

        namedJdbc.query(sql, keyMap, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet resultSet) throws SQLException {
                repeatVO.setTypeRepeat(resultSet.getBoolean("TYPEREPEAT"));
                repeatVO.setSrcRepeat(resultSet.getBoolean("SRCREPEAT"));
                repeatVO.setStatusIgnore(resultSet.getString("STATUSIGNORE"));
                repeatVO.setTimeTypeIgnore(resultSet.getString("TIMETYPEIGNORE"));
                repeatVO.setDayIgnore(resultSet.getInt("DAYIGNORE"));
            }

        });

        return repeatVO;
    }
}
