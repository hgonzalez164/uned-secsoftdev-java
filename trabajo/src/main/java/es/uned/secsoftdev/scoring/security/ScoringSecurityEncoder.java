package es.uned.secsoftdev.scoring.security;

import java.security.MessageDigest;

import es.uned.secsoftdev.scoring.config.ScoringAuditLogger;

public class ScoringSecurityEncoder {

	private static final String ALGORITHM = "MD5";

	private static final String ENCODING = "UTF-8";

	public static String encodePassword(String username, String password) {
		String saltedPassword = username + ":" + password;
		return encodePassword(saltedPassword);
	}

	public static String encodePassword(String password) {
		try {
			MessageDigest md = MessageDigest.getInstance(ALGORITHM);
			byte[] digest = md.digest(password.getBytes(ENCODING));
			StringBuilder sb = new StringBuilder(2 * digest.length);
			for (byte b : digest) {
				sb.append(String.format("%02x", b & 0xff));
			}
			return sb.toString();
		} catch (Exception e) {
			ScoringAuditLogger.log("Excepcion: " + e.getLocalizedMessage());
			return null;
		}
	}

}
