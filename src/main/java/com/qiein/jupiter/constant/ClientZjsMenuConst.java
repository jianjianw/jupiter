package com.qiein.jupiter.constant;

import java.util.HashMap;
import java.util.Map;

/**
 * 转介绍客资必填项菜单常量
 * @Auther: Tt(yehuawei)
 * @Date: 2018/7/7 10:49
 */
public class ClientZjsMenuConst {
    public static Map<String,String> zjsMenu = new HashMap<>();

    static {
        zjsMenu.put("kzname","客资姓名");
        zjsMenu.put("kzphone","客资电话");
        zjsMenu.put("kzwechat","客资微信");
        zjsMenu.put("kzqq","客资qq");
        zjsMenu.put("sex","客资性别");
        zjsMenu.put("kzww","客资旺旺");
        zjsMenu.put("matename","配偶姓名");
        zjsMenu.put("matephone","配偶电话");
        zjsMenu.put("matewechat","配偶微信");
        zjsMenu.put("mateqq","配偶qq");
        zjsMenu.put("shopname","门店名称");
        zjsMenu.put("zxstyle","咨询方式");
        zjsMenu.put("typeid","拍摄类型");
        zjsMenu.put("address","客人地址");
        zjsMenu.put("remark","备注");
        zjsMenu.put("ysrange","预算范围");
        zjsMenu.put("marrytime","婚期时间");
        zjsMenu.put("yxlevel","意向等级");
        zjsMenu.put("yptime","预拍时间");
//        zjsMenu.put("channelname","所属渠道");
//        zjsMenu.put("sourcename","所属来源");
//        zjsMenu.put("srctype","来源类型");;
////        zjsMenu.put("keyword","关键字");
////        zjsMenu.put("adaddress","广告着陆页");
////        zjsMenu.put("adid","广告ID");
    }
}
