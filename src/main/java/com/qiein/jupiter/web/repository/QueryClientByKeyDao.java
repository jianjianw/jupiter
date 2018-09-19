package com.qiein.jupiter.web.repository;

import com.qiein.jupiter.exception.ExceptionEnum;
import com.qiein.jupiter.exception.RException;
import com.qiein.jupiter.util.CollectionUtils;
import com.qiein.jupiter.util.DBSplitUtil;
import com.qiein.jupiter.util.StringUtil;
import com.qiein.jupiter.web.entity.vo.ClientVO;
import com.qiein.jupiter.web.entity.vo.SearchClientVO;
import org.apache.commons.lang3.StringUtils;
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
 * 页面查询客资信息
 *
 * @Author: shiTao
 */
@Repository
public class QueryClientByKeyDao {
    @Autowired
    private NamedParameterJdbcOperations namedJdbc;


    /**
     * TODO 搜索 之所以分两个，是否更快？先不要改，因为 索引目前是分了两个
     *
     * @param companyId
     * @param key
     * @return
     */
    public List<SearchClientVO> search(int companyId, final String key) {
        long t = System.currentTimeMillis();
        List<SearchClientVO> clientVOList = searchClientInfoBySearchKey(companyId, key);

        if (CollectionUtils.isEmpty(clientVOList)) {
            clientVOList = searchClientDetailBySearchKey(companyId, key);
        }
        System.out.println(System.currentTimeMillis() - t);

        return clientVOList;
    }

    /**
     * 页面全局搜索客资 根据key  搜索Info表
     *
     * @return
     */
    private List<SearchClientVO> searchClientInfoBySearchKey(int companyId, final String key) {
        final List<SearchClientVO> clientVOList = new ArrayList<>();
        String infoTableName = DBSplitUtil.getInfoTabName(companyId);
        String detailTableName = DBSplitUtil.getDetailTabName(companyId);
        StringBuilder sql = new StringBuilder();
        sql.append(
                "  SELECT info.ID,info.LETTERID, info.KZID, info.KZNAME, info.KZPHONE, info.KZWECHAT, info.KZQQ, " +
                        "info.KZWW, info.CREATETIME, info.STATUSID, info.SOURCEID, ");

        sql.append(" det.APPOINTNAME, det.GROUPNAME ,det.KZID, det.MATENAME, det.MATEWECHAT, det.MATEPHONE, det.MATEQQ ");
        sql.append(" FROM ");
        sql.append(infoTableName);
        sql.append(" info LEFT JOIN ");
        sql.append(detailTableName);
        sql.append(" det ON info.KZID = det.KZID WHERE info.COMPANYID = :companyId AND info.ISDEL = 0 ");

        sql.append("  AND ( info.ID = :key OR info.LETTERID like :key OR info.KZNAME LIKE :key OR info.KZPHONE LIKE " +
                " :key OR info.KZWECHAT LIKE :key OR info.KZWW LIKE :key OR info.KZQQ LIKE :key )");
        Map<String, Object> keyMap = new HashMap<>();
        keyMap.put("companyId", companyId);
        keyMap.put("key", key + "%");

        //查询
        namedJdbc.query(sql.toString(), keyMap, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                SearchClientVO info = new SearchClientVO();
                info.setKey(key);
                info.setId(rs.getString("ID"));
                info.setLetterId(rs.getString("LETTERID"));
                info.setKzId(rs.getString("KZID"));
                info.setKzName(rs.getString("KZNAME"));
                info.setKzPhone(rs.getString("KZPHONE"));
                info.setKzWeChat(rs.getString("KZWECHAT"));
                info.setKzQq(rs.getString("KZQQ"));
                info.setKzWw(rs.getString("KZWW"));
                info.setCreateTime(rs.getInt("CREATETIME"));
                info.setStatusId(rs.getInt("STATUSID"));
                info.setSourceId(rs.getInt("SOURCEID"));
                info.setAppointName(rs.getString("APPOINTNAME"));
                info.setGroupName(rs.getString("GROUPNAME"));
                info.setParam(getMatchShow(info));
                clientVOList.add(info);
            }
        });

        return clientVOList;
    }


    /**
     * 页面全局搜索客资 根据key  搜索详情表
     *
     * @return
     */
    private List<SearchClientVO> searchClientDetailBySearchKey(int companyId, final String key) {
        final List<SearchClientVO> clientVOList = new ArrayList<>();
        String infoTableName = DBSplitUtil.getInfoTabName(companyId);
        String detailTableName = DBSplitUtil.getDetailTabName(companyId);
        StringBuilder sql = new StringBuilder();
        sql.append(
                "   SELECT det.KZID, det.MATENAME, det.MATEWECHAT, det.MATEPHONE, det.MATEQQ, info.CREATETIME," +
                        " info.STATUSID, info.SOURCEID, det.APPOINTNAME, det.GROUPNAME ,  ");
        sql.append(" det.APPOINTNAME, det.GROUPNAME ,det.KZID, det.MATENAME, det.MATEWECHAT, det.MATEPHONE, det.MATEQQ ");
        sql.append(" FROM ");
        sql.append(infoTableName);
        sql.append(" info LEFT JOIN ");
        sql.append(detailTableName);
        sql.append(" det ON info.KZID = det.KZID WHERE info.COMPANYID = :companyId AND info.ISDEL = 0 ");
        sql.append("  AND (   det.MATENAME LIKE :key OR det.MATEWECHAT LIKE :key OR det.MATEPHONE LIKE :key OR det.MATEQQ LIKE :key  )");
        Map<String, Object> keyMap = new HashMap<>();
        keyMap.put("companyId", companyId);
        keyMap.put("key", key + "%");

        //查询
        namedJdbc.query(sql.toString(), keyMap, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                SearchClientVO info = new SearchClientVO();
                info.setKey(key);
                info.setKzId(rs.getString("KZID"));
                info.setMateName(rs.getString("MATENAME"));
                info.setMatePhone(rs.getString("MATEPHONE"));
                info.setMateWeChat(rs.getString("MATEWECHAT"));
                info.setMateQq(rs.getString("MATEQQ"));
                info.setCreateTime(rs.getInt("CREATETIME"));
                info.setStatusId(rs.getInt("STATUSID"));
                info.setSourceId(rs.getInt("SOURCEID"));
                info.setAppointName(rs.getString("APPOINTNAME"));
                info.setGroupName(rs.getString("GROUPNAME"));
                info.setParam(getMatchShow(info));
                clientVOList.add(info);
            }
        });

        return clientVOList;
    }


    /**
     * 拼接显示字符串
     *
     * @param vo
     * @return
     */
    private String getMatchShow(SearchClientVO vo) {
        StringBuilder showSb = new StringBuilder();
        if (StringUtil.isNotEmpty(vo.getKzName())) {
            showSb.append(StringUtil.subStr(vo.getKzName(), 6));
            showSb.append("&nbsp;&nbsp;");
        } else if (StringUtil.isNotEmpty(vo.getMateName())) {
            showSb.append(StringUtil.subStr(vo.getMateName(), 6));
            showSb.append("&nbsp;&nbsp;");
        }
        if (StringUtil.isNotEmpty(vo.getKzName()) && vo.getKzName().contains(vo.getKey())) {
            showSb.append("姓名 ：");
            showSb.append(vo.getKzName().replace(vo.getKey(), ("<b class='col-red'>" + vo.getKey() + "</b>")));
            return showSb.toString();
        }
        if (StringUtil.isNotEmpty(vo.getKzPhone()) && vo.getKzPhone().contains(vo.getKey())) {
            showSb.append("电话：");
            showSb.append(vo.getKzPhone().replace(vo.getKey(), ("<b class='col-red'>" + vo.getKey() + "</b>")));
            return showSb.toString();
        }
        if (StringUtil.isNotEmpty(vo.getKzWeChat()) && vo.getKzWeChat().contains(vo.getKey())) {
            showSb.append("微信：");
            showSb.append(vo.getKzWeChat().replace(vo.getKey(), ("<b class='col-red'>" + vo.getKey() + "</b>")));
            return showSb.toString();
        }
        if (StringUtil.isNotEmpty(vo.getKzQq()) && vo.getKzQq().contains(vo.getKey())) {
            showSb.append("QQ：");
            showSb.append(vo.getKzQq().replace(vo.getKey(), ("<b class='col-red'>" + vo.getKey() + "</b>")));
            return showSb.toString();
        }
        if (StringUtil.isNotEmpty(vo.getKzWw()) && vo.getKzWw().contains(vo.getKey())) {
            showSb.append("旺旺：");
            showSb.append(vo.getKzWw().replace(vo.getKey(), ("<b class='col-red'>" + vo.getKey() + "</b>")));
            return showSb.toString();
        }
//        if (StringUtils.isNotEmpty(vo.getId()) && vo.getId().contains(vo.getKey())) {
//            showSb.append("编号：");
//            showSb.append(vo.getId().replace(vo.getKey(), ("<b class='col-red'>" + vo.getKey() + "</b>")));
//            return showSb.toString();
//        }
        //字母编号
        if (StringUtil.isNotEmpty(vo.getLetterId()) && vo.getLetterId().contains(vo.getKey())) {
            showSb.append("编号：");
            showSb.append(vo.getLetterId().replace(vo.getKey(), ("<b class='col-red'>" + vo.getKey() + "</b>")));
            return showSb.toString();
        }

        if (StringUtil.isNotEmpty(vo.getMateName()) && vo.getMateName().contains(vo.getKey())) {
            showSb.append("配偶姓名：");
            showSb.append(vo.getMateName().replace(vo.getKey(), ("<b class='col-red'>" + vo.getKey() + "</b>")));
            return showSb.toString();
        }
        if (StringUtil.isNotEmpty(vo.getMatePhone()) && vo.getMatePhone().contains(vo.getKey())) {
            showSb.append("配偶电话：");
            showSb.append(vo.getMatePhone().replace(vo.getKey(), ("<b class='col-red'>" + vo.getKey() + "</b>")));
            return showSb.toString();
        }
        if (StringUtil.isNotEmpty(vo.getMateWeChat()) && vo.getMateWeChat().contains(vo.getKey())) {
            showSb.append("配偶微信：");
            showSb.append(vo.getMateWeChat().replace(vo.getKey(), ("<b class='col-red'>" + vo.getKey() + "</b>")));
            return showSb.toString();
        }
        if (StringUtil.isNotEmpty(vo.getMateQq()) && vo.getMateQq().contains(vo.getKey())) {
            showSb.append("配偶QQ：");
            showSb.append(vo.getMateQq().replace(vo.getKey(), ("<b class='col-red'>" + vo.getKey() + "</b>")));
            return showSb.toString();
        }
        return showSb.toString();
    }
}
