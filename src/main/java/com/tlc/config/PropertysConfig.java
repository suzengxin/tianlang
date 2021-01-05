package com.tlc.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PropertysConfig {
    //用户路径
    public static String PATH_HOME;
    //部署系统
    public static String SYSTEM;
    //音乐文件路径
    public static String SYSTEM_MUSIC_PATH;
    //书籍文件路径
    public static String SYSTEM_BOOK_PATH;
    
    @Value("${path.home}")
    public void setPathHome(String value) {
    	PropertysConfig.PATH_HOME = value;
    }
    
    @Value("${system}")
    public void setSystem(String value) {
    	PropertysConfig.SYSTEM = value;
    }
    
    @Value("${system.music.path}")
    public void setSystemMusicPath(String value) {
    	PropertysConfig.SYSTEM_MUSIC_PATH = value;
    }
    
    @Value("${system.book.path}")
    public void setSystemBookPath(String value) {
    	PropertysConfig.SYSTEM_BOOK_PATH = value;
    }
}
