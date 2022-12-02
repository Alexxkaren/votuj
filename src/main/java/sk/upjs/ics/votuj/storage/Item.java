package sk.upjs.ics.votuj.storage;

public class Item {

	private Long id;
	private String name;
	private String info;
	private Program program;

	public Item() {
	}

	public Item(Long id, String name, String info, Program program) {
		super();
		this.id = id;
		this.name = name;
		this.info = info;
		this.program = program;
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

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public Program getProgram() {
		return program;
	}

	public void setProgram(Program program) {
		this.program = program;
	}

}
