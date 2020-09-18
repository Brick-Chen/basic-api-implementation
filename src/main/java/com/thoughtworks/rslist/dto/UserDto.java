package com.thoughtworks.rslist.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.io.Serializable;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto implements Serializable {
    @NotEmpty
    @Size(max = 8)
    @JsonProperty("user_name")
    private String name;

    @NotNull
    @Min(18)
    @Max(100)
    @JsonProperty("user_age")
    private Integer age;

    @NotEmpty
    @JsonProperty("user_gender")
    private String gender;

    @Email
    @JsonProperty("user_email")
    private String email;

    @Pattern(regexp = "^1\\d{10}$")
    @JsonProperty("user_phone")
    private String phone;

//    public UserDto() {
//
//    }

//    public UserDto(String name, Integer age, String gender, String email, String phone) {
//        this.name = name;
//        this.age = age;
//        this.gender = gender;
//        this.email = email;
//        this.phone = phone;
//    }
//
//    public UserDto(@Valid UserDto other) {
//        this.name = other.getName();
//        this.age = other.getAge();
//        this.gender = other.getGender();
//        this.email = other.getEmail();
//        this.phone = other.getPhone();
//    }

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
