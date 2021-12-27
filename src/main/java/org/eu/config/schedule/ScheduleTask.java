package org.eu.config.schedule;


import com.alibaba.fastjson.JSONObject;
import com.baidubce.http.ApiExplorerClient;
import com.baidubce.http.HttpMethodName;
import com.baidubce.model.ApiExplorerRequest;
import com.baidubce.model.ApiExplorerResponse;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.eu.config.SystemConfig;
import org.eu.entity.Kill;
import org.eu.entity.Loss;
import org.eu.service.KillService;
import org.eu.service.LossService;
import org.eu.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;


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


    @Scheduled(cron = "0 0 0 1/1 * ? ")
    public void deleteUselessPic() {

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0);

//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        String start = sdf.format(calendar.getTime());
//        calendar.add(Calendar.DATE,1);
//        String end = sdf.format(calendar.getTime());
//        System.out.println("清理"+start+"至"+end+"无效图片文件");



        QueryWrapper<Loss> lossQueryWrapper = new QueryWrapper<>();
//        lossQueryWrapper.between("create_time",start,end);
        List<Loss> lossList = lossService.list(lossQueryWrapper);

        QueryWrapper<Kill> killQueryWrapper = new QueryWrapper<>();
//        killQueryWrapper.between("create_time",start,end);
        List<Kill> killList = killService.list(killQueryWrapper);


        List<String> picFileNameList= new ArrayList<>();
        for(Loss loss:lossList){
            String img = loss.getImg();
            String[] tempStr = img.split("/");
            String imgFileName = tempStr[tempStr.length-1];
            picFileNameList.add(imgFileName);
        }


        List<String> killImgList = new ArrayList<>();
        for(Kill kill:killList){
            String img = kill.getImg();
            String[] tempStr = img.split("/");
            String imgFileName = tempStr[tempStr.length-1];
            killImgList.add(imgFileName);
            picFileNameList.add(imgFileName);
        }

        File imgFolder = new File(SystemConfig.SAVE_PATH);
        File[] files = imgFolder.listFiles();
        Integer num = 0;
        for(File file :files){
//            System.out.println("目录下文件:"+file.getName());
            if(!picFileNameList.contains(file.getName())){
                FileUtil.deleteTempFile(file.getAbsolutePath());
                num++;
            }
        }

        System.out.println("清理"+num+"张无效图片文件");

    }

}
