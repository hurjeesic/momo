package team.ohjj.momo.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "portfolio", uniqueConstraints = @UniqueConstraint(columnNames = { "project", "user" }))
@Getter
@Setter
@ToString
public class Portfolio {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonProperty
	private int no;

	@JoinColumn(name = "user", nullable = false)
	@ManyToOne(targetEntity = User.class)
	@JsonProperty
	private User user;

	@JoinColumn(name = "project", nullable = false)
	@ManyToOne(targetEntity = Project.class)
	@JsonIgnore
	private Project project;

	@Column(nullable = false)
	@JsonProperty
	private String content;
}
