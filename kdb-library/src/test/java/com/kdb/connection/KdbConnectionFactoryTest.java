package com.kdb.connection;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.kdb.annotation.ReadOnly;
import com.kdb.annotation.WriteOnly;
import kx.c;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class KdbConnectionFactoryTest {

  private Injector injector;

  @BeforeEach
  void setUp() {
    injector = Guice.createInjector(binder -> {
      KdbConfig kdbConfig = KdbConfig.create("localhost", 5001, "", "");

      binder.bind(KdbConfig.class).annotatedWith(WriteOnly.class)
          .toInstance(kdbConfig);
      binder.bind(KdbConfig.class).annotatedWith(ReadOnly.class)
          .toInstance(kdbConfig);

      KdbConnectionPoolConfig kdbConnectionPoolConfig =
          new KdbConnectionPoolConfig.KdbConnectionPoolConfigBuilder()
              .setMaxIdle(10)
              .setMaxTotal(10)
              .setMinIdle(1)
              .setMaxWait(10)
              .build();

      binder.bind(KdbConnectionPoolConfig.class).annotatedWith(WriteOnly.class)
          .toInstance(kdbConnectionPoolConfig);
      binder.bind(KdbConnectionPoolConfig.class).annotatedWith(ReadOnly.class)
          .toInstance(kdbConnectionPoolConfig);
    });
  }

  @Test
  void should_query_success() {
    KdbConnection kdbConnection = injector.getInstance(KdbConnection.class);
    Object o = kdbConnection.syncExecute("1+1");
    assertEquals((long) 2, o);
  }

  @Test
  void should_create_table_success() {
    KdbConnection kdbConnection = injector.getInstance(KdbConnection.class);
    kdbConnection.syncExecute(
        "test1:([]city:`Istanbul`Moscow`London`StPetersburg;country:`Turkey`Russia`UK`Russia;pop:15067724 12615279 9126366 5383890)");

    Object o = kdbConnection.syncExecute("test1");

    c.Flip flip = (c.Flip) o;
    System.out.println(flip);
    assertEquals(3, flip.y.length);
  }
}
