package com.qiein.jupiter.config;


import com.qiein.jupiter.aop.interceptor.TokenInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * 拦截器
 */
@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {

    @Autowired
    private TokenInterceptor tokenInterceptor;

    public void addInterceptors(InterceptorRegistry registry) {
        //注册自定义拦截器，添加拦截路径和排除拦截路径
        registry.addInterceptor(tokenInterceptor)
                //要拦截的url
                .addPathPatterns("/**")
                //要排除的url
                .excludePathPatterns("/")
                .excludePathPatterns("/staff/get_company_list")
                .excludePathPatterns("/staff/login_with_company_id")
                .excludePathPatterns("/staff/verify_code")
                .excludePathPatterns("/staff/need_verity_code")
                .excludePathPatterns("/outer/**");
    }

}
