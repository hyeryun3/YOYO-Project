package yy.project.YOYO.domain;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class UserTeam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long utID;

    @NotNull
    private Boolean boss;

    private String startPlace;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="uID")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="tID")
    private Team team;
}
