package team.ohjj.momo.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class User {
    @Id
    @GeneratedValue
    @JsonProperty
    private int no;

    @Column(unique = true, nullable = false)
    @JsonProperty
    private String email;

    @JsonIgnore
    @Column(nullable = false)
    private String password;

    @Column(unique = true, nullable = false)
    @JsonProperty
    private String nickname;

    @JsonProperty
    private String phone;

    @JsonIgnore
    @Column(nullable = false)
    private byte type;
}
