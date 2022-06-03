package yy.project.YOYO.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import yy.project.YOYO.domain.Comment;
import yy.project.YOYO.service.CommentService;
import yy.project.YOYO.vo.CommentVO;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
public class CommentController {
    @Inject
    CommentService service;

    @RequestMapping(value = "/comment/writeOk,  method = RequsetMethod.Post")
    public int writeOk(CommentVO vo, HttpSession session){
        vo.setUID(Long.toString(session.getAttribute("logId"));
        return service.commentWrite(vo);
    }

    @GetMapping("/comment/list")
    public List<CommentVO> list(Long tID){
        return service.commentList(tID);
    }

    @PostMapping("/comment/commentOk")
    public int commentOk(CommentVO vo, ){
        return service.commentEdit(vo);
    }

    @GetMapping("/comment/del")
    public int delOk(Long tId,){
        return service.commentDel(tId);
    }



}
