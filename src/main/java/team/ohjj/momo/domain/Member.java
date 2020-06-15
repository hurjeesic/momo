package team.ohjj.momo.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "project", "user" }))
@Getter
@Setter
public class Member {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonProperty
	private int no;

	@JoinColumn(name = "project", nullable = false)
	@ManyToOne(targetEntity = Project.class)
	@JsonProperty
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
	private boolean complete;
}
