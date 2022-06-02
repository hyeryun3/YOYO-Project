package yy.project.YOYO.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import yy.project.YOYO.domain.UserTeam;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface UserTeamRepository extends JpaRepository<UserTeam, Long> {

    @Query("select ut from UserTeam ut where ut.user.uID=:uId")
    List<UserTeam> findByUID(@Param("uId") Long uId);

    @Query("select ut from UserTeam ut where ut.team.tID=:tId")
    List<UserTeam> findByTID(@Param("tId") Long tId);
}
