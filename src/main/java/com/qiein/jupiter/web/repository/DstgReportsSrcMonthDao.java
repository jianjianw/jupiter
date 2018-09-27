package com.qiein.jupiter.web.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import com.qiein.jupiter.enums.TableEnum;
import com.qiein.jupiter.web.entity.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.qiein.jupiter.util.DBSplitUtil;
import com.qiein.jupiter.util.StringUtil;
import com.qiein.jupiter.web.entity.vo.DsInvalidVO;
import com.qiein.jupiter.web.entity.vo.ReportsParamSrcMonthVO;

/**
 * @author: Hjf
 * @Date: 2018-8-10
 */
@Repository
public class DstgReportsSrcMonthDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * 获取报表内详情数据
	 */
	public ZjskzOfMonthMapVO ZjskzOfMonthIn(List<Map<String, Object>> dayList, Integer companyId, String month, String sourceId, DsInvalidVO dsInvalidVO,String typeId) {
		List<ZjskzOfMonthReportsVO> zjskzOfMonthReportsVOS = new ArrayList<>();
		String tableInfo = DBSplitUtil.getTable(TableEnum.info, companyId);
		//总客资
		getAllClientCount(dayList, tableInfo, month, zjskzOfMonthReportsVOS, sourceId, companyId,typeId);
		//待定
		getPendingClientCount(dayList, tableInfo, month, zjskzOfMonthReportsVOS, sourceId, companyId, dsInvalidVO,typeId);
		//筛选待定
		getFilterWaitClientCount(dayList, tableInfo, month, zjskzOfMonthReportsVOS, sourceId, companyId,typeId);
		//筛选无效
		getFilterInValidClientCount(dayList, tableInfo, month, zjskzOfMonthReportsVOS, sourceId, companyId,typeId);
		//无效
		getValidClientCount(dayList, tableInfo, month, zjskzOfMonthReportsVOS, sourceId, companyId, dsInvalidVO,typeId);
		//筛选中
		getFilterInClientCount(dayList, tableInfo, month, zjskzOfMonthReportsVOS, sourceId, companyId,typeId);
		//入店量
		getComeShopClient(dayList, tableInfo, month, zjskzOfMonthReportsVOS, sourceId, companyId,typeId);
		//成交量
		getSuccessClient(dayList, tableInfo, month, zjskzOfMonthReportsVOS, sourceId, companyId,typeId);
		computerRate(zjskzOfMonthReportsVOS, dsInvalidVO);
		ZjskzOfMonthMapVO zjskzOfMonthMapVO = new ZjskzOfMonthMapVO();
		List<Map<String,Object>> list=new ArrayList<>();
		Map<String, Object> clientCountMap = new HashMap<>();
		clientCountMap.put("name", "客资量");
		for (ZjskzOfMonthReportsVO zjskzOfMonthReportsVO : zjskzOfMonthReportsVOS) {
			clientCountMap.put(zjskzOfMonthReportsVO.getDayId(), zjskzOfMonthReportsVO.getClientCount());
		}
		list.add(clientCountMap);
		Map<String, Object> validClientCountMap = new HashMap<>();
		validClientCountMap.put("name", "有效量");
		for (ZjskzOfMonthReportsVO zjskzOfMonthReportsVO : zjskzOfMonthReportsVOS) {
			validClientCountMap.put(zjskzOfMonthReportsVO.getDayId(), zjskzOfMonthReportsVO.getValidClientCount());
		}
		list.add(validClientCountMap);
		Map<String, Object> comeShopClientCountMap = new HashMap<>();
		comeShopClientCountMap.put("name", "入店量");
		for (ZjskzOfMonthReportsVO zjskzOfMonthReportsVO : zjskzOfMonthReportsVOS) {
			comeShopClientCountMap.put(zjskzOfMonthReportsVO.getDayId(), zjskzOfMonthReportsVO.getComeShopClientCount());
		}
		list.add(comeShopClientCountMap);
		Map<String, Object> successClientCountMap = new HashMap<>();
		successClientCountMap.put("name", "成交量");
		for (ZjskzOfMonthReportsVO zjskzOfMonthReportsVO : zjskzOfMonthReportsVOS) {
			successClientCountMap.put(zjskzOfMonthReportsVO.getDayId(), zjskzOfMonthReportsVO.getSuccessClientCount());
		}
		list.add(successClientCountMap);
		Map<String, Object> validRateMap = new HashMap<>();
		validRateMap.put("name", "有效率");
		for (ZjskzOfMonthReportsVO zjskzOfMonthReportsVO : zjskzOfMonthReportsVOS) {
			validRateMap.put(zjskzOfMonthReportsVO.getDayId(), zjskzOfMonthReportsVO.getValidRate());
		}
		list.add(validRateMap);
		Map<String, Object> validClientComeShopRateMap = new HashMap<>();
		validClientComeShopRateMap.put("name", "入店率");
		for (ZjskzOfMonthReportsVO zjskzOfMonthReportsVO : zjskzOfMonthReportsVOS) {
			validClientComeShopRateMap.put(zjskzOfMonthReportsVO.getDayId(), zjskzOfMonthReportsVO.getClientComeShopRate());
		}
		list.add(validClientComeShopRateMap);
		Map<String, Object> comeShopSuccessRateMap = new HashMap<>();
		comeShopSuccessRateMap.put("name", "成交率");
		for (ZjskzOfMonthReportsVO zjskzOfMonthReportsVO : zjskzOfMonthReportsVOS) {
			comeShopSuccessRateMap.put(zjskzOfMonthReportsVO.getDayId(), zjskzOfMonthReportsVO.getComeShopSuccessRate());
		}
		list.add(comeShopSuccessRateMap);
		zjskzOfMonthMapVO.setList(list);
		return zjskzOfMonthMapVO;
	}
	
	/**
     * 获取总客资量--报表外
     * @param start
     * @param end
     * @param companyId
     * */
	public List<Map<String, Object>> getDSTGSrcMonthReportsSum(int firstDay , int lastDay,ReportsParamSrcMonthVO reportsParamSrcMonthVO,
			DsInvalidVO dsInvalidVO) {
		StringBuilder sql = new StringBuilder();
		//String infoTabName = DBSplitUtil.getInfoTabName(reportsParamSrcMonthVO.getCompanyId());
		sql.append("SELECT info.SOURCEID,src.SRCNAME,FROM_UNIXTIME(info.CREATETIME, '%d') time,COUNT(1) count,src.SRCIMG srcImg ");
		sql.append(" FROM hm_crm_client_info info ");
		sql.append(" LEFT JOIN hm_crm_source src ON src.ID=info.SOURCEID ");
		sql.append(" WHERE info.ISDEL = 0");
			if(StringUtil.isNotEmpty(reportsParamSrcMonthVO.getTypeId())){
				sql.append(" AND info.TYPEID IN("+reportsParamSrcMonthVO.getTypeId()+") ");
			}
		sql.append(" AND info.CREATETIME BETWEEN "+firstDay+" AND "+ lastDay);	
		sql.append(" AND src.TYPEID IN(1,2)");
			if (StringUtil.isNotEmpty(reportsParamSrcMonthVO.getSourceId())) {
				sql.append(" AND src.ID IN (" + reportsParamSrcMonthVO.getSourceId() + ")");
			}
		sql.append(" AND info.COMPANYID=? ");
		sql.append(" GROUP BY info.SOURCEID,time ");
		sql.append(" ORDER BY info.SOURCEID ");
        String sqlString = sql.toString(); 
        List<Map<String, Object>> listSum = jdbcTemplate.queryForList(sqlString, new Object[]{reportsParamSrcMonthVO.getCompanyId()});
        // 最终用返回
 		List<Map<String, Object>> newmaps = new ArrayList<>();

 		for (Map<String, Object> map6 : listSum) {
 			Map<String, Object> row = new HashMap<>();
 			row.put("SOURCEID", map6.get("SOURCEID"));
 			row.put("SRCNAME", map6.get("SRCNAME"));
 			row.put("srcImg", map6.get("srcImg"));
 			row.put((String) map6.get("time"), map6.get("count"));
 			//合计
 			int count=Integer.valueOf(map6.get("count").toString());
 			boolean flag = false;
 			for (Map<String, Object> newmap : newmaps) {
 				if (newmap.get("SOURCEID").equals(row.get("SOURCEID"))) {
 					newmap.put((String) map6.get("time"), map6.get("count"));
 					Integer total = (Integer)newmap.get("total");
 					newmap.put("total", total + count);
 					flag = true;
 					break;
 				}
 			}
 			if (!flag) {
 				row.put("total",count);
 				newmaps.add(row);
 			}
 		}	
        //合计横向的一行
 		Map<String,Object> map1=new LinkedHashMap();
 		int Monthsum=0;
        for (Map<String, Object> map : newmaps) {
        		Monthsum +=Integer.valueOf(String.valueOf(map.get("total").toString()));
    		}
        	map1.put("srcName", "合计");
        	map1.put("total", Monthsum);
 		String[] days={"01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31"};
 		for (String day : days) {
 			int daysum=0;
 			for (Map<String, Object> map : newmaps) {
 				if(map.containsKey(day)){
 					daysum+=Integer.valueOf(String.valueOf(map.get(day).toString()));
 				}
 			}map1.put(day, daysum);
		}
 		newmaps.add(0,map1);
		return newmaps;
	}
	
	/**
     * 获取客资量--报表外
     * @param start
     * @param end
     * @param companyId
     * */
	public List<Map<String, Object>> getDSTGSrcMonthReportsAll(int firstDay , int lastDay ,ReportsParamSrcMonthVO reportsParamSrcMonthVO,
			DsInvalidVO invalidConfig) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT info.SOURCEID,src.SRCNAME,FROM_UNIXTIME(info.CREATETIME, '%d') time,COUNT(1) count,src.SRCIMG srcImg ");
		sql.append(" FROM hm_crm_client_info info ");
		sql.append(" LEFT JOIN hm_crm_source src ON src.ID=info.SOURCEID ");
		sql.append(" WHERE info.ISDEL = 0 AND info.STATUSID NOT IN(0,98,99) ");
			if(StringUtil.isNotEmpty(reportsParamSrcMonthVO.getTypeId())){
				sql.append(" AND info.TYPEID IN("+reportsParamSrcMonthVO.getTypeId()+") ");
			}
		sql.append(" AND info.CREATETIME BETWEEN "+firstDay+" AND "+ lastDay);	
		sql.append(" AND src.TYPEID IN(1,2)");
			if (StringUtil.isNotEmpty(reportsParamSrcMonthVO.getSourceId())) {
				sql.append(" AND src.ID IN (" + reportsParamSrcMonthVO.getSourceId() + ")");
			}
		sql.append(" AND info.COMPANYID=? ");
		sql.append(" GROUP BY info.SOURCEID,time ");
		sql.append(" ORDER BY info.SOURCEID ");
        String sqlString = sql.toString(); 
        List<Map<String, Object>> listAll = jdbcTemplate.queryForList(sqlString, new Object[]{reportsParamSrcMonthVO.getCompanyId()});
        // 最终用返回
 		List<Map<String, Object>> newmaps = new ArrayList<>();

 		for (Map<String, Object> map6 : listAll) {
 			Map<String, Object> row = new HashMap<>();
 			row.put("SOURCEID", map6.get("SOURCEID"));
 			row.put("SRCNAME", map6.get("SRCNAME"));
 			row.put("srcImg", map6.get("srcImg"));
 			row.put((String) map6.get("time"), map6.get("count"));
 			//合计
 			int count=Integer.valueOf(map6.get("count").toString());
 			boolean flag = false;
 			for (Map<String, Object> newmap : newmaps) {
 				if (newmap.get("SOURCEID").equals(row.get("SOURCEID"))) {
 					newmap.put((String) map6.get("time"), map6.get("count"));
 					Integer total = (Integer)newmap.get("total");
 					newmap.put("total", total + count);
 					flag = true;
 					break;
 				}
 			}
 			if (!flag) {
 				row.put("total",count);
 				newmaps.add(row);
 			}
 		}
        //合计横向的一行
        Map<String,Object> map1=new LinkedHashMap();
 		int Monthsum=0;
        for (Map<String, Object> map : newmaps) {
        		Monthsum +=Integer.valueOf(String.valueOf(map.get("total").toString()));
    		}
        	map1.put("srcName", "合计");
        	map1.put("total", Monthsum);
 		String[] days={"01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31"};
 		for (String day : days) {
 			int daysum=0;
 			for (Map<String, Object> map : newmaps) {
 				if(map.containsKey(day)){
 					daysum+=Integer.valueOf(String.valueOf(map.get(day).toString()));
 				}
 			}map1.put(day, daysum);
		}
 		newmaps.add(0,map1);
		return newmaps;
	}
	
	/**
     * 获取待定客资量--报表外
     * @param start
     * @param end
     * @param companyId
     * */
	public List<Map<String, Object>> getDSTGSrcMonthReportsDdNum(int firstDay , int lastDay,ReportsParamSrcMonthVO reportsParamSrcMonthVO,
			DsInvalidVO invalidConfig) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT info.SOURCEID,src.SRCNAME,FROM_UNIXTIME(info.CREATETIME, '%d') time,COUNT(1) count,src.SRCIMG srcImg ");
		sql.append(" FROM hm_crm_client_info info ");
		sql.append(" LEFT JOIN hm_crm_source src ON src.ID=info.SOURCEID ");
		sql.append(" WHERE info.ISDEL = 0 AND info.STATUSID NOT IN(0,98,99) ");
			if(StringUtil.isNotEmpty(invalidConfig.getDsDdStatus())){
				sql.append(" AND info.STATUSID IN("+invalidConfig.getDsDdStatus()+")");
			}
			if(StringUtil.isNotEmpty(reportsParamSrcMonthVO.getTypeId())){
				sql.append(" AND info.TYPEID IN("+reportsParamSrcMonthVO.getTypeId()+") ");
			}
		sql.append(" AND info.CREATETIME BETWEEN "+firstDay+" AND "+ lastDay);	
		sql.append(" AND src.TYPEID IN(1,2)");
			if (StringUtil.isNotEmpty(reportsParamSrcMonthVO.getSourceId())) {
				sql.append(" AND src.ID IN (" + reportsParamSrcMonthVO.getSourceId() + ")");
			}
		sql.append(" AND info.COMPANYID=? ");
		sql.append(" GROUP BY info.SOURCEID,time ");
		sql.append(" ORDER BY info.SOURCEID ");
        String sqlString = sql.toString(); 
        List<Map<String, Object>> listAll = jdbcTemplate.queryForList(sqlString, new Object[]{reportsParamSrcMonthVO.getCompanyId()});
        // 最终用返回
 		List<Map<String, Object>> newmaps = new ArrayList<>();

 		for (Map<String, Object> map6 : listAll) {
 			Map<String, Object> row = new HashMap<>();
 			row.put("SOURCEID", map6.get("SOURCEID"));
 			row.put("SRCNAME", map6.get("SRCNAME"));
 			row.put("srcImg", map6.get("srcImg"));
 			row.put((String) map6.get("time"), map6.get("count"));
 			//合计
 			int count=Integer.valueOf(map6.get("count").toString());
 			boolean flag = false;
 			for (Map<String, Object> newmap : newmaps) {
 				if (newmap.get("SOURCEID").equals(row.get("SOURCEID"))) {
 					newmap.put((String) map6.get("time"), map6.get("count"));
 					Integer total = (Integer)newmap.get("total");
 					newmap.put("total", total + count);
 					flag = true;
 					break;
 				}
 			}
 			if (!flag) {
 				row.put("total",count);
 				newmaps.add(row);
 			}
 		}
        //合计横向的一行
 		Map<String,Object> map1=new LinkedHashMap();
 		int Monthsum=0;
        for (Map<String, Object> map : newmaps) {
        		Monthsum +=Integer.valueOf(String.valueOf(map.get("total").toString()));
    		}
        	map1.put("srcName", "合计");
        	map1.put("total", Monthsum);
 		String[] days={"01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31"};
 		for (String day : days) {
 			int daysum=0;
 			for (Map<String, Object> map : newmaps) {
 				if(map.containsKey(day)){
 					daysum+=Integer.valueOf(String.valueOf(map.get(day).toString()));
 				}
 			}map1.put(day, daysum);
		}
 		newmaps.add(0,map1);
		return newmaps;
	}
	
	/**
     * 获取无效客资量--报表外
     * @param start
     * @param end
     * @param companyId
     * */
	public List<Map<String, Object>> getDSTGSrcMonthReportsInvalid(int firstDay , int lastDay,ReportsParamSrcMonthVO reportsParamSrcMonthVO,
			DsInvalidVO invalidConfig) {
		StringBuilder sql = new StringBuilder();
		
		sql.append("SELECT info.SOURCEID,src.SRCNAME,FROM_UNIXTIME(info.CREATETIME, '%d') time,COUNT(1) count,src.SRCIMG srcImg ");
		sql.append(" FROM hm_crm_client_info info ");
		sql.append(" LEFT JOIN hm_crm_source src ON src.ID=info.SOURCEID ");
		sql.append(" LEFT JOIN hm_crm_client_detail deta ON info.KZID=deta.KZID ");
		sql.append(" WHERE info.ISDEL = 0 AND info.STATUSID NOT IN(0,98,99) ");
			//无效指标&&意向等级
        	if(StringUtil.isNotEmpty(invalidConfig.getDsInvalidStatus()) && StringUtil.isNotEmpty(invalidConfig.getDsInvalidLevel())){
        		sql.append(" AND ( info.STATUSID  IN("+invalidConfig.getDsInvalidStatus()+")"+ "OR deta.YXLEVEL IN("+invalidConfig.getDsInvalidLevel()+"))");
        	}
        	//只有无效指标
        	if(StringUtil.isNotEmpty(invalidConfig.getDsInvalidStatus()) && StringUtil.isEmpty(invalidConfig.getDsInvalidLevel())){
        		sql.append(" AND info.STATUSID  IN("+invalidConfig.getDsInvalidStatus()+")");
        	}
        	//只有意向指标
        	if(StringUtil.isEmpty(invalidConfig.getDsInvalidStatus()) && StringUtil.isNotEmpty(invalidConfig.getDsInvalidLevel())){
        		sql.append(" AND deta.YXLEVEL IN("+invalidConfig.getDsInvalidLevel()+")");
        	}
			if(StringUtil.isNotEmpty(reportsParamSrcMonthVO.getTypeId())){
				sql.append(" AND info.TYPEID IN("+reportsParamSrcMonthVO.getTypeId()+") ");
			}
		sql.append(" AND info.CREATETIME BETWEEN "+firstDay+" AND "+ lastDay);	
		sql.append(" AND src.TYPEID IN(1,2)");
			if (StringUtil.isNotEmpty(reportsParamSrcMonthVO.getSourceId())) {
				sql.append(" AND src.ID IN (" + reportsParamSrcMonthVO.getSourceId() + ")");
			}
		sql.append(" AND info.COMPANYID=? ");
		sql.append(" GROUP BY info.SOURCEID,time ");
		sql.append(" ORDER BY info.SOURCEID ");
        String sqlString = sql.toString(); 
        List<Map<String, Object>> listAll = jdbcTemplate.queryForList(sqlString, new Object[]{reportsParamSrcMonthVO.getCompanyId()});
        // 最终用返回
 		List<Map<String, Object>> newmaps = new ArrayList<>();

 		for (Map<String, Object> map6 : listAll) {
 			Map<String, Object> row = new HashMap<>();
 			row.put("SOURCEID", map6.get("SOURCEID"));
 			row.put("SRCNAME", map6.get("SRCNAME"));
 			row.put("srcImg", map6.get("srcImg"));
 			row.put((String) map6.get("time"), map6.get("count"));
 			//合计
 			int count=Integer.valueOf(map6.get("count").toString());
 			boolean flag = false;
 			for (Map<String, Object> newmap : newmaps) {
 				if (newmap.get("SOURCEID").equals(row.get("SOURCEID"))) {
 					newmap.put((String) map6.get("time"), map6.get("count"));
 					Integer total = (Integer)newmap.get("total");
 					newmap.put("total", total + count);
 					flag = true;
 					break;
 				}
 			}
 			if (!flag) {
 				row.put("total",count);
 				newmaps.add(row);
 			}
 		}
        //合计横向的一行
 		Map<String,Object> map1=new LinkedHashMap();
 		int Monthsum=0;
        for (Map<String, Object> map : newmaps) {
        		Monthsum +=Integer.valueOf(String.valueOf(map.get("total").toString()));
    		}
        	map1.put("srcName", "合计");
        	map1.put("total", Monthsum);
 		String[] days={"01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31"};
 		for (String day : days) {
 			int daysum=0;
 			for (Map<String, Object> map : newmaps) {
 				if(map.containsKey(day)){
 					daysum+=Integer.valueOf(String.valueOf(map.get(day).toString()));
 				}
 			}map1.put(day, daysum);
		}
 		newmaps.add(0,map1);
		return newmaps;
	}
	
	/**
     * 获取有效客资量--报表外
     * @param start
     * @param end
     * @param companyId
     * */
	public List<Map<String, Object>> getDSTGSrcMonthReportsvalid(int firstDay , int lastDay,ReportsParamSrcMonthVO reportsParamSrcMonthVO,
			DsInvalidVO invalidConfig) {
		//构造集合对象，把指标放入
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT info.SOURCEID,src.SRCNAME,FROM_UNIXTIME(info.CREATETIME, '%d') time,COUNT(1) count,src.SRCIMG srcImg ");
		sql.append(" FROM hm_crm_client_info info ");
		sql.append(" LEFT JOIN hm_crm_source src ON src.ID=info.SOURCEID ");
		sql.append(" LEFT JOIN hm_crm_client_detail deta ON info.KZID=deta.KZID ");
		sql.append(" WHERE info.ISDEL = 0 AND info.STATUSID NOT IN(0,98,99) ");
			//减去无效客资状态指标
        	if(StringUtil.isNotEmpty(invalidConfig.getDsInvalidStatus()) ){
        		sql.append(" AND info.STATUSID NOT IN("+invalidConfig.getDsInvalidStatus()+")");
        	}
        	//减去无效意向指标
        	if( StringUtil.isNotEmpty(invalidConfig.getDsInvalidLevel())){
        		sql.append(" AND deta.YXLEVEL NOT IN("+invalidConfig.getDsInvalidLevel()+")");
        	}
        	//待定不计算为有效时
        	if(!(invalidConfig.getDdIsValid())){
        		sql.append(" AND info.STATUSID NOT IN("+invalidConfig.getDsDdStatus()+")");
        	}
			if(StringUtil.isNotEmpty(reportsParamSrcMonthVO.getTypeId())){
				sql.append(" AND info.TYPEID IN("+reportsParamSrcMonthVO.getTypeId()+") ");
			}
		sql.append(" AND info.CREATETIME BETWEEN "+firstDay+" AND "+ lastDay);	
		sql.append(" AND src.TYPEID IN(1,2)");
			if (StringUtil.isNotEmpty(reportsParamSrcMonthVO.getSourceId())) {
				sql.append(" AND src.ID IN (" + reportsParamSrcMonthVO.getSourceId() + ")");
			}
		sql.append(" AND info.COMPANYID=? ");
		sql.append(" GROUP BY info.SOURCEID,time ");
		sql.append(" ORDER BY info.SOURCEID ");
        String sqlString = sql.toString(); 
        List<Map<String, Object>> listAll = jdbcTemplate.queryForList(sqlString, new Object[]{reportsParamSrcMonthVO.getCompanyId()});
        // 最终用返回
 		List<Map<String, Object>> newmaps = new ArrayList<>();

 		for (Map<String, Object> map6 : listAll) {
 			Map<String, Object> row = new HashMap<>();
 			row.put("SOURCEID", map6.get("SOURCEID"));
 			row.put("SRCNAME", map6.get("SRCNAME"));
 			row.put("srcImg", map6.get("srcImg"));
 			row.put((String) map6.get("time"), map6.get("count"));
 			//合计
 			int count=Integer.valueOf(map6.get("count").toString());
 			boolean flag = false;
 			for (Map<String, Object> newmap : newmaps) {
 				if (newmap.get("SOURCEID").equals(row.get("SOURCEID"))) {
 					newmap.put((String) map6.get("time"), map6.get("count"));
 					Integer total = (Integer)newmap.get("total");
 					newmap.put("total", total + count);
 					flag = true;
 					break;
 				}
 			}
 			if (!flag) {
 				row.put("total",count);
 				newmaps.add(row);
 			}
 		}
        //合计横向的一行
 		Map<String,Object> map1=new LinkedHashMap();
 		int Monthsum=0;
        for (Map<String, Object> map : newmaps) {
        		Monthsum +=Integer.valueOf(String.valueOf(map.get("total").toString()));
    		}
        	map1.put("srcName", "合计");
        	map1.put("total", Monthsum);
 		String[] days={"01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31"};
 		for (String day : days) {
 			int daysum=0;
 			for (Map<String, Object> map : newmaps) {
 				if(map.containsKey(day)){
 					daysum+=Integer.valueOf(String.valueOf(map.get(day).toString()));
 				}
 			}map1.put(day, daysum);
		}
 		newmaps.add(0,map1);
		return newmaps;
	}
	
	/**
	 * 总客资
	 */
	private void getAllClientCount(List<Map<String, Object>> dayList, String tableInfo, String month, List<ZjskzOfMonthReportsVO> zjskzOfMonthReportsVOS, String sourceId, Integer companyId, String typeId) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT ");

		for (Map<String, Object> day : dayList) {
			sql.append("(select count(info.id) from " + tableInfo + " info where info.SOURCEID IN ");
			if (StringUtil.isEmpty(sourceId)) {
				sql.append("(select id from hm_crm_source where companyId=" + companyId + " and typeid in ("+typeId+") )");
			} else {
				sql.append("(" + sourceId + ")");
			}
			sql.append(" and info.isdel = 0");
			sql.append(" and info.companyId="+companyId);
			sql.append(" and FROM_UNIXTIME(info.CREATETIME, '%Y/%m/%d')= '" + day.get("day") + "') " + day.get("dayKey") + ",");
		}
		sql.append("(select count(info.id) from " + tableInfo + " info where info.SOURCEID IN ");
		if (StringUtil.isEmpty(sourceId)) {
			sql.append("(select id from hm_crm_source where companyId=" + companyId + " and typeid in ("+typeId+") )");
		} else {
			sql.append("(" + sourceId + ")");
		}
		sql.append(" and info.isdel = 0");
		sql.append(" and info.companyId="+companyId);
		sql.append(" and FROM_UNIXTIME(info.CREATETIME, '%Y/%m')= '" + month + "') " + "hj ");
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql.toString(), new Object[]{});
		for (Map<String, Object> map : list) {
			for (String key : map.keySet()) {
				ZjskzOfMonthReportsVO zjskzOfMonthReportsVO = new ZjskzOfMonthReportsVO();
				zjskzOfMonthReportsVO.setDayId(key);
				zjskzOfMonthReportsVO.setAllClientCount(Integer.parseInt(Long.toString((Long) (map.get(key)))));
				zjskzOfMonthReportsVOS.add(zjskzOfMonthReportsVO);
			}
		}
	}

	/**
	 * 待定量
	 */
	private void getPendingClientCount(List<Map<String, Object>> dayList, String tableInfo, String month, List<ZjskzOfMonthReportsVO> zjskzOfMonthReportsVOS, String sourceId, Integer companyId, DsInvalidVO dsInvalidVO,String typeId) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT ");

		for (Map<String, Object> day : dayList) {
			sql.append("(select count(info.id) from " + tableInfo + " info where info.SOURCEID IN ");
			if (StringUtil.isEmpty(sourceId)) {
				sql.append("(select id from hm_crm_source where companyId=" + companyId + " and typeid in ("+typeId+") )");
			} else {
				sql.append("(" + sourceId + ")");
			}
			sql.append(" and info.isdel = 0");
			sql.append(" and info.companyId="+companyId);
			sql.append(" and INSTR( '" + dsInvalidVO.getDsDdStatus() + "', CONCAT(',',info.STATUSID + '',',')) != 0");
			sql.append(" and FROM_UNIXTIME(info.CREATETIME, '%Y/%m/%d')= '" + day.get("day") + "') " + day.get("dayKey") + ",");
		}
		sql.append("(select count(info.id) from " + tableInfo + " info where info.SOURCEID IN ");
		if (StringUtil.isEmpty(sourceId)) {
			sql.append("(select id from hm_crm_source where companyId=" + companyId + " and typeid in ("+typeId+") )");
		} else {
			sql.append("(" + sourceId + ")");
		}
		sql.append(" and info.isdel = 0");
		sql.append(" and info.companyId="+companyId);
		sql.append(" and INSTR( '" + dsInvalidVO.getDsDdStatus() + "', CONCAT(',',info.STATUSID + '',',')) != 0");
		sql.append(" and FROM_UNIXTIME(info.CREATETIME, '%Y/%m')= '" + month + "') " + "hj ");
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql.toString(), new Object[]{});
		List<ZjskzOfMonthReportsVO> zjskzOfMonthReportsBak = new LinkedList<>();
		for (Map<String, Object> map : list) {
			for (String key : map.keySet()) {
				ZjskzOfMonthReportsVO zjskzOfMonthReportsVO = new ZjskzOfMonthReportsVO();
				zjskzOfMonthReportsVO.setDayId(key);
				zjskzOfMonthReportsVO.setPendingClientCount(Integer.parseInt(Long.toString((Long) (map.get(key)))));
				zjskzOfMonthReportsBak.add(zjskzOfMonthReportsVO);
			}
		}
		for (ZjskzOfMonthReportsVO zjskzOfMonthReportsVO : zjskzOfMonthReportsVOS) {
			for (ZjskzOfMonthReportsVO zjskzOfMonthReportsVO1 : zjskzOfMonthReportsBak) {
				if (zjskzOfMonthReportsVO.getDayId().equalsIgnoreCase(zjskzOfMonthReportsVO1.getDayId())) {
					zjskzOfMonthReportsVO.setPendingClientCount(zjskzOfMonthReportsVO1.getPendingClientCount());
					break;
				}
			}
		}
	}

	/**
	 * 筛选待定
	 */
	private void getFilterWaitClientCount(List<Map<String, Object>> dayList, String tableInfo, String month, List<ZjskzOfMonthReportsVO> zjskzOfMonthReportsVOS, String sourceId, Integer companyId,String typeId) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT ");

		for (Map<String, Object> day : dayList) {
			sql.append("(select count(info.id) from " + tableInfo + " info where info.SOURCEID IN ");
			if (StringUtil.isEmpty(sourceId)) {
				sql.append("(select id from hm_crm_source where companyId=" + companyId + " and typeid in ("+typeId+") )");
			} else {
				sql.append("(" + sourceId + ")");
			}
			sql.append(" and info.isdel = 0");
			sql.append(" and info.companyId="+companyId);
			sql.append("    and info.STATUSID = 98 ");
			sql.append(" and FROM_UNIXTIME(info.CREATETIME, '%Y/%m/%d')= '" + day.get("day") + "') " + day.get("dayKey") + ",");
		}
		sql.append("(select count(info.id) from " + tableInfo + " info where info.SOURCEID IN ");
		if (StringUtil.isEmpty(sourceId)) {
			sql.append("(select id from hm_crm_source where companyId=" + companyId + " and typeid in ("+typeId+") )");
		} else {
			sql.append("(" + sourceId + ")");
		}
		sql.append(" and info.isdel = 0");
		sql.append(" and info.companyId="+companyId);
		sql.append("    and info.STATUSID = 98 ");
		sql.append(" and FROM_UNIXTIME(info.CREATETIME, '%Y/%m')= '" + month + "') " + "hj ");
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql.toString(), new Object[]{});
		List<ZjskzOfMonthReportsVO> zjskzOfMonthReportsBak = new LinkedList<>();
		for (Map<String, Object> map : list) {
			for (String key : map.keySet()) {
				ZjskzOfMonthReportsVO zjskzOfMonthReportsVO = new ZjskzOfMonthReportsVO();
				zjskzOfMonthReportsVO.setDayId(key);
				zjskzOfMonthReportsVO.setFilterPendingClientCount(Integer.parseInt(Long.toString((Long) (map.get(key)))));
				zjskzOfMonthReportsBak.add(zjskzOfMonthReportsVO);
			}
		}
		for (ZjskzOfMonthReportsVO zjskzOfMonthReportsVO : zjskzOfMonthReportsVOS) {
			for (ZjskzOfMonthReportsVO zjskzOfMonthReportsVO1 : zjskzOfMonthReportsBak) {
				if (zjskzOfMonthReportsVO.getDayId().equalsIgnoreCase(zjskzOfMonthReportsVO1.getDayId())) {
					zjskzOfMonthReportsVO.setFilterPendingClientCount(zjskzOfMonthReportsVO1.getFilterPendingClientCount());
					break;
				}
			}
		}
	}

	/**
	 * 筛选无效
	 */
	private void getFilterInValidClientCount(List<Map<String, Object>> dayList, String tableInfo, String month, List<ZjskzOfMonthReportsVO> zjskzOfMonthReportsVOS, String sourceId, Integer companyId,String typeId) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT ");

		for (Map<String, Object> day : dayList) {
			sql.append("(select count(info.id) from " + tableInfo + " info where info.SOURCEID IN ");
			if (StringUtil.isEmpty(sourceId)) {
				sql.append("(select id from hm_crm_source where companyId=" + companyId + " and typeid in ("+typeId+") )");
			} else {
				sql.append("(" + sourceId + ")");
			}
			sql.append(" and info.isdel = 0");
			sql.append(" and info.companyId="+companyId);
			sql.append("   and info.STATUSID = 99 ");
			sql.append(" and FROM_UNIXTIME(info.CREATETIME, '%Y/%m/%d')= '" + day.get("day") + "') " + day.get("dayKey") + ",");
		}
		sql.append("(select count(info.id) from " + tableInfo + " info where info.SOURCEID IN ");
		if (StringUtil.isEmpty(sourceId)) {
			sql.append("(select id from hm_crm_source where companyId=" + companyId + " and typeid in ("+typeId+") )");
		} else {
			sql.append("(" + sourceId + ")");
		}
		sql.append(" and info.isdel = 0");
		sql.append(" and info.companyId="+companyId);
		sql.append("   and info.STATUSID = 99 ");
		sql.append(" and FROM_UNIXTIME(info.CREATETIME, '%Y/%m')= '" + month + "') " + "hj ");
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql.toString(), new Object[]{});
		List<ZjskzOfMonthReportsVO> zjskzOfMonthReportsBak = new LinkedList<>();
		for (Map<String, Object> map : list) {
			for (String key : map.keySet()) {
				ZjskzOfMonthReportsVO zjskzOfMonthReportsVO = new ZjskzOfMonthReportsVO();
				zjskzOfMonthReportsVO.setDayId(key);
				zjskzOfMonthReportsVO.setFilterInValidClientCount(Integer.parseInt(Long.toString((Long) (map.get(key)))));
				zjskzOfMonthReportsBak.add(zjskzOfMonthReportsVO);
			}
		}
		for (ZjskzOfMonthReportsVO zjskzOfMonthReportsVO : zjskzOfMonthReportsVOS) {
			for (ZjskzOfMonthReportsVO zjskzOfMonthReportsVO1 : zjskzOfMonthReportsBak) {
				if (zjskzOfMonthReportsVO.getDayId().equalsIgnoreCase(zjskzOfMonthReportsVO1.getDayId())) {
					zjskzOfMonthReportsVO.setFilterInValidClientCount(zjskzOfMonthReportsVO1.getFilterInValidClientCount());
					break;
				}
			}
		}
	}

	/**
	 * 筛选中
	 */
	private void getFilterInClientCount(List<Map<String, Object>> dayList, String tableInfo, String month, List<ZjskzOfMonthReportsVO> zjskzOfMonthReportsVOS, String sourceId, Integer companyId,String typeId) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT ");

		for (Map<String, Object> day : dayList) {
			sql.append("(select count(info.id) from " + tableInfo + " info where info.SOURCEID IN ");
			if (StringUtil.isEmpty(sourceId)) {
				sql.append("(select id from hm_crm_source where companyId=" + companyId + " and typeid in ("+typeId+") )");
			} else {
				sql.append("(" + sourceId + ")");
			}
			sql.append(" and info.isdel = 0");
			sql.append(" and info.companyId="+companyId);
			sql.append("   and info.STATUSID = 0 ");
			sql.append(" and FROM_UNIXTIME(info.CREATETIME, '%Y/%m/%d')= '" + day.get("day") + "') " + day.get("dayKey") + ",");
		}
		sql.append("(select count(info.id) from " + tableInfo + " info where info.SOURCEID IN ");
		if (StringUtil.isEmpty(sourceId)) {
			sql.append("(select id from hm_crm_source where companyId=" + companyId + " and typeid in ("+typeId+") )");
		} else {
			sql.append("(" + sourceId + ")");
		}
		sql.append(" and info.isdel = 0");
		sql.append(" and info.companyId="+companyId);
		sql.append(" and  info.CLASSID = 1 and info.STATUSID = 0 ");
		sql.append(" and FROM_UNIXTIME(info.CREATETIME, '%Y/%m')= '" + month + "') " + "hj ");
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql.toString(), new Object[]{});
		List<ZjskzOfMonthReportsVO> zjskzOfMonthReportsBak = new LinkedList<>();
		for (Map<String, Object> map : list) {
			for (String key : map.keySet()) {
				ZjskzOfMonthReportsVO zjskzOfMonthReportsVO = new ZjskzOfMonthReportsVO();
				zjskzOfMonthReportsVO.setDayId(key);
				zjskzOfMonthReportsVO.setFilterInClientCount(Integer.parseInt(Long.toString((Long) (map.get(key)))));
				zjskzOfMonthReportsBak.add(zjskzOfMonthReportsVO);
			}
		}
		for (ZjskzOfMonthReportsVO zjskzOfMonthReportsVO : zjskzOfMonthReportsVOS) {
			for (ZjskzOfMonthReportsVO zjskzOfMonthReportsVO1 : zjskzOfMonthReportsBak) {
				if (zjskzOfMonthReportsVO.getDayId().equalsIgnoreCase(zjskzOfMonthReportsVO1.getDayId())) {
					zjskzOfMonthReportsVO.setFilterInClientCount(zjskzOfMonthReportsVO1.getFilterInClientCount());
					break;
				}
			}
		}
	}

	/**
	 * 无效量
	 */
	private void getValidClientCount(List<Map<String, Object>> dayList, String tableInfo, String month, List<ZjskzOfMonthReportsVO> zjskzOfMonthReportsVOS, String sourceId, Integer companyId, DsInvalidVO dsInvalidVO,String typeId) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT ");
		String tableDetail = DBSplitUtil.getTable(TableEnum.detail, companyId);
		for (Map<String, Object> day : dayList) {
			sql.append("(select count(info.id) from " + tableInfo + " info left join " + tableDetail + " detail on detail.kzid=info.kzid  where info.SOURCEID IN ");
			if (StringUtil.isEmpty(sourceId)) {
				sql.append("(select id from hm_crm_source where companyId=" + companyId + " and typeid in ("+typeId+") )");
			} else {
				sql.append("(" + sourceId + ")");
			}
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
			sql.append(" and info.isdel = 0");
			sql.append(" and info.companyId="+companyId);
			sql.append(" and FROM_UNIXTIME(info.CREATETIME, '%Y/%m/%d')= '" + day.get("day") + "') " + day.get("dayKey") + ",");
		}
		sql.append("(select count(info.id) from " + tableInfo + " info left join " + tableDetail + " detail on detail.kzid=info.kzid   where info.SOURCEID IN ");
		if (StringUtil.isEmpty(sourceId)) {
			sql.append("(select id from hm_crm_source where companyId=" + companyId + " and typeid in ("+typeId+") )");
		} else {
			sql.append("(" + sourceId + ")");
		}
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
		sql.append(" and info.isdel = 0");
		sql.append(" and info.companyId="+companyId);
		sql.append(" and FROM_UNIXTIME(info.CREATETIME, '%Y/%m')= '" + month + "') " + "hj ");
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql.toString(), new Object[]{});
		List<ZjskzOfMonthReportsVO> zjskzOfMonthReportsBak = new LinkedList<>();
		for (Map<String, Object> map : list) {
			for (String key : map.keySet()) {
				ZjskzOfMonthReportsVO zjskzOfMonthReportsVO = new ZjskzOfMonthReportsVO();
				zjskzOfMonthReportsVO.setDayId(key);
				zjskzOfMonthReportsVO.setInValidClientCount(Integer.parseInt(Long.toString((Long) (map.get(key)))));
				zjskzOfMonthReportsBak.add(zjskzOfMonthReportsVO);
			}
		}
		for (ZjskzOfMonthReportsVO zjskzOfMonthReportsVO : zjskzOfMonthReportsVOS) {
			for (ZjskzOfMonthReportsVO zjskzOfMonthReportsVO1 : zjskzOfMonthReportsBak) {
				if (zjskzOfMonthReportsVO.getDayId().equalsIgnoreCase(zjskzOfMonthReportsVO1.getDayId())) {
					zjskzOfMonthReportsVO.setInValidClientCount(zjskzOfMonthReportsVO1.getInValidClientCount());
					break;
				}
			}
		}
	}

	/**
	 * 入店量
	 */
	private void getComeShopClient(List<Map<String, Object>> dayList, String tableInfo, String month, List<ZjskzOfMonthReportsVO> zjskzOfMonthReportsVOS, String sourceId, Integer companyId,String typeId) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT ");

		for (Map<String, Object> day : dayList) {
			sql.append("(select count(info.id) from " + tableInfo + " info where info.SOURCEID IN ");
			if (StringUtil.isEmpty(sourceId)) {
				sql.append("(select id from hm_crm_source where companyId=" + companyId + " and typeid in ("+typeId+") )");
			} else {
				sql.append("(" + sourceId + ")");
			}
			sql.append(" and info.isdel = 0");
			sql.append(" and info.companyId="+companyId);
			sql.append(" and FROM_UNIXTIME(info.ComeShopTime, '%Y/%m/%d')= '" + day.get("day") + "') " + day.get("dayKey") + ",");
		}
		sql.append("(select count(info.id) from " + tableInfo + " info where info.SOURCEID IN ");
		if (StringUtil.isEmpty(sourceId)) {
			sql.append("(select id from hm_crm_source where companyId=" + companyId + " and typeid in ("+typeId+") )");
		} else {
			sql.append("(" + sourceId + ")");
		}
		sql.append(" and info.isdel = 0");
		sql.append(" and info.companyId="+companyId);
		sql.append(" and FROM_UNIXTIME(info.ComeShopTime, '%Y/%m')= '" + month + "') " + "hj ");
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql.toString(), new Object[]{});
		List<ZjskzOfMonthReportsVO> zjskzOfMonthReportsBak = new LinkedList<>();
		for (Map<String, Object> map : list) {
			for (String key : map.keySet()) {
				ZjskzOfMonthReportsVO zjskzOfMonthReportsVO = new ZjskzOfMonthReportsVO();
				zjskzOfMonthReportsVO.setDayId(key);
				zjskzOfMonthReportsVO.setComeShopClientCount(Integer.parseInt(Long.toString((Long) (map.get(key)))));
				zjskzOfMonthReportsBak.add(zjskzOfMonthReportsVO);
			}
		}
		for (ZjskzOfMonthReportsVO zjskzOfMonthReportsVO : zjskzOfMonthReportsVOS) {
			for (ZjskzOfMonthReportsVO zjskzOfMonthReportsVO1 : zjskzOfMonthReportsBak) {
				if (zjskzOfMonthReportsVO.getDayId().equalsIgnoreCase(zjskzOfMonthReportsVO1.getDayId())) {
					zjskzOfMonthReportsVO.setComeShopClientCount(zjskzOfMonthReportsVO1.getComeShopClientCount());
					break;
				}
			}
		}
	}

	/**
	 * 成交量
	 */
	private void getSuccessClient(List<Map<String, Object>> dayList, String tableInfo, String month, List<ZjskzOfMonthReportsVO> zjskzOfMonthReportsVOS, String sourceId, Integer companyId,String typeId) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT ");

		for (Map<String, Object> day : dayList) {
			sql.append("(select count(info.id) from " + tableInfo + " info where info.SOURCEID IN ");
			if (StringUtil.isEmpty(sourceId)) {
				sql.append("(select id from hm_crm_source where companyId=" + companyId + " and typeid in ("+typeId+") )");
			} else {
				sql.append("(" + sourceId + ")");
			}
			sql.append(" and info.isdel = 0");
			sql.append(" and info.companyId="+companyId);
			sql.append(" and FROM_UNIXTIME(info.SuccessTime, '%Y/%m/%d')= '" + day.get("day") + "') " + day.get("dayKey") + ",");
		}
		sql.append("(select count(info.id) from " + tableInfo + " info where info.SOURCEID IN ");
		if (StringUtil.isEmpty(sourceId)) {
			sql.append("(select id from hm_crm_source where companyId=" + companyId + " and typeid in ("+typeId+") )");
		} else {
			sql.append("(" + sourceId + ")");
		}
		sql.append(" and info.isdel = 0");
		sql.append(" and info.companyId="+companyId);
		sql.append(" and FROM_UNIXTIME(info.SuccessTime, '%Y/%m')= '" + month + "') " + "hj ");
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql.toString(), new Object[]{});
		List<ZjskzOfMonthReportsVO> zjskzOfMonthReportsBak = new LinkedList<>();
		for (Map<String, Object> map : list) {
			for (String key : map.keySet()) {
				ZjskzOfMonthReportsVO zjskzOfMonthReportsVO = new ZjskzOfMonthReportsVO();
				zjskzOfMonthReportsVO.setDayId(key);
				zjskzOfMonthReportsVO.setSuccessClientCount(Integer.parseInt(Long.toString((Long) (map.get(key)))));
				zjskzOfMonthReportsBak.add(zjskzOfMonthReportsVO);
			}
		}
		for (ZjskzOfMonthReportsVO zjskzOfMonthReportsVO : zjskzOfMonthReportsVOS) {
			for (ZjskzOfMonthReportsVO zjskzOfMonthReportsVO1 : zjskzOfMonthReportsBak) {
				if (zjskzOfMonthReportsVO.getDayId().equalsIgnoreCase(zjskzOfMonthReportsVO1.getDayId())) {
					zjskzOfMonthReportsVO.setSuccessClientCount(zjskzOfMonthReportsVO1.getSuccessClientCount());
					break;
				}
			}
		}
	}


	/**
	 * 计算Rate
	 */
	private void computerRate(List<ZjskzOfMonthReportsVO> zjskzOfMonthReportsVOS, DsInvalidVO invalidConfig) {
		for (ZjskzOfMonthReportsVO dstgGoldDataReportsVO : zjskzOfMonthReportsVOS) {
			//有效量
			if (invalidConfig.getDdIsValid()) {
				dstgGoldDataReportsVO.setValidClientCount(dstgGoldDataReportsVO.getAllClientCount() - dstgGoldDataReportsVO.getInValidClientCount() - dstgGoldDataReportsVO.getFilterInClientCount() - dstgGoldDataReportsVO.getFilterInValidClientCount() - dstgGoldDataReportsVO.getFilterPendingClientCount());
			} else {
				dstgGoldDataReportsVO.setValidClientCount(dstgGoldDataReportsVO.getAllClientCount() - dstgGoldDataReportsVO.getPendingClientCount() - dstgGoldDataReportsVO.getInValidClientCount() - dstgGoldDataReportsVO.getFilterInClientCount() - dstgGoldDataReportsVO.getFilterInValidClientCount() - dstgGoldDataReportsVO.getFilterPendingClientCount());
			}
			//客资量(总客资-筛选待定-筛选中-筛选无效)
			dstgGoldDataReportsVO.setClientCount(dstgGoldDataReportsVO.getAllClientCount() - dstgGoldDataReportsVO.getFilterPendingClientCount() - dstgGoldDataReportsVO.getFilterInValidClientCount() - dstgGoldDataReportsVO.getFilterInClientCount());
			//有效率
			double validRate = (double) dstgGoldDataReportsVO.getValidClientCount() / dstgGoldDataReportsVO.getClientCount();
			dstgGoldDataReportsVO.setValidRate(parseDouble(((Double.isNaN(validRate) || Double.isInfinite(validRate)) ? 0.0 : validRate) * 100));

			//毛客资入店率
			double clientComeShopRate = (double) dstgGoldDataReportsVO.getComeShopClientCount() / dstgGoldDataReportsVO.getClientCount();
			dstgGoldDataReportsVO.setClientComeShopRate(parseDouble(((Double.isNaN(clientComeShopRate) || Double.isInfinite(clientComeShopRate)) ? 0.0 : clientComeShopRate) * 100));
			//有效客资入店率
			double validComeShopRate = (double) dstgGoldDataReportsVO.getComeShopClientCount() / dstgGoldDataReportsVO.getValidClientCount();
			dstgGoldDataReportsVO.setValidClientComeShopRate(parseDouble(((Double.isNaN(validComeShopRate) || Double.isInfinite(validComeShopRate)) ? 0.0 : validComeShopRate) * 100));

			//入店成交率
			double comeShopSuccessRate = (double) dstgGoldDataReportsVO.getSuccessClientCount() / dstgGoldDataReportsVO.getComeShopClientCount();
			dstgGoldDataReportsVO.setComeShopSuccessRate(parseDouble(((Double.isNaN(comeShopSuccessRate) || Double.isInfinite(comeShopSuccessRate)) ? 0.0 : comeShopSuccessRate) * 100));

		}
	}
	/**
	 * 只保留2位小数
	 */
	public double parseDouble(double result) {
		return Double.parseDouble(String.format("%.2f", result));
	}
	
	/**
	 * 电商推广月度报表入店量--HJF
	 */

	public List<Map<String, Object>> getDSTGSrcMonthReportsCome(int firstDay, int lastDay,
			ReportsParamSrcMonthVO reportsParamSrcMonthVO, DsInvalidVO invalidConfig) {
		StringBuilder sql = new StringBuilder();
		//String infoTabName = DBSplitUtil.getInfoTabName(reportsParamSrcMonthVO.getCompanyId());
		sql.append("SELECT info.SOURCEID,src.SRCNAME,FROM_UNIXTIME(info.CREATETIME, '%d') time,COUNT(1) count,src.SRCIMG srcImg ");
		sql.append(" FROM hm_crm_client_info info ");
		sql.append(" LEFT JOIN hm_crm_source src ON src.ID=info.SOURCEID ");
		sql.append(" WHERE info.ISDEL = 0");
			if(StringUtil.isNotEmpty(reportsParamSrcMonthVO.getTypeId())){
				sql.append(" AND info.TYPEID IN("+reportsParamSrcMonthVO.getTypeId()+") ");
			}
		sql.append(" AND info.COMESHOPTIME BETWEEN "+firstDay+" AND "+ lastDay);	
		sql.append(" AND src.TYPEID IN(1,2)");
			if (StringUtil.isNotEmpty(reportsParamSrcMonthVO.getSourceId())) {
				sql.append(" AND src.ID IN (" + reportsParamSrcMonthVO.getSourceId() + ")");
			}
		sql.append(" AND info.COMPANYID=? ");
		sql.append(" GROUP BY info.SOURCEID,time ");
		sql.append(" ORDER BY info.SOURCEID ");
        String sqlString = sql.toString(); 
        List<Map<String, Object>> listSum = jdbcTemplate.queryForList(sqlString, new Object[]{reportsParamSrcMonthVO.getCompanyId()});
        // 最终用返回
 		List<Map<String, Object>> newmaps = new ArrayList<>();

 		for (Map<String, Object> map6 : listSum) {
 			Map<String, Object> row = new HashMap<>();
 			row.put("SOURCEID", map6.get("SOURCEID"));
 			row.put("SRCNAME", map6.get("SRCNAME"));
 			row.put("srcImg", map6.get("srcImg"));
 			row.put((String) map6.get("time"), map6.get("count"));
 			//合计
 			int count=Integer.valueOf(map6.get("count").toString());
 			boolean flag = false;
 			for (Map<String, Object> newmap : newmaps) {
 				if (newmap.get("SOURCEID").equals(row.get("SOURCEID"))) {
 					newmap.put((String) map6.get("time"), map6.get("count"));
 					Integer total = (Integer)newmap.get("total");
 					newmap.put("total", total + count);
 					flag = true;
 					break;
 				}
 			}
 			if (!flag) {
 				row.put("total",count);
 				newmaps.add(row);
 			}
 		}	
        //合计横向的一行
 		Map<String,Object> map1=new LinkedHashMap();
 		int Monthsum=0;
        for (Map<String, Object> map : newmaps) {
        		Monthsum +=Integer.valueOf(String.valueOf(map.get("total").toString()));
    		}
        	map1.put("srcName", "合计");
        	map1.put("total", Monthsum);
 		String[] days={"01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31"};
 		for (String day : days) {
 			int daysum=0;
 			for (Map<String, Object> map : newmaps) {
 				if(map.containsKey(day)){
 					daysum+=Integer.valueOf(String.valueOf(map.get(day).toString()));
 				}
 			}map1.put(day, daysum);
		}
 		newmaps.add(0,map1);
		return newmaps;
	}
	
	/**
	 * 电商推广月度报表成交量--HJF
	 */
	public List<Map<String, Object>> getDSTGSrcMonthReportsSuccess(int firstDay, int lastDay,
			ReportsParamSrcMonthVO reportsParamSrcMonthVO, DsInvalidVO invalidConfig) {
		StringBuilder sql = new StringBuilder();
		//String infoTabName = DBSplitUtil.getInfoTabName(reportsParamSrcMonthVO.getCompanyId());
		sql.append("SELECT info.SOURCEID,src.SRCNAME,FROM_UNIXTIME(info.CREATETIME, '%d') time,COUNT(1) count,src.SRCIMG srcImg ");
		sql.append(" FROM hm_crm_client_info info ");
		sql.append(" LEFT JOIN hm_crm_source src ON src.ID=info.SOURCEID ");
		sql.append(" WHERE info.ISDEL = 0");
			if(StringUtil.isNotEmpty(reportsParamSrcMonthVO.getTypeId())){
				sql.append(" AND info.TYPEID IN("+reportsParamSrcMonthVO.getTypeId()+") ");
			}
		sql.append(" AND info.SUCCESSTIME BETWEEN "+firstDay+" AND "+ lastDay);	
		sql.append(" AND src.TYPEID IN(1,2)");
			if (StringUtil.isNotEmpty(reportsParamSrcMonthVO.getSourceId())) {
				sql.append(" AND src.ID IN (" + reportsParamSrcMonthVO.getSourceId() + ")");
			}
		sql.append(" AND info.COMPANYID=? ");
		sql.append(" GROUP BY info.SOURCEID,time ");
		sql.append(" ORDER BY info.SOURCEID ");
        String sqlString = sql.toString(); 
        List<Map<String, Object>> listSum = jdbcTemplate.queryForList(sqlString, new Object[]{reportsParamSrcMonthVO.getCompanyId()});
        // 最终用返回
 		List<Map<String, Object>> newmaps = new ArrayList<>();

 		for (Map<String, Object> map6 : listSum) {
 			Map<String, Object> row = new HashMap<>();
 			row.put("SOURCEID", map6.get("SOURCEID"));
 			row.put("SRCNAME", map6.get("SRCNAME"));
 			row.put("srcImg", map6.get("srcImg"));
 			row.put((String) map6.get("time"), map6.get("count"));
 			//合计
 			int count=Integer.valueOf(map6.get("count").toString());
 			boolean flag = false;
 			for (Map<String, Object> newmap : newmaps) {
 				if (newmap.get("SOURCEID").equals(row.get("SOURCEID"))) {
 					newmap.put((String) map6.get("time"), map6.get("count"));
 					Integer total = (Integer)newmap.get("total");
 					newmap.put("total", total + count);
 					flag = true;
 					break;
 				}
 			}
 			if (!flag) {
 				row.put("total",count);
 				newmaps.add(row);
 			}
 		}	
        //合计横向的一行
 		Map<String,Object> map1=new LinkedHashMap();
 		int Monthsum=0;
        for (Map<String, Object> map : newmaps) {
        		Monthsum +=Integer.valueOf(String.valueOf(map.get("total").toString()));
    		}
        	map1.put("srcName", "合计");
        	map1.put("total", Monthsum);
 		String[] days={"01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31"};
 		for (String day : days) {
 			int daysum=0;
 			for (Map<String, Object> map : newmaps) {
 				if(map.containsKey(day)){
 					daysum+=Integer.valueOf(String.valueOf(map.get(day).toString()));
 				}
 			}map1.put(day, daysum);
		}
 		newmaps.add(0,map1);
		return newmaps;
	}
	
	
}
