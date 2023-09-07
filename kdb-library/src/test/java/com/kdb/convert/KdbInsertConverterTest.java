package com.kdb.convert;

import com.kdb.model.Stock;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class KdbInsertConverterTest {

  private final KdbInsertConverter kdbInsertConverter = new KdbInsertConverter();

  @Test
  void createTable() {
    String table = kdbInsertConverter.createTable(Stock.class);
    Assertions.assertEquals("stock", table);

    Assertions.assertThrows(NullPointerException.class, () -> {
      kdbInsertConverter.createTable(null);
    });
  }

  @Test
  void createColumns() {
  }

  @Test
  void createRows() {
  }
}
