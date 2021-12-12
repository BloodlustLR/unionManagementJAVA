package org.eu.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

public interface OCRService {

    String detectPic(MultipartFile multipartFile) throws IOException;

}
