package yy.project.YOYO.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import yy.project.YOYO.domain.User;
import yy.project.YOYO.service.UserService;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class FindController {

    private final UserService userService;

    @GetMapping(value="/findID")
    public String findId() {
        return "findID";
    }

    @ResponseBody
    @PostMapping(value="/findID")
    public String findIdRes(@RequestParam("name") String userName, @RequestParam("email") String email) {

        User user = userService.findByUserNameAndEmail(userName, email);
        if( user == null){
            return "no";
        }else{
            return user.getUserID();
        }
    }

    @GetMapping(value="/findPW")
    public String findPW() {
        return "findPW";
    }

    @ResponseBody
    @PostMapping("/findPW")
    public String findPw2(@RequestParam("name") String userName, @RequestParam("email") String email, @RequestParam("id") String id) {

        User user = userService.findByUserNameAndEmail(userName,email);
        if(user.getUserID().equals(id)) {
            List<User> userInfo = userService.findByEmail(email);
            if (userInfo.size() != 0) {
                userInfo.get(0).getPassword();
                String tempPW = userService.getTempPW();
                user.setPassword(tempPW);
                userService.save(user); // 임시번호 DB변경
                userService.mailToPW(userName, email, tempPW); // 메일발송

                return tempPW;

            } else {
                return "no";
            }
        }else{
            return "no";
        }
    }
}
