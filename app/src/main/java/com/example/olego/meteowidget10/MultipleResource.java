package com.example.olego.meteowidget10;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by olego on 20.11.2017.
 */

public class MultipleResource {

    @SerializedName("outside")
    public String outside;
    @SerializedName("inside")
    public String inside;
}
