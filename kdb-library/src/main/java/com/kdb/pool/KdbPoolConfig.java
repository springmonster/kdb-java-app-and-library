package com.kdb.pool;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.kdb.connection.KdbConfig;
import kx.c;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

@Singleton
public class KdbPoolConfig extends GenericObjectPoolConfig<c> {

    @Inject
    public KdbPoolConfig(KdbConfig kdbConfig) {
        this.setMaxTotal(kdbConfig.getMaxTotal());
        this.setMaxIdle(kdbConfig.getMaxIdle());
        this.setMinIdle(kdbConfig.getMinIdle());
        this.setMaxWait(kdbConfig.getMaxWait());
        this.setTimeBetweenEvictionRuns(kdbConfig.getTimeBetweenEvictionRuns());
        this.setTestWhileIdle(kdbConfig.isTestWhileIdle());
        this.setJmxEnabled(kdbConfig.isJmxEnabled());
    }
}
