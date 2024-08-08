package org.franco.service;

import org.franco.domain.ResponseResult;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface UploadService {
    ResponseResult uploadImg(MultipartFile imgFile);
}
