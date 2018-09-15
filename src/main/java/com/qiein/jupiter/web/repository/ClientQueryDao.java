package com.qiein.jupiter.web.repository;

import com.alibaba.fastjson.JSONObject;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
        final PlatPageVO pageVO = new PlatPageVO();
        pageVO.setCurrentPage(vo.getCurrentPage());
        pageVO.setPageSize(vo.getPageSize());
        //查询参数
        Map<String, Object> keyMap = new HashMap<>();
        keyMap.put("companyId", companyId);
        keyMap.put("page", vo.getCurrentPage());
        keyMap.put("size", vo.getPageSize());
        //select
        StringBuilder baseSelect = getBaseSelect();
        //from
        StringBuilder fromSql = new StringBuilder();
        fromSql.append(getFromSql(companyId));
        //where
        StringBuilder whereSql = new StringBuilder();
        whereSql.append(" WHERE  info.COMPANYID = :companyId AND info.ISDEL = 1 ");
        handleWhereSql(vo, keyMap, whereSql);
        //ORDER
        StringBuilder orderLimitSql = new StringBuilder();
        orderLimitSql.append(" ORDER BY info.UPDATETIME DESC, info.ID DESC ");
        //分页
        orderLimitSql.append(" limit :page , :size ");
        String querySql = baseSelect.append(fromSql).append(whereSql).append(orderLimitSql).toString();
        //执行查询
        final List<JSONObject> result = new ArrayList<>();
        namedJdbc.query(querySql, keyMap, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                result.add(resultToClientInfo(rs));
            }
        });
        //分页
        String countSql = "SELECT COUNT(*) COUNT " + fromSql + whereSql;
        namedJdbc.query(countSql, keyMap, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                int count = rs.getInt("COUNT");
                pageVO.setTotalCount(count);
                int totalPageNum = (count + pageVO.getPageSize() - 1) / pageVO.getPageSize();
                pageVO.setTotalPage(totalPageNum);
            }
        });
        //执行分页
        pageVO.setData(result);
        return pageVO;
    }


    /**
     * 查询客资详情
     */
    public void searchClientDeatilInfo(PlatPageVO pageVO, String sql) {

    }

    /**
     * 处理分页信息
     */
    private void handlePageInfo(PlatPageVO pageVO, String sql) {

    }


    /**
     * 将结果转换为客资信息
     *
     * @param rs
     */
    private JSONObject resultToClientInfo(ResultSet rs) throws SQLException {
        JSONObject info = new JSONObject();
        info.put("id", rs.getInt("ID"));
        info.put("letterid", rs.getString("LETTERID"));
        info.put("kzid", rs.getString("KZID"));
        info.put("typeid", rs.getInt("TYPEID"));
        info.put("classid", rs.getInt("CLASSID"));
        info.put("statusid", rs.getInt("STATUSID"));
        info.put("kzname", rs.getString("KZNAME"));
        info.put("kzphone", rs.getString("KZPHONE"));
        info.put("kzwechat", rs.getString("KZWECHAT"));
        info.put("kzphoneflag", rs.getString("KZPHONE_FLAG"));
        info.put("weflag", rs.getInt("WEFLAG"));
        info.put("kzqq", rs.getString("KZQQ"));
        info.put("kzww", rs.getString("KZWW"));
        info.put("sex", rs.getInt("SEX"));
        info.put("channelid", rs.getInt("CHANNELID"));
        info.put("sourceid", rs.getInt("SOURCEID"));
        info.put("collectorid", rs.getInt("COLLECTORID"));
        info.put("promotorid", rs.getInt("PROMOTORID"));
        info.put("appointorid", rs.getInt("APPOINTORID"));
        info.put("receptorid", rs.getInt("RECEPTORID"));
        info.put("receivetime", rs.getInt("RECEIVETIME"));
        info.put("shopid", rs.getInt("SHOPID"));
        info.put("allottype", rs.getInt("ALLOTTYPE"));
        info.put("createtime", rs.getInt("CREATETIME"));
        info.put("tracetime", rs.getInt("TRACETIME"));
        info.put("appointtime", rs.getInt("APPOINTTIME"));
        info.put("comeshoptime", rs.getInt("COMESHOPTIME"));
        info.put("successtime", rs.getInt("SUCCESSTIME"));
        info.put("updatetime", rs.getInt("UPDATETIME"));
        info.put("srctype", rs.getInt("SRCTYPE"));
        info.put("groupid", rs.getString("GROUPID"));
        info.put("collectorname", rs.getString("COLLECTORNAME"));
        info.put("promotername", rs.getString("PROMOTERNAME"));
        info.put("appointname", rs.getString("APPOINTNAME"));
        info.put("receptorname", rs.getString("RECEPTORNAME"));
        info.put("shopname", rs.getString("SHOPNAME"));
        info.put("memo", rs.getString("MEMO"));
        info.put("oldkzname", rs.getString("OLDKZNAME"));
        info.put("oldkzphone", rs.getString("OLDKZPHONE"));
        info.put("amount", rs.getString("AMOUNT"));
        info.put("stayamount", rs.getString("STAYAMOUNT"));
        info.put("talkimg", rs.getString("TALKIMG"));
        info.put("orderimg", rs.getString("ORDERIMG"));
        info.put("zxstyle", rs.getInt("ZXSTYLE"));
        info.put("yxlevel", rs.getInt("YXLEVEL"));
        info.put("ysrange", rs.getInt("YSRANGE"));
        info.put("adaddress", rs.getString("ADADDRESS"));
        info.put("adid", rs.getString("ADID"));
        info.put("marrytime", rs.getInt("MARRYTIME"));
        info.put("yptime", rs.getInt("YPTIME"));
        info.put("matename", rs.getString("MATENAME"));
        info.put("matephone", rs.getString("MATEPHONE"));
        info.put("matewechat", rs.getString("MATEWECHAT"));
        info.put("mateqq", rs.getString("MATEQQ"));
        info.put("address", rs.getString("ADDRESS"));
        info.put("groupname", rs.getString("GROUPNAME"));
        info.put("paystyle", rs.getInt("PAYSTYLE"));
        info.put("htnum", rs.getString("HTNUM"));
        info.put("invalidlabel", rs.getString("INVALIDLABEL"));
        info.put("filmingcode", rs.getInt("FILMINGCODE"));
        info.put("filmingarea", rs.getString("FILMINGAREA"));
        info.put("keyword", rs.getString("KEYWORD"));
        info.put("packagecode", rs.getInt("PACKAGECODE"));

        return info;
    }

    /**
     * 获取基础select
     *
     * @return
     */
    private StringBuilder getBaseSelect() {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT info.ID, info.KZID, info.TYPEID, info.CLASSID, info.STATUSID,info.LETTERID,info.KZPHONE_FLAG, ").
                append("info.KZNAME, info.KZPHONE, info.KZWECHAT, info.WEFLAG,info.SRCTYPE, ")
                .append(" info.KZQQ, info.KZWW, info.SEX, info.CHANNELID, info.SOURCEID, info.COLLECTORID, det.COLLECTORNAME,")
                .append(" info.PROMOTORID, det.PROMOTERNAME, info.APPOINTORID, ")
                .append("  det.APPOINTNAME, info.RECEPTORID, det.RECEPTORNAME, info.SHOPID, det.SHOPNAME, info.ALLOTTYPE,")
                .append(" info.CREATETIME, info.RECEIVETIME, info.TRACETIME, info.APPOINTTIME, det.PACKAGECODE, ")
                .append(" info.COMESHOPTIME, info.SUCCESSTIME, info.UPDATETIME, det.MEMO,det.FILMINGCODE,det.FILMINGAREA, ")
                .append(" det.OLDKZNAME, det.OLDKZPHONE, det.AMOUNT, det.STAYAMOUNT, det.TALKIMG, det.ORDERIMG, ")
                .append(" det.ZXSTYLE, det.YXLEVEL, det.YSRANGE, det.ADADDRESS, det.ADID, det.MARRYTIME,")
                .append(" det.YPTIME, det.MATENAME, det.MATEPHONE, det.ADDRESS, det.MATEWECHAT,det.MATEQQ, ")
                .append(" info.GROUPID, det.GROUPNAME, det.PAYSTYLE, det.HTNUM, det.INVALIDLABEL, det.KEYWORD, log.CONTENT ");
        return sql;
    }

    /**
     * 获取from
     *
     * @param companyId
     * @return
     */
    private StringBuilder getFromSql(int companyId) {
        StringBuilder from = new StringBuilder();
        from.append(" FROM ").append(DBSplitUtil.getInfoTabName(companyId));
        from.append(" info LEFT JOIN ");
        from.append(DBSplitUtil.getDetailTabName(companyId));
        from.append(" det ON info.KZID = det.KZID AND det.COMPANYID = info.COMPANYID ");
        from.append(" LEFT JOIN ");
        from.append(DBSplitUtil.getRemarkTabName(companyId));
        from.append(" log ON log.KZID = info.KZID AND log.COMPANYID = info.COMPANYID ");
        return from;
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
