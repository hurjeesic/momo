package team.ohjj.momo.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@Setter
@EqualsAndHashCode
public class User implements Serializable {
	private static final long serialVersionUID = 6494678977089006639L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonIgnore
	private int no;

	@Column(unique = true, nullable = false)
	@JsonProperty
	private String email;

	@Column(nullable = false)
	@JsonIgnore
	private String password;

	@Column(unique = true, nullable = false)
	@JsonProperty
	private String nickname;

	@JsonProperty
	private String phone;


	@Column(nullable = false)
	@JsonIgnore
	private byte type;
}
