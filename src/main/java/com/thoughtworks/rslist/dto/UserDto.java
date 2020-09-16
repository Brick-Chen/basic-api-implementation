package com.thoughtworks.rslist.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Setter
@Getter
public class UserDto {
    @NotEmpty
    @Size(max = 8)
    private String name;

    @NotNull
    private Integer age;

    @NotEmpty
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
