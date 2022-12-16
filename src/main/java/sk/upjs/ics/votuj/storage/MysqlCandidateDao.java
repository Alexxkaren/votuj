package sk.upjs.ics.votuj.storage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

public class MysqlCandidateDao implements CandidateDao {

	private JdbcTemplate jdbcTemplate;

	public MysqlCandidateDao(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public Candidate save(Candidate candidate) {
		// TODO 
		return null;
	}

	@Override
	public boolean delete(Long id) {
		// TODO 
		return false;
	}
	
	@Override
	public List<Candidate> getByTermParty(Party party, Term term) {
		String sql = "SELECT id, name, surname, candidate_number, info, id_party FROM candidate " +
				"LEFT JOIN candidate_has_term ON candidate_has_term.id_candidate = candidate.id " +
				"WHERE id_party = " + party.getId() +  " AND candidate_has_term.id_term = " + term.getId();
		return jdbcTemplate.query(sql, new CandidateRowMapper() );
		// TODO unit test !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	}
	
	@Override
	public Candidate getById(Long id) {
		String sql = "SELECT id, name, surname, candidate_number, info, id_party FROM candidate " +
					 "WHERE id = " + id;
		return jdbcTemplate.queryForObject(sql, new CandidateRowMapper() );
		// TODO unit test !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		
	}
	
	private class CandidateRowMapper implements RowMapper<Candidate> {

		@Override
		public Candidate mapRow(ResultSet rs, int rowNum) throws SQLException {
			Candidate candidate = new Candidate();
			candidate.setId(rs.getLong("id"));
			candidate.setName(rs.getString("name"));
			candidate.setSurname(rs.getString("surname"));
			candidate.setCandidateNumber(rs.getString("candidate_number"));
			candidate.setInfo(rs.getString("info"));
			Party party = DaoFactory.INSTANCE.getPartyDao().getById(rs.getLong("id_party"));
			candidate.setParty(party);
			
			return candidate;
		};

	};
	


}
