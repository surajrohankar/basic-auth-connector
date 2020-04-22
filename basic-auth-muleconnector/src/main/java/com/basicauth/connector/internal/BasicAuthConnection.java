package com.basicauth.connector.internal;


import org.mule.runtime.http.api.client.HttpClient;
import org.mule.runtime.http.api.client.auth.HttpAuthentication;

/**
 * This class represents an extension connection just as example (there is no real connection with anything here c:).
 */
public final class BasicAuthConnection {

   private HttpClient httpClient;
   private String username;
   private String password;
   private HttpAuthentication authentication;

    public BasicAuthConnection(HttpClient httpClient, String username, String password, HttpAuthentication authentication) {
       this.httpClient=httpClient;
       this.username=username;
       this.password=password;
       this.authentication=authentication;

    }

  public HttpClient getHttpClient() {
    return httpClient;
  }

  public String getUsername() {
    return username;
  }

  public String getPassword() {
    return password;
  }

  public HttpAuthentication getAuthentication(){
        return authentication;
  }

  public void invalidate() {
    // do something to invalidate this connection!
  }
}
