package sk.upjs.ics.votuj.storage;

import java.time.LocalDateTime;

public class Vote {

	private Long id;
	private Integer age;
	private boolean male;
	private LocalDateTime date;
	private Region region;
	private Party party;

	public Vote() {
	}

	public Vote(Long id, Integer age, boolean male, LocalDateTime date, Region region, Party party) {
		super();
		this.id = id;
		this.age = age;
		this.male = male;
		this.date = date;
		this.region = region;
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

	public Region getRegion() {
		return region;
	}

	public void setRegion(Region region) {
		this.region = region;
	}

	public Party getParty() {
		return party;
	}

	public void setParty(Party party) {
		this.party = party;
	}

}
