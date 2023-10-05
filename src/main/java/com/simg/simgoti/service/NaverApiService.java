package com.simg.simgoti.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.*;
import java.util.HashMap;

public class NaverApiService {
    private String accessKey = "F4nqr68CXn2V3IiCZ3I8";
    private String secretKey = "0MGqxfvGg18SI5TgLcz8gLGUEPOKQxbXsP2Cg71w";
    private String smsUrl = "https://sens.apigw.ntruss.com/sms/v2/services/ncp:sms:kr:317060051431:simgoti/messages";
    private String makeSignature(String url, String timestamp) throws Exception {
        String space = " ";					// one space
        String newLine = "\n";					// new line
        String method = "POST";					// method

        String message = new StringBuilder()
                .append(method)
                .append(space)
                .append(url)
                .append(newLine)
                .append(timestamp)
                .append(newLine)
                .append(accessKey)
                .toString();

        SecretKeySpec signingKey = new SecretKeySpec(secretKey.getBytes("UTF-8"), "HmacSHA256");
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(signingKey);

        byte[] rawHmac = mac.doFinal(message.getBytes("UTF-8"));
        String encodeBase64String = Base64.encodeBase64String(rawHmac);

        return encodeBase64String;
    }

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public ResponseEntity<String> sendSms(String phone, String msg) throws Exception{
        String timestamp = String.valueOf(System.currentTimeMillis());
        // 요청 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Content-type", "application/json; charset=utf-8");
        headers.set("x-ncp-apigw-timestamp", timestamp);
        headers.set("x-ncp-iam-access-key", accessKey);
        // signature 서명
        headers.set("x-ncp-apigw-signature-v2", this.makeSignature("/sms/v2/services/ncp:sms:kr:317060051431:simgoti/messages", timestamp));

        // 요청 본문 데이터 설정
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("from", "01075408898");
        requestBody.put("contentType", "COMM");
        requestBody.put("type", "SMS");
        requestBody.put("countryCode", "82");
        requestBody.put("content", "[SIMG 해외여행자보험]");
        List<Object> messages = new ArrayList<>();
        Map<String,String> message = new HashMap<>();
        message.put("to",phone);
        message.put("content", msg);
        messages.add(message);
        requestBody.put("messages", messages);

        // 요청 본문 데이터를 JSON 문자열로 확인
        try {
            String requestBodyJson = objectMapper.writeValueAsString(requestBody);
            System.out.println("요청 본문 JSON: " + requestBodyJson);
        } catch (Exception e) {
            e.printStackTrace();
        }


        // HttpEntity를 사용하여 헤더와 본문을 함께 설정
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        // POST 요청 보내기
        ResponseEntity<String> response = restTemplate.postForEntity(
                smsUrl,
                requestEntity,
                String.class);

        return response;
    }

}
