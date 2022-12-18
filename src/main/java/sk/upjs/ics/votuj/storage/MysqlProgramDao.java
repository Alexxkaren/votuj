package sk.upjs.ics.votuj.storage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class MysqlProgramDao implements ProgramDao {

	private JdbcTemplate jdbcTemplate;

	public MysqlProgramDao(JdbcTemplate jdbcTemplate) {
		super();
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public Program save(Program program) throws NoSuchElementException,NullPointerException {
		if (program == null) {
			throw new NullPointerException("Cannot save null");
		}
		if (program.getName() == null) {
			throw new NullPointerException("Program name cannot be null");
		}
		if (program.getParty() == null) {
			throw new NullPointerException("Program party cannot be null");
		}
		if (program.getTerm() == null) {
			throw new NullPointerException("Program term cannot be null");
		}
		// insert
		if (program.getId() == null) {
			SimpleJdbcInsert saveInsert = new SimpleJdbcInsert(jdbcTemplate);
			saveInsert.withTableName("program");
			saveInsert.usingColumns("name", "id_party", "is_active","id_term");
			saveInsert.usingGeneratedKeyColumns("id");
			Map<String, Object> values = new HashMap<>();
			values.put("name", program.getName());
			values.put("id_party", program.getParty().getId());
			values.put("id_active", program.isActive()); 
			values.put("id_term", program.getTerm().getId());
			long id = saveInsert.executeAndReturnKey(values).longValue();
			return new Program(id, program.getName(), program.getParty(),program.isActive(), program.getTerm());
			// update
		} else {
			String sql = "UPDATE program SET name= ?, id_party= ?, is_active= ?, id_term= ? " + "WHERE id = ? ";
			int updated = jdbcTemplate.update(sql, program.getName(),program.getParty().getId(),program.isActive(),
					program.getTerm().getId(),program.getId());
			if (updated == 1) {
				return program;
			} else {
				throw new NoSuchElementException("program with id: " + program.getId() + " not in DB.");
			}
		}
	}

	@Override
	public boolean delete(Long id) {
		int delete;
		try {
			delete = jdbcTemplate.update("DELETE FROM program WHERE  id= " + id);
		} catch (DataIntegrityViolationException e) {
			throw new ObjectUndeletableException(
					"This political program belongs to a political party. Program can not be deleted");
		}
		return delete == 1;
	}

	@Override
	public List<Program> getByParty(Party party) {
		String sql = "SELECT id, name, id_party, is_active, id_term FROM program WHERE id_party = " + party.getId();
		return jdbcTemplate.query(sql, new ProgramRowMapper());
		// TODO unit test !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	}
	/*
	 * //stare --> ked tak treba zmenit aj v programDao
	 * 
	 * @Override public Program getByTermParty(Term term, Party party) { String sql
	 * = "SELECT id, name, id_party, is_active, id_term FROM program "+
	 * "WHERE id_party = " + party.getId() + " AND id_term = " + term.getId();
	 * return jdbcTemplate.queryForObject(sql, new ProgramRowMapper()); // TODO unit
	 * test !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! }
	 */

	@Override
	public List<Program> getByTermParty(Term term, Party party) {
		String sql = "SELECT id, name, id_party, is_active, id_term FROM program " + "WHERE id_party = " + party.getId()
				+ " AND id_term = " + term.getId();

		return jdbcTemplate.query(sql, new ProgramRowMapper());
		// TODO unit test !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	}

	@Override
	public Program getById(Long id) {
		String sql = "SELECT id, name, id_party, is_active, id_term FROM program WHERE id = " + id;
		return jdbcTemplate.queryForObject(sql, new ProgramRowMapper());
		// TODO unit test !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	}

	private class ProgramRowMapper implements RowMapper<Program> {

		@Override
		public Program mapRow(ResultSet rs, int rowNum) throws SQLException {
			Program program = new Program();
			program.setId(rs.getLong("id"));
			program.setName(rs.getString("name"));
			program.setParty(DaoFactory.INSTANCE.getPartyDao().getById(rs.getLong("id_party")));
			// program.setParty(party);
			program.setIsActive(rs.getBoolean("is_active"));
			Term term = DaoFactory.INSTANCE.getTermDao().getById(rs.getLong("id_term"));
			program.setTerm(term);
			return program;
		}

	}
}
