package com.app.controllers;

import com.app.exceptions.SQLErrorException;
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
import static org.mockito.ArgumentMatchers.any;
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


    public static ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void authenticationTest() throws Exception {
        Request request = new Request("test message", "7972757302", "2022-04-30T15:45:20");
        String jsonString = objectMapper.writeValueAsString(request);
        MvcResult result = mvc.perform(post("/schedule/message").contentType(MediaType.APPLICATION_JSON_VALUE).content(jsonString).accept(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isBadRequest()).andReturn();

        String actualResponseString = result.getResponse().getContentAsString();
        Response response = objectMapper.readValue(actualResponseString, Response.class);

        //responce code for authentication failed - 1001
        assertThat(response.getCode()).isEqualTo(1001);
    }


    //test for - Invalid token passes as header...........
    @Test
    void authenticationTestForInvalidToken() throws Exception {
        Request request = new Request("test message", "7972757302", "2022-04-30T15:45:20");
        String jsonString = objectMapper.writeValueAsString(request);
        when(authService.validateToken(any())).thenReturn(null);
        MvcResult result = mvc.perform(post("/schedule/message").header("token", "Invalid token").contentType(MediaType.APPLICATION_JSON_VALUE).content(jsonString).accept(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isBadRequest()).andReturn();

        String actualResponseString = result.getResponse().getContentAsString();
        Response response = objectMapper.readValue(actualResponseString, Response.class);
        System.out.println(response.toString());
        //responce code for authentication failed - 1001
        assertThat(response.getCode()).isEqualTo(1001);
    }


    //test for invalid phone number here............
    @Test
    void validationTestForPhoneNumber() throws Exception {
        Client dummyclient = new Client(101, "dummy", "dummytoken");
        when(authService.validateToken("dummytoken")).thenReturn(dummyclient);
        //phone number is missing....
        Request request = new Request("test message", "", "2022-04-30T15:45:20");
        String jsonString = objectMapper.writeValueAsString(request);
        MvcResult result = mvc.perform(post("/schedule/message").contentType(MediaType.APPLICATION_JSON_VALUE).header("token", "dummytoken").content(jsonString).accept(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isOk()).andReturn();

        String actualResponseString = result.getResponse().getContentAsString();
        Response response = objectMapper.readValue(actualResponseString, Response.class);

        //responce code for non-valid request body - 1002
        //expected message = "phone number is invalid"
        assertThat(response.getCode()).isEqualTo(1002);
        assertThat(response.getMessage() == "phone number is invalid");
    }


    //test for invalid schedule time.....
    @Test
    void validationTestForScheduledTime() throws Exception {
        Client dummyclient = new Client(101, "dummy", "dummytoken");
        when(authService.validateToken("dummytoken")).thenReturn(dummyclient);
        //Invalid scheduledTime will be sent.......
        Request request = new Request("test message", "7972757302", "2022-04-30Invalid");
        String jsonString = objectMapper.writeValueAsString(request);
        MvcResult result = mvc.perform(post("/schedule/message").contentType(MediaType.APPLICATION_JSON_VALUE).header("token", "dummytoken").content(jsonString).accept(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isOk()).andReturn();

        String actualResponseString = result.getResponse().getContentAsString();
        Response response = objectMapper.readValue(actualResponseString, Response.class);

        //responce code for non-valid request body - 1002
        //expected message = "Invalid date format"
        assertThat(response.getCode()).isEqualTo(1002);
        assertThat(response.getMessage() == "Invalid date format");
    }


    //test for invalid message string.....
    @Test
    void validationTestForMessage() throws Exception {
        Client dummyclient = new Client(101, "dummy", "dummytoken");
        when(authService.validateToken("dummytoken")).thenReturn(dummyclient);
        //empty message will be sent.........
        Request request = new Request("test message", "7972757302", "2022-04-30Invalid");
        String jsonString = objectMapper.writeValueAsString(request);
        MvcResult result = mvc.perform(post("/schedule/message").contentType(MediaType.APPLICATION_JSON_VALUE).header("token", "dummytoken").content(jsonString).accept(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isOk()).andReturn();

        String actualResponseString = result.getResponse().getContentAsString();
        Response response = objectMapper.readValue(actualResponseString, Response.class);

        //responce code for non-valid request body - 1002
        //expected message = "message should not be empty"
        assertThat(response.getCode()).isEqualTo(1002);
        assertThat(response.getMessage() == "message should not be empty");
    }


    //test for - checking message is getting scheduled or not...
    @Test
    void scheduleMessageTest() throws Exception {
        Client dummyclient = new Client(10178, "dummy", "dummytoken");
        Request request = new Request("test message", "7972757302", "2022-04-30T15:45:20");
        when(messageservice.saveMessage(any(), any())).thenReturn(1);
        when(authService.validateToken("dummytoken")).thenReturn(dummyclient);

        String jsonString = objectMapper.writeValueAsString(request);
        MvcResult result = mvc.perform(post("/schedule/message").header("token", "dummytoken").contentType(MediaType.APPLICATION_JSON_VALUE).content(jsonString).accept(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isOk()).andReturn();

        String actualResponseString = result.getResponse().getContentAsString();
        Response response = objectMapper.readValue(actualResponseString, Response.class);
        //responce code for scheduling message succesfully - 1000
        assertThat(response.getCode()).isEqualTo(1000);

    }



    //test for - what if SQL exception occured.........
    @Test
    void scheduleMessageTestForSQLException() throws Exception {
        Client dummyclient = new Client(101, "dummy", "dummytoken");
        Request request = new Request("test message", "7972757302", "2022-04-30T15:45:20");

        when(messageservice.saveMessage(any(), any())).thenThrow(new SQLErrorException("sql error while inserting message"));
        when(authService.validateToken("dummytoken")).thenReturn(dummyclient);

        String jsonString = objectMapper.writeValueAsString(request);

        MvcResult result = mvc.perform(post("/schedule/message").header("token", "dummytoken").contentType(MediaType.APPLICATION_JSON_VALUE).content(jsonString).accept(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isOk()).andReturn();


        String actualResponseString = result.getResponse().getContentAsString();
        Response response = objectMapper.readValue(actualResponseString, Response.class);
        System.out.println(response.toString());
        //responce SQL exception response - 1003
        assertThat(response.getCode()).isEqualTo(1003);
    }


}