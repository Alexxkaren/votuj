package sk.upjs.ics.votuj.storage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;


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

}
