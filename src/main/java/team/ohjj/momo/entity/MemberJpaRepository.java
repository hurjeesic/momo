package team.ohjj.momo.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import team.ohjj.momo.domain.Member;
import team.ohjj.momo.domain.Project;

import java.util.List;

public interface MemberJpaRepository extends JpaRepository<Member, Integer> {
	List<Member> findAllByProject(Project project);
}
