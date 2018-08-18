package com.qiein.jupiter.web.repository;

import com.qiein.jupiter.enums.TableEnum;
import com.qiein.jupiter.util.DBSplitUtil;
import com.qiein.jupiter.util.StringUtil;
import com.qiein.jupiter.web.entity.vo.DsInvalidVO;
import com.qiein.jupiter.web.entity.vo.KeyWordReportsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 关键词报表
 * author xiangliang
 */
@Repository
public class KeyWordReportsDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    /**
     * 获取报表
     */
    public List<KeyWordReportsVO> getKeyWordReports(String startTime,String endTime,String typeIds,String keyWord,Integer companyId,DsInvalidVO dsInvalidVO){
        List<KeyWordReportsVO>  keyWordReportsVOS=new ArrayList<>();
        String tableInfo= DBSplitUtil.getTable(TableEnum.info,companyId);
        String tableDetail=DBSplitUtil.getTable(TableEnum.detail,companyId);
        getAllClientCount(startTime, endTime, keyWordReportsVOS, typeIds,keyWord, tableInfo, tableDetail);
        getPendingClientCount(startTime, endTime,keyWordReportsVOS, typeIds,keyWord, tableInfo, tableDetail, dsInvalidVO);
        getComeShopClient(startTime, endTime, keyWordReportsVOS, typeIds,keyWord, tableInfo, tableDetail);
        getSuccessClient(startTime, endTime, keyWordReportsVOS, typeIds,keyWord, tableInfo, tableDetail);
        getFilterWaitClientCount(startTime, endTime, keyWordReportsVOS, typeIds,keyWord, tableInfo, tableDetail);
        getFilterInValidClientCount(startTime, endTime, keyWordReportsVOS, typeIds,keyWord,tableInfo, tableDetail);
        getFilterInValidClientCount(startTime, endTime, keyWordReportsVOS, typeIds,keyWord, tableInfo, tableDetail);
        getInValidClientCount(startTime, endTime, keyWordReportsVOS, typeIds,keyWord, tableInfo, tableDetail, dsInvalidVO);
        getAvgAmount(startTime, endTime, keyWordReportsVOS, typeIds,keyWord, tableInfo, tableDetail);
        getAmount(startTime, endTime, keyWordReportsVOS, typeIds,keyWord, tableInfo, tableDetail);
        computerRate(keyWordReportsVOS, dsInvalidVO);
        computerTotal(keyWordReportsVOS);
        return keyWordReportsVOS;
    }
    /**
     * 总客资
     */
    private void getAllClientCount(String startTime, String endTime, List<KeyWordReportsVO> keyWordReportsVOS, String typeIds, String keyWord,String tableInfo, String tableDetail) {
        StringBuilder sql = new StringBuilder();
        getBaseSql(sql,typeIds, tableInfo, tableDetail);
        sql.append(" and info.createtime between ? and ?");
        sql.append(" GROUP BY detail.keyword ");
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql.toString(), new Object[]{keyWord,startTime, endTime});
        for (Map<String, Object> map : list) {
            KeyWordReportsVO keyWordReportsVO = new KeyWordReportsVO();
            keyWordReportsVO.setAllClientCount(Integer.parseInt(Long.toString((Long) (map.get("count")))));
            keyWordReportsVO.setKeyWord((String) map.get("keyWord"));
            keyWordReportsVOS.add(keyWordReportsVO);
        }
    }

    /**
     * 待定量
     */
    private void getPendingClientCount(String startTime, String endTime, List<KeyWordReportsVO> keyWordReportsVOS, String typeIds, String keyWord,String tableInfo, String tableDetail, DsInvalidVO dsInvalidVO) {
        StringBuilder sql = new StringBuilder();
        getBaseSql(sql,typeIds, tableInfo, tableDetail);
        sql.append(" and info.createtime between ? and ?");
        sql.append(" and INSTR( '" + dsInvalidVO.getDsDdStatus() + "', CONCAT(',',info.STATUSID + '',',')) != 0");
        sql.append(" GROUP BY detail.keyword  ");
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql.toString(), new Object[]{keyWord,startTime, endTime});
        List<KeyWordReportsVO> keyWordReportsBak = new LinkedList<>();
        for (Map<String, Object> map : list) {
            KeyWordReportsVO keyWordReportsVO = new KeyWordReportsVO();
            keyWordReportsVO.setPendingClientCount(Integer.parseInt(Long.toString((Long) (map.get("count")))));
            keyWordReportsVO.setKeyWord((String) map.get("keyWord"));
            keyWordReportsBak.add(keyWordReportsVO);
        }
        for (KeyWordReportsVO keyWordReportsVO : keyWordReportsVOS) {
            for (KeyWordReportsVO keyWordReportsVO1 : keyWordReportsBak) {
                    if (keyWordReportsVO.getKeyWord().equalsIgnoreCase(keyWordReportsVO1.getKeyWord()) ) {
                        keyWordReportsVO.setPendingClientCount(keyWordReportsVO1.getPendingClientCount());
                        break;
                    }

            }
        }
    }

    /**
     * 入店量
     */
    private void getComeShopClient(String startTime, String endTime, List<KeyWordReportsVO> keyWordReportsVOS, String typeIds, String keyWord,String tableInfo, String tableDetail) {
        StringBuilder sql = new StringBuilder();
        getBaseSql(sql,typeIds, tableInfo, tableDetail);
        sql.append(" and info.ComeShopTime between ? and ?");
        sql.append(" GROUP BY detail.keyword  ");
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql.toString(), new Object[]{keyWord,startTime, endTime});
        List<KeyWordReportsVO> keyWordReportsBak = new LinkedList<>();
        for (Map<String, Object> map : list) {
            KeyWordReportsVO keyWordReportsVO = new KeyWordReportsVO();
            keyWordReportsVO.setComeShopClientCount(Integer.parseInt(Long.toString((Long) (map.get("count")))));
            keyWordReportsVO.setKeyWord((String) map.get("keyWord"));
            keyWordReportsBak.add(keyWordReportsVO);
        }
        for (KeyWordReportsVO keyWordReportsVO : keyWordReportsVOS) {
            for (KeyWordReportsVO keyWordReportsVO1 : keyWordReportsBak) {
                if (keyWordReportsVO.getKeyWord().equalsIgnoreCase(keyWordReportsVO1.getKeyWord()) ) {
                    keyWordReportsVO.setComeShopClientCount(keyWordReportsVO1.getComeShopClientCount());
                    break;
                }

            }
        }
    }

    /**
     * 成交量
     */
    private void getSuccessClient(String startTime, String endTime, List<KeyWordReportsVO> keyWordReportsVOS, String typeIds, String keyWord,String tableInfo, String tableDetail) {
        StringBuilder sql = new StringBuilder();
        getBaseSql(sql,typeIds, tableInfo, tableDetail);
        sql.append(" and info.SuccessTime between ? and ?");
        sql.append(" GROUP BY detail.keyword  ");
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql.toString(), new Object[]{keyWord,startTime, endTime});
        List<KeyWordReportsVO> keyWordReportsBak = new LinkedList<>();
        for (Map<String, Object> map : list) {
            KeyWordReportsVO keyWordReportsVO = new KeyWordReportsVO();
            keyWordReportsVO.setSuccessClientCount(Integer.parseInt(Long.toString((Long) (map.get("count")))));
            keyWordReportsVO.setKeyWord((String) map.get("keyWord"));
            keyWordReportsBak.add(keyWordReportsVO);
        }
        for (KeyWordReportsVO keyWordReportsVO : keyWordReportsVOS) {
            for (KeyWordReportsVO keyWordReportsVO1 : keyWordReportsBak) {
                if (keyWordReportsVO.getKeyWord().equalsIgnoreCase(keyWordReportsVO1.getKeyWord()) ) {
                    keyWordReportsVO.setSuccessClientCount(keyWordReportsVO1.getSuccessClientCount());
                    break;
                }

            }
        }

    }

    /**
     * 筛选待定
     */
    private void getFilterWaitClientCount(String startTime, String endTime, List<KeyWordReportsVO> keyWordReportsVOS, String typeIds, String keyWord,String tableInfo, String tableDetail) {
        StringBuilder sql = new StringBuilder();
        getBaseSql(sql,typeIds, tableInfo, tableDetail);
        sql.append(" and info.createtime between ? and ?");
        sql.append("  and info.CLASSID = 1 and info.STATUSID = 98 ");
        sql.append(" GROUP BY detail.keyword  ");
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql.toString(), new Object[]{keyWord,startTime, endTime});
        List<KeyWordReportsVO> keyWordReportsBak = new LinkedList<>();
        for (Map<String, Object> map : list) {
            KeyWordReportsVO keyWordReportsVO = new KeyWordReportsVO();
            keyWordReportsVO.setFilterPendingClientCount(Integer.parseInt(Long.toString((Long) (map.get("count")))));
            keyWordReportsVO.setKeyWord((String) map.get("keyWord"));
            keyWordReportsBak.add(keyWordReportsVO);
        }
        for (KeyWordReportsVO keyWordReportsVO : keyWordReportsVOS) {
            for (KeyWordReportsVO keyWordReportsVO1 : keyWordReportsBak) {
                if (keyWordReportsVO.getKeyWord().equalsIgnoreCase(keyWordReportsVO1.getKeyWord()) ) {
                    keyWordReportsVO.setFilterPendingClientCount(keyWordReportsVO1.getFilterPendingClientCount());
                    break;
                }

            }
        }
    }

    /**
     * 筛选无效
     */
    private void getFilterInValidClientCount(String startTime, String endTime, List<KeyWordReportsVO> keyWordReportsVOS, String typeIds, String keyWord,String tableInfo, String tableDetail) {
        StringBuilder sql = new StringBuilder();
        getBaseSql(sql,typeIds, tableInfo, tableDetail);
        sql.append(" and info.createtime between ? and ?");
        sql.append(" and info.CLASSID = 6 and info.STATUSID = 99 ");
        sql.append(" GROUP BY detail.keyword  ");
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql.toString(), new Object[]{keyWord,startTime, endTime});
        List<KeyWordReportsVO> keyWordReportsBak = new LinkedList<>();
        for (Map<String, Object> map : list) {
            KeyWordReportsVO keyWordReportsVO = new KeyWordReportsVO();
            keyWordReportsVO.setFilterInValidClientCount(Integer.parseInt(Long.toString((Long) (map.get("count")))));
            keyWordReportsVO.setKeyWord((String) map.get("keyWord"));
            keyWordReportsBak.add(keyWordReportsVO);
        }
        for (KeyWordReportsVO keyWordReportsVO : keyWordReportsVOS) {
            for (KeyWordReportsVO keyWordReportsVO1 : keyWordReportsBak) {
                if (keyWordReportsVO.getKeyWord().equalsIgnoreCase(keyWordReportsVO1.getKeyWord()) ) {
                    keyWordReportsVO.setFilterInValidClientCount(keyWordReportsVO1.getFilterInValidClientCount());
                    break;
                }

            }
        }
    }

    /**
     * 筛选中
     */
    private void getFilterInClientCount(String startTime, String endTime, List<KeyWordReportsVO> keyWordReportsVOS, String typeIds, String keyWord,String tableInfo, String tableDetail) {
        StringBuilder sql = new StringBuilder();
        getBaseSql(sql,typeIds, tableInfo, tableDetail);
        sql.append(" and info.createtime between ? and ?");
        sql.append(" and info.CLASSID = 1 and info.STATUSID = 0 ");
        sql.append(" GROUP BY detail.keyword  ");
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql.toString(), new Object[]{keyWord,startTime, endTime});
        List<KeyWordReportsVO> keyWordReportsBak = new LinkedList<>();
        for (Map<String, Object> map : list) {
            KeyWordReportsVO keyWordReportsVO = new KeyWordReportsVO();
            keyWordReportsVO.setFilterInClientCount(Integer.parseInt(Long.toString((Long) (map.get("count")))));
            keyWordReportsVO.setKeyWord((String) map.get("keyWord"));
            keyWordReportsBak.add(keyWordReportsVO);
        }
        for (KeyWordReportsVO keyWordReportsVO : keyWordReportsVOS) {
            for (KeyWordReportsVO keyWordReportsVO1 : keyWordReportsBak) {
                if (keyWordReportsVO.getKeyWord().equalsIgnoreCase(keyWordReportsVO1.getKeyWord()) ) {
                    keyWordReportsVO.setFilterInClientCount(keyWordReportsVO1.getFilterInClientCount());
                    break;
                }

            }
        }
    }

    /**
     * 无效
     */
    private void getInValidClientCount(String startTime, String endTime, List<KeyWordReportsVO> keyWordReportsVOS, String typeIds, String keyWord,String tableInfo, String tableDetail, DsInvalidVO dsInvalidVO) {
        StringBuilder sql = new StringBuilder();
        getBaseSql(sql,typeIds, tableInfo, tableDetail);
        sql.append(" and info.createtime between ? and ?");
        if (StringUtil.isNotEmpty(dsInvalidVO.getDsInvalidStatus()) && StringUtil.isNotEmpty(dsInvalidVO.getDsInvalidLevel())) {
            sql.append(" and (info.STATUSID in(" + dsInvalidVO.getDsInvalidStatus() + ") or");
            sql.append("   detail.YXLEVEL IN(" + dsInvalidVO.getDsInvalidLevel() + ") )");
        }
        if (StringUtil.isNotEmpty(dsInvalidVO.getDsInvalidStatus()) && StringUtil.isEmpty(dsInvalidVO.getDsInvalidLevel())) {
            sql.append(" and info.STATUSID in (" + dsInvalidVO.getDsInvalidStatus() + ")");
        }
        if (StringUtil.isNotEmpty(dsInvalidVO.getDsInvalidLevel()) && StringUtil.isEmpty(dsInvalidVO.getDsInvalidStatus())) {
            sql.append(" and detail.YXLEVEL IN(" + dsInvalidVO.getDsInvalidLevel() + ") ");
        }
        sql.append(" GROUP BY detail.keyword  ");
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql.toString(), new Object[]{keyWord,startTime, endTime});
        List<KeyWordReportsVO> keyWordReportsBak = new LinkedList<>();
        for (Map<String, Object> map : list) {
            KeyWordReportsVO keyWordReportsVO = new KeyWordReportsVO();
            keyWordReportsVO.setInValidClientCount(Integer.parseInt(Long.toString((Long) (map.get("count")))));
            keyWordReportsVO.setKeyWord((String) map.get("keyWord"));
            keyWordReportsBak.add(keyWordReportsVO);
        }
        for (KeyWordReportsVO keyWordReportsVO : keyWordReportsVOS) {
            for (KeyWordReportsVO keyWordReportsVO1 : keyWordReportsBak) {
                if (keyWordReportsVO.getKeyWord().equalsIgnoreCase(keyWordReportsVO1.getKeyWord()) ) {
                    keyWordReportsVO.setInValidClientCount(keyWordReportsVO1.getInValidClientCount());
                    break;
                }

            }
        }
    }

    /**
     * 成交均价
     */
    private void getAvgAmount(String startTime, String endTime, List<KeyWordReportsVO> keyWordReportsVOS, String typeIds, String keyWord,String tableInfo, String tableDetail) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT detail.keyword keyword, AVG( detail.AMOUNT) avg_amount ");
        sql.append(" FROM "+tableInfo+" info");
        sql.append(" LEFT JOIN "+tableDetail+" detail ON info.kzid = detail.kzid");

        sql.append(" WHERE detail.keyword IS NOT NULL");
        sql.append(" and detail.keyword !=''");
        if(StringUtil.isNotEmpty(typeIds)){
            sql.append(" and info.typeid in ("+typeIds+") ");
        }
        sql.append(" and detail.keyword like concat('%',?,'%')");
        sql.append(" and info.SuccessTime between ? and ?");
        sql.append(" GROUP BY detail.keyword ");
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql.toString(), new Object[]{ keyWord,startTime, endTime});
        List<KeyWordReportsVO> keyWordReportsBak = new LinkedList<>();
        for (Map<String, Object> map : list) {
            KeyWordReportsVO keyWordReportsVO = new KeyWordReportsVO();
            keyWordReportsVO.setAvgAmount(((BigDecimal) map.get("avg_amount")).doubleValue());
            keyWordReportsVO.setKeyWord((String) map.get("keyWord"));
            keyWordReportsBak.add(keyWordReportsVO);
        }
        for (KeyWordReportsVO keyWordReportsVO : keyWordReportsVOS) {
            for (KeyWordReportsVO keyWordReportsVO1 : keyWordReportsBak) {
                if (keyWordReportsVO.getKeyWord().equalsIgnoreCase(keyWordReportsVO1.getKeyWord()) ) {
                    keyWordReportsVO.setAvgAmount(keyWordReportsVO1.getAvgAmount());
                    break;
                }

            }
        }
    }

    /**
     * 成交总价
     */
    private void getAmount(String startTime, String endTime, List<KeyWordReportsVO> keyWordReportsVOS, String typeIds, String keyWord,String tableInfo, String tableDetail) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT detail.keyword keyword, sum(detail.AMOUNT) as sum_amount ");
        sql.append(" FROM "+tableInfo+" info");
        sql.append(" LEFT JOIN "+tableDetail+" detail ON info.kzid = detail.kzid");
        sql.append(" WHERE detail.keyword IS NOT NULL");
        sql.append(" and detail.keyword !=''");
        if(StringUtil.isNotEmpty(typeIds)){
            sql.append(" and info.typeid in ("+typeIds+") ");
        }
        sql.append(" and detail.keyword like concat('%',?,'%')");
        sql.append(" and info.SuccessTime between ? and ?");
        sql.append(" GROUP BY detail.keyword ");
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql.toString(), new Object[]{keyWord,startTime, endTime});
        List<KeyWordReportsVO> keyWordReportsBak = new LinkedList<>();
        for (Map<String, Object> map : list) {
            KeyWordReportsVO keyWordReportsVO = new KeyWordReportsVO();
            keyWordReportsVO.setAmount(((BigDecimal) map.get("sum_amount")).doubleValue());
            keyWordReportsVO.setKeyWord((String) map.get("keyWord"));
            keyWordReportsBak.add(keyWordReportsVO);
        }
        for (KeyWordReportsVO keyWordReportsVO : keyWordReportsVOS) {
            for (KeyWordReportsVO keyWordReportsVO1 : keyWordReportsBak) {
                if (keyWordReportsVO.getKeyWord().equalsIgnoreCase(keyWordReportsVO1.getKeyWord()) ) {
                    keyWordReportsVO.setAmount(keyWordReportsVO1.getAmount());
                    break;
                }

            }
        }
    }



    /**
     * 计算Rate
     */
    private void computerRate(List<KeyWordReportsVO> oldKzReportsVOS, DsInvalidVO invalidConfig) {
        for (KeyWordReportsVO oldKzReportsVO : oldKzReportsVOS) {
            //有效量
            if (invalidConfig.getDdIsValid()) {
                oldKzReportsVO.setValidClientCount(oldKzReportsVO.getAllClientCount() - oldKzReportsVO.getInValidClientCount() - oldKzReportsVO.getFilterInClientCount() - oldKzReportsVO.getFilterInValidClientCount() - oldKzReportsVO.getFilterPendingClientCount());
            } else {
                oldKzReportsVO.setValidClientCount(oldKzReportsVO.getAllClientCount() - oldKzReportsVO.getPendingClientCount() - oldKzReportsVO.getInValidClientCount() - oldKzReportsVO.getFilterInClientCount() - oldKzReportsVO.getFilterInValidClientCount() - oldKzReportsVO.getFilterPendingClientCount());
            }
            //客资量(总客资-筛选待定-筛选中-筛选无效)
            oldKzReportsVO.setClientCount(oldKzReportsVO.getAllClientCount() - oldKzReportsVO.getFilterPendingClientCount() - oldKzReportsVO.getFilterInValidClientCount() - oldKzReportsVO.getFilterInClientCount());
            //有效率
            double validRate = (double) oldKzReportsVO.getValidClientCount() / oldKzReportsVO.getClientCount();
            oldKzReportsVO.setValidRate(parseDouble(((Double.isNaN(validRate) || Double.isInfinite(validRate)) ? 0.0 : validRate) * 100));
            //无效率
            double invalidRate = (double) oldKzReportsVO.getInValidClientCount() / oldKzReportsVO.getClientCount();
            oldKzReportsVO.setInValidRate(parseDouble(((Double.isNaN(invalidRate) || Double.isInfinite(invalidRate)) ? 0.0 : invalidRate) * 100));
            //待定率
            double waitRate = (double) oldKzReportsVO.getPendingClientCount() / oldKzReportsVO.getClientCount();
            oldKzReportsVO.setWaitRate(parseDouble(((Double.isNaN(waitRate) || Double.isInfinite(waitRate)) ? 0.0 : waitRate) * 100));
            //毛客资入店率
            double clientComeShopRate = (double) oldKzReportsVO.getComeShopClientCount() / oldKzReportsVO.getClientCount();
            oldKzReportsVO.setClientComeShopRate(parseDouble(((Double.isNaN(clientComeShopRate) || Double.isInfinite(clientComeShopRate)) ? 0.0 : clientComeShopRate) * 100));
            //有效客资入店率
            double validComeShopRate = (double) oldKzReportsVO.getComeShopClientCount() / oldKzReportsVO.getValidClientCount();
            oldKzReportsVO.setClientComeShopRate(parseDouble(((Double.isNaN(validComeShopRate) || Double.isInfinite(validComeShopRate)) ? 0.0 : validComeShopRate) * 100));
            //毛客资成交率
            double successRate = (double) oldKzReportsVO.getSuccessClientCount() / oldKzReportsVO.getClientCount();
            oldKzReportsVO.setClientSuccessRate(parseDouble(((Double.isNaN(successRate) || Double.isInfinite(successRate)) ? 0.0 : successRate) * 100));
            //有效客资成交率
            double validSuccessRate = (double) oldKzReportsVO.getSuccessClientCount() / oldKzReportsVO.getValidClientCount();
            oldKzReportsVO.setValidClientSuccessRate(parseDouble(((Double.isNaN(validSuccessRate) || Double.isInfinite(validSuccessRate)) ? 0.0 : validSuccessRate) * 100));
            //入店成交率
            double comeShopSuccessRate = (double) oldKzReportsVO.getSuccessClientCount() / oldKzReportsVO.getComeShopClientCount();
            oldKzReportsVO.setComeShopSuccessRate(parseDouble(((Double.isNaN(comeShopSuccessRate) || Double.isInfinite(comeShopSuccessRate)) ? 0.0 : comeShopSuccessRate) * 100));

        }
    }

    /**
     * 计算总计
     */
    private void computerTotal(List<KeyWordReportsVO> oldKzReportsVOS) {
        KeyWordReportsVO oldKzReportsTotal = new KeyWordReportsVO();
        oldKzReportsTotal.setKeyWord("合计");
        for (KeyWordReportsVO oldKzReportsVO : oldKzReportsVOS) {
            oldKzReportsTotal.setAllClientCount(oldKzReportsVO.getAllClientCount() + oldKzReportsTotal.getAllClientCount());
            oldKzReportsTotal.setClientCount(oldKzReportsVO.getClientCount() + oldKzReportsTotal.getClientCount());
            oldKzReportsTotal.setValidClientCount(oldKzReportsVO.getValidClientCount() + oldKzReportsTotal.getValidClientCount());
            oldKzReportsTotal.setPendingClientCount(oldKzReportsVO.getPendingClientCount() + oldKzReportsTotal.getPendingClientCount());
            oldKzReportsTotal.setInValidClientCount(oldKzReportsVO.getInValidClientCount() + oldKzReportsTotal.getInValidClientCount());
            oldKzReportsTotal.setComeShopClientCount(oldKzReportsVO.getComeShopClientCount() + oldKzReportsTotal.getComeShopClientCount());
            oldKzReportsTotal.setSuccessClientCount(oldKzReportsVO.getSuccessClientCount() + oldKzReportsTotal.getSuccessClientCount());
            oldKzReportsTotal.setAmount(oldKzReportsVO.getAmount() + oldKzReportsTotal.getAmount());
        }
        //客资量(总客资-筛选待定-筛选中-筛选无效)
        oldKzReportsTotal.setClientCount(oldKzReportsTotal.getAllClientCount() - oldKzReportsTotal.getFilterPendingClientCount() - oldKzReportsTotal.getFilterInValidClientCount() - oldKzReportsTotal.getFilterInClientCount());
        //有效率
        double validRate = (double) oldKzReportsTotal.getValidClientCount() / oldKzReportsTotal.getClientCount();
        oldKzReportsTotal.setValidRate(parseDouble(((Double.isNaN(validRate) || Double.isInfinite(validRate)) ? 0.0 : validRate) * 100));
        //无效率
        double invalidRate = (double) oldKzReportsTotal.getInValidClientCount() / oldKzReportsTotal.getClientCount();
        oldKzReportsTotal.setInValidRate(parseDouble(((Double.isNaN(invalidRate) || Double.isInfinite(invalidRate)) ? 0.0 : invalidRate) * 100));
        //待定率
        double waitRate = (double) oldKzReportsTotal.getPendingClientCount() / oldKzReportsTotal.getClientCount();
        oldKzReportsTotal.setWaitRate(parseDouble(((Double.isNaN(waitRate) || Double.isInfinite(waitRate)) ? 0.0 : waitRate) * 100));
        //毛客资入店率
        double clientComeShopRate = (double) oldKzReportsTotal.getComeShopClientCount() / oldKzReportsTotal.getClientCount();
        oldKzReportsTotal.setClientComeShopRate(parseDouble(((Double.isNaN(clientComeShopRate) || Double.isInfinite(clientComeShopRate)) ? 0.0 : clientComeShopRate) * 100));
        //有效客资入店率
        double validComeShopRate = (double) oldKzReportsTotal.getComeShopClientCount() / oldKzReportsTotal.getValidClientCount();
        oldKzReportsTotal.setClientComeShopRate(parseDouble(((Double.isNaN(validComeShopRate) || Double.isInfinite(validComeShopRate)) ? 0.0 : validComeShopRate) * 100));
        //毛客资成交率
        double successRate = (double) oldKzReportsTotal.getSuccessClientCount() / oldKzReportsTotal.getClientCount();
        oldKzReportsTotal.setClientSuccessRate(parseDouble(((Double.isNaN(successRate) || Double.isInfinite(successRate)) ? 0.0 : successRate) * 100));
        //有效客资成交率
        double validSuccessRate = (double) oldKzReportsTotal.getSuccessClientCount() / oldKzReportsTotal.getValidClientCount();
        oldKzReportsTotal.setValidClientSuccessRate(parseDouble(((Double.isNaN(validSuccessRate) || Double.isInfinite(validSuccessRate)) ? 0.0 : validSuccessRate) * 100));
        //入店成交率
        double comeShopSuccessRate = (double) oldKzReportsTotal.getSuccessClientCount() / oldKzReportsTotal.getComeShopClientCount();
        oldKzReportsTotal.setComeShopSuccessRate(parseDouble(((Double.isNaN(comeShopSuccessRate) || Double.isInfinite(comeShopSuccessRate)) ? 0.0 : comeShopSuccessRate) * 100));
        //成交均价
        double avgAmount = oldKzReportsTotal.getAmount() / oldKzReportsTotal.getSuccessClientCount();
        oldKzReportsTotal.setAvgAmount(parseDouble(((Double.isNaN(avgAmount) || Double.isInfinite(avgAmount)) ? 0.0 : avgAmount) ));

        oldKzReportsVOS.add(0, oldKzReportsTotal);
    }

    /**
     * 只保留2位小数
     */
    public double parseDouble(double result) {
        return Double.parseDouble(String.format("%.2f", result));
    }



    private void getBaseSql(StringBuilder sql,String typeIds,String tableInfo,String tableDetail){
        sql.append(" SELECT count(info.id) count,detail.keyword keyWord");
        sql.append(" FROM "+tableInfo+" info");
        sql.append(" LEFT JOIN "+tableDetail+" detail ON info.kzid = detail.kzid");

        sql.append(" WHERE detail.keyword IS NOT NULL");
        sql.append(" and detail.keyword !=''");
        if(StringUtil.isNotEmpty(typeIds)){
            sql.append(" and  info.typeid in ("+typeIds+") ");
        }
        sql.append(" and detail.keyword like concat('%',?,'%')");
    }
}
