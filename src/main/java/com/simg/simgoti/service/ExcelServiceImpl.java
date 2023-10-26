package com.simg.simgoti.service;

import com.simg.simgoti.dto.AdminInsSumDto;
import com.simg.simgoti.dto.Pageable;
import com.simg.simgoti.mapper.AdminMapper;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExcelServiceImpl implements ExcelService{
    private final AdminMapper adminMapper;
    @Override
    public XSSFWorkbook createInsListExcel(String filePath, Character useYN, Pageable pageable) throws Exception{
        try{
            List<AdminInsSumDto> list =  adminMapper.selectAdminInsSumDtoList(useYN, pageable.getStart(), pageable.getSize(), pageable.getOrderBy());

            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet("기명요청");
            int rowNo = 0;

            Row headerRow = sheet.createRow(rowNo++);
            headerRow.createCell(0).setCellValue("가입번호");
            headerRow.createCell(1).setCellValue("고객명");
            headerRow.createCell(2).setCellValue("여행지");
            headerRow.createCell(3).setCellValue("담보유형코드");
            headerRow.createCell(4).setCellValue("담보유형명");
            headerRow.createCell(5).setCellValue("피보험자수");
            headerRow.createCell(6).setCellValue("보험료");
            headerRow.createCell(7).setCellValue("가입신청일시");
            headerRow.createCell(8).setCellValue("가입상태");
            headerRow.createCell(9).setCellValue("결제여부");

            for(AdminInsSumDto dto : list){
                Row row = sheet.createRow(rowNo++);
                row.createCell(0).setCellValue(dto.getAplPk());
                row.createCell(1).setCellValue(dto.getClntNm());
                row.createCell(2).setCellValue(dto.getTrPlace());
                row.createCell(3).setCellValue(dto.getCovCode());
                row.createCell(4).setCellValue(dto.getCovNm());
                row.createCell(5).setCellValue(dto.getClntCnt());
                row.createCell(6).setCellValue(dto.getPremium());
                row.createCell(7).setCellValue(dto.getAplCdt());
                row.createCell(8).setCellValue(dto.getAplState());
                row.createCell(9).setCellValue(dto.getPayedYN());
            }

            return workbook;
        }
        catch (Exception e){System.out.println(e); return null;}

    }
}
