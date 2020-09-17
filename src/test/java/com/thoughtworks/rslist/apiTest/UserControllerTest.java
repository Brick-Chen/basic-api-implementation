package com.thoughtworks.rslist.apiTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.dto.UserDto;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;

        @Test
    public void should__register_when_user_info_is_valid() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        UserDto userDto =
                new UserDto("chen", 22, "male", "xxx@123.com", "15297134217");
        String userJson = objectMapper.writeValueAsString(userDto);
        mockMvc.perform(post("/user/register")
                .content(userJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        List<UserEntity> users = userRepository.findAll();
        Assertions.assertEquals(1, users.size());
        Assertions.assertEquals("chen", users.get(0).getUserName());
        Assertions.assertEquals(22, users.get(0).getAge());
        Assertions.assertEquals("male", users.get(0).getGender());
        Assertions.assertEquals("xxx@123.com", users.get(0).getEmail());
        Assertions.assertEquals("15297134217", users.get(0).getPhone());
    }

    @Test
    public void should_get_user_when_input_user_id() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        UserEntity user = UserEntity.builder()
                .userName("chen")
                .age(22)
                .gender("male")
                .email("xxx@123.com")
                .phone("15297134217")
                .build();
        userRepository.save(user);

        Integer id = user.getId();
        String url = "/users/" + id;
        mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userName" ,is("chen")))
                .andExpect(jsonPath("$.age" ,is(22)))
                .andExpect(jsonPath("$.gender" ,is("male")))
                .andExpect(jsonPath("$.email" ,is("xxx@123.com")))
                .andExpect(jsonPath("$.phone" ,is("15297134217")));
    }


//    @Test
//    public void should_not_register_when_user_name_is_empty() throws Exception {
//        ObjectMapper objectMapper = new ObjectMapper();
//        UserDto userDto =
//                new UserDto("", 20, "female", "xxx@123.com", "10123456789");
//        String userJson = objectMapper.writeValueAsString(userDto);
//        mockMvc.perform(post("/user/register")
//                .content(userJson)
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    public void should_not_register_when_user_name_is_above_8_characters() throws Exception {
//        ObjectMapper objectMapper = new ObjectMapper();
//        UserDto userDto =
//                new UserDto("123456789", 20, "female", "xxx@123.com", "10123456789");
//        String userJson = objectMapper.writeValueAsString(userDto);
//        mockMvc.perform(post("/user/register")
//                .content(userJson)
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    public void should_not_register_when_user_gender_is_null() throws Exception {
//        ObjectMapper objectMapper = new ObjectMapper();
//        UserDto userDto =
//                new UserDto("chen", 20, null, "xxx@123.com", "10123456789");
//        String userJson = objectMapper.writeValueAsString(userDto);
//        mockMvc.perform(post("/user/register")
//                .content(userJson)
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    public void should_not_register_when_user_age_is_null() throws Exception {
//        ObjectMapper objectMapper = new ObjectMapper();
//        UserDto userDto =
//                new UserDto("chen", null, "male", "xxx@123.com", "10123456789");
//        String userJson = objectMapper.writeValueAsString(userDto);
//        mockMvc.perform(post("/user/register")
//                .content(userJson)
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    public void should_not_register_when_user_age_is_below_18() throws Exception {
//        ObjectMapper objectMapper = new ObjectMapper();
//        UserDto userDto =
//                new UserDto("chen", 17, "male", "xxx@123.com", "10123456789");
//        String userJson = objectMapper.writeValueAsString(userDto);
//        mockMvc.perform(post("/user/register")
//                .content(userJson)
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    public void should_not_register_when_user_age_is_above_100() throws Exception {
//        ObjectMapper objectMapper = new ObjectMapper();
//        UserDto userDto =
//                new UserDto("chen", 101, "male", "xxx@123.com", "10123456789");
//        String userJson = objectMapper.writeValueAsString(userDto);
//        mockMvc.perform(post("/user/register")
//                .content(userJson)
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    public void should_not_register_when_user_email_is_invalid() throws Exception {
//        ObjectMapper objectMapper = new ObjectMapper();
//        UserDto userDto =
//                new UserDto("chen", 22, "male", "xxx@", "10123456789");
//        String userJson = objectMapper.writeValueAsString(userDto);
//        mockMvc.perform(post("/user/register")
//                .content(userJson)
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    public void should_not_register_when_user_phone_is_not_start_with_1() throws Exception {
//        ObjectMapper objectMapper = new ObjectMapper();
//        UserDto userDto =
//                new UserDto("chen", 22, "male", "xxx@123.com", "85297134217");
//        String userJson = objectMapper.writeValueAsString(userDto);
//        mockMvc.perform(post("/user/register")
//                .content(userJson)
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    public void should_not_register_when_user_phone_number_length_is_not_11() throws Exception {
//        ObjectMapper objectMapper = new ObjectMapper();
//        UserDto userDto =
//                new UserDto("chen", 22, "male", "xxx@123.com", "152971342179");
//        String userJson = objectMapper.writeValueAsString(userDto);
//        mockMvc.perform(post("/user/register")
//                .content(userJson)
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    public void should__register_when_user_info_is_valid() throws Exception {
//        ObjectMapper objectMapper = new ObjectMapper();
//        UserDto userDto =
//                new UserDto("chen", 22, "male", "xxx@123.com", "15297134217");
//        String userJson = objectMapper.writeValueAsString(userDto);
//        mockMvc.perform(post("/user/register")
//                .content(userJson)
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isCreated())
//                .andExpect(header().string("index", "2"));
//    }
//
//    @Test
//    public void should_return_users_list_with_expect_format() throws Exception {
//        mockMvc.perform(get("/users"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(1)))
//                .andExpect(jsonPath("$[0].user_name", is("admin")))
//                .andExpect(jsonPath("$[0].user_age", is(99)))
//                .andExpect(jsonPath("$[0].user_gender", is("male")))
//                .andExpect(jsonPath("$[0].user_email", is("admin@twu.com")))
//                .andExpect(jsonPath("$[0].user_phone", is("18888888888")));
//    }

}
