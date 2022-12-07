package sk.upjs.ics.votuj.storage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.NoSuchElementException;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class MysqlAdminDao implements AdminDao {

	private JdbcTemplate jdbcTemplate;

	public MysqlAdminDao(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public Admin getByName(String name) throws NoSuchElementException {
		if (name == null || name.equals("")) {
			throw new NullPointerException("Name cannot be null or empty");
		}
		String sql = "SELECT id, name, password FROM admin WHERE name = '" + name + "'";
		try {
			return jdbcTemplate.queryForObject(sql, new RowMapper<Admin>() {

				@Override
				public Admin mapRow(ResultSet rs, int rowNum) throws SQLException {
					Admin admin = new Admin();
					admin.setId(rs.getLong("id"));
					admin.setName(rs.getString("name"));
					admin.setPassword(rs.getString("password"));
					return admin;
				}

			});
		} catch (EmptyResultDataAccessException e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setContentText("Zadane meno je nespravne. Skontrolujte svoje prihlasovacie meno a heslo");
			alert.show();
			throw new NoSuchElementException("Admin with login name: " + name + "not in DB");
		}
	}

	// TODO unit test !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
}
