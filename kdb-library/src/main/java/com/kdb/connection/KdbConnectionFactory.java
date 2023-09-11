package com.kdb.connection;

import java.io.IOException;
import kx.c;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

public class KdbConnectionFactory extends BasePooledObjectFactory<c> {

  private final KdbConfig kdbConfig;

  public KdbConnectionFactory(KdbConfig kdbConfig) {
    this.kdbConfig = kdbConfig;
  }

  @Override
  public c create() {
    c kdbConnection;
    try {
      kdbConnection = new c(kdbConfig.getHost(), kdbConfig.getPort(), kdbConfig.getCredentials());
      System.out.println("KdbConnectionFactory create: " + kdbConnection);
    } catch (kx.c.KException | IOException e) {
      throw new RuntimeException(e);
    }
    return kdbConnection;
  }

  @Override
  public PooledObject<c> wrap(c kdbConnection) {
    return new DefaultPooledObject<>(kdbConnection);
  }

  @Override
  public void destroyObject(PooledObject<c> p) throws Exception {
    c kdbConnection = p.getObject();
    if (kdbConnection != null && kdbConnection.s != null && kdbConnection.s.isConnected()) {
      System.out.println("KdbConnectionFactory destroyObject: " + kdbConnection);
      kdbConnection.close();
    }
  }

  @Override
  public boolean validateObject(PooledObject<c> p) {
    try {
      c kdbConnection = p.getObject();
      System.out.println("KdbConnectionFactory validateObject: " + kdbConnection);
      if (kdbConnection.s == null || !kdbConnection.s.isConnected()) {
        System.out.println(
            "KdbConnectionFactory validateObject: " + kdbConnection + " is not connected");
        return false;
      }
      kdbConnection.k("1");
      System.out.println(
          "KdbConnectionFactory validateObject: " + kdbConnection + " is connected "
              + kdbConnection.s.isConnected());
    } catch (c.KException | IOException e) {
      return false;
    }
    return true;
  }
}
