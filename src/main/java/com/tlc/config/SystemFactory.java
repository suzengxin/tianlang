package com.tlc.config;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import com.tlc.service.FileService;

@Component
public class SystemFactory implements ApplicationContextAware{
	
    private static Map<String, FileService> systems = new ConcurrentHashMap<>();
	
	public FileService getFileService(String system) throws Exception{
		FileService strategy = systems.get(system);
        if(strategy == null) {
            throw new RuntimeException("No Strategy Defined");
        }
        return strategy;
    }

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		systems = applicationContext.getBeansOfType(FileService.class);
	}
}
