package yy.project.YOYO.vo;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter @Setter
public class FoodVO {

    private Long fID;

    private String foodName;

    private String foodImg;

    private String foodCategory;

    private String temperature;

    private String season;

    private String weather;

    private String event;

    private char priority;

}
