package com.kdb.convert;

import com.kdb.model.Stock;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class KdbEntityParser1Test {

  private final KdbEntityGenerator kdbEntityGenerator = new KdbEntityGenerator();

  @Test
  void createTable() {
    String table = kdbEntityGenerator.createTable(Stock.class);
    Assertions.assertEquals("stock", table);

    Assertions.assertThrows(NullPointerException.class, () -> {
      kdbEntityGenerator.createTable(null);
    });
  }

  @Test
  void createColumns() {
  }

  @Test
  void createRows() {
  }
}
