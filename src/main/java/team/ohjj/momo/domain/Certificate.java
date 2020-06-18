package team.ohjj.momo.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Calendar;

@Entity
@Table(name = "certificate")
@Getter
@Setter
@ToString
public class Certificate {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonProperty
	private int no;

	@JoinColumn(name = "user", nullable = false)
	@ManyToOne(targetEntity = User.class)
	@JsonIgnore
	private User user;

	@Column(nullable = false)
	@JsonProperty
	private String name;

	@Column(nullable = false)
	@JsonProperty
	private String field;

	@Column(name = "acquisition_date", nullable = false)
	@JsonProperty
	private Calendar acquisitionDate;
}
