package com.thoughtworks.rslist.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;

@Setter
@Getter
public class UserDto {
    @NotEmpty
    @Size(max = 8)
    private String name;

    @NotNull
    @Min(18)
    @Max(100)
    private Integer age;

    @NotEmpty
    private String gender;

    @Email
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
