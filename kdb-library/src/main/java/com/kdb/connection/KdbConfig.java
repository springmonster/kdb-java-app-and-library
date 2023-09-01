package com.kdb.connection;

import com.google.inject.Singleton;

@Singleton
public class KdbConfig {

  private final String host;

  private final int port;

  private final String username;

  private final String password;

  public KdbConfig(String host, int port, String username, String password) {
    this.host = host;
    this.port = port;
    this.username = username;
    this.password = password;
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
