package yy.project.YOYO.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import yy.project.YOYO.argumentresolver.Login;
import yy.project.YOYO.domain.Comment;
import yy.project.YOYO.domain.Team;
import yy.project.YOYO.domain.User;
import yy.project.YOYO.domain.UserTeam;
import yy.project.YOYO.service.CommentService;
import yy.project.YOYO.service.TeamService;
import yy.project.YOYO.service.UserService;
import yy.project.YOYO.service.UserTeamService;
import yy.project.YOYO.vo.CommentVO;
import yy.project.YOYO.vo.MeetingVO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static yy.project.YOYO.controller.MeetingController.meetingvo;

@Slf4j
@Controller
@RequiredArgsConstructor
public class CommentController {


    private final CommentService commentService;
    private final UserService userService;
    private final TeamService teamService;
    private final UserTeamService userTeamService;

//    @GetMapping("/viewMeeting/{tID}")
//    public String viewMeeting(){
//
//        return "viewMeeting";
//    }

    @GetMapping("/viewMeeting/{tID}")
    public String viewMeeting(@PathVariable("tID") Long tID,Model model,@Login User loginUser){
        Team team = teamService.findBytID(tID);
        meetingvo.setTID(tID);
        System.out.println(team.getTeamName());
        model.addAttribute("team",team);

        List<UserTeam> ut = userTeamService.findByTID(tID);
        List<String> userIDs = new ArrayList<>();
        for(int i=0; i<ut.size(); i++){
            if(!ut.get(i).getUser().getUserID().equals(loginUser.getUserID())) {
                userIDs.add(ut.get(i).getUser().getUserID());
            }
        }

        model.addAttribute("teamName",team.getTeamName());
        model.addAttribute("user", loginUser.getUserID());
        model.addAttribute("userIDs",userIDs);
        model.addAttribute("date", team.getDate());
        model.addAttribute("tID",tID);

        return "viewMeeting";
    }



    @ResponseBody
    @GetMapping("/findComment")
    public List<CommentVO> team() {

        return commentService.findComment(meetingvo.getTID());

    }


    @ResponseBody
    @GetMapping("/saveComment")
    public CommentVO saveComment(@Login User loginUser, @RequestParam String comment) {

        Comment comment1 = new Comment();
        comment1.setCommentContent(comment);
        comment1.setWriterUID(loginUser.getUID());
        comment1.setTeam(teamService.findBytID2(meetingvo.getTID()));
        Long cmID = commentService.save(comment1);
        Comment resComment = commentService.findBycmID(cmID);

        CommentVO vo = new CommentVO();
        vo.setCmID(cmID);
        vo.setWriterName(loginUser.getUserName());
        vo.setTbID(meetingvo.getTID());
        vo.setCommentContent(comment);

        return vo;
    }


    @ResponseBody
    @GetMapping("/deleteCommentCheck")
    public boolean deleteCommentCheck(@RequestParam Long cmID, @Login User loginUser) {
        if (loginUser == null) {
            return false;
        }
        //댓글 주인이거나, 그 글의 글쓴이거나 둘 중 하나
        else if (loginUser.getUID() == commentService.findBycmID(cmID).getWriterUID()) {
            return true;
        } else return false;
    }


    @ResponseBody
    @GetMapping("/deleteComment")
    public boolean deleteComment(@RequestParam Long cmID) {

        commentService.delete(cmID);

        return true;
    }

    @ResponseBody
    @GetMapping("/updateComment")
    public boolean updateComment(@RequestParam Long cmID) {
        return true;
    }

    @ResponseBody
    @GetMapping("/updateCommentCheck")
    public boolean updateCommentCheck(@RequestParam Long cmID, @Login User loginUser) {
        if (loginUser == null) {
            return false;
        } else {
            return commentService.updateCommentCheck(cmID, loginUser.getUID());
        }
    }


    @ResponseBody
    @GetMapping("/updateCommentSave")
    public boolean updateCommentSave(@RequestParam Long cmID, @RequestParam String commentContent) {

        commentService.updateCommentSave(cmID, commentContent);
        return true;
    }

    static Long stid;

    @ResponseBody
    @PostMapping("/locationMap")
    public void locationMap(@RequestParam("tid") Long tid){
        stid = tid;
    }

    @ResponseBody
    @PostMapping("/mapData")
    public void mapData(@RequestParam("x") String x,@RequestParam("y") String y){
        Team team = teamService.findBytID(stid);
        team.setPlaceX(x);
        team.setPlaceY(y);
        teamService.save(team);
    }
}
