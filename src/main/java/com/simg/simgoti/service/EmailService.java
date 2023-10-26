package com.simg.simgoti.service;

import org.apache.pdfbox.pdmodel.PDDocument;

public interface EmailService {
    PDDocument createPdfKr(int aplPk) throws Exception;
    void sendApplyMail(int aplPk,String title, String email, String fileName) throws Exception;
    String selectEmailByAplPk(int aplPk) throws Exception;
    void sendPaymentMail(String title, String email, String accBank, String accName, String accNum, String dueDt, int prem) throws Exception;
    void sendClaimMail(String title, String email) throws Exception;
}
