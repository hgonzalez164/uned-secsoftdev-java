package es.uned.secsoftdev.scoring.model;

public class ScoringModel {

	public native String event();

	public native double score(String args, Object[] vars);
}
