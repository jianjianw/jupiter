#8.30  新增公司报表设置
ALTER TABLE hm_pub_company
  ADD `REPORTSCONFIG` VARCHAR(1024) DEFAULT NULL
COMMENT '报表配置'



###9.14
client info 表新增 LETTERID  短字母编号