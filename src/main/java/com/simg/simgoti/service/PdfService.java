package com.simg.simgoti.service;

import org.apache.pdfbox.pdmodel.PDDocument;

public interface PdfService {
    PDDocument createPdfKr(int aplPk) throws Exception;
    void sendMail(int aplPk, String email, String filePath) throws Exception;
}
