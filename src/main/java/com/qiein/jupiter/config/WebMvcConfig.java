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
                .excludePathPatterns("/outer/**")
                //排除apollo
                .excludePathPatterns("/apollo")
                //login
                .excludePathPatterns("/login/get_company_list_by_phone")
                .excludePathPatterns("/login/login_by_phone")
                .excludePathPatterns("/login/get_company_list_by_wechat")
                .excludePathPatterns("/login/login_by_wechat")
                .excludePathPatterns("/login/get_company_list_by_ding")
                .excludePathPatterns("/login/login_by_ding")
                .excludePathPatterns("/login/verify_code")
                .excludePathPatterns("/login/need_verity_code")
                .excludePathPatterns("/login/check_token");
    }

}
