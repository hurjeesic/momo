package team.ohjj.momo.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "applicant", uniqueConstraints = @UniqueConstraint(columnNames = { "project", "user" }))
@Getter
@Setter
@ToString
public class Applicant {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonProperty
	private int no;

	@JoinColumn(name = "project", nullable = false)
	@ManyToOne(targetEntity = Project.class)
	@JsonIgnore
	private Project project;

	@JoinColumn(name = "user", nullable = false)
	@ManyToOne(targetEntity = User.class)
	@JsonProperty
	private User user;

	@JoinColumn(name = "field", nullable = false)
	@ManyToOne(targetEntity = ApplyField.class)
	@JsonProperty
	private ApplyField field;

	@Column(nullable = false)
	@JsonProperty
	private String content;
}