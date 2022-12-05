package sk.upjs.ics.votuj.storage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;


public class MysqlTermDao implements TermDao {

	private JdbcTemplate jdbcTemplate;

	public MysqlTermDao(JdbcTemplate jdbcTemplate) {
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
	public List<Term> getAll() {
		// TODO spraviť unit test 
		// POZOR na to v terme -- sikme apostrofy treba!!!!!!!!!
		String sql = "SELECT id, since, `to` FROM term;"; // sql skript
		List<Term> terms = jdbcTemplate.query(sql, new RowMapper<Term> (){

			public Term mapRow(ResultSet rs, int rowNum) throws SQLException {
				Long id = rs.getLong("id");
				Integer since = rs.getInt("since");
				Integer to = rs.getInt("to");
				return new Term(id, since, to);
			}
			
		});
		return terms;
		
	}

	@Override
	public Term getById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

}
