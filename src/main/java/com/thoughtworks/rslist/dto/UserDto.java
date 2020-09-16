package com.thoughtworks.rslist.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
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

    @Pattern(regexp = "^1\\d{10}$")
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

    public UserDto(@Valid UserDto other) {
        this.name = other.getName();
        this.age = other.getAge();
        this.gender = other.getGender();
        this.email = other.getEmail();
        this.phone = other.getPhone();
    }

    public boolean equals(Object o) {
        if (!(o instanceof UserDto)) {
            return false;
        }
        if (this == o) {
            return true;
        }
        UserDto other = (UserDto) o;
        return this.getName().equals(((UserDto) o).getName());
    }
}
