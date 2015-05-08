package es.uned.secsoftdev.utils;

import java.io.File;
import java.lang.reflect.Field;
import java.net.URL;

import es.uned.secsoftdev.scoring.config.ScoringAuditLogger;

public class JniUtils {

	public static final String LIBRARY_NAME = "secsoftdev";

	public static void loadLibraryPath(Class<?> clazz) {
		loadLibraryPath(clazz, LIBRARY_NAME);
	}

	public static void loadLibraryPath(Class<?> clazz, String library) {

		try {

			URL url = clazz.getResource(".");
			if (url == null) {
				String dir = clazz.getPackage().getName()
						.replaceAll("\\.", "/");
				url = new File(dir).toURI().toURL();
			}

			String javaLibraryPath = System.getProperty("java.library.path");
			System.setProperty("java.library.path", url.getFile() + ";"
					+ javaLibraryPath);

			final Field sysPathsField = ClassLoader.class
					.getDeclaredField("sys_paths");
			sysPathsField.setAccessible(true);
			sysPathsField.set(null, null);

			System.loadLibrary(library);

		} catch (Throwable e) {
			ScoringAuditLogger.log("Error al establecer java.library.path: "
					+ e.getLocalizedMessage());
		}
	}

}
