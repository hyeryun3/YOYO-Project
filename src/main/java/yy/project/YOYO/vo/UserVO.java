package yy.project.YOYO.vo;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter @Setter
public class UserVO {
    private String userID;
    private String password;
    private String address;

    private List<MultipartFile> userImage;
    private String userName;
    private String email;

}
