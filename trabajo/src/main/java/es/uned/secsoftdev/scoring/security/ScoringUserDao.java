package es.uned.secsoftdev.scoring.security;

import java.sql.Types;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.stereotype.Component;

import es.uned.secsoftdev.scoring.config.ScoringAuditLogger;

@Component
public class ScoringUserDao {

	private final Logger logger = Logger.getLogger(ScoringUserDao.class
			.getName());

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public ScoringUser findScoringUser(String username) {

		String sql = "select id, username, password, failed_login_count from users where username = ?";
		PreparedStatementCreatorFactory factory = new PreparedStatementCreatorFactory(
				sql);
		factory.addParameter(new SqlParameter(Types.VARCHAR));
		PreparedStatementCreator psc = factory
				.newPreparedStatementCreator(new Object[] { username });

		ScoringUser user = null;
		try {
			List<ScoringUser> result = this.jdbcTemplate.query(psc,
					new BeanPropertyRowMapper<ScoringUser>(ScoringUser.class));
			user = result.iterator().next();

		} catch (DataAccessException e) {
			logger.log(Level.INFO, "Usuario '" + username + "' no encontrado: "
					+ e.getLocalizedMessage());
			ScoringAuditLogger.log("Error al recuperar usuario: "
					+ e.getLocalizedMessage());
		}
		return user;
	}

	public void saveUserLoginAttemps(ScoringUser user) {

		String sql = "update users set failed_login_count = ? where id = ?";
		PreparedStatementCreatorFactory factory = new PreparedStatementCreatorFactory(
				sql, Types.INTEGER, Types.INTEGER);
		PreparedStatementCreator psc = factory
				.newPreparedStatementCreator(new Object[] {
						user.getFailedLoginCount(), user.getId() });
		this.jdbcTemplate.update(psc);
	}

}
