package com.basicauth.connector.internal;

import org.mule.runtime.api.connection.ConnectionException;
import org.mule.runtime.api.i18n.I18nMessageFactory;
import org.mule.runtime.api.lifecycle.Initialisable;
import org.mule.runtime.api.lifecycle.InitialisationException;
import org.mule.runtime.api.tls.TlsContextFactory;
import org.mule.runtime.extension.api.annotation.Expression;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.api.connection.ConnectionValidationResult;
import org.mule.runtime.api.connection.PoolingConnectionProvider;
import org.mule.runtime.api.connection.ConnectionProvider;
import org.mule.runtime.api.connection.CachedConnectionProvider;
import org.mule.runtime.extension.api.annotation.param.ParameterGroup;
import org.mule.runtime.extension.api.annotation.param.RefName;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;

import org.mule.runtime.extension.api.annotation.param.display.Placement;
import org.mule.runtime.extension.api.annotation.param.display.Summary;
import org.mule.runtime.http.api.HttpConstants;
import org.mule.runtime.http.api.HttpService;
import org.mule.runtime.http.api.client.HttpClient;
import org.mule.runtime.http.api.client.HttpClientConfiguration;
import org.mule.runtime.http.api.client.auth.HttpAuthentication;
import org.mule.runtime.http.api.client.auth.HttpAuthenticationBuilder;
import org.mule.runtime.http.api.tcp.TcpClientSocketProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

import static org.mule.runtime.api.meta.ExpressionSupport.NOT_SUPPORTED;
import static org.mule.runtime.extension.api.annotation.param.ParameterGroup.CONNECTION;


/**
 * This class (as it's name implies) provides connection instances and the funcionality to disconnect and validate those
 * connections.
 * <p>
 * All connection related parameters (values required in order to create a connection) must be
 * declared in the connection providers.
 * <p>
 * This particular example is a {@link PoolingConnectionProvider} which declares that connections resolved by this provider
 * will be pooled and reused. There are other implementations like {@link CachedConnectionProvider} which lazily creates and
 * caches connections or simply {@link ConnectionProvider} if you want a new connection each time something requires one.
 */
public class BasicAuthConnectionProvider<MetrcConnectionParameter> implements PoolingConnectionProvider<BasicAuthConnection> {

  private final Logger LOGGER = LoggerFactory.getLogger(BasicAuthConnectionProvider.class);

 /**
  * A parameter that is always required to be configured.
  */
 @Expression(NOT_SUPPORTED)
 @Placement(tab = "Security", order = 1)
 @Parameter
 @Optional(defaultValue = "HTTP")
 @DisplayName("Protocol")
 @Summary("Protocol to use for communication. Valid values are HTTP and HTTPS")
 private HttpConstants.Protocol protocol;



  @Parameter
  @DisplayName("Username")
  private String username;

  @Parameter
  @DisplayName("Password")
  private String password;

 @Inject
 private HttpService httpService;

 private HttpClient httpClient;

 @RefName
 private String configName;

 private HttpAuthentication authentication;

 @Override
 public BasicAuthConnection connect() throws ConnectionException {
      this.authentication = CreateAuthentication.createAuth(username, password);
      httpClient = httpService.getClientFactory().create(new HttpClientConfiguration.Builder()
              .setName(configName)
              .build());
      httpClient.start();
      return new BasicAuthConnection(httpClient, username, password, authentication);
 }



  @Override
  public void disconnect(BasicAuthConnection connection) {
    try {
      connection.invalidate();
    } catch (Exception e) {
      LOGGER.error("Error while disconnecting" + e.getMessage(), e);
    }
  }

  @Override
  public ConnectionValidationResult validate(BasicAuthConnection connection) {
    return ConnectionValidationResult.success();
  }
}
