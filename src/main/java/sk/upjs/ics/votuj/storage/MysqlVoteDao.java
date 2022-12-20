package sk.upjs.ics.votuj.storage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

public class MysqlVoteDao implements VoteDao {

	private JdbcTemplate jdbcTemplate;

	public MysqlVoteDao(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public Vote save(Vote vote) throws NoSuchElementException, NullPointerException {
		if (vote == null) {
			throw new NullPointerException("Cannot save null");
		}
		if (vote.getAge() == null) {
			throw new NullPointerException("Age can not be null");
		}
		if (vote.getDate() == null) {
			throw new NullPointerException("Date can not be null");
		}
		if (vote.getRegion() == null) {
			throw new NullPointerException("Region can not be less or equal to 0");
		}
		if (vote.getParty() == null) {
			throw new NullPointerException("Vote party can not be null");
		}
		// insert
		if (vote.getId() == null) {
			SimpleJdbcInsert saveInsert = new SimpleJdbcInsert(jdbcTemplate);
			saveInsert.withTableName("vote");
			saveInsert.usingColumns("age", "male", "date", "id_region", "id_party");
			saveInsert.usingGeneratedKeyColumns("id");
			Map<String, Object> values = new HashMap<>();
			// ZMENA
			values.put("age", Integer.parseInt(vote.getAge()));
			values.put("male", vote.getMale());
			values.put("date", vote.getDate().atZone(ZoneId.systemDefault()).toLocalDateTime());
			values.put("id_region", vote.getRegion().getId());
			values.put("id_party", vote.getParty().getId());
			long id = saveInsert.executeAndReturnKey(values).longValue();
			return new Vote(id, vote.getAge(), vote.getMale(), vote.getDate(), vote.getRegion(), vote.getParty());
			// update
		} else {
			String sql = "UPDATE vote SET age= ?, male= ?, date= ?, id_region= ?, id_party= ? " + "WHERE id = ? ";
			int updated = jdbcTemplate.update(sql, vote.getAge(), vote.getMale(), vote.getDate(),
					vote.getRegion().getId(), vote.getParty().getId(), vote.getId());
			if (updated == 1) {
				return vote;
			} else {
				throw new NoSuchElementException("vote with id: " + vote.getId() + " not in DB.");
			}
		}
	}

	@Override
	public boolean delete(Long id) throws ObjectUndeletableException {
		int delete;
		try {
			delete = jdbcTemplate.update("DELETE FROM vote WHERE  id= " + id);
		} catch (DataIntegrityViolationException e) {
			throw new ObjectUndeletableException("Vote can not be deleted");
		}
		return delete == 1;
	}

	@Override
	public List<Vote> getByParty(Party party) {
		String sql = "SELECT id, age, male, date, id_region, id_party FROM vote WHERE id_party = " + party.getId();
		return jdbcTemplate.query(sql, new VoteRowMapper());
	}

	@Override
	public List<Vote> getAll() {
		String sql = "SELECT id, age, male, date, id_region, id_party FROM vote ";
		List<Vote> list = jdbcTemplate.query(sql, new VoteRowMapper());
		return list;

	}

	@Override
	public Vote getById(Long id) {
		String sql = "SELECT id, age, male, date, id_region, id_party FROM vote WHERE id = " + id;
		return jdbcTemplate.queryForObject(sql, new VoteRowMapper());

	}

	private class VoteRowMapper implements RowMapper<Vote> {

		@Override
		public Vote mapRow(ResultSet rs, int rowNum) throws SQLException {
			Vote vote = new Vote();
			vote.setId(rs.getLong("id"));
			vote.setAge(rs.getString("age"));
			vote.setMale(rs.getBoolean("male"));
			vote.setDate(rs.getTimestamp("date").toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
			Region region = DaoFactory.INSTANCE.getRegionDao().getById(rs.getLong("id_region"));
			vote.setRegion(region);
			Party party = DaoFactory.INSTANCE.getPartyDao().getById(rs.getLong("id_party"));
			vote.setParty(party);

			return vote;
		}

	}

}
