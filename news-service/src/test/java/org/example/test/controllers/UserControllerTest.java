package org.example.test.controllers;

import org.assertj.core.api.WithAssertions;
import org.example.dto.UserDto;
import org.example.mappers.NewsMapper;
import org.example.model.User;
import org.example.repository.UserRepository;
import org.example.services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class UserControllerTest extends AbstractControllerTest implements WithAssertions {

   @MockBean
    private UserService userService;

   @MockBean
   private UserRepository userRepository;

  @MockBean
   private NewsMapper newsMapper;

    @Test
    public void shouldCreateUser() throws Exception {


        UserDto  userRequest = createUserDto(null, "Alex", "Fadeev");

           String content = objectMapper.writeValueAsString(userRequest);

      when(userService.create(userRequest)).thenReturn(createUserDto("1", "Alex", "Fadeev"));

        String actualResponse = this.mockMvc.perform(post("/users/api/create")
               .contentType(MediaType.APPLICATION_JSON)
               .content(content))
               .andDo(print())
               .andExpect(status().isOk())
               .andReturn()
                .getResponse()
                .getContentAsString();

        String expectedResponse = loadJsonUserDtoResponse("C:\\Users\\User\\IdeaProjects\\spring\\news-service\\src\\test\\resources\\created_user_response..txt");

        verify(userService, times(1)).create(userRequest);

        assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    @Test
    public void whenGetAllUsersReturnListUsers() throws Exception {

        User user1 = createUser(1L, "Alex", "Fadeev");
        User user2 = createUser(2L, "Ivan", "Petrov");

        List<User> userList = new ArrayList<>();
        userList.add(user1);
        userList.add(user2);

        when(userService.findAll(PageRequest.of(0,10))).thenReturn(userList);

        String actualResponse = this.mockMvc.perform(MockMvcRequestBuilders.get("/users/api/list")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String expectedResponse = loadJsonUserResponse("C:\\Users\\User\\IdeaProjects\\spring\\news-service\\src\\test\\resources\\all_users_response.txt");

        verify(userService, times(1)).findAll(PageRequest.of(0,10));

        assertThat(actualResponse).isEqualTo(expectedResponse);
    }
    @Test
    public void whenUpdateUserReturnUser () throws Exception {

        UserDto userDto = createUserDto("1", "Alex", "Fadeev");

        when(this.userService.update(userDto)).thenReturn(userDto);

        String actualResponse = this.mockMvc.perform(put("/users/api/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String expectedResponse = loadJsonUserDtoResponse("C:\\Users\\User\\IdeaProjects\\spring\\news-service\\src\\test\\resources\\created_user_response..txt");

        verify(userService, times(1)).update(userDto);

        assertThat(actualResponse).isEqualTo(expectedResponse);
    }
    @Test
    public void whenDeleteUserReturnOk() throws Exception {

        this.mockMvc.perform(delete("/users/api/delete/{id}", "1"))
                .andExpect(status().isOk());

        verify(userService, times(1)).delete("1");
    }
}
