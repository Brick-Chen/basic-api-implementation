package com.thoughtworks.rslist.dto;

import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Setter
@Getter
public class UserDto {
    @NotNull
    @Size(max = 8)
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
