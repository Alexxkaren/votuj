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



public class MysqlTermDao implements TermDao {

	private JdbcTemplate jdbcTemplate;

	public MysqlTermDao(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public Term save(Term term) throws NullPointerException, NoSuchElementException {
		if (term==null) {
			throw new NullPointerException("Cannot save null");
		}
		if (term.getSince()==null) {
			throw new NullPointerException("Term since cannot be null");
		}
		if (term.getTo()==null) {
			throw new NullPointerException("Term to cannot be null");
		}
		//////////////////////////////////////INSERT///////////////////ú
		if (term.getId() == null) { 
			SimpleJdbcInsert saveInsert = new SimpleJdbcInsert(jdbcTemplate);
			saveInsert.withTableName("term");
			saveInsert.usingColumns("since", "`to`");
			saveInsert.usingGeneratedKeyColumns("id");
			Map<String, Object> values = new HashMap<>();
			values.put("since", term.getSince());
			values.put("`to`", term.getTo());
			long id = saveInsert.executeAndReturnKey(values).longValue();
			return new Term(id,term.getSince(), term.getTo());
		/////////////////////////////////////////UPDATE/////////////////
		} else {
			String sql = "UPDATE term SET since= ?, `to`=? " 
					+ "WHERE id = ? ";
			int updated = jdbcTemplate.update(sql, term.getSince(), term.getTo(), term.getId());
			if (updated == 1) {
				return term; 
			} else {
				throw new NoSuchElementException("term with id: " + term.getId() + " not in DB.");
			}
		}		
	}

	@Override
	public boolean delete(Long id) throws ObjectUndeletableException {
		int wasDeleted;
		try {
			wasDeleted = jdbcTemplate.update("DELETE FROM term WHERE id = " + id);
		} catch (DataIntegrityViolationException e) {
			throw new ObjectUndeletableException("Term with id: " + id + "cannot be deleted, some candidate/program already has this term");
		}
				
		return wasDeleted == 1;
	}

	@Override
	public List<Term> getAll() {
		String sql = "SELECT id, since, `to` FROM term";
		List<Term> terms = jdbcTemplate.query(sql, new TermRowMapper());
		return terms;
		// TODO unit test !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	}

	@Override
	public Term getById(Long id) {
		String sql = "SELECT id, since, `to` FROM term WHERE id = " + id; 
		return jdbcTemplate.queryForObject(sql, new TermRowMapper());
		// TODO unit test !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	}
	
	@Override
	public List<Term> getByCandidate(Candidate candidate) {
		String sql = "SELECT id, since, `to` FROM  term "
					+ "JOIN candidate_has_term cht ON cht.id_term = term.id "
					+ "WHERE cht.id_candidate = " + candidate.getId();
		List<Term> listT = jdbcTemplate.query(sql, new TermRowMapper());
		return listT;
		
		
	}
	
	private class TermRowMapper implements RowMapper<Term> {

		@Override
		public Term mapRow(ResultSet rs, int rowNum) throws SQLException {
			Long id = rs.getLong("id");
			Integer since = rs.getInt("since");
			Integer to = rs.getInt("to");
			return new Term(id, since, to);
		}
		
	}

}
