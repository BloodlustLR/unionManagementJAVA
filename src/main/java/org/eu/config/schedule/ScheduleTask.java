package org.eu.config.schedule;


import com.alibaba.fastjson.JSONObject;
import com.baidubce.http.ApiExplorerClient;
import com.baidubce.http.HttpMethodName;
import com.baidubce.model.ApiExplorerRequest;
import com.baidubce.model.ApiExplorerResponse;
import org.eu.config.SystemConfig;
import org.eu.service.KillService;
import org.eu.service.LossService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Configuration
public class ScheduleTask {

    @Autowired
    LossService lossService;

    @Autowired
    KillService killService;

    @Scheduled(cron = "0 0/30 * * * ? ")
    public void requestAccessTokenSchedule() {
        Map<String,String> accessTokenMap = requestAccessToken();
        SystemConfig.ACCESS_TOKEN = accessTokenMap.get("access_token");
        System.out.println("刷新ACCESS_TOKEN-"+SystemConfig.ACCESS_TOKEN);
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


//    @Scheduled(cron = "0 0/1 * * * ? ")
//    public void deleteUselessPic() {
//        System.out.println("清理无效图片文件");
//
//    }

}
