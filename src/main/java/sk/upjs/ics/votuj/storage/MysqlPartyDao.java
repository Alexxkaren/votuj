package sk.upjs.ics.votuj.storage;

import org.springframework.jdbc.core.JdbcTemplate;

public class MysqlPartyDao implements PartyDao {

	private JdbcTemplate jdbcTemplate;

	public MysqlPartyDao(JdbcTemplate jdbcTemplate) {
		super();
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

}
