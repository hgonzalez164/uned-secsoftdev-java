package es.uned.secsoftdev.scoring;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ScoringDataReader {

	private static final String SEPARATOR = "\t";

	private Logger logger = Logger.getLogger(getClass().getName());

	private File input;

	private FileReader fr;

	private BufferedReader br;

	public ScoringDataReader(File file) {
		super();
		this.input = file;
	}

	public boolean init() {
		try {
			this.fr = new FileReader(this.input);
			this.br = new BufferedReader(fr);
			return true;
		} catch (Exception e) {
			logger.log(Level.SEVERE,
					"Error al inicializar reader: " + e.getLocalizedMessage());
			return false;
		}
	}

	public ScoringDataInputRecord read() {

		try {
			String line = this.br.readLine();
			if (line == null) {
				return null;
			}
			return parse(line);
		} catch (IOException e) {
			logger.log(Level.SEVERE,
					"Error al leer registro: " + e.getLocalizedMessage());
			return null;
		}
	}

	private ScoringDataInputRecord parse(String line) {

		ScoringDataInputRecord record = new ScoringDataInputRecord();

		String[] split = line.split(SEPARATOR);
		if (split != null && split.length > 2 && split[0] != null
				&& split[0].length() > 0) {

			record.setId(split[0]);

			Object[] values = new Object[split.length - 1];
			for (int i = 1; i < split.length; i++) {
				if (split[i] != null && split[i].length() > 0) {

					try {
						values[i - 1] = Double.valueOf(split[i]).doubleValue();
					} catch (NumberFormatException e) {
						values[i - 1] = split[i];
					}
				}
			}

			record.setInputs(values);
		}

		return record;
	}

	public boolean close() {
		try {
			this.br.close();
			this.fr.close();
			return true;
		} catch (IOException e) {
			logger.log(Level.WARNING,
					"Error al cerrar reader: " + e.getLocalizedMessage());
			return false;
		}
	}

}
