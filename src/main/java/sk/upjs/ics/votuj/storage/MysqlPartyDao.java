package sk.upjs.ics.votuj.storage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class MysqlPartyDao implements PartyDao {

	private JdbcTemplate jdbcTemplate;

	public MysqlPartyDao(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public Party save(Party party) throws NoSuchElementException,NullPointerException {
		if (party == null) {
			throw new NullPointerException("Cannot save null");
		}
		if (party.getName() == null) {
			throw new NullPointerException("Party name cannot be null");
		}
		if (party.getInfo() == null) {
			throw new NullPointerException("Party info cannot be null");
		}
		// insert
		if (party.getId() == null) {
			SimpleJdbcInsert saveInsert = new SimpleJdbcInsert(jdbcTemplate);
			saveInsert.withTableName("party");
			saveInsert.usingColumns("name", "info");
			saveInsert.usingGeneratedKeyColumns("id");
			Map<String, Object> values = new HashMap<>();
			values.put("name", party.getName());
			values.put("info", party.getInfo());
			long id = saveInsert.executeAndReturnKey(values).longValue();
			return new Party(id, party.getName(), party.getInfo());
		} else {			// update
			String sql = "UPDATE party SET name= ?, info=? " + "WHERE id = ? ";
			int updated = jdbcTemplate.update(sql, party.getName(), party.getInfo(), party.getId());
			if (updated == 1) {
				return party;
			} else {
				throw new NoSuchElementException("party with id: " + party.getId() + " not in DB.");
			}
		}
	}

	@Override
	public boolean delete(Long id)throws ObjectUndeletableException {
		int delete;
		try {
			delete = jdbcTemplate.update("DELETE FROM party WHERE  id= " + id);
		} catch (DataIntegrityViolationException e) {
			throw new ObjectUndeletableException("Some candidate is in this political party. Party can not be deleted");
		}
		return delete == 1;
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
