package com.qiein.jupiter;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.qiein.jupiter.util.TimeUtil;
import com.qiein.jupiter.web.entity.po.StaffPO;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.qiein.jupiter.enums.OrderSuccessTypeEnum;
import com.qiein.jupiter.enums.TipMsgEnum;
import com.qiein.jupiter.enums.WebSocketMsgEnum;
import com.qiein.jupiter.web.entity.dto.OrderSuccessMsg;
import com.qiein.jupiter.web.entity.dto.WebSocketMsgDTO;

public class AllTest {

    @Test
    public void test() {
        List<Object> list = new ArrayList<Object>();
        System.out.println(list.isEmpty());

    }

    public static void main(String[] args) {
        System.out.println(getMap().get("KEY"));
    }

    public static Map<String, String> getMap() {
        Map<String, String> map = new HashMap<>();
        map.put("KEY", "INIT");
        try {
            map.put("KEY", "TRY");
            return map;
        } catch (Exception e) {
            map.put("KEY", "CATCH");
        } finally {
            map.put("KEY", "FINALLY");
            map = null;
        }
        return map;
    }

    public static int getInt() {
        int a = 0;
        try {
            a = 1;
            return a;
        } catch (Exception e) {
            a = 2;
        } finally {
            a = 3;
        }
        return a;
    }

    public static String getString() {
        String a = "0";
        try {
            a = "1";
            return a;
        } catch (Exception e) {
            a = "2";
        } finally {
            a = "3";
        }
        return a;
    }

    @Test
    public void testTime() {
        Long time = 43223L;
        Date javaDate = HSSFDateUtil.getJavaDate(time);
        System.out.println(javaDate);
    }

    @Test
    public void testEnum() {
        System.out.println(TipMsgEnum.ADD_BRAND_SUCCESS);
    }

    @Test
    public void testMsg() {
        OrderSuccessMsg orderSuccessMsg = new OrderSuccessMsg();
        orderSuccessMsg.setCompanyId(1);
        orderSuccessMsg.setStaffName("张三1");
        orderSuccessMsg.setShopName("三亚");
        orderSuccessMsg.setAmount("12000");
        orderSuccessMsg.setType(OrderSuccessTypeEnum.ArrivalShop);
        orderSuccessMsg.setSrcImg(
                "https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=156581925,3170808657&fm=27&gp=0.jpg");
        orderSuccessMsg.setHeadImg(
                "https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=4005596794,992112216&fm=27&gp=0.jpg");
        WebSocketMsgDTO webSocketMsgDTO = new WebSocketMsgDTO();
        webSocketMsgDTO.setCompanyId(1);
        webSocketMsgDTO.setType(WebSocketMsgEnum.OrderSuccess);
        webSocketMsgDTO.setContent(JSONObject.toJSONString(orderSuccessMsg));

        System.out.println(JSONObject.toJSONString(webSocketMsgDTO));
    }

    @Test
    public void testDate() {
        Date yesterDay = TimeUtil.getYesterDay(new Date());
        int i = TimeUtil.dateToIntMillis(yesterDay);
        System.out.println(i);
    }


    @Test
    public void testObj1() {
        StaffPO staffPO = new StaffPO();
        doSomething(staffPO);
        System.out.println(staffPO.getPhone());
    }

    private void doSomething(StaffPO staffPO) {
        staffPO.setPhone("12312321");
    }


    @Test
    public void testList() {
//        List<Integer> list = null;
//        for (Integer integer : list) {
//
//        }

    }

    @Test
    public void testSql(){
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT source.id,source.srcname,");
        //总客资
        sb.append("IFNULL(client_count.client_count,0) as all_client_count,");
        //有效客资
        sb.append("IFNULL(client_count.client_count- IFNULL(client_wait_count.client_count ,0)- IFNULL(client_in_valid_count.client_count,0) ,0) as valid_client_count,");
        //筛选客资
        sb.append("IFNULL(client_wait_count.client_count ,0) as wait_client_count,");
        //无效客资
        sb.append("IFNULL(client_in_valid_count.client_count,0) as in_valid_client_count,");
        //预约量
        sb.append("IFNULL(client_appoint_count.client_count,0) as appoint_client_count,");
        //入店量
        sb.append("IFNULL(client_come_shop_count.client_count,0) as come_shop_client_count,");
        //成交量
        sb.append("IFNULL(client_success_count.client_count,0) as success_client_count,");
        //有效率
        sb.append("IFNULL(IFNULL(client_count.client_count- IFNULL(client_wait_count.client_count ,0)- IFNULL(client_in_valid_count.client_count,0) ,0)/client_count.client_count,0) * 100 as valid_rate,");
        //无效率
        sb.append("IFNULL(IFNULL(client_in_valid_count.client_count,0)/client_count.client_count,0)* 100 as in_valid_rate,");
        //毛客资入店率 = 入店量 / 客资量
        sb.append("IFNULL(IFNULL(client_appoint_count.client_count,0)/ client_count.client_count,0)* 100 as appoint_rate,");
        //有效客资入店率 = 入店量 / 有效量
        sb.append("IFNULL(IFNULL(client_appoint_count.client_count,0) /IFNULL(client_count.client_count- IFNULL(client_wait_count.client_count ,0)- IFNULL(client_in_valid_count.client_count,0) ,0),0)* 100 as valid_appoint_rate,");
        //毛客资成交率 = 成交量 / 客资量
        sb.append("IFNULL(IFNULL(client_success_count.client_count,0)/ client_count.client_count,0)* 100 as success_rate,");
        //有效客资成交率 = 成交量 / 有效量
        sb.append("IFNULL(IFNULL(client_success_count.client_count,0)/ IFNULL(client_count.client_count- IFNULL(client_wait_count.client_count ,0)- IFNULL(client_in_valid_count.client_count,0) ,0),0)* 100 as valid_success_rate,");
        //入店成交率 = 成交量 / 入店量
        sb.append("IFNULL(IFNULL(client_come_shop_count.client_count,0)/ IFNULL(client_appoint_count.client_count,0),0)* 100 as come_shop_success_rate,");
        //花费：花费记录在各个来源上面，做累加计算
        sb.append("IFNULL(client_cost.source_cost,0) AS  source_client_cost,");
        //毛客资成本：花费 / 客资量
        sb.append("IFNULL(IFNULL(client_cost.source_cost,0)/ client_count.client_count,0) as client_cost,");
        //有效客资成本：花费 / 有效量
        sb.append("IFNULL(IFNULL(client_cost.source_cost,0) /IFNULL(client_count.client_count- IFNULL(client_wait_count.client_count ,0)- IFNULL(client_in_valid_count.client_count,0) ,0),0 )  as valid_client_cost,");
        // 入店成本：花费 / 入店量
        sb.append("IFNULL(IFNULL(client_cost.source_cost,0) /IFNULL(client_come_shop_count.client_count,0),0 )  as appoint_client_cost,");
        //成交成本：花费 / 成交量
        sb.append("IFNULL(IFNULL(client_cost.source_cost,0) /IFNULL(client_success_count.client_count,0),0 )  as success_client_cost,");
        //套系总金额：成交客资里面的 sum(AMOUNT)
        sb.append("IFNULL(client_success_amount.client_amout,0) as success_client_amount,");
        //总已收金额：成交客资和保留成交客资里面的 sum(STAYAMOUT)
        sb.append("IFNULL(client_success_stay_amount.client_stay_amout,0) as success_client_stay_amount,");
        //已收占比：总已收 / 套系总金额
        sb.append("IFNULL(IFNULL(client_success_stay_amount.client_stay_amout,0)/IFNULL(client_success_amount.client_amout,0),0)* 100 as receive_ratio,");
        //ROI ：套系总金额 / 花费
        sb.append("IFNULL(IFNULL(client_success_amount.client_amout,0)/IFNULL(client_cost.source_cost,0) ,0)* 100 as roi,");
        //筛选率
        sb.append("IFNULL(IFNULL(client_wait_count.client_count ,0)/IFNULL(client_count.client_count,0),0)* 100 as wait_rate");

        sb.append(" from hm_crm_source source");
        //总客资量
        sb.append(" left join");
        sb.append(" (select SOURCEID,COUNT(1) as client_count from hm_crm_client_info_1 client where client.isdel = 0 and client.companyId = ? AND client.CREATETIME BETWEEN ? AND ? ");
        //TODO 条件拼接
        sb.append(" GROUP BY SOURCEID) client_count on client_count.SOURCEID = source.id");
//        fieldList.add(hashMap.get("companyId"));
//        fieldList.add(hashMap.get("startTime"));
//        fieldList.add(hashMap.get("endTime"));
        //待定客资
        sb.append(" left join");
        sb.append(" (select SOURCEID,COUNT(1) as client_count from hm_crm_client_info_1 client where client.isdel = 0 and (client.statusId= 1 or client.STATUSID = 6 or client.STATUSID = 12) and client.companyId = ? AND client.CREATETIME BETWEEN ? AND ? ");
        //TODO 条件拼接
        sb.append(" GROUP BY SOURCEID) client_wait_count on client_wait_count.SOURCEID = source.id");
//        fieldList.add(hashMap.get("companyId"));
//        fieldList.add(hashMap.get("startTime"));
//        fieldList.add(hashMap.get("endTime"));
        //无效客资
        sb.append(" left join");
        sb.append(" (select SOURCEID,COUNT(1) as client_count from hm_crm_client_info_1 client");
        sb.append(" left join");
        sb.append(" hm_crm_client_detail_1 detail on client.KZID = detail.KZID");
        sb.append(" left join");
        sb.append(" (select company.id,company.DSINVALIDSTATUS,company.DSINVALIDLEVEL from hm_pub_company company where id = ?) company");
//        fieldList.add(hashMap.get("companyId"));
        sb.append(" on client.COMPANYID = company.id");
        sb.append(" where");
        sb.append(" IF(company.DSINVALIDLEVEL is not null,");
        sb.append("(client.CLASSID = 2 and detail.YXLEVEL in(select  cast(SUBSTRING_INDEX(SUBSTRING_INDEX(result.DSINVALIDLEVEL, ',', numbers.n), ',', -1) as SIGNED) as  yx_level  from (select @rownum := @rownum+1 as n from (SELECT @rownum:=0,t.dicCode from hm_crm_dictionary t where t.dictype = 'yx_level' and t.priority >0 and t.companyId = ?) t  ) numbers inner join (select company.id,company.DSINVALIDSTATUS,company.DSINVALIDLEVEL from  hm_pub_company company where ID = ?) result");
//        fieldList.add(hashMap.get("companyId"));
//        fieldList.add(hashMap.get("companyId"));
        sb.append(" on CHAR_LENGTH(result.DSINVALIDLEVEL)-CHAR_LENGTH(REPLACE(result.DSINVALIDLEVEL, ',', ''))>=numbers.n-1)),");
        sb.append(" 1!=1) ");
        sb.append(" or");
        sb.append(" ( IF( company.DSINVALIDSTATUS is not null,");
        sb.append(" client.STATUSID in (select  cast(SUBSTRING_INDEX(SUBSTRING_INDEX(result.DSINVALIDSTATUS, ',', numbers.n), ',', -1) as SIGNED) as  invalid_status  from (select id as n from  hm_crm_client_status) numbers inner join (select company.id,company.DSINVALIDSTATUS,company.DSINVALIDLEVEL from  hm_pub_company company where ID = ?) result");
//        fieldList.add(hashMap.get("companyId"));
        sb.append(" on CHAR_LENGTH(result.DSINVALIDSTATUS)-CHAR_LENGTH(REPLACE(result.DSINVALIDSTATUS, ',', ''))>=numbers.n-1),");
        sb.append(" 1 = 1)");
        sb.append(" and  client.isdel = 0  and client.CLASSID =5 and YXLEVEL is not null) and client.companyId = ? and client.CREATETIME BETWEEN ? AND ?");
        //TODO 条件拼接
//        fieldList.add(hashMap.get("companyId"));
//        fieldList.add(hashMap.get("startTime"));
//        fieldList.add(hashMap.get("endTime"));
        sb.append(" GROUP BY SOURCEID) client_in_valid_count on client_in_valid_count.SOURCEID = source.id");
        //预约量
        sb.append(" left join");
        sb.append(" (select client_count.SOURCEID,IFNULL(IFNULL(client_count.client_count,0) - IFNULL(client_wait_count.client_count,0) - IFNULL(client_in_valid_count.client_count,0),0) as client_count from ");
        sb.append(" (select SOURCEID,COUNT(1) as client_count from hm_crm_client_info_1 client where client.isdel = 0  and client.companyId = ? and client.COMESHOPTIME BETWEEN ? AND ? ");
        //TODO 条件拼接
        sb.append(" GROUP BY SOURCEID) client_count");
//        fieldList.add(hashMap.get("companyId"));
//        fieldList.add(hashMap.get("startTime"));
//        fieldList.add(hashMap.get("endTime"));
        sb.append(" left join");
        sb.append(" (select SOURCEID,COUNT(1) as client_count from hm_crm_client_info_1 client where client.isdel = 0 and client.companyId = ?  and (client.statusId= 1 or client.STATUSID = 6 or client.STATUSID = 12)  AND client.COMESHOPTIME  BETWEEN ? AND ? ");
        //TODO 条件拼接
        sb.append(" GROUP BY SOURCEID) client_wait_count ");
//        fieldList.add(hashMap.get("companyId"));
//        fieldList.add(hashMap.get("startTime"));
//        fieldList.add(hashMap.get("endTime"));
        sb.append(" on client_count.SOURCEID = client_wait_count.SOURCEID");
        sb.append(" left join ");
        sb.append(" (select SOURCEID,COUNT(1) as client_count from hm_crm_client_info_1 client");
        sb.append(" left join");
        sb.append(" hm_crm_client_detail_1 detail on client.KZID = detail.KZID");
        sb.append(" left join");
        sb.append(" (select company.id,company.DSINVALIDSTATUS,company.DSINVALIDLEVEL from hm_pub_company company where id = ?) company");
//        fieldList.add(hashMap.get("companyId"));
        sb.append(" on client.COMPANYID = company.id ");
        sb.append(" where");
        sb.append(" IF(");
        sb.append(" company.DSINVALIDLEVEL is not null,");
        sb.append(" (client.CLASSID = 2 and detail.YXLEVEL in(");
        sb.append(" select  cast(SUBSTRING_INDEX(SUBSTRING_INDEX(result.DSINVALIDLEVEL, ',', numbers.n), ',', -1) as SIGNED) as  yx_level  from (select @rownum := @rownum+1 as n from (SELECT @rownum:=0,t.dicCode from hm_crm_dictionary t where t.dictype = 'yx_level' and t.priority >0 and t.companyId=?) t  ) numbers inner join (select company.id,company.DSINVALIDSTATUS,company.DSINVALIDLEVEL from  hm_pub_company company where ID = ?) result");
//        fieldList.add(hashMap.get("companyId"));
//        fieldList.add(hashMap.get("companyId"));
        sb.append(" on CHAR_LENGTH(result.DSINVALIDLEVEL)-CHAR_LENGTH(REPLACE(result.DSINVALIDLEVEL, ',', ''))>=numbers.n-1))");
        sb.append(" , 1 != 1)");
        sb.append(" or");
        sb.append(" ( IF(");
        sb.append(" company.DSINVALIDSTATUS is not null,");
        sb.append(" client.STATUSID in (select  cast(SUBSTRING_INDEX(SUBSTRING_INDEX(result.DSINVALIDSTATUS, ',', numbers.n), ',', -1) as SIGNED) as  invalid_status  from (select id as n from  hm_crm_client_status) numbers inner join (select company.id,company.DSINVALIDSTATUS,company.DSINVALIDLEVEL from  hm_pub_company company where ID = ?) result");
//        fieldList.add(hashMap.get("companyId"));
        sb.append(" on CHAR_LENGTH(result.DSINVALIDSTATUS)-CHAR_LENGTH(REPLACE(result.DSINVALIDSTATUS, ',', ''))>=numbers.n-1),");
        sb.append(" 1 = 1)");
        sb.append(" and  client.isdel = 0  and client.CLASSID =5 and detail.YXLEVEL is not null) AND client.COMESHOPTIME BETWEEN ? AND ?");
//        fieldList.add(hashMap.get("startTime"));
//        fieldList.add(hashMap.get("endTime"));
        //TODO 条件拼接
        sb.append(" GROUP BY SOURCEID) client_in_valid_count");
        sb.append(" on client_in_valid_count.client_count = client_wait_count.SourceId) client_appoint_count on client_appoint_count.SOURCEID = source.id");
        //入店量
        sb.append(" left join");
        sb.append(" (select SOURCEID,COUNT(1) as client_count from hm_crm_client_info_1 client where client.isdel = 0 and (client.classId = 4 or client.classId = 5) and client.statusID != 98 and client.statusId != 99 and client.companyId =? and  client.COMESHOPTIME BETWEEN ? and ? ");
        //TODO 条件拼接
        sb.append(" GROUP BY SOURCEID) client_come_shop_count on client_come_shop_count.SOURCEID = source.id");
//        fieldList.add(hashMap.get("companyId"));
//        fieldList.add(hashMap.get("startTime"));
//        fieldList.add(hashMap.get("endTime"));
        //成交量
        sb.append(" left join");
        sb.append(" (select SOURCEID,COUNT(1) as client_count from hm_crm_client_info_1 client where client.isdel = 0 and client.classId = 5 and client.statusID != 98 and client.statusId != 99 and client.companyId = ? and client.SUCCESSTIME BETWEEN ? and ? ");
        //TODO 条件拼接
        sb.append(" GROUP BY SOURCEID) client_success_count on client_success_count.SOURCEID = source.id");
//        fieldList.add(hashMap.get("companyId"));
//        fieldList.add(hashMap.get("startTime"));
//        fieldList.add(hashMap.get("endTime"));
        //花费
        sb.append(" left join");
        sb.append(" (select SRCID,SUM(cost.cost) as source_cost from hm_crm_cost cost,hmm_client_info_1 client where cost.srcId = client.sourceId and cost.companyId = ? AND cost.CREATETIME BETWEEN ? AND ? ");
        //TODO 条件拼接
        sb.append(" group by cost.SRCID ) as client_cost on  client_cost.SRCID = source.id");
//        fieldList.add(hashMap.get("companyId"));
//        fieldList.add(hashMap.get("startTime"));
//        fieldList.add(hashMap.get("endTime"));
        //套系总金额
        sb.append(" left join");
        sb.append(" (select client.SOURCEID,SUM(detail.AMOUNT) as client_amout from hm_crm_client_info_1 client,hm_crm_client_detail_1 detail where client.KZID = detail.KZID and  client.classId = 5 and client.companyId = ? and client.CREATETIME BETWEEN ? AND ? ");
        //TODO 条件拼接
        sb.append(" group by client.SOURCEID) as client_success_amount on client_success_amount.SOURCEID = source.id");
//        fieldList.add(hashMap.get("companyId"));
//        fieldList.add(hashMap.get("startTime"));
//        fieldList.add(hashMap.get("endTime"));
        //已收总金额
        sb.append(" left join");
        sb.append(" (select client.SOURCEID,SUM(detail.STAYAMOUNT) as client_stay_amout from hm_crm_client_info_1 client,hm_crm_client_detail_1 detail where client.KZID = detail.KZID and  client.classId = 5  and client.companyId = ? and client.CREATETIME BETWEEN ? AND ? ");
        //TODO 条件拼接
        sb.append(" group by client.SOURCEID) as client_success_stay_amount on client_success_stay_amount.SOURCEID = source.id");
//        fieldList.add(hashMap.get("companyId"));
//        fieldList.add(hashMap.get("startTime"));
//        fieldList.add(hashMap.get("endTime"));
        sb.append(" where source.ISSHOW = 1 and (source.TYPEID = 1 or source.TYPEID = 2) and source.COMPANYID = ?");
//        fieldList.add(hashMap.get("companyId"));
        //TODO 条件拼接
        sb.append(" order by id");
        System.out.println(sb.toString());
    }
}
