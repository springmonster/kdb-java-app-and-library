package com.kdb.connection;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.kdb.config.KdbConfig;
import kx.c;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class KdbConnectionTest {

    @Test
    void getConnection() {
        Injector injector = Guice.createInjector(binder -> {
            KdbConfig kdbConfig = new KdbConfig();
            kdbConfig.setHost("localhost");
            kdbConfig.setPort(5001);

            binder.bind(KdbConfig.class).toInstance(kdbConfig);
        });

        KdbConnection kdbConnection = injector.getInstance(KdbConnection.class);
        assertNotNull(kdbConnection.getConnection());
    }

    @Test
    void executeSync() {
        Injector injector = Guice.createInjector(binder -> {
            KdbConfig kdbConfig = new KdbConfig();
            kdbConfig.setHost("localhost");
            kdbConfig.setPort(5001);

            binder.bind(KdbConfig.class).toInstance(kdbConfig);
        });

        KdbConnection kdbConnection = injector.getInstance(KdbConnection.class);
        Object o = kdbConnection.executeSync("1+1");
        assertEquals((long) 2, o);
    }

    @Test
    void executeAsync() {
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
        assertEquals(2, flip.y.length);
    }
}
