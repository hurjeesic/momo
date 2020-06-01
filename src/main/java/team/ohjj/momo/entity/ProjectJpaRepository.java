package team.ohjj.momo.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import team.ohjj.momo.domain.Project;

public interface ProjectJpaRepository extends JpaRepository<Project, Integer> {

}