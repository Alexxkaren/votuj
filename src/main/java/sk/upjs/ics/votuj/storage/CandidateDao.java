package sk.upjs.ics.votuj.storage;

import java.util.List;
import java.util.NoSuchElementException;

public interface CandidateDao {

	Candidate save(Candidate candidate)throws NoSuchElementException,NullPointerException;
	
	boolean delete(Long id); // NAIMPLEMENTOVANE	

	List<Candidate> getByTermParty(Party party, Term term); //NAIMPLEMENTOVANE
	
	Candidate getById(Long id); //NAIMPLEMENTOVANE

}
