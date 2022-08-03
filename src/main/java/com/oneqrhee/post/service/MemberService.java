package com.oneqrhee.post.service;

import com.oneqrhee.post.dto.member.LoginDto;
import com.oneqrhee.post.dto.member.MemberRequestDto;
import com.oneqrhee.post.dto.member.MemberResponseDto;
import com.oneqrhee.post.dto.member.TokenDto;
import com.oneqrhee.post.entity.Member;
import com.oneqrhee.post.entity.RefreshToken;
import com.oneqrhee.post.jwt.TokenProvider;
import com.oneqrhee.post.repository.MemberRepository;
import com.oneqrhee.post.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public ResponseEntity<String> signup(MemberRequestDto memberRequestDto) {
        if (memberRepository.existsByNickname(memberRequestDto.getNickname())) {
            return new ResponseEntity<>("중복된 닉네임입니다.", HttpStatus.BAD_REQUEST);
        }
        if(!memberRequestDto.getPassword().equals(memberRequestDto.getPasswordConfirm())){
            return new ResponseEntity<>("비밀번호와 비밀번호 확인이 일치하지 않습니다.", HttpStatus.BAD_REQUEST);
        }
        if(!memberRequestDto.getNickname().matches("^[a-zA-Z0-9]{4,12}")){
            return new ResponseEntity<>("닉네임은 영어대소문자와 숫자를 포함하여 4~12자로 입력해주세요.", HttpStatus.BAD_REQUEST);
        }
        if(!memberRequestDto.getPassword().matches("^[a-z0-9]{4,32}")){
            return new ResponseEntity<>("비밀번호는 영어소문자와 숫자를 포함하여 4~32자로 입력해주세요.", HttpStatus.BAD_REQUEST);
        }

        Member member = memberRequestDto.toMember(passwordEncoder);
        MemberResponseDto.of(memberRepository.save(member));
        return new ResponseEntity<>("회원가입이 완료되었습니다.", HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<String> login(LoginDto loginDto, HttpServletResponse response) {
        // 1. Login ID/PW 를 기반으로 AuthenticationToken 생성
        UsernamePasswordAuthenticationToken authenticationToken = loginDto.toAuthentication();

        // 2. 실제로 검증 (사용자 비밀번호 체크) 이 이루어지는 부분
        //    authenticate 메서드가 실행이 될 때 CustomUserDetailsService 에서 만들었던 loadUserByUsername 메서드가 실행됨
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

        response.setHeader("Authorization", "Bearer "+tokenDto.getAccessToken());
        response.setHeader("Refresh-Token", tokenDto.getRefreshToken());
        response.setHeader("Access-Token-Expire-Time", Long.toString(tokenDto.getAccessTokenExpiresIn()));

        // 4. RefreshToken 저장
        RefreshToken refreshToken = RefreshToken.builder()
                .key(authentication.getName())
                .value(tokenDto.getRefreshToken())
                .build();

        refreshTokenRepository.save(refreshToken);

        return new ResponseEntity<>("로그인되었습니다.", HttpStatus.OK);
    }
}
