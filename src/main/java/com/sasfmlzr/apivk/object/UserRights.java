package com.sasfmlzr.apivk.object;


import java.util.Objects;

public class UserRights {


    private Boolean allowWriteToBot=false;        // разрешение писать боту
    private Boolean adminCommands=false;          // разрешение на админские команды
    private String nameRight;


    public UserRights(String nameRight) {
        if (Objects.equals(nameRight, "Бог")){
            this.adminCommands=true;
            this.allowWriteToBot=true;
        }
        if (Objects.equals(nameRight, "Пользователь")){
            this.adminCommands=false;
            this.allowWriteToBot=true;
        }
        if (Objects.equals(nameRight, "Колян")){
            this.adminCommands=false;
            this.allowWriteToBot=false;
        }
        this.nameRight = nameRight;
    }


    public UserRights() {
    }


    public String getNameRight()
    {
        return nameRight;
    }

    public Boolean getAllowWriteToBot()
    {
        return allowWriteToBot;
    }

    public Boolean getAdminCommands()
    {
        return adminCommands;
    }



}


