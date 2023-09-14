package com.kuanghc.entity;


import com.google.gson.annotations.SerializedName;
import com.kdb.annotation.Column;
import com.kdb.entity.BaseEntity;

public record EC1EntityNoAnnotation(
    @Column("city")
    @SerializedName("city")
    String city,
    @Column("country")
    @SerializedName("country")
    String country,
    @Column("pop")
    @SerializedName("pop")
    Long pop
) implements BaseEntity {

}
