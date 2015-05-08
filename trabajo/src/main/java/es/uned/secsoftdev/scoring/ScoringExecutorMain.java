package es.uned.secsoftdev.scoring;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import es.uned.secsoftdev.scoring.config.ScoringAuditLogger;
import es.uned.secsoftdev.scoring.config.ScoringConfigException;
import es.uned.secsoftdev.scoring.config.ScoringConfigService;
import es.uned.secsoftdev.scoring.config.UserConfig;
import es.uned.secsoftdev.scoring.model.ScoringModel;
import es.uned.secsoftdev.scoring.model.ScoringModelFactory;
import es.uned.secsoftdev.utils.JniUtils;

public class ScoringExecutorMain {

	private static final String ARGS_REGEX = "^[a-zA-Z0-9]*$";

	private static Logger logger = Logger.getLogger(ScoringExecutorMain.class
			.getName());

	private ScoringModelFactory modelFactory;

	private ScoringConfigService configService;

	public void setModelFactory(ScoringModelFactory modelFactory) {
		this.modelFactory = modelFactory;
	}

	public void setConfigService(ScoringConfigService configService) {
		this.configService = configService;
	}

	static {
		JniUtils.loadLibraryPath(ScoringExecutorMain.class);
	}

	public ScoringExecutorMain() {
		super();
	}

	public static void main(String[] args) throws Exception {

		ScoringAuditLogger.log("INICIO EJECUCION PROCESO");

		if (!validateArgs(args)) {
			logger.log(Level.INFO, "Parametros incorrectos");
			return;
		}

		ScoringExecutorMain executor = new ScoringExecutorMain();

		ScoringConfigService service = new ScoringConfigService();
		ScoringModelFactory factory = new ScoringModelFactory();
		executor.setConfigService(service);
		executor.setModelFactory(factory);

		try {
			executor.execute(args[0], args[1], args[2], args[3]);
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Error en la ejecucion del proceso");
			ScoringAuditLogger.log("Error en el proceso: "
					+ e.getLocalizedMessage());
		}

		ScoringAuditLogger.log("FIN EJECUCION PROCESO");
	}

	private static boolean validateArgs(String[] args) {

		if (args == null || args.length < 3) {
			return false;
		}

		for (int i = 0; i < args.length; i++) {
			String arg = args[i];

			// Comprobar que los parametros tengan contenido
			// Longitud minima de 1 y maxima de 10
			// Solo deben contener letras y numeros
			if (arg == null || arg.length() == 0 || arg.length() > 10
					|| !arg.matches(ARGS_REGEX)) {
				return false;
			}
		}

		return true;
	}

	public void execute(String username, String password, String inputFileName,
			String modelArgs) {

		UserConfig userConfig = null;
		String userHome = null;
		try {
			userConfig = this.configService.retrieveUserConfig(username,
					password);
			userHome = this.configService.retrieveUserHomePath(userConfig);
		} catch (ScoringConfigException e) {
			logger.log(Level.SEVERE,
					"Error al recuperar configuracion del usuario");
			ScoringAuditLogger
					.log("Error en la configuracion para el usuario: "
							+ username);
			return;
		}

		try {
			execute(userConfig, userHome, inputFileName, modelArgs);
		} catch (Throwable t) {
			logger.log(Level.SEVERE, t.getLocalizedMessage(), t);
		}
	}

	public String[] execute(UserConfig userConfig, String userHome,
			String inputFileName, String modelArgs) {

		String eventName = userConfig.getEvent();
		ScoringModel model = this.modelFactory.createScoringModel(userConfig);
		if (model == null) {
			logger.log(Level.INFO, "Error al recuperar modelo");
			return null;
		}

		ScoringDataHandler scoringDataHandler = new ScoringDataHandler();
		String inputFilePath = scoringDataHandler.generateInputFilePath(
				userHome, inputFileName);
		File inputFile = new File(inputFilePath);
		ScoringDataReader reader = new ScoringDataReader(inputFile);

		String outputFilePath = scoringDataHandler.generateOutputFilePath(
				userHome, eventName, inputFileName);
		File outputFile = new File(outputFilePath);
		ScoringDataWriter writer = new ScoringDataWriter(outputFile);

		if (!reader.init()) {
			logger.log(Level.SEVERE, "Error al iniciar reader");
			return null;
		}

		if (!writer.init()) {
			logger.log(Level.SEVERE, "Error al iniciar writer");
			return null;
		}

		logger.log(Level.INFO, "Inicio proceso de scoring");

		int index = 0;
		int records = 0;

		ScoringDataInputRecord inputRecord = reader.read();
		while (inputRecord != null) {

			index++;
			if (inputRecord.getId() == null) {
				logger.log(Level.INFO, "Registro excluido, linea: " + index);
			}

			double score = model.score(modelArgs, inputRecord.getInputs());

			ScoringDataOutputRecord outputRecord = new ScoringDataOutputRecord();
			outputRecord.setId(inputRecord.getId());
			outputRecord.setScore(score);

			writer.write(outputRecord);
			inputRecord = reader.read();
			records++;
		}

		reader.close();
		writer.close();

		logger.log(Level.INFO, "Proceso completado");
		logger.log(Level.INFO, "Total registros procesados = " + records);
		logger.log(Level.INFO, "Fichero de resultados: " + outputFilePath);
		return new String[] { String.valueOf(records), outputFilePath };
	}

}
