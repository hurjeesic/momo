package team.ohjj.momo.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@Setter
public class Project implements Serializable {
    private static final long serialVersionUID = 6494678977089006639L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty
    private int no;

    @Column(nullable = false)
    @JsonProperty
    private String title;

    @Column(nullable = false)
    @JsonProperty
    private String content;

    @JoinColumn(name = "organizer", nullable = false)
    @ManyToOne(targetEntity = User.class)
    @JsonProperty
    private User organizer;

    @Column
    @JsonProperty
    private byte process;

    @Column
    @JsonProperty
    private byte apply;
}
