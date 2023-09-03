package com.kdb.connection;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import com.google.inject.Singleton;

@Singleton
public class KdbConfig {

  private final String host;

  private final int port;

  private final String username;

  private final String password;

  private KdbConfig(String host, int port, String username, String password) {
    this.host = host;
    this.port = port;
    this.username = username;
    this.password = password;
  }

  public static KdbConfig create(String host, int port, String username, String password) {
    checkNotNull(host);
    checkArgument(port > 0 && port < 65536, "port %s must be between 1 and 65535", port);
    return new KdbConfig(host, port, username, password);
  }

  public String getHost() {
    return host;
  }

  public int getPort() {
    return port;
  }

  public String getCredentials() {
    return username + ":" + password;
  }
}
