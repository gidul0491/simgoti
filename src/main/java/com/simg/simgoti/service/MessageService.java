package com.simg.simgoti.service;

import org.apache.pdfbox.pdmodel.PDDocument;

public interface MessageService {
    PDDocument createPdfKr(int aplPk) throws Exception;
    void sendApplyMail(int aplPk,String title, String email, String fileName) throws Exception;
}
