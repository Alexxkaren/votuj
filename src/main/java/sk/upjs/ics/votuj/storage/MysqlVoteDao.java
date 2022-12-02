package sk.upjs.ics.votuj.storage;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;

public class MysqlVoteDao implements VoteDao {

	private JdbcTemplate jdbcTemplate;

	public MysqlVoteDao(JdbcTemplate jdbcTemplate) {
		super();
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public Vote save(Vote vote) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Vote> getByParty(Party party) {
		// TODO Auto-generated method stub
		return null;
	}

}
