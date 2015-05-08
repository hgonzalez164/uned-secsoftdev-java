package es.uned.secsoftdev.scoring.config;

public class UserConfig {

	private String uuid;

	private String username;

	private String password;

	private String event;

	public UserConfig(String uuid) {
		super();
		this.uuid = uuid;
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

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((uuid == null) ? 0 : uuid.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		UserConfig other = (UserConfig) obj;
		return this.uuid.equals(other.uuid)
				&& this.username.equals(other.username)
				&& this.password.equals(other.password)
				&& this.event.equals(other.event);
	}

}
