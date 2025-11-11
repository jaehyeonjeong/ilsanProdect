package com.definejae234.cardproject.member.service;

import com.definejae234.cardproject.member.constant.Role;
import com.definejae234.cardproject.member.dto.CustomUserDetails;
import com.definejae234.cardproject.member.entity.Member;
import com.definejae234.cardproject.member.repository.MemberRepository;
import com.definejae234.cardproject.member.social.GoogleUserInfo;
import com.definejae234.cardproject.member.social.KakaoUserInfo;
import com.definejae234.cardproject.member.social.SocialUserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class OAuth2DetailsService  extends DefaultOAuth2UserService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        Map<String, Object> oauth2UserInfo = (Map) oAuth2User.getAttributes();
        String provider = userRequest.getClientRegistration().getRegistrationId();
        SocialUserInfo socialUserInfo = null;
        if (provider.equals("google")) {
            socialUserInfo = new GoogleUserInfo(oauth2UserInfo);
        } else if (provider.equals("kakao")) {
            socialUserInfo = new KakaoUserInfo(oauth2UserInfo);
        }
        Optional<Member> findedMember = memberRepository.findByUserID(socialUserInfo.getProviderID());
        Member returnMember = null;
        if (findedMember.isPresent()) {
            returnMember = findedMember.get();
        } else {
            Member member = Member.builder()
                    .userID(socialUserInfo.getProviderID())
                    .userName(socialUserInfo.getName())
                    .userEmail(socialUserInfo.getEmail())
                    .role(Role.ROLE_USER)
                    .userPW(passwordEncoder.encode(UUID.randomUUID().toString()))
                    .build();
            returnMember = memberRepository.save(member);
        }
        return new CustomUserDetails(returnMember,oAuth2User.getAttributes());
    }
}
