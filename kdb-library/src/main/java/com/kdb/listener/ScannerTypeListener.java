package com.kdb.listener;

import com.google.inject.TypeLiteral;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;
import com.kdb.entity.BaseEntity;
import com.kdb.mapper.KdbEntityGenerator;
import java.util.List;
import org.reflections.Reflections;

public class ScannerTypeListener implements TypeListener {

  private final String pkg;

  public ScannerTypeListener(String pkg) {
    this.pkg = pkg;
  }

  @Override
  public <I> void hear(TypeLiteral<I> typeLiteral, TypeEncounter<I> typeEncounter) {
    Reflections reflections = new Reflections(pkg);
    List<Class<? extends BaseEntity>> entityClasses = reflections.getSubTypesOf(BaseEntity.class)
        .stream()
        .toList();

    for (Class<? extends BaseEntity> clazz : entityClasses) {
      KdbEntityGenerator.createTable(clazz);
      KdbEntityGenerator.createColumns(clazz);
    }
  }
}
