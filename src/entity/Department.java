package entity;

public class Department {
	private int id;
	private String name;

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Department(int id) {
		this.id = id;
	}
	
}
