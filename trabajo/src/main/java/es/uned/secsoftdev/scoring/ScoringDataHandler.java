package es.uned.secsoftdev.scoring;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class ScoringDataHandler {

	private static final String SCORE_FILE_PREFIX = "scorings";

	private static final SimpleDateFormat df = new SimpleDateFormat(
			"yyyy-MM-dd-HH-mm-ss-SSS");

	public List<String> listUserFiles(String userHome) {

		List<String> userDataFiles = new ArrayList<String>();

		File f = new File(userHome);
		if (f.exists() && f.canRead() && f.isDirectory()) {
			File[] files = f.listFiles();

			for (int i = 0; i < files.length; i++) {
				File file = files[i];
				if (file.isFile()
						&& !file.getName().startsWith(SCORE_FILE_PREFIX)) {
					userDataFiles.add(file.getName());
				}
			}
		}
		return userDataFiles;
	}

	public String generateInputFilePath(String userHome, String inputFileName) {
		StringBuilder sb = new StringBuilder();
		sb.append(userHome).append(inputFileName);
		return sb.toString();
	}

	public String generateOutputFilePath(String userHome, String eventName,
			String inputFileName) {
		StringBuilder sb = new StringBuilder();
		sb.append(userHome).append(SCORE_FILE_PREFIX).append('_')
				.append(eventName).append('_')
				.append(inputFileName.replaceAll("\\.", "_")).append('_')
				.append(df.format(new Date()));
		return sb.toString();
	}

}
