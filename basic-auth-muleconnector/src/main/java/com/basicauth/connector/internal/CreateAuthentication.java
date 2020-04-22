package com.basicauth.connector.internal;

import org.mule.runtime.http.api.client.auth.HttpAuthentication;
import org.mule.runtime.http.api.client.auth.HttpAuthenticationBuilder;

public class CreateAuthentication {
    public static HttpAuthentication createAuth(String username, String password){
        HttpAuthenticationBuilder authenticationBuilder = HttpAuthentication.basic(username, password);
        return authenticationBuilder.build();
    }
}
