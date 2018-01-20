package com.apiVKmanual.object;


public class BotDatabase_IdRequestResponse {


    private int id;
    public String request;
    public String response;



    public BotDatabase_IdRequestResponse(int id, String request, String response) {
        this.id = id;
        this.request = request;
        this.response = response;

    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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


