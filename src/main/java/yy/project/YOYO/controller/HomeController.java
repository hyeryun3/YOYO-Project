package yy.project.YOYO.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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
        return "home";}
}
