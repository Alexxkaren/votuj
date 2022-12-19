package sk.upjs.ics.votuj.storage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;


public class MysqlRegionDao implements RegionDao{

	private JdbcTemplate jdbcTemplate;

	public MysqlRegionDao(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	
	@Override
	public Region getById(Long id) {
		String sql = "SELECT id, name FROM region WHERE id = " + id;
		return jdbcTemplate.queryForObject(sql, new RegionRowMapper());
		// TODO unit test !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	}

	@Override
	public List<Region> getAll() {
		String sql = "SELECT id, name FROM region";
		List<Region> list = jdbcTemplate.query(sql, new RegionRowMapper());
		return list;
		// TODO unit test !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	}
	
	private class RegionRowMapper implements RowMapper<Region> {

		@Override
		public Region mapRow(ResultSet rs, int rowNum) throws SQLException {
			Region region = new Region();
			region.setId(rs.getLong("id"));
			region.setName(rs.getString("name"));
			return region;
		}

	}

	@Override
	// just for test 
	public Region save(Region region) throws NoSuchElementException, NullPointerException {
		if (region == null) {
			throw new NullPointerException("Cannot save null");
		}
		if (region.getName() == null) {
			throw new NullPointerException("Name can not be null");
		}
		
		// insert
		if (region.getId() == null) {
			SimpleJdbcInsert saveInsert = new SimpleJdbcInsert(jdbcTemplate);
			saveInsert.withTableName("region");
			saveInsert.usingColumns("name");
			saveInsert.usingGeneratedKeyColumns("id");
			Map<String, Object> values = new HashMap<>();
			values.put("name", region.getName());
			long id = saveInsert.executeAndReturnKey(values).longValue();
			return new Region (id, region.getName());
			// update
		} else {
			String sql = "UPDATE region SET name= ? " + "WHERE id = ? ";
			int updated = jdbcTemplate.update(sql, region.getName(),region.getId());
			if (updated == 1) {
				return region;
			} else {
				throw new NoSuchElementException("region with id: " + region.getId() + " not in DB.");
			}
		}
	}


	@Override
	// just for test 
	public boolean delete(Long id) throws ObjectUndeletableException {
		int delete;
		try {
			delete = jdbcTemplate.update("DELETE FROM region WHERE  id= " + id);
		} catch (DataIntegrityViolationException e) {
			throw new ObjectUndeletableException("Region can not be deleted");
		}
		return delete == 1;
	}

}
