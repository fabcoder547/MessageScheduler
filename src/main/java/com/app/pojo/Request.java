package com.app.pojo;

import com.app.annotations.ValidDateTime;
import com.app.annotations.phone;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;


public class Request {

    public Request() {
    }

    @NotNull
    @NotEmpty(message = "message should not be empty")
    private String message;

    public Request(String message, String phonenumber, String scheduledTime) {
        this.message = message;
        this.phonenumber = phonenumber;
        this.scheduledTime = scheduledTime;
    }

    //created a custom validation annoation for phone number
    @phone
    private String phonenumber;

    @ValidDateTime
    private String scheduledTime;


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getScheduledTime() {
        return scheduledTime;
    }

    public void setScheduledTime(String scheduledTime) {
        this.scheduledTime = scheduledTime;
    }

    @Override
    public String toString() {
        return "Request [message=" + message + ", phonenumber=" + phonenumber + ", scheduledTime=" + scheduledTime
                + "]";
    }


}
