package sk.upjs.ics.votuj.storage;

public interface AdminDao {

	Admin getByName(String name) throws NullPointerException;
}
