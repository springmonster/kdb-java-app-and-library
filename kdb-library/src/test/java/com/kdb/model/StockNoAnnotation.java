package com.kdb.model;

import com.kdb.annotation.Column;
import com.kdb.entity.BaseEntity;
import java.sql.Timestamp;

public class StockNoAnnotation implements BaseEntity {

  @Column("id")
  private final int id;

  @Column("createdTimestamp")
  private final Timestamp createdTimestamp;

  public StockNoAnnotation(int id, Timestamp createdTimestamp) {
    this.id = id;
    this.createdTimestamp = createdTimestamp;
  }

  public int getId() {
    return id;
  }

  public Timestamp getCreatedTimestamp() {
    return createdTimestamp;
  }
}
