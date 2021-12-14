package org.eu.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public interface OCRService {

    Map<String,String> detectPic(MultipartFile multipartFile) throws IOException;

}
