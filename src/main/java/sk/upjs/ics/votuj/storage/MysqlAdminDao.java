package sk.upjs.ics.votuj.storage;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

public class MysqlAdminDao implements AdminDao {

	private JdbcTemplate jdbcTemplate;

	@Override
	public Admin getByName(String name) {
		if (name == null || name.equals("")) {
			throw new NullPointerException("Name cannot be null or empty");
		}
		String sql = "SELECT id, name, password FROM admin WHERE name = " + name;
		return jdbcTemplate.query(sql, new ResultSetExtractor<Admin>() {

			@Override
			public Admin extractData(ResultSet rs) throws SQLException, DataAccessException {
				Admin admin = new Admin();
				admin.setName(rs.getString("name"));
				admin.setId(rs.getLong("id"));
				admin.setPassword(rs.getString("password"));
				return admin;
			}

		});
	}

	// TODO unit test !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
}
