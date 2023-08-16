package com.kdb.listener;

import com.google.inject.TypeLiteral;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;
import com.kdb.annotation.Table;
import com.kdb.convert.KdbInsertConverter;
import org.reflections.Reflections;

import java.util.Set;

public class ScannerTypeListener implements TypeListener {

    private final String pkg;

    public ScannerTypeListener(String pkg) {
        this.pkg = pkg;
    }

    @Override
    public <I> void hear(TypeLiteral<I> typeLiteral, TypeEncounter<I> typeEncounter) {
        Reflections reflections = new Reflections(pkg);
        Set<Class<?>> annotatedClasses = reflections.getTypesAnnotatedWith(Table.class);

        for (Class<?> clazz : annotatedClasses) {
            KdbInsertConverter.createTable(clazz);
            KdbInsertConverter.createColumns(clazz);
        }
    }
}
