package com.simon.classroom.vod.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    /**
     *  上传文件
     */
    String upload(MultipartFile file);
}
