package com.simg.simgoti.service;

import lombok.RequiredArgsConstructor;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.cookie.BasicCookieStore;
import org.apache.hc.client5.http.cookie.Cookie;
import org.apache.hc.client5.http.cookie.CookieStore;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.tomcat.Jar;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class Hanacrowling {
    private final CommonService commonService;
    String sessionID = "JSESSIONID=;";
    String urlPrem = "https://day.hanainsure.co.kr/p/join/travel/getInjrCalc.json";
    String urlSet = "https://day.hanainsure.co.kr/p/join/travel/setStepParam.json";
    String jsonbodyA = "{\"planCd\":\"A\",\"DelCovList\":[],\"injrIngrCovInfBcVo\":[{\"covCd\":\"151910\",\"covNm\":\"상해사망\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"100000000\",\"basSicDivCd\":\"1\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"151920\",\"covNm\":\"상해후유장해\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"100000000\",\"basSicDivCd\":\"1\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"154960\",\"covNm\":\"해외여행중 질병사망 및 80%이상 후유장해\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"10000000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"150690\",\"covNm\":\"해외 상해의료비\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"10000000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"155300\",\"covNm\":\"해외 질병의료비\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"10000000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"244250\",\"covNm\":\"국내 상해의료비(급여)\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"10000000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"244260\",\"covNm\":\"국내 상해의료비(비급여)\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"10000000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"244270\",\"covNm\":\"국내 질병의료비(급여)\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"10000000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"244280\",\"covNm\":\"국내 질병의료비(비급여)\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"10000000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"244290\",\"covNm\":\"국내 비급여3대특약\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"3500000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"100230\",\"covNm\":\"해외여행중 휴대품손해(분실제외)\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"200000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"201040\",\"covNm\":\"해외여행중 배상책임\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"10000000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"155840\",\"covNm\":\"해외여행중 중대사고 구조송환비용(14일이상)\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"30000000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"155570\",\"covNm\":\"항공기납치\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"1400000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"157400\",\"covNm\":\"해외여행중 여권분실후 재발급비용\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"67000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"}],\"compInjrIngrCovInfBcVo\":[{\"covCd\":\"151920\",\"covNm\":\"상해후유장해\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"100000000\",\"basSicDivCd\":\"1\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"150690\",\"covNm\":\"해외 상해의료비\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"10000000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"155300\",\"covNm\":\"해외 질병의료비\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"10000000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"244250\",\"covNm\":\"국내 상해의료비(급여)\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"10000000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"244260\",\"covNm\":\"국내 상해의료비(비급여)\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"10000000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"244270\",\"covNm\":\"국내 질병의료비(급여)\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"10000000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"244280\",\"covNm\":\"국내 질병의료비(비급여)\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"10000000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"244290\",\"covNm\":\"국내 비급여3대특약\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"3500000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"100230\",\"covNm\":\"해외여행중 휴대품손해(분실제외)\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"200000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"201040\",\"covNm\":\"해외여행중 배상책임\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"10000000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"155840\",\"covNm\":\"해외여행중 중대사고 구조송환비용(14일이상)\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"30000000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"155570\",\"covNm\":\"항공기납치\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"1400000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"157400\",\"covNm\":\"해외여행중 여권분실후 재발급비용\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"67000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"}]}";

    String jsonbodyB = "{\"planCd\":\"B\",\"DelCovList\":[],\"injrIngrCovInfBcVo\":[{\"covCd\":\"151910\",\"covNm\":\"상해사망\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"200000000\",\"basSicDivCd\":\"1\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"151920\",\"covNm\":\"상해후유장해\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"200000000\",\"basSicDivCd\":\"1\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"154960\",\"covNm\":\"해외여행중 질병사망 및 80%이상 후유장해\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"30000000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"150690\",\"covNm\":\"해외 상해의료비\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"20000000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"155300\",\"covNm\":\"해외 질병의료비\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"30000000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"244250\",\"covNm\":\"국내 상해의료비(급여)\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"30000000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"244260\",\"covNm\":\"국내 상해의료비(비급여)\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"30000000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"244270\",\"covNm\":\"국내 질병의료비(급여)\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"30000000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"244280\",\"covNm\":\"국내 질병의료비(비급여)\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"30000000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"244290\",\"covNm\":\"국내 비급여3대특약\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"3500000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"100230\",\"covNm\":\"해외여행중 휴대품손해(분실제외)\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"500000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"201040\",\"covNm\":\"해외여행중 배상책임\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"20000000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"157410\",\"covNm\":\"해외여행중 항공기 및 수하물 지연비용\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"100000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"157420\",\"covNm\":\"해외여행중 중단사고발생 추가비용\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"100000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"155840\",\"covNm\":\"해외여행중 중대사고 구조송환비용(14일이상)\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"30000000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"155570\",\"covNm\":\"항공기납치\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"1400000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"157400\",\"covNm\":\"해외여행중 여권분실후 재발급비용\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"67000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"360520\",\"covNm\":\"해외여행중 특정전염병 보장\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"100000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"360530\",\"covNm\":\"해외여행중 식중독 보장(2일이상 입원)\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"100000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"}],\"compInjrIngrCovInfBcVo\":[{\"covCd\":\"151920\",\"covNm\":\"상해후유장해\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"200000000\",\"basSicDivCd\":\"1\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"150690\",\"covNm\":\"해외 상해의료비\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"20000000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"155300\",\"covNm\":\"해외 질병의료비\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"30000000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"244250\",\"covNm\":\"국내 상해의료비(급여)\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"30000000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"244260\",\"covNm\":\"국내 상해의료비(비급여)\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"30000000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"244270\",\"covNm\":\"국내 질병의료비(급여)\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"30000000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"244280\",\"covNm\":\"국내 질병의료비(비급여)\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"30000000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"244290\",\"covNm\":\"국내 비급여3대특약\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"3500000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"100230\",\"covNm\":\"해외여행중 휴대품손해(분실제외)\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"500000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"201040\",\"covNm\":\"해외여행중 배상책임\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"20000000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"157410\",\"covNm\":\"해외여행중 항공기 및 수하물 지연비용\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"100000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"157420\",\"covNm\":\"해외여행중 중단사고발생 추가비용\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"100000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"155840\",\"covNm\":\"해외여행중 중대사고 구조송환비용(14일이상)\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"30000000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"155570\",\"covNm\":\"항공기납치\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"1400000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"157400\",\"covNm\":\"해외여행중 여권분실후 재발급비용\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"67000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"360520\",\"covNm\":\"해외여행중 특정전염병 보장\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"100000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"360530\",\"covNm\":\"해외여행중 식중독 보장(2일이상 입원)\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"100000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"}]}\n";
    private String jsonbodyC = "{\"planCd\":\"C\",\"DelCovList\":[],\"injrIngrCovInfBcVo\":[{\"covCd\":\"360560\",\"covNm\":\"해외 폭력상해피해 변호사선임비\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"5000000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"151910\",\"covNm\":\"상해사망\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"300000000\",\"basSicDivCd\":\"1\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"151920\",\"covNm\":\"상해후유장해\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"300000000\",\"basSicDivCd\":\"1\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"154960\",\"covNm\":\"해외여행중 질병사망 및 80%이상 후유장해\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"50000000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"150690\",\"covNm\":\"해외 상해의료비\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"30000000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"155300\",\"covNm\":\"해외 질병의료비\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"50000000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"244250\",\"covNm\":\"국내 상해의료비(급여)\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"50000000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"244260\",\"covNm\":\"국내 상해의료비(비급여)\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"50000000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"244270\",\"covNm\":\"국내 질병의료비(급여)\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"50000000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"244280\",\"covNm\":\"국내 질병의료비(비급여)\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"50000000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"244290\",\"covNm\":\"국내 비급여3대특약\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"3500000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"100230\",\"covNm\":\"해외여행중 휴대품손해(분실제외)\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"1000000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"201040\",\"covNm\":\"해외여행중 배상책임\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"30000000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"157410\",\"covNm\":\"해외여행중 항공기 및 수하물 지연비용\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"200000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"157420\",\"covNm\":\"해외여행중 중단사고발생 추가비용\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"200000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"155841\",\"covNm\":\"해외여행중 중대사고 구조송환비용(7일이상)\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"30000000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"155570\",\"covNm\":\"항공기납치\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"1400000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"157400\",\"covNm\":\"해외여행중 여권분실후 재발급비용\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"67000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"360520\",\"covNm\":\"해외여행중 특정전염병 보장\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"200000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"360530\",\"covNm\":\"해외여행중 식중독 보장(2일이상 입원)\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"200000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"}],\"compInjrIngrCovInfBcVo\":[{\"covCd\":\"360560\",\"covNm\":\"해외 폭력상해피해 변호사선임비\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"5000000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"151920\",\"covNm\":\"상해후유장해\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"300000000\",\"basSicDivCd\":\"1\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"150690\",\"covNm\":\"해외 상해의료비\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"30000000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"155300\",\"covNm\":\"해외 질병의료비\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"50000000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"244250\",\"covNm\":\"국내 상해의료비(급여)\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"50000000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"244260\",\"covNm\":\"국내 상해의료비(비급여)\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"50000000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"244270\",\"covNm\":\"국내 질병의료비(급여)\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"50000000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"244280\",\"covNm\":\"국내 질병의료비(비급여)\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"50000000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"244290\",\"covNm\":\"국내 비급여3대특약\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"3500000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"100230\",\"covNm\":\"해외여행중 휴대품손해(분실제외)\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"1000000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"201040\",\"covNm\":\"해외여행중 배상책임\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"30000000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"157410\",\"covNm\":\"해외여행중 항공기 및 수하물 지연비용\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"200000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"157420\",\"covNm\":\"해외여행중 중단사고발생 추가비용\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"200000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"155841\",\"covNm\":\"해외여행중 중대사고 구조송환비용(7일이상)\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"30000000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"155570\",\"covNm\":\"항공기납치\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"1400000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"157400\",\"covNm\":\"해외여행중 여권분실후 재발급비용\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"67000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"360520\",\"covNm\":\"해외여행중 특정전염병 보장\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"200000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"360530\",\"covNm\":\"해외여행중 식중독 보장(2일이상 입원)\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"200000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"}]}\n";

    String jsonbodyG = "{\"planCd\":\"G\",\"childUser\":\"Y\",\"DelCovList\":[],\"injrIngrCovInfBcVo\":[{\"covCd\":\"151920\",\"covNm\":\"상해후유장해\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"100000000\",\"basSicDivCd\":\"1\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"150690\",\"covNm\":\"해외 상해의료비\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"10000000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"155300\",\"covNm\":\"해외 질병의료비\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"10000000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"244250\",\"covNm\":\"국내 상해의료비(급여)\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"10000000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"244260\",\"covNm\":\"국내 상해의료비(비급여)\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"10000000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"244270\",\"covNm\":\"국내 질병의료비(급여)\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"10000000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"244280\",\"covNm\":\"국내 질병의료비(비급여)\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"10000000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"244290\",\"covNm\":\"국내 비급여3대특약\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"3500000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"100230\",\"covNm\":\"해외여행중 휴대품손해(분실제외)\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"200000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"201040\",\"covNm\":\"해외여행중 배상책임\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"10000000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"155840\",\"covNm\":\"해외여행중 중대사고 구조송환비용(14일이상)\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"30000000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"155570\",\"covNm\":\"항공기납치\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"1400000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"157400\",\"covNm\":\"해외여행중 여권분실후 재발급비용\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"67000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"}],\"compInjrIngrCovInfBcVo\":[{\"covCd\":\"151920\",\"covNm\":\"상해후유장해\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"100000000\",\"basSicDivCd\":\"1\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"150690\",\"covNm\":\"해외 상해의료비\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"10000000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"155300\",\"covNm\":\"해외 질병의료비\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"10000000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"244250\",\"covNm\":\"국내 상해의료비(급여)\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"10000000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"244260\",\"covNm\":\"국내 상해의료비(비급여)\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"10000000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"244270\",\"covNm\":\"국내 질병의료비(급여)\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"10000000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"244280\",\"covNm\":\"국내 질병의료비(비급여)\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"10000000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"244290\",\"covNm\":\"국내 비급여3대특약\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"3500000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"100230\",\"covNm\":\"해외여행중 휴대품손해(분실제외)\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"200000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"201040\",\"covNm\":\"해외여행중 배상책임\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"10000000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"155840\",\"covNm\":\"해외여행중 중대사고 구조송환비용(14일이상)\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"30000000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"155570\",\"covNm\":\"항공기납치\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"1400000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"157400\",\"covNm\":\"해외여행중 여권분실후 재발급비용\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"67000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"}]}\n";

    String jsonbodyH = "{\"planCd\":\"H\",\"childUser\":\"Y\",\"DelCovList\":[],\"injrIngrCovInfBcVo\":[{\"covCd\":\"151920\",\"covNm\":\"상해후유장해\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"200000000\",\"basSicDivCd\":\"1\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"150690\",\"covNm\":\"해외 상해의료비\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"20000000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"155300\",\"covNm\":\"해외 질병의료비\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"30000000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"244250\",\"covNm\":\"국내 상해의료비(급여)\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"30000000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"244260\",\"covNm\":\"국내 상해의료비(비급여)\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"30000000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"244270\",\"covNm\":\"국내 질병의료비(급여)\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"30000000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"244280\",\"covNm\":\"국내 질병의료비(비급여)\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"30000000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"244290\",\"covNm\":\"국내 비급여3대특약\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"3500000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"100230\",\"covNm\":\"해외여행중 휴대품손해(분실제외)\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"500000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"201040\",\"covNm\":\"해외여행중 배상책임\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"20000000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"157410\",\"covNm\":\"해외여행중 항공기 및 수하물 지연비용\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"100000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"157420\",\"covNm\":\"해외여행중 중단사고발생 추가비용\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"100000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"155840\",\"covNm\":\"해외여행중 중대사고 구조송환비용(14일이상)\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"30000000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"155570\",\"covNm\":\"항공기납치\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"1400000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"157400\",\"covNm\":\"해외여행중 여권분실후 재발급비용\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"67000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"360520\",\"covNm\":\"해외여행중 특정전염병 보장\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"100000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"360530\",\"covNm\":\"해외여행중 식중독 보장(2일이상 입원)\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"100000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"}],\"compInjrIngrCovInfBcVo\":[{\"covCd\":\"151920\",\"covNm\":\"상해후유장해\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"200000000\",\"basSicDivCd\":\"1\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"150690\",\"covNm\":\"해외 상해의료비\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"20000000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"155300\",\"covNm\":\"해외 질병의료비\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"30000000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"244250\",\"covNm\":\"국내 상해의료비(급여)\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"30000000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"244260\",\"covNm\":\"국내 상해의료비(비급여)\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"30000000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"244270\",\"covNm\":\"국내 질병의료비(급여)\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"30000000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"244280\",\"covNm\":\"국내 질병의료비(비급여)\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"30000000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"244290\",\"covNm\":\"국내 비급여3대특약\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"3500000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"100230\",\"covNm\":\"해외여행중 휴대품손해(분실제외)\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"500000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"201040\",\"covNm\":\"해외여행중 배상책임\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"20000000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"157410\",\"covNm\":\"해외여행중 항공기 및 수하물 지연비용\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"100000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"157420\",\"covNm\":\"해외여행중 중단사고발생 추가비용\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"100000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"155840\",\"covNm\":\"해외여행중 중대사고 구조송환비용(14일이상)\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"30000000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"155570\",\"covNm\":\"항공기납치\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"1400000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"157400\",\"covNm\":\"해외여행중 여권분실후 재발급비용\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"67000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"360520\",\"covNm\":\"해외여행중 특정전염병 보장\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"100000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"360530\",\"covNm\":\"해외여행중 식중독 보장(2일이상 입원)\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"100000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"}]}\n";

    String jsonbodyI = "{\"planCd\":\"I\",\"childUser\":\"Y\",\"DelCovList\":[],\"injrIngrCovInfBcVo\":[{\"covCd\":\"360560\",\"covNm\":\"해외 폭력상해피해 변호사선임비\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"5000000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"151920\",\"covNm\":\"상해후유장해\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"300000000\",\"basSicDivCd\":\"1\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"150690\",\"covNm\":\"해외 상해의료비\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"30000000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"155300\",\"covNm\":\"해외 질병의료비\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"50000000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"244250\",\"covNm\":\"국내 상해의료비(급여)\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"50000000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"244260\",\"covNm\":\"국내 상해의료비(비급여)\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"50000000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"244270\",\"covNm\":\"국내 질병의료비(급여)\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"50000000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"244280\",\"covNm\":\"국내 질병의료비(비급여)\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"50000000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"244290\",\"covNm\":\"국내 비급여3대특약\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"3500000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"100230\",\"covNm\":\"해외여행중 휴대품손해(분실제외)\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"1000000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"201040\",\"covNm\":\"해외여행중 배상책임\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"30000000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"157410\",\"covNm\":\"해외여행중 항공기 및 수하물 지연비용\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"200000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"157420\",\"covNm\":\"해외여행중 중단사고발생 추가비용\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"200000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"155841\",\"covNm\":\"해외여행중 중대사고 구조송환비용(7일이상)\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"30000000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"155570\",\"covNm\":\"항공기납치\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"1400000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"157400\",\"covNm\":\"해외여행중 여권분실후 재발급비용\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"67000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"360520\",\"covNm\":\"해외여행중 특정전염병 보장\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"200000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"360530\",\"covNm\":\"해외여행중 식중독 보장(2일이상 입원)\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"200000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"}],\"compInjrIngrCovInfBcVo\":[{\"covCd\":\"360560\",\"covNm\":\"해외 폭력상해피해 변호사선임비\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"5000000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"151920\",\"covNm\":\"상해후유장해\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"300000000\",\"basSicDivCd\":\"1\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"150690\",\"covNm\":\"해외 상해의료비\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"30000000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"155300\",\"covNm\":\"해외 질병의료비\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"50000000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"244250\",\"covNm\":\"국내 상해의료비(급여)\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"50000000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"244260\",\"covNm\":\"국내 상해의료비(비급여)\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"50000000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"244270\",\"covNm\":\"국내 질병의료비(급여)\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"50000000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"244280\",\"covNm\":\"국내 질병의료비(비급여)\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"50000000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"244290\",\"covNm\":\"국내 비급여3대특약\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"3500000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"100230\",\"covNm\":\"해외여행중 휴대품손해(분실제외)\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"1000000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"201040\",\"covNm\":\"해외여행중 배상책임\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"30000000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"157410\",\"covNm\":\"해외여행중 항공기 및 수하물 지연비용\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"200000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"157420\",\"covNm\":\"해외여행중 중단사고발생 추가비용\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"200000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"155841\",\"covNm\":\"해외여행중 중대사고 구조송환비용(7일이상)\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"30000000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"155570\",\"covNm\":\"항공기납치\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"1400000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"157400\",\"covNm\":\"해외여행중 여권분실후 재발급비용\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"67000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"360520\",\"covNm\":\"해외여행중 특정전염병 보장\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"200000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"},{\"covCd\":\"360530\",\"covNm\":\"해외여행중 식중독 보장(2일이상 입원)\",\"sbcAmtCurCd\":\"KRW\",\"sbcAmt\":\"200000\",\"basSicDivCd\":\"2\",\"rowStat\":\"2\",\"premMnulInpDivCd\":\"2\"}]}\n";

    public void hanaPrem() throws Exception{

        File file = new File("D:/hanaPrems.txt");
        PrintWriter writer = new PrintWriter(file);

        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyyMMddHHmm");
        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyyMMdd");


        LocalDateTime from = LocalDateTime.of(2023,11,01,00,00);
        LocalDateTime to = from;




        System.out.println("| insAge | gender |            period           | days |  practical  |  standard   |   advanced  | ");
        writer.println("| insAge | gender |            period           | days |  practical  |  standard   |   advanced  | ");

        for(int l=1; l<=2; l++){
            String gen = String.valueOf(l);
            String gender = "M";
            if(l==2){
                gender = "F";
            }
            LocalDate birth0 = LocalDate.of(2024, 10,1);
            for(int k=81; k<82; k++){
                final RestTemplate restTemplate = getCookie();
                String premA0 = "0";
                String premB0 = "0";
                String premC0 = "0";
                String premA = "1";
                String premB = "1";
                String premC = "1";
                LocalDate birthDt = birth0.minusYears(k);
                String birth = birthDt.format(formatter2);
                int insAge = commonService.calculateInsAge(birth);

//                for(int j=0; j<90; j++){
                int[] borderDay = {1,3,4,5,6,7,8,11,15,18,22,25,28,31,46,62,89};
                for(int j : borderDay ){

                    to = from.plusDays(j);
                    String fromDt = from.format(formatter1);
                    String toDt = to.format(formatter1);
                    String period = String.valueOf(commonService.getPeriod(fromDt, toDt));

                    // 여행기간, 나이, 성별 설정
                    if(this.hanaSet(restTemplate, fromDt,toDt, birth, gen).equals("true")){
//                    System.out.printf("-");
                    }

                    // 요청 헤더 설정
                    HttpHeaders headers = new HttpHeaders();
                    headers.set("Cookie",sessionID);
                    headers.set("Content-Type","application/json;charset=UTF-8");
//        headers.set("","");

                    for(int i=0; i<3; i++){
                        String jsonbody = null;

                        if(insAge >= 19){
                            switch (i){
                                case 0:jsonbody = jsonbodyA;break;
                                case 1:jsonbody = jsonbodyB;break;
                                case 2:jsonbody = jsonbodyC;break;
                            }
                        }
                        else{
                            switch (i){
                                case 0:jsonbody = jsonbodyG;break;
                                case 1:jsonbody = jsonbodyH;break;
                                case 2:jsonbody = jsonbodyI;break;
                            }
                        }

                        // HttpEntity를 사용하여 헤더와 본문을 함께 설정
                        HttpEntity<String> requestEntity = new HttpEntity<>(jsonbody, headers);
                        // POST 요청 보내기
                        ResponseEntity<String> response = restTemplate.exchange(urlPrem, HttpMethod.POST, requestEntity, String.class);
                        // 응답 파싱후 보험료 반환
                        JSONParser jsonParser = new JSONParser();
                        JSONObject jsonObject = (JSONObject) jsonParser.parse(response.getBody().toString());
                        JSONObject obj1 = (JSONObject)jsonObject.get("body");
                        JSONObject obj2 = (JSONObject)obj1.get("ctrCovCtrCndInfBcVo");
                        String prem = obj2.get("aplPrem").toString();
                        switch (i){
                            case 0:premA0 = premA;premA = prem;break;
                            case 1:premB0 = premB;premB = prem;break;
                            case 2:premC0 = premC;premC = prem;break;
                        }
                    }
                    if((!premA.equals(premA0)) || (!premB.equals(premB0)) || (!premC.equals(premC0))){
                        System.out.printf("| %6s | %6s | %s ~ %s | %4s | ", String.valueOf(insAge), gender, fromDt, toDt, period);
                        System.out.printf("%11s | %11s | %11S |\n", premA, premB, premC);

                        writer.printf("| %6s | %6s | %s ~ %s | %4s | ", String.valueOf(insAge), gender, fromDt, toDt, period);
                        writer.printf("%11s | %11s | %11S |\n", premA, premB, premC);
                    }

                }
            }
        }
        writer.close();
    }

    public String hanaSet(RestTemplate restTemplate, String fromDt, String toDt, String birth, String gen) throws Exception{
        // 요청 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.set("Cookie",sessionID);
        headers.set("Content-Type","application/json;charset=UTF-8");
//        headers.set("","");

        // 여행일자,나이,성별 세팅
        String travelerInfo = "{\n" +
                "    \"stepType\": \"joinTravel0102\",\n" +
                "    \"sFmdt\": \""+fromDt.substring(0,8)+"\",\n" +
                "    \"sFmTime\": \""+fromDt.substring(8,12)+"\",\n" +
                "    \"sApplyTime\": \"0000\",\n" +
                "    \"sTodt\": \""+toDt.substring(0,8)+"\",\n" +
                "    \"sToTime\": \""+toDt.substring(8,12)+"\",\n" +
                "    \"sTripPurpose\": \"01\",\n" +
                "    \"sTripPurposeNm\": \"여행 / 관광\",\n" +
                "    \n" +
                "    \"insrdList\": [\n" +
                "        {\n" +
                "            \"sBirth\": \""+birth+"\",\n" +
                "            \"sSex\": \""+gen+"\"\n" +
                "        }\n" +
                "    ]\n" +
                "}";

        // HttpEntity를 사용하여 헤더와 본문을 함께 설정
        HttpEntity<String> requestEntity = new HttpEntity<>(travelerInfo, headers);
        // POST 요청 보내기
        ResponseEntity<String> response = restTemplate.exchange(urlSet, HttpMethod.POST, requestEntity, String.class);

        // 응답 파싱후 보험료 반환
//        System.out.println(response);
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(response.getBody().toString());
        JSONObject obj1 = (JSONObject)jsonObject.get("header");
        String result = obj1.get("success").toString();
        return result;
    }

    public RestTemplate getCookie(){
        try {
            CookieStore cookieStore = new BasicCookieStore();
            CloseableHttpClient httpClient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();

            HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
            requestFactory.setHttpClient(httpClient);

            RestTemplate restTemplate = new RestTemplate(requestFactory);

            HttpGet httpGet = new HttpGet("https://day.hanainsure.co.kr/p/join/travel?sInsTypeName=overseas&sAffiliatedConcernKey=&sValue1=&covidUser=N"); // 원하는 URL을 입력하세요
            CloseableHttpResponse response = httpClient.execute(httpGet);
            try {
                // 응답 처리 로직이 필요한 경우 여기에 작성

                // 모든 쿠키 얻기
                List<Cookie> cookies = cookieStore.getCookies();
                for (Cookie cookie : cookies) {
                    if ("JSESSIONID".equals(cookie.getName())) {
                        sessionID = "JSESSIONID=IabY9X1uUjHwEImBTapW22IWgBfinaGmCq9FfWR0Pght2bE9nNPpDpMoFT9YcHMb.amV1c19kb21haW4vRGZyb250;";
                        sessionID = "JSESSIONID="+cookie.getValue()+";";
                        System.out.println(sessionID);
                    }
                }
            } finally {
                response.close();
            }
            return restTemplate;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
