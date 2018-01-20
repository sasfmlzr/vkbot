package com.fomenko.vkbot.views;

import com.fomenko.vkbot.controller.Controller;
import com.fomenko.vkbot.model.Model;

public abstract class AbstractView implements View{
    private Model model;
    private Controller controller;
    private String resoursePath;
    @Override
    public void setController(Controller controller){
        this.controller=controller;
    };
    @Override
    public void setModel(Model model){
        this.model=model;
    };




}
