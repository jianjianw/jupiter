package com.qiein.jupiter.web.remote;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * 阿波罗远程服务
 *
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

    /**
     * 获取渠道图片列表
     */
    @POST("/img/get_public_image_by_type")
    Call<ResponseBody> getSrcImgListRpc(@Query("sign") String sign,
                                        @Query("time") long time,
                                        @Body RequestBody body);

    /**
     * 短信账号记录
     */
    @GET("/send_msg/msg_template_log")
    Call<ResponseBody> msgTemplateLog(@Query("startTime") String startTime,
                                      @Query("endTime") String endTime,
                                      @Query("companyId") int companyId);

    /**
     * 获取短信发送记录页面
     */
    @GET("/send_msg/find_send_msg")
    Call<ResponseBody> findSendMsg(@Query("startTime") String startTime,
                                   @Query("endTime") String endTime,
                                   @Query("companyId") int companyId,
                                   @Query("phone") String phone,
                                   @Query("type") String type,
                                   @Query("pageNum") int pageNum,
                                   @Query("pageSize") int pageSize);

    /**
     * 获取短信模板
     */
    @GET("/send_msg/find_company_template")
    Call<ResponseBody> getTemplate(@Query("templateType") String templateType,
                                   @Query("companyId") int companyId);

    /**
     * 短信发送
     */
    @POST("/send_msg/send_msg")
    Call<ResponseBody> sendMsg(@Query("sign") String sign,
                               @Body RequestBody body);
}
