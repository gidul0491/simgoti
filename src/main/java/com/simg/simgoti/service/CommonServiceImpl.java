package com.simg.simgoti.service;

import com.simg.simgoti.mapper.CommonMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommonServiceImpl implements CommonService {
    private final CommonMapper commonMapper;

    public String selectPolNoByCovCode(int covCode) throws Exception {
        return commonMapper.selectPolNoByCovCode(covCode);
    }

    // 생년월일 8자리를 보험나이로 변환, 유효성검사도 가능
    public int calculateInsAge(String birth) throws Exception{

        int year = Integer.parseInt(birth.substring(0,4));
        int mth = Integer.parseInt(birth.substring(4,6));
        int dt = Integer.parseInt(birth.substring(6,8));

        LocalDate birthday = LocalDate.of(year, mth, dt);

        // 보험나이는 오늘을 6개월 이후의 하루 전날로 계산한 만나이와 동일함
        LocalDate now = LocalDate.now();
        now = now.plusMonths(6);

        int insAge = now.getYear() - birthday.getYear();

        // 월 비교
        if(mth > now.getMonthValue()){
            insAge--;
        }
        else if(mth == now.getMonthValue() && dt > now.getDayOfMonth()){
            insAge--;
        }

        return insAge;
    }

    // 여행기간을 입력받아 유효한 기간인지 아닌지를 검사
    public String validateInsDate(String startDt, String endDt) throws Exception {
        LocalDateTime now = LocalDateTime.now();
        now = now.withHour(0).withMinute(0).withSecond(0).withNano(0);

        int startYear = Integer.parseInt(startDt.split("-")[0]);
        int startMonth = Integer.parseInt(startDt.split("-")[1]);
        int startDate = Integer.parseInt(startDt.split("-")[2].split(" ")[0]);
        int startTime = Integer.parseInt(startDt.split(" ")[1].split(":")[0]);
        int endYear = Integer.parseInt(endDt.split("-")[0]);
        int endMonth = Integer.parseInt(endDt.split("-")[1]);
        int endDate = Integer.parseInt(endDt.split("-")[2].split(" ")[0]);
        int endTime = Integer.parseInt(endDt.split(" ")[1].split(":")[0]);
        LocalDateTime trFromDt = LocalDateTime.of(startYear, startMonth, startDate,startTime,0);
        LocalDateTime trToDt = LocalDateTime.of(endYear, endMonth, endDate, endTime,0);

        // 지금 시간(지금날짜의 0시0분)보다 이른 시간을 출발시간으로 설정한 경우
        if(trFromDt.isBefore(now)){
            return "출발시간을 현재시간 이후로 선택해주세요.";
        }
        // 출발시간이 지금 시간(지금날짜의 0시 0분)의 3개월 이후와 같거나 그 이후인 경우
        if(trFromDt.isAfter(now.plusMonths(3)) || trFromDt.isEqual(now.plusMonths(3))){
            return "출발시간은 현재 날짜로부터 3개월 이내로만 선택가능합니다.";
        }
        // 여행종료시간이 출발시간과 동일하거나 이전인 경우
        if(trToDt.isEqual(trFromDt) || trToDt.isBefore(trFromDt)){
            return "여행종료시간을 출발시간 이전으로 선택해주세요.";
        }
        // 여행종료시간이 출발시간의 3개월이후보다 늦은 경우
        if(trToDt.isAfter(trFromDt.plusMonths(3))){
            return "여행종료시간은 출발시간으로부터 3개월 이내로만 선택가능합니다.";
        }

        return "ok";
    }
}
