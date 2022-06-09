package yy.project.YOYO.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import yy.project.YOYO.argumentresolver.Login;
import yy.project.YOYO.domain.User;

@Controller
@Slf4j
@RequiredArgsConstructor
public class HomeController {


    @GetMapping("/")
    public String home(Model model, @Login User loginUser){

        if(loginUser==null){
            model.addAttribute("user","x");
        }else{
            model.addAttribute("user","o");
        }
        return "home";
    }

    @ResponseBody
    @PostMapping("/getRole")
    public String getRole(@Login User loginUser){
        if(loginUser == null){
            return "notUser";
        }else {
            String admin = loginUser.getRole();
            return admin;
        }
    }
}
