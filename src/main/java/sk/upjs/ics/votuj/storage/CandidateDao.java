package sk.upjs.ics.votuj.storage;

import java.util.List;
import java.util.NoSuchElementException;

public interface CandidateDao {


	boolean delete(Long id) throws ObjectUndeletableException; 	

	List<Candidate> getByTermParty(Party party, Term term); 
	
	Candidate getById(Long id); 
	
	List<Candidate> getAll(); 

	Candidate save(Candidate candidate, List<Term> terms) throws NoSuchElementException, NullPointerException;

	
}
