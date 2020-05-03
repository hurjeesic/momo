package team.ohjj.momo.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import team.ohjj.momo.domain.User;

public interface UserRepository extends JpaRepository<User, Integer> {
}
