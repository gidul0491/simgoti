package com.simg.simgoti.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class MainController {
    @RequestMapping(value = "/home")
    public String index() throws Exception {
        return "index.html";
    }

    @RequestMapping(value = "/apply")
    public String apply() throws Exception {
        return "applyTravelType.html";
    }

    @RequestMapping(value = "/applyTravelPeriod")
    public String applyTravelPeriod() throws Exception {
        return "applyTravelPeriod.html";
    }
}
