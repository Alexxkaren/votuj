package sk.upjs.ics.votuj.storage;

import java.util.Objects;

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

	@Override
	public int hashCode() {
		return Objects.hash(id, since, to);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Term other = (Term) obj;
		return Objects.equals(id, other.id) && Objects.equals(since, other.since) && Objects.equals(to, other.to);
	}
	
	

	
}
