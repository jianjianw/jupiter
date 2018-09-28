package com.qiein.jupiter.web.repository;

import com.alibaba.fastjson.JSON;
import com.qiein.jupiter.enums.TableEnum;
import com.qiein.jupiter.exception.ExceptionEnum;
import com.qiein.jupiter.exception.RException;
import com.qiein.jupiter.util.DBSplitUtil;
import com.qiein.jupiter.util.StringUtil;
import com.qiein.jupiter.web.entity.dto.ProvinceAnalysisParamDTO;
import com.qiein.jupiter.web.entity.dto.SourceClientDataDTO;
import com.qiein.jupiter.web.entity.dto.ZjsClientYearReportDTO;
import com.qiein.jupiter.web.entity.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;

import java.lang.reflect.Field;
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

    @Autowired
    private NamedParameterJdbcOperations namedJdbc;

    /**
     * 获取转介绍年度报表
     *
     * @param zjsClientYearReportDTO
     * @param dsInvalidVO
     * @return
     */
    public List<ZjsClientYearReportVO2> getZjsKzYearReport(ZjsClientYearReportDTO zjsClientYearReportDTO, DsInvalidVO dsInvalidVO) {
        List<ZjsClientYearReportVO> resultContent = new LinkedList<>();
        List<Integer> timeList = getMonthTimeStamp(zjsClientYearReportDTO.getYear());
//        int month = 1;
        for (int i = 0; i < timeList.size(); i += 2) {
            String sql = getFinalSQL(zjsClientYearReportDTO, dsInvalidVO);
            Map<String,Object> objs = getParam(zjsClientYearReportDTO, timeList, i, dsInvalidVO);
            System.out.println(Arrays.asList(objs));
            List<ZjsClientYearReportVO> now = namedJdbc.query(sql,
                    objs,
                    new RowMapper<ZjsClientYearReportVO>() {
                        @Override
                        public ZjsClientYearReportVO mapRow(ResultSet rs, int i) throws SQLException {
                            ZjsClientYearReportVO zjsClientYearReportVO = new ZjsClientYearReportVO();
                            List<SourceClientDataDTO> list = new ArrayList<>();
                            do {
                                SourceClientDataDTO scd = new SourceClientDataDTO();
                                scd.setSrcId(rs.getInt("srcId"));
                                scd.setDataNum(rs.getString("dataNum"));
                                scd.setSrcImg(rs.getString("srcImg"));
                                scd.setSrcName(rs.getString("srcName"));
                                list.add(scd);
                            } while (rs.next());
                            zjsClientYearReportVO.setSourceData(list);
                            return zjsClientYearReportVO;
                        }
                    });

            if (now.isEmpty()) {
                ZjsClientYearReportVO empty = new ZjsClientYearReportVO();
                now.add(empty);
            }

            now.get(0).setDataType(zjsClientYearReportDTO.getDataType());
            now.get(0).setMonthName(String.valueOf((i + 2) / 2) + "month");
            resultContent.addAll(now);
        }

        return total(transform(resultContent));
    }

    /**
     * 获取转介绍年度渠道详情报表
     *
     * @param zjsClientYearReportDTO
     * @param dsInvalidVO
     * @return
     */
    public List<Map<String, Object>> getZjsYearDetailReport(ZjsClientYearReportDTO zjsClientYearReportDTO, DsInvalidVO dsInvalidVO) {
        List<RegionReportsVO> resultContent = new ArrayList<>();
        List<Integer> timeList = getMonthTimeStamp(zjsClientYearReportDTO.getYear());
//        int month = 1;
        for (int i = 0; i < timeList.size(); i += 2) {
            String sql = getAllTargetSQL(zjsClientYearReportDTO, dsInvalidVO).toString();
//            System.out.println("输出sql: " + sql);
//            System.out.println(String.valueOf((i + 2) / 2) + "月time: begin: " + timeList.get(i) + " ,end: " + timeList.get(i + 1));
            Map<String,Object> paramMap = new HashMap<>();
            paramMap.put("start",timeList.get(i));
            paramMap.put("end",timeList.get(i+1));
            paramMap.put("zjsValidStatus",dsInvalidVO.getZjsValidStatus());
//            Object[] objs = new Object[]{timeList.get(i), timeList.get(i + 1),
//                    timeList.get(i), timeList.get(i + 1), dsInvalidVO.getZjsValidStatus(),
//                    timeList.get(i), timeList.get(i + 1),
//                    timeList.get(i), timeList.get(i + 1),
//                    timeList.get(i), timeList.get(i + 1),
//                    timeList.get(i), timeList.get(i + 1),
//                    timeList.get(i), timeList.get(i + 1),
//                    timeList.get(i), timeList.get(i + 1),
//                    timeList.get(i), timeList.get(i + 1)};
            List<RegionReportsVO> now = namedJdbc.query(sql, paramMap, new RowMapper<RegionReportsVO>() {
                @Override
                public RegionReportsVO mapRow(ResultSet rs, int rowNum) throws SQLException {
//                    System.out.println(JSON.toJSONString(rs.getString(2)));
                    RegionReportsVO regionReportsVO = new RegionReportsVO();
//                                regionReportsVO.setRegionName(rs.getString("regionName"));
                    regionReportsVO.setAllClientCount(rs.getInt("allClientCount"));                     //总客资
                    regionReportsVO.setPendingClientCount(rs.getInt("pendingClientCount"));             //待定量
                    regionReportsVO.setFilterPendingClientCount(rs.getInt("filterPendingClientCount")); //筛选待定量
                    regionReportsVO.setValidClientCount(rs.getInt("validClientCount"));                 //有效量
                    regionReportsVO.setInValidClientCount(rs.getInt("inValidClientCount"));             //无效量
                    regionReportsVO.setFilterInValidClientCount(rs.getInt("filterInValidClientCount")); //筛选无效量
                    regionReportsVO.setComeShopClientCount(rs.getInt("comeShopClientCount"));           //到店量
                    regionReportsVO.setSuccessClientCount(rs.getInt("successClientCount"));             //成交量
                    regionReportsVO.setFilterInClientCount(rs.getInt("filterInClientCount"));           //筛选中
                    System.out.println(rs.getInt("allClientCount") + "," + rs.getInt("pendingClientCount") + "," +
                            rs.getInt("filterPendingClientCount") + "," + rs.getInt("inValidClientCount") + "," +
                            rs.getInt("inValidClientCount") + "," + rs.getInt("filterInValidClientCount") + "," +
                            rs.getInt("comeShopClientCount") + "," + rs.getInt("successClientCount") + "," +
                            rs.getInt("filterInClientCount"));
                    System.out.println("本条记录： " + regionReportsVO);
                    return regionReportsVO;
                }
            });

            if (now.isEmpty()) {
                RegionReportsVO empty = new RegionReportsVO();
                now.add(empty);
            }

            now.get(0).setRegionName(String.valueOf((i + 2) / 2) + "月");
            resultContent.addAll(now);
        }
//        System.out.println("resultContent: " + JSON.toJSONString(resultContent));
        dTotal(resultContent);
        calculate(resultContent, dsInvalidVO);

        return dtransform(resultContent);
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
        System.out.println("最终输出sql： " + fianlSQL);
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
                .append(" LEFT JOIN (" + getFilterPendingClientCount(zjsClientYearReportDTO) + ") sxdd ON zkz.srcId = sxdd.srcId ")    //筛选待定
                .append(" LEFT JOIN (" + getFilterInClientCount(zjsClientYearReportDTO) + ") sxz ON zkz.srcId = sxz.srcId ")          //筛选中
                .append(" LEFT JOIN (" + getFilterInValidClientCount(zjsClientYearReportDTO) + ") sxwxl ON zkz.srcId = sxwxl.srcId ")  //筛选无效量
        ;

//                .append("(" + getFilterPendingClientCount(provinceAnalysisParamDTO) + ") sxdd , ")     //筛选待定
//                .append("(" + getFilterInClientCount(provinceAnalysisParamDTO) + ") sxz , ")   //筛选中
//                .append("(" + getFilterInValidClientCount(provinceAnalysisParamDTO) + ") sxwxl ");   //筛选无效量

        return sb;
    }

    /**
     * 获取有效量 validClientCount sql 有效量 =  总客资-无效-筛选待定-筛选中-筛选无效-待定量?? 有效量应该是只要符合有效指标就算
     *
     * @param zjsClientYearReportDTO
     */
    private StringBuilder getValidClientSQL(ZjsClientYearReportDTO zjsClientYearReportDTO, DsInvalidVO dsInvalidVO) {
        StringBuilder sb = new StringBuilder();
        getBaseSQL(sb, zjsClientYearReportDTO)
                .append(" AND info.CREATETIME BETWEEN :start AND :end ")
                .append(" AND INSTR( :zjsValidStatus ,CONCAT( '\"'+info.STATUSID , '\"'))>0 ")
                .append(" GROUP BY info.SOURCEID");
        return sb;
    }
//    private StringBuilder getValidClientSQL(ZjsClientYearReportDTO zjsClientYearReportDTO, DsInvalidVO dsInvalidVO) {
//        StringBuilder sb = new StringBuilder();
//        sb.append("SELECT zkz.srcId,zkz.SRCIMG,zkz.SRCNAME,IFNULL(zkz.dataNum,0) - IFNULL(wxl.dataNum,0) - IFNULL(sxdd.dataNum,0) - " +
//                "IFNULL(sxz.dataNum,0) - IFNULL(sxwxl.dataNum,0) - IFNULL(ddl.dataNum,0) dataNum ")
//                .append(" FROM ")
//                .append("(" + getAllClientSQL(zjsClientYearReportDTO) + ") zkz , ")     //总客资
//                .append("(" + getInValidClientSQL(zjsClientYearReportDTO, dsInvalidVO) + ") wxl , ") //无效量
//                .append("(" + getFilterPendingClientCount(zjsClientYearReportDTO) + ") sxdd , ")     //筛选待定
//                .append("(" + getFilterInClientCount(zjsClientYearReportDTO) + ") sxz , ")   //筛选中
//                .append("(" + getFilterInValidClientCount(zjsClientYearReportDTO) + ") sxwxl , ")   //筛选无效量
//                .append("(" + getPendingClientCount(zjsClientYearReportDTO, dsInvalidVO) + ") ddl ");    //待定量
//
//        return sb;
//    }

    /**
     * 获取通用sql头部
     *
     * @param sb
     * @param zjsClientYearReportDTO
     */
    private StringBuilder getBaseSQL(StringBuilder sb, ZjsClientYearReportDTO zjsClientYearReportDTO) {
        String infoTabName = DBSplitUtil.getTable(TableEnum.info, zjsClientYearReportDTO.getCompanyId());
        String detailTabName = DBSplitUtil.getTable(TableEnum.detail, zjsClientYearReportDTO.getCompanyId());
        sb.append(" SELECT info.SOURCEID srcId,src.SRCNAME ,src.SRCIMG , COUNT(1) dataNum")
                .append(" FROM " + infoTabName + " info")
                .append(" INNER JOIN " + detailTabName + " detail ON info.KZID = detail.KZID AND info.COMPANYID = detail.COMPANYID ")
                .append(" INNER JOIN hm_crm_source src ON src.ID = info.SOURCEID AND src.COMPANYID = info.COMPANYID")
                .append(" WHERE info.ISDEL = 0 AND info.COMPANYID = " + zjsClientYearReportDTO.getCompanyId())
                .append(" AND src.TYPEID IN (3,4,5) ");
        if (StringUtil.isNotEmpty(zjsClientYearReportDTO.getSourceIds()) && !zjsClientYearReportDTO.getSourceIds().equals("0")) {
            sb.append(" AND info.SOURCEID IN (" + zjsClientYearReportDTO.getSourceIds() + ") ");
        }
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
        sb.append(" AND (info.COMESHOPTIME BETWEEN :start AND :end) ").append("GROUP BY info.SOURCEID");
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
        sb.append(" AND (info.SUCCESSTIME BETWEEN :start AND :end) ").append("GROUP BY info.SOURCEID");
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
        sb.append(" AND info.CREATETIME BETWEEN :start AND :end ").append("GROUP BY info.SOURCEID");
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
        inValidClientSQL.append(" AND (info.CREATETIME BETWEEN :start AND :end) ");
        if (StringUtil.isNotEmpty(dsInvalidVO.getZjsValidStatus())) {
            inValidClientSQL.append(" AND INSTR('" + dsInvalidVO.getZjsValidStatus() + "',CONCAT( ','+info.STATUSID + '', ','))=0 ");
        }
//        if (StringUtil.isNotEmpty(dsInvalidVO.getDsInvalidStatus()) && StringUtil.isNotEmpty(dsInvalidVO.getDsInvalidLevel())) {
//            inValidClientSQL.append(" and (info.STATUSID in(" + dsInvalidVO.getDsInvalidStatus() + ") or");
//            inValidClientSQL.append("   detail.YXLEVEL IN(" + dsInvalidVO.getDsInvalidLevel() + ") )");
//        }
//        if (StringUtil.isNotEmpty(dsInvalidVO.getDsInvalidStatus()) && StringUtil.isEmpty(dsInvalidVO.getDsInvalidLevel())) {
//            inValidClientSQL.append(" and info.STATUSID in (" + dsInvalidVO.getDsInvalidStatus() + ") ");
//        }
//        if (StringUtil.isNotEmpty(dsInvalidVO.getDsInvalidLevel()) && StringUtil.isEmpty(dsInvalidVO.getDsInvalidStatus())) {
//            inValidClientSQL.append(" and detail.YXLEVEL IN(" + dsInvalidVO.getDsInvalidLevel() + ") ");
//        }
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
                .append(" AND (info.CREATETIME BETWEEN :start AND :end) ");
        filterPendingClientSQL.append("GROUP BY info.SOURCEID");
        return filterPendingClientSQL;
    }

    /**
     * 筛选中量
     */
    private StringBuilder getFilterInClientCount(ZjsClientYearReportDTO zjsClientYearReportDTO) {
        StringBuilder filterInClienSQL = new StringBuilder();
        getBaseSQL(filterInClienSQL, zjsClientYearReportDTO);
        filterInClienSQL.append(" and info.CREATETIME BETWEEN :start AND :end ")
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
                .append(" AND (info.CREATETIME BETWEEN :start AND :end) ").append("GROUP BY info.SOURCEID");
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
        pendingClientSQL.append(" AND (info.CREATETIME BETWEEN :start AND :end) ")
                .append(" AND INSTR( :zjsValidStatus , CONCAT(',',info.STATUSID + '',',')) != 0 ").append(" GROUP BY info.SOURCEID");
        return pendingClientSQL;
    }

    /**
     * 获取参数
     *
     * @return
     */
    public Map<String,Object> getParam(ZjsClientYearReportDTO zjsClientYearReportDTO, List<Integer> timeList, int i, DsInvalidVO dsInvalidVO) {
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("start",timeList.get(i));
        paramMap.put("end",timeList.get(i+1));
        paramMap.put("zjsValidStatus",dsInvalidVO.getZjsValidStatus());
        return paramMap;
//        switch (zjsClientYearReportDTO.getDataType()) {
////            case "总客资":
////                return new Object[]{ timeList.get(i), timeList.get(++i)};
//            case "客资量":
//                return new Object[]{timeList.get(i), timeList.get(i + 1),
//                        timeList.get(i), timeList.get(i + 1),
//                        timeList.get(i), timeList.get(i + 1),
//                        timeList.get(i), timeList.get(i + 1)};
//            case "有效量":
//                return new Object[]{timeList.get(i), timeList.get(i + 1),dsInvalidVO.getZjsValidStatus()};
//            case "入店量":
//                return new Object[]{timeList.get(i), timeList.get(++i)};
//            case "成交量":
//                return new Object[]{timeList.get(i), timeList.get(++i)};
//            default:
//                throw new RException(ExceptionEnum.SEARCH_TYPE_IS_UNKNOW);
//        }
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
     *
     * @param vo1List
     * @return
     */
    public static List<ZjsClientYearReportVO2> transform(List<ZjsClientYearReportVO> vo1List) {
        List<ZjsClientYearReportVO2> newlist = new LinkedList<>();
        Map<Integer, Integer> exist = new HashMap<>();   //key为srcId value为下标
        int count = 0;

        for (ZjsClientYearReportVO zcyr : vo1List) {
            for (SourceClientDataDTO scd : zcyr.getSourceData()) {
                if (!exist.containsKey(scd.getSrcId())) {
                    exist.put(scd.getSrcId(), count++);
                    ZjsClientYearReportVO2 newOne = new ZjsClientYearReportVO2();
                    newOne.setDataType(zcyr.getDataType());
                    newOne.setSrcId(scd.getSrcId());
                    newOne.setSrcName(scd.getSrcName());
                    newOne.setSrcImg(scd.getSrcImg());
                    newOne.setDataMap(new HashMap<String, String>());
                    newOne.getDataMap().put(zcyr.getMonthName(), scd.getDataNum());
                    newlist.add(newOne);
                } else {
                    ZjsClientYearReportVO2 getOne = newlist.get(exist.get(scd.getSrcId()));
                    getOne.getDataMap().put(zcyr.getMonthName(), scd.getDataNum());
                }

            }
        }

        return newlist;
    }

    /**
     * 合计
     *
     * @param list
     * @return
     */
    private static List<ZjsClientYearReportVO2> total(List<ZjsClientYearReportVO2> list) {
        ZjsClientYearReportVO2 hTotal = new ZjsClientYearReportVO2();
        hTotal.setSrcName("合计");
        hTotal.setDataMap(new HashMap<String, String>());
        list.add(0, hTotal);
        for (ZjsClientYearReportVO2 zcyr : list) {
            hTotal.setDataType(zcyr.getDataType());
            Map<String, String> map = zcyr.getDataMap();
            double total = 0;
            for (String key : map.keySet()) {
                if (!hTotal.getDataMap().containsKey(key)) {
                    hTotal.getDataMap().put(key, map.get(key));
                } else {
                    hTotal.getDataMap().put(key, String.valueOf(Integer.valueOf(hTotal.getDataMap().get(key)) + Integer.valueOf(map.get(key))));
                }
                total +=Double.valueOf(map.get(key));
            }
            map.put("合计", String.valueOf(total));
        }

        for (String key : hTotal.getDataMap().keySet()) {
            if (key.equals("合计"))
                continue;
            hTotal.getDataMap().put("合计", String.valueOf(Double.valueOf(hTotal.getDataMap().get(key)) + Double.valueOf(hTotal.getDataMap().get("合计"))));
        }
        return list;
    }

    /**
     * 详情合计
     *
     * @param list
     * @return
     */
    private static List<RegionReportsVO> dTotal(List<RegionReportsVO> list) {
        RegionReportsVO total = new RegionReportsVO();
        total.setRegionName("合计");
        for (RegionReportsVO zcyr : list) {
            total.setAllClientCount(zcyr.getAllClientCount() + total.getAllClientCount());
            total.setClientCount(zcyr.getClientCount() + total.getClientCount());
            total.setValidClientCount(zcyr.getValidClientCount() + total.getValidClientCount());
            total.setInValidClientCount(zcyr.getInValidClientCount() + total.getInValidClientCount());
            total.setComeShopClientCount(zcyr.getComeShopClientCount() + total.getComeShopClientCount());
            total.setSuccessClientCount(zcyr.getSuccessClientCount() + total.getSuccessClientCount());
            total.setPendingClientCount(zcyr.getPendingClientCount() + total.getPendingClientCount());
            total.setFilterInClientCount(zcyr.getFilterInClientCount() + total.getFilterInClientCount());
            total.setFilterInValidClientCount(zcyr.getFilterInValidClientCount() + total.getFilterInValidClientCount());
            total.setFilterPendingClientCount(zcyr.getFilterPendingClientCount() + total.getFilterPendingClientCount());
            total.setAmount(zcyr.getAmount() + total.getAmount());
        }//客资量(总客资-筛选待定-筛选中-筛选无效)
        list.add(total);
        return list;
    }

    //===================================================================================================================================================================================================
    //             渠道年度详情报表
    //===================================================================================================================================================================================================

    /**
     * 获取所有指标sql
     *
     * @return
     */
    private StringBuilder getAllTargetSQL(ZjsClientYearReportDTO zjsClientYearReportDTO, DsInvalidVO dsInvalidVO) {
        StringBuilder sql = new StringBuilder();
        //总客资  待定量 筛选待定 无效量 筛选无效量 入店量 成交量 成交均价 营业额
        sql.append("SELECT ")
                .append("zkz.dataNum allClientCount , ddl.dataNum pendingClientCount , sxdd.dataNum filterPendingClientCount ,yxl.dataNum validClientCount ," +
                        "sxz.dataNum filterInClientCount , wxl.dataNum inValidClientCount , sxwxl.dataNum filterInValidClientCount  , " +
                        "rdl.dataNum comeShopClientCount  , cjl.dataNum successClientCount  ")
                .append(" FROM ")
                .append("(" + getAllClientSQL(zjsClientYearReportDTO) + ") zkz ,") //总客资
                .append("(" + getPendingClientCount(zjsClientYearReportDTO, dsInvalidVO) + ") ddl ,") //待定量
                .append("(" + getFilterPendingClientCount(zjsClientYearReportDTO) + ") sxdd ,") //筛选待定
                .append("(" + getFilterInClientCount(zjsClientYearReportDTO) + ") sxz ,") //筛选中
                .append("(" + getValidClientSQL(zjsClientYearReportDTO, dsInvalidVO) + ")yxl ,")     //有效量
                .append("(" + getInValidClientSQL(zjsClientYearReportDTO, dsInvalidVO) + ") wxl ,") //无效量
                .append("(" + getFilterInValidClientCount(zjsClientYearReportDTO) + ") sxwxl ,")   //筛选无效量
                .append("(" + getComeShopClientSQL(zjsClientYearReportDTO) + ") rdl ,") //入店量
                .append("(" + getSuccessClientSQL(zjsClientYearReportDTO) + ") cjl "); //成交量
        while (sql.indexOf("GROUP BY info.SOURCEID") > 0) {
            sql.replace(sql.indexOf(GROUP_BY), sql.indexOf(GROUP_BY) + GROUP_BY.length(), "");
        }
        return sql;
    }

    /**
     * 转换
     *
     * @param vo1List
     * @return
     */
    public static List<Map<String, Object>> dtransform(List<RegionReportsVO> vo1List) {
        List<Map<String, Object>> list = new ArrayList<>();
        String[] cols = {"clientCount", "validClientCount", "comeShopClientCount", "successClientCount", "validRate", "clientComeShopRate", "comeShopSuccessRate"};

        for (String col : cols) {
            Map<String, Object> map = new HashMap<>();
            map.put("dataType", col);
            Map<String, Object> subMap = new HashMap<>();
            map.put("data", subMap);
            for (RegionReportsVO rr : vo1List) {
                Object value = null;
                try {
                    Field field = rr.getClass().getDeclaredField(col);
                    field.setAccessible(true);
                    value = field.get(rr);
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

                subMap.put(rr.getRegionName(), value);

            }
            list.add(map);
        }
//        System.out.println(list);
        return list;
    }

    /**
     * 计算 有效量 各种率 合计
     */
    private void calculate(List<RegionReportsVO> list, DsInvalidVO invalidConfig) {
        for (RegionReportsVO rrv : list) {
            //有效量(总客资-无效量-筛选中-筛选无效-筛选待定)
//            if (invalidConfig.getDdIsValid()) {
//                rrv.setValidClientCount(rrv.getAllClientCount() - rrv.getInValidClientCount() - rrv.getFilterInClientCount() - rrv.getFilterInValidClientCount() - rrv.getFilterPendingClientCount());
//            } else {// 有效量(总客资-无效量-筛选中-筛选无效-筛选待定-待定量)
//                rrv.setValidClientCount(rrv.getAllClientCount() - rrv.getInValidClientCount() - rrv.getFilterInClientCount() - rrv.getFilterPendingClientCount() - rrv.getFilterInValidClientCount() - rrv.getPendingClientCount());
//            }
            //客资量(总客资-筛选待定-筛选中-筛选无效)
            rrv.setClientCount(rrv.getAllClientCount() - rrv.getFilterPendingClientCount() - rrv.getFilterInValidClientCount() - rrv.getFilterInClientCount());

            //计算各种百分比
            everyRate(rrv);

        }
    }

    /**
     * 计算各种百分比
     *
     * @param rrv
     */
    private void everyRate(RegionReportsVO rrv) {
        //有效率 (有效量 / 客资量)
        double validRate = (double) rrv.getValidClientCount() / rrv.getClientCount();
        rrv.setValidRate(parseDouble(((Double.isNaN(validRate) || Double.isInfinite(validRate)) ? 0.0 : validRate) * 100));
        //毛客资入店率 (入店量 / 总客资)
        double clientComeShopRate = (double) rrv.getComeShopClientCount() / rrv.getAllClientCount();
        rrv.setClientComeShopRate(parseDouble(((Double.isNaN(clientComeShopRate) || Double.isInfinite(clientComeShopRate)) ? 0.0 : clientComeShopRate) * 100));
        //入店成交率（成交量/入店量）
        double comeShopSuccessRate = (double) rrv.getSuccessClientCount() / rrv.getComeShopClientCount();
        rrv.setComeShopSuccessRate(parseDouble(((Double.isNaN(comeShopSuccessRate) || Double.isInfinite(comeShopSuccessRate)) ? 0.0 : comeShopSuccessRate) * 100));
    }

    /**
     * 只保留2位小数
     */
    public double parseDouble(double result) {
        return Double.parseDouble(String.format("%.2f", result));
    }

    private static final String GROUP_BY = "GROUP BY info.SOURCEID";

}
