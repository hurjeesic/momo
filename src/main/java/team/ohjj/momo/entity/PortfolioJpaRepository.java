package team.ohjj.momo.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import team.ohjj.momo.domain.Portfolio;
import team.ohjj.momo.domain.Project;
import team.ohjj.momo.domain.User;

import java.util.Optional;

public interface PortfolioJpaRepository extends JpaRepository<Portfolio, Integer> {
	Optional<Portfolio> findByProjectAndUser(Project project, User user);
}
