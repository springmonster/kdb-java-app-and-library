package com.kdb.convert;

import com.kdb.model.Stock;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class KdbEntityParserTest {

  private final KdbEntityParser kdbEntityParser = new KdbEntityParser();

  @Test
  void createTable() {
    String table = kdbEntityParser.createTable(Stock.class);
    Assertions.assertEquals("stock", table);

    Assertions.assertThrows(NullPointerException.class, () -> {
      kdbEntityParser.createTable(null);
    });
  }

  @Test
  void createColumns() {
  }

  @Test
  void createRows() {
  }
}
