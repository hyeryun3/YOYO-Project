package yy.project.YOYO.service;

import org.springframework.stereotype.Service;
import yy.project.YOYO.domain.UserTeam;

import java.util.List;

@Service
public interface UserTeamService {
    UserTeam save(UserTeam userTeam);
    void deleteByUID(Long uid);
    List<UserTeam> findByUID(Long uid);
    List<UserTeam> findByTID(Long tid);

}
