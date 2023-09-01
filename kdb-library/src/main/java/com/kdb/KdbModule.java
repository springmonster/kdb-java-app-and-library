package com.kdb;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.matcher.Matchers;
import com.kdb.annotation.ReadOnly;
import com.kdb.annotation.WriteOnly;
import com.kdb.connection.KdbConfig;
import com.kdb.connection.KdbConnectionPoolConfig;
import com.kdb.listener.ScannerTypeListener;
import java.util.Properties;

public class KdbModule extends AbstractModule {

  private final Properties properties;

  private final String scannerPackage;

  public KdbModule(Properties properties, String scannerPackage) {
    this.properties = properties;
    this.scannerPackage = scannerPackage;
  }

  @Override
  protected void configure() {
    binder().bindListener(Matchers.any(), new ScannerTypeListener(this.scannerPackage));
  }

  @Provides
  @Singleton
  @WriteOnly
  KdbConfig writeOnlyKdbConfig() {
    return new KdbConfig(
        properties.getProperty("writeOnly.kdb.hostname"),
        Integer.parseInt(properties.getProperty("writeOnly.kdb.port")),
        properties.getProperty("writeOnly.kdb.username"),
        properties.getProperty("writeOnly.kdb.password")
    );
  }

  @Provides
  @Singleton
  @ReadOnly
  KdbConfig readOnlyKdbConfig() {
    return new KdbConfig(
        properties.getProperty("readOnly.kdb.hostname"),
        Integer.parseInt(properties.getProperty("readOnly.kdb.port")),
        properties.getProperty("readOnly.kdb.username"),
        properties.getProperty("readOnly.kdb.password")
    );
  }

  @Provides
  @Singleton
  @WriteOnly
  KdbConnectionPoolConfig writeOnlyKdbConnectionPoolConfig() {
    return new KdbConnectionPoolConfig(
        Integer.parseInt(properties.getProperty("writeOnly.kdb.pool.maxTotal")),
        Integer.parseInt(properties.getProperty("writeOnly.kdb.pool.maxIdle")),
        Integer.parseInt(properties.getProperty("writeOnly.kdb.pool.minIdle")),
        Integer.parseInt(properties.getProperty("writeOnly.kdb.pool.maxWait"))
    );
  }

  @Provides
  @Singleton
  @ReadOnly
  KdbConnectionPoolConfig readOnlyKdbConnectionPoolConfig() {
    return new KdbConnectionPoolConfig(
        Integer.parseInt(properties.getProperty("readOnly.kdb.pool.maxTotal")),
        Integer.parseInt(properties.getProperty("readOnly.kdb.pool.maxIdle")),
        Integer.parseInt(properties.getProperty("readOnly.kdb.pool.minIdle")),
        Integer.parseInt(properties.getProperty("readOnly.kdb.pool.maxWait"))
    );
  }
}
