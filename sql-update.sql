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
