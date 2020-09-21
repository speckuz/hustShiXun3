package com.example.tokendemo.Authentication;

import com.example.tokendemo.Authentication.TokenAuthenticationFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuthenticationFilterConfiguration {
    @Bean
    public FilterRegistrationBean filterRegistrationBean(){
        FilterRegistrationBean bean=new FilterRegistrationBean();
        bean.setFilter(new TokenAuthenticationFilter());
        bean.addUrlPatterns("/*");
        return bean;
    }
}