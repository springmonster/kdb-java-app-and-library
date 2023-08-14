package com.kuanghc;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.kdb.config.KdbConfig;
import com.kdb.connection.KdbConnection;
import com.kdb.convert.KdbConverter;
import com.kuanghc.model.EC1Model;
import kx.c;

import java.util.Arrays;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        Injector injector = Guice.createInjector(binder -> {
            KdbConfig kdbConfig = new KdbConfig();
            kdbConfig.setHost("localhost");
            kdbConfig.setPort(5001);

            binder.bind(KdbConfig.class).toInstance(kdbConfig);
        });

        KdbConnection kdbConnection = injector.getInstance(KdbConnection.class);

        kdbConnection.executeAsync("t1:([] `a`b`c; 1 2 3)");
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

        String[] columns = KdbConverter.createColumns(EC1Model.class);
        System.out.println(Arrays.toString(columns));

        Object[] rows = KdbConverter.createRows(createEC1ModelList(), EC1Model.class);
        System.out.println(Arrays.deepToString(rows));

        Object objects = new Object[]{
                ".u.upd".toCharArray(),
                "ec1",
                new c.Flip(new c.Dict(columns, rows))
        };
        kdbConnection.executeAsync(objects);
    }

    private static List<EC1Model> createEC1ModelList() {
        return List.of(
                new EC1Model("a", "aa", System.currentTimeMillis()),
                new EC1Model("b", "bb", System.currentTimeMillis()),
                new EC1Model("c", "cc", System.currentTimeMillis()),
                new EC1Model("d", "dd", System.currentTimeMillis()),
                new EC1Model("e", "ee", System.currentTimeMillis())
        );
    }
}
