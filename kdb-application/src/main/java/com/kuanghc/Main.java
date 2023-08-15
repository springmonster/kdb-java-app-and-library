package com.kuanghc;

import com.google.inject.Module;
import com.google.inject.*;
import com.kdb.connection.KdbConfig;
import com.kdb.connection.KdbConnection;
import com.kdb.convert.KdbInsertConverter;
import com.kdb.convert.KdbRetrieveConverter;
import com.kuanghc.model.EC1Model;
import com.kuanghc.model.EC1Response;
import kx.c;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Main {

    public static void main(String[] args) throws UnsupportedEncodingException {
        Injector injector = Guice.createInjector(new Module() {
            @Override
            public void configure(Binder binder) {
            }

            @Provides
            KdbConfig kdbConfig() {
                KdbConfig kdbConfig = new KdbConfig();
                kdbConfig.setHost("localhost");
                kdbConfig.setPort(5001);
                kdbConfig.setUsername("");
                kdbConfig.setPassword("");
                return kdbConfig;
            }
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

        String[] columns = KdbInsertConverter.createColumns(EC1Model.class);
        System.out.println(Arrays.toString(columns));

        Object[] rows = KdbInsertConverter.createRows(createEC1ModelList(), EC1Model.class);
        System.out.println(Arrays.deepToString(rows));

        Object objects = new Object[]{
                ".u.upd".toCharArray(),
                "ec1",
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
                new EC1Model("e", "ee", System.currentTimeMillis())
        );
    }
}
