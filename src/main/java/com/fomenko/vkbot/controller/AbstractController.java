package com.fomenko.vkbot.controller;

import com.fomenko.vkbot.model.Model;
import com.fomenko.vkbot.views.View;

public abstract class AbstractController implements Controller {
    protected Model model;
    protected View view;
    private String resourcePath;
    @Override
    public void setModel(Model model) {
        this.model = model;
    }

    @Override
    public void setView(View view) {
        this.view = view;
    }

    @Override
    public void start() {
        view.show();
    }

    @Override
    public void setResourcePath(String resourcePath){
        this.resourcePath=resourcePath;
    };

    public String getResourcePath(){
        return resourcePath;
    };










}