package com.simg.simgoti.service;

import com.simg.simgoti.dto.Pageable;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public interface ExcelService {
    XSSFWorkbook createInsListExcel(String filePath, Character useYN, Pageable pageable) throws Exception;
}
