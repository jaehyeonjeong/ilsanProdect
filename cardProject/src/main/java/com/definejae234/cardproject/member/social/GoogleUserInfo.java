package com.jjyy0614.jpa.social;

import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class GoogleUserInfo implements SocialUserInfo{
    private final Map<String, Object> attributes;

    @Override
    public String getName() {
        // userName
        return (String)attributes.get("name");
    }

    @Override
    public String getEmail() {
        // userEmail
        return (String)attributes.get("email");
    }

    @Override
    public String getProvider() {
        return "google";
    }

    @Override
    public String getProviderID() {
        // 이게 유저 ID가 됨
        return getProvider()+"_"+attributes.get("sub").toString();
    }
}
