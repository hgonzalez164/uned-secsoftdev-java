package es.uned.secsoftdev.scoring;

import java.io.File;
import java.net.URL;
import java.util.UUID;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import es.uned.secsoftdev.scoring.config.ScoringConfigException;
import es.uned.secsoftdev.scoring.config.ScoringConfigService;
import es.uned.secsoftdev.scoring.config.UserConfig;
import es.uned.secsoftdev.scoring.model.ScoringModel1;
import es.uned.secsoftdev.scoring.model.ScoringModel2;
import es.uned.secsoftdev.scoring.model.ScoringModel3;
import es.uned.secsoftdev.scoring.model.ScoringModelFactory;

public class ScoringExecutorMainTest {

	private ScoringExecutorMain executor;

	private ScoringModelFactory factory;

	private ScoringConfigService service;

	private String userHome;

	@Before
	public void before() throws Exception {
		this.executor = new ScoringExecutorMain();
		this.factory = EasyMock.createMock(ScoringModelFactory.class);
		this.service = EasyMock.createMock(ScoringConfigService.class);
		this.executor.setConfigService(service);
		this.executor.setModelFactory(factory);

		ClassLoader classLoader = Thread.currentThread()
				.getContextClassLoader();
		URL resourceURL = classLoader
				.getResource("es/uned/secsoftdev/scoring/test/");
		this.userHome = new File(resourceURL.toURI()).getAbsolutePath() + "/";
	}

	@Test
	public void test1() {

		String uuid = UUID.randomUUID().toString();
		UserConfig userConfig = new UserConfig(uuid);
		userConfig.setUsername("test1");
		userConfig.setPassword("0cbc6611f5540bd0809a388dc95a615b");
		userConfig.setEvent("Event1");

		try {
			EasyMock.expect(this.service.retrieveUserConfig("test1", "test"))
					.andReturn(userConfig);
			EasyMock.expect(this.service.retrieveUserHomePath(userConfig))
					.andReturn(this.userHome);

		} catch (ScoringConfigException e) {
			Assert.fail(e.getLocalizedMessage());
		}

		ScoringModel1 model = new ScoringModel1();
		EasyMock.expect(this.factory.createScoringModel(userConfig)).andReturn(
				model);

		EasyMock.replay(this.factory, this.service);

		this.executor.execute("test1", "test", "input1.txt", "args");

		EasyMock.verify(this.factory, this.service);
	}

	@Test
	public void test2() {

		String uuid = UUID.randomUUID().toString();
		UserConfig userConfig = new UserConfig(uuid);
		userConfig.setUsername("test1");
		userConfig.setPassword("0cbc6611f5540bd0809a388dc95a615b");
		userConfig.setEvent("Event1");

		try {
			EasyMock.expect(this.service.retrieveUserConfig("test1", "test"))
					.andReturn(userConfig);
			EasyMock.expect(this.service.retrieveUserHomePath(userConfig))
					.andReturn(this.userHome);

		} catch (ScoringConfigException e) {
			Assert.fail(e.getLocalizedMessage());
		}

		ScoringModel1 model = new ScoringModel1();
		EasyMock.expect(this.factory.createScoringModel(userConfig)).andReturn(
				model);

		EasyMock.replay(this.factory, this.service);

		this.executor.execute("test1", "test", "input2.txt", "A");

		EasyMock.verify(this.factory, this.service);
	}

	@Test
	public void test3() {

		String uuid = UUID.randomUUID().toString();
		UserConfig userConfig = new UserConfig(uuid);
		userConfig.setUsername("test1");
		userConfig.setPassword("0cbc6611f5540bd0809a388dc95a615b");
		userConfig.setEvent("Event1");

		try {
			EasyMock.expect(this.service.retrieveUserConfig("test1", "test"))
					.andReturn(userConfig);
			EasyMock.expect(this.service.retrieveUserHomePath(userConfig))
					.andReturn(this.userHome);

		} catch (ScoringConfigException e) {
			Assert.fail(e.getLocalizedMessage());
		}

		ScoringModel1 model = new ScoringModel1();
		EasyMock.expect(this.factory.createScoringModel(userConfig)).andReturn(
				model);

		EasyMock.replay(this.factory, this.service);

		this.executor.execute("test1", "test", "input3.txt", "AA");

		EasyMock.verify(this.factory, this.service);
	}

	@Test
	public void test4() {

		String uuid = UUID.randomUUID().toString();
		UserConfig userConfig = new UserConfig(uuid);
		userConfig.setUsername("test2");
		userConfig.setPassword("0cbc6611f5540bd0809a388dc95a615b");
		userConfig.setEvent("Event2");

		try {
			EasyMock.expect(this.service.retrieveUserConfig("test2", "test"))
					.andReturn(userConfig);
			EasyMock.expect(this.service.retrieveUserHomePath(userConfig))
					.andReturn(this.userHome);

		} catch (ScoringConfigException e) {
			Assert.fail(e.getLocalizedMessage());
		}

		ScoringModel2 model = new ScoringModel2();
		EasyMock.expect(this.factory.createScoringModel(userConfig)).andReturn(
				model);

		EasyMock.replay(this.factory, this.service);

		this.executor.execute("test2", "test", "input1.txt", "MALLOC");

		EasyMock.verify(this.factory, this.service);
	}

	@Test
	public void test5() {

		String uuid = UUID.randomUUID().toString();
		UserConfig userConfig = new UserConfig(uuid);
		userConfig.setUsername("test2");
		userConfig.setPassword("0cbc6611f5540bd0809a388dc95a615b");
		userConfig.setEvent("Event2");

		try {
			EasyMock.expect(this.service.retrieveUserConfig("test2", "test"))
					.andReturn(userConfig);
			EasyMock.expect(this.service.retrieveUserHomePath(userConfig))
					.andReturn(this.userHome);

		} catch (ScoringConfigException e) {
			Assert.fail(e.getLocalizedMessage());
		}

		ScoringModel2 model = new ScoringModel2();
		EasyMock.expect(this.factory.createScoringModel(userConfig)).andReturn(
				model);

		EasyMock.replay(this.factory, this.service);

		this.executor.execute("test2", "test", "input1.txt", "FREE");

		EasyMock.verify(this.factory, this.service);
	}

	@Test
	public void test6() {

		String uuid = UUID.randomUUID().toString();
		UserConfig userConfig = new UserConfig(uuid);
		userConfig.setUsername("test3");
		userConfig.setPassword("0cbc6611f5540bd0809a388dc95a615b");
		userConfig.setEvent("Event3");

		try {
			EasyMock.expect(this.service.retrieveUserConfig("test3", "test"))
					.andReturn(userConfig);
			EasyMock.expect(this.service.retrieveUserHomePath(userConfig))
					.andReturn(this.userHome);

		} catch (ScoringConfigException e) {
			Assert.fail(e.getLocalizedMessage());
		}

		ScoringModel3 model = new ScoringModel3();
		EasyMock.expect(this.factory.createScoringModel(userConfig)).andReturn(
				model);

		EasyMock.replay(this.factory, this.service);

		// this.executor.execute("test3", "test", "input4.txt", "POINTER");
		// this.executor.execute("test3", "test", "input4.txt", "INTEGER");
		// this.executor.execute("test3", "test", "input4.txt", "FORMAT");
		this.executor.execute("test3", "test", "input4.txt", "FILE");

		EasyMock.verify(this.factory, this.service);
	}

}
