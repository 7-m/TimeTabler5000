package org.mfd.timetable5k.core;

import java.io.Serializable;
import java.util.*;

public class Subject
		implements Serializable {
	private static final long serialVersionUID = -7972684511614226039L;

	private String name;

	/* holds a list of constraints to be met while allocating this subject. An empty
	 * list signifies that the subject is bounded by no constraints and requires
	 * an hour to be allocated at a allocation. Any constraints present should be
	 * honoured by whatever class that allocates the subject. */

	//lowercase modifiers, at the moment its not sanitized.
	private Map<String, String> constraintModifiers = new HashMap<>();
	private int                 requiredHours;
	private List<Teacher>       teachers            = new ArrayList<>();

	public Subject(String name, int requiredHours, AbstractMap.SimpleEntry<String, String>... constraints) {
		this.name = name.toLowerCase().trim();
		this.requiredHours = requiredHours;
		Arrays.stream(constraints)
				.forEach(p -> constraintModifiers.put(p.getKey(), p.getValue() == null ? "" : p.getValue()));


	}

	public String getName() {
		return name;
	}

	public Map<String, String> getConstraintModifiers() {
		return constraintModifiers;
	}

	public int getRequiredHours() {
		return requiredHours;
	}

	public List<Teacher> getTeachers() {
		return Collections.unmodifiableList(teachers);
	}

	public void addTeacher(Teacher... ts) {
		Collections.addAll(teachers, ts);
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}

	/**
	 Returns true if the names of the subjects are same.
	 @param obj
	 @return true if obj is subject and obj.name.equalsIgnoreCase(this.name)
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Subject) {
			Subject s = (Subject) obj;
			return s.name.equalsIgnoreCase(name);
		} else
			return false;
	}

	@Override
	public String toString() {
		return "[name: " + name + ", hoursRequired: " + requiredHours + "]";
	}

	public void removeTeacher(Teacher toremove) {
		teachers.remove(toremove);
	}
}
