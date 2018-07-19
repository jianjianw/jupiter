package com.qiein.jupiter.constant;

/**
 * 天润外呼系统url
 *
 * @Author: shiTao
 */
public class TiOutCallUrlConst {

    /**
     * 新增获取修改坐席
     */
    public static String addOrUpdateUser = "http://api.clink.cn/interfaceAction/clientInterface!save.action";

    /**
     * 删除坐席
     */
    public static String delUser = "http://api.clink.cn/interfaceAction/clientInterface!delete.action";

    /**
     * 验证坐席工号是否存在
     */
    public static String validateCno = "http://api.clink.cn/interfaceAction/clientInterface!validateCno.action";

    /**
     * 坐席上线
     */
    public static String userOnLine = "http://api.clink.cn/interface/client/ClientOnline";

    /**
     * 坐席下线
     */
    public static String userOffLine = "http://api.clink.cn/interface/client/ClientOffline";

    /**
     * 修改坐席绑定的手机
     */
    public static String changeBindPhone = "http://api.clink.cn/interface/client/ChangeBindTel";
    /**
     * 解除绑定手机号码
     */
    public static String unBindUserPhone = "http://api.clink.cn/interfaceAction/clientInterface!unBindCurrentTel.action";

    /**
     * 打电话
     */
    public static String call = "http://api.clink.cn/interface/PreviewOutcall";

    /**
     * 挂断
     */
    public static String hangUp = "http://api.clink.cn/interface/HangUp";
    /**
     * 通话记录
     */
    public static String recordList = "http://InterfaceServerIp:port /interfaceAction/cdrIbInterface!listCdrIb.action";

    /**
     * 验证白名单并获取语音验证码
     */
    public static String validateAndGetCode = "http://api.clink.cn/interfaceAction/whiteList!validateAndGetCode.action";

    /**
     * 添加电话号码到白名单
     */
    public static String validateCode = "http://api.clink.cn/interfaceAction/whiteList!validateCode.action";
}
