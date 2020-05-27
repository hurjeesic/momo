package team.ohjj.momo.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "apply_field", uniqueConstraints = @UniqueConstraint(columnNames = { "project", "field" }))
@Getter
@Setter
public class ApplyField {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty
    private int no;

    @JoinColumn(name = "project", nullable = false)
    @ManyToOne(targetEntity = Project.class)
    @JsonIgnore
    private Project project;

    @Column(nullable = false)
    @JsonProperty
    private String field;

    @Column(nullable = false)
    @JsonProperty
    private int number;

    @Override
    public String toString() {
        return "ApplyField{" +
                "no=" + no +
                ", project=" + project.toString() +
                ", field='" + field + '\'' +
                ", number=" + number +
                '}';
    }
}
