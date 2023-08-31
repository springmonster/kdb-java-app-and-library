package com.kdb.connection;

import com.google.inject.Inject;
import kx.c;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPool;

public class KdbConnection {

    ObjectPool<c> pool;

    @Inject
    public KdbConnection(KdbConfig kdbConfig) {
        this.pool = new GenericObjectPool<>(new KdbConnectionFactory(kdbConfig), new com.kdb.connection.KdbConnectionPoolConfig(kdbConfig));
    }

    public Object executeSync(String obj) {
        c kdbConnection = null;
        try {
            kdbConnection = pool.borrowObject();
            return kdbConnection.k(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (kdbConnection != null) {
                try {
                    pool.returnObject(kdbConnection);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public void executeAsync(Object obj) {
        c kdbConnection = null;
        try {
            kdbConnection = pool.borrowObject();
            kdbConnection.ks(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (kdbConnection != null) {
                try {
                    pool.returnObject(kdbConnection);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
