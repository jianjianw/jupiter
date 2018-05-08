package com.qiein.jupiter.config;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;

/**
 * 配置json的转换配置
 */
@Configuration
public class JsonConvertConfig extends WebMvcConfigurerAdapter {
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();
        //自定义配置...
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        //fastJsonConfig.setDateFormat("yyyy-MM-dd HH:mm:ss");    // 自定义时间格式
        fastJsonConfig.setSerializerFeatures(
                //配置美观的输出格式
//                SerializerFeature.PrettyFormat,
                //配置输出空值
                SerializerFeature.WriteMapNullValue,
                //将字符串类型字段的空值输出为空字符串 ""
                SerializerFeature.WriteNullStringAsEmpty,
                //使用enum的toString方法序列化
                SerializerFeature.WriteEnumUsingToString
        );
        converter.setFastJsonConfig(fastJsonConfig);
        converters.add(converter);
    }
}
