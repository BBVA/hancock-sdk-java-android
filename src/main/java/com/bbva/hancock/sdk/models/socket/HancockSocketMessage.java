package com.bbva.hancock.sdk.models.socket;

import com.google.gson.JsonObject;
import lombok.Data;

import java.io.Serializable;

@Data
public class HancockSocketMessage implements Serializable {

    private static final long serialVersionUID = -5868311404867886960L;
    private String kind;
    private JsonObject raw;
    private String matchedAddress;

}
