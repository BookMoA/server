package com.umc.server.service.S3Service;

import static com.umc.server.util.FileNameUtils.newFileName;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.umc.server.apiPayload.code.status.ErrorStatus;
import com.umc.server.apiPayload.exception.handler.MemberHandler;
import jakarta.transaction.Transactional;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional
public class S3ServiceImpl implements S3Service {
    @Value("${S3_BUCKET}")
    private String bucket;

    private final AmazonS3Client amazonS3Client;

    public String uploadFile(MultipartFile file) throws IOException {
        if (file.getOriginalFilename() == null || file.getOriginalFilename().isEmpty()) {
            throw new MemberHandler(ErrorStatus._BAD_REQUEST);
        }

        String fileName = newFileName(file.getOriginalFilename()); // s3에 저장될 이름
        File convertedFile = convertMultiPartToFile(file); // 임시파일로 변경한 후 업로드

        try {
            amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, convertedFile));

            return amazonS3Client.getUrl(bucket, fileName).toString();

        } finally {
            convertedFile.delete(); // 임시 파일 삭제
        }
    }

    // TODO: multipartFile -> 임시 File로 변경
    public File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convertedFile = new File(Objects.requireNonNull(file.getOriginalFilename()));
        try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
            fos.write(file.getBytes());
        }
        return convertedFile;
    }
}
