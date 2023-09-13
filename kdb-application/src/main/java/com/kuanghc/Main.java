package com.kuanghc;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.kdb.KdbModule;
import com.kdb.connection.KdbConnection;
import com.kdb.mapper.KdbEntityGenerator;
import com.kdb.mapper.KdbEntityParser;
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

    Injector injector = Guice.createInjector(new AbstractModule() {
      @Override
      protected void configure() {
        Properties properties = new Properties();
        InputStream inputStream = KdbModule.class.getClassLoader()
            .getResourceAsStream("application-dev.properties");
        try {
          properties.load(inputStream);
        } catch (IOException e) {
          throw new RuntimeException(e);
        }

        install(new KdbModule(properties, "com.kuanghc.model"));
      }
    });

    KdbConnection kdbConnection = injector.getInstance(KdbConnection.class);

    kdbConnection.syncExecute("t1:([] `a`b`c; 1 2 3)");
    Object o = kdbConnection.syncExecute("t1");
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

    String table = KdbEntityGenerator.createTable(EC1Model.class);
    System.out.println(table);

    String[] columns = KdbEntityGenerator.createColumns(EC1Model.class);
    System.out.println(Arrays.toString(columns));

    Object[] rows = KdbEntityGenerator.createRows(createEC1ModelList(), EC1Model.class);
    System.out.println(Arrays.deepToString(rows));

    Object objects = new Object[]{
        ".u.upd1".toCharArray(),
        table,
        new c.Flip(new c.Dict(columns, rows))
    };
    kdbConnection.asyncExecute(objects);

    Object o1 = kdbConnection.syncExecute(table);
    List<Map<String, Object>> list = KdbEntityParser.convertToList(o1);
    System.out.println(list);
    List<EC1Response> ec1Models = KdbEntityParser.convertToList(list, EC1Response.class);
    System.out.println(ec1Models);
  }

  private static List<EC1Model> createEC1ModelList() {
    return List.of(
        new EC1Model("a", "aa", System.currentTimeMillis()),
        new EC1Model("b", "bb", System.currentTimeMillis()),
        new EC1Model("c", "cc", System.currentTimeMillis()),
        new EC1Model("d", "dd", System.currentTimeMillis()),
        new EC1Model("e", "ee", System.currentTimeMillis()),
        new EC1Model("f", "ff", System.currentTimeMillis())
    );
  }
}
