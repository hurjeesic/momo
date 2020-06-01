package team.ohjj.momo.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.io.Serializable;

@Entity
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

	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public byte getType() {
		return type;
	}

	public void setType(byte type) {
		this.type = type;
	}
}
