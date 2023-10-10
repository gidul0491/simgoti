package com.simg.simgoti.controller;

import com.simg.simgoti.dto.AdminInsSumDto;
import com.simg.simgoti.dto.Pageable;
import com.simg.simgoti.service.AdminService;
import com.simg.simgoti.service.ClientService;
import com.simg.simgoti.service.CommonService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {
    private final ClientService clientService;
    private final AdminService adminService;
    private final CommonService commonService;

    @RequestMapping(value = "/insList", method = RequestMethod.GET)
    public Object selectAdminInsSumDtoList(Character useYN, Pageable pageable) throws Exception {
        Pageable pageableStartCal = new Pageable(pageable.getPage(), pageable.getSize(), pageable.getOrderBy());
        System.out.println(pageableStartCal);
        List<AdminInsSumDto> list = adminService.selectAdminInsSumDtoList(useYN, pageableStartCal);

        return list;
    };

    @RequestMapping(value = "/payedYN", method = RequestMethod.PUT)
    public Object updateInsPayedYN(@RequestParam int aplPk, @RequestParam Character payedYN, @RequestParam Character useYN) throws Exception{
        adminService.updateInsPayedYN(aplPk, payedYN);
        return adminService.selectAdminInsSumDto(aplPk, useYN);
    }
}
