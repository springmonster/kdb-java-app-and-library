package com.kdb.model;

import com.google.gson.annotations.SerializedName;

public record EC1Response(
    @SerializedName("city")
    String city,
    @SerializedName("country")
    String country,
    @SerializedName("pop")
    Long pop
) {

}
