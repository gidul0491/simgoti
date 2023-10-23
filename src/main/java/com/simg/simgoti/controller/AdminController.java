package com.simg.simgoti.controller;

import com.simg.simgoti.dto.AdminInsSumDto;
import com.simg.simgoti.dto.Pageable;
import com.simg.simgoti.service.AdminService;
import com.simg.simgoti.service.ClientService;
import com.simg.simgoti.service.CommonService;
import com.simg.simgoti.service.EmailService;
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
    private final EmailService emailService;

    @RequestMapping(value = "/insList", method = RequestMethod.GET)
    public Object selectAdminInsSumDtoList(Character useYN, Pageable pageable) throws Exception {
        Pageable pageableStartCal = new Pageable(pageable.getPage(), pageable.getSize(), pageable.getOrderBy());
        System.out.println(pageableStartCal);
        List<AdminInsSumDto> list = adminService.selectAdminInsSumDtoList(useYN, pageableStartCal);

        return list;
    };

    @RequestMapping(value = "/payedYN", method = RequestMethod.PUT)
    public Object updateInsPayedYN(@RequestParam int aplPk, @RequestParam Character payedYN) throws Exception{
        adminService.updateInsPayedYN(aplPk, payedYN);
        if(payedYN.equals('Y')){ // payedYN을 Y로 바꾸는경우(결제되지 않은 상태에서 결제확인후 Y로 바꾸는 경우)
            String clntEmail = emailService.selectEmailByAplPk(aplPk);
            emailService.sendApplyMail(aplPk,"[SIMG 해외여행자보험] 가입이 완료되었습니다.", clntEmail, "가입증명서.pdf");
        }
        return adminService.selectAdminInsSumDto(aplPk);
    }

    @RequestMapping(value = "/stateCode", method = RequestMethod.PUT)
    public Object updateStateCode(@RequestParam int aplPk, @RequestParam int aplStateCode) throws Exception{
        adminService.updateInsStateCode(aplPk, aplStateCode);
        return adminService.selectAdminInsSumDto(aplPk);
    }
}
