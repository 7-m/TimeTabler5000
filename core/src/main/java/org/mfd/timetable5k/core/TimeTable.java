package org.mfd.timetable5k.core;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

/**
 A timetable repeats after a pre-defined <b>interval</b>.Each timetable is composed of several <b>days</b>. Each day has a <b>schedule</b>
 which may or may not be unique. A schedule is composed of <b>allocated periods</b> and <b>unallocated periods
 </b> which can be ambiguosly referred to as a <b><period/b>. <b>Unallocated  period </b> is an empty day time interval to which a
 combination of teacher and subject can be allocated which then makes it an <b>allocated period</b>. A  period can be referred to using the
 notation <b>period(day_no, period_no )</b> where numbering starts from 0. The function returns the period on the specified <b>day</b> at the
 specified <b>period</b>.
 */
public class TimeTable {
	/* the class will format the timetables*/
	ClassTimeTable[] classTimeTables;
	Classroom[]      classrooms;
	int              ndays;
	int              nPeriodsAday;
	Period[][]       timetable;
	Map<Teacher, Map<Integer, Teacher.AllotedPeriod>> teacherAllotedPeriodMap;

	/**
	 @param timetable the time table of all classes represented as a 2D array where the ith column represents the ith classes timetable in a
	 flatenned form i.e all the periods sequentially.
	 */
	public TimeTable(int ndays,
			int nPeriodsAday,
			Period[][] timetable,
			Classroom[] classroom,
			Map<Teacher, Map<Integer, Teacher.AllotedPeriod>> teacherAllotedPeriodMap) {
		this.ndays = ndays;
		this.nPeriodsAday = nPeriodsAday;
		this.timetable = timetable;
		this.classrooms = classroom;
		this.teacherAllotedPeriodMap = teacherAllotedPeriodMap;

		makeTimeTable();
	}

	public Map<Teacher, Map<Integer, Teacher.AllotedPeriod>> getTeacherAllotedPeriodMap() {
		return Collections.unmodifiableMap(teacherAllotedPeriodMap);
	}

	/**
	 Returns the array that was used to create this timetable
	 */
	public Period[][] get2DTimetable() {
		/* return a copy of the original*/
		return timetable;//Arrays.stream(timetable).map(arr -> Arrays.copyOf(arr, arr.length)).toArray(Period[][]::new);
	}


	public int getnPeriodsAday() {
		return nPeriodsAday;
	}

	public int getNdays() {
		return ndays;
	}


	public ClassTimeTable getTimeTableFor(int i) {
		return classTimeTables[i];
	}

	private void makeTimeTable() {
		classTimeTables = new ClassTimeTable[classrooms.length];

		for (int i = 0; i < classrooms.length; i++) {

			Period[][] classtt = new Period[ndays][nPeriodsAday];
			int dayno = 0;
			Period[] periodsforday = new Period[nPeriodsAday];

			for (int j = 0; j < ndays * nPeriodsAday; j++) { // iterate down the array
				// skip reinitializing for the first iteration and begin only from the next one
				if (j != 0 && j % nPeriodsAday == 0) {
					classtt[dayno++] = periodsforday;
					periodsforday = new Period[nPeriodsAday];
				}
				periodsforday[j % nPeriodsAday] = timetable[j][i];
			}
			classtt[dayno] =
					periodsforday; // for the last day as the loop exits before it gets a chance to put the periods in the days list.

			classTimeTables[i] = new ClassTimeTable(classrooms[i], classtt);

		}


	}

	/**
	 Reperesents a single classes timetable
	 */
	public static class ClassTimeTable {
		Period[][] classtt;
		Classroom  classroom;

		ClassTimeTable(Classroom classroom, Period[][] classtt) {
			this.classroom = classroom;
			this.classtt = classtt;
		}

		public Period[] allperiods() {
			return Arrays.stream(classtt).flatMap(Arrays::stream).toArray(Period[]::new);
		}

		public Period[] periodsForDay(int dayNo) {
			return Arrays.copyOf(classtt[dayNo], classtt[dayNo].length);
		}

		public Period period(int dayNo, int periodNo) {
			return classtt[dayNo][periodNo];
		}


	}

}
