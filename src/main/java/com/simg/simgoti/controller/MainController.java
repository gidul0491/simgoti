package com.simg.simgoti.controller;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/simg")
public class MainController {
    @RequestMapping("")
    public String index() throws Exception {
        return "index.html";
    }

//    @RequestMapping(value = "/apply")
//    public String apply() throws Exception {
//        return "applyTravelType.html";
//    }
//
//    @RequestMapping(value = "/applyTravelPeriod")
//    public String applyTravelPeriod() throws Exception {
//        return "applyTravelPeriod.html";
//    }
//
//    @RequestMapping(value = "/applyInsuredPerson")
//    public String applyInsuredPerson() throws Exception {
//        return "applyInsuredPerson.html";
//    }

    @RequestMapping( "/simgOti")
    public String simgOti() throws Exception {
        return "/simgOti.html";
    }

    @RequestMapping(value = "/simgOti/calculate", method = RequestMethod.GET)
    public String calculate() throws Exception {
        return "/calculate.html";
    }

    @RequestMapping(value = "/simgOti/applyA", method = RequestMethod.GET)
    public String applyA() throws Exception {
        return "/applyAlone.html";
    }

    @RequestMapping(value = "/simgOti/applyPurpose", method = RequestMethod.GET)
    public String applyPurpose() throws Exception {
        return "/applyPurpose.html";
    }

}
