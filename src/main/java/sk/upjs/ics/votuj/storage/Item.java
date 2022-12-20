package sk.upjs.ics.votuj.storage;

import java.util.List;

public class Item {

	private Long id;
	private String name;
	private String info;
	private Program program;
	// pridavam feature:
	private List<Category> categories;

	public Item() {
	}

	public Item(Long id, String name, String info, Program program, List<Category> cats) {
		super();
		this.id = id;
		this.name = name;
		this.info = info;
		this.program = program;
		this.categories = cats;
	}

	public Item(Long id, String name, String info, Program program) {
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

	// pridane
	public List<Category> getCategories() {
		return categories;
	}

	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}

	@Override
	public String toString() {
		return "Item [id=" + id + ", name=" + name + ", program=" + program + ", categories=" + categories + "]";
	}

}
