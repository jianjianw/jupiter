package com.qiein.jupiter.web;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.auth.InstanceProfileCredentialsProvider;
import com.aliyuncs.ccc.model.v20170705.StartBack2BackCallRequest;
import com.aliyuncs.ccc.model.v20170705.StartBack2BackCallResponse;
import com.aliyuncs.profile.DefaultProfile;

import java.util.HashMap;
import java.util.Map;

/**
 * FileName: CallTest
 *
 * @author: yyx
 * @Date: 2018-8-1 10:35
 */
public class CallTest {
    public static void main(String [] args){
        // 创建DefaultAcsClient实例并初始化
        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou");
        InstanceProfileCredentialsProvider provider = new InstanceProfileCredentialsProvider(
                "yiyanxu@hanmu.onaliyun.com"
        );
        DefaultAcsClient client = new DefaultAcsClient(profile, provider);


        StartBack2BackCallRequest request1 = new StartBack2BackCallRequest();
        request1.setCaller("15990187374");
        request1.setCallee("13397117667");
        request1.setInstanceId("d36fcfd0-bca5-46db-9efc-1947996f1cc8");
        request1.setCallCenterNumber("057128284903");
        StartBack2BackCallResponse response ;
        try{
            response = client.getAcsResponse(request1);
            String taskId = response.getTaskId();
            System.out.println(taskId);
        }catch (Exception e){
            e.printStackTrace();
        }

    }


}


//        DescribeRegionsRequest request = new DescribeRegionsRequest();
//        DescribeRegionsResponse response ;
//        try {
//            response = client.getAcsResponse(request);
//            for (DescribeRegionsResponse.Region region:response.getRegions()) {
//                System.out.println(JSONObject.toJSONString(region));
//            }
//        } catch (ServerException e) {
//            e.printStackTrace();
//        } catch (ClientException e) {
//            e.printStackTrace();
//        }

