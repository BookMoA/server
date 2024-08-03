package com.umc.server.service.MemberService;

import com.umc.server.apiPayload.code.status.ErrorStatus;
import com.umc.server.apiPayload.exception.handler.MemberHandler;
import com.umc.server.converter.MemberConverter;
import com.umc.server.domain.Member;
import com.umc.server.repository.MemberRepository;
import com.umc.server.util.PasswordUtil;
import com.umc.server.web.dto.request.MemberRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    // TODO: 회원가입 기능
    public void signUp(MemberRequestDTO.SignUpRequestDTO signUpRequestDTO) {

        // 존재하는 회원인지 확인
        final String email = signUpRequestDTO.getEmail();
        Boolean isExistingMember = memberRepository.existsByEmail(email);
        if (isExistingMember) {
            throw new MemberHandler(ErrorStatus.valueOf("MEMBER_ALREADY_EXISTS"));
        }

        // password 암호화 진행
        final String password = signUpRequestDTO.getPassword();
        final String encodedPassword = PasswordUtil.encryptPassword(password);
        signUpRequestDTO.setPassword(encodedPassword);

        // converter를 이용하여 DTO를 Member로 변경
        final Member newMember = MemberConverter.toMember(signUpRequestDTO);

        // 레포지토리에 저장
        memberRepository.save(newMember);
    }

    // TODO: 로그인
    public Member signIn(MemberRequestDTO.SignInRequestDTO signInRequestDTO) {

        // 이메일 확인
        final String email = signInRequestDTO.getEmail();
        Member signInmember =
                memberRepository
                        .findByEmail(email)
                        .orElseThrow(
                                () -> new MemberHandler(ErrorStatus.valueOf("MEMBER_NOT_FOUND")));

        // 비밀번호 확인
        final String password = signInRequestDTO.getPassword();
        final String encodedPassword = PasswordUtil.encryptPassword(password);
        if (!signInmember.getPassword().equals(encodedPassword)) {
            throw new MemberHandler(ErrorStatus.valueOf("MEMBER_NOT_FOUND"));
        }

        // 비밀번호가 맞다면 access token 발급

        // active data 업데이트

        // repository에 저장

        return signInmember;
    }
}
