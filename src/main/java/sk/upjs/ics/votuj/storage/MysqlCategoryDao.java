package sk.upjs.ics.votuj.storage;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;

public class MysqlCategoryDao implements CategoryDao {

	private JdbcTemplate jdbcTemplate;

	public MysqlCategoryDao(JdbcTemplate jdbcTemplate) {
		super();
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public Category save(Category category) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean delete(Long id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Category getById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Category> getAll() {
		// TODO Auto-generated method stub
		return null;
	}

}
