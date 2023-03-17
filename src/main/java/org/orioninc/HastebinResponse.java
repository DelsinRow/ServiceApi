package org.orioninc;

import com.fasterxml.jackson.annotation.JsonProperty;

public class HastebinResponse {
    private String key;

    public HastebinResponse() {
        super();
    }

    @JsonProperty("key")
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

}
