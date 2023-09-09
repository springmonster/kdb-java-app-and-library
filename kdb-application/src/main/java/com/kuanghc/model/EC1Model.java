package com.kuanghc.model;


import com.google.gson.annotations.SerializedName;
import com.kdb.annotation.Column;
import com.kdb.annotation.Table;

@Table("ec1")
public record EC1Model(
    @Column("city")
    @SerializedName("city")
    String city,
    @Column("country")
    @SerializedName("country")
    String country,
    @Column("pop")
    @SerializedName("pop")
    Long pop
) {

}
