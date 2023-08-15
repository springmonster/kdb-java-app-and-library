package com.kdb.connection;

import com.google.inject.Singleton;

@Singleton
public class KdbConfig {

    private String host;

    private int port;

    private String username;

    private String password;

    public String getCredentials() {
        return username + ":" + password;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
