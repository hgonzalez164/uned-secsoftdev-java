package es.uned.secsoftdev.scoring.web;

import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import es.uned.secsoftdev.scoring.ScoringDataHandler;
import es.uned.secsoftdev.scoring.ScoringExecutorMain;
import es.uned.secsoftdev.scoring.config.ScoringAuditLogger;
import es.uned.secsoftdev.scoring.config.ScoringConfigException;
import es.uned.secsoftdev.scoring.config.ScoringConfigService;
import es.uned.secsoftdev.scoring.config.UserConfig;
import es.uned.secsoftdev.scoring.model.ScoringModelFactory;
import es.uned.secsoftdev.scoring.security.ScoringSecurityException;
import es.uned.secsoftdev.scoring.security.ScoringSecurityService;
import es.uned.secsoftdev.scoring.security.ScoringUser;

@Controller
public class ScoringController implements ErrorController {

	private static final String CSRF_TOKEN = "CSRF_TOKEN";

	private static final String ARGS_REGEX = "^[a-zA-Z0-9]*$";

	@Autowired
	private ScoringSecurityService scoringSecurityService;

	@Autowired
	private ScoringConfigService scoringConfigService;

	@Autowired
	private ScoringModelFactory scoringModelFactory;

	@Autowired
	private ScoringDataHandler scoringDataHandler;

	@RequestMapping("/")
	public String home(@ModelAttribute("exec") ExecutionCommand exec,
			HttpServletRequest request, Model model) {

		HttpSession session = request.getSession();
		ScoringUser user = (ScoringUser) session
				.getAttribute(ScoringSecurityService.USER_SESSION_NAME);
		model.addAttribute("user", user);

		UserConfig userConfig = null;
		String userHome = null;
		try {
			userConfig = this.scoringConfigService.retrieveUserConfig(user);
			userHome = this.scoringConfigService
					.retrieveUserHomePath(userConfig);
		} catch (ScoringConfigException e) {
			ScoringAuditLogger
					.log("Error al recuperar configuracion del usuario "
							+ user.getUsername() + " : "
							+ e.getLocalizedMessage());
		}

		model.addAttribute("config", userConfig);

		List<String> dataFiles = scoringDataHandler.listUserFiles(userHome);
		model.addAttribute("dataFiles", dataFiles);

		// CSRF Token
		String csrfToken = UUID.randomUUID().toString();
		exec.setCsrfToken(csrfToken);
		request.getSession().setAttribute(CSRF_TOKEN, csrfToken);

		return "home";
	}

	@RequestMapping(value = "/execute", method = RequestMethod.POST)
	public @ResponseBody ExecutionCommand execute(
			@ModelAttribute("exec") ExecutionCommand exec,
			HttpServletRequest request, HttpServletResponse response,
			BindingResult bindingResult) {

		// Check CSRF
		String csrfToken = exec.getCsrfToken();
		String sessionCsrfToken = (String) request.getSession().getAttribute(
				CSRF_TOKEN);
		if (csrfToken == null || csrfToken.isEmpty()
				|| sessionCsrfToken == null || sessionCsrfToken.isEmpty()
				|| !csrfToken.equals(sessionCsrfToken)) {
			exec.setStatus("ERROR");
			exec.setDetails("Token no valido");
			return exec;
		}

		ScoringUser user = this.scoringSecurityService
				.retrieveAuthenticatedUser(request);

		UserConfig userConfig = null;
		String userHome = null;
		try {
			userConfig = this.scoringConfigService.retrieveUserConfig(user);
			userHome = this.scoringConfigService
					.retrieveUserHomePath(userConfig);
		} catch (ScoringConfigException e) {
			ScoringAuditLogger
					.log("Error al recuperar configuracion del usuario "
							+ user.getUsername() + " : "
							+ e.getLocalizedMessage());
			exec.setStatus("ERROR");
			exec.setDetails("Configuracion incorrecta");
			return exec;
		}

		List<String> dataFiles = scoringDataHandler.listUserFiles(userHome);

		// Validar parametros de entrada
		String inputFile = exec.getFile();
		if (inputFile == null || inputFile.length() == 0
				|| inputFile.length() > 10 || !inputFile.matches(ARGS_REGEX)
				|| !dataFiles.contains(inputFile)) {
			exec.setStatus("ERROR");
			exec.setDetails("Fichero incorrecto");
			return exec;
		}

		String modelArgs = exec.getModelArgs();

		ScoringExecutorMain executor = new ScoringExecutorMain();
		executor.setConfigService(this.scoringConfigService);
		executor.setModelFactory(this.scoringModelFactory);

		String[] result = null;
		try {
			result = executor.execute(userConfig, userHome, inputFile,
					modelArgs);
		} catch (Exception e) {
			ScoringAuditLogger.log("Error al ejecutar proceso de scoring: "
					+ e.getLocalizedMessage());
			exec.setStatus("ERROR");
			exec.setDetails("Ejecucion erronea");
			return exec;
		}

		if (result != null) {
			exec.setStatus("OK");
			exec.setDetails("Registros procesados: "
					+ result[0]
					+ " Fichero salida: "
					+ result[1].substring(result[1].lastIndexOf("/") + 1,
							result[1].length()));
		} else {
			exec.setStatus("ERROR");
			exec.setDetails("Resultado nulo");
		}

		return exec;
	}

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String loginForm(@ModelAttribute("auth") AuthenticationCommand auth) {
		return "login";
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String loginPost(@ModelAttribute("auth") AuthenticationCommand auth,
			HttpServletRequest request, HttpServletResponse response,
			BindingResult bindingResult) {

		try {
			this.scoringSecurityService.authenticate(auth, request, response);
		} catch (ScoringSecurityException e) {
			bindingResult.addError(new ObjectError(bindingResult
					.getObjectName(), e.getLocalizedMessage()));
			return "login";
		}

		return "redirect:/";
	}

	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logout(HttpServletRequest request,
			HttpServletResponse response) {
		this.scoringSecurityService.logout(request, response);
		return "redirect:/";
	}

	@RequestMapping("/error")
	public String error(Model model) {
		return "error";
	}

	@Override
	public String getErrorPath() {
		return "/error";
	}

}
