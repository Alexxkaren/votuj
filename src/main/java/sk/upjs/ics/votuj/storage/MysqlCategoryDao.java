package sk.upjs.ics.votuj.storage;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;


public class MysqlCategoryDao implements CategoryDao {

	private JdbcTemplate jdbcTemplate;

	public MysqlCategoryDao(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public Category save(Category category) {
		return category; 
	}

	@Override
	// dorobit
	public boolean delete(Long id) {
		int delete;
		try {
			delete = jdbcTemplate.update("DELETE FROM category WHERE  id= " + id);
		}catch(DataIntegrityViolationException e) {
			
		}
		return false;
		
	}
	
	
	/*
	 * 
	 * int changed;
		try {
			changed = jdbcTemplate.update("DELETE FROM student WHERE id = " + id);
		} catch (DataIntegrityViolationException e) {
			throw new EntityUndeletableException(
					"Student with id: " + id + "is part of some session, cannot be deleted");
		}

		return changed == 1; 
	 */
	
	@Override
	public List<Category> getByItem(Item item) {
		String sql = "SELECT id, name FROM category " + 
				     "JOIN item_has_category ihc ON ihc.id_category = category.id " +
				     "WHERE ihc.id_item = " + item.getId();
		List<Category> list = jdbcTemplate.query(sql, new CategoryRowMapper());
		return list;
		// TODO unit test !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	}


	@Override
	public Category getById(Long id) {
		String sql = "SELECT id, name FROM category WHERE id = " + id;
		return jdbcTemplate.queryForObject(sql, new CategoryRowMapper());
		// TODO unit test !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

	}

	@Override
	public List<Category> getAll() {
		String sql = "SELECT id, name FROM category";
		List<Category> list = jdbcTemplate.query(sql, new CategoryRowMapper());
		return list;
		// TODO unit test !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	}
	
	private class CategoryRowMapper implements RowMapper<Category> {

		@Override
		public Category mapRow(ResultSet rs, int rowNum) throws SQLException {
			Category category = new Category();
			category.setId(rs.getLong("id"));
			category.setName(rs.getString("name"));
			
			return category;
		}
	}


}
