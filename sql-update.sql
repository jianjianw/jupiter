#8.30  新增公司报表设置
ALTER TABLE hm_pub_company
  ADD `REPORTSCONFIG` VARCHAR(1024) DEFAULT NULL
COMMENT '报表配置'



###9.14
client info 表新增 LETTERID  短字母编号

###  10/15   添加收款类型字段
ALTER TABLE hm_crm_cash_log
  ADD `TYPEID` smallint(6) DEFAULT 1
COMMENT '收款类型  1：首款  2：补款'

###  10/19 增加   报表设置  、录入指定客服权限
INSERT INTO  hm_pub_permission (`ID`, `PERMISSIONNAME`, `TYPEID`) VALUES (202, '报表设置', 1);
INSERT INTO  hm_pub_permission (`ID`, `PERMISSIONNAME`, `TYPEID`) VALUES (203, '录入指定客服', 1);
INSERT INTO  hm_pub_permission (`ID`, `PERMISSIONNAME`, `TYPEID`) VALUES (204, '修改客资联系方式', 1);