package sk.upjs.ics.votuj.storage;

import java.util.List;
import java.util.NoSuchElementException;

public interface TermDao {

	Term save(Term term) throws NullPointerException, NoSuchElementException; // NAIMPLEMENTOVANE

	boolean delete(Long id) throws ObjectUndeletableException ; // NAIMPLEMENTOVANE

	Term getById(Long id); //TODO podla unit testu throws

	List<Term> getAll(); //TODO podla unit testu throws
	
	List<Term> getByCandidate(Candidate candidate);

}
