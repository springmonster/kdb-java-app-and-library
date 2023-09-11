package com.kdb.integration;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.kdb.KdbModule;
import com.kdb.connection.KdbConnection;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

@Disabled
public class IntegrationTest {

  @Test
  void should_get_connection_success() throws InterruptedException {
    Injector injector = Guice.createInjector(new AbstractModule() {
      @Override
      protected void configure() {
        Properties properties = new Properties();
        InputStream inputStream = KdbModule.class.getClassLoader()
            .getResourceAsStream("application-test.properties");
        try {
          properties.load(inputStream);
        } catch (IOException e) {
          throw new RuntimeException(e);
        }

        install(new KdbModule(properties, "com.kuanghc.model"));
      }
    });

    KdbConnection kdbConnection = injector.getInstance(KdbConnection.class);

    Thread t1 = new Thread(() -> {
      while (true) {
        try {
          Thread.sleep(1000);
          System.out.println("kdbConnection: " + kdbConnection);
          Object o = kdbConnection.syncExecute("t1");
          System.out.println("thread 1 o: " + o);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    });
    t1.start();

    Thread t2 = new Thread(() -> {
      while (true) {
        try {
          Thread.sleep(1000);
          System.out.println("kdbConnection: " + kdbConnection);
          Object o = kdbConnection.syncExecute("t1");
          System.out.println("thread 2 o: " + o);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    });
    t2.start();

    t1.join();
    t2.join();
  }
}
