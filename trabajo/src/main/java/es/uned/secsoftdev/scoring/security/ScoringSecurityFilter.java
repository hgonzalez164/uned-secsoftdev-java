package es.uned.secsoftdev.scoring.security;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class ScoringSecurityFilter extends OncePerRequestFilter {

	@Autowired
	private ScoringSecurityService scoringSecurityService;

	private Set<String> publicUrls;

	public ScoringSecurityFilter() {
		super();
		this.publicUrls = new HashSet<String>();
		this.publicUrls.add("/favicon.ico");
		this.publicUrls.add("/login");
		this.publicUrls.add("/error");
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request,
			HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		if (!checkPublicUrls(request)) {

			boolean authenticated = this.scoringSecurityService
					.checkAuthentication(request, response);

			if (!authenticated) {
				response.sendRedirect("/login");
				return;
			}
		}

		filterChain.doFilter(request, response);
	}

	private boolean checkPublicUrls(HttpServletRequest request) {
		return this.publicUrls.contains(request.getRequestURI());
	}

}
