package com.qiein.jupiter;


import com.alibaba.fastjson.JSONObject;
import com.qiein.jupiter.web.entity.vo.StaffVO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AllTest {


    @Test
    public void test(){
        StaffVO staffVO=new StaffVO();
        staffVO.setPhone("13112345678");
        staffVO.setToken("12321312123");
        System.out.println(JSONObject.toJSONString(staffVO));
    }
}
