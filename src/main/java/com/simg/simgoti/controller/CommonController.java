package com.simg.simgoti.controller;

import com.simg.simgoti.service.ClientService;
import com.simg.simgoti.service.CommonService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/common")
@RequiredArgsConstructor
public class CommonController {
    private final CommonService commonService;
    @RequestMapping(value = "/countryList", method = RequestMethod.GET)
    public Object getCountryList() throws Exception{
        return commonService.getCountryList();
    }
}
