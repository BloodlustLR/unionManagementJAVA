package org.eu.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baidu.aip.ocr.AipOcr;
import org.apache.http.client.config.RequestConfig;
import org.eu.config.SystemConfig;
import org.eu.mapper.ArmyMapper;
import org.eu.service.OCRService;
import org.eu.util.FileUtil;
import org.eu.util.HttpClientUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.baidubce.http.ApiExplorerClient;
import com.baidubce.http.HttpMethodName;
import com.baidubce.model.ApiExplorerRequest;
import com.baidubce.model.ApiExplorerResponse;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URLEncoder;
import java.util.*;
import java.util.List;

@Service
public class OCRServiceImpl implements OCRService {



    @Autowired
    ArmyMapper armyMapper;

    @Override
    public Map<String,Object> detectPic(MultipartFile multipartFile,String mode) throws IOException {

        Map<String,Object> resultMap = new HashMap<>();

        //获取最后一个.的位置
        int lastIndexOf = multipartFile.getOriginalFilename().lastIndexOf(".");
        //获取文件的后缀名 .jpg
        String suffix = multipartFile.getOriginalFilename().substring(lastIndexOf);

        String fileName = Calendar.getInstance().getTimeInMillis()+suffix;
        String filePath = SystemConfig.SAVE_PATH+File.separator+fileName;

        System.out.println("源文件-"+filePath);
        FileUtil.inputStreamToFile(multipartFile.getInputStream(),new File(filePath));

        System.out.println("转换JPG");

        //read image file
        BufferedImage bufferedImage = ImageIO.read(new File(filePath));

        // create a blank, RGB, same width and height, and a white background
        BufferedImage newBufferedImage = new BufferedImage(bufferedImage.getWidth(),
                bufferedImage.getHeight(), BufferedImage.TYPE_INT_RGB);

        //TYPE_INT_RGB:创建一个RBG图像，24位深度，成功将32位图转化成24位
        newBufferedImage.createGraphics().drawImage(bufferedImage, 0, 0, Color.WHITE, null);

        // write to jpeg file
        String jpgName = Calendar.getInstance().getTimeInMillis()+".jpg";
        String jpgPath = SystemConfig.SAVE_PATH+File.separator+jpgName;
        System.out.println("转换文件-"+jpgPath);
        ImageIO.write(newBufferedImage, "jpg", new File(jpgPath));
        FileUtil.deleteTempFile(filePath);
        String fileUrl = SystemConfig.HOST+"/api/upload/"+jpgName;

        String ocrData = BaiduOCRWebApi(SystemConfig.ACCESS_TOKEN,fileUrl);
//        File jpgFile = new File(jpgPath);
//        byte[] bytes = FileUtil.fileConvertToByteArray(jpgFile);
//        BASE64Encoder base64Encoder =new BASE64Encoder();
//        String base64EncoderImg = base64Encoder.encode(bytes);
//        base64EncoderImg = base64EncoderImg.replaceAll("[\\s*\t\n\r]", "");
//        String urlEncoded = URLEncoder.encode(base64EncoderImg, "utf-8");
//        String ocrData = BaiduOCRApi(SystemConfig.ACCESS_TOKEN,base64EncoderImg);

        resultMap.put("img","/api/upload/"+jpgName);

        if(ocrData!=null){
            switch(mode) {
                case "loss":
                    resultMap.putAll(analysisLossData(ocrData));
                    break;
                case "kill":
                    resultMap.putAll(analysisKillData(ocrData));
                    break;
            }
        }

        return resultMap;

    }


//    private String tesseractOCR(String filePath){
//
//        BytePointer outText;
//
//        TessBaseAPI api = new TessBaseAPI();
//
//
//        if (api.Init(SystemConfig.APP_ID, SystemConfig.LANGUAGE) != 0) {
//            System.err.println("无法初始化tesseract");
//            return null;
//        }
//
//        // Open input image with leptonica library
//        PIX image = pixRead(filePath);
//        api.SetImage(image);
//        // Get OCR result
//        outText = api.GetUTF8Text();
//        try {
//            System.out.println(outText.getString());
//            return outText.getString();
//        }finally{
//            // Destroy used object and release memory
//            api.End();
//            outText.deallocate();
//            pixDestroy(image);
//
//        }
//    }

    private String BaiduOCRApi(String accessToken, String base64Pic) throws UnsupportedEncodingException {

//        String path = "https://aip.baidubce.com/rest/2.0/ocr/v1/accurate_basic";
//        ApiExplorerRequest request = new ApiExplorerRequest(HttpMethodName.POST, path);
//
//
//        // 设置header参数
//        request.addHeaderParameter("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
//
//        // 设置query参数
//        request.addQueryParameter("access_token", accessToken);
//
//
//        // 设置jsonBody参数
//        String jsonBody = "image="+base64Pic;
//        request.setJsonBody(jsonBody);
//
//        ApiExplorerClient client = new ApiExplorerClient();
//
//        String result = null;
//        try {
//            ApiExplorerResponse response = client.sendRequest(request);
//            // 返回结果格式为Json字符串
//            result = response.getResult();
//            System.out.println("Res:"+result);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        String path = "https://aip.baidubce.com/rest/2.0/ocr/v1/general_basic?access_token="+accessToken;
        String path = "https://aip.baidubce.com/rest/2.0/ocr/v1/accurate_basic?access_token="+accessToken;

        Map<String,String> bodyMap = new HashMap<>();
        bodyMap.put("image",base64Pic);

        String result = HttpClientUtils.postUrlencoded(path,bodyMap);
        System.out.println("Res:"+result);
        return result;
    }


    private String BaiduOCRWebApi(String accessToken, String picUrl) throws UnsupportedEncodingException {

//        String path = "https://aip.baidubce.com/rest/2.0/ocr/v1/accurate_basic";
//        ApiExplorerRequest request = new ApiExplorerRequest(HttpMethodName.POST, path);
//
//        // 设置header参数
//        request.addHeaderParameter("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
//
//        // 设置query参数
//        request.addQueryParameter("access_token", accessToken);
//
//
//        // 设置jsonBody参数
//        String jsonBody = "url="+picUrl+"&language_type=CHN_ENG&detect_direction=false&paragraph=false&probability=false";
//        request.setJsonBody(jsonBody);
//
//        ApiExplorerClient client = new ApiExplorerClient();
//
//        String result = null;
//        try {
//            ApiExplorerResponse response = client.sendRequest(request);
//            // 返回结果格式为Json字符串
//            result = response.getResult();
//            System.out.println("Res:"+result);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        String path = "https://aip.baidubce.com/rest/2.0/ocr/v1/general_basic?access_token="+accessToken;
        String path = "https://aip.baidubce.com/rest/2.0/ocr/v1/accurate_basic?access_token="+accessToken;

        Map<String,String> bodyMap = new HashMap<>();
        System.out.println("url-"+picUrl);
        bodyMap.put("url",picUrl);
        bodyMap.put("language_type","CHN_ENG");
        bodyMap.put("detect_direction","false");
        bodyMap.put("paragraph","false");
        bodyMap.put("probability","false");

        String result = HttpClientUtils.postUrlencoded(path,bodyMap);
        System.out.println("Res:"+result);
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


//    损失识别

    public Map<String,Object> analysisLossData(String ocrData){
        Map<String,Object> resultMap = new HashMap<>();

        JSONObject ocrStrj = JSONObject.parseObject(ocrData);

        if(!ocrStrj.containsKey("words_result")){
            return resultMap;
        }

        List<String> wordResultList = JSONObject.parseArray(ocrStrj.getString("words_result"),String.class);
        List<String> wordList = new ArrayList<>();
        for(String wordResult:wordResultList){
            JSONObject wordResultStrj = JSONObject.parseObject(wordResult);
            String word = wordResultStrj.getString("words");
            wordList.add(word);
        }

        String analysisMode = null;

        for(int i = 0;i<wordList.size();i++) {
            if(wordList.get(i).indexOf("击毁报告") != -1 || wordList.get(i).indexOf("损失报告") != -1){
                analysisMode = "single";
                break;
            }
            if(wordList.get(i).indexOf("军团战斗记录") != -1){
                analysisMode = "multiple";
                break;
            }
        }

        if(analysisMode=="single"){
            resultMap.putAll(analysisSingleLossData(wordList));
        }else if(analysisMode=="multiple"){
            resultMap = analysisMultipleLossData(wordList);
        }

        return resultMap;
    }

    public Map<String,String> analysisSingleLossData(List<String> wordList){
        Map<String,String> resultMap = new HashMap<>();

        List<String> armyShortNameList = armyMapper.listArmyShorName();

        Integer hasShipName=0;
        Integer hasCharacterName=0;
        Integer hasKMInfo=0;
        Integer attackerLocation=0;

//        String reportId = "";//报告ID
        String reportTime = "";//报告日期
        String shipName = "";//舰船名
        String money = "";//金额
        String area = "";//地区
        String constellation = "";//星域
        String galaxy = "";//星座
        String armyShortName = "";//军团简称
        String characterName = "";//角色名称
        String kmShip = "";//KM舰船
        String kmArmyShortName = "";//KM军团
        String kmGameId = "";//击杀者游戏名
        String highATKShip = "";//最高伤舰船
        for(int i = 0;i<wordList.size();i++){

//            if(wordList.get(i).indexOf("击毁报告") != -1 || wordList.get(i).indexOf("损失报告") != -1){
//                reportId = wordList.get(i);
//                if(reportId.length()<10){
//                    reportId +=  wordList.get(i+1);
//                }
//                reportId = joinString(reportId.split("三/"));
//                reportId = joinString(reportId.split("损失报告"));
//                reportId = joinString(reportId.split("击毁报告"));
//                reportId = joinString(reportId.split("\\["));
//                reportId = joinString(reportId.split("\\]"));
//                reportId = joinString(reportId.split("回"));
//                reportId = joinString(reportId.split("司"));
//                reportId = joinString(reportId.split("10:"));
//                reportId = joinString(reportId.split("10："));
//                reportId = joinString(reportId.split("0:"));
//                reportId = joinString(reportId.split("0："));
//                reportId = joinString(reportId.split("日"));
//                reportId = joinString(reportId.split("间"));
//                reportId = joinString(reportId.split(":"));
//                reportId = joinString(reportId.split("："));
//                continue;
//            }

            if(wordList.get(i).indexOf("UTC") != -1||wordList.get(i).indexOf("UT0") != -1||wordList.get(i).indexOf("UTO") != -1){
                reportTime = wordList.get(i);
                if(reportTime.length()<=10){
                    reportTime +=  wordList.get(i+1);
                }
                reportTime = joinString(reportTime.split("UTC\\+8"));
                reportTime = joinString(reportTime.split("UT0\\+8"));
                reportTime = joinString(reportTime.split("UTO\\+8"));
                reportTime = reportTime.replaceAll("/","-");

                String time = reportTime.substring(10);
                time = joinString(time.split(":"));
                if(time.length()<6){
                    int rest = 6 - time.length();
                    for(int t=0;t<rest;t++){
                        time+="0";
                    }
                }
                reportTime = reportTime.substring(0,10)+" "+time.substring(0,2)+":"+time.substring(2,4)+":"+time.substring(4,6);
                continue;
            }

            if((wordList.get(i).indexOf("号") != -1||wordList.get(i).indexOf("级") != -1)&&wordList.get(i).indexOf("舰") != -1 &&  hasShipName==0)
            {
                shipName = wordList.get(i);
                shipName = joinString(shipName.split("护卫舰"));
                shipName = joinString(shipName.split("驱逐舰"));
                shipName = joinString(shipName.split("战列巡洋舰"));
                shipName = joinString(shipName.split("巡洋舰"));
                shipName = joinString(shipName.split("战列舰"));
                shipName = joinString(shipName.split("旗舰"));
                shipName = joinString(shipName.split("工业舰"));

                hasShipName = 1;
                continue;
            }

            if(wordList.get(i).indexOf("星币") != -1||wordList.get(i).indexOf("星市") != -1)
            {
                money = wordList.get(i);
                money = joinString(money.split("星币"));
                money = joinString(money.split("星市"));
                money = joinString(money.split(","));
                money = joinString(money.split("\\."));
                money = joinString(money.split("\\]"));
                continue;
            }

            if(wordList.get(i).indexOf("<") != -1 )
            {
                String[] places = wordList.get(i).split("<");
                if(places.length>2){
                    area = places[2];
                }
                if(places.length>1){
                    constellation = places[1];
                }
                if(places.length>0){
                    galaxy = places[0];
                }
                continue;
            }

//            for(int j=0;j<6;j++)
//            {
//                if(hasCharacterName == 0&&wordList.get(j).indexOf("[") != -1 && wordList.get(j).indexOf("]") != -1)
//                {
//                    if(wordList.get(j).indexOf("损失报告") == -1 && wordList.get(j).indexOf("击毁报告") == -1 ){
//                        Integer firstIndex = wordList.get(j).indexOf("[");
//                        Integer lastIndex = wordList.get(j).indexOf("]");
//                        armyShortName = wordList.get(j).substring(firstIndex+1,lastIndex);
//                        characterName = wordList.get(j).substring(lastIndex+1);
//                        hasCharacterName = 1;
//                    }
//                }
//            }

            if(i<12){
                if(hasCharacterName == 0&&(wordList.get(i).indexOf("[") != -1 || wordList.get(i).indexOf("]") != -1))
                {
                    if(wordList.get(i).indexOf("损失报告") == -1 && wordList.get(i).indexOf("击毁报告") == -1 ){
                        String armyShorStr = joinString(wordList.get(i).split("\\["));
                        armyShorStr = joinString(armyShorStr.split("\\]"));
                        for(String shortName :armyShortNameList){
                            if(armyShorStr.indexOf(shortName)>-1&&armyShortName.length()<shortName.length()){
                                armyShortName = shortName;
                                characterName = joinString(armyShorStr.split(shortName));
                                hasCharacterName = 1;
                            }
                        }
                    }
                }
            }

            if(wordList.get(i).indexOf("其他") != -1 ){
//                if(wordList.get(i+1).indexOf("[") != -1 && wordList.get(i+1).indexOf("]") != -1)
//                {
//                    Integer firstIndex = wordList.get(i+1).indexOf("[");
//                    Integer lastIndex = wordList.get(i+1).indexOf("]");
//                    kmArmyShortName = wordList.get(i+1).substring(firstIndex+1,lastIndex);
//                    kmGameId = wordList.get(i+1).substring(lastIndex+1);
//                    hasKMInfo = 1;
//                }

                if(hasKMInfo==0){
                    String armyShorStr = joinString(wordList.get(i+1).split("\\["));
                    armyShorStr = joinString(armyShorStr.split("\\]"));
                    for(String shortName :armyShortNameList){
                        if(armyShorStr.indexOf(shortName)>-1){
                            kmArmyShortName = shortName;
                            kmGameId = joinString(armyShorStr.split(shortName));
                            hasKMInfo = 1;
                        }
                    }
                }
            }


            if(wordList.get(i).indexOf("最后一击") != -1 )
            {
                attackerLocation = i;
                for(int j=attackerLocation-5;j<attackerLocation+1;j++)
                {
                    if((wordList.get(j).indexOf("号") != -1||wordList.get(j).indexOf("级") != -1) && wordList.get(j).indexOf("旗舰") == -1)
                    {
                        kmShip = wordList.get(j);
                    }
                }
//                if(hasKMInfo!=1){
//                    for(int j=attackerLocation-12;j<attackerLocation+1;j++)
//                    {
//                        if(wordList.get(j).indexOf("[") != -1 && wordList.get(j).indexOf("]") != -1)
//                        {
//                            Integer firstIndex = wordList.get(j).indexOf("[");
//                            Integer lastIndex = wordList.get(j).indexOf("]");
//                            kmArmyShortName = wordList.get(j).substring(firstIndex+1,lastIndex);
//                            kmGameId = wordList.get(j).substring(lastIndex+1);
//                            hasKMInfo = 1;
//                        }
//                    }
//                }
                if(hasKMInfo!=1){
                    for(int j=attackerLocation-12;j<attackerLocation+1;j++)
                    {
                        if(wordList.get(j).indexOf("[") != -1 || wordList.get(j).indexOf("]") != -1)
                        {
                            String armyShorStr = joinString(wordList.get(j).split("\\["));
                            armyShorStr = joinString(armyShorStr.split("\\]"));
                            for(String shortName :armyShortNameList){
                                if(armyShorStr.indexOf(shortName)>-1&&kmArmyShortName.length()<shortName.length()){
                                    kmArmyShortName = shortName;
                                    kmGameId = joinString(armyShorStr.split(shortName));
                                    hasKMInfo = 1;
                                }
                            }
                        }
                    }
                }
            }

            if(wordList.get(i).indexOf("造成伤害最多") != -1 )
            {
                attackerLocation = i;
                for(int j=attackerLocation-8;j<attackerLocation+1;j++)
                {
                    if((wordList.get(j).indexOf("号") != -1||wordList.get(j).indexOf("级") != -1) && wordList.get(j).indexOf("旗舰") == -1)
                    {
                        highATKShip=wordList.get(j);
                    }
                }
            }

        }

//        System.out.println("报告编号- " + reportId);
        System.out.println("报告时间- " + reportTime);
        System.out.println("舰船名称- " + shipName);
        System.out.println("金额- " + money);
        System.out.println("地区- " + area);
        System.out.println("星域- " + constellation);
        System.out.println("星系- " + galaxy);
        System.out.println("军团简称- " + armyShortName);
        System.out.println("角色名称- " + characterName);
        System.out.println("KM舰船- " + kmShip);
        System.out.println("KM军团- " + kmArmyShortName);
        System.out.println("KM角色名- " + kmGameId);
        System.out.println("最高伤害舰船- " + highATKShip);

        resultMap.put("info","success");
        resultMap.put("dataType","single");
//        resultMap.put("reportId",reportId);
        resultMap.put("reportTime",reportTime);
        resultMap.put("shipName",shipName);
        resultMap.put("money",money);
        resultMap.put("area",area);
        resultMap.put("constellation",constellation);
        resultMap.put("galaxy",galaxy);
        resultMap.put("armyShortName",armyShortName);
        resultMap.put("gameId",characterName);
        resultMap.put("kmShip",kmShip);
        resultMap.put("kmArmyShortName",kmArmyShortName);
        resultMap.put("kmGameId",kmGameId);
        resultMap.put("highATKShip",highATKShip);

        return resultMap;
    }


    public Map<String,Object> analysisMultipleLossData(List<String> wordList){
        Map<String,Object> resultMap = new HashMap<>();
        List<String> armyShortNameList = armyMapper.listArmyShorName();

        List<Map<String,String>> detectList = new ArrayList<>();

        String armyShortName = "";//军团简称
        for(int i = 0;i<wordList.size();i++){
            if(wordList.get(i).indexOf("[") != -1 || wordList.get(i).indexOf("]") != -1)
            {
                String armyShorStr = joinString(wordList.get(i).split("\\["));
                armyShorStr = joinString(armyShorStr.split("\\]"));
                for(String shortName :armyShortNameList){
                    if(armyShorStr.indexOf(shortName)>-1&&armyShortName.length()<shortName.length()){
                        armyShortName = shortName;
                    }
                }
            }
        }

        for(int i = 0;i<wordList.size();i++){

            Boolean isContainArmyShortName = false;
            String characterName = null;//角色名称
            if(armyShortName.equals("")){
                for(String shortName :armyShortNameList){
                    if(wordList.get(i).indexOf(shortName)>-1){
                        String armyShorStr = joinString(wordList.get(i).split("\\["));
                        armyShorStr = joinString(armyShorStr.split("\\]"));
                        if(armyShortName.length()<shortName.length()){
                            armyShortName = shortName;
                            characterName = joinString(armyShorStr.split(shortName));
                            isContainArmyShortName = true;
                        }
                    }
                }
            }else{
                if(wordList.get(i).indexOf(armyShortName)>-1){
                    String armyShorStr = joinString(wordList.get(i).split("\\["));
                    armyShorStr = joinString(armyShorStr.split("\\]"));
                    characterName = joinString(armyShorStr.split(armyShortName));
                    isContainArmyShortName = true;
                }
            }

            if(isContainArmyShortName){

//            }
//
//            if(wordList.get(i).indexOf("[") != -1 || wordList.get(i).indexOf("]") != -1)
//            {
                Map<String,String> detectMap = new HashMap<>();

                String reportTime = "";//报告日期
                String shipName = "";//舰船名
                String money = "";//金额
                String area = "";//地区
                String constellation = "";//星域
                String galaxy = "";//星座

//                String armyShorStr = joinString(wordList.get(i).split("\\["));
//                armyShorStr = joinString(armyShorStr.split("\\]"));
//                for(String shortName :armyShortNameList){
//                    if(armyShorStr.indexOf(shortName)>-1){
//                        armyShortName = shortName;
//                        characterName = joinString(armyShorStr.split(shortName));
//                    }
//                }

                if(wordList.get(i+1).indexOf("太空舱")>-1){
                    continue;
                }

                for(int j = i+1;j<i+7;j++){

                    if(shipName.equals("")&&(wordList.get(j).indexOf("号") != -1||wordList.get(j).indexOf("级") != -1) && wordList.get(j).indexOf("旗舰") == -1){
                        shipName = wordList.get(j);
                        continue;
                    }
                    if(wordList.get(j).indexOf("星币") != -1||wordList.get(j).indexOf("星市") != -1)
                    {
                        money = wordList.get(j);
                        money = joinString(money.split("星币"));
                        money = joinString(money.split("星市"));
                        money = joinString(money.split(","));
                        money = joinString(money.split("\\."));
                        money = joinString(money.split("\\]"));
                        continue;
                    }

                    if(wordList.get(j).indexOf("星币") != -1)
                    {
                        money = wordList.get(j);
                        money = joinString(money.split("星币"));
                        money = joinString(money.split(","));
                        money = joinString(money.split("\\."));
                        money = joinString(money.split("\\]"));
                        continue;
                    }

                    if(wordList.get(j).indexOf("<") != -1 )
                    {
                        String[] places = wordList.get(j).split("<");
                        if(places.length>2){
                            area = places[2];
                        }
                        if(places.length>1){
                            constellation = places[1];
                        }
                        if(places.length>0){
                            galaxy = places[0];
                        }
                        continue;
                    }

                    if(wordList.get(j).indexOf("/") != -1){
                        reportTime = wordList.get(j);
                        if(reportTime.length()<=10){
                            reportTime +=  wordList.get(j+1);
                        }
                        reportTime = joinString(reportTime.split("UTC\\+8"));
                        reportTime = joinString(reportTime.split("UT0\\+8"));
                        reportTime = joinString(reportTime.split("UTO\\+8"));
                        reportTime = reportTime.replaceAll("/","-");

                        String time = reportTime.substring(10);
                        time = joinString(time.split(":"));
                        if(time.length()<6){
                            int rest = 6 - time.length();
                            for(int t=0;t<rest;t++){
                                time+="0";
                            }
                        }
                        reportTime = reportTime.substring(0,10)+" "+time.substring(0,2)+":"+time.substring(2,4)+":"+time.substring(4,6);
                        continue;
                    }
                }

                detectMap.put("reportTime",reportTime);
                detectMap.put("shipName",shipName);
                detectMap.put("money",money);
                detectMap.put("area",area);
                detectMap.put("constellation",constellation);
                detectMap.put("galaxy",galaxy);
                detectMap.put("armyShortName",armyShortName);
                detectMap.put("gameId",characterName);

                detectList.add(detectMap);
            }
        }
        resultMap.put("info","success");
        resultMap.put("dataType","multiple");
        resultMap.put("list",detectList);

        return resultMap;
    }


//    击杀识别

    public Map<String,Object> analysisKillData(String ocrData){
        Map<String,Object> resultMap = new HashMap<>();

        JSONObject ocrStrj = JSONObject.parseObject(ocrData);

        if(!ocrStrj.containsKey("words_result")){
            return resultMap;
        }

        List<String> wordResultList = JSONObject.parseArray(ocrStrj.getString("words_result"),String.class);
        List<String> wordList = new ArrayList<>();
        for(String wordResult:wordResultList){
            JSONObject wordResultStrj = JSONObject.parseObject(wordResult);
            String word = wordResultStrj.getString("words");
            wordList.add(word);
        }

        String analysisMode = null;

        for(int i = 0;i<wordList.size();i++) {
            if(wordList.get(i).indexOf("击毁报告") != -1 || wordList.get(i).indexOf("损失报告") != -1){
                analysisMode = "single";
                break;
            }
            if(wordList.get(i).indexOf("军团战斗记录") != -1){
                analysisMode = "multiple";
                break;
            }
        }

        if(analysisMode=="single"){
            resultMap.putAll(analysisSingleKillData(wordList));
        }else if(analysisMode=="multiple"){
            resultMap = analysisMultipleKillData(wordList);
        }

        return resultMap;
    }



    public Map<String,String> analysisSingleKillData(List<String> wordList){
        Map<String,String> resultMap = new HashMap<>();

        List<String> armyShortNameList = armyMapper.listArmyShorName();

        Integer hasShipName=0;
        Integer hasCharacterName=0;
        Integer hasKMInfo=0;
        Integer attackerLocation=0;

//        String reportId = "";//报告ID
        String reportTime = "";//报告日期
        String shipName = "";//舰船名
        String money = "";//金额
        String area = "";//地区
        String constellation = "";//星域
        String galaxy = "";//星座
        String armyShortName = "";//军团简称
        String characterName = "";//角色名称
        String kmShip = "";//KM舰船
        String kmArmyShortName = "";//KM军团
        String kmGameId = "";//击杀者游戏名
        String highATKShip = "";//最高伤舰船
        for(int i = 0;i<wordList.size();i++){

//            if(wordList.get(i).indexOf("击毁报告") != -1 || wordList.get(i).indexOf("损失报告") != -1){
//                reportId = wordList.get(i);
//                if(reportId.length()<10){
//                    reportId +=  wordList.get(i+1);
//                }
//                reportId = joinString(reportId.split("三/"));
//                reportId = joinString(reportId.split("损失报告"));
//                reportId = joinString(reportId.split("击毁报告"));
//                reportId = joinString(reportId.split("\\["));
//                reportId = joinString(reportId.split("\\]"));
//                reportId = joinString(reportId.split("回"));
//                reportId = joinString(reportId.split("司"));
//                reportId = joinString(reportId.split("10:"));
//                reportId = joinString(reportId.split("10："));
//                reportId = joinString(reportId.split("0:"));
//                reportId = joinString(reportId.split("0："));
//                reportId = joinString(reportId.split("日"));
//                reportId = joinString(reportId.split("间"));
//                reportId = joinString(reportId.split(":"));
//                reportId = joinString(reportId.split("："));
//                continue;
//            }

            if(wordList.get(i).indexOf("UTC") != -1||wordList.get(i).indexOf("UT0") != -1||wordList.get(i).indexOf("UTO") != -1){
                reportTime = wordList.get(i);
                if(reportTime.length()<=10){
                    reportTime +=  wordList.get(i+1);
                }
                reportTime = joinString(reportTime.split("UTC\\+8"));
                reportTime = joinString(reportTime.split("UT0\\+8"));
                reportTime = joinString(reportTime.split("UTO\\+8"));
                reportTime = reportTime.replaceAll("/","-");

                String time = reportTime.substring(10);
                time = joinString(time.split(":"));
                if(time.length()<6){
                    int rest = 6 - time.length();
                    for(int t=0;t<rest;t++){
                        time+="0";
                    }
                }
                reportTime = reportTime.substring(0,10)+" "+time.substring(0,2)+":"+time.substring(2,4)+":"+time.substring(4,6);
                continue;
            }

            if((wordList.get(i).indexOf("号") != -1||wordList.get(i).indexOf("级") != -1)&&wordList.get(i).indexOf("舰") != -1 &&  hasShipName==0)
            {
                shipName = wordList.get(i);
                shipName = joinString(shipName.split("护卫舰"));
                shipName = joinString(shipName.split("驱逐舰"));
                shipName = joinString(shipName.split("战列巡洋舰"));
                shipName = joinString(shipName.split("巡洋舰"));
                shipName = joinString(shipName.split("战列舰"));
                shipName = joinString(shipName.split("旗舰"));
                shipName = joinString(shipName.split("工业舰"));

                hasShipName = 1;
                continue;
            }

            if(wordList.get(i).indexOf("星币") != -1||wordList.get(i).indexOf("星市") != -1)
            {
                money = wordList.get(i);
                money = joinString(money.split("星币"));
                money = joinString(money.split("星市"));
                money = joinString(money.split(","));
                money = joinString(money.split("\\."));
                money = joinString(money.split("\\]"));
                continue;
            }

            if(wordList.get(i).indexOf("<") != -1 )
            {
                String[] places = wordList.get(i).split("<");
                if(places.length>2){
                    area = places[2];
                }
                if(places.length>1){
                    constellation = places[1];
                }
                if(places.length>0){
                    galaxy = places[0];
                }
                continue;
            }

//            for(int j=0;j<6;j++)
//            {
//                if(hasCharacterName == 0&&wordList.get(j).indexOf("[") != -1 && wordList.get(j).indexOf("]") != -1)
//                {
//                    if(wordList.get(j).indexOf("损失报告") == -1 && wordList.get(j).indexOf("击毁报告") == -1 ){
//                        Integer firstIndex = wordList.get(j).indexOf("[");
//                        Integer lastIndex = wordList.get(j).indexOf("]");
//                        armyShortName = wordList.get(j).substring(firstIndex+1,lastIndex);
//                        characterName = wordList.get(j).substring(lastIndex+1);
//                        hasCharacterName = 1;
//                    }
//                }
//            }

            if(i<12){
                if(hasCharacterName == 0&&(wordList.get(i).indexOf("[") != -1 || wordList.get(i).indexOf("]") != -1))
                {
                    if(wordList.get(i).indexOf("损失报告") == -1 && wordList.get(i).indexOf("击毁报告") == -1 ){
                        String armyShorStr = joinString(wordList.get(i).split("\\["));
                        armyShorStr = joinString(armyShorStr.split("\\]"));
                        for(String shortName :armyShortNameList){
                            if(armyShorStr.indexOf(shortName)>-1&&armyShortName.length()<shortName.length()){
                                armyShortName = shortName;
                                characterName = joinString(armyShorStr.split(shortName));
                                hasCharacterName = 1;
                            }
                        }
                    }
                }
            }

            if(wordList.get(i).indexOf("其他") != -1 ){
//                if(wordList.get(i+1).indexOf("[") != -1 && wordList.get(i+1).indexOf("]") != -1)
//                {
//                    Integer firstIndex = wordList.get(i+1).indexOf("[");
//                    Integer lastIndex = wordList.get(i+1).indexOf("]");
//                    kmArmyShortName = wordList.get(i+1).substring(firstIndex+1,lastIndex);
//                    kmGameId = wordList.get(i+1).substring(lastIndex+1);
//                    hasKMInfo = 1;
//                }

                if(hasKMInfo==0){
                    String armyShorStr = joinString(wordList.get(i+1).split("\\["));
                    armyShorStr = joinString(armyShorStr.split("\\]"));
                    for(String shortName :armyShortNameList){
                        if(armyShorStr.indexOf(shortName)>-1){
                            kmArmyShortName = shortName;
                            kmGameId = joinString(armyShorStr.split(shortName));
                            hasKMInfo = 1;
                        }
                    }
                }
            }


            if(wordList.get(i).indexOf("最后一击") != -1 )
            {
                attackerLocation = i;
                for(int j=attackerLocation-5;j<attackerLocation+1;j++)
                {
                    if((wordList.get(j).indexOf("号") != -1||wordList.get(j).indexOf("级") != -1) && wordList.get(j).indexOf("旗舰") == -1)
                    {
                        kmShip = wordList.get(j);
                    }
                }
//                if(hasKMInfo!=1){
//                    for(int j=attackerLocation-12;j<attackerLocation+1;j++)
//                    {
//                        if(wordList.get(j).indexOf("[") != -1 && wordList.get(j).indexOf("]") != -1)
//                        {
//                            Integer firstIndex = wordList.get(j).indexOf("[");
//                            Integer lastIndex = wordList.get(j).indexOf("]");
//                            kmArmyShortName = wordList.get(j).substring(firstIndex+1,lastIndex);
//                            kmGameId = wordList.get(j).substring(lastIndex+1);
//                            hasKMInfo = 1;
//                        }
//                    }
//                }
                if(hasKMInfo!=1){
                    for(int j=attackerLocation-12;j<attackerLocation+1;j++)
                    {
                        if(wordList.get(j).indexOf("[") != -1 || wordList.get(j).indexOf("]") != -1)
                        {
                            String armyShorStr = joinString(wordList.get(j).split("\\["));
                            armyShorStr = joinString(armyShorStr.split("\\]"));
                            for(String shortName :armyShortNameList){
                                if(armyShorStr.indexOf(shortName)>-1&&kmArmyShortName.length()<shortName.length()){
                                    kmArmyShortName = shortName;
                                    kmGameId = joinString(armyShorStr.split(shortName));
                                    hasKMInfo = 1;
                                }
                            }
                        }
                    }
                }
            }

            if(wordList.get(i).indexOf("造成伤害最多") != -1 )
            {
                attackerLocation = i;
                for(int j=attackerLocation-8;j<attackerLocation+1;j++)
                {
                    if((wordList.get(j).indexOf("号") != -1||wordList.get(j).indexOf("级") != -1) && wordList.get(j).indexOf("旗舰") == -1)
                    {
                        highATKShip=wordList.get(j);
                    }
                }
            }

        }

//        System.out.println("报告编号- " + reportId);
        System.out.println("报告时间- " + reportTime);
        System.out.println("舰船名称- " + shipName);
        System.out.println("金额- " + money);
        System.out.println("地区- " + area);
        System.out.println("星域- " + constellation);
        System.out.println("星系- " + galaxy);
        System.out.println("军团简称- " + armyShortName);
        System.out.println("角色名称- " + characterName);
        System.out.println("KM舰船- " + kmShip);
        System.out.println("KM军团- " + kmArmyShortName);
        System.out.println("KM角色名- " + kmGameId);
        System.out.println("最高伤害舰船- " + highATKShip);

        resultMap.put("info","success");
        resultMap.put("dataType","single");
//        resultMap.put("reportId",reportId);
        resultMap.put("reportTime",reportTime);
        resultMap.put("shipName",shipName);
        resultMap.put("money",money);
        resultMap.put("area",area);
        resultMap.put("constellation",constellation);
        resultMap.put("galaxy",galaxy);
        resultMap.put("armyShortName",armyShortName);
        resultMap.put("gameId",characterName);
        resultMap.put("kmShip",kmShip);
        resultMap.put("kmArmyShortName",kmArmyShortName);
        resultMap.put("kmGameId",kmGameId);
        resultMap.put("highATKShip",highATKShip);

        return resultMap;
    }


    public Map<String,Object> analysisMultipleKillData(List<String> wordList){
        Map<String,Object> resultMap = new HashMap<>();
        List<String> armyShortNameList = armyMapper.listArmyShorName();

        List<Map<String,String>> detectList = new ArrayList<>();

        String armyShortName = "";//军团简称
        for(int i = 0;i<wordList.size();i++){
            if(wordList.get(i).indexOf("[") != -1 || wordList.get(i).indexOf("]") != -1)
            {
                String armyShorStr = joinString(wordList.get(i).split("\\["));
                armyShorStr = joinString(armyShorStr.split("\\]"));
                for(String shortName :armyShortNameList){
                    if(armyShorStr.indexOf(shortName)>-1&&armyShortName.length()<shortName.length()){
                        armyShortName = shortName;
                    }
                }
            }
        }

        for(int i = 0;i<wordList.size();i++){

            Boolean isContainArmyShortName = false;
            String characterName = null;//角色名称
            if(armyShortName.equals("")){
                for(String shortName :armyShortNameList){
                    if(wordList.get(i).indexOf(shortName)>-1){
                        String armyShorStr = joinString(wordList.get(i).split("\\["));
                        armyShorStr = joinString(armyShorStr.split("\\]"));
                        if(armyShortName.length()<shortName.length()){
                            armyShortName = shortName;
                            characterName = joinString(armyShorStr.split(shortName));
                            isContainArmyShortName = true;
                        }
                    }
                }
            }else{
                if(wordList.get(i).indexOf(armyShortName)>-1){
                    String armyShorStr = joinString(wordList.get(i).split("\\["));
                    armyShorStr = joinString(armyShorStr.split("\\]"));
                    characterName = joinString(armyShorStr.split(armyShortName));
                    isContainArmyShortName = true;
                }
            }

            if(isContainArmyShortName){

//            }
//
//            if(wordList.get(i).indexOf("[") != -1 || wordList.get(i).indexOf("]") != -1)
//            {
                Map<String,String> detectMap = new HashMap<>();

                String reportTime = "";//报告日期
                String shipName = "";//舰船名
                String money = "";//金额
                String area = "";//地区
                String constellation = "";//星域
                String galaxy = "";//星座

//                String armyShorStr = joinString(wordList.get(i).split("\\["));
//                armyShorStr = joinString(armyShorStr.split("\\]"));
//                for(String shortName :armyShortNameList){
//                    if(armyShorStr.indexOf(shortName)>-1){
//                        armyShortName = shortName;
//                        characterName = joinString(armyShorStr.split(shortName));
//                    }
//                }

                if(wordList.get(i+1).indexOf("太空舱")>-1){
                    continue;
                }

                for(int j = i+1;j<i+7;j++){

                    if(shipName.equals("")&&(wordList.get(j).indexOf("号") != -1||wordList.get(j).indexOf("级") != -1) && wordList.get(j).indexOf("旗舰") == -1){
                        shipName = wordList.get(j);
                        continue;
                    }
                    if(wordList.get(j).indexOf("星币") != -1||wordList.get(j).indexOf("星市") != -1)
                    {
                        money = wordList.get(j);
                        money = joinString(money.split("星币"));
                        money = joinString(money.split("星市"));
                        money = joinString(money.split(","));
                        money = joinString(money.split("\\."));
                        money = joinString(money.split("\\]"));
                        continue;
                    }

                    if(wordList.get(j).indexOf("星币") != -1)
                    {
                        money = wordList.get(j);
                        money = joinString(money.split("星币"));
                        money = joinString(money.split(","));
                        money = joinString(money.split("\\."));
                        money = joinString(money.split("\\]"));
                        continue;
                    }

                    if(wordList.get(j).indexOf("<") != -1 )
                    {
                        String[] places = wordList.get(j).split("<");
                        if(places.length>2){
                            area = places[2];
                        }
                        if(places.length>1){
                            constellation = places[1];
                        }
                        if(places.length>0){
                            galaxy = places[0];
                        }
                        continue;
                    }

                    if(wordList.get(j).indexOf("/") != -1){
                        reportTime = wordList.get(j);
                        if(reportTime.length()<=10){
                            reportTime +=  wordList.get(j+1);
                        }
                        reportTime = joinString(reportTime.split("UTC\\+8"));
                        reportTime = joinString(reportTime.split("UT0\\+8"));
                        reportTime = joinString(reportTime.split("UTO\\+8"));
                        reportTime = reportTime.replaceAll("/","-");

                        String time = reportTime.substring(10);
                        time = joinString(time.split(":"));
                        if(time.length()<6){
                            int rest = 6 - time.length();
                            for(int t=0;t<rest;t++){
                                time+="0";
                            }
                        }
                        reportTime = reportTime.substring(0,10)+" "+time.substring(0,2)+":"+time.substring(2,4)+":"+time.substring(4,6);
                        continue;
                    }
                }

                detectMap.put("reportTime",reportTime);
                detectMap.put("shipName",shipName);
                detectMap.put("money",money);
                detectMap.put("area",area);
                detectMap.put("constellation",constellation);
                detectMap.put("galaxy",galaxy);
                detectMap.put("armyShortName",armyShortName);
                detectMap.put("gameId",characterName);

                detectList.add(detectMap);
            }
        }
        resultMap.put("info","success");
        resultMap.put("dataType","multiple");
        resultMap.put("list",detectList);

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
