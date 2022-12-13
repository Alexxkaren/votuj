package sk.upjs.ics.votuj.storage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneId;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;


public class MysqlVoteDao implements VoteDao {

	private JdbcTemplate jdbcTemplate;

	public MysqlVoteDao(JdbcTemplate jdbcTemplate) {
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

	@Override
	public List<Vote> getAll() {
		String sql = "SELECT id, age, male, date, id_region, id_party FROM vote ";
		List<Vote> list = jdbcTemplate.query(sql, new VoteRowMapper());
		return list;
		// TODO unit test !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	}

	@Override
	public Vote getById(Long id) {
		String sql = "SELECT id, age, male, date, id_region, id_party FROM vote WHERE id = " + id;
		return jdbcTemplate.queryForObject(sql, new VoteRowMapper());
		// TODO unit test !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	
	}
	
	private class VoteRowMapper implements RowMapper<Vote> {

		@Override
		public Vote mapRow(ResultSet rs, int rowNum) throws SQLException {
			Vote vote = new Vote();
			vote.setId(rs.getLong("id"));
			vote.setAge(rs.getInt("age"));
			vote.setMale(rs.getBoolean("male"));
			//TODO toto v teste treba skontrolovat ci dava dobre casy
			vote.setDate(rs.getDate("date").toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
			Region region = DaoFactory.INSTANCE.getRegionDao().getById(rs.getLong("id_region"));
			vote.setRegion(region);
			Party party = DaoFactory.INSTANCE.getPartyDao().getById(rs.getLong("id_party"));
			vote.setParty(party);
			
			return vote;
		}
		
	}

}
