#8.30  新增公司报表设置
ALTER TABLE hm_pub_company
  ADD `REPORTSCONFIG` VARCHAR(1024) DEFAULT NULL
COMMENT '报表配置'



###9.14
client info 表新增 LETTERID  短字母编号


###10月
员工表staff
新增
WHEELFLAG	tinyint	255	0	-1	0	0	0			0	轮单标志					0	0	0	0	0	0	0
SETTINGS	varchar	1024	0	-1	0	0	0				个人设置			utf8	utf8_general_ci	0	0	0	0	0	0	0

page_conf
ACTIONSQL	varchar	1024	0	-1	0	0	0				action的SQL语句			utf8	utf8_general_ci	0	0	0	0	0	0	0
SQLCONDITION	varchar	2048	0	-1	0	0	0				SQL条件的序列化			utf8	utf8_general_ci	0	0	0	0	0	0	0
SHOWFLAG	bit	1	0	-1	0	0	0			b'1'	显示标志					0	0	0	0	0	0	0

###  10/15   添加收款类型字段
ALTER TABLE hm_crm_cash_log
  ADD `TYPEID` smallint(6) DEFAULT 1
COMMENT '收款类型  1：首款  2：补款'

###  10/19 增加   报表设置  、录入指定客服权限
INSERT INTO  hm_pub_permission (`ID`, `PERMISSIONNAME`, `TYPEID`) VALUES (202, '报表设置', 1);
INSERT INTO  hm_pub_permission (`ID`, `PERMISSIONNAME`, `TYPEID`) VALUES (203, '录入指定客服', 1);
INSERT INTO  hm_pub_permission (`ID`, `PERMISSIONNAME`, `TYPEID`) VALUES (204, '修改客资联系方式', 1);


###  10/23   增加报表权限
INSERT INTO hm_pub_permission(`ID`, `PERMISSIONNAME`, `TYPEID`) VALUES (205, '电商推广-来源统计', 1);
INSERT INTO hm_pub_permission(`ID`, `PERMISSIONNAME`, `TYPEID`) VALUES (206, '电商推广-渠道统计', 1);
INSERT INTO hm_pub_permission(`ID`, `PERMISSIONNAME`, `TYPEID`) VALUES (207, '电商推广-推广统计', 1);
INSERT INTO hm_pub_permission(`ID`, `PERMISSIONNAME`, `TYPEID`) VALUES (208, '电商推广-客资状态', 1);
INSERT INTO hm_pub_permission(`ID`, `PERMISSIONNAME`, `TYPEID`) VALUES (209, '电商推广-月度统计', 1);
INSERT INTO hm_pub_permission(`ID`, `PERMISSIONNAME`, `TYPEID`) VALUES (210, '电商推广-年度统计', 1);
INSERT INTO hm_pub_permission(`ID`, `PERMISSIONNAME`, `TYPEID`) VALUES (211, '电商推广-广告信息', 1);
INSERT INTO hm_pub_permission(`ID`, `PERMISSIONNAME`, `TYPEID`) VALUES (212, '电商推广-咨询方式', 1);
INSERT INTO hm_pub_permission(`ID`, `PERMISSIONNAME`, `TYPEID`) VALUES (213, '电商推广-省域分析', 1);
INSERT INTO hm_pub_permission(`ID`, `PERMISSIONNAME`, `TYPEID`) VALUES (214, '电商推广-市域分析', 1);
INSERT INTO hm_pub_permission(`ID`, `PERMISSIONNAME`, `TYPEID`) VALUES (215, '电商推广-关键词分析', 1);
INSERT INTO hm_pub_permission(`ID`, `PERMISSIONNAME`, `TYPEID`) VALUES (216, '电商推广-无效原因', 1);
INSERT INTO hm_pub_permission(`ID`, `PERMISSIONNAME`, `TYPEID`) VALUES (217, '电商客服-客服数据', 1);
INSERT INTO hm_pub_permission(`ID`, `PERMISSIONNAME`, `TYPEID`) VALUES (218, '电商客服-无效原因', 1);
INSERT INTO hm_pub_permission(`ID`, `PERMISSIONNAME`, `TYPEID`) VALUES (219, '电商客服-客资状态', 1);
INSERT INTO hm_pub_permission(`ID`, `PERMISSIONNAME`, `TYPEID`) VALUES (220, '转介绍-来源分析', 1);
INSERT INTO hm_pub_permission(`ID`, `PERMISSIONNAME`, `TYPEID`) VALUES (221, '转介绍-邀约统计', 1);
INSERT INTO hm_pub_permission(`ID`, `PERMISSIONNAME`, `TYPEID`) VALUES (222, '转介绍-月度统计', 1);
INSERT INTO hm_pub_permission(`ID`, `PERMISSIONNAME`, `TYPEID`) VALUES (223, '转介绍-年度统计', 1);
INSERT INTO hm_pub_permission(`ID`, `PERMISSIONNAME`, `TYPEID`) VALUES (224, '转介绍-全员提报', 1);
INSERT INTO hm_pub_permission(`ID`, `PERMISSIONNAME`, `TYPEID`) VALUES (225, '转介绍-老客统计', 1);
INSERT INTO hm_pub_permission(`ID`, `PERMISSIONNAME`, `TYPEID`) VALUES (226, '门市-入店统计', 1);
INSERT INTO hm_pub_permission(`ID`, `PERMISSIONNAME`, `TYPEID`) VALUES (227, '财务-渠道订单统计', 1);
INSERT INTO hm_pub_permission(`ID`, `PERMISSIONNAME`, `TYPEID`) VALUES (228, '财务-月度订单统计', 1);
INSERT INTO hm_pub_permission(`ID`, `PERMISSIONNAME`, `TYPEID`) VALUES (229, '其他-联系方式修改', 1);
INSERT INTO hm_pub_permission(`ID`, `PERMISSIONNAME`, `TYPEID`) VALUES (230, '其他-微信扫码记录', 1);
