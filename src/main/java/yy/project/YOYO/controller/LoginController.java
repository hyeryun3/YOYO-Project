package yy.project.YOYO.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
//import org.json.simple.JSONObject;
//import org.json.simple.parser.JSONParser;
//import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import yy.project.YOYO.argumentresolver.Login;
import yy.project.YOYO.domain.User;
import yy.project.YOYO.interceptor.SessionConst;
import yy.project.YOYO.service.UserService;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@Slf4j
public class LoginController {

    private final UserService userService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @ResponseBody
    @PostMapping("/loginUser")
    public String loginCheck(HttpServletRequest request) {

        String id = request.getParameter("id");
        String pw = request.getParameter("pw");

        log.info("id : {}", id);
        log.info("pw : {}", pw);

        User loginUser = userService.login(id, pw);

        if (loginUser == null) {
            return "아이디 또는 비밀번호가 일치하지 않습니다.";
        } else {

            HttpSession session = request.getSession();
            // 세션에 LOGIN_USER라는 이름(SessionConst.class에 LOGIN_USER값을 "loginUser")을 가진 상자에 loginUser 객체를 담음.
            // 즉, 로그인 회원 정보를 세션에 담아놓는다.
            session.setAttribute(SessionConst.LOGIN_USER, loginUser);
            // users/login으로 매핑하는 컨트롤러를 찾아간다. (HomeController에 있다)

            log.info("role : {}",loginUser.getRole());
            String role= loginUser.getRole();
            if (role.equals("admin")) {
                log.info("-----------------------------admin 접속완료---------------------------");
                return "admin";
            }
            log.info("-----------------------------custom 접속완료---------------------------");
            return "custom";

        }
    }


    @ResponseBody
    @PostMapping("/checkUser")
    public int checkUser(@Login User loginUser){
        if(loginUser!=null)return 0;
        else return 1;

    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        //세션 값을 담아온다.
        log.info("로그아웃");
        HttpSession session = request.getSession(false);

        //현재 담겨져있는 세션값이 존재한다면 세션을 드랍한다.
        if(session !=null){
            session.invalidate();
        }
        // 로그인 페이지로 이동
        return "redirect:/login";
    }
}

