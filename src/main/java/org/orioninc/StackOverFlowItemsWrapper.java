package org.orioninc;

import com.fasterxml.jackson.annotation.JsonProperty;

public class StackOverFlowItemsWrapper {
    private String creationDate;
    private String questionId;
    private String title;

    public StackOverFlowItemsWrapper() {
        super();
    }

    @JsonProperty("creation_date")
    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    @JsonProperty("question_id")
    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    @JsonProperty("title")
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void asd() {

    }
}
