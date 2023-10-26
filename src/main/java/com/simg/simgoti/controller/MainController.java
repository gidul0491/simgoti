package com.simg.simgoti.controller;

import com.simg.simgoti.service.Hanacrowling;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
//@RequestMapping("/simg")
@RequiredArgsConstructor
public class MainController {
    private final Hanacrowling hanacrowling;
    @RequestMapping("")
    public String index() throws Exception {

        return "index.html";
    }

    @RequestMapping(value = "/simg/simgOti/calculate/hana", method = RequestMethod.GET)
    public String calculateHana() throws Exception {
        hanacrowling.hanaPrem();
        return "done";
    }

    @RequestMapping(value = "/simg/simgOti/calculate", method = RequestMethod.GET)
    public String calculate() throws Exception {
        return "/calculate.html";
    }

    @RequestMapping(value = "/simg/simgOti/applyAlone", method = RequestMethod.GET)
    public String applyAlone() throws Exception {
        return "/apply1Alone.html";
    }

    @RequestMapping(value = "/simg/simgOti/applyMany", method = RequestMethod.GET)
    public String applyMany() throws Exception {
        return "/apply1Many.html";
    }

    @RequestMapping(value = "/simg/simgOti/applyPurpose", method = RequestMethod.GET)
    public String applyPurpose() throws Exception {
        return "/apply2.html";
    }

    @RequestMapping(value = "/simg/simgOti/applyFinal", method = RequestMethod.GET)
    public String applyFinal(HttpServletRequest req, HttpServletResponse resp) throws Exception {

        return "/apply3.html";
    }

    @RequestMapping(value = "/simg/simgOti/myPage", method = RequestMethod.GET)
    public String myPage(HttpServletRequest req, HttpServletResponse resp) throws Exception {

        // url이 같은 경우 웹브라우저에서 자체적으로 캐시를 사용하여 이전에 사용했던 html파일을 그대로 사용하는 경우가 있음
        // 그러면 myPage2.html을 반환해야 하는경우에 myPage1.html을 반환하여 로그인이 안됨
        // 그래서 response로 캐시를 사용하지말라는 의미로 아래 헤더를 설정함
        resp.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        resp.setHeader("Pragma", "no-cache");
        resp.setHeader("Expires", "0");

        HttpSession session = req.getSession();

        if(session.getAttribute("myPageClntPk") != null){
            return "/myPage2.html";
        }
        else{
            return "/myPage1.html";
        }
    }

    @RequestMapping(value = "/simg/simgOti/admin/apply", method = RequestMethod.GET)
    public String admin() throws Exception {
        return "/admin/adminApply.html";
    }

    @RequestMapping(value = "/simg/simgOti/admin/claim", method = RequestMethod.GET)
    public String adminClaim() throws Exception {
        return "/admin/adminClaim.html";
    }

    @RequestMapping(value = "/simg/simgOti/claim", method = RequestMethod.GET)
    public String claim() throws Exception{
        return "/claim.html";
    }
}
