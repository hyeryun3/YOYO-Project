package yy.project.YOYO.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import yy.project.YOYO.domain.Comment;
import yy.project.YOYO.repository.CommentRepository;
import yy.project.YOYO.repository.UserRepository;
import yy.project.YOYO.vo.CommentVO;

import javax.transaction.Transactional;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;


@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService{

    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    @Override
    public List<CommentVO> findComment(Long tID) {
        List<Comment> comments = commentRepository.findAll();
        List<CommentVO> cvos = new ArrayList<>();
        System.out.println(tID);
        for(Comment c : comments){
            if(c.getTeam().getTID()==tID){
                System.out.println(tID);
                CommentVO commentVO = new CommentVO();
                commentVO.setCommentContent(c.getCommentContent());
                commentVO.setCmID(c.getCmID());
                commentVO.setTbID(c.getTeam().getTID());
                commentVO.setWriterName(userRepository.findByuID(c.getWriterUID()).getUserName());
                cvos.add(commentVO);
            }
        }

        return cvos;
    }

    @Override
    public Long save(Comment comment1) {
        Comment save = commentRepository.save(comment1);

        return save.getCmID();

    }

    @Override
    public Comment findBycmID(Long cmID) {
        return commentRepository.findBycmID(cmID);
    }

    @Override
    public void delete(Long cmID) {
        commentRepository.delete(commentRepository.findBycmID(cmID));
    }

    @Override
    public void deleteByWriterUID(Long uId){
        commentRepository.deleteByWriterUID(uId);
    }

    @Override
    public boolean updateCommentCheck(Long cmID, Long uid) {
        if(commentRepository.findBycmID(cmID).getWriterUID()==uid){
            return true;
        }
        else return false;
    }


    @Transactional
    @Override
    public void updateCommentSave(Long cmID, String commentContent) {
        Comment c = commentRepository.findBycmID(cmID);
        c.setCommentContent(commentContent);
        commentRepository.save(c);
    }
}
