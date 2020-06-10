package team.ohjj.momo.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import team.ohjj.momo.domain.Applicant;
import team.ohjj.momo.domain.ApplicantPK;

import java.util.Optional;

public interface ApplicantJpaRepository extends JpaRepository<Applicant, ApplicantPK> {

}
