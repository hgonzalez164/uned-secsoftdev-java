package es.uned.secsoftdev.scoring.model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import es.uned.secsoftdev.scoring.ScoringExecutorMain;
import es.uned.secsoftdev.scoring.config.UserConfig;
import es.uned.secsoftdev.utils.JniUtils;

public class ScoringModelFactoryTest {

	private ScoringModelFactory factory;

	static {
		JniUtils.loadLibraryPath(ScoringExecutorMain.class);
	}

	@Before
	public void before() {
		this.factory = new ScoringModelFactory();
	}

	@Test
	public void testEvent1() {

		UserConfig userConfig = new UserConfig("1");
		userConfig.setUsername("test");
		userConfig.setPassword("[SECRET]");
		userConfig.setEvent(ScoringModelType.Model1.event());

		ScoringModel model = this.factory.createScoringModel(userConfig);
		Assert.assertNotNull(model);
		Assert.assertTrue(model instanceof ScoringModel1);
		Assert.assertEquals(ScoringModelType.Model1.event(), model.event());
	}

	@Test
	public void testEvent2() {

		UserConfig userConfig = new UserConfig("2");
		userConfig.setUsername("test");
		userConfig.setPassword("[SECRET]");
		userConfig.setEvent(ScoringModelType.Model2.event());

		ScoringModel model = this.factory.createScoringModel(userConfig);
		Assert.assertNotNull(model);
		Assert.assertTrue(model instanceof ScoringModel2);
		Assert.assertEquals(ScoringModelType.Model2.event(), model.event());
	}

	@Test
	public void testEvent3() {

		UserConfig userConfig = new UserConfig("3");
		userConfig.setUsername("test");
		userConfig.setPassword("[SECRET]");
		userConfig.setEvent(ScoringModelType.Model3.event());

		ScoringModel model = this.factory.createScoringModel(userConfig);
		Assert.assertNotNull(model);
		Assert.assertTrue(model instanceof ScoringModel3);
		Assert.assertEquals(ScoringModelType.Model3.event(), model.event());
	}

}
