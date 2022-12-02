package sk.upjs.ics.votuj.storage;

public interface PartyDao {

	Party save(Party party);

	boolean delete(Long id);
}
