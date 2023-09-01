package com.kuanghc.model;


import com.kdb.annotation.Column;
import com.kdb.annotation.Table;

@Table("ec1")
public record EC1Model(
    @Column("city")
    String city,
    @Column("country")
    String country,
    @Column("pop")
    Long pop
) {

}
