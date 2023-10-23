package com.simg.simgoti.service;

import com.simg.simgoti.mapper.CommonMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class CommonServiceImpl implements CommonService {
    private final CommonMapper commonMapper;
    private final HanaPremiumImpl hanaPremium;

    public String selectPolNoByCovCode(int covCode) throws Exception {
        return commonMapper.selectPolNoByCovCode(covCode);
    }

    // 생년월일 8자리를 보험나이로 변환, 유효성검사도 가능  + 6자리를 입력했을때도 변환가능하게 기능추가
    public int calculateInsAge(String birth) throws Exception{

        int year=1997;
        int mth=12;
        int dt=5;
        LocalDate now = LocalDate.now();
        if(birth.length() == 8){
            year = Integer.parseInt(birth.substring(0,4));
            mth = Integer.parseInt(birth.substring(4,6));
            dt = Integer.parseInt(birth.substring(6,8));
        }
        else if(birth.length()==6){
            int yr = Integer.parseInt(birth.substring(0,2));
            int nowYr = now.getYear() % 100;
            int nowCentury = now.getYear() - nowYr;
            if(yr > nowYr){
                year = nowCentury -100 + yr;
            }
            else{
                year = nowCentury + yr;
            }
            mth = Integer.parseInt(birth.substring(2,4));
            dt = Integer.parseInt(birth.substring(4,6));
        }

        LocalDate birthday = LocalDate.of(year, mth, dt);

        // 보험나이는 생년월일에 -6개월을 한 뒤 구한 만나이
        birthday = birthday.minusMonths(6);

        int insAge = now.getYear() - birthday.getYear();

        // 월 비교
        if(mth > now.getMonthValue()){
            insAge--;
        }
        else if(mth == now.getMonthValue() && dt > now.getDayOfMonth()){
            insAge--;
        }
//        System.out.println("insAge : " + insAge);
        return insAge;
    }

    // 생년월일 8자리를 만나이로 변환, 유효성검사도 가능  + 6자리를 입력했을때도 변환가능하게 기능추가
    public int calculateManAge(String birth) throws Exception{

        int year=1997;
        int mth=12;
        int dt=5;
        LocalDate now = LocalDate.now();
        if(birth.length() == 8){
            year = Integer.parseInt(birth.substring(0,4));
            mth = Integer.parseInt(birth.substring(4,6));
            dt = Integer.parseInt(birth.substring(6,8));
        }
        else if(birth.length()==6){
            int yr = Integer.parseInt(birth.substring(0,2));
            int nowYr = now.getYear() % 100;
            int nowCentury = now.getYear() - nowYr;
            if(yr > nowYr){
                year = nowCentury -100 + yr;
            }
            else{
                year = nowCentury + yr;
            }
            mth = Integer.parseInt(birth.substring(2,4));
            dt = Integer.parseInt(birth.substring(4,6));
        }

        LocalDate birthday = LocalDate.of(year, mth, dt);

        int manAge = now.getYear() - birthday.getYear();

        // 월 비교
        if(mth > now.getMonthValue()){
            manAge--;
        }
        else if(mth == now.getMonthValue() && dt > now.getDayOfMonth()){
            manAge--;
        }

        return manAge;
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

    @Override
    public int getPeriod(String startDt, String endDt) {
        int result = 1;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        if(startDt.length() > 16){
            formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        }
        if(startDt.length()==12){
            formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm");
        }
        LocalDateTime startLdt = LocalDateTime.parse(startDt, formatter);
        LocalDateTime endLdt = LocalDateTime.parse(endDt, formatter);
        Duration duration = Duration.between(startLdt, endLdt);
        long seconds = duration.getSeconds();
        result = (int) Math.ceil( (double) seconds / ((24 * 60 * 60)) );
        return result;
    }

    @Override
    public char getGen(String clntJuminB){
        int num = Integer.parseInt(clntJuminB.substring(0,1));
        if(num % 2 == 1){
            return 'M';
        }
        else{
            return 'F';
        }
    }

    @Override
    public String attachJosa(String word){
        char lastChar = word.charAt(word.length()-1);
        char josa = '을';
        if((lastChar - '가') % 28 == 0){
            josa = '를';
        }
        return word+josa;
    }
}
