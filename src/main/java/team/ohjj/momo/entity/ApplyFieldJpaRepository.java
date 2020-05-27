package team.ohjj.momo.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import team.ohjj.momo.domain.ApplyField;
import team.ohjj.momo.domain.Project;

import java.util.List;

public interface ApplyFieldJpaRepository extends JpaRepository<ApplyField, Integer> {
	List<ApplyField> findAllByProject(Project project);
}