package com.simg.simgoti.service;

import com.simg.simgoti.dto.PdfClntDto;
import com.simg.simgoti.dto.PdfCovDto;
import com.simg.simgoti.dto.PdfInsInfoDto;
import com.simg.simgoti.mapper.ClientMapper;
import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
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
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;


@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    private final ClientMapper clientMapper;
    private final CommonService commonService;

    DecimalFormat df = new DecimalFormat("###,###");

    private final String smtpServer = "smtp.gmail.com"; // gmail smtp server
    private final String senderEmail = "quack0258@gmail.com"; // gmail 아이디
    private final String smtpPass = "cdljuescbaujqurb"; // gmail 비번
    private final int port = 587;


//        private final String senderEmail = "231015"; // simg account
//        private final String smtpPass = "Qkrwnsgh97^^"; // simg pass
//        private final String smtpServer = "simg.daouoffice.com"; // daouoffice smtp server

// 시간 자르는 메소드
public String cutSec(String str) {
    if(str.length()>16){
        return str.substring(0,str.length()-3);
    }
    else{
        return str;
    }
}
    @Override
    public PDDocument createPdfKr(int aplPk) throws Exception {


        // 새로운 PDF 문서 생성
        PDDocument document = new PDDocument();
        PDType0Font nanumGothic = PDType0Font.load(document, getClass().getResourceAsStream("/pdfResources/NanumGothicCoding.ttf"));

        // pk값으로 기명요청서 정보 가져오기
        PdfInsInfoDto dto = clientMapper.selectInsForPdf(aplPk);
        // pk값으로 기명요청인원 리스트 가져오기
        ArrayList<PdfClntDto> clnt = clientMapper.selectClntForPdf(aplPk);
        // pk값으로 담보 리스트 가져오기
        ArrayList<PdfCovDto> cov = clientMapper.selectCovDForPdf(aplPk);
        int clntNum = clnt.size();
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
            float bottomY = margin;

            // 로고추가
            ClassPathResource resource = new ClassPathResource("pdfResources"+File.separator+"hanaSimg_logo.png");
            InputStream fileStream = resource.getInputStream();
            BufferedImage img = ImageIO.read(fileStream);
            PDImageXObject pdImage = LosslessFactory.createFromImage(document, img);

            float imgWidth = pageX / 3;
            float imgHeight = imgWidth * ((float) img.getHeight(null) / img.getWidth(null));
            yPosition -= imgHeight;
            contentStream.drawImage(pdImage, margin, yPosition,imgWidth, imgHeight);

            // 타이틀 추가
            yPosition -= 10;
            resource = new ClassPathResource("pdfResources"+File.separator+"title_bg.png");
            fileStream = resource.getInputStream();
            img = ImageIO.read(fileStream);
            pdImage = LosslessFactory.createFromImage(document, img);

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

            // 기본정보, 증권번호(테이블제목)
            yPosition -= 40;
            float tableTitleFontSize = 13f;
            contentStream.setNonStrokingColor(0,0,0);
            contentStream.beginText();
            contentStream.setFont(nanumGothic, tableTitleFontSize);
            contentStream.newLineAtOffset(margin + 4, yPosition + 4);
            contentStream.showText("● 기본정보");
            contentStream.endText();

            contentStream.beginText();
            float polFontSize = 10f;
            String polNo = "증권번호 : " + dto.getPolNo();
            float polStringWidth = nanumGothic.getStringWidth(polNo) / 1000 * polFontSize;

            contentStream.setFont(nanumGothic, polFontSize);
            contentStream.newLineAtOffset(margin + contentX - polStringWidth, yPosition + 4);
            contentStream.showText(polNo);
            contentStream.endText();

            // 기본정보 테이블
            yPosition -= 6;
            resource = new ClassPathResource("pdfResources"+File.separator+"info1.png");
            fileStream = resource.getInputStream();
            img = ImageIO.read(fileStream);
            pdImage = LosslessFactory.createFromImage(document, img);

            imgWidth = contentX;
            imgHeight = imgWidth * ((float) img.getHeight(null) / img.getWidth(null));
            yPosition -= imgHeight;
            contentStream.drawImage(pdImage, margin, yPosition, imgWidth, imgHeight);

            // 기본정보 테이블 1번째 행
            yPosition += imgHeight;
            float info1TrHeight = imgHeight * 8/35;
            float info1FontSize = 10f;

            yPosition -= info1TrHeight;

            String info1Text = "결제자";
            contentStream.beginText();
            contentStream.setFont(nanumGothic, info1FontSize);
            contentStream.newLineAtOffset(margin + info1FontSize, yPosition + info1FontSize);
            contentStream.showText(info1Text);
            contentStream.endText();

            info1Text = dto.getClntNm();
            contentStream.beginText();
            contentStream.setFont(nanumGothic, info1FontSize);
            contentStream.newLineAtOffset(margin + imgWidth / 5 + info1FontSize, yPosition + info1FontSize);
            contentStream.showText(info1Text);
            contentStream.endText();

            info1Text = "단체계약자";
            contentStream.beginText();
            contentStream.setFont(nanumGothic, info1FontSize);
            contentStream.newLineAtOffset(margin + imgWidth/2 + info1FontSize, yPosition + info1FontSize);
            contentStream.showText(info1Text);
            contentStream.endText();

            info1Text = "에스아이엠지";
            contentStream.beginText();
            contentStream.setFont(nanumGothic, info1FontSize);
            contentStream.newLineAtOffset(margin + imgWidth/2 + imgWidth/5 + info1FontSize, yPosition + info1FontSize);
            contentStream.showText(info1Text);
            contentStream.endText();

            // 기본정보 테이블 2번째 행
            yPosition -= info1TrHeight;

            info1Text = "피보험자수";
            contentStream.beginText();
            contentStream.setFont(nanumGothic, info1FontSize);
            contentStream.newLineAtOffset(margin + info1FontSize, yPosition + info1FontSize);
            contentStream.showText(info1Text);
            contentStream.endText();

            info1Text = dto.getClntCnt() + "명";
            contentStream.beginText();
            contentStream.setFont(nanumGothic, info1FontSize);
            contentStream.newLineAtOffset(margin + imgWidth / 5 + info1FontSize, yPosition + info1FontSize);
            contentStream.showText(info1Text);
            contentStream.endText();

            info1Text = "여행지";
            contentStream.beginText();
            contentStream.setFont(nanumGothic, info1FontSize);
            contentStream.newLineAtOffset(margin + imgWidth/2 + info1FontSize, yPosition + info1FontSize);
            contentStream.showText(info1Text);
            contentStream.endText();

            info1Text = dto.getTrPlace();
            contentStream.beginText();
            contentStream.setFont(nanumGothic, info1FontSize);
            contentStream.newLineAtOffset(margin + imgWidth/2 + imgWidth/5 + info1FontSize, yPosition + info1FontSize);
            contentStream.showText(info1Text);
            contentStream.endText();

            // 기본정보 테이블 3번째줄
            yPosition -= info1TrHeight;

            info1Text = "보험기간";
            contentStream.beginText();
            contentStream.setFont(nanumGothic, info1FontSize);
            contentStream.newLineAtOffset(margin + info1FontSize, yPosition + info1FontSize);
            contentStream.showText(info1Text);
            contentStream.endText();

            info1Text = cutSec(dto.getTrFromDt()) + "부터";
            contentStream.beginText();
            contentStream.setFont(nanumGothic, info1FontSize);
            contentStream.newLineAtOffset(margin + imgWidth / 5 + info1FontSize, yPosition + info1FontSize);
            contentStream.showText(info1Text);
            contentStream.endText();

            yPosition -= info1FontSize * 1.1;
            info1Text = cutSec(dto.getTrToDt()) + "까지";
            contentStream.beginText();
            contentStream.setFont(nanumGothic, info1FontSize);
            contentStream.newLineAtOffset(margin + imgWidth / 5 + info1FontSize, yPosition + info1FontSize);
            contentStream.showText(info1Text);
            contentStream.endText();
            yPosition += info1FontSize * 1.1;

            info1Text = "여행목적";
            contentStream.beginText();
            contentStream.setFont(nanumGothic, info1FontSize);
            contentStream.newLineAtOffset(margin + imgWidth/2 + info1FontSize, yPosition + info1FontSize);
            contentStream.showText(info1Text);
            contentStream.endText();

            info1Text = dto.getTrPurpose();
            contentStream.beginText();
            contentStream.setFont(nanumGothic, info1FontSize);
            contentStream.newLineAtOffset(margin + imgWidth/2 + imgWidth/5 + info1FontSize, yPosition + info1FontSize);
            contentStream.showText(info1Text);
            contentStream.endText();

            // 기본정보 테이블 1번째줄
            yPosition = yPosition + info1TrHeight * 3 - imgHeight;

            info1Text = "납입보험료";
            contentStream.beginText();
            contentStream.setFont(nanumGothic, info1FontSize);
            contentStream.newLineAtOffset(margin + info1FontSize, yPosition + info1FontSize);
            contentStream.showText(info1Text);
            contentStream.endText();

            NumberFormat numberFormat = NumberFormat.getInstance();
            info1Text = numberFormat.format(dto.getPremium()) + "원";
            contentStream.beginText();
            contentStream.setFont(nanumGothic, info1FontSize);
            contentStream.newLineAtOffset(margin + imgWidth / 5 + info1FontSize, yPosition + info1FontSize);
            contentStream.showText(info1Text);
            contentStream.endText();

            info1Text = "보험료 납입일시";
            contentStream.beginText();
            contentStream.setFont(nanumGothic, info1FontSize);
            contentStream.newLineAtOffset(margin + imgWidth/2 + info1FontSize, yPosition + info1FontSize);
            contentStream.showText(info1Text);
            contentStream.endText();

            info1Text = cutSec(dto.getPayedDt());
            contentStream.beginText();
            contentStream.setFont(nanumGothic, info1FontSize);
            contentStream.newLineAtOffset(margin + imgWidth/2 + imgWidth/5 + info1FontSize, yPosition + info1FontSize);
            contentStream.showText(info1Text);
            contentStream.endText();

            // 기본정보 아래 작은글씨 설명
            yPosition -= 15;
            contentStream.setNonStrokingColor(100,100,100);
            float smallFontSize = 6f;
            String smallText1 = "* 이 상품은 에스아이엠지를 계약자로 지정한 단체보험으로, 피보험자의 봏머청구는 하나손해보험에서 정상적으로 처리됩니다.";
            contentStream.beginText();
            contentStream.setFont(nanumGothic, smallFontSize);
            contentStream.newLineAtOffset(margin, yPosition);
            contentStream.showText(smallText1);
            contentStream.endText();

            yPosition -= smallFontSize * 1.5;

            String smallText2 = "* 이 상품은 "+commonService.attachJosa(dto.getTrPlace())+" 제외한 세계 어느 지역의 여행이든 보장하나, 대한민국 외교부가 지정한 여행금지국가와 3단계 여행경보지역은 보장에서 제외됩니다.";
            contentStream.beginText();
            contentStream.setFont(nanumGothic, smallFontSize);
            contentStream.newLineAtOffset(margin, yPosition);
            contentStream.showText(smallText2);
            contentStream.endText();

            // 피보험자 정보
            yPosition -= 40;
            // 표 제목
            contentStream.setNonStrokingColor(0,0,0);
            contentStream.beginText();
            contentStream.setFont(nanumGothic, tableTitleFontSize);
            contentStream.newLineAtOffset(margin + 4, yPosition + 4);
            contentStream.showText("● 보험가입자(피보험자)정보");
            contentStream.endText();
            // 표 그림
            yPosition -= 6;
            resource = new ClassPathResource("pdfResources"+File.separator+"info2.png");
            fileStream = resource.getInputStream();
            img = ImageIO.read(fileStream);
            pdImage = LosslessFactory.createFromImage(document, img);
            imgWidth = contentX;
            imgHeight = imgWidth * ((float) img.getHeight(null) / img.getWidth(null));
            yPosition -= imgHeight;
            contentStream.drawImage(pdImage, margin, yPosition, imgWidth, imgHeight);
            // 피보험자정보 1번째 행
            float info2TrHeight = imgHeight * 78/272;
            yPosition += imgHeight;
            yPosition -= info2TrHeight;

            String info2Text = clntNum==1?"가입자":"대표가입자";
            contentStream.beginText();
            contentStream.setFont(nanumGothic, info1FontSize);
            contentStream.newLineAtOffset(margin + info1FontSize, yPosition + info1FontSize);
            contentStream.showText(info2Text);
            contentStream.endText();

            info2Text = dto.getClntNm();
            contentStream.beginText();
            contentStream.setFont(nanumGothic, info1FontSize);
            contentStream.newLineAtOffset(margin + imgWidth / 5 + info1FontSize, yPosition + info1FontSize);
            contentStream.showText(info2Text);
            contentStream.endText();

            info2Text = "보험종류";
            contentStream.beginText();
            contentStream.setFont(nanumGothic, info1FontSize);
            contentStream.newLineAtOffset(margin + imgWidth/2 + info1FontSize, yPosition + info1FontSize);
            contentStream.showText(info2Text);
            contentStream.endText();

            info2Text = "해외여행자보험";
            contentStream.beginText();
            contentStream.setFont(nanumGothic, info1FontSize);
            contentStream.newLineAtOffset(margin + imgWidth/2 + imgWidth/5 + info1FontSize, yPosition + info1FontSize);
            contentStream.showText(info2Text);
            contentStream.endText();

            // 피보험자정보 테이블 2번재 행
            yPosition -= info2TrHeight;

            info2Text = "이메일";
            contentStream.beginText();
            contentStream.setFont(nanumGothic, info1FontSize);
            contentStream.newLineAtOffset(margin + info1FontSize, yPosition + info1FontSize);
            contentStream.showText(info2Text);
            contentStream.endText();

            info2Text = dto.getClntEmail();
            contentStream.beginText();
            contentStream.setFont(nanumGothic, info1FontSize);
            contentStream.newLineAtOffset(margin + imgWidth / 5 + info1FontSize, yPosition + info1FontSize);
            contentStream.showText(info2Text);
            contentStream.endText();

            info2Text = "휴대폰번호";
            contentStream.beginText();
            contentStream.setFont(nanumGothic, info1FontSize);
            contentStream.newLineAtOffset(margin + imgWidth/2 + info1FontSize, yPosition + info1FontSize);
            contentStream.showText(info2Text);
            contentStream.endText();

            String phoneNum = dto.getClntPhone();
            info2Text = phoneNum.substring(0,phoneNum.length()-4) + "****";
            contentStream.beginText();
            contentStream.setFont(nanumGothic, info1FontSize);
            contentStream.newLineAtOffset(margin + imgWidth/2 + imgWidth/5 + info1FontSize, yPosition + info1FontSize);
            contentStream.showText(info2Text);
            contentStream.endText();

            // 피보험자정보 테이블 3번째 줄
            yPosition -= info2TrHeight;

            info2Text = "보험금수령인";
            contentStream.beginText();
            contentStream.setFont(nanumGothic, info1FontSize);
            contentStream.newLineAtOffset(margin + info1FontSize, yPosition + info1FontSize);
            contentStream.showText(info2Text);
            contentStream.endText();

            info2Text = "사망시:법정상속인";
            contentStream.beginText();
            contentStream.setFont(nanumGothic, info1FontSize);
            contentStream.newLineAtOffset(margin + imgWidth / 5 + info1FontSize, yPosition + info1FontSize);
            contentStream.showText(info2Text);
            contentStream.endText();

            yPosition -= info1FontSize * 1.1;
            info2Text = "사망외:본인(단, 미성년자의 경우 법정대리인)";
            contentStream.beginText();
            contentStream.setFont(nanumGothic, info1FontSize);
            contentStream.newLineAtOffset(margin + imgWidth / 5 + info1FontSize, yPosition + info1FontSize);
            contentStream.showText(info2Text);
            contentStream.endText();
            yPosition += info1FontSize * 1.1;

            if(clntNum != 1){
                // 피보험자테이블 4번째줄 피보험자리스트
                yPosition = yPosition + info2TrHeight * 3 - imgHeight;

                // 행 이미지 기본설정
                resource = new ClassPathResource("pdfResources"+File.separator+"info2_insList.png");
                fileStream = resource.getInputStream();
                img = ImageIO.read(fileStream);
                pdImage = LosslessFactory.createFromImage(document, img);
                imgWidth = contentX;
                for(int i=0; i<clntNum+1; i++){
                    float rowHeight = info1FontSize * 1.1f;
                    if(i ==0){
                        rowHeight = info2TrHeight;
                    }

                    if(i != clntNum){
                        // 행 이미지 높이, 위치
                        imgHeight = rowHeight;
                        yPosition -= imgHeight;
                        contentStream.drawImage(pdImage, margin, yPosition, imgWidth, imgHeight);
                    }
                    else {
                        // 테이블 하단 라인
                        resource = new ClassPathResource("pdfResources"+File.separator+"table_line.png");
                        fileStream = resource.getInputStream();
                        img = ImageIO.read(fileStream);
                        pdImage = LosslessFactory.createFromImage(document, img);
                        imgWidth = contentX;
                        imgHeight = imgWidth * ((float) img.getHeight(null) / img.getWidth(null));
                        yPosition -= imgHeight;
                        contentStream.drawImage(pdImage, margin, yPosition, imgWidth, imgHeight);
                    }

                    if(i == 0){
                        info2Text = "피보험자";
                        contentStream.beginText();
                        contentStream.setFont(nanumGothic, info1FontSize);
                        contentStream.newLineAtOffset(margin + info1FontSize, yPosition + info1FontSize);
                        contentStream.showText(info2Text);
                        contentStream.endText();
                    }

                    if(i != clntNum) {
                        // 행 내용
                        PdfClntDto clntDto;
                        try{
                            clntDto = clnt.get(i);
                            info2Text = clntDto.getClntNm() + "(" + clntDto.getClntBirth() + "-" + clntDto.getClntGen() + "******)";
                        }
                        catch(Exception e){
                            info2Text = "-";
                        }

                        contentStream.beginText();
                        contentStream.setFont(nanumGothic, info1FontSize);
                        contentStream.newLineAtOffset(margin + imgWidth / 5 + info1FontSize, yPosition + info1FontSize);
                        contentStream.showText(info2Text);
                        contentStream.endText();
                    }
                }
            }

            // 연령별 보장내용
            yPosition -= 40;
            if(yPosition-6 -imgHeight-info2TrHeight*2 < bottomY){
                contentStream.close();
                // 새로운 페이지 생성
                page = new PDPage(PDRectangle.A4);
                document.addPage(page);
                // 페이지 컨텐츠 스트림 생성
                contentStream = new PDPageContentStream(document, page);
                yPosition = pageY - margin;
                contentStream.setStrokingColor(230, 230, 230);
            }

            contentStream.beginText();
            contentStream.setFont(nanumGothic, tableTitleFontSize);
            contentStream.newLineAtOffset(margin + 4, yPosition + 4);
            contentStream.showText("● 연령별 보장내용");
            contentStream.endText();

            // 연련별 보장내용 테이블
            yPosition -= 6;
            // 헤더
            resource = new ClassPathResource("pdfResources"+File.separator+"cov_header.png");
            fileStream = resource.getInputStream();
            img = ImageIO.read(fileStream);
            pdImage = LosslessFactory.createFromImage(document, img);

            imgWidth = contentX;
            imgHeight = imgWidth * ((float) img.getHeight(null) / img.getWidth(null));
            yPosition -= imgHeight;
            contentStream.drawImage(pdImage, margin, yPosition, imgWidth, imgHeight);
            contentStream.setStrokingColor(230, 230, 230);
            contentStream.drawLine(margin, yPosition, margin + imgWidth, yPosition);

            String covHeader = "0 ~ 18세";
            float covHeaderStringWidth = nanumGothic.getStringWidth(covHeader) / 1000 * info1FontSize;
            contentStream.beginText();
            contentStream.setFont(nanumGothic, info1FontSize);
            contentStream.newLineAtOffset(margin + contentX - imgWidth*1/5 - covHeaderStringWidth - info1FontSize, yPosition + info1FontSize);
            contentStream.showText(covHeader);
            contentStream.endText();

            covHeader = "19 ~ 79세";
            covHeaderStringWidth = nanumGothic.getStringWidth(covHeader) / 1000 * info1FontSize;
            contentStream.beginText();
            contentStream.setFont(nanumGothic, info1FontSize);
            contentStream.newLineAtOffset(margin + contentX - covHeaderStringWidth - info1FontSize, yPosition + info1FontSize);
            contentStream.showText(covHeader);
            contentStream.endText();

            // 담보 리스트 나열
            int covNum = cov.size();
            float covTrHeight = info2TrHeight;
            float covDFontSize = 10f;
            Integer[] covOnlyForAdultArray = {1012010101, 1012010103,1012010201, 1012010203,1012010301, 1012010303};
            List<Integer> covOnlyForAdult = new ArrayList<>(Arrays.asList(covOnlyForAdultArray));
            for(int i=0; i<covNum; i++){
                yPosition -= covTrHeight;
                PdfCovDto covD = cov.get(i);

                if(yPosition > bottomY){
                    // 세부담보 내용 쓰기
                    String covDText = covD.getCovDNm();
                    contentStream.beginText();
                    contentStream.setFont(nanumGothic, covDFontSize);
                    contentStream.newLineAtOffset(margin + covDFontSize, yPosition + covDFontSize);
                    contentStream.showText(covDText);
                    contentStream.endText();

                    if(covOnlyForAdult.contains(covD.getCovDCode())){
                        covDText = "-";
                    }
                    else{
                        covDText = df.format(covD.getCovDAmt())+"원";
                    }
                    float amtStringWidth = nanumGothic.getStringWidth(covDText) / 1000 * covDFontSize;
                    contentStream.beginText();
                    contentStream.setFont(nanumGothic, covDFontSize);
                    contentStream.newLineAtOffset(margin + contentX - imgWidth*1/5 - amtStringWidth - covDFontSize, yPosition + covDFontSize);
                    contentStream.showText(covDText);
                    contentStream.endText();

                    int amt = covD.getCovDAmt();
                    covDText = df.format(covD.getCovDAmt())+"원";
                    amtStringWidth = nanumGothic.getStringWidth(covDText) / 1000 * covDFontSize;
                    contentStream.beginText();
                    contentStream.setFont(nanumGothic, covDFontSize);
                    contentStream.newLineAtOffset(margin + contentX - amtStringWidth - covDFontSize, yPosition + covDFontSize);
                    contentStream.showText(covDText);
                    contentStream.endText();

                    contentStream.drawLine(margin, yPosition, margin + imgWidth, yPosition);

                }
                else{
                    contentStream.close();
                    // 새로운 페이지 생성
                    page = new PDPage(PDRectangle.A4);
                    document.addPage(page);
                    // 페이지 컨텐츠 스트림 생성
                    contentStream = new PDPageContentStream(document, page);
                    i-=1;
                    yPosition = pageY - margin;
                    contentStream.setStrokingColor(230, 230, 230);
                }

            }

            // 하단 확인 인장
            yPosition -= 40;
            resource = new ClassPathResource("pdfResources"+File.separator+"hana_signature.png");
            fileStream = resource.getInputStream();
            img = ImageIO.read(fileStream);
            pdImage = LosslessFactory.createFromImage(document, img);

            imgWidth = contentX / 4;
            imgHeight = imgWidth * ((float) img.getHeight(null) / img.getWidth(null));
            yPosition -= imgHeight * 2;
            if(yPosition < bottomY){
                contentStream.close();
                // 새로운 페이지 생성
                page = new PDPage(PDRectangle.A4);
                document.addPage(page);
                // 페이지 컨텐츠 스트림 생성
                contentStream = new PDPageContentStream(document, page);
                yPosition = pageY - margin -imgHeight * 2;
            }
            contentStream.drawImage(pdImage, margin+contentX-imgWidth, yPosition, imgWidth, imgHeight);

            // 하단 확인 글귀
            yPosition += imgHeight + 20;
            float checkFontSize = 12f;

            String clntNim = dto.getClntNm()+"님";
            if(clntNum > 1){
                clntNim = clntNim+" 외 "+String.valueOf(clntNum -1)+"명께서";
            }
            String checkText = "하나손해보험은 "+clntNim+" 해외여행자보험에 가입하셨음을 확인합니다.";
            float checkFontWidth = nanumGothic.getStringWidth(checkText)/1000*checkFontSize;
            contentStream.beginText();
            contentStream.setFont(nanumGothic, checkFontSize);
            contentStream.newLineAtOffset(margin + (contentX - checkFontWidth)/2, yPosition + checkFontSize);
            contentStream.showText(checkText);
            contentStream.endText();
            yPosition -= imgHeight;
            yPosition -= 20;

            // 최하단 안내 이미지
            yPosition -= 20;
            resource = new ClassPathResource("pdfResources"+File.separator+"hana_bottom.png");
            fileStream = resource.getInputStream();
            img = ImageIO.read(fileStream);
            pdImage = LosslessFactory.createFromImage(document, img);

            imgWidth = contentX;
            imgHeight = imgWidth * ((float) img.getHeight(null) / img.getWidth(null));
            yPosition -= imgHeight;
            if(yPosition < bottomY){
                contentStream.close();
                // 새로운 페이지 생성
                page = new PDPage(PDRectangle.A4);
                document.addPage(page);
                // 페이지 컨텐츠 스트림 생성
                contentStream = new PDPageContentStream(document, page);
                yPosition = pageY - margin -imgHeight;
            }
            contentStream.drawImage(pdImage, margin+contentX-imgWidth, yPosition, imgWidth, imgHeight);

//            // 테이블 위치, 크기 관련 필드
//            float tableWidth = page.getMediaBox().getWidth() - 2 * margin;
//            int rows = 4;
//            int cols = 4;
//            float rowHeight = 20f;
//            float tableHeight = rowHeight * rows;
//            float colWidth = tableWidth / (float) cols;
//            float cellMargin = 5f;
//
//            // 셀 내용 배열 생성
//            String[][] content = new String[rows][cols];
////            for (int row = 0; row < rows; row++) {
////                for (int col = 0; col < cols; col++) {
////                    content[row][col] = "Row " + row + ", Col " + col;
////                }
////            }
//            content[0][0] = "증권번호";
//            content[0][1] = dto.getPolNo();
//            content[1][0] = "이름";
//            content[1][1] = dto.getClntNm();
//            content[2][0] = "보험료";
//            content[2][1] = String.valueOf(dto.getPremium()) + "원";
//            content[3][0] = "보험종류";
//            content[3][1] = "해외여행자보험";
//            content[4][0] = "보험시작일시";
//            content[4][1] = dto.getTrFromDt();
//            content[5][0] = "보험종료일시";
//            content[5][1] = dto.getTrToDt();
//            content[6][0] = "여행국가";
//            content[6][1] = dto.getTrPlace();
//            content[7][0] = "담보유형";
//            content[7][1] = dto.getCovNm();
//            content[8][0] = "피보험자 수";
//            content[8][1] = String.valueOf(dto.getClntCnt()) + " 명";
//
//            // 표 그리기
//            float tableY = yPosition;
//            for (int row = 0; row <= rows; row++) {
//                contentStream.drawLine(margin, tableY, margin + tableWidth, tableY);
//                tableY -= rowHeight;
//            }
//
//            float tableX = margin;
//            for (int col = 0; col <= cols; col++) {
//                contentStream.drawLine(tableX, yPosition, tableX, yPosition - tableHeight);
//                tableX += colWidth;
//            }
//
//            // 셀 내용 추가
//
//            float textX, textY;
//            for (int row = 0; row < rows; row++) {
//                textY = yPosition - 15f;
//                tableX = margin + cellMargin;
//                for (int col = 0; col < cols; col++) {
//                    textX = tableX + 2f;
//                    contentStream.beginText();
//                    contentStream.setFont(nanumGothic, 12);
//                    contentStream.newLineAtOffset(textX, textY);
//                    contentStream.showText(content[row][col]);
//                    contentStream.endText();
//                    tableX += colWidth;
//                }
//                yPosition -= rowHeight;
//            }


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
            else{
                System.out.println("가입증명서 생성 실패");
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
        }
    }
    @Override
    public String selectEmailByAplPk(int aplPk) throws Exception{
        return clientMapper.selectEmailByAplPk(aplPk);
    }
}

