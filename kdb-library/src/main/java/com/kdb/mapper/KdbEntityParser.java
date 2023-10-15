package com.kdb.mapper;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import kx.c;

/**
 * This class is used to parse kdb query result to entity
 */
public class KdbEntityParser {

  public static List<Map<String, Object>> convertToList(Object obj)
      throws UnsupportedEncodingException {
    List<Map<String, Object>> result;

    if (obj instanceof c.Flip flip) {
      result = convertFlip(flip);
    } else {
      throw new RuntimeException("Object is not a Flip");
    }
    return result;
  }

  private static List<Map<String, Object>> convertFlip(c.Flip flip)
      throws UnsupportedEncodingException {
    // Convert columns
    String[] columns = getColumns(flip);

    // Convert rows
    List<Object[]> rows = getRows(flip);

    // Combine columns and rows
    return combineColumnsAndRows(columns, rows);
  }

  private static String[] getColumns(c.Flip flip) {
    int length = flip.x.length;
    String[] columns = new String[length];
    System.arraycopy(flip.x, 0, columns, 0, length);
    return columns;
  }

  private static List<Object[]> getRows(c.Flip flip) throws UnsupportedEncodingException {
    List<Object[]> result = new ArrayList<>();
    int cols = flip.x.length;
    int rows = c.n(flip.y[0]);
    for (int i = 0; i < rows; i++) {
      Object[] row = new Object[cols];
      for (int j = 0; j < cols; j++) {
        row[j] = c.at(flip.y[j], i);
      }
      result.add(row);
    }
    return result;
  }

  private static List<Map<String, Object>> combineColumnsAndRows(String[] columns,
      List<Object[]> rows) {
    List<Map<String, Object>> result = new ArrayList<>();
    for (Object[] row : rows) {
      Map<String, Object> map = new java.util.HashMap<>();
      for (int i = 0; i < columns.length; i++) {
        map.put(columns[i], row[i]);
      }
      result.add(map);
    }
    return result;
  }
}
