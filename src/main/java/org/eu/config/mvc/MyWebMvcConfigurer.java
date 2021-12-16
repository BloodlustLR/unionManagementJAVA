package org.eu.config.mvc;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MyWebMvcConfigurer implements WebMvcConfigurer {

    /**
     * 虚拟路径配置
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //Windows
        registry.addResourceHandler("/upload/**").addResourceLocations("file:D:/union_management/download/");
        //Linux
//        registry.addResourceHandler("/upload/**").addResourceLocations("file:/data/img/");
    }

}
