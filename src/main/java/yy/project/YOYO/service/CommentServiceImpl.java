package yy.project.YOYO.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import yy.project.YOYO.repository.CommentRepository;
import yy.project.YOYO.vo.CommentVO;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService{

    private final CommentRepository commentRepository;

    //댓글 등록
    public int commentWrite(CommentVO vo) {
        return commentRepository.commentWrite(vo);
    }

    //댓글 목록
    public List<CommentVO> commentList(Long tID) {
        return commentRepository.commentList(tID);
    }

    //댓글 수정
    public int commentEdit(CommentVO vo) {
        return commentRepository.commentEdit(vo);
    }

    //댓글 삭제
    public int commentDel(Long cmID, String uID) {
        return commentRepository.commentDel(cmID, uID);
    }
}
