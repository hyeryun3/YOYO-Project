package yy.project.YOYO.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import yy.project.YOYO.argumentresolver.Login;
import yy.project.YOYO.domain.User;
import yy.project.YOYO.domain.UserTeam;
import yy.project.YOYO.service.CommentService;
import yy.project.YOYO.service.UserService;
import yy.project.YOYO.service.UserTeamService;
import yy.project.YOYO.vo.UserVO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserTeamService userTeamService;
    private final CommentService commentService;

    @GetMapping("/myPage")
    public String myPage(Model model, @Login User loginUser){
//        ==임시 로그인 ==
        User user = userService.findByUserID(loginUser.getUserID());

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
    public String modifyMyPage(@Login User loginUser,@ModelAttribute("user") UserVO userForm) throws Exception {
//        == 임시 로그인 ==
        userService.updateUser(userForm,loginUser);

        return "redirect:/";
    }

    @GetMapping("/deleteUser")
    public String deleteUser( @Login User loginUser,HttpServletRequest request){
        //        == 임시 로그인 ==
        commentService.deleteByWriterUID(loginUser.getUID());

        List<UserTeam> ut = userTeamService.findByUID(loginUser.getUID());
        if(ut.size()!=0){
            userTeamService.deleteByUID(loginUser.getUID());// UserTeam에서 삭제
        }
        userService.deleteUser(loginUser); // 회원리스트에서 삭제

        HttpSession session = request.getSession(false);
        if(session !=null){
            session.invalidate();
        }
        return "redirect:/";
    }

}
