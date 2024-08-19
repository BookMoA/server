package com.umc.server.service.MemberService;

import com.umc.server.domain.Member;
import com.umc.server.repository.MemberRepository;
import com.umc.server.service.S3Service.S3Service;
import com.umc.server.web.dto.response.MemberResponseDTO;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class AwsServiceImpl implements AwsService {

    private final MemberRepository memberRepository;
    private final S3Service s3Service;

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
            final String newUrl = s3Service.uploadFile(file);
            signInMember.setProfileURL(newUrl);
        }

        Member updatedMember = memberRepository.save(signInMember);
        return MemberResponseDTO.EditProfileInfo.of(
                updatedMember.getEmail(),
                updatedMember.getNickname(),
                updatedMember.getProfileURL());
    }
}
