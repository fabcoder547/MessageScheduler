package com.app.controllers;

import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.app.service.Messageservice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import com.app.dao.MessageDao;
import com.app.exceptions.SQLErrorException;

import com.app.pojo.Client;
import com.app.pojo.Request;
import com.app.pojo.Response;

@RestController
@RequestMapping("/schedule/")
public class MessageController {


    Logger logger = LoggerFactory.getLogger(MessageController.class);
    @Autowired
    Messageservice messageservice;


    @GetMapping("/test")

    public Response testRoute(){
        String requestId = UUID.randomUUID().toString();
        logger.info("in tesing route");
        return new Response(requestId,200,"test sucessful..");
    }





    /* This is the controller for scheduling whatsapp message */
    @PostMapping("/message")
    public Response scheduleMessageHandler(@RequestBody @Valid Request requestBody, HttpServletRequest request) {
        Response response = null;
        String requestId =  UUID.randomUUID().toString();
       logger.info("request for scheduling message-> "+requestId);

        try {
            Client client = (Client) request.getAttribute("client");
            //if client is null...authentication is failed
            if(client == null){
                response = new Response(requestId, 1001, "Authentication failed..");
                return response;
            }
          logger.info("client: "+client.toString());
            int result = messageservice.saveMessage(requestBody, client);
                response = new Response(requestId, 1000, "Message schedules successfully");

        } catch (SQLErrorException e) {
            logger.info("sql exception occured");
            response = new Response(requestId, e.getErrorCode(), e.getErrorMessage());
        } catch (Exception e) {
            logger.info("exception here "+e.getMessage());
            response = new Response(requestId, 1004, "something went wrong!!");
        }
        return response;

    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    Response onMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String requestId =  UUID.randomUUID().toString();
//        System.out.println(e);
//        System.out.println(e.getBindingResult().toString());
//        System.out.println(e.getBindingResult().getFieldErrors().toString());
        FieldError fieldError = e.getBindingResult().getFieldErrors().get(0);
        String errorMessage = fieldError.getDefaultMessage();
//		for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
//			System.out.println("here... " + fieldError.getField() + " " + fieldError.getDefaultMessage());
//		}
        Response response = new Response(requestId, 1002, errorMessage);

        return response;
    }


}



























