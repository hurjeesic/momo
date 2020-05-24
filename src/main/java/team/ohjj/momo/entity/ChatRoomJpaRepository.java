package team.ohjj.momo.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import team.ohjj.momo.domain.ChatRoom;

public interface ChatRoomJpaRepository extends JpaRepository<ChatRoom, String> {

}
