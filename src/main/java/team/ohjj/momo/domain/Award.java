package team.ohjj.momo.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Calendar;

@Entity
@Table(name = "award")
@Getter
@Setter
@ToString
public class Award {
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
	private String title;

	@Column(nullable = false)
	@JsonProperty
	private String field;

	@Column(nullable = false)
	@JsonProperty
	private SCALE scale;

	@Column(name = "acquisition_date", nullable = false)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonProperty
	private Calendar acquisitionDate;
}
