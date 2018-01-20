package com.fomenko.vkbot.controller;

import com.fomenko.vkbot.model.Model;
import com.fomenko.vkbot.views.View;

public interface Controller {

    void setModel(Model model);

    void setView(View view);

    void start();


    void setResourcePath(String resoursePath);

    String getResourcePath();


}