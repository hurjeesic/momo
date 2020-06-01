package team.ohjj.momo.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "chat_message")
@Getter
@Setter
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    private Date sendTime;

    @Override
    public String toString() {
        return "ChatMessage{" +
                "no=" + no +
                ", type=" + type +
                ", room=" + room.toString() +
                ", sender=" + sender.toString() +
                ", message='" + message + '\'' +
                ", sendTime=" + sendTime +
                '}';
    }
}
