package yy.project.YOYO.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import yy.project.YOYO.domain.Comment;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface CommentRepository extends JpaRepository<Comment, Long> {

    Comment findBycmID(Long cmID);

    void deleteByWriterUID(Long uId);
}
