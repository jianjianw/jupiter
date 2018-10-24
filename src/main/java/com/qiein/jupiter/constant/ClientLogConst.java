package com.qiein.jupiter.constant;

import com.qiein.jupiter.util.NumUtil;
import com.qiein.jupiter.util.StringUtil;
import com.qiein.jupiter.util.TimeUtil;
import com.qiein.jupiter.web.entity.po.CashLogPO;
import org.apache.commons.lang3.StringUtils;

/**
 * 客资日志操作常量
 *
 * @author JingChenglong 2018/05/09 17:49
 */
public class ClientLogConst {

    /**
     * 客资操作日志类型
     */
    public static final int INFO_LOGTYPE_ADD = 1;// 新增
    public static final int INFO_LOGTYPE_EDIT = 2;// 修改
    public static final int INFO_LOGTYPE_INVITE = 3;// 客服跟进
    public static final int INFO_LOGTYPE_APPOINT = 4;// 预约进店
    public static final int INFO_LOGTYPE_SUCCESS = 5;// 订单
    public static final int INFO_LOGTYPE_MIX = 6;// 转移
    public static final int INFO_LOGTYPE_ALLOT = 7;// 分配
    public static final int INFO_LOGTYPE_REPEAT = 8;// 重复
    public static final int INFO_LOGTYPE_COMESHOP = 9;// 进店
    public static final int INFO_LOGTYPE_REMOVE = 10;// 删除
    public static final int INFO_LOGTYPE_RECEIVE = 11;// 领取
    public static final int INFO_LOGTYPE_SCAN_WECAHT = 12;// 微信二维码扫描
    public static final int INFO_LOGTYPE_CASH = 13;// 客资收款收款记录
    public static final int INFO_LOG_TYPE_CALL = 14;//通话

    public static final String INFO_LOG_AUTO_ALLOT_TEMPLATE = "系统自动分配该客资给 => ${groupName} 的  ${appointorName} ";
    public static final String INFO_LOG_HANDLER_ALLOT_TEMPLATE = "手动分配该客资给客服 => ${groupName} 的  ${appointorName} ";
    public static final String INFO_LOG_HANDLER_ALLOT_TEMPLATE_MSJD = "手动分配该客资给门市 => ${shopName} 的  ${appointorName} ";
    public static final String INFO_LOG_AUTO_REVEICE_TEMPLATE = "推送客资领取消息给 => ${groupName} 的  ${appointorName} ";
    public static final String INFO_LOG_RECEIVE = "在客户端通过客资分配领取了客资";
    public static final String INFO_LOG_RECEIVE_PC = "通过客资分配领取了客资";
    public static final String INFO_LOG_EDIT_SEX = "将客资性别修改为：";
    public static final String INFO_LOG_EDIT_WCFLAG = "将客资微信标记为：";
    public static final String INFO_LOG_EDIT_BE_STATUS = "将筛选客资状态修改为";
    public static final String INFO_LOG_NOT_ARRIVE_SHOP = "将客资状态标记为未到店";
    public static final String CONTINUOUS_SABOTEUR_DONW = "连续怠工三次自动下线ಥ﹏ಥ";
    public static final String LIMITDAY_OVERFLOW = "今日领取客资数达到最大限额";
    public static final String INFO_LOG_CALL_PHONE = "已拨打客资电话";
    public static final String INFO_LOG_EXCEL_IMPORT = "Excel导入客资";


    /**
     * 生成客资自动分配日志
     *
     * @param groupName
     * @param appointorName
     * @return
     */
    public static final String getAutoAllotLog(String groupName, String appointorName) {

        if (StringUtil.isEmpty(groupName)) {
            groupName = "-";
        }
        if (StringUtil.isEmpty(appointorName)) {
            appointorName = "-";
        }

        return INFO_LOG_AUTO_ALLOT_TEMPLATE.replace("${groupName}", groupName).replace("${appointorName}",
                appointorName);
    }

    /**
     * 手动分配日志
     *
     * @param groupName
     * @param appointorName
     * @return
     */
    public static final String getAllotLog(String groupName, String appointorName) {

        if (StringUtil.isEmpty(groupName)) {
            groupName = "-";
        }
        if (StringUtil.isEmpty(appointorName)) {
            appointorName = "-";
        }

        return INFO_LOG_HANDLER_ALLOT_TEMPLATE.replace("${groupName}", groupName)
                .replace("${appointorName}", appointorName);
    }

    /**
     * 手动分配日志，分配给门市
     *
     * @param shopName
     * @param appointorName
     * @return
     */
    public static final String getAllotLogMsjd(String shopName, String appointorName) {

        if (StringUtil.isEmpty(shopName)) {
            shopName = "-";
        }
        if (StringUtil.isEmpty(appointorName)) {
            appointorName = "-";
        }

        return INFO_LOG_HANDLER_ALLOT_TEMPLATE_MSJD.replace("${shopName}", shopName)
                .replace("${appointorName}", appointorName);
    }

    /**
     * 生成客资自动分配日志
     *
     * @param groupName
     * @param appointorName
     * @return
     */
    public static final String getAutoReceiveLog(String groupName, String appointorName) {

        if (StringUtil.isEmpty(groupName)) {
            groupName = "-";
        }
        if (StringUtil.isEmpty(appointorName)) {
            appointorName = "-";
        }

        return INFO_LOG_AUTO_REVEICE_TEMPLATE.replace("${groupName}", groupName).replace("${appointorName}",
                appointorName);
    }

    /**
     * 生成收款记录修改日志
     *
     * @param newCash
     * @param oldCash
     * @return
     */
    public static final String getCashEditLog(CashLogPO newCash, CashLogPO oldCash) {
        if (NumUtil.isInValid(newCash.getId())) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        String title = newCash.getOperaName() + " 修改了编号为 " + newCash.getId() + " 的收款记录";
        if (oldCash.getAmount() != newCash.getAmount()) {
            sb.append("，收款金额由 " + oldCash.getAmount() + " 改为：" + newCash.getAmount());
        }
        if (StringUtil.isNotEmpty(newCash.getPayStyleName()) && StringUtil.isNotEmpty(oldCash.getPayStyleName()) && !oldCash.getPayStyleName().equals(newCash.getPayStyleName())) {
            sb.append("，收款方式由 " + oldCash.getPayStyleName() + " 改为：" + newCash.getPayStyleName());
        }
        if (NumUtil.isValid(newCash.getPaymentTime()) && NumUtil.isValid(oldCash.getPaymentTime()) && TimeUtil.checkTimesDifRange(newCash.getPaymentTime(), oldCash.getPaymentTime())) {
            sb.append("，收款时间由 " + TimeUtil.intMillisToTimeStr(oldCash.getPaymentTime()) + " 改为：" + TimeUtil.intMillisToTimeStr(newCash.getPaymentTime()));
        }
        if (StringUtil.isNotEmpty(newCash.getStaffName()) && StringUtil.isNotEmpty(oldCash.getStaffName()) && !newCash.getStaffName().equals(oldCash.getStaffName())) {
            sb.append("，收款人由 " + oldCash.getStaffName() + " 改为：" + newCash.getStaffName());
        }
        if (StringUtil.isEmpty(sb.toString())) {
            title = "";
        }
        return title + sb.toString();
    }

    /**
     * 添加收款日志
     *
     * @param newCash
     * @param oldCash
     * @return
     */
    public static final String getCashAddLog(CashLogPO cashLogPO) {
        StringBuilder sb = new StringBuilder();
        sb.append(cashLogPO.getOperaName() + " 添加了收款记录，");
        sb.append("，收款金额: " + cashLogPO.getAmount());
        sb.append("，收款方式： " + cashLogPO.getPayStyleName());
        sb.append("，收款时间： " + TimeUtil.intMillisToTimeStr(cashLogPO.getPaymentTime()));
        sb.append("，收款人： " + cashLogPO.getStaffName());
        return sb.toString();
    }

    /**
     * 删除收款日志
     */
    public static final String getCashDeleteLog(CashLogPO cashLogPO, String staffName) {
        StringBuilder sb = new StringBuilder();
        sb.append(staffName + " 删除了编号为：" + cashLogPO.getId() + " 的 " + cashLogPO.getStaffName() + " 的收款记录:" + cashLogPO.getAmount());
        return sb.toString();
    }
}