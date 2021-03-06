package com.thoughtworks.rslist.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RsEvent {
    @NotNull
    private String eventName;

    @NotNull
    private String keyword;

    @NotNull
    private int userId;

    private int id;

    private int voteNum;

    @Valid
    private UserDto user;

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

    @JsonIgnore
    public void setUser(UserDto user) {
        this.user = user;
    }

    @JsonProperty
    public UserDto getUser() {
        return user;
    }

    @JsonProperty
    public int getId() {
        return id;
    }

    @JsonIgnore
    public void setId(int id) {
        this.id = id;
    }

    public int getVoteNum() {
        return voteNum;
    }

    public void setVoteNum(int voteNum) {
        this.voteNum = voteNum;
    }
}
