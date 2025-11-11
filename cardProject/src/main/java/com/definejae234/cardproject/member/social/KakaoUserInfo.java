package com.jjyy0614.jpa.social;

import lombok.RequiredArgsConstructor;

import java.util.Map;

@RequiredArgsConstructor
public class KakaoUserInfo implements SocialUserInfo{
    private final Map<String, Object> attributes;
    @Override
    public String getName() {
        Map<String,Object> properties = (Map)attributes.get("properties");
        return properties.get("nickname").toString();
    }

    @Override
    public String getEmail() {
        return "이메일을 등록해주세요";
    }

    @Override
    public String getProvider() {
        return "kakao";
    }

    @Override
    public String getProviderID() {
        return getProvider()+"_"+attributes.get("id").toString();
    }
}
