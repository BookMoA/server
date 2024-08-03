package com.umc.server.service.MemberService;

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

        // password 암호화 진행
        final String password = signUpRequestDTO.getPassword();
        final String encodedPassword = PasswordUtil.encryptPassword(password);
        signUpRequestDTO.setPassword(encodedPassword);

        // converter를 이용하여 DTO를 Member로 변경
        final Member newMember = MemberConverter.toMember(signUpRequestDTO);

        // 레포지토리에 저장
        memberRepository.save(newMember);

        // 로그인 함수 불러옴
    }
}
