package com.kuanghc.model;


import com.google.gson.annotations.SerializedName;
import com.kdb.annotation.Column;
import com.kdb.annotation.Table;

public record EC1Response(
        @SerializedName("city")
        String city,
        @SerializedName("country")
        String country,
        @SerializedName("pop")
        Long pop
) {

}
