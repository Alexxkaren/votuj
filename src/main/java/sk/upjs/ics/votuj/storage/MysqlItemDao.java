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

public class MysqlItemDao implements ItemDao {

	private JdbcTemplate jdbcTemplate;

	public MysqlItemDao(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public Item save(Item item, List<Category> categoriess) throws NoSuchElementException, NullPointerException {
		if (item == null) {
			throw new NullPointerException("Cannot save null");
		}
		if (item.getName() == null) {
			throw new NullPointerException("Item name cannot be null");
		}
		if (item.getInfo() == null) {
			throw new NullPointerException("Item info cannot be null");
		}
		if (item.getProgram() == null) {
			throw new NullPointerException("Item program cannot be null");
		}
		// insert
		if (item.getId() == null) {
			SimpleJdbcInsert saveInsert = new SimpleJdbcInsert(jdbcTemplate);
			saveInsert.withTableName("item");
			saveInsert.usingColumns("name", "info", "id_program");
			saveInsert.usingGeneratedKeyColumns("id");
			Map<String, Object> values = new HashMap<>();
			values.put("name", item.getName());
			values.put("info", item.getInfo());
			values.put("id_program", item.getProgram().getId());
			long id = saveInsert.executeAndReturnKey(values).longValue();
			Item item2 = new Item(id, item.getName(), item.getInfo(), item.getProgram(), categoriess);
			saveCategories(item2);
			return item2;
			// update
		} else {
			for (Category c : categoriess) {
				if (!item.getCategories().contains(c)) {
					item.getCategories().add(c);
				}
			}

			String sql = "UPDATE item SET name= ?, info= ?, id_program = ? " + " WHERE id = ? ";
			int updated = jdbcTemplate.update(sql, item.getName(), item.getInfo(), item.getProgram().getId(),
					item.getId());
			if (updated == 1) {
				String sqlDelete = "DELETE from item_has_category " + "WHERE id_item= " + item.getId();
				jdbcTemplate.update(sqlDelete);
				saveCategories(item);
				return item;
			} else {
				throw new NoSuchElementException("item with id: " + item.getId() + " not in DB.");
			}
		}
	}

	private void saveCategories(Item item) {
		StringBuilder sb = new StringBuilder();
		sb.append("INSERT INTO item_has_category (id_item, id_category) VALUES ");
		for (Category category : item.getCategories()) {
			if (category == null || category.getId() == null) {
				throw new NullPointerException("Item has category that has null id or is null");
			}
			sb.append("(").append(item.getId());
			sb.append(",").append(category.getId());
			sb.append("),");
		}
		String sql = sb.substring(0, sb.length() - 1);
		jdbcTemplate.update(sql);

	}

	@Override
	public boolean delete(Long id) throws ObjectUndeletableException {
		int delete;
		try {
			jdbcTemplate.update("DELETE FROM item_has_category WHERE  id_item= " + id);
			delete = jdbcTemplate.update("DELETE FROM item WHERE  id= " + id);
		} catch (DataIntegrityViolationException e) {
			throw new ObjectUndeletableException("Item can not be deleted");
		}
		return delete == 1;
	}

	@Override
	public List<Item> getByProgram(Program program) {
		String sql = "SELECT id, name, info, id_program FROM item WHERE id_program = " + program.getId();
		List<Item> list = jdbcTemplate.query(sql, new ItemRowMapper());
		return list;
	}

	
	@Override
	public List<Item> getByTerm(Term term) {
		String sql = "SELECT item.id, item.name, item.info, item.id_program FROM item "
				+ "JOIN program ON program.id = item.id_program " + "WHERE program.id_term = " + term.getId();
		List<Item> list = jdbcTemplate.query(sql, new ItemRowMapper());
		return list;

	}

	@Override
	public List<Item> getByProgramCategory(Program program, Category category) {
		String sql = "SELECT id, name, info, id_program FROM item "
				+ "JOIN item_has_category ihc ON ihc.id_item = item.id " + "WHERE id_program = " + program.getId()
				+ " AND ihc.id_category = " + category.getId();
		List<Item> list = jdbcTemplate.query(sql, new ItemRowMapper());
		return list;

	}


	@Override
	public List<Item> getByTermPartyCategory(Term term, Party party, Category category) {
		String sql = "SELECT item.id, item.name, item.info, item.id_program FROM item "
				+ "JOIN item_has_category ihc ON ihc.id_item = item.id "
				+ "JOIN program ON program.id = item.id_program " + "JOIN term ON term.id = program.id_term "
				+ "JOIN party ON party.id = program.id_party " + "WHERE program.id_term = " + term.getId()
				+ " AND party.id = " + party.getId() + " AND ihc.id_category = " + category.getId();
		List<Item> list = jdbcTemplate.query(sql, new ItemRowMapper());
		return list;
	}

	@Override
	public List<Item> getByTermParty(Term term, Party party) {
		String sql = "SELECT item.id, item.name, item.info, item.id_program FROM item "
				+ "JOIN program ON program.id = item.id_program " + "JOIN term ON term.id = program.id_term "
				+ "JOIN party ON party.id = program.id_party " + "WHERE program.id_term = " + term.getId()
				+ " AND party.id = " + party.getId();
		List<Item> list = jdbcTemplate.query(sql, new ItemRowMapper());
		return list;
		

	}

	@Override
	public Item getById(Long id) {
		String sql = "SELECT id, name, info, id_program FROM item WHERE id = " + id;
		return jdbcTemplate.queryForObject(sql, new ItemRowMapper());

	}

	@Override
	public List<Item> getAll() {
		String sql = "SELECT id, name, info, id_program FROM item ";
		List<Item> list = jdbcTemplate.query(sql, new ItemRowMapper());
		return list;
	}

	private class ItemRowMapper implements RowMapper<Item> {

		@Override
		public Item mapRow(ResultSet rs, int rowNum) throws SQLException {
			Item item = new Item();
			item.setId(rs.getLong("id"));
			item.setName(rs.getString("name"));
			item.setInfo(rs.getString("info"));
			Program program = DaoFactory.INSTANCE.getProgramDao().getById(rs.getLong("id_program"));
			item.setProgram(program);
			List<Category> categories = DaoFactory.INSTANCE.getCategoryDao().getByItem(item);
			item.setCategories(categories);
			return item;

		}

	}

}
