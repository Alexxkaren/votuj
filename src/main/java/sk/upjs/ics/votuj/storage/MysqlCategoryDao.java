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

public class MysqlCategoryDao implements CategoryDao {

	private JdbcTemplate jdbcTemplate;

	public MysqlCategoryDao(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public Category save(Category category) throws NoSuchElementException, NullPointerException {
		if (category == null) {
			throw new NullPointerException("Cannot save null");
		}
		if (category.getName() == null) {
			throw new NullPointerException("Category name cannot be null");
		}
		// insert
		if (category.getId() == null) {
			SimpleJdbcInsert saveInsert = new SimpleJdbcInsert(jdbcTemplate);
			saveInsert.withTableName("category");
			saveInsert.usingColumns("name");
			saveInsert.usingGeneratedKeyColumns("id");
			Map<String, Object> values = new HashMap<>();
			values.put("name", category.getName());
			long id = saveInsert.executeAndReturnKey(values).longValue();
			return new Category(id, category.getName());
			// update
		} else {
			String sql = "UPDATE category SET name= ? " + "WHERE id = ? ";
			int updated = jdbcTemplate.update(sql, category.getName(), category.getId());
			if (updated == 1) {
				return category;
			} else {
				throw new NoSuchElementException("category with id: " + category.getId() + " not in DB.");
			}
		}

	}

	@Override
	public boolean delete(Long id) throws ObjectUndeletableException {
		int delete;
		try {
			delete = jdbcTemplate.update("DELETE FROM category WHERE  id= " + id);
		} catch (DataIntegrityViolationException e) {
			throw new ObjectUndeletableException("Some item has this category.Category can not be deleted");
		}
		return delete == 1;
	}

	@Override
	public List<Category> getByItem(Item item) {
		String sql = "SELECT id, name FROM category " + "JOIN item_has_category ihc ON ihc.id_category = category.id "
				+ "WHERE ihc.id_item = " + item.getId();
		List<Category> list = jdbcTemplate.query(sql, new CategoryRowMapper());
		return list;
	}

	@Override
	public Category getById(Long id) {
		String sql = "SELECT id, name FROM category WHERE id = " + id;
		return jdbcTemplate.queryForObject(sql, new CategoryRowMapper());

	}

	@Override
	public List<Category> getAll() {
		String sql = "SELECT id, name FROM category";
		List<Category> list = jdbcTemplate.query(sql, new CategoryRowMapper());
		return list;

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
