package com.sasfmlzr.apivk.object;

public class BotDatabase_RequestResponse {
    public String request;
    public String response;

    public BotDatabase_RequestResponse(String request, String response) {
        this.request = request;
        this.response = response;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
