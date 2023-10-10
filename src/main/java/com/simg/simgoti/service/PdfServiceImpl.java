package com.simg.simgoti.service;

import com.simg.simgoti.dto.MyPageInsSummaryDto;
import com.simg.simgoti.dto.PdfInsInfoDto;
import com.simg.simgoti.mapper.ClientMapper;
import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.springframework.stereotype.Service;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.IOException;
import java.util.Properties;


@Service
@RequiredArgsConstructor
public class PdfServiceImpl implements PdfService {
    private final ClientMapper clientMapper;

    @Override
    public PDDocument createPdfKr(int aplPk) throws Exception {
        // 새로운 PDF 문서 생성
        PDDocument document = new PDDocument();
        PDType0Font nanumGothic = PDType0Font.load(document, getClass().getResourceAsStream("/static/fonts/NanumGothicCoding.ttf"));
        // 새로운 페이지 생성
        PDPage page = new PDPage(PDRectangle.A4);
        document.addPage(page);

        // pk값으로 계약서 정보 가져오기
        PdfInsInfoDto dto = clientMapper.selectInsForPdf(aplPk);
        try {
            // 페이지 컨텐츠 스트림 생성
            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            // 텍스트 추가
//            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
//            contentStream.beginText();
//            contentStream.newLineAtOffset(100, 700);
//            contentStream.showText("Hello, PDFBox!");
//            contentStream.showText(String.valueOf(aplPk));
//            contentStream.endText();

            // 표 시작 위치와 셀 크기 설정
            float margin = 50;
            float yStart = page.getMediaBox().getHeight() - margin;
            float tableWidth = page.getMediaBox().getWidth() - 2 * margin;
            float yPosition = yStart;
            int rows = 9;
            int cols = 2;
            float rowHeight = 20f;
            float tableHeight = rowHeight * rows;
            float colWidth = tableWidth / (float) cols;
            float cellMargin = 5f;

            // 셀 내용 배열 생성
            String[][] content = new String[rows][cols];
//            for (int row = 0; row < rows; row++) {
//                for (int col = 0; col < cols; col++) {
//                    content[row][col] = "Row " + row + ", Col " + col;
//                }
//            }
            content[0][0] = "계약번호";
            content[0][1] = String.valueOf(aplPk);
            content[1][0] = "이름";
            content[1][1] = dto.getClntNm();
            content[2][0] = "보험료";
            content[2][1] = String.valueOf(dto.getPremium())+"원";
            content[3][0] = "보험종류";
            content[3][1] = "해외여행자보험";
            content[4][0] = "보험시작일시";
            content[4][1] = dto.getTrFromDt();
            content[5][0] = "보험종료일시";
            content[5][1] = dto.getTrToDt();
            content[6][0] = "여행국가";
            content[6][1] = dto.getTrPlace();
            content[7][0] = "담보유형";
            content[7][1] = dto.getCovNm();
            content[8][0] = "여행인원";
            content[8][1] = String.valueOf(dto.getClntCnt());


            // 표 그리기
            float tableY = yPosition;
            for (int row = 0; row <= rows; row++) {
                contentStream.drawLine(margin, tableY, margin + tableWidth, tableY);
                tableY -= rowHeight;
            }

            float tableX = margin;
            for (int col = 0; col <= cols; col++) {
                contentStream.drawLine(tableX, yStart, tableX, yStart - tableHeight);
                tableX += colWidth;
            }

            // 셀 내용 추가
            float textX, textY;
            for (int row = 0; row < rows; row++) {
                textY = yPosition - 15f;
                tableX = margin + cellMargin;
                for (int col = 0; col < cols; col++) {
                    textX = tableX + 2f;
                    contentStream.beginText();
                    contentStream.setFont(nanumGothic, 12);
                    contentStream.newLineAtOffset(textX, textY);
                    contentStream.showText(content[row][col]);
                    contentStream.endText();
                    tableX += colWidth;
                }
                yPosition -= rowHeight;
            }


            // 페이지 컨텐츠 스트림 닫기
            contentStream.close();

            // PDF 문서 저장
//            document.save(path + "application_" + aplPk +".pdf");

            // PDF 문서 닫기
//            document.close();

            System.out.println("PDF 생성 완료!");
            return document;
        } catch (IOException e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void sendMail(int aplPk, String email, String filePath) throws Exception {
                final String smtpServer = "smtp.gmail.com"; // gmail smtp server
        final String senderEmail = "quack0258@gmail.com"; // gmail 아이디
        final String smtpPass = "cdljuescbaujqurb"; // gmail 비번
//        final String senderEmail = "231015"; // simg account
//        final String smtpPass = "Qkrwnsgh97^^"; // simg pass
//        final String smtpServer = "simg.daouoffice.com"; // daouoffice smtp server


        final int port = 587;

        Properties props = new Properties();

        props.put("mail.smtp.host", smtpServer);
        props.put("mail.smtp.port", port);
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.starttls.required", "true");
        props.put("mail.smtp.auth", "true");
        props.put("mail.transport.protocol","smtp");
//        props.put("mail.debug","true");
        props.put("mail.default.encoding","UTF-8");
        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, smtpPass);
            }
        });


        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress("pol0258@simg.kr")); // 보내는 이메일 주소
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(email)); // 받는 이메일 주소
            message.setSubject("[SIMG 해외여행자보험] 가입증명서");

            // 메일 본문
            String content = "SIMG 해외여행자보험 가입증명서 발송 메일";
//            message.setText(content);
            MimeBodyPart contentPart = new MimeBodyPart();
            contentPart.setText(content);

            // 첨부할 파일 추가
            MimeBodyPart attachmentPart = new MimeBodyPart();
            FileDataSource fileDataSource = new FileDataSource(filePath);
            attachmentPart.setDataHandler(new DataHandler(fileDataSource));
            attachmentPart.setFileName("가입증명서.pdf");

            // 메시지 본문과 첨부 파일을 함께 처리할 Multipart 객체 생성
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(contentPart);
            multipart.addBodyPart(attachmentPart);

            message.setContent(multipart);

            Transport.send(message);
            System.out.println("이메일을 성공적으로 보냈습니다.");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

    }

}

