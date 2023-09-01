package com.kdb.connection;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.kdb.annotation.ReadOnly;
import com.kdb.annotation.WriteOnly;
import kx.c;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPool;

@Singleton
public class KdbConnection {

  private final ObjectPool<c> writeOnlyKdbConnectionPool;

  private final ObjectPool<c> readOnlyKdbConnectionPool;

  @Inject
  public KdbConnection(@WriteOnly KdbConfig writeOnlyKdbConfig,
      @WriteOnly KdbConnectionPoolConfig writeOnlyKdbConnectionPoolConfig,
      @ReadOnly KdbConfig readOnlyKdbConfig,
      @ReadOnly KdbConnectionPoolConfig readOnlyKdbConnectionPoolConfig) {
    this.writeOnlyKdbConnectionPool = new GenericObjectPool<>(
        new KdbConnectionFactory(writeOnlyKdbConfig),
        writeOnlyKdbConnectionPoolConfig);

    this.readOnlyKdbConnectionPool = new GenericObjectPool<>(
        new KdbConnectionFactory(readOnlyKdbConfig),
        readOnlyKdbConnectionPoolConfig);
  }

  public Object syncExecute(Object obj) {
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
