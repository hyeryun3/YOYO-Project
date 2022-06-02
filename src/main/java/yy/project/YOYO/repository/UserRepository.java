package yy.project.YOYO.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import yy.project.YOYO.domain.User;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<User, Long> {

    @Query ("select u from User u where u.userID = :id and u.password = :pw")
    User loginCheck(@Param("id") String id, @Param("pw") String pw);
    User findByuID(Long uId);

    @Query("select u from User u where u.userID = :userID")
    List<User> searchUserInfo(@Param("userID") String userID);

    User findByUserID(String userID);

    List<User> findByEmail(String email);

    User findByUserNameAndEmail(String userName, String email);
}
