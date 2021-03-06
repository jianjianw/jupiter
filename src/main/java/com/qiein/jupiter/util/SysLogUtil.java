package com.qiein.jupiter.util;

import java.util.Map;

import com.qiein.jupiter.constant.CommonConstant;
import com.qiein.jupiter.exception.ExceptionEnum;
import com.qiein.jupiter.exception.RException;

/**
 * 系统日志
 *
 * @author JingChenglong 2018/05/14 14:41
 */
public class SysLogUtil {

    public static final int LOG_TYPE_LOGIN = 1;
    public static final int LOG_TYPE_GROUP = 2;
    public static final int LOG_TYPE_CLIENT = 3;
    public static final int LOG_TYPE_CHANNEL = 4;
    public static final int LOG_TYPE_SOURCE = 5;
    public static final int LOG_TYPE_ROLE = 6;
    public static final int LOG_TYPE_DIC=9;
    public static final int LOG_TYPE_STAFF = 10;
    public static final int LOG_TYPE_COMPANY_CONFIG = 11;
    public static final String LOG_SUP_LOGIN = "登录";
    public static final String LOG_SUP_GROUP = "部门";
    public static final String LOG_SUP_CLIENT = "客资";
    public static final String LOG_SUP_CHANNEL = "渠道";
    public static final String LOG_SUP_SOURCE = "来源";
    public static final String LOG_SUP_ROLE = "权限";
    public static final String LOG_SUP_STAFF = "员工";
    public static final String LOG_SUP_LOCK = "锁定状态";
    public static final String LOG_SUP_GIVE = "交接";
    public static final String LOG_SUP_GROUP_STAFF="小组员工关系";
    public static final String LOG_SUP_INVALID_REASON="无效原因";
    public static final String LOG_SUP_RUN_OFF_REASON="流失原因";
    public static final String LOG_SUP_DIC="字典";
    public static final String LOG_SUP_COMMON_TYPE="咨询类型";
    public static final String LOG_SUP_COMPANY_CONFIG = "公司配置";

    public static final String SYS_LOG_PREFIX_ADD = "新增了";
    public static final String SYS_LOG_PREFIX_EDIT = "修改了";
    public static final String SYS_LOG_PREFIX_REMOVE = "删除了";
    public static final String SYS_LOG_PREFIX_DO = "进行了";
    public static final String SYS_LOG_PREFIX_IMPORT = "导入了";
    public static final String SYS_LOG_PREFIX_EXPORT = "导出了";
    public static final String SYS_LOG_PREFIX_RE="移出";
    public static final String SYS_LOG_PREFIX_PHYSICAL_REMOVE = "物理删除了";
    public static String getAddLog(String supName, String... params) {

        if (StringUtil.haveEmpty(supName)) {
            throw new RException(ExceptionEnum.UNKNOW_ERROR);
        }

        return getLog(SYS_LOG_PREFIX_ADD, supName, params);
    }

    public static String getEditLog(String supName, String objName, Map<String, String> params) {

        if (StringUtil.haveEmpty(supName, objName)) {
            throw new RException(ExceptionEnum.UNKNOW_ERROR);
        }
        StringBuilder sb = new StringBuilder();
        sb.append(SYS_LOG_PREFIX_EDIT);
        sb.append(supName);
        sb.append(":");
        sb.append(objName);
        if (params != null) {
            sb.append("=>[");
            for (String key : params.keySet()) {
                if (StringUtil.isEmpty(params.get(key))) {
                    continue;
                }
                sb.append(key);
                sb.append("→");
                sb.append(params.get(key));
                sb.append(CommonConstant.STR_SEPARATOR);
            }
            sb.deleteCharAt(sb.length() - 1);
            sb.append("]");
        }
        return sb.toString();
    }

    public static String getRemoveLog(String supName, String... params) {
        if (StringUtil.haveEmpty(supName)) {
            throw new RException(ExceptionEnum.UNKNOW_ERROR);
        }
        return getLog(SYS_LOG_PREFIX_REMOVE, supName, params);
    }
    public static String getPhysicalRemoveLog(String supName, String... params) {
        if (StringUtil.haveEmpty(supName)) {
            throw new RException(ExceptionEnum.UNKNOW_ERROR);
        }
        return getLog(SYS_LOG_PREFIX_PHYSICAL_REMOVE, supName, params);
    }

    public static String getLog(String prefix, String supName, String... params) {

        if (StringUtil.haveEmpty(prefix, supName)) {
            throw new RException(ExceptionEnum.UNKNOW_ERROR);
        }

        StringBuilder sb = new StringBuilder();
        sb.append(prefix);
        sb.append(supName);
        if (params != null) {
            sb.append("=>[");
            for (String param : params) {
                if (StringUtil.isNotEmpty(param)){
                    sb.append(param);
                    sb.append(CommonConstant.STR_SEPARATOR);
                }
            }
            sb.deleteCharAt(sb.length() - 1);
            sb.append("]");
        }
        return sb.toString();
    }

    public static String getImportLog(Map<String, String> params) {

        StringBuilder sb = new StringBuilder();
        sb.append(SYS_LOG_PREFIX_IMPORT);
        sb.append(LOG_SUP_CLIENT);
        if (params != null) {
            sb.append(":");
            sb.append("=>[");
            for (String key : params.keySet()) {
                if (StringUtil.isEmpty(params.get(key))) {
                    continue;
                }
                sb.append(key);
                sb.append("→");
                sb.append(params.get(key));
                sb.append(CommonConstant.STR_SEPARATOR);
            }
            sb.deleteCharAt(sb.length() - 1);
            sb.append("]");
        }
        return sb.toString();
    }

    public static String getKzEnterLog(Map<String, String> params) {

        StringBuilder sb = new StringBuilder();
        sb.append(SYS_LOG_PREFIX_ADD);
        sb.append(LOG_SUP_CLIENT);
        if (params != null) {
            sb.append(":");
            sb.append("=>[");
            for (String key : params.keySet()) {
                if (StringUtil.isEmpty(params.get(key))) {
                    continue;
                }
                sb.append(key);
                sb.append("→");
                sb.append(params.get(key));
                sb.append(CommonConstant.STR_SEPARATOR);
            }
            sb.deleteCharAt(sb.length() - 1);
            sb.append("]");
        }
        return sb.toString();
    }

    public static String getExportLog(Map<String, String> params) {

        StringBuilder sb = new StringBuilder();
        sb.append(SYS_LOG_PREFIX_EXPORT);
        sb.append(LOG_SUP_CLIENT);
        if (params != null) {
            sb.append(":");
            sb.append("=>[");
            for (String key : params.keySet()) {
                if (StringUtil.isEmpty(params.get(key))) {
                    continue;
                }
                sb.append(key);
                sb.append("→");
                sb.append(params.get(key));
                sb.append(CommonConstant.STR_SEPARATOR);
            }
            sb.deleteCharAt(sb.length() - 1);
            sb.append("]");
        }
        return sb.toString();
    }

}