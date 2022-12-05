package sk.upjs.ics.votuj.storage;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;

public class MysqlCandidateDao implements CandidateDao {

	private JdbcTemplate jdbcTemplate;

	public MysqlCandidateDao(JdbcTemplate jdbcTemplate) {
		super();
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public Candidate save(Candidate candidate) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Candidate> getByTermParty(Party party, Term term) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean delete(Long id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Candidate getById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

}
