package com.umc.server.service.MemberService;

import static com.umc.server.util.FileNameUtils.newFileName;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.umc.server.apiPayload.code.status.ErrorStatus;
import com.umc.server.apiPayload.exception.handler.MemberHandler;
import com.umc.server.domain.Member;
import com.umc.server.repository.MemberRepository;
import com.umc.server.web.dto.response.MemberResponseDTO;
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
public class AwsServiceImpl implements AwsService {

    @Value("${S3_BUCKET}")
    private String bucket;

    private final AmazonS3Client amazonS3Client;
    private final MemberRepository memberRepository;

    // TODO: 프로필 정보 변경
    public MemberResponseDTO.EditProfileInfo editProfileInfo(
            MultipartFile file, Member signInMember, String nickname, String email)
            throws IOException {

        if (nickname != null) {
            signInMember.setNickname(nickname);
        }

        if (email != null) {
            signInMember.setEmail(email);
        }

        if (file != null && !file.isEmpty()) {
            final String newUrl = uploadFile(file);
            signInMember.setProfileURL(newUrl);
        }

        Member updatedMember = memberRepository.save(signInMember);
        return MemberResponseDTO.EditProfileInfo.of(
                updatedMember.getEmail(),
                updatedMember.getNickname(),
                updatedMember.getProfileURL());
    }

    // TODO: s3에 이미지 업로드
    private String uploadFile(MultipartFile file) throws IOException {
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
    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convertedFile = new File(Objects.requireNonNull(file.getOriginalFilename()));
        try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
            fos.write(file.getBytes());
        }
        return convertedFile;
    }
}
