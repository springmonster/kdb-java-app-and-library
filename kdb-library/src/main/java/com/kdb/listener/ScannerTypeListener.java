package com.kdb.listener;

import com.google.inject.TypeLiteral;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;
import com.kdb.annotation.Table;
import com.kdb.convert.KdbEntityParser;
import org.reflections.Reflections;

public class ScannerTypeListener implements TypeListener {

  private final String pkg;

  public ScannerTypeListener(String pkg) {
    this.pkg = pkg;
  }

  @Override
  public <I> void hear(TypeLiteral<I> typeLiteral, TypeEncounter<I> typeEncounter) {
    Reflections reflections = new Reflections(pkg);
    var annotatedClasses = reflections.getTypesAnnotatedWith(Table.class);

    for (Class<?> clazz : annotatedClasses) {
      KdbEntityParser.createTable(clazz);
      KdbEntityParser.createColumns(clazz);
    }
  }
}
