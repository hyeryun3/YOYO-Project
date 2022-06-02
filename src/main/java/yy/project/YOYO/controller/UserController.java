package yy.project.YOYO.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import yy.project.YOYO.domain.User;
import yy.project.YOYO.domain.UserTeam;
import yy.project.YOYO.service.UserService;
import yy.project.YOYO.service.UserTeamService;
import yy.project.YOYO.vo.UserVO;

import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserTeamService userTeamService;

    @GetMapping("/myPage")
    public String myPage(Model model){
//        ==임시 로그인 ==
        User user = userService.findByUserID("rabbith3");

        UserVO vo = new UserVO();
        vo.setUserID(user.getUserID());
        vo.setPassword(user.getPassword());
        vo.setAddress(user.getAddress());
        vo.setUserName(user.getUserName());
        vo.setEmail(user.getEmail());

        String userImg = "";
        if(user.getUserImage()==null){
            userImg = "/adminImage/userIcon.png";
        }else{
            userImg = user.getUserImage();
        }

        model.addAttribute("user",user);
        model.addAttribute("userImg",userImg);

        return "myPage";
    }

    @PostMapping("/myPage")
    public String modifyMyPage(@ModelAttribute("user") UserVO userForm) throws Exception {
//        == 임시 로그인 ==
        userService.updateUser("rabbith3", userForm);

        return "redirect:/myPage";
    }

    @GetMapping("/deleteUser")
    public String deleteUser(){
        //        == 임시 로그인 ==
        User user = userService.findByUserID("rabbith3");
        List<UserTeam> ut = userTeamService.findByUID(user.getUID());
        if(ut.size()!=0){
            userTeamService.deleteByUID(user.getUID());// UserTeam에서 삭제
        }
        userService.deleteUser(user); // 회원리스트에서 삭제
        return "redirect:/";
    }

}
