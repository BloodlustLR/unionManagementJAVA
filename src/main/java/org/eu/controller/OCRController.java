package org.eu.controller;

import org.eu.service.OCRService;
import org.eu.util.FileUtil;
import org.eu.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/ocr")
public class OCRController {

    @Autowired
    OCRService ocrService;


    @PostMapping("/detectPic")
    public Map<String,String> detectPic(@RequestParam("file") MultipartFile multipartFile) throws IOException {

        return ocrService.detectPic(multipartFile);
    }


}
