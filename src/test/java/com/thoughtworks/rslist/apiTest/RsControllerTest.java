package com.thoughtworks.rslist.apiTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.dto.UserDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class RsControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    public void should_not_register_when_user_name_is_empty() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        UserDto userDto =
                new UserDto("", 20, "female", "xxx@123.com", "10123456789");
        String userJson = objectMapper.writeValueAsString(userDto);
        mockMvc.perform(post("/user/register")
                .content(userJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void should_not_register_when_user_name_is_above_8_characters() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        UserDto userDto =
                new UserDto("123456789", 20, "female", "xxx@123.com", "10123456789");
        String userJson = objectMapper.writeValueAsString(userDto);
        mockMvc.perform(post("/user/register")
                .content(userJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void should_not_register_when_user_gender_is_null() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        UserDto userDto =
                new UserDto("chen", 20, null, "xxx@123.com", "10123456789");
        String userJson = objectMapper.writeValueAsString(userDto);
        mockMvc.perform(post("/user/register")
                .content(userJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void should_not_register_when_user_age_is_null() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        UserDto userDto =
                new UserDto("chen", null, "male", "xxx@123.com", "10123456789");
        String userJson = objectMapper.writeValueAsString(userDto);
        mockMvc.perform(post("/user/register")
                .content(userJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void should_not_register_when_user_age_is_below_18() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        UserDto userDto =
                new UserDto("chen", 17, "male", "xxx@123.com", "10123456789");
        String userJson = objectMapper.writeValueAsString(userDto);
        mockMvc.perform(post("/user/register")
                .content(userJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void should_not_register_when_user_age_is_above_100() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        UserDto userDto =
                new UserDto("chen", 101, "male", "xxx@123.com", "10123456789");
        String userJson = objectMapper.writeValueAsString(userDto);
        mockMvc.perform(post("/user/register")
                .content(userJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
