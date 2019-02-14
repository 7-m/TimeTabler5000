package org.mfd.timetable5k.core;

import org.mfd.timetable5k.core.tablerimplementations.backtracking.TimeTablerBacktrackImpl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface ConstraintCollection {
	/**
	 For checking whether the subject meets the required subject-hours for the class.
	 */
	default Constraint getSubjectMaxHourConstraint() {
		return (i, j, timeTabler, s, t) -> timeTabler.getClassroom(j).requiresHoursFor(s);
	}

	/**
	 For constrainting collisons i.e, a teacher ending up with simultaneous periods. Applies to all teachers.
	 */

	default Constraint getTeacherNoConcurrentPeriodsConstraint() {
		return (i, j, timeTabler, s, t) -> {
			for (int k = 0; k < timeTabler.getnClass(); k++)
				if (timeTabler.getPeriodAt(i, k) != null && k != j && timeTabler.getPeriodAt(i, k).getTeachers().stream()
																				.anyMatch(outer -> t.stream().anyMatch(inner -> inner
																						.equals(outer))))
					return false;
			return true;
		};
	}

	/**
	 For constraining when a lab can begin to avoid lab beginning after school or before break etc as specified by  Classroom.getNoLabBegin .
	 Applies to all subjects with "lab" constraint.
	 */

	default Constraint getSubjectNoLabBeginConstraint(int[] noLabBegin) {

		return (i, j, timeTabler, s, t) -> {
			if (!s.getConstraintModifiers().containsKey("lab"))
				return true;
			// if its a lab then
			for (int n : noLabBegin)
				if (i % timeTabler.getnPeriodADay() == n)
					return false;


			// then check for lab subject specific constraints

			if (s.getConstraintModifiers().containsKey("lab-constraint")){

				Integer[] nolabs=Utils.parseNoLabs(s.getConstraintModifiers().get("lab-constraint"));
				for (int noLab : nolabs)
					if (i == noLab)
						return false;
			}

			return true;
		};
	}

	/**
	 To check whether the teacher has no period constraints for i and can take period at i. Applies to all subjects.
	 */

	default Constraint getTeacherNoAllotConstraint() {
		return (i, j, timeTabler, s, t) -> t.stream().allMatch(teacher -> teacher.canTake(i) && teacher.hasPeriod(i) == null);
	}


	/**
	 For checking whether the teacher has two consecitive periods for lab. Applies to all subjects with "lab" constraint
	 */

	default Constraint getTeacherConsecutiveLabConstraint() {
		return (i, j, timeTabler, s, t) -> {
			if (!s.getConstraintModifiers().containsKey("lab"))
				return true;

			// todo this does not rigorously check the i+1 th period. For example, what if i+1 th period was already llocated to the teacher in
			return getTeacherNoAllotConstraint().satisfies(i + 1, j, timeTabler, s, t) &&
					getTeacherNoConcurrentPeriodsConstraint().satisfies(i + 1, j, timeTabler, s, t);

		};
	}

	/**
	 Constraint for electives. Applies to all subjects with constraint "elective"
	 */

	default Constraint getSubjectConcurrentElectiveConstraint() {
		return (i, j, timeTabler, s, t) -> {
			//check for elective modifiere
			if (!s.getConstraintModifiers().containsKey("elective")) return true;


			// first determine the classes with the same subject
			for (int k = 0; k < timeTabler.getnClass(); k++) {
				//check if the simultaneous period can be alloted
				if (timeTabler.getClassroom(k).getSubjectsToTeach().contains(s) && timeTabler.getPeriodAt(i, k) != null)
					return false;
			}

			//thats all folks!
			return true;

		};
	}

	/**
	 Constraint to prevent multiple classes having the same lab due to limited labrooms. Applies to all subjects with lab
	 */

	default Constraint getSubjectNoConcurrentLabConstraint() {
		return (i, j, timeTabler, s, t) -> {
			if (!s.getConstraintModifiers().containsKey("lab"))
				return true;

			String labRoom = s.getConstraintModifiers().get("lab");
			//check the ith and i+1th row for lab constraint, the "lab" keys value is the lab room no.

			for (int k = 0; k < j; k++) {
				final Period currperiod = timeTabler.getPeriodAt(i, k);
				final Period nextperiod = timeTabler.getPeriodAt(i + 1, k);
				if ((currperiod != null && currperiod.getSubject()
													 .getConstraintModifiers().containsKey("lab") && currperiod.getSubject()
																											   .getConstraintModifiers()
																											   .get("lab")
																											   .equals(labRoom)) ||
						(nextperiod != null && nextperiod.getSubject()
														 .getConstraintModifiers().containsKey("lab") && nextperiod.getSubject()
																												   .getConstraintModifiers()
																												   .get("lab")
																												   .equals(labRoom)))
					return false;
			}
			return true;

		};
	}

	default Constraint getClassSpacingConstraint(int gap,
			int repeatLimit) {
		return new SpacingConstraint(gap, repeatLimit);
	}

	/**
	 The constraint limits the maximum number of first hours a teacher can have in a timetable. Theroy counts as 1 hour so do 2 periods of
	 labs.
	 */
	default Constraint getTeacherMaxFirstHourConstraint(int maxFirstHours) {
		return new Constraint() {
			/* teacherFirstHourCountMap: maps a teacher to the total no. of first hour they have taken in the timetable */
			Map<Teacher, Integer> tfhcm = new HashMap<>();

			@Override
			public boolean satisfies(int i, int j, TimeTablerBacktrackImpl timeTable, Subject s, List<Teacher> t) {
				// test for first hour
				if (i % timeTable.getnPeriodADay() == 0) {
					return t.stream()
							.allMatch(teacher -> tfhcm.get(teacher) == null || tfhcm.get(teacher) < maxFirstHours);
				} else
					return true; // dont bother checking
			}

			@Override
			public void allocate(int i, int j, TimeTablerBacktrackImpl timeTable, Subject s, List<Teacher> t) {
				if (i % timeTable.getnPeriodADay() == 0) {
					t.forEach(teacher -> tfhcm.compute(teacher, (k, v) -> v == null ? 1 : v + 1));

				}

			}

			@Override
			public void deallocate(int i, int j, TimeTablerBacktrackImpl timeTable, Subject s, List<Teacher> t) {
				if (i % timeTable.getnPeriodADay() == 0) {
					t.forEach(teacher -> tfhcm.compute(teacher, (k, v) -> v - 1));

				}
			}
		};
	}


	/**
	 The constraint limits the maximum number of  hours after lunch a teacher can have in a timetable. Theroy counts as 1 hour so do 2
	 periods of labs.
	 */
	default Constraint getTeacherMaxAfterLunchHourConstraint(int afterLunchHours) {
		return new Constraint() {


			/* teacherFirstHourCountMap: maps a teacher to the total no. of first hour they have taken in the timetable */
			Map<Teacher, Integer> tfhcm = new HashMap<>();


			@Override
			public void initialize(Classroom[] classrooms,
					int nPeriodADay,
					int nDays,
					Map<Teacher, Map<Integer, Teacher.AllotedPeriod>> teacherAllotedMap) {
				if (teacherAllotedMap != null)
					teacherAllotedMap.forEach((teacher, teacherAlloted) -> teacherAlloted.forEach((i, allotedPeriod) -> {
						if (i % nPeriodADay == 4 && i % nPeriodADay == 5) {
							tfhcm.compute(teacher, (k, v) -> v == null ? 1 : v + 1);

						}
					}));
			}

			@Override
			public boolean satisfies(int i, int j, TimeTablerBacktrackImpl timeTable, Subject s, List<Teacher> t) {
				// test for first hour
				if (i % timeTable.getnPeriodADay() == 4 && i % timeTable.getnPeriodADay() == 5) {
					return t.stream()
							.allMatch(teacher -> tfhcm.get(teacher) == null || tfhcm.get(teacher) < afterLunchHours);
				} else
					return true; // dont bother checking
			}

			@Override
			public void allocate(int i, int j, TimeTablerBacktrackImpl timeTable, Subject s, List<Teacher> t) {
				if (i % timeTable.getnPeriodADay() == 4 && i % timeTable.getnPeriodADay() == 5) {
					t.forEach(teacher -> tfhcm.compute(teacher, (k, v) -> v == null ? 1 : v + 1));

				}

			}

			@Override
			public void deallocate(int i, int j, TimeTablerBacktrackImpl timeTable, Subject s, List<Teacher> t) {
				if (i % timeTable.getnPeriodADay() == 4 && i % timeTable.getnPeriodADay() == 5) {
					t.forEach(teacher -> tfhcm.compute(teacher, (k, v) -> v - 1));

				}
			}
		};
	}

	/**
	 The constraint limits a teacher to a day of -3 theory -2 theory +1 lab -1 theory +2 lab
	 */
	default Constraint getTeacherTheroyLabLimitConstraint() {
		return new Constraint() {
			/* teahcerDayLabTheoryCountMap: maps a teacher to its count of subject and lab hours every day */
			Map<Teacher, Map<Integer, Integer>> tdltm = new HashMap<>();

			@Override
			public boolean satisfies(int i, int j, TimeTablerBacktrackImpl timeTable, Subject s, List<Teacher> t) {
				int dayno = i / timeTable.getnPeriodADay(); // retrieves the dayno to which the period belongs

				return t.stream()
						.allMatch(teacher -> tdltm.get(teacher) == null
								|| tdltm.get(teacher).get(dayno) == null
								|| tdltm.get(teacher).get(dayno) < 17);
			}

			@Override
			public void allocate(int i, int j, TimeTablerBacktrackImpl timeTable, Subject s, List<Teacher> t) {
				int dayno = i / timeTable.getnPeriodADay(); // retrieves the dayno to which the period belongs
				t.forEach(teacher -> tdltm.compute(teacher, (k, v) -> {
					if (v == null)
						v = new HashMap<>();

					int inc = s.getConstraintModifiers().containsKey("lab") ? 7 : 5;
					v.compute(dayno, (k1, v1) -> v1 == null ? inc : v1 + inc);
					return v;
				}));
			}

			@Override
			public void deallocate(int i, int j, TimeTablerBacktrackImpl timeTable, Subject s, List<Teacher> t) {
				int dayno = i / timeTable.getnPeriodADay(); // retrieves the dayno to which the period belongs

				int inc = s.getConstraintModifiers().containsKey("lab") ? 7 : 5;

				t.forEach(teacher -> tdltm.compute(teacher, (k, v) -> {
					v.compute(dayno, (k1, v1) -> v1 - inc);
					return v;
				}));
			}
		};
	}

	// todo: written only for backtracking timetabler
	default Constraint getTeacherNoContinuousConstraint() {
		return new Constraint() {
			@Override
			public boolean satisfies(int i, int j, TimeTablerBacktrackImpl timeTable, Subject s, List<Teacher> t) {
				/* go ahead if its the first day of the week, the 1 3 5 is coz of breaks, breaks i.e 1 2, 3 4, combinations are okay*/
				return t.stream().allMatch(teacher -> {
					int p = i%timeTable.getnPeriodADay();
					if (p == 1 || p == 3 || p == 5)
						return teacher.hasPeriod(i - 1) == null;
					else
						return true;
				});
			}
		};
	}

	default Constraint getClassMaxAfterLunch(int maxClasses) {
		return new Constraint() {
			Map<Integer, Map<Subject, Integer>> subCount = new HashMap<>();

			@Override
			public boolean satisfies(int i, int j, TimeTablerBacktrackImpl timeTable, Subject s, List<Teacher> t) {

				final int periodNo = i % timeTable.getnPeriodADay();

				if (periodNo == 4 || periodNo == 5)
					return subCount.get(j) == null || subCount.get(j).get(s) == null || subCount.get(j).get(s) < maxClasses;
				else
					return true;
			}

			@Override
			public void allocate(int i, int j, TimeTablerBacktrackImpl timeTable, Subject s, List<Teacher> t) {
				final int periodNo = i % timeTable.getnPeriodADay();
				if (periodNo == 4 || periodNo == 5)
					subCount.compute(j, (classno, subjectCountMap) -> {
						if (subjectCountMap == null)
							subjectCountMap = new HashMap<>();
						subjectCountMap.compute(s, (k, v) -> v == null ? 1 : v + 1);
						return subjectCountMap;
					});
			}

			@Override
			public void deallocate(int i, int j, TimeTablerBacktrackImpl timeTable, Subject s, List<Teacher> t) {
				final int periodNo = i % timeTable.getnPeriodADay();
				if (periodNo == 4 || periodNo == 5)
					subCount.compute(j, (classno, subjectCountMap) -> {
						subjectCountMap.compute(s, (k, v) -> v - 1);
						return subjectCountMap;
					});
			}
		};
	}

	/**
	 Constraint for setting repetitions of a period allowed in a day and if so, the minimum gap that should be between the repeating periods.
	 Applies to all Subjects
	 */
	final class SpacingConstraint
			implements Constraint {
		int gap; // no. of periods between this and the last occurence of the period in the day
		int repeatLimit; // max no. of periods in the day to which i belongs


		SpacingConstraint(int gap,
				int repeatLimit) {
			this.gap = gap;
			this.repeatLimit = repeatLimit;
		}


		@Override
		public boolean satisfies(int i,
				int j,
				TimeTablerBacktrackImpl timeTabler,
				Subject s,
				List<Teacher> t) {

			// we check gap periods up and gap periods down for the same period


			Period mock = new Period(t, s);

			// check above the period
			//determine the first period no. of the day to which i belongs
			int start = (i / timeTabler.getnPeriodADay()) * timeTabler.getnPeriodADay();
			int end = start + timeTabler.getnPeriodADay();


			// check that count is below repeatLimit (i.e adding a subject after this constraint check will reach the repeatLimit )
			int count = 0;
			for (int k = start; k < end; k++) {
				final Period periodAt = timeTabler.getPeriodAt(k, j);
				if (periodAt != null && periodAt.equals(mock))
					count++;
			}

			if (count >= repeatLimit)
				return false;

			// check gap constraint by checking above and below the current i for the day for the same subject

			// check above

			for (int k = i; k >= start; k--) {
				final Period periodAt = timeTabler.getPeriodAt(k, j);
				if (periodAt != null && periodAt.equals(mock) && (i - k) - 1 <= gap)
					return false;
			}

			for (int k = i; k < end; k++) {
				final Period periodAt = timeTabler.getPeriodAt(k, j);
				if (periodAt != null && periodAt.equals(mock) && (k - i) - 1 <= gap)
					return false;
			}

			return true;


		}
	}

	default Constraint getTeacherMondayLunchMeetingConstraint(){
		return new Constraint() {
			@Override
			public boolean satisfies(int i, int j, TimeTablerBacktrackImpl timeTable, Subject s, List<Teacher> t) {
				/* Mondays period 4  (i,e period 5 counting from 1)should be alloted iff 3 is not alloted*/
				if (i == 4)
					return t.stream().allMatch(teacher -> teacher.hasPeriod(3) == null);
				return true;
			}
		};
	}
}

