package org.orioninc;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class StackOverflowItemsArray {
    List<StackOverFlowItemsWrapper> items;

    public StackOverflowItemsArray() {
        super();
    }
    @JsonProperty("items")
    public List<StackOverFlowItemsWrapper> getItems() {
        return items;
    }
    public void setItems(List<StackOverFlowItemsWrapper> items) {
        this.items = items;
    }
}
