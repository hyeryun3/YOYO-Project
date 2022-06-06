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

    @NotNull
    private String foodName;

    @NotNull
    private String foodImg;

    @NotNull
    private String foodCategory;

    @NotNull
    private String temperature;

    @NotNull
    private String season;

    private String weather;

    private String event;

    @NotNull
    private char priority;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="tID")
    private Team team;

    @Override
    public int hashCode() {

        return foodName.hashCode();
    }

    @Override
    public boolean equals(Object obj) {

        if(obj instanceof Food) {
            Food food=(Food)obj;
            boolean bool=food.foodName.equals(this.foodName);
            return true;
        }else {
            return false;
        }
    }
}
