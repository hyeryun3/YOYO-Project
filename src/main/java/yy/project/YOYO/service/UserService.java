package yy.project.YOYO.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import yy.project.YOYO.domain.User;
import yy.project.YOYO.vo.UserVO;

import java.util.List;

@Service
public interface UserService{

    User login(String id, String pw);

    List<User> findByLUserID(String userID);

    int memberCheck(String userID);

    User save(User user);

    User findByUserID(String userID);

    String filePathForUserProfileImage(List<MultipartFile> images) throws Exception;

    void updateUser(UserVO form,User loginUser) throws Exception;

    void deleteUser(User user);

    User findByUserNameAndEmail(String userName,String email);

    List<User> findByEmail(String email);

    String getTempPW();

    void mailToPW(String userName, String email, String tempPW);

    Page<User> findAll(Pageable pageable);

    Page<User> findByUserIDIgnoreCaseContainingOrAddressContainingOrUserNameIgnoreCaseContainingOrEmailIgnoreCaseContaining(String searchWord1, String searchWord2, String searchWord3, String searchWord4, Pageable pageable);

    List<User> deleteByUserIDIn(List<String> userID);
}
