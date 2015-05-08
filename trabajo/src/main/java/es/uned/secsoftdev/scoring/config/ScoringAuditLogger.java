package es.uned.secsoftdev.scoring.config;

import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ScoringAuditLogger {

	private static final String AUDIT_FILE = "audit.log";

	private static final String AUDIT_LOGGER = "AUDIT";

	private static Logger auditLogger = null;

	private static Boolean isEnabled = Boolean.TRUE;

	private static Boolean isTest = Boolean.FALSE;

	private static void init() {

		try {
			auditLogger = Logger.getLogger(AUDIT_LOGGER);

			if (!isTest) {
				auditLogger.setUseParentHandlers(false);

				Handler[] handlers = auditLogger.getHandlers();
				for (int i = 0; i < handlers.length; i++) {
					auditLogger.removeHandler(handlers[i]);
				}

				FileHandler fileHandler = new FileHandler(AUDIT_FILE);
				auditLogger.addHandler(fileHandler);
			}

			isEnabled = true;
		} catch (Exception e) {
			isEnabled = false;
		}
	}

	public static void log(String message) {
		if (auditLogger == null) {
			init();
		}
		if (isEnabled) {
			auditLogger.log(Level.INFO, message);
		}
	}

	public static void setTestMode(Boolean testMode) {
		isTest = testMode;
	}

}
