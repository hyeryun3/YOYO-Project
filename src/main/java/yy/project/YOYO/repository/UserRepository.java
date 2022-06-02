package yy.project.YOYO.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import yy.project.YOYO.domain.User;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<User, Long> {

    User findByUserID(String userID);

    List<User> findByEmail(String email);

    User findByUserNameAndEmail(String userName, String email);
}
