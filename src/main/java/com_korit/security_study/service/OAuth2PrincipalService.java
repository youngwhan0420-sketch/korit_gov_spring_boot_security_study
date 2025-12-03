package com_korit.security_study.service;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class OAuth2PrincipalService extends DefaultOAuth2UserService { //오어스2객체를 만들어주면 핸들러에서 파싱한다.

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        //Spring Security가 OAuth2 provider에게 AccessToken으로 사용자 정보를 요청 하고
        //그 결과로 받은 사용자 정보(JSON)를 파싱한 객체를 리턴 받는다.
        OAuth2User oAuth2User = super.loadUser(userRequest);

        //사용자 정보를 추출하는데 Map형태로 추출한다.
        Map<String, Object> attributes = oAuth2User.getAttributes();

        //어떤 provider인지 확인을 한다.
        //provider => 공급처(google, naver, kakao)
        String provider = userRequest.getClientRegistration().getRegistrationId(); //이렇게 하면 provider을 받아올 수 있다.
        //이메일을 뽑아와야 한다.
        String email = (String) attributes.get("email");
        // 공급처에서 발행한 사용자 식별자
        String providerUserId = attributes.get("sub").toString();
        // 먼저 잡아두는 이유는 플랫폼마다 다 다르게 넣어져 있어서 파싱을 다 다르게 해줘야한다.



        switch (provider) {
            case "google":
                providerUserId = attributes.get("sub").toString();
                email = (String) attributes.get("email");
                break;
            case "naver":
                break;
            case "kakao":
                break;
        }

        Map<String, Object> newAttributes = Map.of(
                "providerUserId", providerUserId,
                "provider", provider,
                "email", email
        );
        // 임시 권한 부여 (Role_TEMPORARY) 얘를 부여한다.
        // 실제 권한은 OAuth2SuccessHandler에서 판단
        List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_TEMPORARY_USER"));

        //Spring Security가 사용할 OAuth2User 객체 생성해서 반환
        //id => principal.getName() 했을 때 사용할 이름
        return new DefaultOAuth2User(authorities, newAttributes, "providerUserId");
    }
}
