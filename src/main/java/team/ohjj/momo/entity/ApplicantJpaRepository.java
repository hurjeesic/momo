package team.ohjj.momo.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import team.ohjj.momo.domain.Applicant;
import team.ohjj.momo.domain.Project;
import team.ohjj.momo.domain.User;

import java.util.List;
import java.util.Optional;

public interface ApplicantJpaRepository extends JpaRepository<Applicant, Integer> {
	Optional<Applicant> findByProjectAndUser(Project project, User user);
	List<Applicant> findAllByProject(Project project);
}
