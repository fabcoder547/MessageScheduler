package com.app.controllers;

import com.app.exceptions.SQLErrorException;
import com.app.pojo.Client;
import com.app.pojo.Request;
import com.app.pojo.Response;
import com.app.service.Messageservice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/schedule/")
public class MessageController {


    Logger logger = LoggerFactory.getLogger(MessageController.class);
    @Autowired
    Messageservice messageservice;


    /*this route is for testing puropse...
         METHOD - GET
         URL -  /schedule/test
    */
    @GetMapping("/test")
    public Response testRoute() {
        logger.info("in tesing route");
        return new Response(200, "test sucessful..");
    }


    /* This is the controller for scheduling whatsapp message
        METHOD - POST
        URL  - /schedule/message
     */
    @PostMapping("/message")
    public Response scheduleMessageHandler(@RequestBody @Valid Request requestBody, HttpServletRequest request) {
        Response response = null;

        logger.info("request for scheduling message-> " + requestBody.toString());

        try {
            Client client = (Client) request.getAttribute("client");
            logger.info("client: " + client);
            int result = messageservice.saveMessage(requestBody, client);
            logger.info("result here..." + result);
            response = new Response(1000, "Message scheduled successfully");

        } catch (SQLErrorException e) {
            logger.warn("sql exception occured");
            response = new Response(e.getErrorCode(), e.getErrorMessage());
        } catch (Exception e) {
            logger.warn("exception here " + e.getMessage());
            response = new Response(1004, "something went wrong!!");
        }
        return response;

    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    Response onMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        logger.info("validation exception occured...");
        FieldError fieldError = e.getBindingResult().getFieldErrors().get(0);
        String errorMessage = fieldError.getDefaultMessage();
        Response response = new Response(1002, errorMessage);
        return response;
    }


}



























