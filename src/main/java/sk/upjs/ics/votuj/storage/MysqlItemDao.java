package sk.upjs.ics.votuj.storage;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;

public class MysqlItemDao implements ItemDao {

	private JdbcTemplate jdbcTemplate;

	public MysqlItemDao(JdbcTemplate jdbcTemplate) {
		super();
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public Item save(Item item) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Item> getByProgram(Program program) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Item> getByProgramCategory(Program program, Category category) {
		// TODO Auto-generated method stub
		return null;
	}

}
