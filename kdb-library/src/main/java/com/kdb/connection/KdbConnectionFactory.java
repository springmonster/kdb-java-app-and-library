package com.kdb.connection;

import com.kdb.connection.KdbConfig;
import kx.c;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

import java.io.IOException;

public class KdbConnectionFactory extends BasePooledObjectFactory<c> {
    private final KdbConfig kdbConfig;

    public KdbConnectionFactory(KdbConfig kdbConfig) {
        this.kdbConfig = kdbConfig;
    }

    @Override
    public c create() {
        c c;
        try {
            c = new c(kdbConfig.getHost(), kdbConfig.getPort(), kdbConfig.getCredentials());
        } catch (kx.c.KException | IOException e) {
            throw new RuntimeException(e);
        }
        return c;
    }

    @Override
    public PooledObject<c> wrap(c c) {
        return new DefaultPooledObject<>(c);
    }

    @Override
    public void destroyObject(PooledObject<c> p) throws Exception {
        c object = p.getObject();
        if (object != null) {
            object.close();
        }
    }

    @Override
    public boolean validateObject(PooledObject<c> p) {
        try {
            c c = p.getObject();
            if (c.s == null || !c.s.isConnected()) {
                return false;
            }
            c.k("1");
        } catch (c.KException | IOException e) {
            return false;
        }
        return true;
    }
}
