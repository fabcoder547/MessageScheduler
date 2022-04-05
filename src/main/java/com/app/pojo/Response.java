package com.app.pojo;

public class Response {

    public Integer getCode() {
        return code;
    }

    @Override
    public String toString() {
        return "Response{" +
                "code=" + code +
                ", Message='" + Message + '\'' +
                '}';
    }

    public Response(Integer code, String message) {
        this.code = code;
        Message = message;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    private Integer code;

    private String Message;


    public Response() {

    }



}
