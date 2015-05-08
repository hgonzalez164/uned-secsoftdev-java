package es.uned.secsoftdev.scoring.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

import es.uned.secsoftdev.utils.JniUtils;

@SpringBootApplication
@ComponentScan("es.uned.secsoftdev.scoring")
public class ScoringExecutorWebMain extends SpringBootServletInitializer {

	static {
		JniUtils.loadLibraryPath(ScoringExecutorWebMain.class);
	}

	public ScoringExecutorWebMain() {
		super();
	}

	@Override
	protected SpringApplicationBuilder configure(
			SpringApplicationBuilder application) {
		return application.sources(ScoringExecutorWebMain.class);
	}

	public static void main(String[] args) throws Exception {

		SpringApplication.run(ScoringExecutorWebMain.class, args);
	}

}
