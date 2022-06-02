package yy.project.YOYO.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import yy.project.YOYO.domain.User;
import yy.project.YOYO.vo.UserVO;

import java.util.List;

@Service
public interface UserService{

    int memberCheck(String userID);

    User save(User user);

    User findByUserID(String userID);

    String filePathForUserProfileImage(List<MultipartFile> images) throws Exception;

    void updateUser(String userID, UserVO form) throws Exception;

    void deleteUser(User user);

    User findByUserNameAndEmail(String userName,String email);

    List<User> findByEmail(String email);

    String getTempPW();

    void mailToPW(String userName, String email, String tempPW);
}
