package yy.project.YOYO.vo;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
public class CommentVO {

    private Long cmID;

    private Long tID;

    private Long uID;

    private String comment_content;

    private LocalDateTime upload_time;

}
