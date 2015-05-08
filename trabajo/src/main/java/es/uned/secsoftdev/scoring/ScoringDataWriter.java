package es.uned.secsoftdev.scoring;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ScoringDataWriter {

	private static final String SEPARATOR = "\t";

	private Logger logger = Logger.getLogger(getClass().getName());

	private File output;

	private FileWriter fw;

	private BufferedWriter bw;

	public ScoringDataWriter(File file) {
		super();
		this.output = file;
	}

	public boolean init() {
		try {
			this.fw = new FileWriter(this.output);
			this.bw = new BufferedWriter(fw);
			return true;
		} catch (Exception e) {
			logger.log(Level.SEVERE,
					"Error al inicializar writer: " + e.getLocalizedMessage());
			return false;
		}
	}

	public boolean write(ScoringDataOutputRecord record) {

		try {
			if (record != null) {
				String line = serialize(record);
				this.bw.write(line);
			}
			return true;
		} catch (IOException e) {
			logger.log(Level.SEVERE,
					"Error al escribir registro: " + e.getLocalizedMessage());
			return false;
		}
	}

	private String serialize(ScoringDataOutputRecord record) {

		StringBuilder sb = new StringBuilder();
		sb.append(record.getId()).append(SEPARATOR).append(record.getScore())
				.append(System.getProperty("line.separator"));
		return sb.toString();
	}

	public boolean close() {
		try {
			this.bw.close();
			this.fw.close();
			return true;
		} catch (IOException e) {
			logger.log(Level.WARNING,
					"Error al cerrar writer: " + e.getLocalizedMessage());
			return false;
		}
	}

}
