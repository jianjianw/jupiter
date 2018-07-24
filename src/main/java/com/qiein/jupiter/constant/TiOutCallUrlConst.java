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
    public final static String addOrUpdateUser = "http://api.clink.cn/interfaceAction/clientInterface!save.action";

    /**
     * 删除坐席
     */
    public final static String delUser = "http://api.clink.cn/interfaceAction/clientInterface!delete.action";

    /**
     * 验证坐席工号是否存在
     */
    public final static String validateCno = "http://api.clink.cn/interfaceAction/clientInterface!validateCno.action";

    /**
     * 坐席上线
     */
    public final static String userOnLine = "http://api.clink.cn/interface/client/ClientOnline";

    /**
     * 坐席下线
     */
    public final static String userOffLine = "http://api.clink.cn/interface/client/ClientOffline";

    /**
     * 修改坐席绑定的手机
     */
    public final static String changeBindPhone = "http://api.clink.cn/interface/client/ChangeBindTel";
    /**
     * 解除绑定手机号码
     */
    public final static String unBindUserPhone = "http://api.clink.cn/interfaceAction/clientInterface!unBindCurrentTel.action";

    /**
     * 打电话
     */
    public final static String call = "http://api.clink.cn/interface/PreviewOutcall";

    /**
     * 挂断
     */
    public final static String hangUp = "http://api.clink.cn/interface/HangUp";
    /**
     * 外呼通话记录
     */
    public final static String outRecordList = "http://api.clink.cn/interfaceAction/cdrObInterface!listCdrOb.action";

    /**
     * 验证白名单并获取语音验证码
     */
    public final static String validateAndGetCode = "http://api.clink.cn/interfaceAction/whiteList!validateAndGetCode.action";

    /**
     * 添加电话号码到白名单
     */
    public final static String validateCode = "http://api.clink.cn/interfaceAction/whiteList!validateCode.action";
    /**
     * 查看用户信息
     */
    public final static String getUserInfo = "http://api.clink.cn/interfaceAction/clientInterface!view.action";
}
