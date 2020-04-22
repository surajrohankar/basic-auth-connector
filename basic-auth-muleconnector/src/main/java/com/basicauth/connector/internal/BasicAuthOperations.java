package com.basicauth.connector.internal;

import static org.mule.runtime.extension.api.annotation.param.MediaType.ANY;
import static org.mule.runtime.http.api.HttpHeaders.Names.CONTENT_TYPE;

import org.mule.runtime.extension.api.annotation.param.MediaType;
import org.mule.runtime.extension.api.annotation.param.Config;
import org.mule.runtime.extension.api.annotation.param.Connection;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;
import org.mule.runtime.http.api.HttpConstants;
import org.mule.runtime.http.api.client.HttpClient;
import org.mule.runtime.http.api.client.auth.HttpAuthentication;
import org.mule.runtime.http.api.client.auth.HttpAuthenticationBuilder;
import org.mule.runtime.http.api.domain.message.request.HttpRequest;
import org.mule.runtime.http.api.domain.message.request.HttpRequestBuilder;
import org.mule.runtime.http.api.domain.message.response.HttpResponse;

import java.io.InputStream;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;


/**
 * This class is a container for operations, every public method in this class will be taken as an extension operation.
 */
public class BasicAuthOperations {



  /**
   * Example of an operation that uses the configuration and a connection instance to perform some action.
   */
  @DisplayName("Get Message")
  @MediaType(value = ANY, strict = false)
  public InputStream getMessage(@Config BasicAuthConfiguration configuration, @Connection BasicAuthConnection connection, @DisplayName("URL") String url) throws ExecutionException, InterruptedException {

    HttpClient client = connection.getHttpClient();
    HttpRequestBuilder builder = HttpRequest.builder();
    builder.uri(url)
            .method(HttpConstants.Method.GET)
            .addHeader(CONTENT_TYPE, MediaType.APPLICATION_JSON);
    CompletableFuture<HttpResponse> response =  client.sendAsync(builder.build(),300, false, connection.getAuthentication());
    InputStream inputStream = response.get().getEntity().getContent();
    return inputStream;
  }

}
