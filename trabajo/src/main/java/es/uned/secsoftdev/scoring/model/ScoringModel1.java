package es.uned.secsoftdev.scoring.model;

public class ScoringModel1 extends ScoringModel {

	public native String event();

	public native double score(String args, Object[] vars);
}
