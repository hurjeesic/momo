package team.ohjj.momo.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import team.ohjj.momo.domain.Member;
import team.ohjj.momo.domain.Project;
import team.ohjj.momo.domain.User;

import java.util.List;
import java.util.Optional;

public interface MemberJpaRepository extends JpaRepository<Member, Integer> {
	List<Member> findAllByProject(Project Project);
	Optional<Member> findByProjectAndUser(Project project, User user);
}
