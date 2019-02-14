package org.mfd.timetable5k.core.tablerimplementations.backtracking;

import org.junit.jupiter.api.Test;
import org.mfd.timetable5k.core.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertTrue;


public class TestBackTrackingAllocator {


	@Test
	public void timeTableMethod() throws IOException, ClassNotFoundException {

		// create a mock subbjects
		Subject s0 = new Subject("s0", 3);
		Subject s1 = new Subject("s1", 3);
		Subject s2 = new Subject("s2", 2);
		Subject s3 = new Subject("s3", 2);

		Teacher t0 = new Teacher("t0", Integer.MAX_VALUE, Collections.emptyList());
		Teacher t1 = new Teacher("t1", Integer.MAX_VALUE, Collections.emptyList());
		Teacher t2 = new Teacher("t2", Integer.MAX_VALUE, Collections.emptyList());
		Teacher t3 = new Teacher("t3", Integer.MAX_VALUE, Collections.emptyList());
		Teacher t4 = new Teacher("t4", Integer.MAX_VALUE, Collections.emptyList());

		// c0 -> s0, s1, s2
		// c1 -> s1, s2, s3
		// c2 -> s0, s1, s3

		Classroom c0 = new Classroom("c0", Arrays.asList(s0, s1, s2));
		Classroom c1 = new Classroom("c1", Arrays.asList(s1, s2, s3));
		Classroom c2 = new Classroom("c2", Arrays.asList(s0, s1, s3));

		// t0->s0, s3
		// t1->s0, s1, s2
		// t2->s1
		// t3->s2
		// t4->s3

		// is it necessary to assign the free period a teachers? yep cause teacher
		// constraints are checked
		c0.assignTeacher(s0, t0);
		c0.assignTeacher(s1, t1);
		c0.assignTeacher(s2, t1);


		c1.assignTeacher(s1, t2);
		c1.assignTeacher(s2, t3);
		c1.assignTeacher(s3, t0);

		c2.assignTeacher(s0, t1);
		c2.assignTeacher(s1, t2);
		c2.assignTeacher(s3, t4);

		// verifyTimeTable the first 100k lines, if mismatch, print the original and mismatched
		// this test is terribly written, fix this ASAP
		CheckUtils.normalize(3, 3, new Classroom[]{c0, c1, c2});
		ConstraintCollection cc = new BackTrackConstraintCollection();


		TimeTablerBacktrackImpl
				tb = null;
		Period[][] tt = null;

		while (tt == null) {
			if (tb != null) tb.stopTimeTabling();
			tb = new TimeTablerBacktrackImpl(3, 3, Utils.genericCopyOf(new Classroom[]{c0, c1, c2}), 3,
											 new LinearSelectionAllocatorStrategy(new PeriodAllocatorImpl()),
											 null, // todo add method to load a teachers timetable
											 cc.getTeacherNoConcurrentPeriodsConstraint(),
											 cc.getSubjectMaxHourConstraint(),
											 cc.getTeacherConsecutiveLabConstraint(),
											 cc.getTeacherNoAllotConstraint());
			tt = tb.nextAllClassTTAs2DArray();
		}


		for (int i = 0; i < 1000; i++) {


			verifyNoConcurrentTeacherConsttraint(tb, tt);
			verifyClassSubjectHourConstraint(tb, tt);
			verifyTeacherConstraint(tb, tt);

			tt = tb.nextAllClassTTAs2DArray(); // go to the next result of  backtracking

		}
		tb.stopTimeTabling();
	}


	@Test
	// @Disabled
	public void timeTableWithLabsMethod() throws IOException, ClassNotFoundException {

		Subject s0 = new Subject("s0", 3);
		Subject s1 = new Subject("s1", 3);
		Subject s2 = new Subject("s2", 2);
		Subject s3 = new Subject("s3", 2);
		Subject lab0 = new Subject("lab0", 4, new AbstractMap.SimpleEntry<>("lab", "1")); //check here...

		//Subject free1 = new Subject("free", 3, false);// represents a single free period
		// Subject free2 = new Subject("free", 4, false);

		Teacher t0 = new Teacher("t0", Integer.MAX_VALUE, Collections.emptyList());
		Teacher t1 = new Teacher("t1", Integer.MAX_VALUE, Collections.emptyList());
		Teacher t2 = new Teacher("t2", Integer.MAX_VALUE, Collections.emptyList());
		Teacher t3 = new Teacher("t3", Integer.MAX_VALUE, Collections.emptyList());
		Teacher t4 = new Teacher("t4", Integer.MAX_VALUE, Collections.emptyList());
		Teacher lt0 = new Teacher("lt0", 12, Collections.emptyList());
		Teacher lt1 = new Teacher("lt1", 12, Collections.emptyList());

		// c0 -> s0, s1, s2
		// c1 -> s1, s2, s3
		// c2 -> s0, s1, s3

        /*Classroom c0 = new Classroom("c0", Arrays.asList(s0, s1, s2, lab0, free1));
        Classroom c1 = new Classroom("c1", Arrays.asList(s1, s2, s3, lab0, free2));
        Classroom c2 = new Classroom("c2", Arrays.asList(s0, s1, s3, lab0, free1));*/

		Classroom c0 = new Classroom("c0", new ArrayList<>(Arrays.asList(s0, s1, s2, lab0)));
		Classroom c1 = new Classroom("c1", new ArrayList<>(Arrays.asList(s1, s2, s3, lab0)));
		Classroom c2 = new Classroom("c2", new ArrayList<>(Arrays.asList(s0, s1, s3, lab0)));

		// t0->s0, s3
		// t1->s0, s1, s2
		// t2->s1
		// t3->s2
		// t4->s3

		// is it necessary to assign the free period a teachers? yep cause teacher
		// constraints are checked
		c0.assignTeacher(s0, t0);
		c0.assignTeacher(s1, t1);
		c0.assignTeacher(s2, t1);
		c0.assignTeacher(lab0, lt0);
		//  c0.assignTeacher(free1, new Teacher("c0 free", 0, Collections.emptyList()));


		c1.assignTeacher(s1, t2);
		c1.assignTeacher(s2, t3);
		c1.assignTeacher(s3, t0);
		c1.assignTeacher(lab0, lt1);// use another teacher cause no solution exists (work it out on paper)
		// c1.assignTeacher(free2, new Teacher("c1 free", 0, Collections.emptyList()));

		c2.assignTeacher(s0, t1);
		c2.assignTeacher(s1, t2);
		c2.assignTeacher(s3, t4);
		c2.assignTeacher(lab0, lt0);

		// c2.assignTeacher(free1, new Teacher("c2 free", 0, Collections.emptyList()));

		CheckUtils.normalize(3, 5, new Classroom[]{c0, c1, c2});
		ConstraintCollection cc = new BackTrackConstraintCollection();


		TimeTablerBacktrackImpl tb = null;
		Period[][] tt = null;

		while (tt == null) {
			if (tb != null) tb.stopTimeTabling();

			tb = new TimeTablerBacktrackImpl(3, 5, Utils.genericCopyOf(new Classroom[]{c0, c1, c2}), 3,
											 new LinearSelectionAllocatorStrategy(new PeriodAllocatorImpl()),
											 null, // todo add method to load a teachers timetable
											 cc.getTeacherNoConcurrentPeriodsConstraint(),
											 cc.getSubjectMaxHourConstraint(),
											 cc.getSubjectNoLabBeginConstraint(new int[]{2}),
											 cc.getTeacherConsecutiveLabConstraint(),
											 cc.getTeacherNoAllotConstraint()
			);
			tt = tb.nextAllClassTTAs2DArray();
		}


		for (int i = 0; i < 1000; i++) {

			verifyNoConcurrentTeacherConsttraint(tb, tt);
			verifyClassSubjectHourConstraint(tb, tt);
			verifyNoLabBeginConstraint(tb, tt, new int[]{2});
			verifyTeacherConstraint(tb, tt);

			tt = tb.nextAllClassTTAs2DArray();
		}
		tb.stopTimeTabling();

	}

	/**
	 Test runs on /tbd-data/sem-3-4-5-ONLY-3.tbd
	 */
	@Test
	void realWorldBackTrackImplTest1() throws Exception {

		File inputFile = new File("/home/mfd/development/idea-workspace/TimeTabler5000/tbd-data/sem-3-4-5-ONLY-3-ver2.tbd");
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream(inputFile));

		Teacher[] teachers = (Teacher[]) ois.readObject();
		Subject[] subjects = (Subject[]) ois.readObject();
		Classroom[] classrooms = (Classroom[]) ois.readObject();
		int[] labConstraints = Arrays.stream((Integer[]) ois.readObject()).mapToInt(i -> i).toArray();
		int daysAWeek = ois.readInt();
		int periodsADays = ois.readInt();


		CheckUtils.normalize(periodsADays, daysAWeek, classrooms);


		TimeTablerBacktrackImpl tb = null;
		Period[][] tt = null;

		// wait for a time table for 4 sseconds, if none exists restart with a new one
		while (tt == null) {
			if (tb != null) tb.stopTimeTabling();
			ConstraintCollection cc = new BackTrackConstraintCollection();
			tb = new TimeTablerBacktrackImpl(periodsADays, daysAWeek, Utils.genericCopyOf(classrooms), 3,
											 new LinearSelectionAllocatorStrategy(new PeriodAllocatorImpl()),
											 null, // todo add method to load a teachers timetable
											 cc.getTeacherNoConcurrentPeriodsConstraint(),
											 cc.getSubjectMaxHourConstraint(),
											 cc.getSubjectNoLabBeginConstraint(labConstraints),
											 cc.getTeacherConsecutiveLabConstraint(),
											 cc.getTeacherNoAllotConstraint(),
											 cc.getClassSpacingConstraint(3, 2),
											 cc.getSubjectConcurrentElectiveConstraint(),
											 cc.getSubjectNoConcurrentLabConstraint(),
											 cc.getTeacherMaxFirstHourConstraint(2),
											 cc.getTeacherMaxAfterLunchHourConstraint(4),
											 cc.getTeacherTheroyLabLimitConstraint(),
											 cc.getTeacherNoContinuousConstraint(),
											 cc.getClassMaxAfterLunch(2),
											 cc.getTeacherMondayLunchMeetingConstraint()


			);

			tt = tb.nextAllClassTTAs2DArray();
		}


		// inserting it temp to excesise the makeTimeTablemethod
		tb.nextTimeTable();

		assertTrue(verifyNoConcurrentTeacherConsttraint(tb, tt));
		assertTrue(verifyTeacherConstraint(tb, tt));
		assertTrue(verifyNoLabBeginConstraint(tb, tt, labConstraints));
		assertTrue(verifyClassSubjectHourConstraint(tb, tt));
		assertTrue(verifyNoConcurrentLabConstraint(tb, tt));
		assertTrue(verifyConcurrentElectiveConstraint(tb, tt));
		assertTrue(verifySpacingConstraint(tb, tt, 3, 2));


		//for (int i = 0; i < 100_000; i++) {
		//assertTrue(Utils.verifyTimeTable(tb, tb.nextAllClassTTAs2DArray()));
		//}
		tb.stopTimeTabling();

	}


	/**
	 Test runs on /tbd-data/sem-3-4-5-ONLY-5.tbd
	 */
	@Test
	void realWorldBackTrackImplTest2() throws Exception {

		File inputFile = new File("/home/mfd/development/idea-workspace/TimeTabler5000/tbd-data/sem-3-4-5-ONLY-5-ver2.tbd");
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream(inputFile));

		Teacher[] teachers = (Teacher[]) ois.readObject();
		Subject[] subjects = (Subject[]) ois.readObject();
		Classroom[] classrooms = (Classroom[]) ois.readObject();
		int[] labConstraints = Arrays.stream((Integer[]) ois.readObject()).mapToInt(i -> i).toArray();
		int daysAWeek = ois.readInt();
		int periodsADays = ois.readInt();


		CheckUtils.normalize(periodsADays, daysAWeek, classrooms);


		TimeTablerBacktrackImpl tb = null;
		Period[][] tt = null;

		// wait for a time table for 4 sseconds, if none exists restart with a new one
		while (tt == null) {
			if (tb != null) tb.stopTimeTabling();
			ConstraintCollection cc = new BackTrackConstraintCollection();
			tb = new TimeTablerBacktrackImpl(periodsADays, daysAWeek, Utils.genericCopyOf(classrooms), 3,
											 new LinearSelectionAllocatorStrategy(new PeriodAllocatorImpl()),
											 null, // todo add method to load a teachers timetable
											 cc.getTeacherNoConcurrentPeriodsConstraint(),
											 cc.getSubjectMaxHourConstraint(),
											 cc.getSubjectNoLabBeginConstraint(labConstraints),
											 cc.getTeacherConsecutiveLabConstraint(),
											 cc.getTeacherNoAllotConstraint(),
											 cc.getClassSpacingConstraint(3, 2),
											 cc.getSubjectConcurrentElectiveConstraint(),
											 cc.getSubjectNoConcurrentLabConstraint(),
											 cc.getTeacherMaxFirstHourConstraint(2),
											 cc.getTeacherMaxAfterLunchHourConstraint(4),
											 cc.getTeacherTheroyLabLimitConstraint(),
											 cc.getTeacherNoContinuousConstraint(),
											 cc.getClassMaxAfterLunch(2),
											 cc.getTeacherMondayLunchMeetingConstraint()



			);

			tt = tb.nextAllClassTTAs2DArray();
		}
		assertTrue(verifyNoConcurrentTeacherConsttraint(tb, tt));
		assertTrue(verifyTeacherConstraint(tb, tt));
		assertTrue(verifyNoLabBeginConstraint(tb, tt, labConstraints));
		assertTrue(verifyClassSubjectHourConstraint(tb, tt));
		assertTrue(verifyNoConcurrentLabConstraint(tb, tt));
		assertTrue(verifyConcurrentElectiveConstraint(tb, tt));
		assertTrue(verifySpacingConstraint(tb, tt, 3, 2));


		//for (int i = 0; i < 100_000; i++) {
		//assertTrue(Utils.verifyTimeTable(tb, tb.nextAllClassTTAs2DArray()));
		//}
		tb.stopTimeTabling();

	}


	/**
	 Test runs on /tbd-data/sem-3-4-5-ONLY-7.tbd
	 */
	@Test
	void realWorldBackTrackImplTest3() throws Exception {

		File inputFile = new File("/home/mfd/development/idea-workspace/TimeTabler5000/tbd-data/sem-3-4-5-ONLY-7-ver2.tbd");
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream(inputFile));

		Teacher[] teachers = (Teacher[]) ois.readObject();
		Subject[] subjects = (Subject[]) ois.readObject();
		Classroom[] classrooms = (Classroom[]) ois.readObject();
		int[] labConstraints = Arrays.stream((Integer[]) ois.readObject()).mapToInt(i -> i).toArray();
		int daysAWeek = ois.readInt();
		int periodsADays = ois.readInt();


		CheckUtils.normalize(periodsADays, daysAWeek, classrooms);


		TimeTablerBacktrackImpl tb = null;
		Period[][] tt = null;

		// wait for a time table for 4 sseconds, if none exists restart with a new one
		while (tt == null) {
			if (tb != null) tb.stopTimeTabling();
			ConstraintCollection cc = new BackTrackConstraintCollection();
			tb = new TimeTablerBacktrackImpl(periodsADays, daysAWeek, Utils.genericCopyOf(classrooms), 3,
											 new LinearSelectionAllocatorStrategy(new PeriodAllocatorImpl()),
											 null, // todo add method to load a teachers timetable
											 cc.getTeacherNoConcurrentPeriodsConstraint(),
											 cc.getSubjectMaxHourConstraint(),
											 cc.getSubjectNoLabBeginConstraint(labConstraints),
											 cc.getTeacherConsecutiveLabConstraint(),
											 cc.getTeacherNoAllotConstraint(),
											 cc.getClassSpacingConstraint(3, 2),
											 cc.getSubjectConcurrentElectiveConstraint(),
											 cc.getSubjectNoConcurrentLabConstraint(),
											 cc.getTeacherMaxFirstHourConstraint(2),
											 cc.getTeacherMaxAfterLunchHourConstraint(4),
											 cc.getTeacherTheroyLabLimitConstraint(),
											// cc.getTeacherNoContinuousConstraint()
											cc.getClassMaxAfterLunch(2),
											cc.getTeacherMondayLunchMeetingConstraint()
			);

			tt = tb.nextAllClassTTAs2DArray();
		}
		assertTrue(verifyNoConcurrentTeacherConsttraint(tb, tt));
		assertTrue(verifyTeacherConstraint(tb, tt));
		assertTrue(verifyNoLabBeginConstraint(tb, tt, labConstraints));
		assertTrue(verifyClassSubjectHourConstraint(tb, tt));
		assertTrue(verifyNoConcurrentLabConstraint(tb, tt));
		assertTrue(verifyConcurrentElectiveConstraint(tb, tt));
		assertTrue(verifySpacingConstraint(tb, tt, 3, 2));


		//for (int i = 0; i < 100_000; i++) {
		//assertTrue(Utils.verifyTimeTable(tb, tb.nextAllClassTTAs2DArray()));
		//}
		tb.stopTimeTabling();

	}


	private boolean verifySpacingConstraint(TimeTablerBacktrackImpl tb,
			Period[][] tt,
			int gap,
			int repeatLimit) {

		// verify the spacing and repetition for each period in its day

		class SubjectStatistics {
			int repeat    = 0;
			int lastindex = -1;

			void reset() {
				repeat = 0;
				lastindex = -1;
			}
		}


		// go down the row
		for (int j = 0; j < tt[0].length; j++) {
			Map<Subject, SubjectStatistics> map = new HashMap<>();

			// go down the class
			for (int i = 0; i < tt.length; i++) {

				// reset at beginning of each day
				if (i % tb.getnPeriodADay() == 0) map.values().stream().forEach(stats -> stats.reset());

				final Subject sub = tt[i][j].getSubject();
				map.putIfAbsent(sub, new SubjectStatistics());
				final SubjectStatistics stats = map.get(sub);


				//verify count

				// handle labs seperately as the same period repeats immediately
				// handle them by moving to the next period which is the next lab period.
				if (sub.getConstraintModifiers().containsKey("lab"))
					i++;


				if (stats.repeat >= repeatLimit)
					return false;
				stats.repeat++;  // increment after test not before so that it allows minimum repeatLimit

				//verify gap
				if (stats.lastindex != -1 && i - stats.lastindex <= gap)
					return false;

				stats.lastindex = i;

			}
		}
		return true;
	}

	private boolean verifyConcurrentElectiveConstraint(TimeTablerBacktrackImpl tb,
			Period[][] tt) {
		for (int i = 0; i < tt.length; i++)
			for (int j = 0; j < tt[0].length; j++) {
				if (tt[i][j].getSubject().getConstraintModifiers().containsKey("elective")) {
					// if a class has an elective subject then make sure every other class
					// with the same subject also has the same period
					Subject seenElective = tt[i][j].getSubject();

					for (int k = 0; k < tb.getnClass(); k++) {
						if (tb.getClassroom(k).getSubjectsToTeach().contains(seenElective) && !tt[i][k].getSubject().equals(seenElective))
							return false;

					}
				}
			}

		return true;

	}

	private boolean verifyNoConcurrentLabConstraint(TimeTablerBacktrackImpl tb,
			Period[][] tt) {
		for (int i = 0; i < tt.length; i++) {
			List<String> seenLabs = new ArrayList<>();
			for (int j = 0; j < tt[i].length; j++) {
				// look for labs along a row (i.e concurrent labs), if they occur twice then fail.

				// only check labs
				if (tt[i][j].getSubject().getConstraintModifiers().containsKey("lab")) {
					final String labname = tt[i][j].getSubject().getConstraintModifiers().get("lab");
					if (seenLabs.contains(labname)) return false;
					else
						seenLabs.add(labname);

				}
			}
		}
		return true;
	}

	private boolean verifyClassSubjectHourConstraint(TimeTablerBacktrackImpl tabler,
			Period[][] tt) {
		Map<Subject, Integer> map = new HashMap<>();
		for (int i = 0; i < tabler.getClassrooms().length; i++) {
			for (int j = 0; j < tt.length; j++) {
				//use a hash map to map subjects to their hours
				Subject s = tt[j][i].getSubject();
				if (map.containsKey(s))
					map.put(s, map.get(s) + 1);
				else
					map.put(s, 1);
			}
			//match the subjects against their expected hours
			for (Subject actual : tabler.getClassrooms()[i].getSubjectsToTeach())
				if (actual.getRequiredHours() != map.get(actual))
					return false;
			map.clear();
		}
		return true;
	}


	private boolean verifyNoLabBeginConstraint(TimeTablerBacktrackImpl tabler,
			Period[][] tt, int[] noLabBegin) {

		Arrays.sort(noLabBegin);
		for (int j = 0; j < tt[0].length; j++)
			for (int i = 0; i < tt.length; i++) {

				if (tt[i][j].getSubject().getConstraintModifiers().containsKey("lab")) {
					int period = i % tabler.getnPeriodADay();

					if (Arrays.binarySearch(noLabBegin, period) >= 0 || !tt[i + 1][j].equals(tt[i][j]))
						return false;
					else
						i++; // don't check next period

				}
			}
		return true;
	}

	private boolean verifyTeacherConstraint(TimeTablerBacktrackImpl tb,
			Period[][] tt) {
		for (int i = 0; i < tt.length; i++)
			for (int j = 0; j < tt[i].length; j++) {
				int finalI = i;
				if (!tt[i][j].getTeachers().stream().allMatch(teacher -> teacher.canTake(finalI)))
					return false;
			}
		return true;
	}

	/* Also verifies TEACHER_LAB_CONSTRAINT */
	boolean verifyNoConcurrentTeacherConsttraint(TimeTablerBacktrackImpl tb,
			Period[][] tt) {


		for (Period[] periods : tt) {
			List<Period> seenTeachers = new ArrayList<>();
			for (Period period : periods) {//check simultaneously running periods for teacher collisions
				if (period.getTeachers().stream().anyMatch(t -> seenTeachers.contains(t)))
					return false;
				seenTeachers.add(period);
			}
		}
		return true;
	}
	/*
	* verifyClassSubjectHourConstraint(tb, tb.nextAllClassTTAs2DArray());
		verifyNoLabBeginConstraint(tb, tb.nextAllClassTTAs2DArray());
		verifiyTeacherLabConstraint(tb, tb.nextAllClassTTAs2DArray());
		*/
}

