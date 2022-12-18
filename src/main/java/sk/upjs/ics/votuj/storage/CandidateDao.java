package sk.upjs.ics.votuj.storage;

import java.util.List;
import java.util.NoSuchElementException;

public interface CandidateDao {

	//Candidate save(Candidate candidate)throws NoSuchElementException,NullPointerException;
	
	boolean delete(Long id) throws ObjectUndeletableException; // NAIMPLEMENTOVANE	

	List<Candidate> getByTermParty(Party party, Term term); //NAIMPLEMENTOVANE
	
	Candidate getById(Long id); //NAIMPLEMENTOVANE

	Candidate save(Candidate candidate, Term term) throws NoSuchElementException, NullPointerException;

}
