package com.qiein.jupiter.util;

import com.qiein.jupiter.exception.ExceptionEnum;
import com.qiein.jupiter.exception.RException;

/**
 * 数据库分割util
 */
public class DBSplitUtil {

	/**
	 * 客资分配日志表
	 */
	public final static String ALLOT_LOG_ = "hm_crm_allot_log_";
	/**
	 * 客资详情表
	 */
	public final static String CLIENT_DETAIL_ = "hm_crm_client_detail_";
	/**
	 * 客资信息表
	 */
	public final static String CLIENT_INFO_ = "hm_crm_client_info_";
	/**
	 * 客资日志表
	 */
	public final static String CLIENT_LOG_ = "hm_crm_client_log_";
	/**
	 * 客资备注表
	 */
	public final static String CLIENT_REMARK_ = "hm_crm_client_remark_";
	/**
	 * 通知消息表
	 */
	public final static String NEWS_ = "hm_crm_news_";

	/**
	 * 获取表名
	 *
	 * @param tableName
	 * @param companyId
	 * @return
	 */
	public static String getTableName(String tableName, int companyId) {
		// 如果表名或者公司id为空，抛出异常
		if (StringUtil.isEmpty(tableName) || companyId == 0) {
			throw new RException(ExceptionEnum.DB_SPLIT_ERROR);
		}
		return " " + tableName + companyId + " ";
	}

}
