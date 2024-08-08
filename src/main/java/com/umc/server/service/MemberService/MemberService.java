package com.umc.server.service.MemberService;

import com.umc.server.domain.Member;
import com.umc.server.web.dto.request.MemberRequestDTO;
import com.umc.server.web.dto.response.MemberResponseDTO;
import jakarta.mail.MessagingException;
import java.util.List;

public interface MemberService {

    // TODO: 회원가입
    MemberResponseDTO.SignInResponseDTO signUp(MemberRequestDTO.SignUpRequestDTO signUpRequestDTO);

    // TODO: 로그인
    MemberResponseDTO.SignInResponseDTO signIn(MemberRequestDTO.SignInRequestDTO signInRequestDTO);

    // TODO: 토큰 만료시 재발급
    MemberResponseDTO.TokenInfo renew(String accessToken, String refreshToken);

    // TODO: 로그아웃
    void signOut(Member signInMember);

    // TODO: 닉네임 존재 여부 확인
    Boolean nicknameExist(String nickname);

    // TODO: 알림 상태 변경
    MemberResponseDTO.PushNotification setNotification(
            Long memberId, Boolean commentPush, Boolean likePush, Boolean nightPush);

    // TODO: 이메일로 인증번호 보내기
    MemberResponseDTO.CodeDTO sendCode(String email) throws MessagingException;

    // TODO: 비밀번호 변경하기
    void changePassword(MemberRequestDTO.ChangePasswordDTO changePasswordDTO);

    // TODO: 책모아 팀원 정보 얻기
    List<MemberResponseDTO.AdminMemberResponseDTO> getAdminInfo();
}
