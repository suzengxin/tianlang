package com.tlc;

import javax.servlet.MultipartConfigElement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.util.unit.DataSize;

//@ServletComponentScan
@SpringBootApplication
public class TianlangApplication extends SpringBootServletInitializer{

	public static void main(String[] args) {
		SpringApplication.run(TianlangApplication.class, args);
	}
	
	//war包打包时使用
//	@Override
//  protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
//      return builder.sources(TianlangApplication.class);
//  }

	@Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();  
        //单个文件最大50M
        factory.setMaxFileSize(DataSize.ofMegabytes(100L));
        //设置总上传数据总大小50M
        factory.setMaxRequestSize(DataSize.ofMegabytes(100L));
        //factory.setLocation("/data/tmp");
        return factory.createMultipartConfig();
    }
}
