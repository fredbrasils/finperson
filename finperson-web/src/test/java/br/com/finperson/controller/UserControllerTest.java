package br.com.finperson.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import br.com.finperson.core.service.UserService;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

	@Mock
    UserService userService;

    @InjectMocks
    UserController userController;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(userController)
                .build();
    }

    @DisplayName(value="Show the registration form's page")
    @Test
    void showRegistrationForm() throws Exception {
    	
        mockMvc.perform(get("/user/registration"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/registration"))
                .andExpect(model().attributeExists("user"));

    }

    @DisplayName(value="Register a new user")
    @Test
    void registerUserAccount() throws Exception {
    	
        mockMvc.perform(post("/user/registration"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));

    }
    
}
