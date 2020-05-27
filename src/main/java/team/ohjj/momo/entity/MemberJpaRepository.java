package team.ohjj.momo.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import team.ohjj.momo.domain.Member;

public interface MemberJpaRepository extends JpaRepository<Member, Integer> {

}
