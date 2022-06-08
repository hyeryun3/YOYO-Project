package yy.project.YOYO.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import yy.project.YOYO.argumentresolver.Login;
import yy.project.YOYO.domain.User;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MapController {
    @GetMapping("/map")
    public String map(){
        return "map";
    }

}
