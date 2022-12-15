package sk.upjs.ics.votuj.storage;

public class Term {

	private Long id;
	private Integer since;
	private Integer to;

	public Term() {
	}

	public Term(Long id, Integer since, Integer to) {
		super();
		this.id = id;
		this.since = since;
		this.to = to;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getSince() {
		return since;
	}

	public void setSince(Integer since) {
		this.since = since;
	}

	public Integer getTo() {
		return to;
	}

	public void setTo(Integer to) {
		this.to = to;
	}

	@Override
	public String toString() {
		return "(id=" + id + ", " + since + " - " + to + ")";
	}

	
}
