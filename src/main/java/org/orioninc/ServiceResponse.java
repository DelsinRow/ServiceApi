package org.orioninc;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ServiceResponse(String key, String title, String source, String date){

    @JsonProperty("key")
    public String getKey() {
        return key;
    }


}
