package sk.upjs.ics.votuj.storage;

import java.util.List;
import java.util.NoSuchElementException;

public interface TermDao {

	Term save(Term term) throws NullPointerException, NoSuchElementException; 

	boolean delete(Long id) throws ObjectUndeletableException;

	Term getById(Long id);

	List<Term> getAll();

	List<Term> getByCandidate(Candidate candidate);

}
