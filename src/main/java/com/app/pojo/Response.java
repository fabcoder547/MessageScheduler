package com.app.pojo;

public class Response {


    private String requestId;

    private Integer code;

    private String Message;


    public Response() {

    }

    public Response(String requestId, Integer code, String message) {
        super();
        this.requestId = requestId;
        this.code = code;
        Message = message;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public Integer getCode() {
        return code;
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

}
