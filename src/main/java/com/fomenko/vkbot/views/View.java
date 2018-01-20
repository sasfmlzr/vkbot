package com.fomenko.vkbot.views;

import com.fomenko.vkbot.controller.Controller;
import com.fomenko.vkbot.model.Model;

public interface View {
    void dispose();

    void setController(Controller controller);

    void setModel(Model model);

    void show();


}
