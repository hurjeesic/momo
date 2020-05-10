package team.ohjj.momo.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import team.ohjj.momo.domain.Project;

public interface ProjectRepository extends JpaRepository<Project, Integer> {

}