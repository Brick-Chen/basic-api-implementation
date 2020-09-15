package com.thoughtworks.rslist.dto;

public class RsEvent {
    private String eventName;
    private String keyword;

    public RsEvent() {

    }

    public RsEvent(String eventName, String keyword) {
        this.eventName = eventName;
        this.keyword = keyword;
    }

    public String getEventName() {
        return this.eventName;
    }

    public String getKeyword() {
        return this.keyword;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

}
