package com.thoughtworks.rslist.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
public class RsEvent {
    @NotEmpty
    private String eventName;

    @NotEmpty
    private String keyword;

    @NotNull
    private int userId;

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getEventName(){
        return eventName;
    }

    public String getKeyword() {
        return keyword;
    }

    @JsonProperty
    public void setUserId(int userId) {
        this.userId = userId;
    }

    @JsonIgnore
    public int getUserId() {
        return userId;
    }
}
