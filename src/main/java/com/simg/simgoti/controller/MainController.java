package com.simg.simgoti.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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

    @RequestMapping(value = "/simgOti/calculate", method = RequestMethod.GET)
    public String calculate() throws Exception {
        return "/calculate.html";
    }

    @RequestMapping(value = "/simgOti/applyAlone", method = RequestMethod.GET)
    public String applyAlone() throws Exception {
        return "/apply1Alone.html";
    }

    @RequestMapping(value = "/simgOti/applyMany", method = RequestMethod.GET)
    public String applyMany() throws Exception {
        return "/apply1Many.html";
    }

    @RequestMapping(value = "/simgOti/applyPurpose", method = RequestMethod.GET)
    public String applyPurpose() throws Exception {
        return "/apply2.html";
    }

    @RequestMapping(value = "/simgOti/applyFinal", method = RequestMethod.GET)
    public String applyFinal(HttpServletRequest req, HttpServletResponse resp) throws Exception {

        return "/apply3.html";
    }

}
