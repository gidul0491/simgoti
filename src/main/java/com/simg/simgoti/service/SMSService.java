package com.simg.simgoti.service;

import org.springframework.http.ResponseEntity;

public interface SMSService {
    ResponseEntity<String> sendSms(String phone, String msg) throws Exception;
    String certificationSms(String phone);

}
