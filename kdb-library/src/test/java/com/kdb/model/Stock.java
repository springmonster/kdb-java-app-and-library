package com.kdb.model;

import com.kdb.annotation.Column;
import com.kdb.annotation.Table;
import com.kdb.entity.BaseEntity;
import java.sql.Timestamp;

@Table("stock")
public class Stock implements BaseEntity {

  @Column("id")
  private final int id;

  @Column("createdTimestamp")
  private final Timestamp createdTimestamp;

  public Stock(int id, Timestamp createdTimestamp) {
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
