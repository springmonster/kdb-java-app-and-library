package com.kdb.mapper;

import com.kdb.model.Stock;
import com.kdb.model.StockNoAnnotation;
import com.kdb.model.StockNoAnnotationValue;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class KdbEntityGeneratorTest {

  @Test
  void createTable() {
    String table = KdbEntityGenerator.createTable(Stock.class);
    Assertions.assertEquals("stock", table);

    Assertions.assertThrows(IllegalArgumentException.class,
        () -> KdbEntityGenerator.createTable(StockNoAnnotation.class));

    Assertions.assertThrows(IllegalArgumentException.class,
        () -> KdbEntityGenerator.createTable(StockNoAnnotationValue.class));
  }

  @Test
  void createColumns() {
  }

  @Test
  void createRows() {
  }
}
