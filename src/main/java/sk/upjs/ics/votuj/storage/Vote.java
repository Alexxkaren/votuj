package sk.upjs.ics.votuj.storage;

import java.time.LocalDateTime;

public class Vote {

	private Long id;
	//private Integer age;
	private String age;////////////////////////
	private Boolean male;
	private LocalDateTime date;
	private Region region;
	private Party party;

	public Vote() {
	}
	
	public Vote(Vote vote, Party party) {
		this.id = vote.getId();
		this.age = vote.getAge();
		this.male = vote.getMale();
		this.date = vote.getDate();
		this.region = vote.getRegion();
		this.party = party;
	}

	public Vote(Long id, String age, Boolean male, LocalDateTime date, Region region, Party party) {
		this.id = id;
		this.age = age;
		this.male = male;
		this.date = date;
		this.region = region;
		this.party = party;
	}
	public Vote(Long id, String age, Boolean male, LocalDateTime date, Region region) {
		this.id = id;
		this.age = age;
		this.male = male;
		this.date = date;
		this.region = region;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public Boolean getMale() {
		return male;
	}

	public void setMale(Boolean male) {
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

	@Override
	public String toString() {
		return "[id=" + id + ", age=" + age + ", male=" + male + ", date=" + date + ", region=" + region
				+ ", party=" + party + "]";
	}
	
	

}
