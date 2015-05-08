package es.uned.secsoftdev.scoring.config;

import java.util.ArrayList;
import java.util.List;

public class ScoringConfig {

	private List<UserConfig> userConfig = new ArrayList<UserConfig>();

	public ScoringConfig() {
		super();
	}

	public List<UserConfig> getUserConfig() {
		return userConfig;
	}

	public void setUserConfig(List<UserConfig> userConfig) {
		this.userConfig = userConfig;
	}

}
