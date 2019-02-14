package org.mfd.timetable5k.core;

import java.io.Serializable;
import java.util.*;

public class Classroom
		implements Serializable {
	private static final long   serialVersionUID = -9058882971992532447L;
	private              String name;

	private Map<Subject, Integer> hoursTaught = new HashMap<>();

	// tried switching it out the List<Teacher> for a Set whilst preserving the interface, tests ran 1 and half sec longer, oof.
	private Map<Subject, List<Teacher>> subjectTeachers = new HashMap<>();


	public Classroom(String name,
			List<Subject> subjectsToTeach) {

		this.name = name;
		// init every subject with an empty teachers list
		subjectsToTeach.forEach(subject -> subjectTeachers.putIfAbsent(subject, new ArrayList<>()));


		for (Subject s : subjectsToTeach)
			hoursTaught.put(s, 0);


	}

	public void addSubjectToTeach(Subject subject) {
		subjectTeachers.putIfAbsent(subject, new ArrayList<>());
		hoursTaught.putIfAbsent(subject, 0);
	}

	public List<Subject> getSubjectsToTeach() {
		return Collections.unmodifiableList(new ArrayList<>(subjectTeachers.keySet()));
	}

	public boolean requiresHoursFor(Subject s) {
		subjectExistenceCheck(s);
		return s.getRequiredHours() - hoursTaught.get(s) != 0;

	}

	private void subjectExistenceCheck(Subject s) {
		if (!hoursTaught.containsKey(s))
			throw new NoSuchElementException("Subject " + s + " not in opted by class");

	}

	public void incHoursFor(Subject s) {
		subjectExistenceCheck(s);

		int hours = hoursTaught.get(s);

		if (hours == s.getRequiredHours())
			throw new IllegalStateException("Subject required hours saturated for " + s);
		hoursTaught.put(s, ++hours);

	}

	public void decHoursFor(Subject s) {
		subjectExistenceCheck(s);

		int hours = hoursTaught.get(s);

		if (hours == 0)
			throw new IllegalStateException("Subject required hours undersaturated for " + s);

		hoursTaught.put(s, --hours);


	}

	/**
	 Checks if atleast 1 teacher is assigned to the subject.
	 */
	public boolean hasTeacherFor(Subject s) {
		subjectExistenceCheck(s);
		return subjectTeachers.get(s).size() > 0;

	}

	public void assignTeacher(Subject subject,
			Teacher... teachers) {
		addSubjectToTeach(subject);

		Arrays.stream(teachers).forEach(t -> {
			if (!subjectTeachers.get(subject).contains(t))
				subjectTeachers.get(subject).add(t);
		});


		for (Teacher teacher : teachers)
			teacher.incWorkBy(subject.getRequiredHours());
	}

	public List<Teacher> getTeacher(Subject subject) {
		subjectExistenceCheck(subject);
		return subjectTeachers.get(subject);
	}

	/**
	 removes all teachers for {@code subject} for this class.
	 */
	public void removeSubjectTeacher(Subject subject) {
		subjectExistenceCheck(subject);

		List<Teacher> teachers = subjectTeachers.get(subject);


		for (Teacher t : teachers) t.decWorkBy(subject.getRequiredHours());

		teachers.clear();

	}

	/*public List<Teacher> getTeachers() {
		return Collections.unmodifiableList(new ArrayList<>(subjectTeachers.values()));

	}*/

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "[name: " + getName() + ", subject=teachers: " + getSubjectsToTeach() + "]";
	}

	public String getName() {
		return name;
	}

	public void removeSubjectToTeach(Subject toremove) {
		subjectExistenceCheck(toremove);
		subjectTeachers.remove(toremove);
		hoursTaught.remove(toremove);
	}
}
