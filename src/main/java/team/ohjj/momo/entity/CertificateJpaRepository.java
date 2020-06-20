package team.ohjj.momo.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import team.ohjj.momo.domain.Certificate;
import team.ohjj.momo.domain.User;

import java.util.List;

public interface CertificateJpaRepository extends JpaRepository<Certificate, Integer> {
	List<Certificate> findAllByUser(User user);
}
