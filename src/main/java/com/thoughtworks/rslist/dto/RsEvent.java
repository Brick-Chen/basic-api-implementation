package com.thoughtworks.rslist.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RsEvent {
    @NotNull
    private String eventName;

    @NotNull
    private String keyword;

    @NotNull
    @Valid
    private UserDto user;
}
