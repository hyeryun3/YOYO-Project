package yy.project.YOYO.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import yy.project.YOYO.domain.Team;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface TeamRepository extends JpaRepository<Team,Long> {
    
}
