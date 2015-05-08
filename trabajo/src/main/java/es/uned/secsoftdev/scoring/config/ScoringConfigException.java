package es.uned.secsoftdev.scoring.config;

public class ScoringConfigException extends Exception {

	private static final long serialVersionUID = -3568552521875031527L;

	private static final String GENERIC_ERROR = "Error de configuracion";

	public ScoringConfigException() {
		super(GENERIC_ERROR);
	}

	public ScoringConfigException(String message) {
		super(message);
	}

	public ScoringConfigException(Throwable cause) {
		super(cause);
	}

}
