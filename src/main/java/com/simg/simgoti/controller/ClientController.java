package com.simg.simgoti.controller;

import com.simg.simgoti.service.ClientService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/client")
@RequiredArgsConstructor
public class ClientController{
    private final ClientService clientService;
//    @RequestMapping(value = "/list", method = RequestMethod.GET)
//    public Object selectClientList() throws Exception{
//        return clientService.selectClientList();
//    }

    @RequestMapping(value = "/coverageList", method = RequestMethod.GET)
    public Object selectCoverageList(@RequestParam int travelDay) throws Exception {
        Map<String, List> result = new HashMap<>();
        result.put("coverageList",clientService.selectCoverageList());
        result.put("coverageTabList", clientService.selectCoverageTypeList(travelDay));
        return result;
    }

    @RequestMapping(value = "/addCalSession", method = RequestMethod.POST)
    public void addCalSession(@RequestParam String startDt, @RequestParam String endDt, @RequestParam String birth, @RequestParam String gender, @RequestParam String country, @RequestParam String coverage, HttpServletRequest req){
        HttpSession session = req.getSession();
        session.setAttribute("startDt", startDt);
        session.setAttribute("endDt", endDt);
        session.setAttribute("birth", birth);
        session.setAttribute("gender", gender);
        session.setAttribute("country", country);
        session.setAttribute("coverage", coverage);
        System.out.println(startDt);
    }

    @RequestMapping(value = "/addAppSession", method = RequestMethod.POST)
    public void addAppSession(@RequestParam String name, @RequestParam String resiA, @RequestParam String resiB, @RequestParam String phone, @RequestParam String email, @RequestParam int cnt, HttpServletRequest req){
        HttpSession session = req.getSession();
        session.setAttribute("name", name);
        session.setAttribute("resiA", resiA);
        session.setAttribute("resiB", resiB);
        session.setAttribute("phone", phone);
        session.setAttribute("email", email);
        session.setAttribute("cnt", cnt);
        System.out.println(phone);
    }
}
