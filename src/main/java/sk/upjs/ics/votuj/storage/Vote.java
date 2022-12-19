package sk.upjs.ics.votuj.storage;

import java.time.LocalDateTime;

public class Vote {

	private Long id;
	private Integer age;
	private boolean male;
	private LocalDateTime date;
	private Long id_region;
	private Party party;

	public Vote() {
	}

	public Vote(Long id, Integer age, boolean male, LocalDateTime date, Long region, Party party) {
		super();
		this.id = id;
		this.age = age;
		this.male = male;
		this.date = date;
		this.id_region = region;
		this.party = party;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public boolean isMale() {
		return male;
	}

	public void setMale(boolean male) {
		this.male = male;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	public Long getRegion() {
		return id_region;
	}

	public void setRegion(Long region) {
		this.id_region = region;
	}

	public Party getParty() {
		return party;
	}

	public void setParty(Party party) {
		this.party = party;
	}

}
