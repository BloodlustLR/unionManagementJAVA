package org.eu.config;


import com.alibaba.fastjson.JSONObject;
import com.baidubce.http.ApiExplorerClient;
import com.baidubce.http.HttpMethodName;
import com.baidubce.model.ApiExplorerRequest;
import com.baidubce.model.ApiExplorerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class InitRunner implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        System.out.println("开始运行初始化逻辑");
        Map<String,String> accessTokenMap = requestAccessToken();
        SystemConfig.ACCESS_TOKEN = accessTokenMap.get("access_token");
        System.out.println("获取到ACCESS_TOKEN-"+SystemConfig.ACCESS_TOKEN);
    }

    public Map<String,String> requestAccessToken(){
        Map<String,String> resultMap = new HashMap<>();

        String path = "https://aip.baidubce.com/oauth/2.0/token";
        ApiExplorerRequest request = new ApiExplorerRequest(HttpMethodName.POST, path);

        // 设置header参数
        request.addHeaderParameter("Content-Type", "application/json;charset=UTF-8");

        // 设置query参数
        request.addQueryParameter("client_id", SystemConfig.API_KEY);
        request.addQueryParameter("client_secret", SystemConfig.SECRET_KEY);
        request.addQueryParameter("grant_type", "client_credentials");

        ApiExplorerClient client = new ApiExplorerClient();

        try {
            ApiExplorerResponse response = client.sendRequest(request);
            // 返回结果格式为Json字符串
            JSONObject resultStrj = JSONObject.parseObject(response.getResult());
            for(String key:resultStrj.keySet()){
                resultMap.put(key,resultStrj.getString(key));
            }
            System.out.println(response.getResult());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMap;
    }
}
