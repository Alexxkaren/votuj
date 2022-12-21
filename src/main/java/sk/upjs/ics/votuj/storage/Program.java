package sk.upjs.ics.votuj.storage;

import java.util.List;
import java.util.Objects;

public class Program {

	private Long id;
	private String name;
	private Party party;
	private boolean isActive;
	private Term term;
	private List<Item> items;

	public Program() {
	}

	public Program(Long id, String name, Party party, boolean is_active, Term term) {
		this.id = id;
		this.name = name;
		this.party = party;
		this.isActive = is_active;
		this.term = term;
	}

	public Program(Long id, String name, Party party, boolean is_active, Term term, List<Item> items) {
		this.id = id;
		this.name = name;
		this.party = party;
		this.isActive = is_active;
		this.term = term;
		this.items = items;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Party getParty() {
		return party;
	}

	public void setParty(Party party) {
		this.party = party;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setIsActive(boolean is_active) {
		this.isActive = is_active;
	}

	public Term getTerm() {
		return term;
	}

	public void setTerm(Term term) {
		this.term = term;
	}

	public List<Item> getItems() {
		return items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}

	@Override
	public String toString() {
		return name + "("+ term.toString() +")";
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, items, name, party, term);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Program other = (Program) obj;
		return Objects.equals(id, other.id) && Objects.equals(items, other.items) && Objects.equals(name, other.name)
				&& Objects.equals(party, other.party) && Objects.equals(term, other.term);
	}

}
