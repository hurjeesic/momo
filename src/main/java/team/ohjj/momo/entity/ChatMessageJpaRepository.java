package team.ohjj.momo.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import team.ohjj.momo.domain.ChatMessage;
import team.ohjj.momo.domain.User;

import java.util.List;

public interface ChatMessageJpaRepository extends JpaRepository<ChatMessage, Integer> {
    List<ChatMessage> findBySender(User user);
}