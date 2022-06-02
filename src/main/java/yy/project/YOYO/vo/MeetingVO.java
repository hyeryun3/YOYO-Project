package yy.project.YOYO.vo;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter
public class MeetingVO {
    private String teamName;
    private List<String> members;
    private String place;
    private LocalDateTime time;
}
