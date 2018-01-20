package com.apiVKmanual.object;


public class UserIdRightsBD {


    private int userID;
    private String login;
    private String nameRight;


    public UserIdRightsBD(String login, int userID, String nameRight) {
        this.userID = userID;
        this.login = login;
        this.nameRight = nameRight;
    }

    public UserIdRightsBD() {
    }

    public int getUserid() {
        return userID;
    }

    public void setUserid(int userID) {
        this.userID = userID;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getNameRight() {
        return nameRight;
    }

    public void setNameRight(String nameRight) {
        this.nameRight = nameRight;
    }



}


