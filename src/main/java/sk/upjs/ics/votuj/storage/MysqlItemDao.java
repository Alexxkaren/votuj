package sk.upjs.ics.votuj.storage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;


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
	public boolean delete(Long id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<Item> getByProgram(Program program) {
		String sql = "SELECT id, name, info, id_program FROM item WHERE id_program = " + program.getId();
		List<Item> list = jdbcTemplate.query(sql, new ItemRowMapper());
		return list;
		// TODO unit test !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	}
	
	@Override
	public List<Item> getByTerm(Term term) {
		String sql = "SELECT item.id, item.name, item.info, item.id_program FROM item "
					+ "JOIN program ON program.id = item.id_program "
					+ "WHERE program.id_term = " + term.getId();
		List<Item> list = jdbcTemplate.query(sql, new ItemRowMapper());
		return list;
		// TODO unit test !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	}

	@Override
	public List<Item> getByProgramCategory(Program program, Category category) {
		String sql = "SELECT id, name, info, id_program FROM item "
					+ "JOIN item_has_category ihc ON ihc.id_item = item.id"
					+ "WHERE id_program = " + program.getId() + " AND ihc.id_category = " +category.getId();
		List<Item> list = jdbcTemplate.query(sql, new ItemRowMapper());
		return list;
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
			item.setName(rs.getString("name")); //POZOOOOOR TU MOZE BYT AJ NULLL!!!!!!
			item.setInfo(rs.getString("info"));
			Program program = DaoFactory.INSTANCE.getProgramDao().getById(rs.getLong("id_program"));
			item.setProgram(program);
			return item;
			
		}
		
	}

}
