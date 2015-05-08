package es.uned.secsoftdev.scoring.security;

import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;
import java.util.logging.Logger;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import es.uned.secsoftdev.scoring.web.AuthenticationCommand;

@Service
public class ScoringSecurityService {

	public static final String AUTH_COOKIE_NAME = "AUTH_COOKIE";

	public static final String USER_SESSION_NAME = "USER_SESSION";

	public static final Integer COOKIE_MAX_AGE = 1000 * 60 * 60 * 24 * 2;

	private static Logger logger = Logger
			.getLogger(ScoringSecurityService.class.getName());

	@Autowired
	private ScoringUserDao scoringUserDao;

	public void authenticate(AuthenticationCommand auth,
			HttpServletRequest request, HttpServletResponse response)
			throws ScoringSecurityException {

		ScoringUser user = this.scoringUserDao.findScoringUser(auth
				.getUsername());
		if (user == null) {
			logger.info("Usuario desconocido: " + auth.getUsername());
			throw new ScoringSecurityException("Datos incorrectos");
		}

		// Comprobar numero de intentos fallidos
		Integer failedLoginCount = user.getFailedLoginCount();
		if (failedLoginCount > 3) {
			logger.info("Cuenta bloqueada usuario: " + auth.getUsername());
			throw new ScoringSecurityException("Cuenta bloqueada");
		}

		String encodedPassword = ScoringSecurityEncoder.encodePassword(
				user.getUsername(), auth.getPassword());
		if (!encodedPassword.equals(user.getPassword())) {

			user.setFailedLoginCount(failedLoginCount + 1);
			this.scoringUserDao.saveUserLoginAttemps(user);
			throw new ScoringSecurityException("Datos incorrectos");
		}

		saveAuthentication(user, auth.getRememberMe(), request, response);
	}

	public ScoringUser retrieveAuthenticatedUser(HttpServletRequest request) {

		HttpSession session = request.getSession();
		ScoringUser user = (ScoringUser) session
				.getAttribute(USER_SESSION_NAME);
		return user;
	}

	public boolean checkAuthentication(HttpServletRequest request,
			HttpServletResponse response) {

		ScoringUser user = retrieveAuthenticatedUser(request);
		if (user != null) {
			return true;
		}

		String cookieValue = findAuthCookieValue(request);
		if (cookieValue != null && !cookieValue.isEmpty()) {

			String[] decodedCookieValue = decodeCookieValue(cookieValue);
			if (decodedCookieValue != null && decodedCookieValue.length == 3) {

				// Buscar usuario
				user = this.scoringUserDao
						.findScoringUser(decodedCookieValue[1]);
				if (user == null) {
					return false;
				}

				// Autenticacion por cookie remember-me
				if (user.getId().toString().equals(decodedCookieValue[0])
						&& user.getPassword().equals(decodedCookieValue[2])) {

					saveAuthentication(user, false, request, response);
					return true;
				}
			}
		}

		return false;
	}

	public void logout(HttpServletRequest request, HttpServletResponse response) {

		HttpSession session = request.getSession();
		session.invalidate();

		Cookie cookie = new Cookie(AUTH_COOKIE_NAME, null);
		String cookiePath = request.getContextPath();
		if (!StringUtils.hasLength(cookiePath)) {
			cookiePath = "/";
		}
		cookie.setPath(cookiePath);
		cookie.setMaxAge(0);
		response.addCookie(cookie);
	}

	private void saveAuthentication(ScoringUser user, Boolean rememberMe,
			HttpServletRequest request, HttpServletResponse response) {

		// Guardar en sesion
		HttpSession session = request.getSession();
		session.setAttribute(USER_SESSION_NAME, user);

		// Guardar cookie [ID:USER:PASS]
		if (rememberMe != null && rememberMe.booleanValue()) {
			String cookieValue = encodeCookieValue(new String[] {
					user.getId().toString(), user.getUsername(),
					user.getPassword() });

			Cookie cookie = new Cookie(AUTH_COOKIE_NAME, cookieValue);
			cookie.setMaxAge(COOKIE_MAX_AGE);
			response.addCookie(cookie);
		}
	}

	private String findAuthCookieValue(HttpServletRequest request) {

		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (int i = 0; i < cookies.length; i++) {
				Cookie cookie = cookies[i];
				if (AUTH_COOKIE_NAME.equals(cookie.getName())) {
					return cookie.getValue();
				}
			}
		}
		return null;
	}

	private String encodeCookieValue(String[] tokens) {

		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < tokens.length; i++) {
			sb.append(tokens[i]);
			if (i < tokens.length - 1) {
				sb.append(":");
			}
		}
		Encoder encoder = Base64.getEncoder();
		String encoded = new String(encoder.encode(sb.toString().getBytes()));
		return encoded;
	}

	private String[] decodeCookieValue(String cookieValue) {

		Decoder decoder = Base64.getDecoder();
		String plain = new String(decoder.decode(cookieValue.getBytes()));
		String[] tokens = StringUtils.delimitedListToStringArray(plain, ":");
		return tokens;
	}

}
