package org.mfd.timetable5k.core;

import org.mfd.timetable5k.core.Subject;
import org.mfd.timetable5k.core.Teacher;
import org.mfd.timetable5k.core.Constraint;
import org.mfd.timetable5k.core.tablerimplementations.backtracking.TimeTablerBacktrackImpl;

import java.util.List;

/**
 Methods for allocating and deallocating the period (subject, teacher) to the given slot, no questions asked. The
 callbacks don't check for constraints and instead {@link Constraint} should be used for this purpose. This is to
 be used in conjunction with {@link Constraint} to allow a flexible way to allocate periods which need to meet
 specific constraints other than the default ones.
 */
public interface PeriodAllocator {

	/**
	 Allocates a period to the slot denoted by i,j@param s
	 @param t
	 @param context
	 @param i
	 @param j

	 */
	public void allocate(Subject s, List<Teacher> t, TimeTablerBacktrackImpl context, int i, int j);

	/**
	 Deallocates the period denoted by i,j
	 @param context
	 @param i
	 @param j
	 */
	public  void deallocate(TimeTablerBacktrackImpl context, int i, int j);
}
