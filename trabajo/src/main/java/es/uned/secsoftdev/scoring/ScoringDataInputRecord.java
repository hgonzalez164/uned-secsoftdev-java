package es.uned.secsoftdev.scoring;

public class ScoringDataInputRecord extends ScoringDataRecord {

	private Object[] inputs;

	public ScoringDataInputRecord() {
		super();
	}

	public Object[] getInputs() {
		return inputs;
	}

	public void setInputs(Object[] inputs) {
		this.inputs = inputs;
	}

}
