package team.ohjj.momo.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import team.ohjj.momo.domain.Project;
import team.ohjj.momo.domain.User;

import java.util.List;

public interface ProjectJpaRepository extends JpaRepository<Project, Integer> {
	List<Project> findAllByOrganizerNotAndComplete(User user, Boolean complete);
	List<Project> findAllByOrganizerNot(User user);
	List<Project> findAllByComplete(boolean complete);
}