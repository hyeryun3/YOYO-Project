package yy.project.YOYO.service;

import org.springframework.stereotype.Service;
import yy.project.YOYO.domain.Team;

@Service
public interface TeamService {
    Team save(Team team);
    Team findBytID(Long tID);
}
