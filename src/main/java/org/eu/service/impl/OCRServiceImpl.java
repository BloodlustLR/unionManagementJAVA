package org.eu.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baidu.aip.ocr.AipOcr;
import org.apache.tomcat.util.buf.StringUtils;
import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.leptonica.PIX;
import org.bytedeco.tesseract.TessBaseAPI;
import org.eu.config.SystemConfig;
import org.eu.service.OCRService;
import org.eu.util.FileUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.baidubce.http.ApiExplorerClient;
import com.baidubce.http.HttpMethodName;
import com.baidubce.model.ApiExplorerRequest;
import com.baidubce.model.ApiExplorerResponse;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;

import static org.bytedeco.leptonica.global.lept.pixDestroy;
import static org.bytedeco.leptonica.global.lept.pixRead;

@Service
public class OCRServiceImpl implements OCRService {

    @Override
    public Map<String,String> detectPic(MultipartFile multipartFile) throws IOException {

        String base64Url = FileUtil.multipartFileToBase64WithoutHeader(multipartFile);
        String urlEncoded = URLEncoder.encode(base64Url, "utf-8");
        String ocrData = BaiduOCRApi(SystemConfig.ACCESS_TOKEN,urlEncoded);

        Map<String,String> resultMap = new HashMap<>();
        resultMap.put("img",FileUtil.multipartFileToBase64(multipartFile));


        //获取最后一个.的位置
//        int lastIndexOf = multipartFile.getOriginalFilename().lastIndexOf(".");
        //获取文件的后缀名 .jpg
//        String suffix = multipartFile.getOriginalFilename().substring(lastIndexOf);
//
//        String fileName = Calendar.getInstance().getTimeInMillis()+suffix;
//
//        FileUtil.inputStreamToFile(multipartFile.getInputStream(),new File(fileName));
//        String ocrData = BaiduOCRSDK(fileName);
//        analysisData(ocrData);
//        FileUtil.deleteTempFile(fileName);
        if(ocrData!=null){
            resultMap.putAll(analysisData(ocrData));
        }

        return resultMap;

    }

    private String tesseractOCR(String filePath){

        BytePointer outText;

        TessBaseAPI api = new TessBaseAPI();


        if (api.Init(SystemConfig.APP_ID, SystemConfig.LANGUAGE) != 0) {
            System.err.println("无法初始化tesseract");
            return null;
        }

        // Open input image with leptonica library
        PIX image = pixRead(filePath);
        api.SetImage(image);
        // Get OCR result
        outText = api.GetUTF8Text();
        try {
            System.out.println(outText.getString());
            return outText.getString();
        }finally{
            // Destroy used object and release memory
            api.End();
            outText.deallocate();
            pixDestroy(image);

        }
    }


    private String BaiduOCRApi(String accessToken, String base64Pic){

        String path = "https://aip.baidubce.com/rest/2.0/ocr/v1/accurate_basic";
        ApiExplorerRequest request = new ApiExplorerRequest(HttpMethodName.POST, path);


        // 设置header参数
        request.addHeaderParameter("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");

        // 设置query参数
        request.addQueryParameter("access_token", accessToken);


        // 设置jsonBody参数
        String jsonBody = "image="+base64Pic;
        request.setJsonBody(jsonBody);

        ApiExplorerClient client = new ApiExplorerClient();

        String result = null;
        try {
            ApiExplorerResponse response = client.sendRequest(request);
            // 返回结果格式为Json字符串
            result = response.getResult();
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public String BaiduOCRSDK(String filePath) {

        // 初始化一个AipOcr
        AipOcr client = new AipOcr(SystemConfig.APP_ID, SystemConfig.API_KEY, SystemConfig.SECRET_KEY);

        // 可选：设置网络连接参数
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);

        // 调用接口
        String path = filePath;

        org.json.JSONObject res = client.basicGeneral(path, new HashMap<String, String>());
        System.out.println(res.toString(2));

        return res.toString(2);

    }

    public Map<String,String> analysisData(String ocrData){
        Map<String,String> resultMap = new HashMap<>();

        JSONObject ocrStrj = JSONObject.parseObject(ocrData);

        List<String> wordResultList = JSONObject.parseArray(ocrStrj.getString("words_result"),String.class);

        Integer hasShipName=0;
        Integer attackerLocation=0;

        List<String> wordList = new ArrayList<>();
        for(String wordResult:wordResultList){
            JSONObject wordResultStrj = JSONObject.parseObject(wordResult);
            String word = wordResultStrj.getString("words");
            wordList.add(word);
        }

        String reportId = "";//报告ID
        String reportTime = "";//报告日期
        String shipName = "";//舰船名
        String money = "";//金额
        String area = "";//地区
        String constellation = "";//星域
        String galaxy = "";//星座
        String characterName = "";//角色名称
        String kmShip = "";//KM舰船
        String highATKShip = "";//最高伤舰船
        for(int i = 0;i<wordList.size();i++){

            if(wordList.get(i).indexOf("击毁报告") != -1 || wordList.get(i).indexOf("损失报告") != -1){
                reportId = wordList.get(i);
                if(reportId.length()<10){
                    reportId +=  wordList.get(i+1);
                }
                reportId = joinString(reportId.split("三/"));
                reportId = joinString(reportId.split("损失报告"));
                reportId = joinString(reportId.split("击毁报告"));
                reportId = joinString(reportId.split("\\["));
                reportId = joinString(reportId.split("\\]"));
                reportId = joinString(reportId.split("回"));
                reportId = joinString(reportId.split("10:"));
                reportId = joinString(reportId.split("10："));
                reportId = joinString(reportId.split("0:"));
                reportId = joinString(reportId.split("0："));
            }

            if(wordList.get(i).indexOf("UTC") != -1){
                reportTime = wordList.get(i);
                if(reportTime.length()<10){
                    reportTime +=  wordList.get(i+1);
                }
                reportTime = joinString(reportTime.split("UTC\\+8"));
                reportTime = reportTime.replaceAll("/","-");
                reportTime = reportTime.substring(0,10)+" "+reportTime.substring(10);

            }

            if(wordList.get(i).indexOf("舰") != -1 &&  hasShipName==0)
            {
                shipName = wordList.get(i);
                shipName = joinString(shipName.split("护卫舰"));
                shipName = joinString(shipName.split("驱逐舰"));
                shipName = joinString(shipName.split("战列巡洋舰"));
                shipName = joinString(shipName.split("巡洋舰"));
                shipName = joinString(shipName.split("战列舰"));
                shipName = joinString(shipName.split("旗舰"));

                hasShipName = 1;
            }

            if(wordList.get(i).indexOf("星币") != -1)
            {
                money = wordList.get(i);
                money = joinString(money.split("星币"));
                money = joinString(money.split(","));

                hasShipName = 1;
            }

            if(wordList.get(i).indexOf("<") != -1 )
            {
                String[] places = wordList.get(i).split("<");
                area = places[2];
                constellation = places[1];
                galaxy = places[0];

            }

            for(int j=0;j<5;j++)
            {
                if(wordList.get(j).indexOf("[") != -1 && wordList.get(j).indexOf("]") != -1)
                {
                    if(wordList.get(j).indexOf("损失报告") == -1 || wordList.get(j).indexOf("击毁报告") == -1 ){
                        Integer lastIndex = wordList.get(j).indexOf("]");
                        characterName = wordList.get(j).substring(lastIndex+1);
                    }
                }
            }

            if(wordList.get(i).indexOf("最后一击") != -1 )
            {
                attackerLocation = i;
                for(int j=attackerLocation-3;j<attackerLocation+1;j++)
                {
                    if(wordList.get(j).indexOf("级") != -1 && wordList.get(j).indexOf("旗舰") == -1)
                    {
                        kmShip = wordList.get(j);
                    }
                }
            }

            if(wordList.get(i).indexOf("造成伤害最多") != -1 )
            {
                attackerLocation = i;
                for(int j=attackerLocation-8;j<attackerLocation+1;j++)
                {
                    if(wordList.get(j).indexOf("级") != -1 && wordList.get(j).indexOf("旗舰") == -1)
                    {
                        highATKShip=wordList.get(j);
                    }
                }
            }

        }

        System.out.println("报告编号- " + reportId);
        System.out.println("报告时间- " + reportTime);
        System.out.println("舰船名称- " + shipName);
        System.out.println("金额- " + money);
        System.out.println("地区- " + area);
        System.out.println("星域- " + constellation);
        System.out.println("星系- " + galaxy);
        System.out.println("角色名称- " + characterName);
        System.out.println("KM舰船- " + kmShip);
        System.out.println("最高伤害舰船- " + highATKShip);

        resultMap.put("info","success");
        resultMap.put("reportId",reportId);
        resultMap.put("reportTime",reportTime);
        resultMap.put("shipName",shipName);
        resultMap.put("money",money);
        resultMap.put("area",area);
        resultMap.put("constellation",constellation);
        resultMap.put("galaxy",galaxy);
        resultMap.put("characterName",characterName);
        resultMap.put("kmShip",kmShip);
        resultMap.put("highATKShip",highATKShip);

        return resultMap;

    }

    private String joinString(String[] strArr){
        StringBuilder sb = new StringBuilder();

        for(String str:strArr){
            sb.append(str);
        }

        return sb.toString();
    }


}
