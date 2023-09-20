package com.simg.simgoti.controller;

import com.simg.simgoti.service.ClientService;
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


}
