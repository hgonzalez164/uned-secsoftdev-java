package es.uned.secsoftdev.scoring.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileOwnerAttributeView;
import java.nio.file.attribute.UserPrincipal;
import java.util.List;
import java.util.UUID;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import es.uned.secsoftdev.scoring.security.ScoringSecurityEncoder;
import es.uned.secsoftdev.scoring.security.ScoringUser;

@Service
public class ScoringConfigService {

	private static final String ENV_HOME_PATH = "HOME";

	private static final String DEFAULT_HOME_PATH = "C:/Temp/Users/";

	private static final String DEFAULT_CONFIG_FILE = "scoring.conf";

	private String configFile = DEFAULT_CONFIG_FILE;

	private ScoringConfig config;

	public void setConfigFile(String configFile) {
		this.configFile = configFile;
	}

	public ScoringConfigService() {
		super();
	}

	public UserConfig retrieveUserConfig(ScoringUser user)
			throws ScoringConfigException {
		return retrieveUserConfig(user.getUsername(), user.getPassword(), true);
	}

	public UserConfig retrieveUserConfig(String username, String password)
			throws ScoringConfigException {
		return retrieveUserConfig(username, password, false);
	}

	private UserConfig retrieveUserConfig(String username, String password,
			boolean passwordEncoded) throws ScoringConfigException {

		ScoringConfig config = getScoringConfig();
		if (config == null) {
			ScoringAuditLogger.log("Configuracion nula");
			throw new ScoringConfigException();
		}

		UserConfig userConfig = findUserConfig(config, username);
		if (userConfig == null) {
			ScoringAuditLogger.log("Configuracion del usuario no encontrada: "
					+ username);
			throw new ScoringConfigException("Credenciales incorrectas");
		}

		if (!checkUserPassword(userConfig, password, passwordEncoded)) {
			ScoringAuditLogger
					.log("Password introducida por el usuario incorrecta: "
							+ username);
			throw new ScoringConfigException("Credenciales incorrectas");
		}

		return userConfig;
	}

	private boolean checkUserPassword(UserConfig userConfig, String password,
			boolean passwordEncoded) {

		String encoded = passwordEncoded ? password : ScoringSecurityEncoder
				.encodePassword(userConfig.getUsername(), password);
		return encoded != null && encoded.equals(userConfig.getPassword());
	}

	public String retrieveUserHomePath(UserConfig userConfig)
			throws ScoringConfigException {

		if (!checkUserConfig(userConfig)) {
			ScoringAuditLogger.log("Error en configuracion del usuario: "
					+ userConfig.getUsername());
			throw new ScoringConfigException("Usuario erroneo");
		}

		StringBuilder sb = new StringBuilder();

		String homePath = System.getenv(ENV_HOME_PATH);
		if (homePath == null || homePath.isEmpty()) {
			sb.append(DEFAULT_HOME_PATH).append(userConfig.getUsername())
					.append('/');
		} else {
			homePath = homePath.replaceAll("\\\\", "/");
			sb.append(homePath);
			if (!homePath.endsWith("/")) {
				sb.append('/');
			}
		}

		return sb.toString();
	}

	public boolean hasEventAccess(UserConfig userConfig, String eventName)
			throws ScoringConfigException {

		if (!checkUserConfig(userConfig)) {
			ScoringAuditLogger.log("Error en configuracion del usuario: "
					+ userConfig.getUsername());
			throw new ScoringConfigException("Usuario erroneo");
		}
		return userConfig.getEvent() != null
				&& userConfig.getEvent().equals(eventName);
	}

	public boolean hasFileAccess(UserConfig userConfig, String filePath)
			throws ScoringConfigException {

		if (!checkUserConfig(userConfig)) {
			ScoringAuditLogger.log("Error en configuracion del usuario: "
					+ userConfig.getUsername());
			throw new ScoringConfigException("Usuario erroneo");
		}

		try {
			Path path = Paths.get(filePath);
			FileOwnerAttributeView ownerAttributeView = Files
					.getFileAttributeView(path, FileOwnerAttributeView.class);
			UserPrincipal owner = ownerAttributeView.getOwner();

			// En Windows: Eliminar la parte del nombre dominio/grupo/maquina
			String ownerName = owner.getName();
			if (ownerName.lastIndexOf("\\") != -1) {
				ownerName = ownerName.substring(
						ownerName.lastIndexOf("\\") + 1, ownerName.length());
			}

			return ownerName.equals(userConfig.getUsername());

		} catch (IOException e) {
			ScoringAuditLogger.log("Excepcion: " + e.getLocalizedMessage());
			return false;
		}
	}

	private boolean checkUserConfig(UserConfig userConfig) {

		ScoringConfig scoringConfig = getScoringConfig();
		UserConfig internalUserConfig = findUserConfig(scoringConfig,
				userConfig.getUsername());

		return internalUserConfig != null
				&& internalUserConfig.equals(userConfig);
	}

	private UserConfig findUserConfig(ScoringConfig config, String username) {

		List<UserConfig> userConfigList = config.getUserConfig();
		if (userConfigList != null && !userConfigList.isEmpty()) {
			for (UserConfig userConf : userConfigList) {
				if (userConf.getUsername().equals(username)) {
					return userConf;
				}
			}
		}
		return null;
	}

	private ScoringConfig getScoringConfig() {
		if (this.config == null) {
			this.config = readConfig();
		}
		return this.config;
	}

	private ScoringConfig readConfig() {

		File f = new File(this.configFile);
		if (!f.exists() || !f.canRead()) {

			// Recuperar del classpath
			try {
				f = new ClassPathResource(DEFAULT_CONFIG_FILE).getFile();
			} catch (IOException ioe) {
				ScoringAuditLogger.log("El fichero de configuracion no existe");
				return null;
			}
		}

		ConfigFileReader reader = new ConfigFileReader();
		try {
			return reader.readConfigFile(f);
		} catch (Exception e) {
			ScoringAuditLogger.log("Excepcion: " + e.getLocalizedMessage());
			return null;
		}
	}

	private class ConfigFileReader {

		private static final String SEPARATOR = "\t";

		private static final String DATA_REGEX = "^[a-zA-Z0-9]*$";

		private ScoringConfig readConfigFile(File file) {

			ScoringConfig config = new ScoringConfig();

			FileReader fr = null;
			BufferedReader br = null;
			try {
				fr = new FileReader(file);
				br = new BufferedReader(fr);

				String line = br.readLine();
				while (line != null) {
					UserConfig userConfig = readUserConfig(line);
					if (userConfig != null) {
						config.getUserConfig().add(userConfig);
					}
					line = br.readLine();
				}
			} catch (IOException e) {
				ScoringAuditLogger.log("Excepcion: " + e.getLocalizedMessage());
			} finally {
				try {
					if (fr != null) {
						fr.close();
					}
					if (br != null) {
						br.close();
					}
				} catch (IOException e) {
					ScoringAuditLogger.log("Excepcion: "
							+ e.getLocalizedMessage());
				}
			}

			return config;
		}

		private UserConfig readUserConfig(String line) {

			if (line == null || !(line.trim().length() > 0)) {
				return null;
			}

			String[] split = line.trim().split(SEPARATOR);
			if (split.length != 3) {
				return null;
			}

			String username = split[0] != null ? split[0].trim() : null;
			String password = split[1] != null ? split[1].trim() : null;
			String event = split[2] != null ? split[2].trim() : null;

			if (!validateConfigString(username)
					|| !validateConfigString(password)
					|| !validateConfigString(event)) {
				return null;
			}

			String uuid = UUID.randomUUID().toString();
			UserConfig userConfig = new UserConfig(uuid);

			userConfig.setUsername(username);
			userConfig.setPassword(password);
			userConfig.setEvent(event);

			return userConfig;
		}

		private boolean validateConfigString(String s) {
			return s != null && !s.trim().isEmpty() && s.matches(DATA_REGEX);
		}
	}

}
