package org.eu.config;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SystemConfig implements InitializingBean {

    @Value("${ocr.APP_ID}")
    private String appId;

    @Value("${ocr.API_KEY}")
    private String apiKey;

    @Value("${ocr.SECRET_KEY}")
    private String secretKey;

    @Value("${ocr.language}")
    private String language;

    @Value("${ocr.data-path}")
    private String dataPath;

    public static String APP_ID;
    public static String API_KEY;
    public static String SECRET_KEY;
    public static String LANGUAGE;
    public static String DATA_PATH;
    public static String ACCESS_TOKEN;


    @Override
    public void afterPropertiesSet() throws Exception {
        APP_ID = appId;
        API_KEY = apiKey;
        SECRET_KEY = secretKey;
        LANGUAGE = language;
        DATA_PATH = dataPath;
        System.out.println("读取配置完毕");
    }
}
