package com.simg.simgoti.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class MainController {
    @GetMapping(value = "/")
    public String index() throws Exception {
        return "index.html";
    }
}
