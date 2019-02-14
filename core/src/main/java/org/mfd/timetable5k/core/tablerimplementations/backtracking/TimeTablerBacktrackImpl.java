package org.mfd.timetable5k.core.tablerimplementations.backtracking;

import org.mfd.timetable5k.core.*;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class TimeTablerBacktrackImpl
		extends TimeTabler {
	Period[][] timeTable;
	private Thread                   timetablerThread;
	private Allocator                al;
	private BlockingQueue<TimeTable> timeTableResultQ = new ArrayBlockingQueue<>(10);
	private long                     timeout;

	public TimeTablerBacktrackImpl(int nPeriodADay,
			int noDaysAWeek,
			Classroom[] classrooms,
			int timeout,
			Allocator al,
			Map<Teacher, Map<Integer, Teacher.AllotedPeriod>> teacherAllotedMap,
			Constraint... constraints) {
		super(nPeriodADay, noDaysAWeek, classrooms, teacherAllotedMap, constraints);
		this.al = al;
		al.init(this);
		this.timeout = timeout;
		timeTable = new Period[getnPeriods()][getnClass()];
		initTimeTablerThread();
	}

	private void initTimeTablerThread() {
		timetablerThread = new Thread(() -> {
			try {
				makeTimeTable(0, 0);
			} catch (InterruptedException e) {
				//do some loggin over here
				System.out.println(timetablerThread.getName() + " stopped");
			}
		}, getClass() + " Thread");
		timetablerThread.start();
	}

	private void makeTimeTable(int i, int j) throws InterruptedException {

		/* termintating conditions */
		if (Thread.interrupted())
			throw new InterruptedException();


		if (j == getnClass()) {
			j = 0;
			i++;
		}

		if (i == getnPeriods()) {
			timeTableResultQ
					.put(new TimeTable(getnDays(), getnPeriodADay(), makeTimeTableCopy(), getClassrooms(), makeTeacherAllotedMap()));
			return;
		}


		if (timeTable[i][j] != null) {// if a multiple allocation was done as in the case of electives or lab

			makeTimeTable(i, j + 1); // move to the next slot
			return;// go back to previous class
		}
		/* back track logic */

		/*
		- try allocation using allocation interface, if true, go to i,j+1, or continue backtracking
		 */
		while (al.hasCandidateFor(i, j)) {
			if (al.allocate(i, j))
				makeTimeTable(i, j + 1);
				// IMPORTANT : if there are ZERO CANDIDATES  THEN BACKTRACK
			else
				return;


			al.deallocate(i, j);
		}

	}

	@Override
	public void stopTimeTabling() {
		timetablerThread.interrupt();

	}

	@Override
	public TimeTable nextTimeTable() {

		try {
			return timeTableResultQ.poll(timeout, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		throw new RuntimeException("Interrupted while waiting for result from TimeTablerReferenceImpl Thread.");
	}


	private Map<Teacher, Map<Integer, Teacher.AllotedPeriod>> makeTeacherAllotedMap() {
		return Arrays.stream(getTeachers()).collect(Collectors.toMap(t -> t, t -> t.getAllocatedPeriodsMap()));
	}

	protected Period[][] makeTimeTableCopy() {
		Period[][] copy = new Period[getnPeriods()][getnClass()];
		for (int i = 0; i < getnPeriods(); i++)
			copy[i] = Arrays.copyOf(timeTable[i], getnClass());

		return copy;
	}

	@Override
	protected Period[][] nextAllClassTTAs2DArray() {

		try {
			final TimeTable tt = timeTableResultQ.poll(timeout, TimeUnit.SECONDS);
			return tt == null ? null : tt.get2DTimetable();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		throw new RuntimeException("Interrupted while waiting for result from TimeTablerReferenceImpl Thread.");
	}

	@Override
	public Period getPeriodAt(int i, int j) {
		return timeTable[i][j];
	}

	@Override
	public void assignPeriod(Period p, int i, int j) {
		timeTable[i][j] = p;
	}


}
