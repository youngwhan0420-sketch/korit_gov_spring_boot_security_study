package com_korit.security_study.service;

import com_korit.security_study.dto.ApiRespDto;
import com_korit.security_study.dto.OAuth2SignupReqDto;
import com_korit.security_study.entity.User;
import com_korit.security_study.entity.UserRole;
import com_korit.security_study.repository.OAuth2UserRepository;
import com_korit.security_study.repository.UserRepository;
import com_korit.security_study.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OAuth2AuthService { //oauth2로 회원가입 또는 연동을 담당하는 서비스

    @Autowired
    private OAuth2UserRepository oAuth2UserRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public ApiRespDto<?> signup(OAuth2SignupReqDto oAuth2SignupReqDto) {
        Optional<User> foundUser = userRepository.getUserByEmail(oAuth2SignupReqDto.getEmail());

        if(foundUser.isPresent()) {
            return new ApiRespDto<>("failed", "이미 존재하는 이메일 입니다.", null);
        }
        Optional<User> optionalUser = userRepository.addUser(oAuth2SignupReqDto.toUserEntity(bCryptPasswordEncoder));
        UserRole userRole = UserRole.builder()
                .userId(optionalUser.get().getUserId())
                .roleId(3)
                .build();
        userRoleRepository.addUserRole(userRole);
        oAuth2UserRepository.addOAuth2User(oAuth2SignupReqDto.toOauth2UserEntity(optionalUser.get().getUserId()));

        return new ApiRespDto<>("success",   oAuth2SignupReqDto.getProvider() + "로 회원가입 완료", null);
    }
}
