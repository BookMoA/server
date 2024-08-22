package com.umc.server.converter;

import com.umc.server.domain.CancelAccount;
import com.umc.server.domain.Club;
import com.umc.server.domain.Member;
import com.umc.server.domain.PushNotification;
import com.umc.server.domain.enums.Role;
import com.umc.server.domain.enums.SignUpType;
import com.umc.server.domain.mapping.ClubMember;
import com.umc.server.web.dto.request.KakaoRequestDTO;
import com.umc.server.web.dto.request.MemberRequestDTO;
import com.umc.server.web.dto.response.ClubResponseDTO;
import com.umc.server.web.dto.response.MemberResponseDTO;

public class MemberConverter {

    // 회원가입시 받은 정보를 member 인스턴스로 변환
    public static Member toMember(MemberRequestDTO.SignUpRequestDTO signUpRequestDTO) {
        return Member.builder()
                .email(signUpRequestDTO.getEmail())
                .password(signUpRequestDTO.getPassword())
                .nickname(signUpRequestDTO.getNickname())
                .signUpType(SignUpType.valueOf("GENERAL"))
                .role(Role.valueOf("USER"))
                .build();
    }

    // 로그인 후 멤버 정보를 responseDTO로 변환
    public static MemberResponseDTO.SignInResponseDTO toSignInResponseDTO(Member member) {

        MemberResponseDTO.PushNotification pushNotificationDTO = null;
        if (member.getPushNotification() != null) {
            PushNotification pushNotification = member.getPushNotification();
            pushNotificationDTO =
                    MemberResponseDTO.PushNotification.of(
                            pushNotification.getLikePushEnabled(),
                            pushNotification.getCommentPushEnabled(),
                            pushNotification.getNightPushEnabled());
        }

        ClubResponseDTO.MyClubResponseDTO myClubResponseDTO = null;
        ClubMember clubMember = member.getClubMember();

        if (clubMember != null) {
            Club myClub = clubMember.getClub();
            myClubResponseDTO =
                    ClubResponseDTO.MyClubResponseDTO.builder()
                            .clubId(myClub.getId())
                            .name(myClub.getName())
                            .intro(myClub.getIntro())
                            .build();
        }

        return MemberResponseDTO.SignInResponseDTO.builder()
                .id(member.getId())
                .email(member.getEmail())
                .nickname(member.getNickname())
                .inFocusMode(
                        member.getInFocusMode() == null ? Boolean.FALSE : member.getInFocusMode())
                .totalPages(member.getTotalPages() == null ? 0L : member.getTotalPages())
                .signUpType(member.getSignUpType())
                .profileURL(member.getProfileURL())
                .refreshToken(member.getRefreshToken())
                .pushNotification(pushNotificationDTO)
                .myClub(myClubResponseDTO)
                .build();
    }

    // 소셜 로그인한 객체를 member 인스턴스로 변환
    public static Member toMember(KakaoRequestDTO.SignUpRequestDTO signUpRequestDTO) {
        return Member.builder()
                .profileURL(signUpRequestDTO.getProfileURL())
                .password(signUpRequestDTO.getPassword())
                .nickname(signUpRequestDTO.getNickname())
                .refreshToken(signUpRequestDTO.getRefreshToken())
                .signUpType(SignUpType.valueOf("SOCIAL"))
                .kakaoId(signUpRequestDTO.getKakaoId())
                .role(Role.valueOf("USER"))
                .build();
    }

    // 입력한 탈퇴 사유를 entity로 변환
    public static CancelAccount toCancelAccount(
            MemberRequestDTO.CancelAccountReasonDTO cancelAccountReasonDTO) {

        return CancelAccount.builder()
                .cancelReason(cancelAccountReasonDTO.getReason())
                .extraOpinion(cancelAccountReasonDTO.getExtraOpinion())
                .build();
    }
}
