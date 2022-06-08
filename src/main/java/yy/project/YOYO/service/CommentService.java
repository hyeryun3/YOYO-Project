package yy.project.YOYO.service;

import org.springframework.stereotype.Service;
import yy.project.YOYO.domain.Comment;
import yy.project.YOYO.vo.CommentVO;


import java.util.List;


@Service
public interface CommentService {
    List<CommentVO> findComment(Long bID);

    Long save(Comment comment1);

    Comment findBycmID(Long cmID);

    void delete(Long cmID);

    boolean updateCommentCheck(Long cmID, Long uid);

    void updateCommentSave(Long cmID, String commentContent);
}