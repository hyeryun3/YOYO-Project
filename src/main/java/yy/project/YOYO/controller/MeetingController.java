package yy.project.YOYO.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import yy.project.YOYO.domain.Team;
import yy.project.YOYO.domain.User;
import yy.project.YOYO.domain.UserTeam;
import yy.project.YOYO.service.TeamService;
import yy.project.YOYO.service.UserService;
import yy.project.YOYO.service.UserTeamService;
import yy.project.YOYO.form.TeamForm;
import yy.project.YOYO.vo.MeetingVO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@Slf4j
@RequiredArgsConstructor
public class MeetingController {

    private final UserService userService;
    private final TeamService teamService;
    private final UserTeamService userTeamService;


    @GetMapping("/createMeeting")
    public String home(Model model){
        model.addAttribute("user", "rabbith3");
        return "createMeeting";
    }

    @ResponseBody
    @PostMapping("/validateMem")
    public int validateMem(@RequestParam("sendData") String id){
        int res = userService.memberCheck(id);
        log.info("res : {}", res);
        return res;
    }

    @ResponseBody
    @PostMapping("/createMeeting")
    public void createMeeting(TeamForm teamForm){
        Team team = new Team();
        System.out.println("================"+teamForm.getMeetingDate());
        String getDates = teamForm.getMeetingDate().replace("T"," ");

//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//        LocalDateTime dateTime = LocalDateTime.parse(teamForm.getMeetingDate());
        LocalDateTime dateTime = LocalDateTime.parse(getDates,DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        System.out.println(teamForm.getWritePlace());
        team.setDate(dateTime);
        team.setTeamName(teamForm.getMeetingName());
        if(teamForm.getWritePlace()!=""){
            team.setPlace(teamForm.getWritePlace());
        }else{
            team.setPlace("중간 지점");
        }
        Team saveTeam = teamService.save(team);

        UserTeam ut = new UserTeam();

//        == 임시 로그인 계정 ==
        User loginuser = userService.findByUserID("rabbith3");
        ut.setUser(loginuser);
        ut.setTeam(saveTeam);
        ut.setBoss(true);
        userTeamService.save(ut);

        for (int i = 0; i < teamForm.getUsers().length; i++) {
            if( teamForm.getUsers()[i] != ""){
                // 추가한 구성원의 정보
                User userInfo = userService.findByUserID(teamForm.getUsers()[i]);

                // 그룹의 구성원이 추가되었으므로, 유저-팀테이블을 업데이트 해준다.
                ut = new UserTeam();
                ut.setTeam(saveTeam);
                ut.setUser(userInfo);
                ut.setBoss(false);
                // 유저-팀 객체를 UserTeam 테이블에 저장
                userTeamService.save(ut);
            }
        }

    }

    @GetMapping("/checkMeeting")
    public String checkMeeting(){
        return "checkMeeting";
    }


    @ResponseBody
    @GetMapping("/findMeeting")
    public List<MeetingVO> findMeeting(Model model){
//        임시 로그인
        User user = userService.findByUserID("rabbith3");
        List<UserTeam> myTeams = userTeamService.findByUID(user.getUID());
        model.addAttribute("user",user);

        MeetingVO vo = null;
        List<MeetingVO> voList = new ArrayList<>();

        for(int i=0; i<myTeams.size(); i++){
            vo = new MeetingVO();
            Team team = myTeams.get(i).getTeam();
            if(team.getDate().isAfter(LocalDateTime.now())) {
                Long tid = team.getTID();
                vo.setPlace(team.getPlace());
                vo.setTeamName(team.getTeamName());
                vo.setTime(team.getDate());
                List<UserTeam> byTID = userTeamService.findByTID(tid);
                List<String> mem = new ArrayList<>();
                for (int j = 0; j < byTID.size(); j++) {
                    if (byTID.get(j).getUser().getUserImage() == null) {
                        mem.add("/adminImage/userIcon.png");
                    } else {
                        mem.add(byTID.get(j).getUser().getUserImage());
                    }
                }
                vo.setMembers(mem);
                vo.setTID(tid);
                voList.add(vo);
            }
        }

        voList = voList.stream().sorted(Comparator.comparing(MeetingVO::getTime)).collect(Collectors.toList());

        return voList;
    }

    @GetMapping("/meeting/{tID}")
    public String meeting(@PathVariable("tID") Long tID,Model model){
        Team team = teamService.findBytID(tID);
        System.out.println(team.getTeamName());
        model.addAttribute("team",team);

        List<UserTeam> ut = userTeamService.findByTID(tID);
        List<String> userIDs = new ArrayList<>();
        for(int i=0; i<ut.size(); i++){
            if(!ut.get(i).getUser().getUserID().equals("rabbith3")) {
                userIDs.add(ut.get(i).getUser().getUserID());
            }
        }
        model.addAttribute("user", "rabbith3");
        model.addAttribute("userIDs",userIDs);
        return "editMeeting";
    }

    @PostMapping("/editMeeting/{tID}")
    public String editMeeting(TeamForm teamForm, @PathVariable("tID") Long tID){
        Team team = teamService.findBytID(tID);
        String getDates = teamForm.getMeetingDate().replace("T"," ");
        LocalDateTime dateTime = LocalDateTime.parse(getDates,DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        team.setDate(dateTime);
        if(teamForm.getWritePlace()!=""){
            team.setPlace(teamForm.getWritePlace());
        }else{
            team.setPlace("중간 지점");
        }
        teamService.save(team);

        userTeamService.deleteByTID(team.getTID());

        UserTeam ut = new UserTeam();

//        == 임시 로그인 계정 ==
        User loginuser = userService.findByUserID("rabbith3");
        ut.setUser(loginuser);
        ut.setTeam(team);
        ut.setBoss(true);
        userTeamService.save(ut);

        for (int i = 0; i < teamForm.getUsers().length; i++) {
            if( teamForm.getUsers()[i] != ""){
                // 추가한 구성원의 정보
                User userInfo = userService.findByUserID(teamForm.getUsers()[i]);

                // 그룹의 구성원이 추가되었으므로, 유저-팀테이블을 업데이트 해준다.
                ut = new UserTeam();
                ut.setTeam(team);
                ut.setUser(userInfo);
                ut.setBoss(false);
                // 유저-팀 객체를 UserTeam 테이블에 저장
                userTeamService.save(ut);
            }
        }
        return "checkMeeting";
    }


}
