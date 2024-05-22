package com.yohanii.lostandfound.config;

import com.yohanii.lostandfound.component.login.argumentresolver.LoginMemberArgumentResolver;
import com.yohanii.lostandfound.component.admin.interceptor.AdminInterceptor;
import com.yohanii.lostandfound.component.login.interceptor.LoginCheckInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginMemberArgumentResolver());
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginCheckInterceptor())
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns("/", "/members", "/members/add-form", "/login", "/logout", "/css/**", "*.ico", "/error", "/posts/lost", "/posts/found");

        registry.addInterceptor(new AdminInterceptor())
                .order(2)
                .addPathPatterns("/admin", "/admin/*");
    }
}
