package com.simg.simgoti.controller;

import com.simg.simgoti.dto.AdminClaimDto;
import com.simg.simgoti.dto.AdminInsSumDto;
import com.simg.simgoti.dto.ClaimDto;
import com.simg.simgoti.dto.Pageable;
import com.simg.simgoti.service.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {
    private final ClientService clientService;
    private final AdminService adminService;
    private final CommonService commonService;
    private final EmailService emailService;
    private final ExcelService excelService;

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

    @RequestMapping(value = "/claimList", method = RequestMethod.GET)
    public Object selectClaimDtoList(Character useYN, Pageable pageable) throws Exception {
        Pageable pageableStartCal = new Pageable(pageable.getPage(), pageable.getSize(), pageable.getOrderBy());
        System.out.println(pageableStartCal);
        List<AdminClaimDto> list = adminService.selectClaimDtoList(useYN, pageableStartCal);

        return list;
    };

    @RequestMapping(value = "/insList/download", method = RequestMethod.GET)
    public void selectClaimDtoList(@RequestParam char useYn, Pageable pageable, HttpServletRequest req, HttpServletResponse resp) throws Exception {
        Pageable pageableStartCal = new Pageable(pageable.getPage(), pageable.getSize(), pageable.getOrderBy());
        String filePath = System.getProperty("user.dir") + File.separator + "tempXxls" + File.separator + "application_list.xlsx";

        XSSFWorkbook workbook = excelService.createInsListExcel(filePath, useYn,pageableStartCal);
        File xlsFile = new File(filePath);
        FileOutputStream fileOut = new FileOutputStream(xlsFile);
        workbook.write(fileOut);
        fileOut.close();

        byte[] files = FileUtils.readFileToByteArray(new File(filePath));

        resp.setContentType("application/octet-stream");
        resp.setContentLength(files.length);
        resp.setHeader("Content-Disposition","attachment;fileName=\""+ URLEncoder.encode("기명요청목록.xlsx","UTF-8")+"\"");

        resp.getOutputStream().write(files);
        resp.getOutputStream().flush();
        resp.getOutputStream().close();

        // 생성한 PDF 파일 삭제
        File generatedPdf = new File(filePath);
        if (generatedPdf.exists()) {
            generatedPdf.delete();
        }
    }
}
