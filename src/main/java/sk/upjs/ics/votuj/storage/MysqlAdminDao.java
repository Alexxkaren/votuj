package sk.upjs.ics.votuj.storage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

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

	// only for test
	@Override
	public Admin save(Admin admin) throws NoSuchElementException,NullPointerException {
		if (admin == null) {
			throw new NullPointerException("Cannot save null");
		}
		if (admin.getName() == null) {
			throw new NullPointerException("Admin name cannot be null");
		}
		if (admin.getPassword() == null) {
			throw new NullPointerException("Admin password cannot be null");
		}
		// insert
		if (admin.getId() == null) {
			SimpleJdbcInsert saveInsert = new SimpleJdbcInsert(jdbcTemplate);
			saveInsert.withTableName("admin");
			saveInsert.usingColumns("name", "password");
			saveInsert.usingGeneratedKeyColumns("id");
			Map<String, Object> values = new HashMap<>();
			values.put("name", admin.getName());
			values.put("password", admin.getPassword());
			long id = saveInsert.executeAndReturnKey(values).longValue();
			return new Admin(id, admin.getName(), admin.getPassword());
			// update
		} else {
			String sql = "UPDATE item SET name= ?, password= ?" + "WHERE id = ? ";
			int updated = jdbcTemplate.update(sql, admin.getName(), admin.getPassword(), admin.getId());
			if (updated == 1) {
				return admin;
			} else {
				throw new NoSuchElementException("admin with id: " + admin.getId() + " not in DB.");
			}
		}
	}
	
	@Override
	public boolean delete(Long id)throws ObjectUndeletableException {
		int delete;
		try {
			delete = jdbcTemplate.update("DELETE FROM admin WHERE  id= " + id);
		} catch (DataIntegrityViolationException e) {
			throw new ObjectUndeletableException("Admin can not be deleted");
		}
		return delete == 1;
	}

	// TODO unit test !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
}
