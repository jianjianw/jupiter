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
    public static final String LOG_SUP_LOGIN = "登录";
    public static final String LOG_SUP_GROUP = "部门";
    public static final String LOG_SUP_CLIENT = "客资";
    public static final String LOG_SUP_CHANNEL = "渠道";
    public static final String LOG_SUP_SOURCE = "来源";

    public static final String SYS_LOG_PREFIX_ADD = "新增了";
    public static final String SYS_LOG_PREFIX_EDIT = "修改了";
    public static final String SYS_LOG_PREFIX_REMOVE = "删除了";
    public static final String SYS_LOG_PREFIX_DO = "进行了";
    public static final String SYS_LOG_PREFIX_IMPORT = "导入了";
    public static final String SYS_LOG_PREFIX_EXPORT = "导出了";

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
                sb.append(param);
                sb.append(CommonConstant.STR_SEPARATOR);
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