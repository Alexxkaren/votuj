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
	public Item save(Item item)throws NoSuchElementException,NullPointerException {
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
			saveInsert.usingColumns("name", "info","id_program");
			saveInsert.usingGeneratedKeyColumns("id");
			Map<String, Object> values = new HashMap<>();
			values.put("name", item.getName());
			values.put("info", item.getInfo());
			values.put("id_program", item.getProgram().getId());
			long id = saveInsert.executeAndReturnKey(values).longValue();
			return new Item(id, item.getName(), item.getInfo(), item.getProgram());
			// update
		} else {
			String sql = "UPDATE item SET name= ?, info= ?, id_program = ?" + "WHERE id = ? ";
			int updated = jdbcTemplate.update(sql, item.getName(), item.getInfo(), item.getProgram().getId(), item.getId());
			if (updated == 1) {
				return item;
			} else {
				throw new NoSuchElementException("item with id: " + item.getId() + " not in DB.");
			}
		}
	}

	@Override
	public boolean delete(Long id)throws ObjectUndeletableException {
		int delete;
		try {
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
		// TODO unit test !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	}

	// idk ci toto sa nemoze vymazat --> budeme chciet vsetky body termu bez ohladu
	// na stranu a program?
	// asi nn -> tu ptm nerobit unit test
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
		// TODO unit test !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

	}

	// mozno by sa zisla este taka: --> uvidime ci pouzijeme zatial nerobit unit
	// test
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
		// TODO unit test !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

	}

	@Override
	public Item getById(Long id) {
		String sql = "SELECT id, name, info, id_program FROM item WHERE id = " + id;
		return jdbcTemplate.queryForObject(sql, new ItemRowMapper());
		// TODO unit test !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

	}

	private class ItemRowMapper implements RowMapper<Item> {

		@Override
		public Item mapRow(ResultSet rs, int rowNum) throws SQLException {
			Item item = new Item();
			item.setId(rs.getLong("id"));
			item.setName(rs.getString("name")); // POZOOOOOR TU MOZE BYT AJ NULLL!!!!!!
			item.setInfo(rs.getString("info"));
			Program program = DaoFactory.INSTANCE.getProgramDao().getById(rs.getLong("id_program"));
			item.setProgram(program);
			// skusam
			List<Category> categories = DaoFactory.INSTANCE.getCategoryDao().getByItem(item);
			item.setCategories(categories);
			return item;

		}

	}

}
