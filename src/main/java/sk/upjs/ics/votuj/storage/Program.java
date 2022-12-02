package sk.upjs.ics.votuj.storage;

public class Program {

	private Long id;
	private String name;
	private Integer termFrom;
	private Integer termTo;
	private Party party;

	public Program() {
	}

	public Program(Long id, String name, Integer termFrom, Integer termTo, Party party) {
		super();
		this.id = id;
		this.name = name;
		this.termFrom = termFrom;
		this.termTo = termTo;
		this.party = party;
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

	public Integer getTermFrom() {
		return termFrom;
	}

	public void setTermFrom(Integer termFrom) {
		this.termFrom = termFrom;
	}

	public Integer getTermTo() {
		return termTo;
	}

	public void setTermTo(Integer termTo) {
		this.termTo = termTo;
	}

	public Party getParty() {
		return party;
	}

	public void setParty(Party party) {
		this.party = party;
	}
}
