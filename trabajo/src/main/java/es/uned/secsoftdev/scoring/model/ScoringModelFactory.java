package es.uned.secsoftdev.scoring.model;

import org.springframework.stereotype.Component;

import es.uned.secsoftdev.scoring.config.ScoringAuditLogger;
import es.uned.secsoftdev.scoring.config.UserConfig;

@Component
public class ScoringModelFactory {

	public ScoringModelFactory() {
		super();
	}

	public ScoringModel createScoringModel(UserConfig userConfig) {

		if (userConfig == null || userConfig.getEvent() == null) {
			ScoringAuditLogger.log("Configuracion o evento nulo");
			return null;
		}

		String event = userConfig.getEvent();
		ScoringModelType modelType = ScoringModelType.findModelType(event);
		if (modelType == null) {
			ScoringAuditLogger.log("Evento no encontrado: "
					+ userConfig.getEvent());
			return null;
		}

		String packageName = ScoringModel.class.getPackage().getName();
		String modelName = composeModelName(packageName, modelType);

		ScoringModel scoringModel = newInstance(modelName);
		if (scoringModel == null || scoringModel.event() == null
				|| !scoringModel.event().equals(userConfig.getEvent())) {
			ScoringAuditLogger.log("Error en el modelo: "
					+ userConfig.getEvent());
			return null;
		}

		return scoringModel;
	}

	private ScoringModel newInstance(String modelName) {

		try {
			Class<?> clazz = Class.forName(modelName, true, Thread
					.currentThread().getContextClassLoader());
			return (ScoringModel) clazz.newInstance();
		} catch (Exception e) {
			ScoringAuditLogger.log("Excepcion al instanciar modelo: "
					+ modelName);
			return null;
		}
	}

	private String composeModelName(String packageName,
			ScoringModelType modelType) {

		StringBuilder sb = new StringBuilder();
		sb.append(packageName).append(".Scoring").append(modelType.name());
		return sb.toString();
	}

}
