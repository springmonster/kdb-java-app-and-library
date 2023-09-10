package com.kdb.connection;

import com.google.inject.Singleton;
import java.time.Duration;
import kx.c;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

@Singleton
public class KdbConnectionPoolConfig extends GenericObjectPoolConfig<c> {

  private KdbConnectionPoolConfig(KdbConnectionPoolConfigBuilder builder) {
    this.setMaxTotal(builder.maxTotal);
    this.setMaxIdle(builder.maxIdle);
    this.setMinIdle(builder.minIdle);
    this.setMaxWait(Duration.ofSeconds(builder.maxWait));
  }

  public static class KdbConnectionPoolConfigBuilder {

    private int maxTotal;
    private int maxIdle;
    private int minIdle;
    private int maxWait;

    public KdbConnectionPoolConfigBuilder setMaxTotal(int maxTotal) {
      this.maxTotal = maxTotal;
      return this;
    }

    public KdbConnectionPoolConfigBuilder setMaxIdle(int maxIdle) {
      this.maxIdle = maxIdle;
      return this;
    }

    public KdbConnectionPoolConfigBuilder setMinIdle(int minIdle) {
      this.minIdle = minIdle;
      return this;
    }

    public KdbConnectionPoolConfigBuilder setMaxWait(int maxWait) {
      this.maxWait = maxWait;
      return this;
    }

    public KdbConnectionPoolConfig build() {
      return new KdbConnectionPoolConfig(this);
    }
  }
}
