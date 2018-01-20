package com.apiVKmanual.object;


public class BotDatabase_IdRequest {


    private int id;
    public String response;


    public BotDatabase_IdRequest(int id, String response) {
        this.id = id;

        this.response = response;

    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }




}


