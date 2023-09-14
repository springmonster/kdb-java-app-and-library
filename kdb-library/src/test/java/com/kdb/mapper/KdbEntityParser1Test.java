package com.kdb.mapper;

import com.kdb.model.Stock;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class KdbEntityParser1Test {

  @Test
  void createTable() {
    String table = KdbEntityGenerator.createTable(Stock.class);
    Assertions.assertEquals("stock", table);

    Assertions.assertThrows(NullPointerException.class, () -> {
      KdbEntityGenerator.createTable(null);
    });
  }

  @Test
  void createColumns() {
  }

  @Test
  void createRows() {
  }
}
