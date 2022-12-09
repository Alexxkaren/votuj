package sk.upjs.ics.votuj.storage;

public class Candidate {

	private Long id;
	private String name;
	private String surname;
	private Integer candidate_number;
	private String info;
	private Party party;

	public Candidate() {
	}

	public Candidate(Long id, String name, String surname, Integer candidate_number, String info, Party party) {
		super();
		this.id = id;
		this.name = name;
		this.surname = surname;
		this.candidate_number = candidate_number;
		this.info = info;
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

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public Integer getCandidate_number() {
		return candidate_number;
	}

	public void setCandidate_number(Integer candidate_number) {
		this.candidate_number = candidate_number;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public Party getParty() {
		return party;
	}

	public void setParty(Party party) {
		this.party = party;
	}

	@Override
	public String toString() {
		return "(id=" + id + ") " + name + " "+ surname ;
	}

}
