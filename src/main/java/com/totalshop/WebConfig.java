package com.totalshop;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/* @洪伟 文件磁盘图片url 映射
        配置server虚拟路径，handler为前台访问的目录，locations为files相对应的本地路径*/
@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {
	//admin图片路径
//	public static String adminImgPath = "C:\\Users\\l\\Desktop\\image\\admin\\";
	//admin图片路径
	public static String adminImgPath = "C:\\Users\\Administrator\\Desktop\\image\\admin\\";
	//商家二维码服务器路径
    public static String realPath = "C:\\Users\\Administrator\\Desktop\\image\\qrCode\\";
    //商家二维码本机路径
//	public static String realPath = "C:\\Users\\l\\Desktop\\image\\qrCode\\";
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("/image/**").addResourceLocations("file:///C:\\Users\\l\\Desktop\\image\\");
        //服务器 文件磁盘图片url 映射
        registry.addResourceHandler("/image/**").addResourceLocations("file:///C:\\Users\\Administrator\\Desktop\\image\\");
        super.addResourceHandlers(registry);
    }
}