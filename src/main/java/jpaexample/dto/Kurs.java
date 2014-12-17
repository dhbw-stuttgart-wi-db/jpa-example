package jpaexample.dto;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Kurs {
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	private int id;

	private String description;

	@OneToMany(mappedBy = "kurs")
	private final List<Student> members = new ArrayList<Student>();

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<Student> getMembers() {
		return members;
	}

}