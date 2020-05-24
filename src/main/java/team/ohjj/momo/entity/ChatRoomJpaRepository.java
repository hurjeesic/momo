package team.ohjj.momo.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import team.ohjj.momo.domain.ChatRoom;
import team.ohjj.momo.domain.Project;

import java.util.List;

public interface ChatRoomJpaRepository extends JpaRepository<ChatRoom, String> {
    List<ChatRoom> findAllByProject(Project project);
}
