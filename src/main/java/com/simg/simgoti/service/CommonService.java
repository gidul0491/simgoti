package com.simg.simgoti.service;

import java.util.List;

public interface CommonService {
    String selectPolNoByCovCode(int covCode) throws Exception;
    int calculateInsAge(String age) throws Exception;
    String validateInsDate(String startDt, String endDt) throws Exception;
    int calculatePremium(String code, int perDay, int min, int day);
    int getPeriod(String startDt, String endDt);
}
