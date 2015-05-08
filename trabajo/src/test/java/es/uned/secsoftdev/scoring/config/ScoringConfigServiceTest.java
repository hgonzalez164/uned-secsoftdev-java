package es.uned.secsoftdev.scoring.config;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.UserPrincipal;
import java.nio.file.attribute.UserPrincipalLookupService;
import java.nio.file.attribute.UserPrincipalNotFoundException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import es.uned.secsoftdev.scoring.ScoringExecutorMain;
import es.uned.secsoftdev.scoring.model.ScoringModelType;
import es.uned.secsoftdev.utils.JniUtils;

public class ScoringConfigServiceTest {

	private ScoringConfigService service;

	static {
		JniUtils.loadLibraryPath(ScoringExecutorMain.class);
	}

	@Before
	public void before() {

		ClassLoader classLoader = Thread.currentThread()
				.getContextClassLoader();
		URL resourceURL = classLoader
				.getResource("es/uned/secsoftdev/scoring/config/scoring.conf");

		this.service = new ScoringConfigService();
		this.service.setConfigFile(resourceURL.getFile());
	}

	@Test
	public void testUserConfig() {

		UserConfig userConfig = null;
		try {
			userConfig = this.service.retrieveUserConfig("test", "test");
		} catch (ScoringConfigException e) {
			Assert.fail(e.getLocalizedMessage());
		}
		Assert.assertNotNull(userConfig);
	}

	@Test
	public void testUserHome() {

		String userHome = null;
		try {
			UserConfig userConfig = this.service.retrieveUserConfig("test",
					"test");
			userHome = this.service.retrieveUserHomePath(userConfig);
		} catch (ScoringConfigException e) {
			Assert.fail(e.getLocalizedMessage());
		}
		Assert.assertNotNull(userHome);
	}

	@Test
	public void testEventAccess() {

		boolean hasEventAccess = false;
		try {
			UserConfig userConfig = this.service.retrieveUserConfig("test",
					"test");
			hasEventAccess = this.service.hasEventAccess(userConfig,
					ScoringModelType.Model1.event());
		} catch (ScoringConfigException e) {
			Assert.fail(e.getLocalizedMessage());
		}

		Assert.assertTrue(hasEventAccess);
	}

	@Test(expected = UserPrincipalNotFoundException.class)
	public void testFileAccess() throws Exception {

		UserConfig userConfig = null;
		try {
			userConfig = this.service.retrieveUserConfig("test", "test");
		} catch (ScoringConfigException e) {
			Assert.fail(e.getLocalizedMessage());
		}

		File file = new File("input.txt");
		file.createNewFile();

		Path path = Paths.get(file.getAbsolutePath());
		UserPrincipalLookupService lookupService = path.getFileSystem()
				.getUserPrincipalLookupService();

		// El usuario "test" debe existir en el equipo
		UserPrincipal test = lookupService.lookupPrincipalByName("test");
		Files.setOwner(path, test);

		boolean hasFileAccess = this.service.hasFileAccess(userConfig,
				file.getAbsolutePath());
		Assert.assertTrue(hasFileAccess);
	}

}
