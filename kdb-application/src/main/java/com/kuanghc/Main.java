package com.kuanghc;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.kdb.KdbModule;
import com.kdb.connection.KdbConnection;
import com.kdb.mapper.KdbEntityGenerator;
import com.kdb.mapper.KdbEntityParser;
import com.kuanghc.entity.EC1Entity;
import com.kuanghc.entity.EC1Response;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import kx.c;
import org.apache.commons.collections4.CollectionUtils;

public class Main {

  public static void main(String[] args) throws Exception {

    Injector injector = initGuice();

    KdbConnection kdbConnection = injector.getInstance(KdbConnection.class);

    createTableThenRetrieve(kdbConnection);

    insertDataThenRetrieve(kdbConnection);
  }

  private static Injector initGuice() {
    return Guice.createInjector(new AbstractModule() {
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

        install(new KdbModule(properties, "com.kuanghc.entity"));
      }
    });
  }

  private static void createTableThenRetrieve(KdbConnection kdbConnection) {
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
  }

  private static void insertDataThenRetrieve(KdbConnection kdbConnection) throws Exception {
    List<EC1Entity> ec1ModelList = createEC1ModelList();

    if (CollectionUtils.isEmpty(ec1ModelList)) {
      throw new Exception("insert data must not be null!");
    }

    String table = KdbEntityGenerator.createTable(ec1ModelList.get(0).getClass());
    System.out.println(table);

    String[] columns = KdbEntityGenerator.createColumns(ec1ModelList.get(0).getClass());
    System.out.println(Arrays.toString(columns));

    Object[] rows = KdbEntityGenerator.createRows(createEC1ModelList(), EC1Entity.class);
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

  private static List<EC1Entity> createEC1ModelList() {
    return List.of(
        new EC1Entity("a", "aa", System.currentTimeMillis()),
        new EC1Entity("b", "bb", System.currentTimeMillis()),
        new EC1Entity("c", "cc", System.currentTimeMillis()),
        new EC1Entity("d", "dd", System.currentTimeMillis()),
        new EC1Entity("e", "ee", System.currentTimeMillis()),
        new EC1Entity("f", "ff", System.currentTimeMillis())
    );
  }
}
