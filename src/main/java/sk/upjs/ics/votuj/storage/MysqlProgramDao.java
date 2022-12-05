package sk.upjs.ics.votuj.storage;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;

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
		// TODO Auto-generated method stub
		return null;
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
