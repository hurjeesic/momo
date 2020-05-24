package team.ohjj.momo.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.io.Serializable;

@Entity
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

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getOrganizer() {
        return organizer;
    }

    public void setOrganizer(User organizer) {
        this.organizer = organizer;
    }

    public byte getProcess() {
        return process;
    }

    public void setProcess(byte process) {
        this.process = process;
    }

    public byte getApply() {
        return apply;
    }

    public void setApply(byte apply) {
        this.apply = apply;
    }
}
