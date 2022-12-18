package sk.upjs.ics.votuj.storage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

public class MysqlCandidateDao implements CandidateDao {

	private JdbcTemplate jdbcTemplate;

	public MysqlCandidateDao(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public Candidate save(Candidate candidate, List<Term> termss) throws NoSuchElementException, NullPointerException {
		if (candidate == null) {
			throw new NullPointerException("Cannot save null");
		}
		if (candidate.getName() == null) {
			throw new NullPointerException("Candidate name cannot be null");
		}
		if (candidate.getSurname() == null) {
			throw new NullPointerException("Candidate surname cannot be null");
		}
		if (candidate.getCandidateNumber() == null) {
			throw new NullPointerException("Candidate number cannot be null");
		}
		if (candidate.getInfo() == null) {
			throw new NullPointerException("Candidate info cannot be null");
		}
		if (candidate.getParty() == null) {
			throw new NullPointerException("Candidate party cannot be null");
		}
		// insert
		if (candidate.getId() == null) {
			//List<Term> tt = new ArrayList<>();
			//tt.add(term);
			SimpleJdbcInsert saveInsert = new SimpleJdbcInsert(jdbcTemplate);
			saveInsert.withTableName("candidate");
			saveInsert.usingColumns("name", "surname", "candidate_number", "info", "id_party");
			saveInsert.usingGeneratedKeyColumns("id");
			Map<String, Object> values = new HashMap<>();
			values.put("name", candidate.getName());
			values.put("surname", candidate.getSurname());
			values.put("candidate_number", candidate.getCandidateNumber());
			values.put("info", candidate.getInfo());
			values.put("id_party", candidate.getParty().getId());
			long id = saveInsert.executeAndReturnKey(values).longValue();
			Candidate candidate2 =  new Candidate(id, candidate.getName(), candidate.getSurname(), candidate.getCandidateNumber(),
					candidate.getInfo(), candidate.getParty(), termss);
			//TU DAKDE POTREBUJEM ABY TEN CANDIDATE MAL LIST TERMOV KED JE NOVY!!!!!
			saveTerms(candidate2); //druhy naviazany save
			return candidate2;
			// update
		} else {
			System.out.println("UPDATED CANDIDATE HAS THIS TERMS:");
			System.out.println(candidate.getTerms().toString());
			System.out.println("I ADD CURRENT");
			//System.out.println(candidate.getTerms().addAll(termss));
			for (Term t : termss) {
				if (!candidate.getTerms().contains(t)) {
					candidate.getTerms().add(t);
				}
			}
			System.out.println("RESULT:");
			System.out.println(candidate.getTerms().toString());
			String sql = "UPDATE candidate SET name= ?, surname= ?, candidate_number= ?, info= ?, id_party= ? "
					+ "WHERE id = ? ";
			int updated = jdbcTemplate.update(sql, candidate.getName(), candidate.getSurname(),
					candidate.getCandidateNumber(), candidate.getInfo(), candidate.getParty().getId(),
					candidate.getId());
			if (updated == 1) {
				String sqlDelete = "DELETE from candidate_has_term " 
						+ "WHERE id_candidate= " + candidate.getId();
				jdbcTemplate.update(sqlDelete);
				saveTerms(candidate);
				System.out.println(candidate.getTerms().toString());
				return candidate;
			} else {
				throw new NoSuchElementException("candidate with id: " + candidate.getId() + " not in DB.");
			}
		}
	}

	private void saveTerms(Candidate candidate) {
		//vieme ze termov nebude viac ako 1000
		//my nemozeme mat kandidata bez termu 
		/*
		if (candidate.getTerms().isEmpty()) {
			//throw new NullPointerException("Candidate doesn´t have any term");
			return;
		}*/
		
		StringBuilder sb = new StringBuilder();
		sb.append("INSERT INTO candidate_has_term (id_candidate, id_term) VALUES ");
		for (Term term : candidate.getTerms()) {
			if (term==null || term.getId()==null) {
				throw new NullPointerException("Candidate has term that is null id of term or is null");
			}
			sb.append("(").append(candidate.getId());
			sb.append(",").append(term.getId());
			sb.append("),");
		}
		System.out.println(sb.toString());
		String sql = sb.substring(0,sb.length()-1);
		System.out.println(sql);
		jdbcTemplate.update(sql);
	}

	@Override
	public boolean delete(Long id) throws ObjectUndeletableException {
		int delete;
		try {
			jdbcTemplate.update("DELETE FROM candidate_has_term WHERE  id_candidate= " + id);
			delete = jdbcTemplate.update("DELETE FROM candidate WHERE  id= " + id);
		} catch (DataIntegrityViolationException e) {
			throw new ObjectUndeletableException("Some party has this candidate.Candidate can not be deleted");
		}
		return delete == 1;
	}

	@Override
	public List<Candidate> getByTermParty(Party party, Term term) {
		String sql = "SELECT id, name, surname, candidate_number, info, id_party FROM candidate "
				+ "LEFT JOIN candidate_has_term ON candidate_has_term.id_candidate = candidate.id "
				+ "WHERE id_party = " + party.getId() + " AND candidate_has_term.id_term = " + term.getId();
		return jdbcTemplate.query(sql, new CandidateRowMapper());
		// TODO unit test !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	}

	@Override
	public Candidate getById(Long id) {
		String sql = "SELECT id, name, surname, candidate_number, info, id_party FROM candidate " + "WHERE id = " + id;
		return jdbcTemplate.queryForObject(sql, new CandidateRowMapper());
		// TODO unit test !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

	}
	
	

	private class CandidateRowMapper implements RowMapper<Candidate> {

		@Override
		public Candidate mapRow(ResultSet rs, int rowNum) throws SQLException {
			Candidate candidate = new Candidate();
			candidate.setId(rs.getLong("id"));
			candidate.setName(rs.getString("name"));
			candidate.setSurname(rs.getString("surname"));
			candidate.setCandidateNumber(rs.getString("candidate_number"));
			candidate.setInfo(rs.getString("info"));
			Party party = DaoFactory.INSTANCE.getPartyDao().getById(rs.getLong("id_party"));
			candidate.setParty(party);

			return candidate;
		};

	};

}
