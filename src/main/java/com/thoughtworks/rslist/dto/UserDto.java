package com.thoughtworks.rslist.dto;

import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class UserDto {
    @NotNull
    private String name;
    private Integer age;
    private String gender;
    private String email;
    private String phone;

    public UserDto() {

    }

    public UserDto(String name, Integer age, String gender, String email, String phone) {
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.email = email;
        this.phone = phone;
    }
}
