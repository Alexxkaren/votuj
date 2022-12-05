package sk.upjs.ics.votuj.storage;

import java.util.List;

public interface ProgramDao {

	Program save(Program program);
	
	boolean delete(Long id);

	List<Program> getByParty(Party party);

	Program getByTermParty(Term term, Party partry);
	
	Program getById(Long id);

}
