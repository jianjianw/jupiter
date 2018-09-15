package com.qiein.jupiter.web.repository;

import com.qiein.jupiter.constant.CommonConstant;
import com.qiein.jupiter.util.DBSplitUtil;
import com.qiein.jupiter.util.NumUtil;
import com.qiein.jupiter.util.StringUtil;
import com.qiein.jupiter.web.entity.vo.PlatPageVO;
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
 * 客资查询
 *
 * @Author: shiTao
 */
@Repository
public class ClientQueryDao {
    @Autowired
    private NamedParameterJdbcOperations namedJdbc;

    /**
     * 查询删除客资
     */
    public PlatPageVO queryDelClientInfo(QueryVO vo, int companyId) {
        PlatPageVO pageVO = new PlatPageVO();
        pageVO.setCurrentPage(vo.getCurrentPage());
        pageVO.setPageSize(vo.getPageSize());
        //查询参数
        Map<String, Object> keyMap = new HashMap<>();
        keyMap.put("companyId", companyId);
        keyMap.put("page", vo.getCurrentPage());
        keyMap.put("size", vo.getPageSize());
        //sql
        StringBuilder baseSelect = getBaseSelect();
        baseSelect.append(getFromSql(companyId));

        baseSelect.append(" WHERE  info.COMPANYID = :companyId AND info.ISDEL = 1 ");
        handleWhereSql(vo, keyMap, baseSelect);
        //ORDER
        baseSelect.append(" ORDER BY info.UPDATETIME DESC, info.ID DESC ");
        //分页
        baseSelect.append(" limit :page , :size ");
        //执行查询
        namedJdbc.query(baseSelect.toString(), keyMap, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                System.out.println(rs.getString("KZID"));
            }
        });
        return pageVO;
    }


    /**
     * 获取基础select
     *
     * @return
     */
    private StringBuilder getBaseSelect() {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT info.ID, info.KZID, info.TYPEID, info.CLASSID, info.STATUSID, ").
                append("info.KZNAME, info.KZPHONE, info.KZWECHAT, info.WEFLAG, ")
                .append(" info.KZQQ, info.KZWW, info.SEX, info.CHANNELID, info.SOURCEID, info.COLLECTORID, det.COLLECTORNAME,")
                .append(" info.PROMOTORID, det.PROMOTERNAME, info.APPOINTORID, ")
                .append("  det.APPOINTNAME, info.RECEPTORID, det.RECEPTORNAME, info.SHOPID, det.SHOPNAME, info.ALLOTTYPE,")
                .append(" info.CREATETIME, info.RECEIVETIME, info.TRACETIME, info.APPOINTTIME, ")
                .append(" info.COMESHOPTIME, info.SUCCESSTIME, info.UPDATETIME, det.MEMO, ")
                .append("det.OLDKZNAME, det.OLDKZPHONE, det.AMOUNT, det.STAYAMOUNT, det.TALKIMG, det.ORDERIMG, ")
                .append("  det.ZXSTYLE, det.YXLEVEL, det.YSRANGE, det.ADADDRESS, det.ADID, det.MARRYTIME,")
                .append(" det.YPTIME, det.MATENAME, det.MATEPHONE, det.ADDRESS,  ")
                .append(" info.GROUPID, det.GROUPNAME, det.PAYSTYLE, det.HTNUM, det.INVALIDLABEL, det.KEYWORD, log.CONTENT ");
        return sql;
    }

    /**
     * 获取from
     *
     * @param companyId
     * @return
     */
    private String getFromSql(int companyId) {
        StringBuilder from = new StringBuilder();
        from.append(" FROM ").append(DBSplitUtil.getInfoTabName(companyId));
        from.append(" info LEFT JOIN ");
        from.append(DBSplitUtil.getDetailTabName(companyId));
        from.append(" det ON info.KZID = det.KZID AND det.COMPANYID = info.COMPANYID ");
        from.append(" LEFT JOIN ");
        from.append(DBSplitUtil.getRemarkTabName(companyId));
        from.append(" log ON log.KZID = info.KZID AND log.COMPANYID = info.COMPANYID ");
        return from.toString();
    }

    /**
     * 查询对象转Map
     */
    private void handleWhereSql(QueryVO vo, Map<String, Object> keyMap, StringBuilder where) {
        where.append(" AND ").append(vo.getTimeType())
                .append(" BETWEEN :startTime AND :endTime");
        keyMap.put("startTime", vo.getStart());
        keyMap.put("endTime", vo.getEnd());
        // 渠道
        if (StringUtil.isNotEmpty(vo.getChannelId())) {
            where.append(dynamixSql(vo.getChannelId(), CommonConstant.STR_SEPARATOR, "info.CHANNELID"));
        }

        // 来源
        if (StringUtil.isNotEmpty(vo.getSourceId())) {
            where.append(dynamixSql(vo.getSourceId(), CommonConstant.STR_SEPARATOR, "info.SOURCEID"));
        }

        // 拍摄地-门店
        if (StringUtil.isNotEmpty(vo.getShopId())) {
            where.append(dynamixSql(vo.getShopId(), CommonConstant.STR_SEPARATOR, "info.SHOPID"));
        }

        // 类型ID
        if (StringUtil.isNotEmpty(vo.getTypeId())) {
            where.append(dynamixSql(vo.getTypeId(), CommonConstant.STR_SEPARATOR, "info.TYPEID"));
        }

        // 录入人
        if (StringUtil.isNotEmpty(vo.getCollectorId())) {
            where.append(dynamixSql(vo.getCollectorId(), CommonConstant.STR_SEPARATOR, "info.COLLECTORID"));
        }

        // 筛选人
        if (StringUtil.isNotEmpty(vo.getPromotorId())) {
            where.append(dynamixSql(vo.getPromotorId(), CommonConstant.STR_SEPARATOR, "info.PROMOTORID"));
        }

        // 邀约人
        if (StringUtil.isNotEmpty(vo.getAppointorId())) {
            where.append(dynamixSql(vo.getAppointorId(), CommonConstant.STR_SEPARATOR, "info.APPOINTORID"));
        }

        // 接待人
        if (StringUtil.isNotEmpty(vo.getReceptorId())) {
            where.append(dynamixSql(vo.getReceptorId(), CommonConstant.STR_SEPARATOR, "info.RECEPTORID"));
        }

        // 状态
        if (StringUtil.isNotEmpty(vo.getStatusId())) {
            where.append(dynamixSql(vo.getStatusId(), CommonConstant.STR_SEPARATOR, "info.STATUSID"));
        }

        // 意向
        if (StringUtil.isNotEmpty(vo.getYxLevel())) {
            where.append(dynamixSql(vo.getYxLevel(), CommonConstant.STR_SEPARATOR, "det.YXLEVEL"));
        }

        // 客资分类
        if (NumUtil.isValid(vo.getClassId())) {
            where.append(" AND info.CLASSID = ");
            where.append(vo.getClassId());
        }

        // 联系方式格式限定
        if (StringUtil.isNotEmpty(vo.getLinkLimit())) {
            where.append(" AND ");
            where.append(vo.getLinkLimit());
        }

        if (StringUtil.isNotEmpty(vo.getSpareSql())) {
            where.append(" AND ");
            where.append(vo.getSpareSql());
        }

        if (StringUtil.isNotEmpty(vo.getSuperSql())) {
            where.append(" AND ");
            where.append(vo.getSuperSql());
        }

        if (StringUtil.isNotEmpty(vo.getFilterSql())) {
            where.append(" AND ");
            where.append(vo.getFilterSql());
        }

        if (StringUtil.isNotEmpty(vo.getSearchKey())) {
            where.append(" AND ( info.ID = '")
                    .append(vo.getSearchKey())
                    .append("%' OR info.KZNAME LIKE '")
                    .append(vo.getSearchKey())
                    .append("%' OR info.KZPHONE LIKE '")
                    .append(vo.getSearchKey()).append("%' OR info.KZWECHAT LIKE '")
                    .append(vo.getSearchKey()).append("%' OR info.KZWW LIKE '")
                    .append(vo.getSearchKey()).append("%' OR info.KZQQ LIKE '")
                    .append(vo.getSearchKey()).append("%' ) ");
        }


    }


    /**
     * 多参数动态sql条件
     *
     * @param param
     * @param spitStr
     * @param column
     * @return
     */
    private String dynamixSql(String param, String spitStr, String column) {

        String[] paramArr = param.split(spitStr);
        if (paramArr.length == 0) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        sb.append(" AND (");
        for (int i = 0; i < paramArr.length; i++) {
            if (StringUtil.isEmpty(paramArr[i])) {
                continue;
            }
            sb.append(" ");
            sb.append(column);
            sb.append(" = ");
            sb.append(Integer.valueOf(paramArr[i]));
            sb.append(" ");
            if (i != paramArr.length - 1) {
                sb.append("OR");
            } else {
                sb.append(") ");
            }
        }
        return sb.toString();
    }
}
