package es.uned.secsoftdev.scoring.web;

import java.io.Serializable;

public class ExecutionCommand implements Serializable {

	private static final long serialVersionUID = -1937086071280545533L;

	private String event;

	private String file;

	private String modelArgs;

	private String csrfToken;

	private String status;

	private String details;

	public ExecutionCommand() {
		super();
	}

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

	public String getModelArgs() {
		return modelArgs;
	}

	public void setModelArgs(String modelArgs) {
		this.modelArgs = modelArgs;
	}

	public String getCsrfToken() {
		return csrfToken;
	}

	public void setCsrfToken(String csrfToken) {
		this.csrfToken = csrfToken;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

}
