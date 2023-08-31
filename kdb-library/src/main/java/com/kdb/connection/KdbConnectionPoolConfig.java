package com.kdb.connection;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import kx.c;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

@Singleton
public class KdbConnectionPoolConfig extends GenericObjectPoolConfig<c> {

    @Inject
    public KdbConnectionPoolConfig(KdbConfig kdbConfig) {
        this.setMaxTotal(kdbConfig.getMaxTotal());
        this.setMaxIdle(kdbConfig.getMaxIdle());
        this.setMinIdle(kdbConfig.getMinIdle());
        this.setMaxWait(kdbConfig.getMaxWait());
        this.setTimeBetweenEvictionRuns(kdbConfig.getTimeBetweenEvictionRuns());
        this.setTestWhileIdle(kdbConfig.isTestWhileIdle());
        this.setJmxEnabled(kdbConfig.isJmxEnabled());
    }
}
