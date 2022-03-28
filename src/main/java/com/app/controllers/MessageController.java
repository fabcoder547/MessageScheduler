package com.app.controllers;

import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.dao.MessageDao;
import com.app.exceptions.SQLErrorException;

import com.app.pojo.Client;
import com.app.pojo.Request;
import com.app.pojo.Response;

@RestController
@RequestMapping("/schedule/")
public class MessageController {

    @Autowired
    MessageDao messageDao;

    /* This is the controller for scheduling whatsapp message */
    @PostMapping("/message")
    public Response scheduleMessageHandler(@RequestBody @Valid Request requestBody, HttpServletRequest request) {
        Response response = null;
        String requestId =  UUID.randomUUID().toString();
        System.out.println("requestID is -->  "+ requestId);

        try {
            Client client = (Client) request.getAttribute("client");
            //if client is null...authentication is failed
            if(client == null){
                response = new Response(requestId, 1001, "Authentication failed..");
                return response;
            }

            System.out.println("client here  " + client.toString());
            int result = messageDao.insetMessage(requestBody, client);
            if (result > 0) {
                response = new Response(requestId, 200, "Message schedules successfully");
            } else {
                response = new Response(requestId, 1002, "Message is not scheduled successfully");
            }

        } catch (SQLErrorException e) {
            response = new Response(requestId, e.getErrorCode(), e.getErrorMessage());
        } catch (Exception e) {
            response = new Response(requestId, 1003, "something went wrong!!");
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




















