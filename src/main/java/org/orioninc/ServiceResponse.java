package org.orioninc;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ServiceResponse(@JsonProperty("key")String key, String title, String source, String date){

}
