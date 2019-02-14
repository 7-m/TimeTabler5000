package org.mfd.timetable5k.core.tablerimplementations.backtracking;

import org.mfd.timetable5k.core.*;

import java.util.List;

/**
 Collection of reusable constraints
 */


public class BackTrackConstraintCollection
		implements ConstraintCollection {

	//Todo: Modfiy TimeTAble to include a method to check and make a period empty instread of doing all that null checking
	//business






	@Override
	public Constraint getClassSpacingConstraint(int gap,
			int repeatLimit) {
		return new SpacingConstraint(gap, repeatLimit);
	}

	/**
	 Constraint for setting repetitions of a period allowed in a day and if so, the minimum gap that should be between the repeating periods.
	 Applies to all Subjects. Adapted for a backtracking allocator
	 */
	public static final class SpacingConstraint
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
			// check above the period

			Period mock = new Period(t, s);

			//determine the first period no. of the day to which i belongs
			int start = (i / timeTabler.getnPeriodADay()) * timeTabler.getnPeriodADay();

			//loop until i, count the no. of occurence of subject s
			int count = 0;
			int lastindex = -1;
			for (; start < i; start++)
				if (timeTabler.getPeriodAt(start, j).equals(mock)) {
					count++;
					lastindex = start;
				}
			// if count equals or exceed repeat limit, no need to continue to check for spacing
			if (count >= repeatLimit)
				return false;

			//its enough to check with the last same subject, as the previous allocation will satisfy the conditions,
			// ones before dont need to be checked which can be shown by induction. Only true for backtracking impl.
			if (lastindex != -1 && i - lastindex <= gap)
				return false;

			return true;
		}
	}


}
