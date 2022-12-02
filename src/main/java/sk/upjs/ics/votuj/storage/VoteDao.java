package sk.upjs.ics.votuj.storage;

import java.util.List;

public interface VoteDao {

	Vote save(Vote vote);

	List<Vote> getByParty(Party party);

}
