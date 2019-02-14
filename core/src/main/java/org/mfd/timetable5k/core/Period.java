package org.mfd.timetable5k.core;

import java.io.Serializable;
import java.util.List;

public class Period
		implements Serializable {
	private static final long serialVersionUID = -6849794470754667710L;
	private List<Teacher> teachers;
	private Subject       subject;

	public Period(List<Teacher> teachers, Subject subject) {
		this.teachers = teachers;
		this.subject = subject;
	}

	public List<Teacher> getTeachers() {
		return teachers;
	}

	public Subject getSubject() {
		return subject;
	}

	@Override
	public String toString() {
		return "[Teacher: " + teachers + ", Subject: " + subject + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Period) {
			return this.teachers.equals(((Period) obj).teachers) && this.subject.equals(((Period) obj).subject);
		}
		return false;
	}
}
