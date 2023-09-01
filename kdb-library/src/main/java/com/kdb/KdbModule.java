package com.kdb;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.matcher.Matchers;
import com.google.inject.name.Names;
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
    Names.bindProperties(binder(), this.properties);
  }

  @Provides
  @Singleton
  @WriteOnly
  KdbConfig writeOnlyKdbConfig() {
    KdbConfig kdbConfig = new KdbConfig();
    kdbConfig.setHost(properties.getProperty("writeOnly.kdb.hostname"));
    kdbConfig.setPort(Integer.parseInt(properties.getProperty("writeOnly.kdb.port")));
    kdbConfig.setUsername(properties.getProperty("writeOnly.kdb.username"));
    kdbConfig.setPassword(properties.getProperty("writeOnly.kdb.password"));
    return kdbConfig;
  }

  @Provides
  @Singleton
  @ReadOnly
  KdbConfig readOnlyKdbConfig() {
    KdbConfig kdbConfig = new KdbConfig();
    kdbConfig.setHost(properties.getProperty("readOnly.kdb.hostname"));
    kdbConfig.setPort(Integer.parseInt(properties.getProperty("readOnly.kdb.port")));
    kdbConfig.setUsername(properties.getProperty("readOnly.kdb.username"));
    kdbConfig.setPassword(properties.getProperty("readOnly.kdb.password"));
    return kdbConfig;
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
