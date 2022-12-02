package sk.upjs.ics.votuj.storage;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;

public class MysqlTermDao implements TermDao {

	private JdbcTemplate jdbcTemplate;

	public MysqlTermDao(JdbcTemplate jdbcTemplate) {
		super();
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public Term save(Term term) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean delete(Long id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Term getByPeriod(Integer since, Integer to) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Term> getAll(Term term) {
		// TODO Auto-generated method stub
		return null;
	}

}
