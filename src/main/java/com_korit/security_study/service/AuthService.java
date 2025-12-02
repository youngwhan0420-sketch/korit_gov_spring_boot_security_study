package com_korit.security_study.service;

import com_korit.security_study.dto.ApiRespDto;
import com_korit.security_study.dto.OAuth2MergeReqDto;
import com_korit.security_study.dto.SigninReqDto;
import com_korit.security_study.dto.SignupReqDto;
import com_korit.security_study.entity.User;
import com_korit.security_study.entity.UserRole;
import com_korit.security_study.entity.Verify;
import com_korit.security_study.repository.UserRepository;
import com_korit.security_study.repository.UserRoleRepository;
import com_korit.security_study.repository.VerifyRepository;
import com_korit.security_study.security.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
public class AuthService {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private VerifyRepository verifyRepository;

    public ApiRespDto<?> signup(SignupReqDto signupReqDto) {
        Optional<User> emailFoundUser = userRepository.getUserByEmail(signupReqDto.getEmail());
        if (emailFoundUser.isPresent()) {
            return new ApiRespDto<>("failed", "이미 존재하는 이메일 입니다.", null);
        }

        Optional<User> usernameFoundUser = userRepository.getUserByUsername(signupReqDto.getUsername());
        if (usernameFoundUser.isPresent()) {
            return new ApiRespDto<>("failed", "이미 존재하는 사용자이름 입니다.", null);
        }

        Optional<User> optionalUser = userRepository.addUser(signupReqDto.toEntity(bCryptPasswordEncoder));
        if (optionalUser.isEmpty()) {
            return new ApiRespDto<>("failed", "회원가입 중 문제가 발생했습니다.", null);
        }

        UserRole userRole = UserRole.builder()
                .userId(optionalUser.get().getUserId())
                .roleId(3)
                .build();
        userRoleRepository.addUserRole(userRole);

        StringBuilder sb = new StringBuilder();
        Random rd = new Random();

        for(int i=0;i<5;i++){
            sb.append(rd.nextInt(10));
        }

        Verify verify = Verify.builder()
                .userId(optionalUser.get().getUserId())
                .verifyCode(sb.toString())
                .build();
        verifyRepository.addVerifyCode(verify);

        return new ApiRespDto<>("success", "회원가입이 완료되었습니다.", optionalUser.get());
    }
    public ApiRespDto<?> signin(SigninReqDto signinReqDto) {
        Optional<User> foundUser = userRepository.getUserByEmail(signinReqDto.getEmail());
        if (foundUser.isEmpty()) {
            return new ApiRespDto<>("failed", "사용자 정보가 일치하지 않습니다.", null);
        }
        if (!bCryptPasswordEncoder.matches(signinReqDto.getPassword(), foundUser.get().getPassword())) {
            return new ApiRespDto<>("failed", "사용자 정보가 일치하지 않습니다.", null);
        }
        String accessToken = jwtUtils.generateToken(foundUser.get().getUserId().toString());
        return new ApiRespDto<>("success", "로그인 성공", accessToken);
    }

    public ApiRespDto<?> getUserByUserId(Integer userId) {
        return new ApiRespDto<>("success", "회원 조회", userRepository.getUserByUserId(userId));
    }
}