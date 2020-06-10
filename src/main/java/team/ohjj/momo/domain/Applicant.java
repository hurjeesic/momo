package team.ohjj.momo.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@IdClass(ApplicantPK.class)
@Getter
@Setter
@ToString
public class Applicant {
	@Id
	@JsonIgnore
	private int project;

	@Id
	@JsonIgnore
	private int user;

	@JoinColumn(name = "field", nullable = false)
	@ManyToOne(targetEntity = ApplyField.class)
	@JsonProperty
	private ApplyField field;

	@Column(nullable = false)
	@JsonProperty
	private String content;
}