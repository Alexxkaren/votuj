package sk.upjs.ics.votuj.storage;

import java.util.List;

public interface CandidateDao {

	Candidate save(Candidate candidate);
	
	boolean delete(Long id);

	List<Candidate> getByTermParty(Party party, Term term);
	
	Candidate getById(Long id);

}
