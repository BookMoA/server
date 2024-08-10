package com.umc.server.service.S3Service;

import java.io.File;
import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;

public interface S3Service {
    String uploadFile(MultipartFile file) throws IOException;

    File convertMultiPartToFile(MultipartFile file) throws IOException;
}
