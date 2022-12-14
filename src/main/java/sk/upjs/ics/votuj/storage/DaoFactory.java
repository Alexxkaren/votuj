package sk.upjs.ics.votuj.storage;

import org.springframework.jdbc.core.JdbcTemplate;

import com.mysql.cj.jdbc.MysqlDataSource;

public enum DaoFactory {

	INSTANCE;

	private CandidateDao candidateDao;
	private CategoryDao categoryDao;
	private ItemDao itemDao;
	private PartyDao partyDao;
	private ProgramDao programDao;
	private TermDao termDao;
	private VoteDao voteDao;
	private AdminDao adminDao;
	private RegionDao regionDao;
	private boolean testing = false;
	private JdbcTemplate jdbcTemplate;

	public void setTesting() {
		this.testing = true;
	}

	public AdminDao getAdminDao() {
		if (adminDao == null) {
			adminDao = new MysqlAdminDao(getJdbcTemplate());
		}
		return adminDao;
	}

	public CandidateDao getCandidateDao() {
		if (candidateDao == null) {
			candidateDao = new MysqlCandidateDao(getJdbcTemplate());
		}
		return candidateDao;
	}

	public CategoryDao getCategoryDao() {
		if (categoryDao == null) {
			categoryDao = new MysqlCategoryDao(getJdbcTemplate());
		}
		return categoryDao;
	}

	public ItemDao getItemDao() {
		if (itemDao == null) {
			itemDao = new MysqlItemDao(getJdbcTemplate());
		}
		return itemDao;
	}

	public PartyDao getPartyDao() {
		if (partyDao == null) {
			partyDao = new MysqlPartyDao(getJdbcTemplate());
		}
		return partyDao;
	}

	public ProgramDao getProgramDao() {
		if (programDao == null) {
			programDao = new MysqlProgramDao(getJdbcTemplate());
		}
		return programDao;
	}

	public TermDao getTermDao() {
		if (termDao == null) {
			termDao = new MysqlTermDao(getJdbcTemplate());
		}
		return termDao;
	}

	public VoteDao getVoteDao() {
		if (voteDao == null) {
			voteDao = new MysqlVoteDao(getJdbcTemplate());
		}
		return voteDao;
	}

	public RegionDao getRegionDao() {
		if (regionDao == null) {
			regionDao = new MysqlRegionDao(getJdbcTemplate());
		}
		return regionDao;
	}

	private JdbcTemplate getJdbcTemplate() {
		if (jdbcTemplate == null) {
			MysqlDataSource dataSource = new MysqlDataSource();
			if (testing) {
				dataSource.setDatabaseName("votuj_test");
				dataSource.setUser("votuj_user");
				dataSource.setPassword("votuj_user01");

			} else {
				dataSource.setDatabaseName("votuj");
				dataSource.setUser("votuj_user");
				dataSource.setPassword("votuj_user01");
			}
			jdbcTemplate = new JdbcTemplate(dataSource);
		}
		return jdbcTemplate;
	}
}
