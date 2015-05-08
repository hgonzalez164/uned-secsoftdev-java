package es.uned.secsoftdev.scoring.web;

import java.io.Serializable;

public class AuthenticationCommand implements Serializable {

	private static final long serialVersionUID = -1731036021030458685L;

	private String username;

	private String password;

	private Boolean rememberMe;

	public AuthenticationCommand() {
		super();
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

	public Boolean getRememberMe() {
		return rememberMe;
	}

	public void setRememberMe(Boolean rememberMe) {
		this.rememberMe = rememberMe;
	}

}
