package com.kdb.connection;

import com.google.inject.Inject;
import com.kdb.annotation.ReadOnly;
import com.kdb.annotation.WriteOnly;
import kx.c;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPool;

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

  public Object executeSync(String obj) {
    c kdbConnection = null;
    try {
      kdbConnection = this.readOnlyKdbConnectionPool.borrowObject();
      return kdbConnection.k(obj);
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

  public void executeAsync(Object obj) {
    c kdbConnection = null;
    try {
      kdbConnection = this.writeOnlyKdbConnectionPool.borrowObject();
      kdbConnection.ks(obj);
      kdbConnection.k("");
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
