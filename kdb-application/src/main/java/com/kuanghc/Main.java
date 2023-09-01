package com.kuanghc;

import com.google.inject.Binder;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.matcher.Matchers;
import com.google.inject.name.Names;
import com.kdb.annotation.ReadOnly;
import com.kdb.annotation.WriteOnly;
import com.kdb.connection.KdbConfig;
import com.kdb.connection.KdbConnection;
import com.kdb.connection.KdbConnectionPoolConfig;
import com.kdb.convert.KdbInsertConverter;
import com.kdb.convert.KdbRetrieveConverter;
import com.kdb.listener.ScannerTypeListener;
import com.kuanghc.model.EC1Model;
import com.kuanghc.model.EC1Response;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import kx.c;

public class Main {

  public static void main(String[] args) throws UnsupportedEncodingException {

    final Properties properties = new Properties();

    Injector injector = Guice.createInjector(new Module() {
      @Override
      public void configure(Binder binder) {
        binder.bindListener(Matchers.any(), new ScannerTypeListener("com.kuanghc.model"));

        Properties properties = getProperties();
        Names.bindProperties(binder, properties);
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

      @Provides
      @Singleton
      Properties getProperties() {
        try {
          InputStream resourceAsStream = this.getClass().getClassLoader()
              .getResourceAsStream("application.properties");
          properties.load(resourceAsStream);
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
        return properties;
      }
    });

    KdbConnection kdbConnection = injector.getInstance(KdbConnection.class);

    kdbConnection.executeSync("t1:([] `a`b`c; 1 2 3)");
    Object o = kdbConnection.executeSync("t1");
    c.Flip flip = (c.Flip) o;
    System.out.println(flip);
    Object[] x = flip.x;
    System.out.println(Arrays.toString(x));
    Object[] y = flip.y;
    Object[] y0 = (Object[]) y[0];
    long[] y1 = (long[]) y[1];
    System.out.println(Arrays.toString(y));
    System.out.println(Arrays.toString(y0));
    System.out.println(Arrays.toString(y1));

    String table = KdbInsertConverter.createTable(EC1Model.class);
    System.out.println(table);

    String[] columns = KdbInsertConverter.createColumns(EC1Model.class);
    System.out.println(Arrays.toString(columns));

    Object[] rows = KdbInsertConverter.createRows(createEC1ModelList(), EC1Model.class);
    System.out.println(Arrays.deepToString(rows));

    Object objects = new Object[]{
        ".u.upd".toCharArray(),
        table,
        new c.Flip(new c.Dict(columns, rows))
    };
    kdbConnection.executeAsync(objects);

    Object o1 = kdbConnection.executeSync("ec1");
    List<Map<String, Object>> list = KdbRetrieveConverter.convertToList(o1);
    System.out.println(list);
    List<EC1Response> ec1Models = KdbRetrieveConverter.convertToList(list, EC1Response.class);
    System.out.println(ec1Models);
  }

  private static List<EC1Model> createEC1ModelList() {
    return List.of(
        new EC1Model("a", "aa", System.currentTimeMillis()),
        new EC1Model("b", "bb", System.currentTimeMillis()),
        new EC1Model("c", "cc", System.currentTimeMillis()),
        new EC1Model("d", "dd", System.currentTimeMillis()),
        new EC1Model("e", "ee", System.currentTimeMillis()),
        new EC1Model("f", "ff", Long.MIN_VALUE)
    );
  }
}
