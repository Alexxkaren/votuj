package sk.upjs.ics.votuj.storage;

import java.util.List;

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
		super();
		this.id = id;
		this.name = name;
		this.party = party;
		this.isActive = is_active;
		this.term = term;
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

	public boolean isIs_active() {
		return isActive;
	}

	public void setIs_active(boolean is_active) {
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
		return  name + "( party=" + party + " term ="+ term +")";
	}
	
	
	
}

	