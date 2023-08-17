package com.kdb.connection;

import com.google.inject.Inject;
import com.kdb.pool.KdbConnectionFactory;
import com.kdb.pool.KdbPoolConfig;
import kx.c;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPool;

public class KdbConnection {

    ObjectPool<c> pool;

    @Inject
    public KdbConnection(KdbConfig kdbConfig) {
        this.pool = new GenericObjectPool<>(new KdbConnectionFactory(kdbConfig), new KdbPoolConfig(kdbConfig));
    }

    public Object executeSync(String obj) {
        c c = null;
        try {
            c = pool.borrowObject();
            return c.k(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (c != null) {
                try {
                    pool.returnObject(c);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public void executeAsync(Object obj) {
        c c = null;
        try {
            c = pool.borrowObject();
            c.ks(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (c != null) {
                try {
                    pool.returnObject(c);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
