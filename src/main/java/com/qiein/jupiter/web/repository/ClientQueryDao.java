package com.qiein.jupiter.web.repository;

import com.alibaba.fastjson.JSONObject;
import com.qiein.jupiter.constant.ClientStatusConst;
import com.qiein.jupiter.constant.CommonConstant;
import com.qiein.jupiter.constant.RoleConstant;
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
import java.text.DecimalFormat;
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
    public PlatPageVO queryDelClientInfo(QueryVO vo) {
        int companyId = vo.getCompanyId();
        final PlatPageVO pageVO = new PlatPageVO();
        pageVO.setCurrentPage(vo.getCurrentPage());
        pageVO.setPageSize(vo.getPageSize());
        //查询参数
        Map<String, Object> keyMap = new HashMap<>();
        keyMap.put("companyId", companyId);
        keyMap.put("page", vo.getCurrentPage() * vo.getPageSize());
        keyMap.put("size", vo.getPageSize());
        //select
        StringBuilder baseSelect = getBaseSelect(true);
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

        int count = namedJdbc.queryForObject(countSql, keyMap, Integer.class);
        pageVO.setTotalCount(count);
        int totalPageNum = (count + pageVO.getPageSize() - 1) / pageVO.getPageSize();
        pageVO.setTotalPage(totalPageNum);

        //执行分页
        pageVO.setData(result);
        return pageVO;
    }

    /**
     * 查询重复客资
     */
    public PlatPageVO checkRepeatInfoHs(QueryVO vo) {
        int companyId = vo.getCompanyId();
        final PlatPageVO pageVO = new PlatPageVO();
        pageVO.setCurrentPage(vo.getCurrentPage());
        pageVO.setPageSize(vo.getPageSize());

        //查询参数
        Map<String, Object> keyMap = new HashMap<>();
        keyMap.put("companyId", companyId);
        keyMap.put("key", vo.getSearchKey());

        StringBuilder baseSelect = getBaseSelect(false);
        baseSelect.append(" FROM hm_crm_client_info info LEFT JOIN hm_crm_client_detail det " +
                " ON info.KZID = det.KZID AND det.COMPANYID = info.COMPANYID  " +
                " WHERE info.COMPANYID = :companyId AND info.ISDEL = 0 " +
                " AND ( info.KZPHONE = :key  OR info.KZWECHAT = :key OR info.KZQQ = :key OR info.KZWW = :key )" +
                " ORDER BY info.ID DESC ");

        //执行查询
        final List<JSONObject> result = new ArrayList<>();
        namedJdbc.query(baseSelect.toString(), keyMap, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                result.add(resultToClientInfo(rs));
            }
        });

        //分页
        String countSql = "SELECT COUNT(*) COUNT " +
                " FROM hm_crm_client_info info LEFT JOIN hm_crm_client_detail det " +
                " ON info.KZID = det.KZID AND det.COMPANYID = info.COMPANYID  " +
                " WHERE info.COMPANYID = :companyId AND info.ISDEL = 0 " +
                " AND ( info.KZPHONE = :key  OR info.KZWECHAT = :key OR info.KZQQ = :key OR info.KZWW = :key )" +
                " ORDER BY info.ID DESC";

        int count = namedJdbc.queryForObject(countSql, keyMap, Integer.class);
        pageVO.setTotalCount(count);
        int totalPageNum = (count + pageVO.getPageSize() - 1) / pageVO.getPageSize();
        pageVO.setTotalPage(totalPageNum);

        //执行分页
        pageVO.setData(result);
        return pageVO;
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
        info.put("typeid", rs.getString("TYPEID"));
        info.put("classid", rs.getString("CLASSID"));
        info.put("statusid", rs.getString("STATUSID"));
        info.put("kzname", rs.getString("KZNAME"));
        info.put("kzphone", rs.getString("KZPHONE"));
        info.put("kzwechat", rs.getString("KZWECHAT"));
        info.put("kzphoneflag", rs.getString("KZPHONE_FLAG"));
        info.put("weflag", rs.getString("WEFLAG"));
        info.put("kzqq", rs.getString("KZQQ"));
        info.put("kzww", rs.getString("KZWW"));
        info.put("sex", rs.getString("SEX"));
        info.put("channelid", rs.getString("CHANNELID"));
        info.put("sourceid", rs.getString("SOURCEID"));
        info.put("collectorid", rs.getString("COLLECTORID"));
        info.put("promotorid", rs.getString("PROMOTORID"));
        info.put("appointorid", rs.getString("APPOINTORID"));
        info.put("receptorid", rs.getString("RECEPTORID"));
        info.put("receivetime", rs.getString("RECEIVETIME"));
        info.put("shopid", rs.getString("SHOPID"));
        info.put("allottype", rs.getString("ALLOTTYPE"));
        info.put("createtime", rs.getString("CREATETIME"));
        info.put("tracetime", rs.getString("TRACETIME"));
        info.put("appointtime", rs.getString("APPOINTTIME"));
        info.put("comeshoptime", rs.getString("COMESHOPTIME"));
        info.put("successtime", rs.getString("SUCCESSTIME"));
        info.put("updatetime", rs.getString("UPDATETIME"));
        info.put("srctype", rs.getString("SRCTYPE"));
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
        info.put("zxstyle", rs.getString("ZXSTYLE"));
        info.put("yxlevel", rs.getString("YXLEVEL"));
        info.put("ysrange", rs.getString("YSRANGE"));
        info.put("adaddress", rs.getString("ADADDRESS"));
        info.put("adid", rs.getString("ADID"));
        info.put("marrytime", rs.getString("MARRYTIME"));
        info.put("yptime", rs.getString("YPTIME"));
        info.put("matename", rs.getString("MATENAME"));
        info.put("matephone", rs.getString("MATEPHONE"));
        info.put("matewechat", rs.getString("MATEWECHAT"));
        info.put("mateqq", rs.getString("MATEQQ"));
        info.put("address", rs.getString("ADDRESS"));
        info.put("groupname", rs.getString("GROUPNAME"));
        info.put("paystyle", rs.getString("PAYSTYLE"));
        info.put("htnum", rs.getString("HTNUM"));
        info.put("invalidlabel", rs.getString("INVALIDLABEL"));
        info.put("filmingcode", rs.getString("FILMINGCODE"));
        info.put("filmingarea", rs.getString("FILMINGAREA"));
        info.put("keyword", rs.getString("KEYWORD"));
        info.put("packagecode", rs.getString("PACKAGECODE"));

        return info;
    }


    /**
     * 获取基础select
     *
     * @return
     */
    public static StringBuilder getBaseSelect(boolean needLog) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT  ");
        sql.append("info.ID, info.KZID, info.TYPEID, info.CLASSID, info.STATUSID,info.LETTERID,info.KZPHONE_FLAG,")
                .append("info.KZNAME, info.KZPHONE, info.KZWECHAT, info.WEFLAG,info.SRCTYPE, ")
                .append("info.KZQQ, info.KZWW, info.SEX, info.CHANNELID, info.SOURCEID, info.COLLECTORID,")
                .append("  info.PROMOTORID, info.APPOINTORID,info.RECEPTORID,info.SHOPID, info.ALLOTTYPE,")
                .append(" info.CREATETIME, info.RECEIVETIME, info.TRACETIME, info.APPOINTTIME, ")
                .append(" .info.COMESHOPTIME, info.SUCCESSTIME, info.UPDATETIME,info.GROUPID, ")

                .append("  det.COLLECTORNAME,det.PROMOTERNAME,det.APPOINTNAME,det.RECEPTORNAME,det.SHOPNAME,")
                .append(" det.PACKAGECODE,det.MEMO,det.FILMINGCODE,det.FILMINGAREA, ")
                .append(" det.OLDKZNAME, det.OLDKZPHONE, det.AMOUNT, det.STAYAMOUNT, det.TALKIMG, det.ORDERIMG, ")
                .append(" det.ZXSTYLE, det.YXLEVEL, det.YSRANGE, det.ADADDRESS, det.ADID, det.MARRYTIME,")
                .append(" det.YPTIME, det.MATENAME, det.MATEPHONE, det.ADDRESS, det.MATEWECHAT,det.MATEQQ, ")
                .append(" det.GROUPNAME, det.PAYSTYLE, det.HTNUM, det.INVALIDLABEL, det.KEYWORD ");
        if (needLog) {
            sql.append("  ,log.CONTENT");
        }
        return sql;
    }

    /**
     * 页面客资搜索
     */
    public PlatPageVO clientSearchPage(QueryVO vo) {
        // 权限限定
        setPmsimit(vo);

        // 职工限定
        setStaffId(vo);

        int companyId = vo.getCompanyId();
        final PlatPageVO pageVO = new PlatPageVO();
        pageVO.setCurrentPage(vo.getCurrentPage());
        pageVO.setPageSize(vo.getPageSize());
        //查询参数
        Map<String, Object> keyMap = new HashMap<>();
        keyMap.put("companyId", companyId);
        keyMap.put("page", vo.getCurrentPage() * vo.getPageSize());
        keyMap.put("size", vo.getPageSize());
        //select
        StringBuilder baseSelect = getBaseSelect(true);
        //from
        StringBuilder fromSql = new StringBuilder();
        fromSql.append(getFromSql(companyId));
        //where
        StringBuilder whereSql = new StringBuilder();
        whereSql.append(" WHERE  info.COMPANYID = :companyId AND info.ISDEL = 0 ");
        // 限制电商和转介绍查看的客资渠道类型
        if (vo.getRole().startsWith("ds")) {
            whereSql.append(" AND ( info.SRCTYPE = 1 OR info.SRCTYPE = 2 ) ");
        } else if (vo.getRole().startsWith("zjs")) {
            whereSql.append(" AND ( info.SRCTYPE = 3 OR info.SRCTYPE = 4 OR info.SRCTYPE = 5 ) ");
        }
        handleWhereSql(vo, keyMap, whereSql);


        //ORDER
        StringBuilder orderLimitSql = new StringBuilder();

        if (StringUtil.isNotEmpty(vo.getSortSpare())) {
            orderLimitSql.append(" ORDER BY ").append(vo.getSortSpare()).append(" ");
        } else {
            orderLimitSql.append(" ORDER BY info.ID DESC ");
        }

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

        int count = namedJdbc.queryForObject(countSql, keyMap, Integer.class);
        pageVO.setTotalCount(count);
        int totalPageNum = (count + pageVO.getPageSize() - 1) / pageVO.getPageSize();
        pageVO.setTotalPage(totalPageNum);

        //执行分页
        pageVO.setData(result);
        //如果说是已订单TAB，则显示订单统计信息
        if (ClientStatusConst.KZ_CLASS_ACTION_SUCCESS.equals(vo.getAction())) {
            pageVO.setOtherData(getOrderCount(vo));
        }
        return pageVO;

    }

    /**
     * 页面客资各个数量查询
     */
    public JSONObject queryPageClientCountInfo(QueryVO vo) {
        JSONObject countJson = new JSONObject();
        int companyId = vo.getCompanyId();
        List<String> actionList = getActionList(vo);
        // 权限限定
        setPmsimit(vo);

        // 职工限定
        setStaffId(vo);

        //查询参数
        Map<String, Object> keyMap = new HashMap<>();
        keyMap.put("companyId", companyId);

        //from
        StringBuilder fromSql = new StringBuilder();
        fromSql.append(getFromSql(companyId));
        //where
        StringBuilder whereSql = new StringBuilder();
        whereSql.append(" WHERE  info.COMPANYID = :companyId AND info.ISDEL = 0 ");
        // 限制电商和转介绍查看的客资渠道类型
        if (vo.getRole().startsWith("ds")) {
            whereSql.append(" AND ( info.SRCTYPE = 1 OR info.SRCTYPE = 2 ) ");
        } else if (vo.getRole().startsWith("zjs")) {
            whereSql.append(" AND ( info.SRCTYPE = 3 OR info.SRCTYPE = 4 OR info.SRCTYPE = 5 ) ");
        }
        handleWhereSql(vo, keyMap, whereSql);

        String countSql = "SELECT COUNT(*) COUNT " + fromSql + whereSql;

        int countAll = namedJdbc.queryForObject(countSql, keyMap, Integer.class);
        countJson.put(ClientStatusConst.KZ_CLASS_ACTION_ALL, countAll);
        for (String action : actionList) {
            if (ClientStatusConst.actionDefault.contains(action)) {
                keyMap.put("action", ClientStatusConst.getClassByAction(action));
                int count = namedJdbc.queryForObject(countSql + " AND info.CLASSID = :action ", keyMap, Integer.class);
                countJson.put(action, count);
            } else {
                // 自定义action
            }
        }
        return countJson;

    }


    /**
     * 页面底部客资 订单数据统计
     *
     * @param vo
     * @return
     */
    public JSONObject getOrderCount(QueryVO vo) {
        // 权限限定
        setPmsimit(vo);

        // 职工限定
        setStaffId(vo);

        int companyId = vo.getCompanyId();
        final PlatPageVO pageVO = new PlatPageVO();
        pageVO.setCurrentPage(vo.getCurrentPage());
        pageVO.setPageSize(vo.getPageSize());
        //查询参数
        Map<String, Object> keyMap = new HashMap<>();
        keyMap.put("companyId", companyId);
        keyMap.put("page", vo.getCurrentPage() * vo.getPageSize());
        keyMap.put("size", vo.getPageSize());
        //select
        StringBuilder baseSelect = new StringBuilder();
        baseSelect.append("SELECT COUNT(1) KZNUM, SUM(det.AMOUNT) SUMAMOUNT, SUM(det.STAYAMOUNT) SUMSTAYMOUNT, AVG(det.AMOUNT) AVGAMOUNT ");
        //from
        StringBuilder fromSql = new StringBuilder();
        fromSql.append(getFromSql(companyId));
        //where
        StringBuilder whereSql = new StringBuilder();
        whereSql.append(" WHERE  info.COMPANYID = :companyId AND info.ISDEL = 0 ");
        // 限制电商和转介绍查看的客资渠道类型
        if (vo.getRole().startsWith("ds")) {
            whereSql.append(" AND ( info.SRCTYPE = 1 OR info.SRCTYPE = 2 ) ");
        } else if (vo.getRole().startsWith("zjs")) {
            whereSql.append(" AND ( info.SRCTYPE = 3 OR info.SRCTYPE = 4 OR info.SRCTYPE = 5 ) ");
        }
        handleWhereSql(vo, keyMap, whereSql);

        String querySql = baseSelect.append(fromSql).append(whereSql).toString();

        //执行查询
        final JSONObject json = new JSONObject();

        namedJdbc.query(querySql, keyMap, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                json.put("kznum", rs.getString("KZNUM"));
                json.put("sumamount", NumUtil.keep2PointZero(rs.getDouble("SUMAMOUNT")));
                json.put("sumstaymount", NumUtil.keep2PointZero(rs.getDouble("SUMSTAYMOUNT")));
                json.put("avgamount", NumUtil.keep2PointZero(rs.getDouble("AVGAMOUNT")));
                if (0.0 != rs.getDouble("SUMAMOUNT")) {
                    json.put("percentage",
                            NumUtil.keep2PointZero(rs.getDouble("SUMSTAYMOUNT") * 100 / rs.getDouble("SUMAMOUNT")) + "%");
                } else {
                    json.put("percentage", "-%");
                }
            }
        });
        return json;

    }


    /**
     * 获取角色action
     *
     * @param vo
     * @return
     */
    private List<String> getActionList(QueryVO vo) {

        String sql = "SELECT cf.ACTION FROM hm_crm_page_conf cf" +
                " WHERE cf.COMPANYID = :companyId AND cf.ROLE = :role ORDER BY cf.PRIORITY ASC";


        //查询参数
        Map<String, Object> keyMap = new HashMap<>();
        keyMap.put("companyId", vo.getCompanyId());
        keyMap.put("role", vo.getRole());


        final List<String> actionList = new ArrayList<>();

        namedJdbc.query(sql, keyMap, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                actionList.add(rs.getString("ACTION"));
            }
        });

        return actionList;
    }

    /**
     * 搜索员工限定
     *
     * @param vo
     */
    private void setStaffId(QueryVO vo) {
        if (StringUtil.isEmpty(vo.getStaffId())) {
            return;
        }
        switch (vo.getRole()) {
            case RoleConstant.DSCJ:
                vo.setCollectorId(vo.getStaffId());
                break;
            case RoleConstant.DSSX:
                vo.setPromotorId(vo.getStaffId());
                break;
            case RoleConstant.DSYY:
                vo.setAppointorId(vo.getStaffId());
                break;
            case RoleConstant.ZJSSX:
                vo.setPromotorId(vo.getStaffId());
                break;
            case RoleConstant.ZJSYY:
                vo.setAppointorId(vo.getStaffId());
                break;
            case RoleConstant.MSJD:
                vo.setReceptorId(vo.getStaffId());
                break;
            case RoleConstant.CWZX:
                vo.setCollectorId(vo.getStaffId());
                break;
            default:
                vo.setCollectorId(vo.getStaffId());
                break;
        }
    }


    /**
     * 权限限定
     */
    private void setPmsimit(QueryVO vo) {
        if (0 == vo.getPmsLimit()) {
            return;
        }
        switch (vo.getPmsLimit()) {
            case 1:
                // 只看自己
                querySelf(vo);
                break;
            case 2:
                // 只看本组
                queryGroup(vo);
                break;
            case 3:
                // 只看部门
                queryDept(vo);
                break;
            case 4:
                // 只看本店
                queryShop(vo);
                break;
            default:
                // 默认只看自己
                querySelf(vo);
                break;
        }

        if (RoleConstant.DSYY.equalsIgnoreCase(vo.getRole())) {
            vo.appendSuperSql(" info.STATUSID != " + ClientStatusConst.BE_ALLOTING + " AND info.STATUSID != "
                    + ClientStatusConst.BE_WAIT_MAKE_ORDER);
        }
    }

    /**
     * 只看自己
     *
     * @param vo
     */
    private void querySelf(QueryVO vo) {
        int staffId = vo.getUid();

        switch (vo.getRole()) {
            case RoleConstant.DSCJ:
                vo.appendCollectorId(staffId);
                break;
            case RoleConstant.DSSX:
                vo.appendPromotorId(staffId);
                break;
            case RoleConstant.DSYY:
                vo.appendAppointorId(staffId);
                break;
            case RoleConstant.ZJSSX:
                vo.appendPromotorId(staffId);
                break;
            case RoleConstant.ZJSYY:
                vo.appendAppointorId(staffId);
                break;
            case RoleConstant.MSJD:
                vo.appendReceptorId(staffId);
                break;
            case RoleConstant.CWZX:
                vo.appendCollectorId(staffId);
                break;
            default:
                vo.appendCollectorId(staffId);
                break;
        }
    }

    /**
     * 只看本组
     */
    private void queryGroup(QueryVO vo) {
        int staffId = vo.getUid();
        int companyId = vo.getCompanyId();
        switch (vo.getRole()) {
            case RoleConstant.DSCJ:
                vo.setCollectorId(getStaffIdsThisGroup(staffId, companyId, RoleConstant.DSCJ));
                break;
            case RoleConstant.DSSX:
                vo.setPromotorId(getStaffIdsThisGroup(staffId, companyId, RoleConstant.DSSX));
                break;
            case RoleConstant.DSYY:
                vo.setAppointorId(getStaffIdsThisGroup(staffId, companyId, RoleConstant.DSYY));
                break;
            case RoleConstant.ZJSSX:
                vo.setPromotorId(getStaffIdsThisGroup(staffId, companyId, RoleConstant.ZJSSX));
                break;
            case RoleConstant.ZJSYY:
                vo.setAppointorId(getStaffIdsThisGroup(staffId, companyId, RoleConstant.ZJSYY));
                break;
            case RoleConstant.MSJD:
                vo.setReceptorId(getStaffIdsThisGroup(staffId, companyId, RoleConstant.MSJD));
                break;
            case RoleConstant.CWZX:
                vo.setCollectorId(getStaffIdsThisGroup(staffId, companyId, RoleConstant.CWZX));
                break;
            default:
                vo.setCollectorId(getStaffIdsThisGroup(staffId, companyId, RoleConstant.CWZX));
                break;
        }
    }

    /**
     * 只看部门
     *
     * @param vo
     */
    private void queryDept(QueryVO vo) {
        int staffId = vo.getUid();
        int companyId = vo.getCompanyId();
        switch (vo.getRole()) {
            case RoleConstant.DSCJ:
                vo.setCollectorId(getStaffIdsThisDept(staffId, companyId, RoleConstant.DSCJ));
                break;
            case RoleConstant.DSSX:
                vo.setPromotorId(getStaffIdsThisDept(staffId, companyId, RoleConstant.DSSX));
                break;
            case RoleConstant.DSYY:
                vo.setAppointorId(getStaffIdsThisDept(staffId, companyId, RoleConstant.DSYY));
                break;
            case RoleConstant.ZJSSX:
                vo.setPromotorId(getStaffIdsThisDept(staffId, companyId, RoleConstant.ZJSSX));
                break;
            case RoleConstant.ZJSYY:
                vo.setAppointorId(getStaffIdsThisDept(staffId, companyId, RoleConstant.ZJSYY));
                break;
            case RoleConstant.MSJD:
                vo.setReceptorId(getStaffIdsThisDept(staffId, companyId, RoleConstant.MSJD));
                break;
            case RoleConstant.CWZX:
                vo.setCollectorId(getStaffIdsThisDept(staffId, companyId, RoleConstant.CWZX));
                break;
            default:
                vo.setCollectorId(getStaffIdsThisDept(staffId, companyId, RoleConstant.CWZX));
                break;
        }
    }

    /**
     * 只看本店
     *
     * @param vo
     */
    private void queryShop(QueryVO vo) {
        int staffId = vo.getUid();
        int companyId = vo.getCompanyId();
        switch (vo.getRole()) {
            case RoleConstant.MSJD:
                vo.setShopId(getShopId(staffId, companyId));
                break;
        }

    }


    private String getShopId(int staffId, int companyId) {
        if (NumUtil.isInValid(staffId) || NumUtil.isInValid(companyId)) {
            return "";
        }
        //查询参数
        Map<String, Object> keyMap = new HashMap<>();
        keyMap.put("companyId", companyId);
        keyMap.put("staffId", staffId);
        String sql = "SELECT grp.SHOPID FROM hm_pub_group_staff rela " +
                " LEFT JOIN hm_pub_group grp ON grp.GROUPID = rela.GROUPID AND grp.COMPANYID = rela.COMPANYID " +
                "  LEFT JOIN hm_pub_shop shop ON shop.ID = grp.SHOPID AND shop.COMPANYID = grp.COMPANYID" +
                " WHERE rela.COMPANYID = :companyId  AND rela.STAFFID =  :staffId  AND shop.ISSHOW = 1";
        final StringBuilder shopIds = new StringBuilder();
        namedJdbc.query(sql, keyMap, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet resultSet) throws SQLException {
                shopIds.append(CommonConstant.STR_SEPARATOR).append(resultSet.getString("SHOPID"));

            }
        });

        if (StringUtil.isNotEmpty(shopIds.toString())) {
            shopIds.append(CommonConstant.STR_SEPARATOR);
        }
        return shopIds.toString();
    }

    /**
     * 获取职工所在小组人员ID集合
     */
    private String getStaffIdsThisGroup(int staffId, int companyId, String groupType) {
        //查询参数
        Map<String, Object> keyMap = new HashMap<>();
        keyMap.put("companyId", companyId);
        keyMap.put("staffId", staffId);
        keyMap.put("groupType", groupType);
        String sql = "SELECT DISTINCT sf.ID FROM hm_pub_group_staff rl " +
                " LEFT JOIN hm_pub_staff sf ON rl.STAFFID = sf.ID AND sf.COMPANYID = :companyId " +
                " WHERE rl.GROUPID IN (" +
                " SELECT DISTINCT grp.GROUPID FROM hm_pub_group_staff rela" +
                " LEFT JOIN hm_pub_group grp ON rela.GROUPID = grp.GROUPID" +
                " AND grp.COMPANYID = :companyId WHERE ( rela.STAFFID = :staffId " +
                " OR INSTR( CONCAT(',', grp.CHIEFIDS, ','), CONCAT(',', :staffId , ',') ) != 0 )" +
                " AND rela.COMPANYID = :companyId AND grp.GROUPTYPE = :groupType )" +
                " AND rl.COMPANYID = :companyId AND sf.ID IS NOT NULL ";

        final StringBuilder ids = new StringBuilder();
        ids.append(CommonConstant.STR_SEPARATOR).append(staffId);

        namedJdbc.query(sql, keyMap, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet resultSet) throws SQLException {
                ids.append(CommonConstant.STR_SEPARATOR).append(resultSet.getString("ID"));

            }
        });
        ids.append(CommonConstant.STR_SEPARATOR);

        return ids.toString();
    }


    /**
     * 获取职工所在部门人员ID集合
     */
    private String getStaffIdsThisDept(int staffId, int companyId, String groupType) {
        //查询参数
        Map<String, Object> keyMap = new HashMap<>();
        keyMap.put("companyId", companyId);
        keyMap.put("staffId", staffId);
        keyMap.put("groupType", groupType);
        String sql = " SELECT DISTINCT sf.ID FROM hm_pub_group_staff rl LEFT JOIN " +
                " hm_pub_staff sf ON rl.STAFFID = sf.ID AND sf.COMPANYID = :companyId " +
                " LEFT JOIN hm_pub_group grp ON rl.GROUPID = grp.GROUPID AND grp.COMPANYID = :companyId " +
                " WHERE grp.PARENTID IN ( " +
                " SELECT DISTINCT grp.PARENTID FROM hm_pub_group_staff rela " +
                " LEFT JOIN hm_pub_group grp ON rela.GROUPID = grp.GROUPID AND grp.COMPANYID = :companyId " +
                " LEFT JOIN hm_pub_group sp ON grp.PARENTID = sp.GROUPID AND sp.COMPANYID = :companyId " +
                " WHERE ( rela.STAFFID = :staffId OR INSTR( CONCAT(',', grp.CHIEFIDS, ','), CONCAT(',', :staffId , ',') ) != 0" +
                " OR INSTR( CONCAT(',', sp.CHIEFIDS, ','), CONCAT(',', :staffId , ',') ) != 0 ) " +
                " AND rela.COMPANYID = :companyId AND grp.GROUPTYPE = :groupType ) AND grp.GROUPTYPE = :groupType " +
                " AND rl.COMPANYID = :companyId AND sf.ID IS NOT NULL";
        //ids
        final StringBuilder ids = new StringBuilder();
        ids.append(CommonConstant.STR_SEPARATOR).append(staffId);
        namedJdbc.query(sql, keyMap, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet resultSet) throws SQLException {
                ids.append(CommonConstant.STR_SEPARATOR).append(resultSet.getString("ID"));

            }
        });
        ids.append(CommonConstant.STR_SEPARATOR);

        return ids.toString();
    }


    /**
     * 获取from
     *
     * @param companyId
     * @return
     */
    public static StringBuilder getFromSql(int companyId) {
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
                    .append(" %' OR info.KZNAME LIKE '")
                    .append(vo.getSearchKey())
                    .append(" %' OR info.KZPHONE LIKE '")
                    .append(vo.getSearchKey()).append(" %' OR info.KZWECHAT LIKE '")
                    .append(vo.getSearchKey()).append(" %' OR info.KZWW LIKE '")
                    .append(vo.getSearchKey()).append(" %' OR info.KZQQ LIKE '")
                    .append(vo.getSearchKey()).append(" %' ) ");
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
    private static String dynamixSql(String param, String spitStr, String column) {

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
                sb.append(" OR ");
            } else {
                sb.append(" ) ");
            }
        }
        return sb.toString();
    }
}
