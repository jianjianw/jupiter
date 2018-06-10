package com.qiein.jupiter.msg.goeasy;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by Tt(叶华葳)
 * on 2018/5/15 0015.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class GoEasyUtilTest {
    @Test
    public void setCCBIPADDRESS() throws Exception {
    	//int companyId, int staffId, int kzNum, String kzId, String logId,int overTime) {
		GoEasyUtil.pushAppInfoReceive(1,12,1,"4fe3728b71ef747e815b95379c528e6e","1",1528270490);
	}

    @Test
	public void destroy() throws Exception {

	}

	@Test
	public void pushAppInfoReceive() throws Exception {
	}

}