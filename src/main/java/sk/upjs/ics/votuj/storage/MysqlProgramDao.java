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
	public List<Program> getByParty(Party party) {
		String sql = "SELECT id, name, id_party, is_active, id_term FROM Program WHERE id_party = " + party.getId();
		return jdbcTemplate.query(sql, new RowMapper<Program>() {

			@Override
			public Program mapRow(ResultSet rs, int rowNum) throws SQLException {
				Program program = new Program();
				program.setId(rs.getLong("id"));
				program.setName(rs.getString("name"));
				program.setParty(party);
				program.setIs_active(rs.getBoolean("is_active"));
				Term term = DaoFactory.INSTANCE.getTermDao().getById(rs.getLong("id_term"));
				program.setTerm(term);
				return program;
			}

		});
		// TODO unit test !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	}

	@Override
	public Program getByTermParty(Term term, Party partry) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean delete(Long id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Program getById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

}
