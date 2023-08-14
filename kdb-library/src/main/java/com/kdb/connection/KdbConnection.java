package com.kdb.connection;

import com.google.inject.Inject;
import com.kdb.config.KdbConfig;
import kx.c;

import java.io.IOException;

public class KdbConnection {

    private final KdbConfig kdbConfig;

    @Inject
    public KdbConnection(KdbConfig kdbConfig) {
        this.kdbConfig = kdbConfig;
    }

    public c getConnection() {
        c c;
        try {
            c = new c(kdbConfig.getHost(), kdbConfig.getPort(), kdbConfig.getCredentials());
        } catch (kx.c.KException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return c;
    }

    public Object executeSync(Object obj) {
        c c = getConnection();
        Object result;
        try {
            result = c.k(obj);
        } catch (kx.c.KException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                c.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return result;
    }

    public void executeAsync(Object obj) {
        c c = getConnection();
        try {
            c.ks(obj);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                c.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
