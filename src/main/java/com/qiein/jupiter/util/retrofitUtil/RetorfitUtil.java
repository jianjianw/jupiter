package com.qiein.jupiter.util.retrofitUtil;

import com.alibaba.fastjson.JSONObject;
import com.qiein.jupiter.exception.ExceptionEnum;
import com.qiein.jupiter.exception.RException;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit2.*;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

/**
 * retrofit JSON 转换
 *
 * @Author: shiTao
 */
public class RetorfitUtil {

    private static final Logger log = LoggerFactory.getLogger(RetorfitUtil.class);

    /**
     * 自定义Converter实现RequestBody到JSON的转换
     */
    public static class JSONConverter implements retrofit2.Converter<ResponseBody, JSONObject> {

        public static final JSONConverter INSTANCE = new JSONConverter();

        @Override
        public JSONObject convert(ResponseBody value) throws IOException {
            return JSONObject.parseObject(value.string());
        }
    }

    /**
     * 用于向Retrofit提供StringConverter
     */
    public static class JSONConverterFactory extends retrofit2.Converter.Factory {

        private static final JSONConverterFactory INSTANCE = new JSONConverterFactory();

        public static JSONConverterFactory create() {
            return INSTANCE;
        }

        // 我们只关实现从ResponseBody 到 JSON 的转换，所以其它方法可不覆盖
        @Override
        public retrofit2.Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
            if (type == String.class) {
                return JSONConverter.INSTANCE;
            }
            //其它类型我们不处理，返回null就行
            return null;
        }
    }


    /**
     * 执行方法同时返回
     *
     * @return
     */
    public static String request(Call<ResponseBody> responseBodyCall) {
        String bodyStr = "";
        try {
            ResponseBody body = responseBodyCall.execute().body();
            if (body == null) {
                throw new RException(ExceptionEnum.REMOTE_CALL_ERROR);
            }
            bodyStr = body.string();
            log.info("http-msg-sync", bodyStr);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bodyStr;
    }

    /**
     * 异步请求
     *
     * @param responseBodyCall
     */
    public static void requestSync(Call<ResponseBody> responseBodyCall) {
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                log.info("http-msg-async", response.isSuccessful());
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                log.info("http-error", throwable.getMessage());
            }
        });
    }

    /**
     * 创建json 类型的Body
     *
     * @return
     */
    public static RequestBody createJSONBody(String json) {
        return RequestBody.create(okhttp3.MediaType.parse("application/json;charset=UTF-8"), json);
    }

}
