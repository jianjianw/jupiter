package com.qiein.jupiter.web.repository;

import com.qiein.jupiter.util.DBSplitUtil;
import com.qiein.jupiter.web.entity.vo.RepeatVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;

/**
 * 客资录入编辑查重
 *
 * @Author: shiTao
 */
public class ClientRepeatCheckDao {

    @Autowired
    private NamedParameterJdbcOperations namedJdbc;


    /**
     * @param companyId
     * @param kzId
     * @param typeId
     * @param srcType
     * @param sourceName
     * @param operaId
     * @param operaName
     * @param kzName
     * @param contact
     */
    public void check(int companyId, String kzId, int typeId, int srcType, String sourceName, int operaId,
                      String operaName, String kzName, String... contact) {

    }

    /**
     * 获取公司重复设置
     */
    public void getCompanyRepeatSet(int companyId) {
        RepeatVO repeatVO = new RepeatVO();

    }


    /**
     * 客资查重
     *
     * @param companyId
     * @param kzId
     * @param typeId
     * @param srcType
     * @param sourceName
     * @param kzName
     * @param contact
     * @param operaId
     * @param operaName
     * @param repeatVO
     */
    public void checkContactInfo(int companyId, String kzId, int typeId, int srcType, String sourceName, String kzName,
                                 String contact, int operaId, String operaName, RepeatVO repeatVO) {
        StringBuilder sql = new StringBuilder();
        sql.append("  SELECT COUNT(1) NUM, info.KZID FROM  ");
        sql.append(DBSplitUtil.getInfoTabName(companyId));
//        sql.append(" ")


    }
}
