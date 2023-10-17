package com.simg.simgoti.service;

import com.simg.simgoti.dto.PdfInsInfoDto;
import com.simg.simgoti.mapper.ClientMapper;
import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.imageio.ImageIO;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Properties;


@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {
    private final ClientMapper clientMapper;

    private final String smtpServer = "smtp.gmail.com"; // gmail smtp server
    private final String senderEmail = "quack0258@gmail.com"; // gmail 아이디
    private final String smtpPass = "cdljuescbaujqurb"; // gmail 비번
    private final int port = 587;

    // pdfResource폴더 경로
    private final String resourcePath = System.getProperty("user.dir") +
            File.separator + "src" +
            File.separator + "main" +
            File.separator + "resources" +
            File.separator + "pdfResources" +
            File.separator;
    String clntEmail;

//        private final String senderEmail = "231015"; // simg account
//        private final String smtpPass = "Qkrwnsgh97^^"; // simg pass
//        private final String smtpServer = "simg.daouoffice.com"; // daouoffice smtp server

    @Override
    public PDDocument createPdfKr(int aplPk) throws Exception {
        // 새로운 PDF 문서 생성
        PDDocument document = new PDDocument();
        PDType0Font nanumGothic = PDType0Font.load(document, getClass().getResourceAsStream("/pdfResources/NanumGothicCoding.ttf"));

        // pk값으로 계약서 정보 가져오기
        PdfInsInfoDto dto = clientMapper.selectInsForPdf(aplPk);
        clntEmail = dto.getClntEmail();
        try {
            // 새로운 페이지 생성
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);
            // 페이지 컨텐츠 스트림 생성
            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            // 텍스트 추가
//            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
//            contentStream.beginText();
//            contentStream.newLineAtOffset(100, 700);
//            contentStream.showText("Hello, PDFBox!");
//            contentStream.showText(String.valueOf(aplPk));
//            contentStream.endText();

            // 문서내 위치,크기 필드
            float pageX = page.getMediaBox().getWidth();
            float pageY = page.getMediaBox().getHeight();
            float margin = 50;
            float yStart = pageY - margin;
            float yPosition = yStart;
            float contentX = pageX - (2 * margin);
            float contentY = pageY - (2 * margin);

            // 테이블 위치, 크기 관련 필드
            float tableWidth = page.getMediaBox().getWidth() - 2 * margin;
            int rows = 9;
            int cols = 2;
            float rowHeight = 20f;
            float tableHeight = rowHeight * rows;
            float colWidth = tableWidth / (float) cols;
            float cellMargin = 5f;

            // 로고추가
            ClassPathResource resource = new ClassPathResource("pdfResources"+File.separator+"hanaSimg_logo.png");
            File logo = resource.getFile();
            Image img = ImageIO.read(logo);
            PDImageXObject pdImage = PDImageXObject.createFromFile(logo.toString(), document);

            float imgWidth = pageX / 3;
            float imgHeight = imgWidth * ((float) img.getHeight(null) / img.getWidth(null));
            yPosition -= imgHeight;
            contentStream.drawImage(pdImage, margin, yPosition,imgWidth, imgHeight);


            // 타이틀 추가
            yPosition -= 10;
            resource = new ClassPathResource("pdfResources"+File.separator+"title_bg.png");
            File titleBg = resource.getFile();
            img = ImageIO.read(titleBg);
            pdImage = PDImageXObject.createFromFile(titleBg.toString(), document);

            imgWidth = contentX;
            imgHeight = imgWidth * ((float) img.getHeight(null) / img.getWidth(null));
            yPosition -= imgHeight;
            contentStream.drawImage(pdImage, margin, yPosition, imgWidth, imgHeight);

            contentStream.setNonStrokingColor(255,255,255);
            contentStream.beginText();
            contentStream.setFont(nanumGothic, 14);
            float titlePadding = imgHeight * 0.33f;
            contentStream.newLineAtOffset(margin + titlePadding, yPosition + titlePadding);
            contentStream.showText("해외여행자보험 가입증명서");
            contentStream.endText();

            // 기본정보, 증권번호
            yPosition -= 20;
            contentStream.setNonStrokingColor(0,0,0);
            contentStream.beginText();
            contentStream.setFont(nanumGothic, 13);
            contentStream.newLineAtOffset(margin + 4, yPosition + 4);
            contentStream.showText("● 기본정보");
            contentStream.endText();

            contentStream.beginText();
            float polFontSize = 13f;
            String polNo = "증권번호 : " + dto.getPolNo();
            float polStringWidth = nanumGothic.getStringWidth(polNo) / 1000 * polFontSize;

            contentStream.setFont(nanumGothic, polFontSize);
            contentStream.newLineAtOffset(margin + contentX - polStringWidth, yPosition + 4);
            contentStream.showText(polNo);
            contentStream.endText();

            // 셀 내용 배열 생성
            String[][] content = new String[rows][cols];
//            for (int row = 0; row < rows; row++) {
//                for (int col = 0; col < cols; col++) {
//                    content[row][col] = "Row " + row + ", Col " + col;
//                }
//            }
            content[0][0] = "증권번호";
            content[0][1] = dto.getPolNo();
            content[1][0] = "이름";
            content[1][1] = dto.getClntNm();
            content[2][0] = "보험료";
            content[2][1] = String.valueOf(dto.getPremium()) + "원";
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
            content[8][0] = "피보험자 수";
            content[8][1] = String.valueOf(dto.getClntCnt()) + " 명";

            // 표 그리기
            float tableY = yPosition;
            for (int row = 0; row <= rows; row++) {
                contentStream.drawLine(margin, tableY, margin + tableWidth, tableY);
                tableY -= rowHeight;
            }

            float tableX = margin;
            for (int col = 0; col <= cols; col++) {
                contentStream.drawLine(tableX, yPosition, tableX, yPosition - tableHeight);
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
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void sendApplyMail(int aplPk,String title, String email, String fileName) throws Exception {
        // 파일생성
        String filePath = System.getProperty("user.dir") + File.separator + "tempPdf" + File.separator + "application_"+aplPk+".pdf";
        PDDocument document = this.createPdfKr(aplPk);
        document.save(filePath);
        document.close();

        // 메일전송----
        Properties props = new Properties();

        props.put("mail.smtp.host", smtpServer);
        props.put("mail.smtp.port", port);
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.starttls.required", "true");
        props.put("mail.smtp.auth", "true");
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.default.encoding", "UTF-8");
        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, smtpPass);
            }
        });


        try {
            // 메시지 본문과 첨부 파일을 함께 처리할 Multipart 객체 생성
            Multipart multipart = new MimeMultipart();

            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress("pol0258@simg.kr")); // 보내는 이메일 주소
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(email)); // 받는 이메일 주소
            message.setSubject(title);

            // 메일 본문
            String content =
                    """
                            <h1 style="color:#05219a;">SIMG 해외여행자보험 가입증명서</h1>
                            <p>안녕하세요</p>
                            <p>SIMG 해외여행자보험 가입증명서를 보내드립니다.</p>
                            <p>첨부파일을 확인해주세요.</p>
                            <br>
                            <p>감사합니다.</p>
                            """;
//            message.setText(content);
            MimeBodyPart contentPart = new MimeBodyPart();
//            contentPart.setText(content);
            contentPart.setContent(content, "text/html; charset=euc-kr");
            multipart.addBodyPart(contentPart);

            if(filePath != null){
                // 첨부할 파일 추가
                MimeBodyPart attachmentPart = new MimeBodyPart();
                FileDataSource fileDataSource = new FileDataSource(filePath);
                attachmentPart.setDataHandler(new DataHandler(fileDataSource));
                attachmentPart.setFileName(fileName);
                multipart.addBodyPart(attachmentPart);
            }

            message.setContent(multipart);

            Transport.send(message);
            System.out.println("이메일 발송완료");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        finally {
            // 생성한 PDF 파일 삭제
            File generatedPdf = new File(filePath);
            if (generatedPdf.exists()) {
                generatedPdf.delete();
            }
            clntEmail = null;
        }
    }

}

