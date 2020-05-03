package team.ohjj.momo.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import team.ohjj.momo.domain.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmailAndPasswordAndType(String email, String password, Byte type);
    Optional<User> findByEmail(String email);
    Optional<User> findByNickname(String nickname);
}
