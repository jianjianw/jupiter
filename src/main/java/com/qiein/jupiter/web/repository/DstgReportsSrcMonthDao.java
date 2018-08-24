package com.qiein.jupiter.web.repository;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
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
	 * 获取报表详情数据
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
     * 获取总客资量
     * @param start
     * @param end
     * @param companyId
     * */
	public List<Map<String, Object>> getDSTGSrcMonthReportsSum(List<Map<String, Object>> dayList , String month,ReportsParamSrcMonthVO reportsParamSrcMonthVO,
			DsInvalidVO dsInvalidVO) {
		StringBuilder sql = new StringBuilder();
		String infoTabName = DBSplitUtil.getInfoTabName(reportsParamSrcMonthVO.getCompanyId());
		
        sql.append("SELECT src.SRCNAME srcName,src.ID srcId,");
        if(StringUtil.isEmpty(reportsParamSrcMonthVO.getTypeId()) ){
        	sql.append("(select COUNT(info.ID) from " + infoTabName + " info where info.SOURCEID=src.ID AND info.ISDEL=0  AND FROM_UNIXTIME(info.CREATETIME, '%Y/%m')='" + month + "') hj,  ");
        	for (Map<String, Object> day : dayList) {
                sql.append("(select count(info.ID) from " + infoTabName + " info where info.SOURCEID=src.ID AND info.ISDEL=0  AND FROM_UNIXTIME(info.CREATETIME, '%Y/%m/%d')='" + day.get("day") + "') " + day.get("dayKey") + ",");
            }
        }else{
        	sql.append("(select COUNT(info.ID) from " + infoTabName + " info where info.SOURCEID=src.ID AND info.ISDEL=0 "+" AND info.TYPEID IN("+reportsParamSrcMonthVO.getTypeId()+")  " +" AND FROM_UNIXTIME(info.CREATETIME, '%Y/%m')='" + month + "') hj,  ");
        	for (Map<String, Object> day : dayList) {
                sql.append("(select count(info.ID) from " + infoTabName + " info where info.SOURCEID=src.ID AND info.ISDEL=0 "+" AND info.TYPEID IN("+reportsParamSrcMonthVO.getTypeId()+")  " +" AND FROM_UNIXTIME(info.CREATETIME, '%Y/%m/%d')='" + day.get("day") + "') " + day.get("dayKey") + ",");
            }
        }
        sql.append(" src.SRCIMG srcImg ");
        sql.append(" FROM hm_crm_source src WHERE src.COMPANYID = ? AND TYPEID IN (1,2) ");
        
        if (StringUtil.isNotEmpty(reportsParamSrcMonthVO.getSourceId())) {
            sql.append(" AND src.ID IN (" + reportsParamSrcMonthVO.getSourceId() + ")");
        }
        sql.append(" ORDER BY src.CHANNELID ASC,src.PRIORITY ASC");
        String sqlString = sql.toString(); 
        List<Map<String, Object>> listSum = jdbcTemplate.queryForList(sqlString, new Object[]{reportsParamSrcMonthVO.getCompanyId()});
		
        //合计横向的一行
        Map<String, Object> map1=new HashMap<>();
        for (Map<String, Object> day : dayList) {
        	long daysum=0;
        	for (Map<String, Object> map : listSum) {
    			daysum +=(long) map.get(day.get("dayKey"));
    		}
        	map1.put((String) day.get("dayKey"), daysum);
		}
        long Monthsum=0;
        for (Map<String, Object> map : listSum) {
        		Monthsum +=(long) map.get("hj");
    		}
        	map1.put("hj", Monthsum);
        listSum.add(map1);
		return listSum;
	}
	
	/**
     * 获取客资量
     * @param start
     * @param end
     * @param companyId
     * */
	public List<Map<String, Object>> getDSTGSrcMonthReportsAll(List<Map<String, Object>> dayList , String month,ReportsParamSrcMonthVO reportsParamSrcMonthVO,
			DsInvalidVO invalidConfig) {
		StringBuilder sql = new StringBuilder();
		String infoTabName = DBSplitUtil.getInfoTabName(reportsParamSrcMonthVO.getCompanyId());
		
        sql.append("SELECT src.SRCNAME srcName,src.ID srcId,");
        if(StringUtil.isEmpty(reportsParamSrcMonthVO.getTypeId()) ){
        	sql.append("(select COUNT(info.ID) from " + infoTabName + " info where info.SOURCEID=src.ID AND info.ISDEL=0 AND info.STATUSID NOT IN(0,98,99) AND FROM_UNIXTIME(info.CREATETIME, '%Y/%m')='" + month + "') hj,  ");
        	for (Map<String, Object> day : dayList) {
                sql.append("(select count(info.ID) from " + infoTabName + " info where info.SOURCEID=src.ID AND info.ISDEL=0 AND info.STATUSID NOT IN(0,98,99)  AND FROM_UNIXTIME(info.CREATETIME, '%Y/%m/%d')='" + day.get("day") + "') " + day.get("dayKey") + ",");
            }
        	
        }else{
        	sql.append("(select COUNT(info.ID) from " + infoTabName + " info where info.SOURCEID=src.ID AND info.ISDEL=0 AND info.STATUSID NOT IN(0,98,99) "+" AND info.TYPEID IN("+reportsParamSrcMonthVO.getTypeId()+")  " +" AND FROM_UNIXTIME(info.CREATETIME, '%Y/%m')='" + month + "') hj,  ");
        	for (Map<String, Object> day : dayList) {
                sql.append("(select count(info.ID) from " + infoTabName + " info where info.SOURCEID=src.ID AND info.ISDEL=0 AND info.STATUSID NOT IN(0,98,99) "+" AND info.TYPEID IN("+reportsParamSrcMonthVO.getTypeId()+")  " +" AND FROM_UNIXTIME(info.CREATETIME, '%Y/%m/%d')='" + day.get("day") + "') " + day.get("dayKey") + ",");
            }
        }
        sql.append(" src.SRCIMG srcImg ");
        sql.append(" FROM hm_crm_source src WHERE src.COMPANYID = ? AND TYPEID IN (1,2) ");
        
        if (StringUtil.isNotEmpty(reportsParamSrcMonthVO.getSourceId())) {
            sql.append(" AND src.ID IN (" + reportsParamSrcMonthVO.getSourceId() + ")");
        }
        sql.append(" ORDER BY src.CHANNELID ASC,src.PRIORITY ASC");
        String sqlString = sql.toString(); 
        List<Map<String, Object>> listAll = jdbcTemplate.queryForList(sqlString, new Object[]{reportsParamSrcMonthVO.getCompanyId()});
        
      //合计横向的一行
        Map<String, Object> map1=new HashMap<>();
        for (Map<String, Object> day : dayList) {
        	long daysum=0;
        	for (Map<String, Object> map : listAll) {
    			daysum +=(long) map.get(day.get("dayKey"));
    		}
        	map1.put((String) day.get("dayKey"), daysum);
		}
        long Monthsum=0;
        for (Map<String, Object> map : listAll) {
        		Monthsum +=(long) map.get("hj");
    		}
        	map1.put("hj", Monthsum);
        	listAll.add(map1);
		return listAll;
	}
	
	/**
     * 获取待定客资量
     * @param start
     * @param end
     * @param companyId
     * */
	public List<Map<String, Object>> getDSTGSrcMonthReportsDdNum(List<Map<String, Object>> dayList , String month,ReportsParamSrcMonthVO reportsParamSrcMonthVO,
			DsInvalidVO invalidConfig) {
		StringBuilder sql = new StringBuilder();
		String infoTabName = DBSplitUtil.getInfoTabName(reportsParamSrcMonthVO.getCompanyId());
		
        sql.append("SELECT src.SRCNAME srcName,src.ID srcId,");
        sql.append("(select COUNT(info.ID) from " + infoTabName + " info where info.SOURCEID=src.ID AND info.ISDEL=0  ");
        	
        if(StringUtil.isNotEmpty(invalidConfig.getDsDdStatus())){
    		sql.append(" AND INSTR('"+invalidConfig.getDsDdStatus()+"' , CONCAT(',',info.STATUSID,',')) != 0");
    	}
    	if(StringUtil.isNotEmpty(reportsParamSrcMonthVO.getTypeId())){
    		sql.append(" AND info.TYPEID IN("+reportsParamSrcMonthVO.getTypeId()+")");
    	}
    	sql.append(" AND FROM_UNIXTIME(info.CREATETIME, '%Y/%m')='" + month + "') hj,  ");
    	//计算每天客资数
    	for (Map<String, Object> day : dayList) {
            sql.append("(select count(info.ID) from " + infoTabName + " info where info.SOURCEID=src.ID AND info.ISDEL=0 ");
            if(StringUtil.isNotEmpty(invalidConfig.getDsDdStatus())){
        		sql.append(" AND INSTR('"+invalidConfig.getDsDdStatus()+"' , CONCAT(',',info.STATUSID,',')) != 0");
        	}
        	if(StringUtil.isNotEmpty(reportsParamSrcMonthVO.getTypeId())){
        		sql.append(" AND info.TYPEID IN("+reportsParamSrcMonthVO.getTypeId()+")");
        	}
            sql.append(" AND FROM_UNIXTIME(info.CREATETIME, '%Y/%m/%d')='" + day.get("day") + "') " + day.get("dayKey") + ",");
        }	
        	
        sql.append(" src.SRCIMG srcImg ");
        sql.append(" FROM hm_crm_source src WHERE src.COMPANYID = ? AND TYPEID IN (1,2) ");
        
        if (StringUtil.isNotEmpty(reportsParamSrcMonthVO.getSourceId())) {
            sql.append(" AND src.ID IN (" + reportsParamSrcMonthVO.getSourceId() + ")");
        }
        sql.append(" ORDER BY src.CHANNELID ASC,src.PRIORITY ASC");
        String sqlString = sql.toString(); 
        List<Map<String, Object>> listdd = jdbcTemplate.queryForList(sqlString, new Object[]{
        		reportsParamSrcMonthVO.getCompanyId(),});
        
        //合计横向的一行
        Map<String, Object> map1=new HashMap<>();
        for (Map<String, Object> day : dayList) {
        	long daysum=0;
        	for (Map<String, Object> map : listdd) {
    			daysum +=(long) map.get(day.get("dayKey"));
    		}
        	map1.put((String) day.get("dayKey"), daysum);
		}
        long Monthsum=0;
        for (Map<String, Object> map : listdd) {
        		Monthsum +=(long) map.get("hj");
    		}
        	map1.put("hj", Monthsum);
        listdd.add(map1);
		return listdd;
	}
	
	/**
     * 获取无效客资量
     * @param start
     * @param end
     * @param companyId
     * */
	public List<Map<String, Object>> getDSTGSrcMonthReportsInvalid(List<Map<String, Object>> dayList , String month,ReportsParamSrcMonthVO reportsParamSrcMonthVO,
			DsInvalidVO invalidConfig) {
		StringBuilder sql = new StringBuilder();
		String infoTabName = DBSplitUtil.getInfoTabName(reportsParamSrcMonthVO.getCompanyId());
		
        sql.append("SELECT src.SRCNAME srcName,src.ID srcId,");
        sql.append("(select COUNT(info.ID) from " + infoTabName + " info, hm_crm_client_detail deta where info.SOURCEID=src.ID AND deta.KZID=info.KZID AND info.ISDEL=0  ");
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
        //拍摄类型
    	if(StringUtil.isNotEmpty(reportsParamSrcMonthVO.getTypeId())){
    		sql.append(" AND info.TYPEID IN("+reportsParamSrcMonthVO.getTypeId()+")");
    	}
    	sql.append(" AND FROM_UNIXTIME(info.CREATETIME, '%Y/%m')='" + month + "') hj,  ");
    	//计算每天客资数
    	for (Map<String, Object> day : dayList) {
            sql.append("(select COUNT(info.ID) from " + infoTabName + " info, hm_crm_client_detail deta where info.SOURCEID=src.ID AND deta.KZID=info.KZID AND info.ISDEL=0  ");
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
        		sql.append(" AND info.TYPEID IN("+reportsParamSrcMonthVO.getTypeId()+")");
        	}
            sql.append(" AND FROM_UNIXTIME(info.CREATETIME, '%Y/%m/%d')='" + day.get("day") + "') " + day.get("dayKey") + ",");
        }	
        	
        sql.append(" src.SRCIMG srcImg ");
        sql.append(" FROM hm_crm_source src WHERE src.COMPANYID = ? AND TYPEID IN (1,2) ");
        
        if (StringUtil.isNotEmpty(reportsParamSrcMonthVO.getSourceId())) {
            sql.append(" AND src.ID IN (" + reportsParamSrcMonthVO.getSourceId() + ")");
        }
        sql.append(" ORDER BY src.CHANNELID ASC,src.PRIORITY ASC");
        String sqlString = sql.toString(); 
        List<Map<String, Object>> listInvalid = jdbcTemplate.queryForList(sqlString, new Object[]{
        		reportsParamSrcMonthVO.getCompanyId(),});
        
        //合计横向的一行
        Map<String, Object> map1=new HashMap<>();
        for (Map<String, Object> day : dayList) {
        	long daysum=0;
        	for (Map<String, Object> map : listInvalid) {
    			daysum +=(long) map.get(day.get("dayKey"));
    		}
        	map1.put((String) day.get("dayKey"), daysum);
		}
        long Monthsum=0;
        for (Map<String, Object> map : listInvalid) {
        		Monthsum +=(long) map.get("hj");
    		}
        	map1.put("hj", Monthsum);
        listInvalid.add(map1);
		return listInvalid;
	}
	
	/**
     * 获取有效客资量
     * @param start
     * @param end
     * @param companyId
     * */
	public List<Map<String, Object>> getDSTGSrcMonthReportsvalid(List<Map<String, Object>> dayList , String month,ReportsParamSrcMonthVO reportsParamSrcMonthVO,
			DsInvalidVO invalidConfig) {
		//构造集合对象，把指标放入
		StringBuilder sql = new StringBuilder();
		String infoTabName = DBSplitUtil.getInfoTabName(reportsParamSrcMonthVO.getCompanyId());
		
        sql.append("SELECT src.SRCNAME srcName,src.ID srcId,");
        sql.append("(select COUNT(info.ID) from " + infoTabName + " info, hm_crm_client_detail deta where info.SOURCEID=src.ID AND info.STATUSID NOT IN(0,98,99) AND deta.KZID=info.KZID AND info.ISDEL=0  ");
        
        //减去无效客资状态指标
        if(StringUtil.isNotEmpty(invalidConfig.getDsInvalidStatus()) ){
    		sql.append("  AND info.STATUSID NOT IN("+invalidConfig.getDsInvalidStatus()+")");
    	}
        //减去无效意向指标
        if(StringUtil.isEmpty(invalidConfig.getDsInvalidStatus()) && StringUtil.isNotEmpty(invalidConfig.getDsInvalidLevel())){
    		sql.append(" AND deta.YXLEVEL NOT IN("+invalidConfig.getDsInvalidLevel()+")");
    	}
        //待定不计算为有效时
        if(!(invalidConfig.getDdIsValid())){
        	sql.append(" AND INSTR('"+invalidConfig.getDsDdStatus()+"', CONCAT(',',info.STATUSID,',')) = 0");
        }
        //拍摄类型
    	if(StringUtil.isNotEmpty(reportsParamSrcMonthVO.getTypeId())){
    		sql.append(" AND info.TYPEID IN("+reportsParamSrcMonthVO.getTypeId()+")");
    	}
    	sql.append(" AND FROM_UNIXTIME(info.CREATETIME, '%Y/%m')='" + month + "') hj,  ");
    	//计算每天客资数
    	for (Map<String, Object> day : dayList) {
            sql.append("(select COUNT(info.ID) from " + infoTabName + " info, hm_crm_client_detail deta where info.SOURCEID=src.ID AND info.STATUSID NOT IN(0,98,99) AND deta.KZID=info.KZID AND info.ISDEL=0  ");
            //减去无效客资状态指标
            if(StringUtil.isNotEmpty(invalidConfig.getDsInvalidStatus()) ){
        		sql.append(" AND info.STATUSID NOT IN("+invalidConfig.getDsInvalidStatus()+")");
        	}
            //减去无效意向指标
            if(StringUtil.isEmpty(invalidConfig.getDsInvalidStatus()) && StringUtil.isNotEmpty(invalidConfig.getDsInvalidLevel())){
        		sql.append(" AND deta.YXLEVEL NOT IN("+invalidConfig.getDsInvalidLevel()+")");
        	}
            //待定不计算为有效时
            if(!(invalidConfig.getDdIsValid())){
            	sql.append(" AND INSTR('"+invalidConfig.getDsDdStatus()+"', CONCAT(',',info.STATUSID,',')) = 0");
            }
        	if(StringUtil.isNotEmpty(reportsParamSrcMonthVO.getTypeId())){
        		sql.append(" AND info.TYPEID IN("+reportsParamSrcMonthVO.getTypeId()+")");
        	}
            sql.append(" AND FROM_UNIXTIME(info.CREATETIME, '%Y/%m/%d')='" + day.get("day") + "') " + day.get("dayKey") + ",");
        }	
        	
        sql.append(" src.SRCIMG srcImg ");
        sql.append(" FROM hm_crm_source src WHERE src.COMPANYID = ? AND TYPEID IN (1,2) ");
        
        if (StringUtil.isNotEmpty(reportsParamSrcMonthVO.getSourceId())) {
            sql.append(" AND src.ID IN (" + reportsParamSrcMonthVO.getSourceId() + ")");
        }
        sql.append(" ORDER BY src.CHANNELID ASC,src.PRIORITY ASC");
        String sqlString = sql.toString(); 
        List<Map<String, Object>> listValid = jdbcTemplate.queryForList(sqlString, new Object[]{
        		reportsParamSrcMonthVO.getCompanyId(),});
        
      //合计横向的一行
        Map<String, Object> map1=new HashMap<>();
        for (Map<String, Object> day : dayList) {
        	long daysum=0;
        	for (Map<String, Object> map : listValid) {
    			daysum +=(long) map.get(day.get("dayKey"));
    		}
        	map1.put((String) day.get("dayKey"), daysum);
		}
        long Monthsum=0;
        for (Map<String, Object> map : listValid) {
        		Monthsum +=(long) map.get("hj");
    		}
        	map1.put("hj", Monthsum);
        listValid.add(map1);
		return listValid;
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
			dstgGoldDataReportsVO.setClientComeShopRate(parseDouble(((Double.isNaN(validComeShopRate) || Double.isInfinite(validComeShopRate)) ? 0.0 : validComeShopRate) * 100));

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
	
	
}
