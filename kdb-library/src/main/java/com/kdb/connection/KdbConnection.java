package com.kdb.connection;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.kdb.annotation.ReadOnly;
import com.kdb.annotation.WriteOnly;
import javax.annotation.Nullable;
import kx.c;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPool;

@Singleton
public class KdbConnection {

  private ObjectPool<c> writeOnlyKdbConnectionPool;

  private ObjectPool<c> readOnlyKdbConnectionPool;

  @Inject
  public KdbConnection(@WriteOnly @Nullable KdbConfig writeOnlyKdbConfig,
      @WriteOnly @Nullable KdbConnectionPoolConfig writeOnlyKdbConnectionPoolConfig,
      @ReadOnly @Nullable KdbConfig readOnlyKdbConfig,
      @ReadOnly @Nullable KdbConnectionPoolConfig readOnlyKdbConnectionPoolConfig) {
    if (writeOnlyKdbConfig != null && writeOnlyKdbConnectionPoolConfig != null) {
      this.writeOnlyKdbConnectionPool = new GenericObjectPool<>(
          new KdbConnectionFactory(writeOnlyKdbConfig),
          writeOnlyKdbConnectionPoolConfig);
    }

    if (readOnlyKdbConfig != null && readOnlyKdbConnectionPoolConfig != null) {
      this.readOnlyKdbConnectionPool = new GenericObjectPool<>(
          new KdbConnectionFactory(readOnlyKdbConfig),
          readOnlyKdbConnectionPoolConfig);
    }
  }

  public Object syncExecute(Object obj) {
    Preconditions.checkNotNull(this.readOnlyKdbConnectionPool,
        "kdb read only configurations are null!");
    c kdbConnection = null;
    try {
      kdbConnection = this.readOnlyKdbConnectionPool.borrowObject();
      if (obj instanceof String) {
        return kdbConnection.k((String) obj);
      } else {
        return kdbConnection.k(obj);
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    } finally {
      if (kdbConnection != null) {
        try {
          this.readOnlyKdbConnectionPool.returnObject(kdbConnection);
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
      }
    }
  }

  public void asyncExecute(Object obj) {
    Preconditions.checkNotNull(this.writeOnlyKdbConnectionPool,
        "kdb write only configurations are null!");
    c kdbConnection = null;
    try {
      kdbConnection = this.writeOnlyKdbConnectionPool.borrowObject();
      kdbConnection.ks(obj);
      Object k = kdbConnection.k("");
      System.out.println("executeAsync result is " + k);
    } catch (Exception e) {
      throw new RuntimeException(e);
    } finally {
      if (kdbConnection != null) {
        try {
          this.writeOnlyKdbConnectionPool.returnObject(kdbConnection);
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
      }
    }
  }
}
