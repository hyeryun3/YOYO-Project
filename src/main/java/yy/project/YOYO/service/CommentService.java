package yy.project.YOYO.service;


import yy.project.YOYO.vo.CommentVO;

import java.util.List;

public interface CommentService {
    //댓글 등록
    int commentWrite(CommentVO vo);

    //댓글 목록
    List<CommentVO> commentList(Long tID);

    //댓글 수정
    int  commentEdit(CommentVO vo);

    //댓글 삭제
    int commentDel(Long cmID, String uID);
}
