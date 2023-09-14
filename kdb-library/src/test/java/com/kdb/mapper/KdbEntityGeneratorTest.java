package com.kdb.mapper;

import com.kdb.model.Stock;
import com.kdb.model.StockNotImplementBaseEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class KdbEntityGeneratorTest {

  @Test
  void createTable() {
    String table = KdbEntityGenerator.createTable(Stock.class);
    Assertions.assertEquals("stock", table);

    Assertions.assertThrows(IllegalArgumentException.class,
        () -> KdbEntityGenerator.createTable(StockNotImplementBaseEntity.class));
  }

  @Test
  void createColumns() {
  }

  @Test
  void createRows() {
  }
}
