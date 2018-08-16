package com.qiein.jupiter.web.repository;

import com.qiein.jupiter.enums.TableEnum;
import com.qiein.jupiter.exception.ExceptionEnum;
import com.qiein.jupiter.exception.RException;
import com.qiein.jupiter.util.DBSplitUtil;
import com.qiein.jupiter.util.StringUtil;
import com.qiein.jupiter.web.entity.dto.ProvinceAnalysisParamDTO;
import com.qiein.jupiter.web.entity.dto.ZjsClientYearReportDTO;
import com.qiein.jupiter.web.entity.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.SimpleFormatter;

/**
 * 转介绍年度客资报表
 *
 * @Auther: Tt(yehuawei)
 * @Date: 2018/8/16 18:07
 */
@Repository
public class ZjsKzOfYearDao {

    private static final String PLACEHOLDER = "$Dy$UpUp$";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 获取转介绍年度报表
     *
     * @param zjsClientYearReportDTO
     * @param dsInvalidVO
     * @return
     */
    public List<ZjsClientYearReportVO2> getZjsKzYearReport(ZjsClientYearReportDTO zjsClientYearReportDTO, DsInvalidVO dsInvalidVO) {
        List<ZjsClientYearReportVO2> resultList = new ArrayList<>();
        for (int i = 0; i < zjsClientYearReportDTO.getYear(); i += 2) {
            System.out.println(i);
            String sql = getFinalSQL(zjsClientYearReportDTO, dsInvalidVO);
            Object[] objs = getParam();
            List<ZjsClientYearReportVO> now = jdbcTemplate.query(sql,
                    objs,
                    new RowMapper<ZjsClientYearReportVO>() {
                        @Override
                        public ZjsClientYearReportVO mapRow(ResultSet rs, int i) throws SQLException {
                            ZjsClientYearReportVO zjsClientYearReportVO = new ZjsClientYearReportVO();
                            Map<Integer,Map<String,Object>> dataMap = new HashMap<>();
                            while (rs.next()){
                                Map<String,Object> map = new HashMap<>();
                                map.put("srcId",rs.getInt("srcId"));
                                map.put("srcName",rs.getString("srcName"));
                                map.put("srcImg",rs.getString("srcImg"));
                                map.put("dataNum",rs.getString("dataNum"));
                                dataMap.put(rs.getInt("srcId"),map);
                            }
                            zjsClientYearReportVO.setDataMap(dataMap);
                            return zjsClientYearReportVO;
                        }
                    });
        }

        return resultList;
    }

    /**
     * 获取最后的sql
     *
     * @param zjsClientYearReportDTO
     * @param dsInvalidVO
     * @return
     */
    public String getFinalSQL(ZjsClientYearReportDTO zjsClientYearReportDTO, DsInvalidVO dsInvalidVO) {
        String fianlSQL = setSearchTypeAndSQL(zjsClientYearReportDTO, dsInvalidVO);
        System.out.println("最终输出sql： "+ fianlSQL);
        return fianlSQL;
    }


    /**
     * 将前端传来的查询数据类型转换成字段,然后选择逻辑分支
     *
     * @param zjsClientYearReportDTO
     */
    private String setSearchTypeAndSQL(ZjsClientYearReportDTO zjsClientYearReportDTO, DsInvalidVO invalidConfig) {
        switch (zjsClientYearReportDTO.getDataType()) {
//            case "总客资":
////                provinceAnalysisParamDTO.setSearchType("allClientCount");
//                return getAllClientSQL(provinceAnalysisParamDTO).toString();
            case "客资量":
                return getClientSQL(zjsClientYearReportDTO).toString();
            case "有效量":
                return getValidClientSQL(zjsClientYearReportDTO, invalidConfig).toString();
            case "入店量":
                return getComeShopClientSQL(zjsClientYearReportDTO).toString();
            case "成交量":
                return getSuccessClientSQL(zjsClientYearReportDTO).toString();
            default:
                throw new RException(ExceptionEnum.SEARCH_TYPE_IS_UNKNOW);
        }
    }

    /**
     * 获取客资量 clientCount sql 客资量(总客资-筛选待定-筛选中-筛选无效)
     *
     * @param zjsClientYearReportDTO
     */
    private StringBuilder getClientSQL(ZjsClientYearReportDTO zjsClientYearReportDTO) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT zkz.srcId,zkz.SRCIMG,zkz.SRCNAME,IFNULL(zkz.dataNum,0) - IFNULL(sxdd.dataNum,0) - IFNULL(sxz.dataNum,0) - IFNULL(sxwxl.dataNum,0) dataNum")
                .append(" FROM ")
                .append(" (" + getAllClientSQL(zjsClientYearReportDTO) + ") zkz ")     //总客资
                .append(" LEFT JOIN ("+getFilterPendingClientCount(zjsClientYearReportDTO) + ") sxdd ON zkz.srcId = sxdd.srcId ")    //筛选待定
                .append(" LEFT JOIN ("+getFilterInClientCount(zjsClientYearReportDTO) + ") sxz ON zkz.srcId = sxz.srcId ")          //筛选中
                .append(" LEFT JOIN ("+getFilterInValidClientCount(zjsClientYearReportDTO) + ") sxwxl ON zkz.srcId = sxwxl.srcId ")  //筛选无效量
        ;

//                .append("(" + getFilterPendingClientCount(provinceAnalysisParamDTO) + ") sxdd , ")     //筛选待定
//                .append("(" + getFilterInClientCount(provinceAnalysisParamDTO) + ") sxz , ")   //筛选中
//                .append("(" + getFilterInValidClientCount(provinceAnalysisParamDTO) + ") sxwxl ");   //筛选无效量

        return sb;
    }

    /**
     * 获取有效量 validClientCount sql 有效量 =  总客资-无效-筛选待定-筛选中-筛选无效-待定量
     *
     * @param zjsClientYearReportDTO
     */
    private StringBuilder getValidClientSQL(ZjsClientYearReportDTO zjsClientYearReportDTO, DsInvalidVO dsInvalidVO) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT zkz.srcId,zkz.SRCIMG,zkz.SRCNAME,IFNULL(zkz.dataNum,0) - IFNULL(sxdd.dataNum,0) - IFNULL(sxz.dataNum,0) - IFNULL(sxwxl.dataNum,0) - IFNULL(ddl.dataNum,0) dataNum ")
                .append(" FROM ")
                .append("(" + getAllClientSQL(zjsClientYearReportDTO) + ") zkz , ")     //总客资
                .append("(" + getInValidClientSQL(zjsClientYearReportDTO, dsInvalidVO) + ") wxl , ") //无效量
                .append("(" + getFilterPendingClientCount(zjsClientYearReportDTO) + ") sxdd , ")     //筛选待定
                .append("(" + getFilterInClientCount(zjsClientYearReportDTO) + ") sxz , ")   //筛选中
                .append("(" + getFilterInValidClientCount(zjsClientYearReportDTO) + ") sxwxl , ")   //筛选无效量
                .append("(" + getPendingClientCount(zjsClientYearReportDTO, dsInvalidVO) + ") ddl ");    //待定量

        return sb;
    }

    /**
     * 获取通用sql头部
     *
     * @param sb
     * @param zjsClientYearReportDTO
     */
    private StringBuilder getBaseSQL(StringBuilder sb, ZjsClientYearReportDTO zjsClientYearReportDTO) {
        String infoTabName = DBSplitUtil.getTable(TableEnum.info, zjsClientYearReportDTO.getCompanyId());
        String detailTabName = DBSplitUtil.getTable(TableEnum.detail, zjsClientYearReportDTO.getCompanyId());
        sb.append(" SELECT info.SOURCEID srcId,src.SRCNAME ,src.SRCIMG , COUNT(1) dataNum ")
                .append(" FROM " + infoTabName + " info")
                .append(" INNER JOIN " + detailTabName + " detail ON info.KZID = detail.KZID AND info.COMPANYID = detail.COMPANYID ")
                .append(" INNER JOIN hm_crm_source src ON src.ID = info.SOURCEID AND src.COMPANYID = info.COMPANYID")
                .append(" WHERE info.ISDEL = 0 AND info.COMPANYID = " + zjsClientYearReportDTO.getCompanyId())
                .append(" AND src.TYPEID IN (3,4,5) ");
        return sb;
    }

    /**
     * 获取入店量 comeShopClientCount sql
     *
     * @param zjsClientYearReportDTO
     */
    private StringBuilder getComeShopClientSQL(ZjsClientYearReportDTO zjsClientYearReportDTO) {
        StringBuilder sb = new StringBuilder();
        getBaseSQL(sb, zjsClientYearReportDTO);
        sb.append(" AND (info.COMESHOPTIME BETWEEN ? AND ?) ");
        return sb;
    }

    /**
     * 获取成交量 successClientCount sql
     *
     * @param zjsClientYearReportDTO
     */
    private StringBuilder getSuccessClientSQL(ZjsClientYearReportDTO zjsClientYearReportDTO) {
        StringBuilder sb = new StringBuilder();
        getBaseSQL(sb, zjsClientYearReportDTO);
        sb.append(" AND (info.SUCCESSTIME BETWEEN ? AND ?) ");
        return sb;
    }

    /**
     * 获取总客资量 allClientCount sql
     *
     * @param zjsClientYearReportDTO
     */
    private StringBuilder getAllClientSQL(ZjsClientYearReportDTO zjsClientYearReportDTO) {
        StringBuilder sb = new StringBuilder();
        getBaseSQL(sb, zjsClientYearReportDTO);
        sb.append(" AND info.CREATETIME BETWEEN ? AND ? ").append("GROUP BY info.SOURCEID");
        return sb;
    }

    /**
     * 无效量
     *
     * @return
     */
    private StringBuilder getInValidClientSQL(ZjsClientYearReportDTO zjsClientYearReportDTO, DsInvalidVO dsInvalidVO) {
        StringBuilder inValidClientSQL = new StringBuilder();
        getBaseSQL(inValidClientSQL, zjsClientYearReportDTO);
        inValidClientSQL.append(" AND (info.CREATETIME BETWEEN ? AND ?) ");
        if (StringUtil.isNotEmpty(dsInvalidVO.getDsInvalidStatus()) && StringUtil.isNotEmpty(dsInvalidVO.getDsInvalidLevel())) {
            inValidClientSQL.append(" and (info.STATUSID in(" + dsInvalidVO.getDsInvalidStatus() + ") or");
            inValidClientSQL.append("   detail.YXLEVEL IN(" + dsInvalidVO.getDsInvalidLevel() + ") )");
        }
        if (StringUtil.isNotEmpty(dsInvalidVO.getDsInvalidStatus()) && StringUtil.isEmpty(dsInvalidVO.getDsInvalidLevel())) {
            inValidClientSQL.append(" and info.STATUSID in (" + dsInvalidVO.getDsInvalidStatus() + ")");
        }
        if (StringUtil.isNotEmpty(dsInvalidVO.getDsInvalidLevel()) && StringUtil.isEmpty(dsInvalidVO.getDsInvalidStatus())) {
            inValidClientSQL.append(" and detail.YXLEVEL IN(" + dsInvalidVO.getDsInvalidLevel() + ") ");
        }
        inValidClientSQL.append("GROUP BY info.SOURCEID");
        return inValidClientSQL;
    }

    /**
     * 筛选待定
     *
     * @return
     */
    private StringBuilder getFilterPendingClientCount(ZjsClientYearReportDTO zjsClientYearReportDTO) {
        StringBuilder filterPendingClientSQL = new StringBuilder();
        getBaseSQL(filterPendingClientSQL, zjsClientYearReportDTO);
        filterPendingClientSQL.append(" AND info.CLASSID = 1 and info.STATUSID = 98 ")
                .append(" AND (info.CREATETIME BETWEEN ? AND ?) ");
        filterPendingClientSQL.append("GROUP BY info.SOURCEID");
        return filterPendingClientSQL;
    }

    /**
     * 筛选中量
     */
    private StringBuilder getFilterInClientCount(ZjsClientYearReportDTO zjsClientYearReportDTO) {
        StringBuilder filterInClienSQL = new StringBuilder();
        getBaseSQL(filterInClienSQL, zjsClientYearReportDTO);
        filterInClienSQL.append(" and info.CREATETIME BETWEEN ? AND ? ")
                .append(" and info.CLASSID = 1 and info.STATUSID = 0 ");
        filterInClienSQL.append("GROUP BY info.SOURCEID");
        return filterInClienSQL;
    }

    /**
     * 筛选无效量
     *
     * @return
     */
    private StringBuilder getFilterInValidClientCount(ZjsClientYearReportDTO zjsClientYearReportDTO) {
        StringBuilder filterInValidClientSQL = new StringBuilder();
        getBaseSQL(filterInValidClientSQL, zjsClientYearReportDTO);
        filterInValidClientSQL.append(" and info.CLASSID = 6 and info.STATUSID = 99 ")
                .append(" AND (info.CREATETIME BETWEEN ? AND ?) ").append("GROUP BY info.SOURCEID");
        return filterInValidClientSQL;
    }

    /**
     * 待定量
     *
     * @return
     */
    private StringBuilder getPendingClientCount(ZjsClientYearReportDTO zjsClientYearReportDTO, DsInvalidVO dsInvalidVO) {
        StringBuilder pendingClientSQL = new StringBuilder(); //"pendingClientCount"
        getBaseSQL(pendingClientSQL, zjsClientYearReportDTO);
        pendingClientSQL.append(" AND (info.CREATETIME BETWEEN ? AND ?) ")
                .append(" AND INSTR( ? , CONCAT(',',info.STATUSID + '',',')) != 0").append("GROUP BY info.SOURCEID");
        return pendingClientSQL;
    }

    /**
     * 获取参数
     *
     * @return
     */
    public Object[] getParam() {
        return null;
    }

    /**
     * 获取当年所有的时间
     *
     * @param year
     * @return
     */
    private List<Integer> getMonthTimeStamp(int year) {
        List<Integer> timeList = new ArrayList<>();
        Calendar c = Calendar.getInstance();
        for (int i = 0; i < 12; i++) {
            c.set(year, i, 1, 0, 0, 0);
//            SimpleDateFormat sdf = new SimpleDateFormat("YYYY/MM/dd HH:mm:ss");
            int begin = (int) (c.getTimeInMillis() / 1000);
            timeList.add(begin);
            c.set(year, i, c.getActualMaximum(Calendar.DAY_OF_MONTH), 23, 59, 59);
            int end = (int) (c.getTimeInMillis() / 1000);
            timeList.add(end);
        }
        return timeList;
    }

    /**
     * 转换
     * @param vo1List
     * @return
     */
    public static List<ZjsClientYearReportVO2> transform (List<ZjsClientYearReportVO> vo1List){

        return null;
    }

}
