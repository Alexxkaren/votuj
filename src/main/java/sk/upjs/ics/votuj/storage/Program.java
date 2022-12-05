package sk.upjs.ics.votuj.storage;

public class Program {

	private Long id;
	private String name;
	private Party party;
	private boolean is_active;
	private Term term;
	
	public Program() {
	}

	public Program(Long id, String name, Party party, boolean is_active, Term term) {
		super();
		this.id = id;
		this.name = name;
		this.party = party;
		this.is_active = is_active;
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
		return is_active;
	}

	public void setIs_active(boolean is_active) {
		this.is_active = is_active;
	}

	public Term getTerm() {
		return term;
	}

	public void setTerm(Term term) {
		this.term = term;
	}
	
}

	