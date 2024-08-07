package com.umc.server.service.MemberService;

import com.umc.server.domain.Member;
import com.umc.server.web.dto.response.MemberResponseDTO;
import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;

public interface AwsService {

    // TODO: s3에 이미지 업로드, 프로필 정보 변경
    MemberResponseDTO.EditProfileInfo editProfileInfo(
            MultipartFile file, Member signInMember, String nickname, String email)
            throws IOException;
}
