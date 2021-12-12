package org.eu.service.impl;

import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.leptonica.PIX;
import org.bytedeco.tesseract.TessBaseAPI;
import org.eu.service.OCRService;
import org.eu.util.FileUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

import static org.bytedeco.leptonica.global.lept.pixDestroy;
import static org.bytedeco.leptonica.global.lept.pixRead;

@Service
public class OCRServiceImpl implements OCRService {

    @Value("${ocr.language}")
    private String language;

    @Value("${ocr.data-path}")
    private String dataPath;

    @Override
    public String detectPic(MultipartFile multipartFile) throws IOException {

        BytePointer outText;

        TessBaseAPI api = new TessBaseAPI();

        //获取最后一个.的位置
        int lastIndexOf = multipartFile.getOriginalFilename().lastIndexOf(".");
        //获取文件的后缀名 .jpg
        String suffix = multipartFile.getOriginalFilename().substring(lastIndexOf);

        String fileName = Calendar.getInstance().getTimeInMillis()+suffix;

        FileUtil.inputStreamToFile(multipartFile.getInputStream(),new File(fileName));

        if (api.Init(dataPath, language) != 0) {
            System.err.println("无法初始化tesseract");
            return null;
        }

        // Open input image with leptonica library
        PIX image = pixRead(fileName);
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
            FileUtil.deleteTempFile(fileName);
        }

    }

}
