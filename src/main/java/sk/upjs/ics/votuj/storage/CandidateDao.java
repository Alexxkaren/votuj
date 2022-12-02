package sk.upjs.ics.votuj.storage;

import java.util.List;

public interface CandidateDao {

	List<Candidate> getByParty(Party party);

	Candidate save(Candidate candidate);

	List<Candidate> getByTermParty(Party party, Term term);

}
