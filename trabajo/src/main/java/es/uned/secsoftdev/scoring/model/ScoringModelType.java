package es.uned.secsoftdev.scoring.model;

public enum ScoringModelType {

	Model1("Event1"),

	Model2("Event2"),

	Model3("Event3");

	private String event;

	private ScoringModelType(String event) {
		this.event = event;
	}

	public String event() {
		return this.event;
	}

	public static ScoringModelType findModelType(String event) {

		for (ScoringModelType type : ScoringModelType.values()) {
			if (type.event().equals(event)) {
				return type;
			}
		}
		return null;
	}
}
