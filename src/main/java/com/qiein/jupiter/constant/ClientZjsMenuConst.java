package com.qiein.jupiter.constant;

import java.util.HashMap;
import java.util.Map;

/**
 * 转介绍客资必填项菜单常量
 * @Auther: Tt(yehuawei)
 * @Date: 2018/7/7 10:49
 */
public class ClientZjsMenuConst {
    public static Map<String,String> QY_ZJS_MENU = new HashMap<>();
    public static Map<String,String> LK_ZJS_MENU = new HashMap<>();

    static {
        QY_ZJS_MENU.put("kzName","客资姓名");
        QY_ZJS_MENU.put("kzPhone","客资电话");
        QY_ZJS_MENU.put("kzWechat","客资微信");
        QY_ZJS_MENU.put("kzQq","客资qq");
        QY_ZJS_MENU.put("sex","客资性别");
        QY_ZJS_MENU.put("kzWw","客资旺旺");
        QY_ZJS_MENU.put("mateName","配偶姓名");
        QY_ZJS_MENU.put("matePhone","配偶电话");
        QY_ZJS_MENU.put("mateWeChat","配偶微信");
        QY_ZJS_MENU.put("mateQq","配偶qq");
        QY_ZJS_MENU.put("shopName","门店名称");
        QY_ZJS_MENU.put("zxStyle","咨询方式");
        QY_ZJS_MENU.put("typeId","拍摄类型");
        QY_ZJS_MENU.put("address","客人地址");
        QY_ZJS_MENU.put("remark","备注");
        QY_ZJS_MENU.put("ysRange","预算范围");
        QY_ZJS_MENU.put("marryTime","婚期时间");
        QY_ZJS_MENU.put("yxLevel","意向等级");
        QY_ZJS_MENU.put("ypTime","预拍时间");
        //以上是亲友转介绍下拉菜单
        //==========================
        //以下是老客转介绍下拉菜单
        LK_ZJS_MENU.put("kzName","客资姓名");
        LK_ZJS_MENU.put("kzPhone","客资电话");
        LK_ZJS_MENU.put("kzWechat","客资微信");
        LK_ZJS_MENU.put("kzQq","客资qq");
        LK_ZJS_MENU.put("sex","客资性别");
        LK_ZJS_MENU.put("mateName","配偶姓名");
        LK_ZJS_MENU.put("matePhone","配偶电话");
        LK_ZJS_MENU.put("mateWeChat","配偶微信");
        LK_ZJS_MENU.put("mateQq","配偶qq");
        LK_ZJS_MENU.put("typeId","拍摄类型");
        LK_ZJS_MENU.put("marryTime","婚期时间");
        LK_ZJS_MENU.put("ypTime","预拍时间");
        LK_ZJS_MENU.put("oldKzName","老客姓名");
        LK_ZJS_MENU.put("oldKzPhone","老客电话");
    }
}
