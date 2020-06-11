package team.ohjj.momo.domain;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class ApplicantPK implements Serializable {
	private int project;
	private int user;
}
