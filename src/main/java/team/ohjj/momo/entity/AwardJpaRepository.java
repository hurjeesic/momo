package team.ohjj.momo.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import team.ohjj.momo.domain.Award;
import team.ohjj.momo.domain.User;

import java.util.List;

public interface AwardJpaRepository extends JpaRepository<Award, Integer> {
	List<Award> findAllByUser(User user);
}
