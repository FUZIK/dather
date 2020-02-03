package com.example.dather.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Weather {

    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("icon")
    @Expose
    private String owmIconUri;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOwmIconUri() {
        return owmIconUri;
    }

    public void setOwmIconUri(String owmIconUri) {
        this.owmIconUri = owmIconUri;
    }

}