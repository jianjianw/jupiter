package com.qiein.jupiter.web.remote;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * @Author: shiTao
 */
public interface ApolloRemoteService {

    /**
     * 获取IP
     *
     * @return
     */
    @GET("/open_api/get_ip")
    Call<ResponseBody> getApolloIp();

    /**
     * 从apollo 获取 CRM 地址
     */
    @GET("/open_api/get_crm_url")
    Call<ResponseBody> getCrmUrlByCidFromApollo(@Query("cid") int companyId);

    /**
     * 发送websocket 消息
     *
     * @return
     */
    @POST("/websocket/post_to_company_staff")
    Call<ResponseBody> postWebSocketMsg(@Body RequestBody body);


    @POST
    Call<ResponseBody> getSrcImgListRpc(@Query("sign") String sign,
                                        @Query("time") long time,
                                        @Body RequestBody body);

}
