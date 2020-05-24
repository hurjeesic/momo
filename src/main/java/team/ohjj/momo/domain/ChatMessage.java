package team.ohjj.momo.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Calendar;

@Entity
@Table(name = "chat_message")
@Getter
@Setter
public class ChatMessage {
    @Id
    @Column(nullable = false)
    @JsonProperty
    private Integer no;

    @Column(nullable = false)
    @JsonProperty
    private MessageType type;

    @JoinColumn(name = "room", nullable = false)
    @ManyToOne(targetEntity = ChatRoom.class)
    @JsonProperty
    private ChatRoom room;

    @JoinColumn(name = "sender", nullable = false)
    @ManyToOne(targetEntity = User.class)
    @JsonProperty
    private User sender;

    @Column(nullable = false)
    @JsonProperty
    private String message;

    @Column(name = "send_time")
    @JsonProperty
    private Calendar sendTime;
}
