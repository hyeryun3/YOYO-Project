package yy.project.YOYO.domain;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;


@Entity
@Getter
@Setter
public class Food {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fID;

    private String foodName;

    private String foodImg;

    private String foodCategory;

    private int temperature;

    private String season;

    private String weather;

    private LocalDate event;

    private char priority;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="tID")
    private Team team;
}
