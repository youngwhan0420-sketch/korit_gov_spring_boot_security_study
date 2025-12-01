package com_korit.security_study.repository;

import com_korit.security_study.entity.OAuth2User;
import com_korit.security_study.mapper.OAuth2UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository


public class OAuth2UserRepository {
    @Autowired
    private final OAuth2UserMapper oAuth2UserMapper;

    public OAuth2UserRepository(OAuth2UserMapper oAuth2UserMapper) {
        this.oAuth2UserMapper = oAuth2UserMapper;
    }

    public Optional<OAuth2User> getOAuth2UserByProviderAndProviderUserId(String provider, String providerUserId) {
        return oAuth2UserMapper.getOAuth2UserByProviderAndProviderUserId(provider, providerUserId);
    }
    public int addOAuth2User(OAuth2User oAuth2User){
        return oAuth2UserMapper.addOAuth2User(oAuth2User);
    }
}
