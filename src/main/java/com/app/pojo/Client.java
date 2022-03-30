package com.app.pojo;

public class Client {

    public Client(int clientId, String clientName, String token) {
        super();
        this.clientId = clientId;
        this.clientName = clientName;
        this.token = token;
    }

    private int clientId;
    private String clientName;

    private String token;

    public Client() {

    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    @Override
    public String toString() {
        return "Client [clientId=" + clientId + ", clientName=" + clientName + ", token=" + token + "]";
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }


}
