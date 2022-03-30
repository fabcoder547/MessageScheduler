package com.app.controllers;

import com.app.pojo.Client;
import com.app.pojo.Request;
import com.app.pojo.Response;
import com.app.service.AuthService;
import com.app.service.Messageservice;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class MessageControllerTest {


    @MockBean
    Messageservice messageservice;
    @MockBean
    AuthService authService;
    @Autowired
    private MockMvc mvc;

    @Test
    void authenticationTest() throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();
        Request request = new Request("test message", "7972757302", "2022-04-30T15:45:20");

        String jsonString = objectMapper.writeValueAsString(request);
        MvcResult result = mvc.perform(
                post("/schedule/message")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(jsonString)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
        ).andExpect(status().isOk()).andReturn();

        String actualResponseString = result.getResponse().getContentAsString();
        Response response = objectMapper.readValue(actualResponseString, Response.class);
        //responce code for authentication failed - 1001
        assertThat(response.getCode()).isEqualTo(1001);
    }


    @Test
    void validationTest() throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();
        //phone number is missing....
        Request request = new Request("test message", "", "2022-04-30T15:45:20");

        String jsonString = objectMapper.writeValueAsString(request);
        MvcResult result = mvc.perform(
                post("/schedule/message")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(jsonString)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
        ).andExpect(status().isOk()).andReturn();


        String actualResponseString = result.getResponse().getContentAsString();
        Response response = objectMapper.readValue(actualResponseString, Response.class);
        //responce code for non-valid request body - 1002
        assertThat(response.getCode()).isEqualTo(1002);

    }


    @Test
    void scheduleMessageTest() throws Exception {
        Client dummyclient = new Client(101, "dummy", "dummytoken");

        when(messageservice.saveMessage(null, null)).thenReturn(1);
        when(authService.validateToken("dummytoken")).thenReturn(dummyclient);
        ObjectMapper objectMapper = new ObjectMapper();

        Request request = new Request("test message", "7972757302", "2022-04-30T15:45:20");

        String jsonString = objectMapper.writeValueAsString(request);
        MvcResult result = mvc.perform(
                post("/schedule/message")
                        .header("token", "dummytoken")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(jsonString)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
        ).andExpect(status().isOk()).andReturn();


        String actualResponseString = result.getResponse().getContentAsString();
        Response response = objectMapper.readValue(actualResponseString, Response.class);
        //responce code for scheduling message succesfully - 1000
        assertThat(response.getCode()).isEqualTo(1000);

    }


}