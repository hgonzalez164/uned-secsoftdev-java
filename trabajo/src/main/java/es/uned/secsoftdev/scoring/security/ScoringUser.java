package es.uned.secsoftdev.scoring.security;

import java.io.Serializable;

public class ScoringUser implements Serializable {

	private static final long serialVersionUID = 7885508011534993312L;

	private Integer id;

	private String username;

	private String password;

	private Integer failedLoginCount;

	public ScoringUser() {
		super();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getFailedLoginCount() {
		return failedLoginCount;
	}

	public void setFailedLoginCount(Integer failedLoginCount) {
		this.failedLoginCount = failedLoginCount;
	}

}
