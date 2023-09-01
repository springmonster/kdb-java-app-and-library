package com.kdb.connection;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.time.Duration;
import kx.c;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

@Singleton
public class KdbConnectionPoolConfig extends GenericObjectPoolConfig<c> {

  @Inject
  public KdbConnectionPoolConfig(int maxTotal, int maxIdle, int minIdle, int maxWait) {
    this.setMaxTotal(maxTotal);
    this.setMaxIdle(maxIdle);
    this.setMinIdle(minIdle);
    this.setMaxWait(Duration.ofMinutes(maxWait));
  }
}
