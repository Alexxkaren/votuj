package sk.upjs.ics.votuj.storage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

public class MysqlProgramDao implements ProgramDao {

	private JdbcTemplate jdbcTemplate;

	public MysqlProgramDao(JdbcTemplate jdbcTemplate) {
		super();
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public Program save(Program program) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean delete(Long id) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public List<Program> getByParty(Party party) {
		String sql = "SELECT id, name, id_party, is_active, id_term FROM program WHERE id_party = " + party.getId();
		return jdbcTemplate.query(sql, new ProgramRowMapper());
		// TODO unit test !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	}
	/*//stare --> ked tak treba zmenit aj v programDao
	@Override
	public Program getByTermParty(Term term, Party party) {
		String sql = "SELECT id, name, id_party, is_active, id_term FROM program "+
					 "WHERE id_party = " + party.getId() + " AND id_term = " + term.getId();
		return jdbcTemplate.queryForObject(sql, new ProgramRowMapper());
		// TODO unit test !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	} */
	
	@Override
	public List<Program> getByTermParty(Term term, Party party) {
		String sql = "SELECT id, name, id_party, is_active, id_term FROM program "+
					 "WHERE id_party = " + party.getId() + " AND id_term = " + term.getId();
		
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
			//program.setParty(party);
			program.setIs_active(rs.getBoolean("is_active"));
			Term term = DaoFactory.INSTANCE.getTermDao().getById(rs.getLong("id_term"));
			program.setTerm(term);
			return program;
		}
		
	}
}
