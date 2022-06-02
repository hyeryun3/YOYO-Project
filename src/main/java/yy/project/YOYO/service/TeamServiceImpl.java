package yy.project.YOYO.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import yy.project.YOYO.domain.Team;
import yy.project.YOYO.repository.TeamRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class TeamServiceImpl implements TeamService{

    private final TeamRepository teamRepository;

    public Team save(Team team){
        return teamRepository.save(team);
    }

    public Team findBytID(Long tID){
        return teamRepository.findBytID(tID);
    }

}
