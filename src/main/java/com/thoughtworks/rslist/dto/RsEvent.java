package com.thoughtworks.rslist.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RsEvent {
    private String eventName;
    private String keyword;
    private UserDto user;
}
