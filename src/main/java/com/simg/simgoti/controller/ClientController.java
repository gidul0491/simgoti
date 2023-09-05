package com.simg.simgoti.controller;

import com.simg.simgoti.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/client")
@RequiredArgsConstructor
public class ClientController{
    private final ClientService clientService;
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Object selectClientList() throws Exception{
        return clientService.selectClientList();
    }
}
