package sk.upjs.ics.votuj.storage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class MysqlPartyDao implements PartyDao {

	private JdbcTemplate jdbcTemplate;

	public MysqlPartyDao(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public Party save(Party party) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean delete(Long id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<Party> getAll() {
		String sql = "SELECT id, name, info FROM party";
		List<Party> list = jdbcTemplate.query(sql, new PartyRowMapper());
		return list;
		// TODO unit test !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	}

	@Override
	public Party getById(Long id) throws NoSuchElementException {
		String sql = "SELECT id, name,info FROM party WHERE id = " + id;
		try {
			Party party = jdbcTemplate.queryForObject(sql, new PartyRowMapper());
			return party;
		} catch (EmptyResultDataAccessException e) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setContentText("Politicka strana s danym id" + id + " sa nenachadza v databaze");
			alert.show();
			throw new NoSuchElementException("party with id: " + id + "not in DB");
		}
		// TODO unit test !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	}

	private class PartyRowMapper implements RowMapper<Party> {

		@Override
		public Party mapRow(ResultSet rs, int rowNum) throws SQLException {
			Party party = new Party();
			party.setId(rs.getLong("id"));
			party.setName(rs.getString("name"));
			party.setInfo(rs.getString("info"));
			return party;
		}

	}

}
