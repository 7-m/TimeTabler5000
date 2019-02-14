package org.mfd.timetable5k.core;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;


public abstract class TimeTabler {


	List<Constraint> constraints;
	private Classroom[] classrooms;
	private Teacher[]   teachers;
	private int         nPeriodADay; //no. of periods in a DAY
	private int         nDays;


	public TimeTabler(int nPeriodADay,
			int nDays,
			Classroom[] classrooms,
			Map<Teacher, Map<Integer, Teacher.AllotedPeriod>> teacherAllotedMap,
			Constraint... constraints
	) {

		this.nPeriodADay = nPeriodADay;
		this.classrooms = classrooms;
		this.nDays = nDays;
		this.constraints = Arrays.asList(constraints);

		/* follow the Constraint contract */
		this.constraints.forEach(c -> c.initialize(classrooms, nPeriodADay, nDays, teacherAllotedMap));

		/* consolidate all teachers*/
		teachers = Arrays.stream(getClassrooms()).flatMap(c -> c.getSubjectsToTeach().stream()).flatMap(s -> s.getTeachers().stream())
						 .distinct()
						 .toArray(Teacher[]::new);

		/* update teacher timetables if aplicable*/

		if (teacherAllotedMap != null)
			Arrays.stream(teachers).forEach(t -> {
				if (teacherAllotedMap.containsKey(t)) {
					t.setAllocatedPeriodsMap(teacherAllotedMap.get(t));
				}
			});


	}


	abstract public void stopTimeTabling();

	/**
	 Retrieves the next possible timetable, each element in the list presents
	 */
	abstract public TimeTable nextTimeTable();

	public Classroom[] getClassrooms() {
		return classrooms;
	}

	/**
	 Returns teachers associated with the given classrooms
	 */
	public Teacher[] getTeachers() {
		return teachers;
	}

	public int getnPeriodADay() {
		return nPeriodADay;
	}

	public int getnPeriods() {
		return nPeriodADay * nDays;
	}

	public int getnClass() {
		return classrooms.length;
	}

	public int getnDays() {
		return nDays;
	}

	public Classroom getClassroom(int i) {
		return classrooms[i];
	}

	public List<Constraint> getConstraints() {
		return Collections.unmodifiableList(constraints);
	}

	protected abstract Period[][] nextAllClassTTAs2DArray();

	protected abstract Period getPeriodAt(int i,
			int j);

	protected abstract void assignPeriod(Period p,
			int i,
			int j);
}